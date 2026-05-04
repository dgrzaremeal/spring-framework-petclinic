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
package org.springframework.samples.petclinic.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the {@link WeightRecordRepository} interface using EntityManager.
 */
@Repository
public class JpaWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final EntityManager em;

    public JpaWeightRecordRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<WeightRecord> findByPetId(Integer petId) {
        TypedQuery<WeightRecord> query = this.em.createQuery(
            "SELECT wr FROM WeightRecord wr WHERE wr.pet.id = :petId ORDER BY wr.measurementDate ASC",
            WeightRecord.class);
        query.setParameter("petId", petId);
        return query.getResultList();
    }

    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.getId() == null) {
            this.em.persist(weightRecord);
        } else {
            this.em.merge(weightRecord);
        }
    }

}
