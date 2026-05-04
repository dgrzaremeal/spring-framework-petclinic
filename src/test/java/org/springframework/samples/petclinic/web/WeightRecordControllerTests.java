package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link WeightRecordController}.
 */
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class WeightRecordControllerTests {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;

    @Autowired
    private WeightRecordController weightRecordController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(weightRecordController).build();

        Owner owner = new Owner();
        owner.setId(TEST_OWNER_ID);
        owner.setFirstName("George");
        owner.setLastName("Franklin");

        PetType cat = new PetType();
        cat.setName("cat");

        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Leo");
        pet.setBirthDate(LocalDate.of(2010, 9, 7));
        pet.setType(cat);
        owner.addPet(pet);

        given(this.clinicService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
        given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(pet);
        given(this.clinicService.findWeightRecordsByPetId(TEST_PET_ID)).willReturn(Collections.emptyList());
        doNothing().when(this.clinicService).saveWeightRecord(any(WeightRecord.class));
    }

    @Test
    void testShowWeightHistory() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/weights", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("weightRecords"))
            .andExpect(view().name("pets/weightRecordList"));
    }

    @Test
    void testInitNewWeightRecordForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/weights/new", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("weightRecord"))
            .andExpect(view().name("pets/createWeightRecordForm"));
    }

    @Test
    void testProcessNewWeightRecordFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/weights/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("weightKg", "4.5")
            .param("measurementDate", "2024/01/15")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID + "/pets/" + TEST_PET_ID + "/weights"));
    }

    @Test
    void testProcessNewWeightRecordFormHasErrors_FutureDate() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/weights/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("weightKg", "4.5")
            .param("measurementDate", LocalDate.now().plusDays(1).toString().replace("-", "/"))
        )
            .andExpect(model().attributeHasErrors("weightRecord"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createWeightRecordForm"));
    }

    @Test
    void testProcessNewWeightRecordFormHasErrors_MissingWeight() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/weights/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("measurementDate", "2024/01/15")
        )
            .andExpect(model().attributeHasErrors("weightRecord"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createWeightRecordForm"));
    }

}
