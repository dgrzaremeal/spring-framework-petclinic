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

import org.jspecify.annotations.NullMarked;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

/**
 * <code>Validator</code> for <code>WeightRecord</code> forms.
 * <p>
 * Validates that the measurement date is not in the future.
 * </p>
 *
 * @author Spring PetClinic Team
 */
@NullMarked
public class WeightRecordValidator implements Validator {

    @Override
    public void validate(Object obj, Errors errors) {
        WeightRecord weightRecord = (WeightRecord) obj;
        LocalDate measurementDate = weightRecord.getMeasurementDate();
        
        // measurement date validation - must not be in the future
        if (measurementDate != null && measurementDate.isAfter(LocalDate.now())) {
            errors.rejectValue("measurementDate", "future.date", "Measurement date cannot be in the future");
        }
    }

    /**
     * This Validator validates *just* WeightRecord instances
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return WeightRecord.class.isAssignableFrom(clazz);
    }

}
