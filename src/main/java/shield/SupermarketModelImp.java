package shield;

import java.io.IOException;
import java.util.Objects;

/** Model implementation of the supermarket. */
public class SupermarketModelImp implements SupermarketModel {
  /** The endpoint used for remote communication. */
  private final String endpoint;
  /** The name of the supermarket. */
  private String name;
  /** The post code of the supermarket. */
  private String postCode;
  /** Tells us whether the supermarket is registered. */
  private boolean registered;

  /**
   * Initialises a supermarket model.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public SupermarketModelImp(String endpoint) {
    this.endpoint = endpoint;
    this.registered = false;
  }

  @Override
  public boolean register(String name, String postCode) {
    assert Objects.nonNull(name) : "Supermarket model should not receive null name.";
    assert Objects.nonNull(postCode) : "Supermarket model should not receive null post code.";

    String request = QueryStringFormatter.supermarketRegisterRequest(name, postCode);
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      if (response.equals(ServerResponse.ALREADY_REGISTERED.toString())) {
        return true;
      }
      if (response.equals(ServerResponse.REGISTRATION_SUCCESS.toString())) {
        this.registered = true;
        this.name = name;
        this.postCode = postCode;
        return true;
      }
    } catch (IOException | RuntimeException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean recordOrder(String CHI, int orderNumber) {
    assert Objects.nonNull(CHI) : "The model should not receive null CHI.";
    assert isValidOrderNumber(orderNumber) : "The model should not receive invalid order number.";

    String request;
    request = QueryStringFormatter.recordSupermarketOrderRequest(CHI, orderNumber, name, postCode);
    boolean success = false;
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      success = response.equals(ServerResponse.ORDER_RECORD_SUCCESS.toString());
    } catch (IOException | RuntimeException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  private boolean isValidOrderNumber(int orderNumber) {
    return orderNumber >= 0;
  }

  @Override
  public boolean updateOrder(int orderNumber, OrderStatus status) {
    assert isValidOrderNumber(orderNumber) : "The model should not receive invalid order number.";

    String request =
        QueryStringFormatter.updateSupermarketOrderRequest(orderNumber, status.toString());
    boolean success = false;
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      success = response.equals(ServerResponse.ORDER_STATUS_UPDATE_SUCCESS.toString());
    } catch (IOException | RuntimeException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  @Override
  public boolean isRegistered() {
    return registered;
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
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public void setRegistered(boolean registered) {
    this.registered = registered;
  }
}
