package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PetGenderTest {

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        pet.setName("Fido");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    void getGender_shouldReturnNull_whenNotSet() {
        assertNull(pet.getGender());
    }

    @Test
    void setGender_shouldSetGenderToMale() {
        pet.setGender(Gender.MALE);
        assertEquals(Gender.MALE, pet.getGender());
    }

    @Test
    void setGender_shouldSetGenderToFemale() {
        pet.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, pet.getGender());
    }

    @Test
    void setGender_shouldSetGenderToUnknown() {
        pet.setGender(Gender.UNKNOWN);
        assertEquals(Gender.UNKNOWN, pet.getGender());
    }

    @Test
    void setGender_shouldAllowChangingGender() {
        pet.setGender(Gender.MALE);
        assertEquals(Gender.MALE, pet.getGender());

        pet.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, pet.getGender());
    }

    @Test
    void setGender_shouldAllowSettingToNull() {
        pet.setGender(Gender.MALE);
        pet.setGender(null);
        assertNull(pet.getGender());
    }
}
