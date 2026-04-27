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


import java.text.ParseException;
import java.util.Locale;

import org.jspecify.annotations.NullMarked;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Gender;

/**
 * Instructs Spring MVC on how to parse and print elements of type 'Gender'.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
@NullMarked
public class GenderFormatter implements Formatter<Gender> {

    @Override
    public String print(Gender gender, Locale locale) {
        return gender.name();
    }

    @Override
    public Gender parse(String text, Locale locale) throws ParseException {
        try {
            return Gender.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new ParseException("gender not found: " + text, 0);
        }
    }

}