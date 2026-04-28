package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenderTest {

    @Test
    void valueOf_shouldReturnMale_whenMaleStringProvided() {
        Gender gender = Gender.valueOf("MALE");
        assertEquals(Gender.MALE, gender);
    }

    @Test
    void valueOf_shouldReturnFemale_whenFemaleStringProvided() {
        Gender gender = Gender.valueOf("FEMALE");
        assertEquals(Gender.FEMALE, gender);
    }

    @Test
    void valueOf_shouldReturnUnknown_whenUnknownStringProvided() {
        Gender gender = Gender.valueOf("UNKNOWN");
        assertEquals(Gender.UNKNOWN, gender);
    }

    @Test
    void getDisplayKey_shouldReturnMaleKey_whenMaleGender() {
        String key = Gender.MALE.getDisplayKey();
        assertEquals("pet.gender.male", key);
    }

    @Test
    void getDisplayKey_shouldReturnFemaleKey_whenFemaleGender() {
        String key = Gender.FEMALE.getDisplayKey();
        assertEquals("pet.gender.female", key);
    }

    @Test
    void getDisplayKey_shouldReturnUnknownKey_whenUnknownGender() {
        String key = Gender.UNKNOWN.getDisplayKey();
        assertEquals("pet.gender.unknown", key);
    }

    @Test
    void values_shouldContainThreeValues() {
        Gender[] values = Gender.values();
        assertEquals(3, values.length);
    }

    @Test
    void values_shouldContainMaleFemaleUnknown() {
        Gender[] values = Gender.values();
        assertTrue(contains(values, Gender.MALE));
        assertTrue(contains(values, Gender.FEMALE));
        assertTrue(contains(values, Gender.UNKNOWN));
    }

    private boolean contains(Gender[] values, Gender target) {
        for (Gender g : values) {
            if (g == target) {
                return true;
            }
        }
        return false;
    }
}
