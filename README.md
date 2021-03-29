![Java CI with Maven](https://github.com/parubok/button-group/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/button-group/blob/master/LICENSE)

# button-group

Extension of `javax.swing.ButtonGroup` which adds the following functionality:
- Type-safety via generics.
- Auto-selects 1st added button.
- Listeners.
- Mnemonics.
- Access to the buttons by index.

This library is packaged as a Java 9 module `org.swingk.button.group` (with a single dependency on a system module `java.desktop`). 

This project has no external dependencies (except JUnit 5, for testing).

Requires Java 11 or later.
