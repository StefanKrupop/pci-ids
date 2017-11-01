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
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.exodusproject.pciid.service.ArgumentValidator;

/**
 * Java representation of a PCI device. Each device has a 16 Bit ID, which is unique in the scope of
 * its vendor, represented by four hex-characters, and a mandatory name. The comment field is
 * optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 1.0
 */
@Getter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class Device implements Comparable<Device> {

    /**
     * String representation of the unique 16 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * Internal set of subsystems belonging to this device.
     */
    private final Set<Subsystem> subsystems;

    /**
     * Integer representation of the unique 16 Bit ID. For internal use only.
     */
    @Getter(AccessLevel.NONE)
    private final Integer numericId;

    /**
     * Create a new Device database entry.
     *
     * @param id Unique 16 Bit ID
     * @param name Full name of the device
     * @param comment Optional comment, may be null
     */
    public Device(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 4, ArgumentValidator.NumberCompare.EQUAL, "Device ID");
        ArgumentValidator.requireNonBlank(name, "Device name");

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.subsystems = new HashSet<>();

        this.numericId = Integer.decode("0x" + id);
    }

    /**
     * Add a new subsystem to the internal subsystems map.
     *
     * @param subsys Subsystem to add
     */
    public void addSubsystem(Subsystem subsys) {
        ArgumentValidator.requireNonNull(subsys, "Device subsystem");

        this.subsystems.add(subsys);
    }

    /**
     * Retrieve an unmodifiable view of the subsystems set.
     *
     * @return Unmodifiable set view
     */
    public Set<Subsystem> getSubsystems() {
        return Collections.unmodifiableSet(this.subsystems);
    }

    /**
     * Compare this object to another {@link Device} object. Comparison will take place on the
     * integer representation of the unique ID.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(Device t) {
        return this.numericId.compareTo(t.numericId);
    }
}
