/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.exodusproject.pciid.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.exodusproject.pciid.service.ArgumentValidator;

/**
 * TODO: Missing class level javadoc
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Getter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class Device {
    
    private final String id;
    private final String name;
    private final String comment;
    
    public Device(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 4, ArgumentValidator.NumberCompare.EQUAL, "Device ID");
        ArgumentValidator.requireNonBlank(name, "Device name");
        
        this.id = id;
        this.name = name;
        this.comment = comment;
    }
}
