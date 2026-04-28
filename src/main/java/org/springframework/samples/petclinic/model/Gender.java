package org.springframework.samples.petclinic.model;

/**
 * Gender enumeration for Pet entity.
 * Represents the biological gender of a pet with localization support.
 */
public enum Gender {
    MALE("pet.gender.male"),
    FEMALE("pet.gender.female"),
    UNKNOWN("pet.gender.unknown");

    private final String displayKey;

    Gender(String displayKey) {
        this.displayKey = displayKey;
    }

    /**
     * Gets the localization key for this gender value.
     * Used to display gender in the UI with proper internationalization.
     *
     * @return the localization key (e.g., "pet.gender.male")
     */
    public String getDisplayKey() {
        return displayKey;
    }
}
