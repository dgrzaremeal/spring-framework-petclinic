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
package org.springframework.samples.petclinic.repository.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the WeightRecordRepository interface using EntityManager.
 * <p/>
 * <p>The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 * @author Spring PetClinic Team
 * @since 2026
 */
@Repository
public class JpaWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final EntityManager em;

    public JpaWeightRecordRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.getId() == null) {
            this.em.persist(weightRecord);
        } else {
            this.em.merge(weightRecord);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<WeightRecord> findByPetId(Integer petId) {
        Query query = this.em.createQuery("SELECT w FROM WeightRecord w where w.pet.id = :id ORDER BY w.measurementDate ASC");
        query.setParameter("id", petId);
        return query.getResultList();
    }

}
