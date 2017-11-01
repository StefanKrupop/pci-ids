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
package net.exodusproject.pciid.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.exodusproject.pciid.model.Device;
import net.exodusproject.pciid.model.Subsystem;
import net.exodusproject.pciid.model.Vendor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 */
public class DatabaseFileParserTest {

    public DatabaseFileParserTest() {
    }

    @Test
    public void testParseDatabaseFile() throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("pci.ids");

        DatabaseFileParser instance = new DatabaseFileParser();
        List<Vendor> result = instance.parseDatabaseFile(is);

        assertEquals(result.size(), 1);

        Vendor v = result.get(0);
        assertNotNull(v);
        assertEquals("001c", v.getId());
        assertEquals("PEAK-System Technik GmbH", v.getName());
        assertEquals("Comment on vendor", v.getComment());

        Device d = v.getDevices().get("0001");
        assertNotNull(d);
        assertEquals("0001", d.getId());
        assertEquals("PCAN-PCI CAN-Bus controller", d.getName());
        assertEquals("Comment on device", d.getComment());

        Subsystem s1 = d.getSubsystems().stream()
                .filter(s -> (s.getVendorId().equals("001c") && s.getId().equals("0004")))
                .findFirst()
                .get();

        assertEquals("0004", s1.getId());
        assertEquals("001c", s1.getVendorId());
        assertEquals("2 Channel CAN Bus SJC1000", s1.getName());
        assertEquals("Comment on subsystem", s1.getComment());

        Subsystem s2 = d.getSubsystems().stream()
                .filter(s -> (s.getVendorId().equals("001c") && s.getId().equals("0005")))
                .findFirst()
                .get();

        assertEquals("0005", s2.getId());
        assertEquals("001c", s2.getVendorId());
        assertEquals("2 Channel CAN Bus SJC1000 MOCK", s2.getName());
        assertNull(s2.getComment());
    }

    @Test
    public void testParseVendorLine() throws Exception {
        String line = "0059  Tiger Jet Network Inc. (Wrong ID)";

        DatabaseFileParser instance = new DatabaseFileParser();
        Vendor result = instance.parseVendorLine(line);

        assertEquals("0059", result.getId());
        assertEquals("Tiger Jet Network Inc. (Wrong ID)", result.getName());
    }

    @Test(expected = IOException.class)
    public void testParseVendorLine_malformedId() throws Exception {
        String line = "005  Tiger Jet Network Inc. (Wrong ID)";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseVendorLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseVendorLine_malformedPrefix() throws Exception {
        String line = "\t0059  Tiger Jet Network Inc. (Wrong ID)";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseVendorLine(line);
    }

    @Test
    public void testParseDeviceLine() throws Exception {
        String line = "\t7801  WinTV HVR-1800 MCE";

        DatabaseFileParser instance = new DatabaseFileParser();
        Device result = instance.parseDeviceLine(line);

        assertEquals("7801", result.getId());
        assertEquals("WinTV HVR-1800 MCE", result.getName());
    }

    @Test(expected = IOException.class)
    public void testParseDeviceLine_malformedId() throws Exception {
        String line = "\t780  WinTV HVR-1800 MCE";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseDeviceLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseDeviceLine_malformedPrefix1() throws Exception {
        String line = "\t\t7801  WinTV HVR-1800 MCE";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseDeviceLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseDeviceLine_malformedPrefix2() throws Exception {
        String line = "7801  WinTV HVR-1800 MCE";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseDeviceLine(line);
    }

    @Test
    public void testParseSubsystemLine() throws Exception {
        String line = "\t\t001c 0004  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        Subsystem result = instance.parseSubsystemLine(line);

        assertEquals("001c", result.getVendorId());
        assertEquals("0004", result.getId());
        assertEquals("2 Channel CAN Bus SJC1000", result.getName());
    }

    @Test(expected = IOException.class)
    public void testParseSubsystemLine_malformedId() throws Exception {
        String line = "\t\t001c 000  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseSubsystemLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseSubsystemLine_malformedVendorId() throws Exception {
        String line = "\t\t001 0004  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseSubsystemLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseSubsystemLine_malformedPrefix1() throws Exception {
        String line = "\t001c 0004  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseSubsystemLine(line);
    }

    @Test(expected = IOException.class)
    public void testParseSubsystemLine_malformedPrefix2() throws Exception {
        String line = "001c 0004  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        instance.parseSubsystemLine(line);
    }

    @Test
    public void testDetermineLineType_COMMENT() {
        String line = "# This is a comment";

        DatabaseFileParser instance = new DatabaseFileParser();
        DatabaseFileParser.LineType result = instance.determineLineType(line);

        assertEquals(DatabaseFileParser.LineType.COMMENT, result);
    }

    @Test
    public void testDetermineLineType_DEVICE() {
        String line = "\t7801  WinTV HVR-1800 MCE";

        DatabaseFileParser instance = new DatabaseFileParser();
        DatabaseFileParser.LineType result = instance.determineLineType(line);

        assertEquals(DatabaseFileParser.LineType.DEVICE, result);
    }

    @Test
    public void testDetermineLineType_SUBSYSTEM() {
        String line = "\t\t001c 0004  2 Channel CAN Bus SJC1000";

        DatabaseFileParser instance = new DatabaseFileParser();
        DatabaseFileParser.LineType result = instance.determineLineType(line);

        assertEquals(DatabaseFileParser.LineType.SUBSYSTEM, result);
    }

    @Test
    public void testDetermineLineType_VENDOR() {
        String line = "0059  Tiger Jet Network Inc. (Wrong ID)";

        DatabaseFileParser instance = new DatabaseFileParser();
        DatabaseFileParser.LineType result = instance.determineLineType(line);

        assertEquals(DatabaseFileParser.LineType.VENDOR, result);
    }
}
