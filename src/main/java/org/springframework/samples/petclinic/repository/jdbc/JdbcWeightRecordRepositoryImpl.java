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
package org.springframework.samples.petclinic.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A simple JDBC-based implementation of the {@link WeightRecordRepository} interface.
 */
@Repository
public class JdbcWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertWeightRecord;

    public JdbcWeightRecordRepositoryImpl(DataSource dataSource,
                                          NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertWeightRecord = new SimpleJdbcInsert(dataSource)
            .withTableName("weight_records")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<WeightRecord> findByPetId(Integer petId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("petId", petId);
        return this.namedParameterJdbcTemplate.query(
            "SELECT id, pet_id, weight_kg, measurement_date FROM weight_records " +
            "WHERE pet_id = :petId ORDER BY measurement_date ASC",
            params,
            new WeightRecordRowMapper());
    }

    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.isNew()) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pet_id", weightRecord.getPet().getId())
                .addValue("weight_kg", weightRecord.getWeightKg())
                .addValue("measurement_date", weightRecord.getMeasurementDate());
            Number newKey = this.insertWeightRecord.executeAndReturnKey(params);
            weightRecord.setId(newKey.intValue());
        } else {
            throw new UnsupportedOperationException("WeightRecord update not supported");
        }
    }

    private static class WeightRecordRowMapper implements RowMapper<WeightRecord> {
        @Override
        public WeightRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            WeightRecord wr = new WeightRecord();
            wr.setId(rs.getInt("id"));
            wr.setWeightKg(rs.getBigDecimal("weight_kg"));
            wr.setMeasurementDate(rs.getObject("measurement_date", java.time.LocalDate.class));
            // pet_id is stored but we don't load the full Pet object here
            // (consistent with JdbcVisitRepositoryImpl pattern for simple queries)
            return wr;
        }
    }

}
