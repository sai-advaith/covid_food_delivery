package shield;

/** Stores all query strings to be used with the Scottish government server's API. */
public enum QueryStrings {
  /** Register shielding individual request to the server */
  REGISTER_SHIELDING_INDIVIDUAL("/registerShieldingIndividual?CHI=%s"),

  /** Request to edit the order in the server */
  EDIT_ORDER("/editOrder?order_id=%d"),

  /** Request the status of the order from the server */
  ORDER_STATUS("/requestStatus?order_id=%d"),

  /** Request to get all the caterers available in the server */
  GET_CATERERS("/getCaterers"),

  /** Get distance between two postcodes using the server */
  GET_DISTANCE("/distance?postcode1=%s&postcode2=%s"),

  /** Show all the food boxes available in the catering company from the server */
  SHOW_FOOD_BOX("/showFoodBox?orderOption=catering"),

  /** Place an order with catering company using the server */
  PLACE_ORDER("/placeOrder?individual_id=%s&catering_business_name=%s&catering_postcode=%s"),

  /** Register catering company on the server */
  REGISTER_CATERING_COMPANY("/registerCateringCompany?business_name=%s&postcode=%s"),

  /** Update the status of an order in catering company on the server */
  UPDATE_CATERING_ORDER_STATUS("/updateOrderStatus?order_id=%d&newStatus=%s"),

  /** Register supermarket on the server */
  REGISTER_SUPERMARKET("/registerSupermarket?business_name=%s&postcode=%s"),

  /** Update the status of a supermarket order on the server */
  UPDATE_SUPERMARKET_STATUS("/updateSupermarketOrderStatus?order_id=%s&newStatus=%s"),

  /** Record a supermarket order on the server */
  RECORD_SUPERMARKET_ORDER(
      "/recordSupermarketOrder?individual_id=%s&order_number=%d&"
          + "supermarket_business_name=%s&supermarket_postcode=%s"),

  /** Request to cancel the order on the server */
  CANCEL_ORDER("/cancelOrder?order_id=%d");

  /** Constant to store the request sent to the server */
  private final String request;

  /**
   * This is a parameterized constructor for the API request enum
   *
   * @param request is a description of what each API request constitutes
   */
  QueryStrings(String request) {
    this.request = request;
  }

  /**
   * Overrides the default toString() method of QueryStrings to return the raw query string.
   *
   * @return the request to the server as a format string
   */
  @Override
  public String toString() {
    return request;
  }
}
