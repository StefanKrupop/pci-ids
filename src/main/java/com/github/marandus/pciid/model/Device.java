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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.github.marandus.pciid.service.ArgumentValidator;

/**
 * Java representation of a PCI device. Each device has a 16 Bit ID, which is unique in the scope of
 * its vendor, represented by four hex-characters, and a mandatory name. The comment field is
 * optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 0.1
 */
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
     * Add a new subsystem to the internal subsystems set.
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

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getComment() {
        return this.comment;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Device)) return false;
        final Device other = (Device) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$name = this.getName();
        final java.lang.Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Device;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Device(id=" + this.getId() + ", name=" + this.getName() + ", comment=" + this.getComment() + ", subsystems=" + this.getSubsystems() + ", numericId=" + this.numericId + ")";
    }
}
