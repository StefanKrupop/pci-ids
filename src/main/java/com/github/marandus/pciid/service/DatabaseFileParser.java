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
package com.github.marandus.pciid.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import com.github.marandus.pciid.model.Device;
import com.github.marandus.pciid.model.Subsystem;
import com.github.marandus.pciid.model.Vendor;
import org.apache.commons.lang3.StringUtils;

/**
 * Parser class for contents of the PCI IDs file.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 1.0
 */
@Slf4j
class DatabaseFileParser {

    /**
     * RegEx pattern used to parse database file lines of type {@link LineType#VENDOR VENDOR}.
     */
    private final Pattern VENDOR_PATTERN = Pattern.compile("^([0-9a-f]{4})\\s+(.+)$");

    /**
     * RegEx pattern used to parse database file lines of type {@link LineType#DEVICE DEVICE}.
     */
    private final Pattern DEVICE_PATTERN = Pattern.compile("^\\t([0-9a-f]{4})\\s+(.+)$");

    /**
     * RegEx pattern used to parse database file lines of type {@link LineType#SUBSYSTEM SUBSYSTEM}.
     */
    private final Pattern SUBSYS_PATTERN = Pattern.compile("^\\t\\t([0-9a-f]{4})\\s([0-9a-f]{4})\\s+(.+)$");

    /**
     * Current comment from database file will be stored here and applied to the next created entry.
     */
    private String currentComment = null;

    /**
     * Process the contents of the PCI IDs database file. The complete file is read and parsed into
     * a list of {@link Vendor} objects. Each object is filled with the corresponding {@link Device}
     * and {@link Subsystem} objects.
     *
     * @param is Input stream holding the database file contents
     * @return List of vendors read from the database file
     * @throws IOException if the database file is malformed or otherwise unprocessable
     */
    List<Vendor> parseDatabaseFile(InputStream is) throws IOException {
        ArgumentValidator.requireNonNull(is, "PCI IDs database InputStream");

        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            List<Vendor> rv = new LinkedList<>();

            Vendor currentVendor = null;
            Device currentDevice = null;

            String line = br.readLine();
            while (line != null) {
                // If current line is blank, discard previous comments and continue
                // This is used to discard the file header
                if (StringUtils.isBlank(line)) {
                    this.currentComment = null;
                    line = br.readLine();
                    continue;
                }

                switch (this.determineLineType(line)) {
                    case COMMENT:
                        if (this.currentComment != null) {
                            // This is a multiline comment, append the current comment to the previous line
                            this.currentComment += "\n" + line.substring(1).trim();
                        }
                        else {
                            this.currentComment = line.substring(1).trim();
                        }
                        break;
                    case DEVICE:
                        // Handle previous device
                        if (currentDevice != null) {
                            if (currentVendor == null) {
                                throw new IOException("Encountered unexpected device line");
                            }
                            // Add the previous device to the current vendor
                            currentVendor.addDevice(currentDevice);
                        }
                        currentDevice = this.parseDeviceLine(line);
                        break;
                    case SUBSYSTEM:
                        if (currentDevice == null) {
                            throw new IOException("Encountered unexpected subsystem line");
                        }
                        currentDevice.addSubsystem(this.parseSubsystemLine(line));
                        break;
                    case VENDOR:
                        // Handle previous vendor
                        if (currentVendor != null) {
                            // Is there a device left to be added to the previous vendor?
                            if (currentDevice != null) {
                                currentVendor.addDevice(currentDevice);
                                currentDevice = null;
                            }
                            // Add the previous vendor to the return list
                            rv.add(currentVendor);
                        }
                        currentVendor = this.parseVendorLine(line);
                        break;
                    default:
                        throw new IOException("Encountered invalid line format in database file");
                }

                line = br.readLine();
            }

            if (currentVendor != null) {
                // Is there a device left to be added to the previous vendor?
                if (currentDevice != null) {
                    currentVendor.addDevice(currentDevice);
                }
                rv.add(currentVendor);
            }

            return rv;
        }
        catch (IOException | RuntimeException ex) {
            throw new IOException("Error while parsing database file", ex);
        }
        finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }

            is.close();
        }
    }

    /**
     * Verify syntax of the current line to be a vendor entry and parse contents into a
     * {@link Vendor} object.
     *
     * @param line Current line from database file
     * @return Vendor object filled with info from line
     * @throws IOException if line is malformed or otherwise unprocessable
     */
    protected Vendor parseVendorLine(String line) throws IOException {
        Matcher m = this.VENDOR_PATTERN.matcher(line);

        if (m.matches()) {
            String id = m.group(1);
            String name = m.group(2);

            Vendor rv = new Vendor(id, name, this.currentComment);
            this.currentComment = null;

            return rv;
        }
        else {
            throw new IOException("Unable to process vendor line: [" + line + "]");
        }
    }

    /**
     * Verify syntax of the current line to be a device entry and parse contents into a
     * {@link Device} object.
     *
     * @param line Current line from database file
     * @return Device object filled with info from line
     * @throws IOException if line is malformed or otherwise unprocessable
     */
    protected Device parseDeviceLine(String line) throws IOException {
        Matcher m = this.DEVICE_PATTERN.matcher(line);

        if (m.matches()) {
            String id = m.group(1);
            String name = m.group(2);

            Device rv = new Device(id, name, this.currentComment);
            this.currentComment = null;

            return rv;
        }
        else {
            throw new IOException("Unable to process device line: [" + line + "]");
        }
    }

    /**
     * Verify syntax of the current line to be a subsystem entry and parse contents into a
     * {@link Subsystem} object.
     *
     * @param line Current line from database file
     * @return Subsystem object filled with info from line
     * @throws IOException if line is malformed or otherwise unprocessable
     */
    protected Subsystem parseSubsystemLine(String line) throws IOException {
        Matcher m = this.SUBSYS_PATTERN.matcher(line);

        if (m.matches()) {
            String id = m.group(2);
            String name = m.group(3);
            String vendorId = m.group(1);

            Subsystem rv = new Subsystem(id, name, this.currentComment, vendorId);
            this.currentComment = null;

            return rv;
        }
        else {
            throw new IOException("Unable to process subsystem line: [" + line + "]");
        }
    }

    /**
     * Determine the type of the current line. Possible return values are:
     * <p>
     * <ul>
     * <li>COMMENT: Starts with a <tt>#</tt> character</li>
     * <li>DEVICE: Starts with exactly one tabulator character</li>
     * <li>SUBSYSTEM: Starts with exactly two tabulator character</li>
     * <li>VENDOR: Has no prefix</li>
     * </ul>
     *
     * @param line Current line from database file
     * @return Type of the current line determined by line prefix
     */
    protected LineType determineLineType(String line) {
        if (line.startsWith("#")) {
            return LineType.COMMENT;
        }
        else if (line.startsWith("\t\t")) {
            return LineType.SUBSYSTEM;
        }
        else if (line.startsWith("\t")) {
            return LineType.DEVICE;
        }
        else {
            return LineType.VENDOR;
        }
    }

    /**
     * Possible line types found in the PCI IDs database file.
     */
    protected static enum LineType {
        COMMENT,
        DEVICE,
        SUBSYSTEM,
        VENDOR;
    }
}
