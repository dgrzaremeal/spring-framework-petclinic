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
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA specialization of the {@link WeightRecordRepository} interface.
 * The {@code @Query} annotation ensures results are ordered by measurement_date ASC (FR-004).
 */
public interface SpringDataWeightRecordRepository extends WeightRecordRepository, Repository<WeightRecord, Integer> {

    @Override
    @Query("SELECT wr FROM WeightRecord wr WHERE wr.pet.id = :petId ORDER BY wr.measurementDate ASC")
    List<WeightRecord> findByPetId(Integer petId);

}
