package shield;

/** Stores all possible order statuses. */
public enum OrderStatus {

  /** Order status for indicating that an order has been placed. */
  PLACED("placed"),

  /** Order status for indicating that an order has been packed. */
  PACKED("packed"),

  /** Order status for indicating that an order has been dispatched. */
  DISPATCHED("dispatched"),

  /** Order status for indicating that an order has been delivered. */
  DELIVERED("delivered"),

  /** Order status for indicating that an order has been cancelled. */
  CANCELLED("cancelled");

  /** Description of the order status in the required format. */
  private final String orderDescription;

  /**
   * This is a parameterized constructor for the order status enum
   *
   * @param orderDescription is a description of what each enum constitutes
   */
  OrderStatus(String orderDescription) {
    this.orderDescription = orderDescription;
  }

  /**
   * Overrides the default toString() method of OrderStatus to return the raw status string.
   *
   * @return the description of an order status in required format
   */
  @Override
  public String toString() {
    return orderDescription;
  }

  /**
   * Parses a status string. The method returns null if no matching status can be found.
   *
   * @param otherStatus status string to be parsed
   * @return the enum instance corresponding to the order status
   */
  public static OrderStatus parseStatus(String otherStatus) {
    for (OrderStatus status : values()) {
      if (status.toString().equals(otherStatus)) {
        return status;
      }
    }
    return null;
  }
}
