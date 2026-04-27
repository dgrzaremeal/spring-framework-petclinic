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

/**
 * Enumeration representing the gender of a pet.
 */
public enum Gender {

    MALE,
    FEMALE,
    UNKNOWN;

    private static final Gender DEFAULT = UNKNOWN;

    public static Gender fromString(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT;
        }
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }

    public String toDisplayKey() {
        return switch (this) {
            case MALE -> "pet.gender.male";
            case FEMALE -> "pet.gender.female";
            case UNKNOWN -> "pet.gender.unknown";
        };
    }
}