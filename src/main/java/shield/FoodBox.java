package shield;

import com.google.gson.annotations.Expose;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Objects;

/** Representation of a food box. */
public class FoodBox {
  /** Represents an invalid item quantity */
  public static final int INVALID_ITEM_QUANTITY = -1;

  @Expose private List<FoodBoxItem> contents;
  private String delivered_by;
  private String diet;
  private String id;
  private String name;

  /**
   * Get the contents of the particular food box
   *
   * @return a list of food box items in the food box itself
   */
  public List<FoodBoxItem> getContents() {
    return contents;
  }

  /**
   * Get the dietary preference of the particular food box
   *
   * @return the dietary preference of the particular food box
   */
  public String getDiet() {
    return diet;
  }

  /**
   * Get the ID of the food box
   *
   * @return a stringified ID of the food box
   */
  public String getId() {
    return id;
  }

  /**
   * This method is used to set the quantity for a particular item ID in the food box
   *
   * @param itemId is the ID for which the quantity needs to be changed
   * @param quantity is the quantity to which the item ID needs to be changed
   * @param ordered whether the food box has been ordered
   * @return if the quantity has been successfully set by the item
   */
  public boolean setQuantityForItem(int itemId, int quantity, boolean ordered) {
    if (itemId < 0 || quantity < 0) {
      return false;
    }
    if (quantity == 0) {
      // Ensure that the food box contents are not completely zeroed out
      int totalQuantity = 0;
      for (FoodBoxItem item : contents) {
        if (itemId != item.getId()) {
          totalQuantity += item.getQuantity();
        } else {
          totalQuantity += quantity;
        }
      }
      if (totalQuantity == 0) {
        return false;
      }
    }
    for (FoodBoxItem item : contents) {
      if (itemId == item.getId()) {
        return item.setQuantity(quantity, ordered);
      }
    }
    return false;
  }

  /**
   * This method is used to convert a JSON object to a stringified JSON Fields which are not marked
   * as exposed will be ignored
   *
   * @return the stringified JSON object
   */
  public String jsonify() {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    return gson.toJson(this);
  }

  /**
   * Get the quantity of a particular item given its ID
   *
   * @param itemId is the ID for which we need to get the quantity
   * @return the quantity of the item with this ID
   */
  public int getItemQuantity(int itemId) {
    if (Objects.isNull(contents)) {
      return INVALID_ITEM_QUANTITY;
    }
    for (FoodBoxItem item : contents) {
      if (Objects.nonNull(item) && item.getId() == itemId) {
        return item.getQuantity();
      }
    }
    return INVALID_ITEM_QUANTITY;
  }

  /**
   * Get the item name of a particular item given its ID
   *
   * @param itemId is the ID for which we need to get the name
   * @return the name of that particular item ID
   */
  public String getItemName(int itemId) {
    if (Objects.isNull(contents)) {
      return null;
    }
    for (FoodBoxItem item : contents) {
      if (Objects.nonNull(item) && item.getId() == itemId) {
        return item.getName();
      }
    }
    return null;
  }

  /**
   * Returns a deep copy of the current instance.
   *
   * @return a copy of this food box
   */
  public FoodBox copy() {
    Gson gson = new Gson();
    JsonObject serialized = gson.toJsonTree(this).getAsJsonObject();
    return gson.fromJson(serialized, this.getClass());
  }
}
