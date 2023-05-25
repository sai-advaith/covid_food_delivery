package shield;

public interface CateringCompanyModel {
  /**
   * Registers the catering company.
   *
   * <p>The function takes name of the catering company and post code of the catering company. After
   * this the catering company will be registered. If successfully registered, the method returns
   * true and updates class variables associated with the method. Otherwise it returns false.
   *
   * @param name the name of the catering company
   * @param postCode the post code of the catering company
   * @return if the catering company has been successfully registered by the system
   */
  boolean register(String name, String postCode);

  /**
   * Updates the order status.
   *
   * <p>This function takes the order number and the status to which the order has to be updated. If
   * the order has been successfully updated by the server, then true is returned. Otherwise, false.
   *
   * @param orderNumber is the order number whose status has to be changed
   * @param status is the status to which the order has to be updated
   * @return true if the order status has been successfully registered
   */
  boolean updateOrderStatus(int orderNumber, OrderStatus status);

  /**
   * Check if catering company has been registered
   *
   * @return true if registration has been complete
   */
  boolean isRegistered();

  /**
   * Get the name of the catering company
   *
   * @return the name of the catering company
   */
  String getName();

  /**
   * Get the post code of the catering company
   *
   * @return the post code of the catering company
   */
  String getPostCode();

  /**
   * Setting the name of the catering company
   *
   * @param name of the catering company
   */
  void setName(String name);

  /**
   * Setting the post code of the catering company
   *
   * @param postCode of the catering company
   */
  void setPostCode(String postCode);

  /**
   * Setting the registration status of the catering company
   *
   * @param registered if the catering company has been registered
   */
  void setRegistered(boolean registered);
}
