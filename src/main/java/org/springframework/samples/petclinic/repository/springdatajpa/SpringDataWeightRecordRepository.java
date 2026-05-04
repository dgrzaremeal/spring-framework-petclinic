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
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;

/**
 * Spring Data JPA specialization of the {@link WeightRecordRepository} interface
 *
 * @author Spring PetClinic Team
 * @since 2026
 */
public interface SpringDataWeightRecordRepository extends WeightRecordRepository, Repository<WeightRecord, Integer> {

    /**
     * Returns all weight records for the given pet, ordered by measurement date ascending.
     * This method name follows Spring Data naming conventions for automatic query generation.
     *
     * @param petId the pet ID
     * @return list of weight records ordered by measurement date ascending
     */
    List<WeightRecord> findByPetIdOrderByMeasurementDateAsc(Integer petId);
}
