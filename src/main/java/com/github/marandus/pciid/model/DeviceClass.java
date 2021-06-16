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

import com.github.marandus.pciid.service.ArgumentValidator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Java representation of a PCI device class. Each class has an 8 Bit unique ID, represented by two
 * hex-characters, and a mandatory name. The comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 0.3
 */
public class DeviceClass implements Comparable<DeviceClass> {

    /**
     * String representation of the unique 8 Bit ID.
     */
    private final String id;
    private final String name;
    private final String comment;

    /**
     * Internal map of subclasses belonging to this device class. Identified by their unique 8 Bit
     * ID.
     */
    private final Map<String, DeviceSubclass> subclasses;

    /**
     * Integer representation of the unique 8 Bit ID. For internal use only.
     */
    private final Integer numericId;

    /**
     * Create a new Device Class database entry.
     *
     * @param id Unique 8 Bit ID
     * @param name Full name of the device class
     * @param comment Optional comment, may be null
     */
    public DeviceClass(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 2, ArgumentValidator.NumberCompare.EQUAL, "Device class ID");
        ArgumentValidator.requireNonBlank(name, "Device class name");

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.subclasses = new HashMap<>();

        this.numericId = Integer.decode("0x" + id);
    }

    /**
     * Add a new device subclass to the internal subclasses map.
     *
     * @param subclass Subclass to add
     */
    public void addSubclass(DeviceSubclass subclass) {
        ArgumentValidator.requireNonNull(subclass, "Device subclass");

        this.subclasses.put(subclass.getId(), subclass);
    }

    /**
     * Retrieve an unmodifiable view of the device subclasses map.
     *
     * @return Unmodifiable map view
     */
    public Map<String, DeviceSubclass> getSubclasses() {
        return Collections.unmodifiableMap(this.subclasses);
    }

    /**
     * Compare this object to another {@link DeviceClass} object. Comparison will take place on the
     * integer representation of the unique ID.
     *
     * @param t Other object
     * @return Result of comparison
     */
    @Override
    public int compareTo(DeviceClass t) {
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
        if (!(o instanceof DeviceClass)) return false;
        final DeviceClass other = (DeviceClass) o;
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
        return other instanceof DeviceClass;
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
        return "DeviceClass(id=" + this.getId() + ", name=" + this.getName() + ", comment=" + this.getComment() + ", subclasses=" + this.getSubclasses() + ", numericId=" + this.numericId + ")";
    }
}
