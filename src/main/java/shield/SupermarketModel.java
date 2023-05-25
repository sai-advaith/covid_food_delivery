package shield;

public interface SupermarketModel {

  /**
   * Registers the supermarket.
   *
   * <p>This method takes the name and post code of the supermarket and registers the supermarket.
   * If the registration has been successful, then it returns true. Otherwise false.
   *
   * @param name the name of the supermarket
   * @param postCode the post code of the catering company
   * @return true if the registration has been successful. Otherwise false
   */
  boolean register(String name, String postCode);

  /**
   * Notifies the Scottish government's system about a new supermarket order.
   *
   * <p>It returns true if the order has been successfully recorded by the system based on a
   * response from the server. Otherwise false.
   *
   * @param CHI is the CHI number used to record the supermarket order
   * @param orderNumber is the order number to be recorded
   * @return true if the order has been successfully recorded
   */
  boolean recordOrder(String CHI, int orderNumber);

  /**
   * Updates the status of a particular order number.
   *
   * <p>It returns true if the order status has been successfully registered by the supermarket.
   * This indication of a successful update is done by the system. Based on the response from the
   * system, we can decide if the update has been successful or not.
   *
   * @param orderNumber the order number of the status to be updated
   * @param status the status to which the order will be updated
   * @return true if the status has been updated successfully, false otherwise
   */
  boolean updateOrder(int orderNumber, OrderStatus status);

  /**
   * Checks if supermarket has been registered
   *
   * @return true if registration has been complete
   */
  boolean isRegistered();

  /**
   * Gets the post code of the catering company
   *
   * @return the post code of the catering company
   */
  String getPostCode();

  /**
   * Gets the name of the supermarket
   *
   * @return the name of the supermarket
   */
  String getName();

  /**
   * Sets the registration status of the supermarket
   *
   * @param registered if the supermarket has been registered
   */
  void setRegistered(boolean registered);

  /**
   * Sets the post code of the supermarket
   *
   * @param postCode of the supermarket
   */
  void setPostCode(String postCode);

  /**
   * Sets the name of the supermarket
   *
   * @param name of the supermarket
   */
  void setName(String name);
}
