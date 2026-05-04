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
package org.springframework.samples.petclinic.web;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Spring PetClinic Team
 */
@Controller
public class WeightRecordController {

    private final ClinicService clinicService;

    public WeightRecordController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
        if (dataBinder.getTarget() instanceof WeightRecord) {
            dataBinder.addValidators(new WeightRecordValidator());
        }
    }

    /**
     * Called before each and every @GetMapping or @PostMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Pet object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param petId the pet identifier from the URI
     * @return WeightRecord
     */
    @ModelAttribute("weightRecord")
    public WeightRecord loadPetWithWeightRecord(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        WeightRecord weightRecord = new WeightRecord();
        weightRecord.setPet(pet);
        return weightRecord;
    }

    // Spring MVC calls method loadPetWithWeightRecord(...) before initNewWeightRecordForm is called
    @GetMapping(value = "/owners/*/pets/{petId}/weights/new")
    public String initNewWeightRecordForm(@PathVariable("petId") int petId, Map<String, Object> model) {
        return "pets/createWeightRecordForm";
    }

    // Spring MVC calls method loadPetWithWeightRecord(...) before processNewWeightRecordForm is called
    @PostMapping(value = "/owners/{ownerId}/pets/{petId}/weights/new")
    public String processNewWeightRecordForm(@Valid WeightRecord weightRecord, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createWeightRecordForm";
        }

        this.clinicService.saveWeightRecord(weightRecord);
        return "redirect:/owners/{ownerId}/pets/{petId}/weights";
    }

    @GetMapping(value = "/owners/{ownerId}/pets/{petId}/weights")
    public String showWeightRecords(@PathVariable int ownerId, @PathVariable int petId, Map<String, Object> model) {
        Pet pet = this.clinicService.findPetById(petId);
        
        // Validate pet ownership
        if (pet.getOwner().getId() != ownerId) {
            return "redirect:/";
        }
        
        model.put("pet", pet);
        model.put("weightRecords", this.clinicService.findWeightRecordsByPetId(petId));
        return "pets/weightRecordList";
    }

}
