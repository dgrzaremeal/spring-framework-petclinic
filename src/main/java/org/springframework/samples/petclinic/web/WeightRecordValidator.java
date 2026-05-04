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

import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

/**
 * {@code Validator} for {@link WeightRecord} that enforces the rule:
 * measurement date must not be in the future.
 */
public class WeightRecordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return WeightRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WeightRecord weightRecord = (WeightRecord) target;
        LocalDate measurementDate = weightRecord.getMeasurementDate();
        if (measurementDate != null && measurementDate.isAfter(LocalDate.now())) {
            errors.rejectValue("measurementDate", "future", "Measurement date must not be in the future");
        }
    }

}
