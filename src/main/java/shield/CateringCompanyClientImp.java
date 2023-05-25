package shield;

import java.util.Objects;

/**
 * Example of a potential application for a catering company.
 *
 * <p>This is the controller part of the application.
 */
public class CateringCompanyClientImp implements CateringCompanyClient {
  /** Object used to call the catering company model. */
  private final CateringCompanyModel model;

  /**
   * Initialises a catering company client.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public CateringCompanyClientImp(String endpoint) {
    model = new CateringCompanyModelImp(endpoint);
  }

  /**
   * Registers the catering company.
   *
   * <p>Return true if the catering company is already registered Return false if {@code name} or
   * {@code postCode} is null or if post code is invalid Otherwise, {@link
   * CateringCompanyModel#register} is called.
   *
   * @param name the name of the catering company
   * @param postCode the post code of the catering company
   * @return true if the catering company was registered successfully, false otherwise
   */
  @Override
  public boolean registerCateringCompany(String name, String postCode) {
    if (isRegistered()) {
      return true;
    }
    if (Objects.isNull(name) || !isValidPostCode(postCode)) {
      return false;
    }
    return model.register(name, postCode);
  }

  /**
   * This method validates a post code in Edinburgh based on particular format.
   *
   * @param postCode the post code of the catering company to be validated
   * @return ture if the post code is valid otherwise false
   */
  private boolean isValidPostCode(String postCode) {
    if (Objects.isNull(postCode)) {
      return false;
    }
    String postCodeRegex = "^[E][H][1-9][0-7]?[_][1-9][A-Z][A-Z]$";
    return postCode.matches(postCodeRegex);
  }

  /**
   * Update order status
   *
   * <p>Return false if the catering company is not registered or if {@code status} is null If
   * neither of the above, then {@link CateringCompanyModel#updateOrderStatus} is called
   *
   * @param orderNumber the order number
   * @param status the updated status for the particular order
   * @return true if the status of the order was updated successfully, false otherwise
   */
  @Override
  public boolean updateOrderStatus(int orderNumber, String status) {
    if (!isRegistered() || Objects.isNull(status) || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    OrderStatus parsedStatus = OrderStatus.parseStatus(status);
    if (Objects.isNull(parsedStatus)) {
      return false;
    }
    return model.updateOrderStatus(orderNumber, parsedStatus);
  }

  // Check if the order number is valid
  private boolean isValidOrderNumber(int orderNumber) {
    return orderNumber >= 0;
  }

  /**
   * Check if the catering company is registered by calling {@link
   * CateringCompanyModel#isRegistered)}
   *
   * @return true if the catering company is registered, false otherwise
   */
  @Override
  public boolean isRegistered() {
    return model.isRegistered();
  }

  /**
   * Get the name of the catering company Return the name of the catering company by calling {@link
   * CateringCompanyModel#getName)}
   *
   * @return the name of the catering company if the catering company is registered, null otherwise
   */
  @Override
  public String getName() {
    return model.isRegistered() ? model.getName() : null;
  }

  /**
   * Get the post code of the catering company Return the post code of the catering company by
   * calling {@link CateringCompanyModel#getPostCode)}
   *
   * @return the post code of the catering company
   */
  @Override
  public String getPostCode() {
    return model.isRegistered() ? model.getPostCode() : null;
  }

  /**
   * Set the registration status of the catering company by calling {@link
   * CateringCompanyModel#setRegistered}
   *
   * @param registrationStatus if the catering company has been successfully registered
   */
  public void setRegistered(boolean registrationStatus) {
    model.setRegistered(registrationStatus);
  }

  /**
   * Set the post code of the catering company by calling {@link CateringCompanyModel#setPostCode}
   *
   * @param postCode post code of the catering company to be set
   */
  public void setPostCode(String postCode) {
    model.setPostCode(postCode);
  }

  /**
   * Set the name of the catering company by calling {@link CateringCompanyModel#setName}
   *
   * @param name name of the catering company to be set
   */
  public void setName(String name) {
    model.setName(name);
  }
}
