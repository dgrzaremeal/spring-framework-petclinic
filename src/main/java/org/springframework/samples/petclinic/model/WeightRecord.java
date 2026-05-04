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
package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Simple JavaBean domain object representing a weight record for a pet.
 *
 * @author Spring PetClinic Team
 */
@Entity
@Table(name = "weight_records")
public class WeightRecord extends BaseEntity {

    /**
     * Holds value of property weightKg.
     */
    @NotNull
    @DecimalMin("0.001")
    @DecimalMax("250.0")
    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    /**
     * Holds value of property measurementDate.
     */
    @NotNull
    @Column(name = "measurement_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate measurementDate;

    /**
     * Holds value of property pet.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;


    /**
     * Creates a new instance of WeightRecord for the current date
     */
    public WeightRecord() {
        this.measurementDate = LocalDate.now();
    }


    /**
     * Getter for property weightKg.
     *
     * @return Value of property weightKg.
     */
    public BigDecimal getWeightKg() {
        return this.weightKg;
    }

    /**
     * Setter for property weightKg.
     *
     * @param weightKg New value of property weightKg.
     */
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    /**
     * Getter for property measurementDate.
     *
     * @return Value of property measurementDate.
     */
    public LocalDate getMeasurementDate() {
        return this.measurementDate;
    }

    /**
     * Setter for property measurementDate.
     *
     * @param measurementDate New value of property measurementDate.
     */
    public void setMeasurementDate(LocalDate measurementDate) {
        this.measurementDate = measurementDate;
    }

    /**
     * Getter for property pet.
     *
     * @return Value of property pet.
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Setter for property pet.
     *
     * @param pet New value of property pet.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }

}
