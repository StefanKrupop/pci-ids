/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.exodusproject.pciid.model;

import java.util.HashMap;
import java.util.Map;
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
public class Vendor {

    private final String id;
    private final String name;
    private final String comment;
    private final Map<String, Device> devices;
    
    public Vendor(String id, String name, String comment) {
        ArgumentValidator.requireStringLength(id, 4, ArgumentValidator.NumberCompare.EQUAL, "Vendor ID");
        ArgumentValidator.requireNonBlank(name, "Vendor name");
        
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.devices = new HashMap<>();
    }
    
    public void addDevice(Device device) {
        ArgumentValidator.requireNonNull(device, "Vendor device");
        
        this.devices.put(device.getId(), device);
    }
}
