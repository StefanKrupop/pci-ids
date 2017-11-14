/*
 * Copyright 2017 Thomas Rix.
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
package com.github.marandus.pciid.model;

import com.github.marandus.argval.ArgumentValidator;
import com.github.marandus.argval.enums.NumberCompareOperator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Java representation of a PCI device subclass program interface. Each interface has an 8 Bit ID,
 * which is unique in the scope of its device subclass, represented by two hex-characters, and a
 * mandatory name. The comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 0.3
 */
@Getter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class ProgramInterface implements Comparable<ProgramInterface> {

    /**
     * String representation of the unique 8 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * Integer representation of the unique 8 Bit ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericId;

    /**
     * Create a new Progrmam Interface database entry.
     *
     * @param id Unique 8 Bit ID
     * @param name Full name of the program interface
     * @param comment Optional comment, may be null
     */
    public ProgramInterface(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 2, NumberCompareOperator.EQUAL, "Program interface ID");
        ArgumentValidator.requireNonBlank(name, "Program interface name");

        this.id = id;
        this.name = name;
        this.comment = comment;

        this.numericId = Integer.decode("0x" + id);
    }

    /**
     * Compare this object to another {@link ProgramInterface} object. Comparison will take place on
     * the integer representation of the unique ID.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(ProgramInterface t) {
        return this.numericId.compareTo(t.numericId);
    }
}
