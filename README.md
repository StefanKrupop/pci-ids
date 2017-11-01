# Java PCI IDs database

A library to parse the PCI IDs database file and make its contents available.
See [The PCI ID Repository](https://pci-ids.ucw.cz/) for details.

## Add as project dependency

To use this library in your project simply add this to your Maven pom.xml.

```xml
<dependency>
    <groupId>net.exodusproject</groupId>
    <artifactId>iommu-database</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

Please note that this JAR is not yet available via a Maven repository, thus you need to clone this
repository and build the library yourself.

```bash
git clone git@github.com:marandus/pci-ids.git
cd pci-ids
mvn install
```

## Usage

First, a new database instance must be created and filled with PCI ID information.
The easiest way to do this is by downloading the pci.ids file from the default location.

```java
PciIdsDatabase db = new PciIdsDatabase();
db.loadRemote();
```

If no internet access is available, the pci.ids file can either be retrieved from a custom URI or a
local file.

```java
// Load pci.ids from custom URI
PciIdsDatabase db = new PciIdsDatabase();
URI customUri = URI.create("http://example.com/pci.ids");
db.loadRemote(customUri);

// Load pci.ids from local file
PciIdsDatabase db = new PciIdsDatabase();
InputStream is = this.getClass().getClassLoader().getResourceAsStream("pci.ids");
db.loadStream(is);
```

Once a database file was loaded and processed by the `PciIdsDatabase` class, the database is ready
to be queried. For example to retrieve all devices manufactured by Intel Corporation
(Vendor ID: 8086), use the following code.

```java
List<Device> devs = db.findAllDevices("8086");
```

The whole database is read-only, it is not possible to modify any maps, lists or entries accessible
from outside the library.

## Contribution guidelines

* Fork the repository and provide pull requests with your changes
* Use verbose commit messages to tell what you have done
* Make sure your code builds successfully and is stable

## License

    Copyright 2017 Thomas Rix
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
