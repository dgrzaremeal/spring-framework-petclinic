/*
 * Copyright 2002-2026 the original author or authors.
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
package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * MVC controller for weight record operations.
 * Handles recording new weight measurements (US1) and viewing weight history (US2).
 */
@Controller
public class WeightRecordController {

    private final ClinicService clinicService;

    public WeightRecordController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder("weightRecord")
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
        dataBinder.addValidators(new WeightRecordValidator());
    }

    /**
     * Pre-populates the weightRecord model attribute with the pet, so that
     * Bean Validation's @NotNull on pet passes before the form is processed.
     */
    @ModelAttribute("weightRecord")
    public WeightRecord loadWeightRecordWithPet(@PathVariable("petId") int petId) {
        WeightRecord weightRecord = new WeightRecord();
        Pet pet = this.clinicService.findPetById(petId);
        weightRecord.setPet(pet);
        return weightRecord;
    }

    /**
     * US2: GET /owners/{ownerId}/pets/{petId}/weights — show weight history list.
     */
    @GetMapping("/owners/{ownerId}/pets/{petId}/weights")
    public String showWeightHistory(@PathVariable int ownerId,
                                    @PathVariable int petId,
                                    Model model) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return "redirect:/owners";
        }
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null || !pet.getOwner().getId().equals(ownerId)) {
            return "redirect:/owners/" + ownerId;
        }
        List<WeightRecord> weightRecords = this.clinicService.findWeightRecordsByPetId(petId);
        model.addAttribute("pet", pet);
        model.addAttribute("weightRecords", weightRecords);
        return "pets/weightRecordList";
    }

    /**
     * US1: GET /owners/{ownerId}/pets/{petId}/weights/new — show new weight record form.
     */
    @GetMapping("/owners/{ownerId}/pets/{petId}/weights/new")
    public String initNewWeightRecordForm(@PathVariable int ownerId,
                                          @PathVariable int petId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return "redirect:/owners";
        }
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null || !pet.getOwner().getId().equals(ownerId)) {
            return "redirect:/owners/" + ownerId;
        }
        return "pets/createWeightRecordForm";
    }

    /**
     * US1: POST /owners/{ownerId}/pets/{petId}/weights/new — save new weight record.
     */
    @PostMapping("/owners/{ownerId}/pets/{petId}/weights/new")
    public String processNewWeightRecordForm(@PathVariable int ownerId,
                                             @PathVariable int petId,
                                             @Valid @ModelAttribute("weightRecord") WeightRecord weightRecord,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createWeightRecordForm";
        }
        this.clinicService.saveWeightRecord(weightRecord);
        return "redirect:/owners/" + ownerId + "/pets/" + petId + "/weights";
    }

}
