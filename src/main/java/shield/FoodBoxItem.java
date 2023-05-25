package shield;

import com.google.gson.annotations.Expose;

/** Representation of a food box item. */
public class FoodBoxItem {
  private static final int QUANTITY_NOT_INITIALIZED = -1;

  @Expose private int id;
  @Expose private String name;
  @Expose private int quantity; // Maximum quantity; we need this name for unmarshalling purposes.
  private int currentQuantity = QUANTITY_NOT_INITIALIZED;

  /**
   * Gets the ID of the particular food box item
   *
   * @return ID of the particular food box item
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the name of the particular food box item
   *
   * @return the name of the food box item
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the current quantity of the food box item.
   *
   * @return the quantity of the particular food box item
   */
  public int getQuantity() {
    if (currentQuantity == QUANTITY_NOT_INITIALIZED) {
      // Lazy-loading current quantity
      // Note: It has to be done here as GSON cannot be coerced to use a specific constructor
      currentQuantity = getMaxQuantity();
    }
    return currentQuantity;
  }

  // Wrapper function for better code readability
  private int getMaxQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity of the food box item.
   *
   * <p>If the item is part of a placed order, the maximum permissible quantity is given by {@link
   * #getQuantity}. Otherwise, the new quantity can be as high as {@link #getMaxQuantity}. In both
   * cases, the lowest quantity possible is 0
   *
   * @param quantity is the quantity to which the items quantity will be changed
   * @param ordered whether the item has been ordered
   * @return true if the quantity has been edited successfully, false otherwise
   */
  public boolean setQuantity(int quantity, boolean ordered) {
    int maxQuantity = ordered ? getQuantity() : getMaxQuantity();
    if (0 <= quantity && quantity <= maxQuantity) {
      this.currentQuantity = quantity;
      return true;
    }
    return false;
  }
}
