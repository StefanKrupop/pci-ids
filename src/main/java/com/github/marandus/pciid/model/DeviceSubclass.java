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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Java representation of a PCI device subclass. Each subclass has an 8 Bit ID, which is unique in
 * the scope of its device class, represented by two hex-characters, and a mandatory name. The
 * comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 0.3
 */
@Getter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class DeviceSubclass implements Comparable<DeviceSubclass> {

    /**
     * String representation of the unique 8 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * Internal set of program interfaces belonging to this device class.
     */
    private final Map<String, ProgramInterface> programInterfaces;

    /**
     * Integer representation of the unique 8 Bit ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericId;

    /**
     * Create a new Device Subclass database entry.
     *
     * @param id Unique 8 Bit ID
     * @param name Full name of the device subclass
     * @param comment Optional comment, may be null
     */
    public DeviceSubclass(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 2, NumberCompareOperator.EQUAL, "Device subclass ID");
        ArgumentValidator.requireNonBlank(name, "Device subclass name");

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.programInterfaces = new HashMap<>();

        this.numericId = Integer.decode("0x" + id);
    }

    /**
     * Add a new program interface to the internal interfaces map.
     *
     * @param iface Program interface to add
     */
    public void addProgramInterface(ProgramInterface iface) {
        ArgumentValidator.requireNonNull(iface, "Device subclass program interface");

        this.programInterfaces.put(iface.getId(), iface);
    }

    /**
     * Retrieve an unmodifiable view of the internal interfaces map.
     *
     * @return Unmodifiable map view
     */
    public Map<String, ProgramInterface> getProgramInterfaces() {
        return Collections.unmodifiableMap(this.programInterfaces);
    }

    /**
     * Compare this object to another {@link DeviceSubclass} object. Comparison will take place on
     * the integer representation of the unique ID.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(DeviceSubclass t) {
        return this.numericId.compareTo(t.numericId);
    }
}
