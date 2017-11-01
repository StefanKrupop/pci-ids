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
package net.exodusproject.pciid.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.exodusproject.pciid.service.ArgumentValidator;

/**
 * Java representation of a PCI device vendor. Each vendor has a unique 16 Bit ID, represented by
 * four hex-characters, and a mandatory name. The comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 1.0
 */
@Getter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class Vendor implements Comparable<Vendor> {

    /**
     * String representation of the unique 16 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * Internal map of devices belonging to this vendor. Identified by their unique 16 Bit ID.
     */
    private final Map<String, Device> devices;

    /**
     * Integer representation of the unique 16 Bit ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericId;

    /**
     * Create a new Vendor database entry.
     *
     * @param id Unique 16 Bit ID
     * @param name Full name of the vendor
     * @param comment Optional comment, may be null
     */
    public Vendor(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 4, ArgumentValidator.NumberCompare.EQUAL, "Vendor ID");
        ArgumentValidator.requireNonBlank(name, "Vendor name");

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.devices = new HashMap<>();

        this.numericId = Integer.decode("0x" + id);
    }

    /**
     * Add a new device to the internal devices map.
     *
     * @param device Device to add
     */
    public void addDevice(Device device) {
        ArgumentValidator.requireNonNull(device, "Vendor device");

        this.devices.put(device.getId(), device);
    }

    /**
     * Retrieve an unmodifiable view of the devices map.
     *
     * @return Unmodifiable map view
     */
    public Map<String, Device> getDevices() {
        return Collections.unmodifiableMap(this.devices);
    }

    /**
     * Compare this object to another {@link Vendor} object. Comparison will take place on the
     * integer representation of the unique ID.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(Vendor t) {
        return this.numericId.compareTo(t.numericId);
    }
}
