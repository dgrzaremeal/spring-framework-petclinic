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
package org.springframework.samples.petclinic.repository.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * A simple JDBC-based implementation of the {@link WeightRecordRepository} interface.
 *
 * @author Spring PetClinic Team
 */
@Repository
public class JdbcWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final JdbcClient jdbcClient;

    private final SimpleJdbcInsert insertWeightRecord;

    public JdbcWeightRecordRepositoryImpl(DataSource dataSource, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;

        this.insertWeightRecord = new SimpleJdbcInsert(dataSource)
            .withTableName("weight_records")
            .usingGeneratedKeyColumns("id");
    }


    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.isNew()) {
            Number newKey = this.insertWeightRecord.executeAndReturnKey(
                createWeightRecordParameterSource(weightRecord));
            weightRecord.setId(newKey.intValue());
        } else {
            throw new UnsupportedOperationException("WeightRecord update not supported");
        }
    }


    /**
     * Creates a {@link MapSqlParameterSource} based on data values from the supplied {@link WeightRecord} instance.
     */
    private MapSqlParameterSource createWeightRecordParameterSource(WeightRecord weightRecord) {
        return new MapSqlParameterSource()
            .addValue("id", weightRecord.getId())
            .addValue("weight_kg", weightRecord.getWeightKg())
            .addValue("measurement_date", weightRecord.getMeasurementDate())
            .addValue("pet_id", weightRecord.getPet().getId());
    }

    @Override
    public List<WeightRecord> findByPetId(Integer petId) {
        JdbcPet pet = this.jdbcClient
            .sql("SELECT id, name, birth_date, type_id, owner_id FROM pets WHERE id=:id")
            .param("id", petId)
            .query(new JdbcPetRowMapper())
            .single();

        List<WeightRecord> weightRecords = this.jdbcClient
            .sql("SELECT id, weight_kg, measurement_date FROM weight_records WHERE pet_id=:id ORDER BY measurement_date ASC")
            .param("id", petId)
            .query(new JdbcWeightRecordRowMapper())
            .list();

        for (WeightRecord weightRecord: weightRecords) {
            weightRecord.setPet(pet);
        }

        return weightRecords;
    }

}
