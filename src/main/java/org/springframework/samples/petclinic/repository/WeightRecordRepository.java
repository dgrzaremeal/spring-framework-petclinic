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
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.WeightRecord;

/**
 * Repository class for <code>WeightRecord</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Spring PetClinic Team
 */
public interface WeightRecordRepository {

    /**
     * Save a <code>WeightRecord</code> to the data store, either inserting or updating it.
     *
     * @param weightRecord the <code>WeightRecord</code> to save
     * @see BaseEntity#isNew
     */
    void save(WeightRecord weightRecord);

    /**
     * Returns all weight records for the given pet, ordered by measurement_date ASC.
     *
     * @param petId the pet ID
     * @return list of weight records ordered by measurement date ascending
     */
    List<WeightRecord> findByPetId(Integer petId);

}
