package shield;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Example of a potential application for a shielding individual.
 *
 * <p>This is the model part of the application. It is responsible for storing data and
 * communicating with the Scottish government's system.
 */
public class ShieldingIndividualModelImp implements ShieldingIndividualModel {
  /** An invalid distance */
  private static final float INVALID_DISTANCE = -1f;
  /** Delimiter used to separate the details of the output from the server */
  private static final char CATERING_COMPANY_INFO_DELIMITER = ',';
  /** The minimum required time between orders i.e. one week (in seconds) */
  private static final int MIN_TIME_BETWEEN_ORDERS = 7 * 24 * 60 * 60;
  /** Invalid number of items */
  private static final int INVALID_NUMBER_OF_ITEMS = -1;
  /** Invalid item quantity */
  private static final int INVALID_ITEM_QUANTITY = -1;

  private final String endpoint;
  private final HashMap<Integer, CateringCompanyOrder> orders;
  private List<FoodBox> cachedFoodBoxes;
  private FoodBox pickedFoodBox;
  private CateringCompanyOrder mostRecentOrder;
  private String chi;
  private String postCode;
  private String name;
  private String surname;
  private String phoneNumber;
  private boolean registered;

  /**
   * Initialises a shielding individual model.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public ShieldingIndividualModelImp(String endpoint) {
    this.endpoint = endpoint;
    orders = new HashMap<>();
    mostRecentOrder = null;
  }

  @Override
  public boolean register(String chi) {
    assert Objects.nonNull(chi) : "CHI number passed cannot be null.";

    String request = QueryStringFormatter.individualRegisterRequest(chi);
    boolean success = false;
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      if (!response.equals(ServerResponse.ALREADY_REGISTERED.toString())
          && !response.equals(ServerResponse.NO_CHI.toString())) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> details = new Gson().fromJson(response, type);
        success = Objects.nonNull(details) && details.size() == 4 && !details.contains(null);
        if (success) {
          this.postCode = details.get(0).replace(' ', '_');
          this.name = details.get(1);
          this.surname = details.get(2);
          this.phoneNumber = details.get(3);
          this.chi = chi;
          this.registered = true;
        }
      }
    } catch (JsonSyntaxException e) {
      System.err.println("ERROR: JSON syntax invalid.");
      e.printStackTrace();
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  // --------------- METHODS RELATED TO ORDERS ---------------

  @Override
  public boolean placeOrder(LocalDateTime timeOrdered) {
    assert Objects.nonNull(timeOrdered) : "Time at which order has been placed cannot be null.";

    if (Objects.isNull(pickedFoodBox)) {
      return false;
    }
    if (!canPlaceOrder(timeOrdered)) {
      return false;
    }
    boolean success = false;
    try {
      // Find closest catering company
      String company = getClosestCateringCompany();
      if (Objects.isNull(company)) {
        return false;
      }
      int firstIndex = company.indexOf(CATERING_COMPANY_INFO_DELIMITER);
      int lastIndex = company.lastIndexOf(CATERING_COMPANY_INFO_DELIMITER);
      String companyName = company.substring(firstIndex + 1, lastIndex);
      String companyPostCode = company.substring(lastIndex + 1);

      // Place order
      String request = QueryStringFormatter.placeOrderRequest(chi, companyName, companyPostCode);
      String response = ClientIO.doPOSTRequest(endpoint + request, pickedFoodBox.jsonify());
      if (!response.equals(ServerResponse.ORDER_PLACE_FAILURE.toString())) {
        int orderNumber = Integer.parseInt(response);
        if (orderNumber < 0) {
          return false;
        }
        CateringCompanyOrder newOrder;
        newOrder = new CateringCompanyOrder(orderNumber, pickedFoodBox, timeOrdered);
        success = addOrder(newOrder);
        if (success) {
          resetFoodBoxChoice();
        }
      }
    } catch (StringIndexOutOfBoundsException e) {
      System.err.println("ERROR: Improper format in company name.");
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.err.println("ERROR: Response has inappropriate format.");
      e.printStackTrace();
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  private boolean canPlaceOrder(LocalDateTime requestedOrderTime) {
    assert Objects.nonNull(requestedOrderTime) : "Requested order time cannot be null.";

    if (Objects.isNull(mostRecentOrder)) {
      return true;
    }
    LocalDateTime lastTimeOrdered = mostRecentOrder.getTimeOrdered();
    Duration timeSinceLastOrder = Duration.between(lastTimeOrdered, requestedOrderTime);
    return lastTimeOrdered.isBefore(requestedOrderTime)
        && (timeSinceLastOrder.getSeconds() >= MIN_TIME_BETWEEN_ORDERS);
  }

  @Override
  public void setMostRecentOrderTimeOrderedBeforeDays(long numDays) {
    LocalDateTime timeOrdered = mostRecentOrder.getTimeOrdered();
    mostRecentOrder.setTimeOrdered(timeOrdered.minusDays(numDays));
  }

  @Override
  public boolean editOrder(int orderNumber) {
    if (!placedByThisIndividual(orderNumber)) {
      return false;
    }
    boolean success = false;
    try {
      FoodBox box = findFoodBoxForOrder(orderNumber);
      if (Objects.isNull(box)) {
        return false;
      }
      String orderData = box.jsonify();
      if (Objects.isNull(orderData)) {
        return false;
      }
      String request = QueryStringFormatter.editOrderRequest(orderNumber);
      // The server checks that the order has not been packed and that the user is not trying
      // to increase the number of contents
      String response = ClientIO.doPOSTRequest(endpoint + request, orderData);
      success = response.equals(ServerResponse.ORDER_EDIT_SUCCESS.toString());
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  // Returns whether an order was placed by this shielding individual
  private boolean placedByThisIndividual(int orderNumber) {
    return orders.containsKey(orderNumber);
  }

  // Finds the corresponding food box given an order number
  private FoodBox findFoodBoxForOrder(int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return null;
    }
    return matchingOrder.getFoodBox();
  }

  @Override
  public boolean cancelOrder(int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    String request = QueryStringFormatter.cancelOrderRequest(orderNumber);
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      if (response.equals(ServerResponse.ORDER_CANCEL_SUCCESS.toString())) {
        matchingOrder.setStatus(OrderStatus.CANCELLED);
        return true;
      }
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean requestOrderStatus(int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return false;
    }
    OrderStatus status = null;
    try {
      String request = QueryStringFormatter.orderStatusRequest(orderNumber);
      String response = ClientIO.doGETRequest(endpoint + request);
      switch (response) {
        case "0":
          status = OrderStatus.PLACED;
          break;
        case "1":
          status = OrderStatus.PACKED;
          break;
        case "2":
          status = OrderStatus.DISPATCHED;
          break;
        case "3":
          status = OrderStatus.DELIVERED;
          break;
        case "4":
          status = OrderStatus.CANCELLED;
          break;
        default:
          status = null;
      }
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (Objects.isNull(status)) {
      return false;
    }
    matchingOrder.setStatus(status);
    return true;
  }

  // --------------- ACCESSOR METHODS FOR ORDERS ---------------

  @Override
  public List<Integer> getOrderNumbers() {
    assert !orders.containsKey(null) : "No order number can be null!";
    assert !orders.containsValue(null) : "No Order Number can point to a null order object!";

    return orders.values().stream()
        .map(CateringCompanyOrder::getNumber)
        .collect(Collectors.toList());
  }

  @Override
  public String getStatusForOrder(int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return null;
    }
    return matchingOrder.getStatus().toString();
  }

  /** Finds the order corresponding to {@code orderNumber} */
  private CateringCompanyOrder findOrder(int orderNumber) {
    return orders.get(orderNumber);
  }

  @Override
  public List<Integer> getItemIdsForOrder(int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return null;
    }
    return matchingOrder.getItemIds();
  }

  @Override
  public String getItemNameForOrder(int itemId, int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return null;
    }
    return matchingOrder.getItemName(itemId);
  }

  @Override
  public int getItemQuantityForOrder(int itemId, int orderNumber) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);
    if (Objects.isNull(matchingOrder)) {
      return INVALID_ITEM_QUANTITY;
    }
    return matchingOrder.getItemQuantity(itemId);
  }

  @Override
  public boolean setItemQuantityForOrder(int itemId, int orderNumber, int quantity) {
    CateringCompanyOrder matchingOrder = findOrder(orderNumber);

    FoodBox foodBox = findFoodBoxForOrder(orderNumber);
    if (Objects.isNull(foodBox)
        || Objects.isNull(matchingOrder)
        || !OrderStatus.PLACED.toString().equals(getStatusForOrder(orderNumber))) {
      return false;
    }
    return foodBox.setQuantityForItem(itemId, quantity, true);
  }

  // --------------- METHODS RELATED TO CATERING COMPANIES ---------------

  @Override
  public List<String> getCateringCompanies() {
    String request = QueryStrings.GET_CATERERS.toString();
    List<String> caterers = null;
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      Type type = new TypeToken<List<String>>() {}.getType();
      caterers = new Gson().fromJson(response, type);
      caterers.removeIf(Objects::isNull);
      caterers.removeIf(String::isEmpty);
    } catch (JsonSyntaxException e) {
      System.err.println("ERROR: JSON syntax invalid.");
      e.printStackTrace();
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return caterers;
  }

  @Override
  public Float getDistance(String postCode1, String postCode2) {
    assert Objects.nonNull(postCode1) && Objects.nonNull(postCode2) : "Post codes cannot be null";

    float distance = INVALID_DISTANCE;
    try {
      String request = QueryStringFormatter.distanceRequest(postCode1, postCode2);
      String response = ClientIO.doGETRequest(endpoint + request);
      distance = Float.parseFloat(response);
    } catch (NumberFormatException e) {
      System.err.println("ERROR: Cannot be parsed to float.");
      e.printStackTrace();
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return distance;
  }

  @Override
  public String getClosestCateringCompany() {
    List<String> caterers = getCateringCompanies();
    if (Objects.isNull(caterers)) {
      return null;
    }
    int minIndex = -1;
    float minDistance = Float.POSITIVE_INFINITY;

    // Find closest catering company
    try {
      for (int i = 0; i < caterers.size(); i++) {
        String company = caterers.get(i);

        int index = company.lastIndexOf(CATERING_COMPANY_INFO_DELIMITER);
        String companyPostCode = company.substring(index + 1);
        float distance = getDistance(this.postCode, companyPostCode);

        if (distance >= 0 && distance < minDistance) {
          minDistance = distance;
          minIndex = i;
        }
      }
      return caterers.get(minIndex);
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      System.err.println("ERROR: Catering company details are invalid");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // --------------- ACCESSOR METHODS RELATED TO SHIELDING INDIVIDUAL ---------------

  @Override
  public boolean isRegistered() {
    return registered;
  }

  @Override
  public String getChi() {
    return chi;
  }

  @Override
  public String getPostCode() {
    return postCode;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSurname() {
    return surname;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  // ---------------- METHODS RELATED TO FOOD BOXES ----------------

  // Caches food boxes
  private void cacheFoodBoxes() {
    try {
      String response = ClientIO.doGETRequest(endpoint + QueryStrings.SHOW_FOOD_BOX.toString());
      Type listType = new TypeToken<List<FoodBox>>() {}.getType();
      cachedFoodBoxes = new Gson().fromJson(response, listType);
    } catch (JsonSyntaxException e) {
      System.err.println("ERROR: Invalid JSON format");
      e.printStackTrace();
    } catch (RuntimeException | IOException e) {
      System.err.println("ERROR: Invalid HTTP get request to the server");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getFoodBoxNumber() {
    List<FoodBox> allFoodBoxes = getAllFoodBoxes();
    if (Objects.isNull(allFoodBoxes)) {
      return -1;
    }
    return allFoodBoxes.size();
  }

  // Returns the list of all food boxes on the server
  private List<FoodBox> getAllFoodBoxes() {
    if (Objects.isNull(cachedFoodBoxes)) {
      cacheFoodBoxes();
    }
    return cachedFoodBoxes;
  }

  @Override
  public Collection<String> getFoodBoxIds(DietaryPreference preference) {
    List<FoodBox> allFoodBoxes = getAllFoodBoxes();
    if (Objects.isNull(allFoodBoxes)) {
      return null;
    }
    Stream<FoodBox> foodBoxStream;
    if (preference == DietaryPreference.NO_PREFERENCE) {
      foodBoxStream = allFoodBoxes.stream();
    } else {
      foodBoxStream =
          allFoodBoxes.stream().filter(box -> box.getDiet().equals(preference.toString()));
    }
    return foodBoxStream.map(FoodBox::getId).collect(Collectors.toList());
  }

  @Override
  public String getDietaryPreferenceForFoodBox(int foodBoxId) {
    assert foodBoxId >= 0 : "Invalid food box ID";

    List<FoodBox> allFoodBoxes = getAllFoodBoxes();
    if (Objects.isNull(allFoodBoxes)) {
      return null;
    }
    String dietaryPreference = null;
    String stringifiedId = String.valueOf(foodBoxId);
    for (FoodBox box : allFoodBoxes) {
      if (Objects.nonNull(box)
          && Objects.nonNull(box.getId())
          && stringifiedId.equals(box.getId())) {
        dietaryPreference = box.getDiet();
      }
    }
    return dietaryPreference;
  }

  @Override
  public List<Integer> getItemIdsForFoodBox(int foodBoxId) {
    assert foodBoxId >= 0 : "Invalid food box ID";

    FoodBox box = findMatchingFoodBox(foodBoxId);
    if (Objects.isNull(box) || Objects.isNull(box.getContents())) {
      return null;
    }
    return box.getContents().stream().map(FoodBoxItem::getId).collect(Collectors.toList());
  }

  @Override
  public int getItemsNumberForFoodBox(int foodBoxId) {
    assert foodBoxId >= 0 : "Invalid food box ID";

    FoodBox box = findMatchingFoodBox(foodBoxId);
    if (Objects.isNull(box) || Objects.isNull(box.getContents())) {
      return INVALID_NUMBER_OF_ITEMS;
    }
    return box.getContents().size();
  }

  @Override
  public String getItemNameForFoodBox(int itemId, int foodBoxId) {
    assert foodBoxId >= 0 : "Invalid food box ID";
    assert itemId >= 0 : "Invalid item ID";

    FoodBox box = findMatchingFoodBox(foodBoxId);
    if (Objects.isNull(box)) {
      return null;
    }
    return box.getItemName(itemId);
  }

  @Override
  public int getItemQuantityForFoodBox(int itemId, int foodBoxId) {
    assert foodBoxId >= 0 : "Invalid food box ID";
    assert itemId >= 0 : "Invalid item ID";

    FoodBox box = findMatchingFoodBox(foodBoxId);
    if (Objects.isNull(box)) {
      return INVALID_ITEM_QUANTITY;
    }
    return box.getItemQuantity(itemId);
  }

  // Finds a food box with a matching ID
  private FoodBox findMatchingFoodBox(int foodBoxId) {
    List<FoodBox> allFoodBoxes = getAllFoodBoxes();
    if (Objects.isNull(allFoodBoxes)) {
      return null;
    }
    String stringifiedId = String.valueOf(foodBoxId);
    for (FoodBox box : allFoodBoxes) {
      if (Objects.nonNull(box) && stringifiedId.equals(box.getId())) {
        return box;
      }
    }
    return null;
  }

  // ---------------- METHODS RELATED TO PICKING A FOOD BOX ----------------

  @Override
  public boolean pickFoodBox(int foodBoxId) {
    try {
      FoodBox matchingFoodBox = findMatchingFoodBox(foodBoxId);
      if (Objects.nonNull(matchingFoodBox)) {
        pickedFoodBox = matchingFoodBox.copy();
        return true;
      }
      resetFoodBoxChoice();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  // Resets food box choice
  private void resetFoodBoxChoice() {
    pickedFoodBox = null;
  }

  @Override
  public boolean changeItemQuantityForPickedFoodBox(int itemId, int quantity) {
    assert quantity >= 0 : "Invalid item quantity";
    assert itemId >= 0 : "Invalid item ID";

    if (Objects.isNull(pickedFoodBox)) {
      return false;
    }
    return pickedFoodBox.setQuantityForItem(itemId, quantity, false);
  }

  // --------------- METHODS ADDED FOR TESTING PURPOSES ---------------

  public boolean addOrder(CateringCompanyOrder order) {
    assert Objects.nonNull(order) : "Order being added cannot be null";

    orders.put(order.getNumber(), order);
    mostRecentOrder = order;
    return true;
  }

  @Override
  public void setRegistered(boolean registrationStatus) {
    this.registered = registrationStatus;
  }

  @Override
  public void setChi(String chi) {
    this.chi = chi;
  }

  @Override
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setSurname(String surname) {
    this.surname = surname;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public void setPickedFoodBox(FoodBox pickedFoodBox) {
    this.pickedFoodBox = pickedFoodBox;
  }

  @Override
  public FoodBox getPickedFoodBox() {
    return registered ? pickedFoodBox : null;
  }
}
