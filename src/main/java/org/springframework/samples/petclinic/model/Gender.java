/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the gender of a {@link Pet}.
 * <p>
 * Provides localization display keys and safe parsing with fallback to {@link #UNKNOWN}.
 * </p>
 */
public enum Gender {

    MALE("pet.gender.male"),
    FEMALE("pet.gender.female"),
    UNKNOWN("pet.gender.unknown");

    private static final Logger logger = LoggerFactory.getLogger(Gender.class);

    private final String displayKey;

    Gender(String displayKey) {
        this.displayKey = displayKey;
    }

    public String getDisplayKey() {
        return displayKey;
    }

    /**
     * Safely parse a string to a Gender value.
     * Returns {@link #UNKNOWN} for null or unrecognized values, logging a warning.
     */
    public static Gender fromString(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid gender value '{}', defaulting to UNKNOWN", value);
            return UNKNOWN;
        }
    }

}
