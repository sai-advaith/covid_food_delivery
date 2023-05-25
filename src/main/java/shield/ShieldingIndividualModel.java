package shield;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/** Interface for the model of a client application for a shielding individual. */
public interface ShieldingIndividualModel {

  /**
   * Registers the shielding individual.
   *
   * <p>Checks the CHI number with PHS to see if the shielding individual is classified as a
   * shielding individual. If so, the personal details corresponding to the CHI are retrieved from
   * PHS. The method returns false if any data retrieved from the Scottish government's server is
   * null, or if any error occurs.
   *
   * @param chi the Community Health Index number of the shielding individual
   * @return true if registration succeeded, false otherwise
   */
  boolean register(String chi);

  /**
   * Places an order. This method uses the food box picked using {@link #pickFoodBox}. It returns
   * false if the shielding individual is not registered, has not picked a food box or has placed an
   * order within the last 7 days, or if any error occurs.
   *
   * @param timeOrdered the exact time at which the order was placed
   * @return true if the order was placed successfully, false otherwise
   */
  boolean placeOrder(LocalDateTime timeOrdered);

  /**
   * Moves back the date time the most recent order was ordered by {@code numDays} days.
   *
   * @param numDays the number of days by which to move the most recent order date time
   */
  void setMostRecentOrderTimeOrderedBeforeDays(long numDays);

  /**
   * Edits an order. This method propagates to the Scottish government's server any changes to an
   * order accumulated by the client. It returns false if the shielding individual is not
   * registered, if the corresponding order was not placed by this individual, or if any error
   * occurs.
   *
   * @param orderNumber the number of the order to be edited
   * @return true if the changes were propagated successfully, false otherwise
   */
  boolean editOrder(int orderNumber);

  /**
   * Cancels an order.
   *
   * <p>The method returns false if the order has already been dispatched or cancelled, if the order
   * was not placed by this individual or if any error occurs.
   *
   * @param orderNumber the number of the order to be cancelled
   * @return true if the order was cancelled successfully, false otherwise
   */
  boolean cancelOrder(int orderNumber);

  /**
   * Retrieves order status from the Scottish government's server.
   *
   * <p>This method fetches the up-to-date status of the order from the Scottish government's server
   * and caches it locally. It returns false if the order number was not placed by this individual
   * or if any error occurs.
   *
   * @param orderNumber the order number whose status should be requested
   * @return true if the request is successful, false otherwise
   */
  boolean requestOrderStatus(int orderNumber);

  /**
   * Retrieves order numbers.
   *
   * <p>This method returns all the order numbers corresponding to a shielding individual.
   *
   * @return the list of order numbers
   */
  List<Integer> getOrderNumbers();

  /**
   * Returns the status of the order given by {@code orderNumber}.
   *
   * <p>The method returns null if an order with this order number cannot be found.
   *
   * @param orderNumber the order number for which we need to return the status
   * @return status of the order number
   */
  String getStatusForOrder(int orderNumber);

  /**
   * Returns all the item IDs corresponding to {@code orderNumber}.
   *
   * <p>The method returns null if the order number does not correspond to the most recent order
   * placed by the shielding individual.
   *
   * @param orderNumber the number of the order for which we wish to retrieve item IDs
   * @return a list of item IDs
   */
  List<Integer> getItemIdsForOrder(int orderNumber);

  /**
   * Returns the item name for a particular order number and item ID.
   *
   * <p>The method returns null if a matching order or item cannot be found.
   *
   * @param itemId the ID of the item whose name we wish to get
   * @param orderNumber the number of the order whose items we are interested in
   * @return the name of the item
   */
  String getItemNameForOrder(int itemId, int orderNumber);

  /**
   * Returns the item quantity for a particular order number and item ID.
   *
   * <p>The method returns null if a matching order or item cannot be found.
   *
   * @param itemId the ID of the item whose quantity we wish to get
   * @param orderNumber the number of the order whose items we are interested in
   * @return the quantity of the item
   */
  int getItemQuantityForOrder(int itemId, int orderNumber);

  /**
   * Sets the quantity for a particular item in an order.
   *
   * <p>The method returns null if a matching order or item cannot be found. It also returns false
   * if updating the item quantity would increase item quantity, or if it would result in a total
   * item quantity of 0 for the particular order.
   *
   * @param itemId the item for which we need to set the quantity
   * @param orderNumber the food box order number we need to refer when setting the quantity of the
   *     item
   * @param quantity the quantity which will be set for the particular item in the order
   * @return true if the item quantity was updated successfully, false otherwise
   */
  boolean setItemQuantityForOrder(int itemId, int orderNumber, int quantity);

  /**
   * Returns a list of catering companies and their locations.
   *
   * <p>Each entry is a comma-delimited string of the format "&lt;catering company
   * ID&gt;,&lt;catering company name&gt;, &lt;catering company post code&gt;". The method returns
   * null if any error occurs.
   *
   * @return a list of catering companies in the specified format
   */
  List<String> getCateringCompanies();

  /**
   * Computes the distance between two locations.
   *
   * <p>The two locations are represented by their respective post codes. The method returns -1.0,
   * one or both post codes is null or if any error occurs.
   *
   * @param postCode1 post code of one location
   * @param postCode2 post code of another location
   * @return the distance between the two post codes in metres
   */
  Float getDistance(String postCode1, String postCode2);

  /**
   * Returns the closest catering company.
   *
   * <p>The output format mirrors that of the list entries returned by {@link
   * #getCateringCompanies}. The method returns null if there are no catering companies, or if any
   * error occurs.
   *
   * @return a catering company in the specified format
   */
  String getClosestCateringCompany();

  /**
   * Tells whether the shielding individual is registered.
   *
   * @return true if the individual is registered, false otherwise.
   */
  boolean isRegistered();

  /**
   * Returns the registered shielding individual's CHI (Community Health Index) number.
   *
   * @return the shielding individual's CHI number
   */
  String getChi();

  /**
   * Returns the registered shielding individual's post code.
   *
   * @return the shielding individual's post code
   */
  String getPostCode();

  /**
   * Returns the registered shielding individual's name.
   *
   * @return the shielding individual's name
   */
  String getName();

  /**
   * Returns the registered shielding individual's surname.
   *
   * @return the shielding individual's surname
   */
  String getSurname();

  /**
   * Returns the registered shielding individual's phone number.
   *
   * @return the shielding individual's phone number
   */
  String getPhoneNumber();

  /**
   * Returns the total number of food boxes.
   *
   * <p>The method returns -1 if the food boxes cannot be retrieved from the server or if any error
   * occurs.
   *
   * @return the number of food boxes
   */
  int getFoodBoxNumber();

  /**
   * Returns the IDs of the food boxes that satisfy the desired dietary preference.
   *
   * <p>If no dietary preference is passed, the method returns all food box IDs. The method returns
   * null if any error occurs.
   *
   * @param preference the dietary preference of the shielding individual (e.g. pollotarian, vegan,
   *     ...)
   * @return a list of stringified food box IDs
   */
  Collection<String> getFoodBoxIds(DietaryPreference preference);

  /**
   * Returns the dietary preference for a particular food box.
   *
   * <p>The food box is identified using its ID. This method returns null if the user is not
   * registered or if any error occurs.
   *
   * @param foodBoxId the ID of the food box whose dietary preference we are interested in
   * @return the dietary preference for the food box
   */
  String getDietaryPreferenceForFoodBox(int foodBoxId);

  /**
   * Returns the IDs of all items in a particular food box.
   *
   * <p>The method returns null if a food box with a matching ID cannot be found, or if any error
   * occurs.
   *
   * @param foodBoxId the ID of the food box whose item IDs we are interested in
   * @return a list of IDs
   */
  List<Integer> getItemIdsForFoodBox(int foodBoxId);

  /**
   * Returns the number of distinct items in a particular food box.
   *
   * <p>The method returns -1 a food box with a matching ID cannot be found, or if any error occurs.
   *
   * @param foodBoxId the ID of the food box for which the number of items should be determined
   * @return the number of distinct items in the food box
   */
  int getItemsNumberForFoodBox(int foodBoxId);

  /**
   * Returns the name of an item for a particular food box.
   *
   * <p>The method returns null if the requested food box or food box item cannot be found, or if
   * any error occurs.
   *
   * @param itemId the ID of the item whose name we wish to determine
   * @param foodBoxId the ID of the food box whose item we are interested in
   * @return the name of the food box item
   */
  String getItemNameForFoodBox(int itemId, int foodBoxId);

  /**
   * Returns the quantity of an item for a particular food box.
   *
   * <p>The method returns -1 if the requested food box or food box item cannot be found, or if any
   * error occurs.
   *
   * @param itemId the ID of the item whose quantity we wish to determine
   * @param foodBoxId the ID of the food box whose item we are interested in
   * @return the quantity of the food box item
   */
  int getItemQuantityForFoodBox(int itemId, int foodBoxId);

  /**
   * Picks a particular food box.
   *
   * <p>The method returns false if the particular food box cannot be found, or if any error occurs.
   *
   * <p>Note: This method resets the the picked food box (i.e. sets it to null) if a food box with a
   * matching ID is not found.
   *
   * @param foodBoxId the ID of the food box to be picked
   * @return true if the food box was picked successfully, false otherwise
   */
  boolean pickFoodBox(int foodBoxId);

  /**
   * Changes the quantity of the picked food box item.
   *
   * <p>This method returns false if the shielding individual has not picked a food box, or if any
   * error occurs.
   *
   * @param itemId the ID of the item whose quantity we wish to change
   * @param quantity the new quantity
   * @return true if the quantity was updated successfully, false otherwise
   */
  boolean changeItemQuantityForPickedFoodBox(int itemId, int quantity);

  // --------------- METHODS ADDED FOR TESTING PURPOSES ---------------

  /**
   * Adds an order to the collection of orders corresponding to a shielding individual.
   *
   * @param order the order to be added
   * @return true if the order was added successfully
   */
  boolean addOrder(CateringCompanyOrder order);

  /**
   * Sets registration status.
   *
   * @param registrationStatus the new registration status.
   */
  void setRegistered(boolean registrationStatus);

  /**
   * Sets the CHI number.
   *
   * @param chi the new CHI number
   */
  void setChi(String chi);

  /**
   * Sets the post code.
   *
   * @param postCode the new post code
   */
  void setPostCode(String postCode);

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  void setName(String name);

  /**
   * Sets the surname.
   *
   * @param surname the new surname
   */
  void setSurname(String surname);

  /**
   * Sets the phone number.
   *
   * @param phoneNumber the new phone number
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Sets the picked food box.
   *
   * @param pickedFoodBox the new food box
   */
  void setPickedFoodBox(FoodBox pickedFoodBox);

  /**
   * Returns the picked food box
   *
   * <p>The method returns null if the shielding individual is not registered.
   *
   * @return a food box
   */
  FoodBox getPickedFoodBox();
}
