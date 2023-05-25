package shield;

/** Stores all possible responses from the server. */
public enum ServerResponse {
  /** Response from server when individual/company/supermarket has already been registered. */
  ALREADY_REGISTERED("already registered"),

  /** Response from server when no CHI number has been provided. */
  NO_CHI("must specify CHI"),

  /** Response from server when order has been successfully cancelled. */
  ORDER_CANCEL_SUCCESS("True"),

  /** Response from server when order has been successfully edited. */
  ORDER_EDIT_SUCCESS("True"),

  /** Response from server when order has been successfully placed. */
  ORDER_PLACE_FAILURE(
      "must provide individual_id and catering_id. The "
          + "individual and the catering must be registered before placing an order"),

  /** Response from server when order has been successfully recorded. */
  ORDER_RECORD_SUCCESS("True"),

  /** Response from server when order status has been successfully updated. */
  ORDER_STATUS_UPDATE_SUCCESS("True"),

  /**
   * Response from the server when catering company and supermarket has been successfully
   * registered.
   */
  REGISTRATION_SUCCESS("registered new");

  /** String to store possible responses from a server as a constant. */
  private final String response;

  /**
   * Parameterized constructor to store the response we get using the reference to the class
   *
   * @param response store the possible response from a server
   */
  ServerResponse(String response) {
    this.response = response;
  }

  /**
   * Overrides the default toString() method of ServerResponse to return the response from a server
   *
   * @return the description of a response from a server in required format
   */
  @Override
  public String toString() {
    return response;
  }
}
