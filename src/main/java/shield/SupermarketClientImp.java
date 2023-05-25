package shield;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/** Example of a potential application for a supermarket. */
public class SupermarketClientImp implements SupermarketClient {
  /** Object used to call the supermarket model */
  private final SupermarketModel model;

  /**
   * Initialises a supermarket client.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public SupermarketClientImp(String endpoint) {
    model = new SupermarketModelImp(endpoint);
  }

  /**
   * Registers the supermarket.
   *
   * <p>Return true if the supermarket is already registered Return false if {@code name} or {@code
   * postCode} is null or if post code is invalid Otherwise it calls {@link
   * SupermarketModel#register(String, String)}
   *
   * @param name the name of the supermarket
   * @param postCode the post code of the supermarket
   * @return true if the supermarket was registered successfully, false otherwise
   */
  @Override
  public boolean registerSupermarket(String name, String postCode) {
    if (isRegistered()) {
      return true;
    }
    if (Objects.isNull(name) || !isValidPostCode(postCode)) {
      return false;
    }

    return model.register(name, postCode);
  }

  /**
   * Used to validate a particular post code to be in the Edinburgh format
   *
   * @param postCode post code to be validated
   * @return true if the post code is valid
   */
  private boolean isValidPostCode(String postCode) {
    if (Objects.isNull(postCode)) {
      return false;
    }
    String postCodeRegex = "^[E][H][1-9][0-7]?[_][1-9][A-Z][A-Z]$";
    return postCode.matches(postCodeRegex);
  }

  /**
   * Notifies the Scottish government's system about a new supermarket order.
   *
   * <p>It returns false if the supermarket is not registered, CHI number does not follow the <a
   * href="https://datadictionary.nhs.uk/attributes/community_health_index_number.html">format
   * specified by the * NHS </a>, if the order number is invalid, or if any error occurs. Otherwise
   * it calls the {@link SupermarketModel#recordOrder(String, int)}
   *
   * @param chi the CHI of the shielding individual associated with this order
   * @param orderNumber the number of the order that should be recorded
   * @return true if the order was recorded successfully, false otherwise
   */
  @Override
  public boolean recordSupermarketOrder(String chi, int orderNumber) {
    if (!isRegistered() || !isValidChi(chi) || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    return model.recordOrder(chi, orderNumber);
  }

  // Checks if an order number is valid
  private boolean isValidOrderNumber(int orderNumber) {
    return orderNumber >= 0;
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

  /**
   * Updates the status for an order with a matching number
   *
   * <p>It returns false if the supermarket is not registered, if {@code status} is not in {@link
   * OrderStatus}, or if any error occurs. Otherwise it calls {@link
   * SupermarketModel#updateOrder(int, OrderStatus)}
   *
   * @param orderNumber the number of the order
   * @param status the updated status for the particular order to be updated
   * @return true if the status of the order was updated successfully, false otherwise
   */
  @Override
  public boolean updateOrderStatus(int orderNumber, String status) {
    if (!isRegistered() || !isValidOrderNumber(orderNumber)) {
      return false;
    }
    OrderStatus parsedStatus = OrderStatus.parseStatus(status);
    if (Objects.isNull(parsedStatus)) {
      return false;
    }
    return model.updateOrder(orderNumber, parsedStatus);
  }

  /**
   * Check if the supermarket is registered by calling {@link SupermarketModel#isRegistered()}
   *
   * @return true if the supermarket is registered, false otherwise
   */
  @Override
  public boolean isRegistered() {
    return model.isRegistered();
  }

  /**
   * Get the name of the supermarket by calling {@link SupermarketModel#getName)} Return the name of
   * the supermarket
   *
   * @return the name of the supermarket if the supermarket is registered, null otherwise
   */
  @Override
  public String getName() {
    return isRegistered() ? model.getName() : null;
  }

  /**
   * Get the post code of the supermarket by calling {@link SupermarketModel#getPostCode)} Return
   * the post code of the supermarket
   *
   * @return the post code of the supermarket if the supermarket is registered, null otherwise
   */
  @Override
  public String getPostCode() {
    return isRegistered() ? model.getPostCode() : null;
  }

  /**
   * Set the registration status of the supermarket by calling {@link
   * SupermarketModel#setRegistered(boolean)}
   *
   * @param registrationStatus if the supermarket has been successfully registered
   */
  public void setRegistered(boolean registrationStatus) {
    model.setRegistered(registrationStatus);
  }

  /**
   * Set the post code of the supermarket by calling {@link SupermarketModel#setPostCode}
   *
   * @param postCode post code of the supermarket to be set
   */
  public void setPostCode(String postCode) {
    model.setPostCode(postCode);
  }

  /**
   * Set the name of the supermarket by calling {@link SupermarketModel#setName}
   *
   * @param name name of the supermarket to be set
   */
  public void setName(String name) {
    model.setName(name);
  }
}
