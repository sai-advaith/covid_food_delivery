package shield;

import java.util.Objects;

/** This enum stores all dietary preferences for a food box. */
public enum DietaryPreference {
  /**
   * Dietary preference indicative of no preference from a shielding individual, bar the special
   * ones, such as {@link #VEGAN} or {#POLLOTARIAN}
   */
  NONE("none"),

  /** Dietary preference indicative of no preference from a shielding individual */
  NO_PREFERENCE(""),

  /** Dietary preference indicative of pollotarian dietary preference from a shielding individual */
  POLLOTARIAN("pollotarian"),

  /** Dietary preference indicative of vegan dietary preference from a shielding individual */
  VEGAN("vegan");

  /** Description of the dietary preference in the required format */
  private final String dietaryPreference;

  /**
   * Initialises an instance of this enum.
   *
   * @param dietaryPreference a dietary preference
   */
  DietaryPreference(String dietaryPreference) {
    this.dietaryPreference = dietaryPreference;
  }

  /**
   * Overrides the default toString() method of DietaryPreference to return the raw dietary
   * preference preference
   *
   * @return the dietary preference chosen by the shielding individual in required format
   */
  @Override
  public String toString() {
    return dietaryPreference;
  }

  /**
   * Parses a dietary preference string. The method returns null if no matching dietary preference
   * can be found.
   *
   * @param otherPreference dietary preference string to be parsed
   * @return the corresponding dietary preference
   */
  public static DietaryPreference parsePreference(String otherPreference) {
    if (Objects.isNull(otherPreference)) {
      return NO_PREFERENCE;
    }
    for (DietaryPreference preference : values()) {
      if (preference.toString().equals(otherPreference)) {
        return preference;
      }
    }
    return null;
  }
}
