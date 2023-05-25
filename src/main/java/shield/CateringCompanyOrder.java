package shield;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Representation of an order placed with a catering company. */
public class CateringCompanyOrder {
  /** The order number. */
  private final int number;
  /** The food box corresponding to the order. */
  private final FoodBox foodBox;
  /** The status of the order. */
  private OrderStatus status;
  /** The time the order was ordered. */
  private LocalDateTime timeOrdered;

  /**
   * Initialises a catering company order.
   *
   * @param number the order number
   * @param foodBox the food box for this order
   * @param orderPlacedDateTime the time the order was placed
   */
  public CateringCompanyOrder(int number, FoodBox foodBox, LocalDateTime orderPlacedDateTime) {
    this.number = number;
    this.foodBox = foodBox;
    this.timeOrdered = orderPlacedDateTime;
    this.status = OrderStatus.PLACED;
  }

  /**
   * Get the status of a particular catering company order
   *
   * @return the status of the order placed
   */
  public OrderStatus getStatus() {
    return status;
  }

  /**
   * Get the order number of the particular catering company order
   *
   * @return order number of the order placed
   */
  public int getNumber() {
    return number;
  }

  /**
   * This method is used to setup the status of a particular catering company order
   *
   * @param status is the status to which the objects status must be changed
   * @return true if the status has been successfully set, otherwise false
   */
  public boolean setStatus(OrderStatus status) {
    if (Objects.nonNull(status)) {
      this.status = status;
      return true;
    }
    return false;
  }

  /**
   * This method is used to get the item quantity given the ID of the item
   *
   * @param itemId is the item ID of which we need to return the quantity
   * @return the quantity of the particular item in the food box if found. Otherwise, -1
   */
  public int getItemQuantity(int itemId) {
    for (FoodBoxItem item : this.foodBox.getContents()) {
      if (Objects.nonNull(item) && itemId == item.getId()) {
        return item.getQuantity();
      }
    }
    return -1;
  }

  /**
   * Get the food box in the order
   *
   * @return the particular food box of the order
   */
  public FoodBox getFoodBox() {
    return foodBox;
  }

  /**
   * Get the item name of a particular item in the food box
   *
   * @param itemId the ID in the food box for which we need to return the name
   * @return the name of the particular item if found, otherwise null
   */
  public String getItemName(int itemId) {
    for (FoodBoxItem item : this.foodBox.getContents()) {
      if (Objects.nonNull(item) && itemId == item.getId()) {
        return item.getName();
      }
    }
    return null;
  }

  /**
   * Get the item IDs of items in the food box
   *
   * @return all the IDs in a particular food box
   */
  public List<Integer> getItemIds() {
    List<Integer> ids = new ArrayList<>();
    for (FoodBoxItem item : foodBox.getContents()) {
      if (Objects.nonNull(item)) {
        ids.add(item.getId());
      }
    }
    return ids;
  }

  /**
   * Get the time at which the order has been placed
   *
   * @return the time object when the order has been placed by the shielding individual
   */
  public LocalDateTime getTimeOrdered() {
    return timeOrdered;
  }

  /**
   * Set the time at which the order has been placed by the shielding individual
   *
   * @param timeOrdered is the time at which the order the order is set to be placed
   */
  public void setTimeOrdered(LocalDateTime timeOrdered) {
    this.timeOrdered = timeOrdered;
  }
}
