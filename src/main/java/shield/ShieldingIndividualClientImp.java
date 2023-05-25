package shield;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Example of a potential application for a shielding individual.
 *
 * <p>This is the controller part of the application. It performs basic input validation and
 * delegates and calls appropriate methods in the associated model.
 */
public class ShieldingIndividualClientImp implements ShieldingIndividualClient {
  /** Represents an invalid distance */
  private static final float INVALID_DISTANCE = -1f;
  /** Represents no food boxes */
  private static final int NO_FOOD_BOX = -1;
  /** Represents an invalid number of items */
  private static final int INVALID_NUMBER_OF_ITEMS = -1;

  private final ShieldingIndividualModel model;

  /**
   * Initialises a shielding individual client.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public ShieldingIndividualClientImp(String endpoint) {
    model = new ShieldingIndividualModelImp(endpoint);
  }

  /**
   * Initiates registration of a shielding individual.
   *
   * <p>If the shielding individual is already registered, the method returns true immediately
   * without updating their CHI number. If the CHI is null or does not follow the <a
   * href="https://datadictionary.nhs.uk/attributes/community_health_index_number.html">format
   * specified by the NHS</a>, the method returns false. Otherwise, the method {@link
   * ShieldingIndividualModel#register(String)} is called.
   *
   * @param chi the CHI number of the shielding individual
   * @return true if registration succeeded, false otherwise
   */
  @Override
  public boolean registerShieldingIndividual(String chi) {
    if (isRegistered()) {
      return true;
    }
    if (!isValidChi(chi)) {
      return false;
    }
    return model.register(chi);
  }

  /**
   * Checks if the given CHI number follows the <a
   * href="https://datadictionary.nhs.uk/attributes/community_health_index_number.html">format
   * specified by the * NHS </a>
   *
   * @param chi the CHI number to be validated
   * @return true if the CHI number is valid.
   */
  private boolean isValidChi(String chi) {
    if (Objects.isNull(chi)) {
      return false;
    }
    String chiRegex = "^[0-9]{10}$";
    if (!chi.matches(chiRegex)) {
      return false;
    }

    String rawBirthDate = chi.substring(0, 6);
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("ddMMyy");
    formatter.setLenient(false); // Prevents lenience in edge cases such as "999999"

    boolean success = false;
    try {
      Date birthDate = formatter.parse(rawBirthDate);
      Date today = new Date();
      // No need to treat the edge case where birthDate and today are equal, since Date contains a
      // timestamp and today is initialised after birthDate
      success = birthDate.before(today);
    } catch (ParseException e) {
      System.err.println("ERROR: Parsing birthdate in CHI failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return success;
  }

  // --------------- METHODS RELATED TO ORDERS ---------------

  /**
   * Initiates placing of a new order.
   *
   * <p>The method returns false if the shielding individual is not registered. Otherwise, it
   * triggers the method {@link ShieldingIndividualModel#placeOrder}.
   *
   * @return true if the order was placed successfully, false otherwise
   */
  @Override
  public boolean placeOrder() {
    LocalDateTime timeOrdered = LocalDateTime.now();
    if (!isRegistered()) {
      return false;
    }
    return model.placeOrder(timeOrdered);
  }

  /**
   * Initiates editing an order.
   *
   * <p>The method returns false if the shielding individual is not registered or the order number
   * is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#editOrder}, which tries to accommodate the changes made using {@link
   * #setItemQuantityForOrder}.
   *
   * @param orderNumber the number of the order for which the edits are to be made
   * @return true if the order was placed successfully, false otherwise
   */
  @Override
  public boolean editOrder(int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    return model.editOrder(orderNumber);
  }

  private boolean isValidOrderNumber(int orderNumber) {
    return orderNumber >= 0;
  }

  /**
   * Initiates order cancellation.
   *
   * <p>The method returns false if the shielding individual is not registered or the order number
   * is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#cancelOrder}.
   *
   * @param orderNumber the number of the order to be cancelled
   * @return true if the order was cancelled successfully, false otherwise
   */
  @Override
  public boolean cancelOrder(int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    return model.cancelOrder(orderNumber);
  }

  /**
   * Initiates fetching of order status.
   *
   * <p>The method returns false if the shielding individual is not registered or the order number
   * is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#requestOrderStatus}.
   *
   * @param orderNumber the number of the order for which status is to be fetched
   * @return true if the status was fetched successfully, false otherwise
   */
  @Override
  public boolean requestOrderStatus(int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    return model.requestOrderStatus(orderNumber);
  }

  // --------------- ACCESSOR METHODS FOR ORDERS ---------------

  /**
   * Initiates retrieval of order numbers.
   *
   * <p>The method returns null if the shielding individual is not registered. Otherwise, it
   * triggers the method {@link ShieldingIndividualModel#getOrderNumbers}.
   *
   * @return a list of order numbers
   */
  @Override
  public List<Integer> getOrderNumbers() {
    if (!isRegistered()) {
      return null;
    }
    return model.getOrderNumbers();
  }

  /**
   * Initiates the retrieval of order status as last cached by the system.
   *
   * <p>The method returns null if the shielding individual is not registered or the order number is
   * invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#getStatusForOrder}.
   *
   * @param orderNumber the number of the order for which status is to be retrieved
   * @return the status of the order
   */
  @Override
  public String getStatusForOrder(int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return null;
    }
    return model.getStatusForOrder(orderNumber);
  }

  /**
   * Initiates retrieval of item IDs corresponding to an order.
   *
   * <p>The method returns null if the shielding individual is not registered or the order number is
   * invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#getItemIdsForOrder}.
   *
   * @param orderNumber the number of the order for which we wish to retrieve item IDs
   * @return a list of item IDs
   */
  @Override
  public List<Integer> getItemIdsForOrder(int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return null;
    }
    return model.getItemIdsForOrder(orderNumber);
  }

  /**
   * Initiates retrieval of item name corresponding to an order.
   *
   * <p>The method returns null if the shielding individual is not registered or the order number or
   * if the item ID is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#getItemNameForOrder}.
   *
   * @param itemId the ID of the item whose name we wish to get
   * @param orderNumber the number of the order whose items we are interested in
   * @return the name of the item
   */
  @Override
  public String getItemNameForOrder(int itemId, int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber) || !isValidItemId(itemId)) {
      return null;
    }
    return model.getItemNameForOrder(itemId, orderNumber);
  }

  // Checks that the item ID is valid
  private boolean isValidItemId(int itemId) {
    return itemId >= 0;
  }

  /**
   * Initiates retrieval of item quantity corresponding to an order.
   *
   * <p>The method returns -1 if the shielding individual is not registered or the order number or
   * if the item ID is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#getItemQuantityForOrder}.
   *
   * @param itemId the ID of the item whose quantity we wish to get
   * @param orderNumber the number of the order whose items we are interested in
   * @return the quantity of the item
   */
  @Override
  public int getItemQuantityForOrder(int itemId, int orderNumber) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber) || !isValidItemId(itemId)) {
      return -1;
    }
    return model.getItemQuantityForOrder(itemId, orderNumber);
  }

  /**
   * Initiates setting of item quantity corresponding to an order.
   *
   * <p>The method returns false if the shielding individual is not registered or the order number
   * or if the item ID is invalid, i.e. less than 0. Otherwise, it triggers the method {@link
   * ShieldingIndividualModel#setItemQuantityForOrder}.
   *
   * @param itemId the ID of the item whose quantity we wish to set
   * @param orderNumber the number of the order whose items we are interested in
   * @param quantity the new quantity
   * @return true if the item quantity was updated successfully, false otherwise
   */
  @Override
  public boolean setItemQuantityForOrder(int itemId, int orderNumber, int quantity) {
    if (!isRegistered()
        || !isValidOrderNumber(orderNumber)
        || !isValidItemId(itemId)
        || !isValidItemQuantity(quantity)) {
      return false;
    }
    return model.setItemQuantityForOrder(itemId, orderNumber, quantity);
  }

  // Checks if the item quantity is valid
  private boolean isValidItemQuantity(int quantity) {
    return quantity >= 0;
  }

  // --------------- METHODS RELATED TO CATERING COMPANIES ---------------

  /**
   * Initiates retrieval of all catering companies.
   *
   * <p>The method returns null if the shielding individual is not registered. Otherwise, it
   * triggers the method {@link ShieldingIndividualModel#getCateringCompanies}.
   *
   * @return a list of catering companies
   */
  @Override
  public List<String> getCateringCompanies() {
    if (!isRegistered()) {
      return null;
    }
    return model.getCateringCompanies();
  }

  /**
   * Initiates calculation of distance between two locations.
   *
   * <p>The method returns -1.0 if the user is not registered or if any of the post codes is
   * invalid. Otherwise, it triggers the method {@link ShieldingIndividualModel#getDistance}.
   *
   * @param postCode1 post code of one location
   * @param postCode2 post code of another location
   * @return the distance between the two locations in metres
   */
  @Override
  public float getDistance(String postCode1, String postCode2) {
    if (!isRegistered() || !isValidPostCode(postCode1) || !isValidPostCode(postCode2)) {
      return INVALID_DISTANCE;
    }
    return model.getDistance(postCode1, postCode2);
  }

  // Verifies the validity of a post code.
  private boolean isValidPostCode(String postCode) {
    if (Objects.isNull(postCode)) {
      return false;
    }
    String postCodeRegex = "^[E][H][1-9][0-7]?[_][1-9][A-Z][A-Z]$";
    return postCode.matches(postCodeRegex);
  }

  /**
   * Initiates retrieval of the closest catering company.
   *
   * <p>The methods returns null if the shielding individual is not registered. Otherwise, the
   * method {@link ShieldingIndividualModel#getClosestCateringCompany} is triggered.
   *
   * @return the closest catering company
   */
  @Override
  public String getClosestCateringCompany() {
    if (!isRegistered()) {
      return null;
    }
    return model.getClosestCateringCompany();
  }

  // --------------- ACCESSOR METHODS ---------------

  /**
   * Initiates retrieval of the shielding individual's registration status.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#isRegistered}.
   *
   * @return true if the individual is registered, false otherwise.
   */
  @Override
  public boolean isRegistered() {
    return model.isRegistered();
  }

  /**
   * Initiates retrieval of the shielding individual's CHI.
   *
   * <p>The method returns null if the shielding individual is not registered. Otherwise, the method
   * {@link ShieldingIndividualModel#getChi} is triggered.
   *
   * @return the shielding individual's CHI number
   */
  @Override
  public String getChi() {
    return model.getChi();
  }

  /**
   * Initiates retrieval of the shielding individual's post code.
   *
   * <p>The method returns null if the shielding individual is not registered. Otherwise, the method
   * {@link ShieldingIndividualModel#getPostCode} is triggered.
   *
   * @return the shielding individual's post code
   */
  public String getPostCode() {
    return model.getPostCode();
  }

  // ---------------- METHODS RELATED TO FOOD BOXES ----------------

  /**
   * Initiates retrieval of food box IDs for a given dietary preference.
   *
   * <p>The method returns null if the shielding individual is not registered or if {@code
   * dietaryPreference} does not match any preference in {@link DietaryPreference}. Otherwise, the
   * method {@link ShieldingIndividualModel#getFoodBoxIds} is triggered.
   *
   * @param dietaryPreference the dietary preference of the shielding individual (e.g. pollotarian,
   *     vegan, ...)
   * @return a list of stringified food box IDs
   */
  @Override
  public Collection<String> showFoodBoxes(String dietaryPreference) {
    if (!isRegistered()) {
      return null;
    }
    DietaryPreference parsedPreference = DietaryPreference.parsePreference(dietaryPreference);
    if (Objects.isNull(parsedPreference)) {
      return null;
    }
    return model.getFoodBoxIds(parsedPreference);
  }

  /**
   * Initiates retrieval of the total number of food boxes.
   *
   * <p>The method returns -1 if the shielding individual is not registered. Otherwise, the method
   * {@link ShieldingIndividualModel#getFoodBoxNumber} is triggered.
   *
   * @return the number of food boxes
   */
  @Override
  public int getFoodBoxNumber() {
    if (!isRegistered()) {
      return NO_FOOD_BOX;
    }
    return model.getFoodBoxNumber();
  }

  /**
   * Initiates retrieval of the dietary preference for a food box.
   *
   * <p>The method returns null if the shielding individual is not registered or if the food box ID
   * is invalid, i.e. less than 0. Otherwise, the method {@link
   * ShieldingIndividualModel#getDietaryPreferenceForFoodBox} is triggered.
   *
   * @param foodBoxId the ID of the food box whose dietary preference we are interested in
   * @return the dietary preference for the food box
   */
  @Override
  public String getDietaryPreferenceForFoodBox(int foodBoxId) {
    if (!isRegistered() || !isValidFoodBoxId(foodBoxId)) {
      return null;
    }
    return model.getDietaryPreferenceForFoodBox(foodBoxId);
  }

  /**
   * Initiates retrieval of the item IDs for a food box.
   *
   * <p>The method returns null if the shielding individual is not registered or if the food box ID
   * is invalid. Otherwise, the method {@link ShieldingIndividualModel#getItemIdsForOrder} is
   * triggered.
   *
   * @param foodBoxId the ID of the food box whose item IDs we are interested in
   * @return a list of IDs
   */
  @Override
  public List<Integer> getItemIdsForFoodBox(int foodBoxId) {
    if (!isRegistered() || !isValidFoodBoxId(foodBoxId)) {
      return null;
    }
    return model.getItemIdsForFoodBox(foodBoxId);
  }

  /**
   * Initiates retrieval of the number of items for a food box.
   *
   * <p>The method returns -1 if the shielding individual is not registered or if the food box ID is
   * invalid. Otherwise, the method {@link ShieldingIndividualModel#getItemsNumberForFoodBox} is
   * triggered.
   *
   * @param foodBoxId the ID of the food box for which the number of items should be determined
   * @return the number of distinct items in the food box
   */
  @Override
  public int getItemsNumberForFoodBox(int foodBoxId) {
    if (!isRegistered() || !isValidFoodBoxId(foodBoxId)) {
      return INVALID_NUMBER_OF_ITEMS;
    }
    return model.getItemsNumberForFoodBox(foodBoxId);
  }

  /**
   * Initiates retrieval of item name for a food box.
   *
   * <p>The method returns null if the shielding individual is not registered or if the food box ID
   * or item ID is invalid. Otherwise, the method {@link
   * ShieldingIndividualModel#getItemNameForFoodBox} is triggered.
   *
   * @param itemId the ID of the item whose name we wish to determine
   * @param foodBoxId the ID of the food box whose item we are interested in
   * @return the name of the food box item
   */
  @Override
  public String getItemNameForFoodBox(int itemId, int foodBoxId) {
    if (!isRegistered() || !isValidFoodBoxId(foodBoxId) || !isValidItemId(itemId)) {
      return null;
    }
    return model.getItemNameForFoodBox(itemId, foodBoxId);
  }

  /**
   * Initiates retrieval of item quantity for a food box.
   *
   * <p>The method returns -1 if the shielding individual is not registered or if the food box ID or
   * item ID is invalid. Otherwise, the method {@link
   * ShieldingIndividualModel#getItemQuantityForFoodBox} is triggered.
   *
   * @param itemId the ID of the item whose quantity we wish to determine
   * @param foodBoxId the ID of the food box whose item we are interested in
   * @return the quantity of the food box item
   */
  @Override
  public int getItemQuantityForFoodBox(int itemId, int foodBoxId) {
    if (!isRegistered() || !isValidFoodBoxId(foodBoxId) || !isValidItemId(itemId)) {
      return -1;
    }
    return model.getItemQuantityForFoodBox(itemId, foodBoxId);
  }

  private boolean isValidFoodBoxId(int foodBoxId) {
    return foodBoxId >= 0;
  }

  // ---------------- METHODS RELATED TO PICKING A FOOD BOX ----------------

  /**
   * Initiates picking of a food box.
   *
   * <p>The method returns false if the shielding individual is not registered. Otherwise, the
   * method {@link ShieldingIndividualModel#pickFoodBox} is triggered.
   *
   * @param foodBoxId the ID of the food box to be picked
   * @return true if the food box was picked successfully, false otherwise
   */
  @Override
  public boolean pickFoodBox(int foodBoxId) {
    // Note: We do not check for invalid food box ID here, since if it is invalid, the model resets
    // the pickedFoodBox
    if (!isRegistered()) {
      return false;
    }
    return model.pickFoodBox(foodBoxId);
  }

  /**
   * Initiates changing item quantity for the picked food box.
   *
   * <p>The method returns false if the shielding individual is not registered or if the item ID is
   * invalid. Otherwise, the method {@link
   * ShieldingIndividualModel#changeItemQuantityForPickedFoodBox} is triggered.
   *
   * @param itemId the ID of the item whose quantity we wish to change
   * @param quantity the new quantity
   * @return true if the quantity was updated successfully, false otherwise
   */
  @Override
  public boolean changeItemQuantityForPickedFoodBox(int itemId, int quantity) {
    if (!isRegistered() || !isValidItemId(itemId) || !isValidItemQuantity(quantity)) {
      return false;
    }
    return model.changeItemQuantityForPickedFoodBox(itemId, quantity);
  }

  // --------------- METHODS ADDED FOR TESTING PURPOSES ---------------

  /**
   * Initiates setting of shielding individual registration status.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setRegistered}
   *
   * @param registered the new registration status
   */
  public void setRegistered(boolean registered) {
    model.setRegistered(registered);
  }

  /**
   * Initiates setting of shielding individual CHI number.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setChi}
   *
   * @param chi the new CHI number
   */
  public void setChi(String chi) {
    model.setChi(chi);
  }

  /**
   * Initiates setting of shielding individual post code.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setPostCode}
   *
   * @param postCode the new post code
   */
  public void setPostCode(String postCode) {
    model.setPostCode(postCode);
  }

  /**
   * Initiates setting of shielding individual name.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setName}
   *
   * @param name the new name
   */
  public void setName(String name) {
    model.setName(name);
  }

  /**
   * Initiates setting of shielding individual surname.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setSurname}
   *
   * @param surname the new surname
   */
  public void setSurname(String surname) {
    model.setSurname(surname);
  }

  /**
   * Initiates setting of shielding individual phone number.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setPhoneNumber}
   *
   * @param phoneNumber the new phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    model.setPhoneNumber(phoneNumber);
  }

  /**
   * Initiates setting of shielding individual picked food box.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#setPickedFoodBox}
   *
   * @param pickedFoodBox the new food box
   */
  public void setPickedFoodBox(FoodBox pickedFoodBox) {
    model.setPickedFoodBox(pickedFoodBox);
  }

  /**
   * Initiates moving back the date time the most recent order was ordered.
   *
   * <p>Triggers the method {@link
   * ShieldingIndividualModel#setMostRecentOrderTimeOrderedBeforeDays}.
   *
   * @param numDays the number of days by which to move the most recent order date time
   */
  public void setMostRecentOrderTimeOrderedBeforeDays(long numDays) {
    model.setMostRecentOrderTimeOrderedBeforeDays(numDays);
  }

  /**
   * Initiates adding a new order to the collection of orders placed by a shielding individual.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#addOrder}.
   *
   * @param order the order to be added
   */
  public void addOrder(CateringCompanyOrder order) {
    model.addOrder(order);
  }

  /**
   * Initiates retrieval of picked food box.
   *
   * <p>Triggers the method {@link ShieldingIndividualModel#getPickedFoodBox}.
   *
   * @return the picked food box
   */
  public FoodBox getPickedFoodBox() {
    return model.getPickedFoodBox();
  }
}
