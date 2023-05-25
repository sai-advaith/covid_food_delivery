package shield;

import java.io.IOException;
import java.util.Objects;

/**
 * Example of a potential application for a catering company.
 *
 * <p>This is the model part of the application.
 */
public class CateringCompanyModelImp implements CateringCompanyModel {
  /** The endpoint used for remote communication. */
  private final String endpoint;
  /** The name of the catering company. */
  private String name;
  /** The post code of the catering company. */
  private String postCode;
  /** Tells us whether the catering company is registered. */
  private boolean registered;

  /**
   * Initialises a catering company model.
   *
   * @param endpoint the endpoint used for remote communication with a server
   */
  public CateringCompanyModelImp(String endpoint) {
    this.endpoint = endpoint;
    this.registered = false;
  }

  @Override
  public boolean register(String name, String postCode) {
    assert Objects.nonNull(name) : "Model should not receive null name for registration.";
    assert Objects.nonNull(postCode) : "Model should not receive null post code for registration.";

    String request = QueryStringFormatter.cateringCompanyRegisterRequest(name, postCode);
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
  public boolean updateOrderStatus(int orderNumber, OrderStatus status) {
    assert isValidOrderNumber(orderNumber) : "Model should not receive invalid order number.";

    String request;
    request = QueryStringFormatter.updateCateringOrderRequest(orderNumber, status.toString());
    try {
      String response = ClientIO.doGETRequest(endpoint + request);
      return response.equals(ServerResponse.ORDER_STATUS_UPDATE_SUCCESS.toString());
    } catch (IOException | RuntimeException e) {
      System.err.println("ERROR: HTTP get request failed.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean isValidOrderNumber(int orderNumber) {
    return orderNumber >= 0;
  }

  @Override
  public boolean isRegistered() {
    return registered;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPostCode() {
    return postCode;
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
