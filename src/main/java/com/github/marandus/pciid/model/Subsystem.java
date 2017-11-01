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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.github.marandus.pciid.service.ArgumentValidator;

/**
 * Java representation of a PCI device subsystem. Each subsystem is identified by the unique
 * combination of the 16 Bit ID of its vendor and its own 16 Bit ID. The name field is mandatory,
 * the comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 */
@Getter
@EqualsAndHashCode(of = {"id", "vendorId", "name"})
@ToString
public class Subsystem implements Comparable<Subsystem> {

    /**
     * String representation of the unique 16 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * String representation of the unique 16 Bit subsystem vendor ID. This ID can be used as a link
     * to another {@link Vendor} object.
     */
    private final String vendorId;

    /**
     * Integer representation of the unique 16 Bit ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericId;

    /**
     * Integer representation of the unique 16 Bit subsystem vendor ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericVendorId;

    /**
     * Create a new Subsystem database entry.
     *
     * @param id Device ID of this subsystem
     * @param name Name of this subsystem
     * @param comment Optional comment, may be null
     * @param vendorId Vendor ID of this subsystem's vendor
     */
    public Subsystem(String id, String name, String comment, String vendorId) {
        ArgumentValidator.requireStringLength(vendorId, 4, ArgumentValidator.NumberCompare.EQUAL, "Subsystem vendor ID");
        ArgumentValidator.requireStringLength(id, 4, ArgumentValidator.NumberCompare.EQUAL, "Subsystem ID");
        ArgumentValidator.requireNonBlank(name, "Subsystem name");

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.vendorId = vendorId;

        this.numericId = Integer.decode("0x" + id);
        this.numericVendorId = Integer.decode("0x" + vendorId);
    }

    /**
     * Compare this object to another {@link Device} object. First comparison will take place on the
     * integer representation of the subsystem vendor unique ID. If these are equal, the integer
     * representation of the subsystem ID will be used for comparison.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(Subsystem t) {
        if (this.numericVendorId.equals(t.numericVendorId)) {
            return this.numericId.compareTo(t.numericId);
        }

        return this.numericVendorId.compareTo(t.numericVendorId);
    }
}
