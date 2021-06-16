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

/**
 * Java representation of a PCI device subclass program interface. Each interface has an 8 Bit ID,
 * which is unique in the scope of its device subclass, represented by two hex-characters, and a
 * mandatory name. The comment field is optional.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 0.3
 */
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
    private final Integer numericId;

    /**
     * Create a new Progrmam Interface database entry.
     *
     * @param id Unique 8 Bit ID
     * @param name Full name of the program interface
     * @param comment Optional comment, may be null
     */
    public ProgramInterface(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 2, ArgumentValidator.NumberCompare.EQUAL, "Program interface ID");
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
        if (!(o instanceof ProgramInterface)) return false;
        final ProgramInterface other = (ProgramInterface) o;
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
        return other instanceof ProgramInterface;
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
        return "ProgramInterface(id=" + this.getId() + ", name=" + this.getName() + ", comment=" + this.getComment() + ", numericId=" + this.numericId + ")";
    }
}
