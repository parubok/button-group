![Java CI with Maven](https://github.com/parubok/button-group/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/button-group/blob/master/LICENSE)

# button-group

This small library provides a subclass of `javax.swing.ButtonGroup` which adds the following functionality:
- Type-safety via generics.
- Auto-selects 1st added button.
- Listeners.
- Mnemonics.
- Access to the buttons by index.
- Implements `Iterable` to iterate over buttons in the group.

Example:
```java
import javax.swing.JRadioButton;

import io.github.parubok.button.group.KButtonGroup;

var radio1 = new JRadioButton("1");
var radio2 = new JRadioButton("2");
var group = new KButtonGroup(radio1, radio2);
```

This library is packaged as a Java 9 module `io.github.parubok.button.group` (with a single dependency on a system module `java.desktop`). 

This project has no external dependencies (except JUnit 5, for testing).

Requires Java 11 or later.
