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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link Gender} enum.
 */
class GenderTest {

    @Test
    void shouldHaveThreeValues() {
        assertThat(Gender.values()).hasSize(3);
    }

    @Test
    void shouldContainMale() {
        assertThat(Gender.valueOf("MALE")).isEqualTo(Gender.MALE);
    }

    @Test
    void shouldContainFemale() {
        assertThat(Gender.valueOf("FEMALE")).isEqualTo(Gender.FEMALE);
    }

    @Test
    void shouldContainUnknown() {
        assertThat(Gender.valueOf("UNKNOWN")).isEqualTo(Gender.UNKNOWN);
    }

    @Test
    void shouldHaveDisplayKeyForMale() {
        assertThat(Gender.MALE.getDisplayKey()).isEqualTo("pet.gender.male");
    }

    @Test
    void shouldHaveDisplayKeyForFemale() {
        assertThat(Gender.FEMALE.getDisplayKey()).isEqualTo("pet.gender.female");
    }

    @Test
    void shouldHaveDisplayKeyForUnknown() {
        assertThat(Gender.UNKNOWN.getDisplayKey()).isEqualTo("pet.gender.unknown");
    }

    @Test
    void shouldDefaultToUnknownForNull() {
        assertThat(Gender.fromString(null)).isEqualTo(Gender.UNKNOWN);
    }

    @Test
    void shouldDefaultToUnknownForInvalidValue() {
        assertThat(Gender.fromString("INVALID")).isEqualTo(Gender.UNKNOWN);
    }

    @Test
    void shouldParseValidValues() {
        assertThat(Gender.fromString("MALE")).isEqualTo(Gender.MALE);
        assertThat(Gender.fromString("FEMALE")).isEqualTo(Gender.FEMALE);
        assertThat(Gender.fromString("UNKNOWN")).isEqualTo(Gender.UNKNOWN);
    }

}
