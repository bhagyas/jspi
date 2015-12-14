##JSPI - Java Internet Printing Protocol Implementation

This library enables you to add remote printing capabilities to your Java applications running on servers and other client applications.


###Usage

####Adding dependency using Apache Maven

1. Clone the project

	Execute `git clone https://github.com/bhagyas/jspi`

2. Install artifact on your Maven repository

	Change into `jspi-core` project and execute `mvn clean install` or `mvn deploy`. Note that you might need to skip the tests when building the project.

3. Add as a dependency

   Add the following on your `pom.xml`

	```
	<dependency>
      <groupId>com.xinterium.jspi</groupId>
      <artifactId>jspi-core</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
    ```


###API and Examples

Examples are available in the `src/test` directory along with the tests.

#### Notes from the History : jspi
This project is based on the original project exported from code.google.com/p/jspi (LGPL)

###Authors
- Bhagya Silva ([https://about.me/bhagyas](https://about.me/bhagyas))

Original library author information can be found on the Google Code project location.
