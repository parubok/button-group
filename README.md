![Java CI with Maven](https://github.com/parubok/button-group/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/button-group/blob/master/LICENSE)
[![Latest Version](https://img.shields.io/maven-central/v/io.github.parubok/button-group)](https://search.maven.org/search?q=a:button-group)
[![javadoc](https://javadoc.io/badge2/io.github.parubok/button-group/javadoc.svg)](https://javadoc.io/doc/io.github.parubok/button-group)

# button-group

This small library provides a subclass of `javax.swing.ButtonGroup` which adds the following functionality:
- Type-safety via generics.
- Selects first added button by default.
- Selection change listeners (see `KButtonGroupListener`).
- Allows to assign button mnemonics.
- Access to the buttons by index.
- Implements `Iterable` to iterate over buttons in the group.

Example:
```java
import javax.swing.JRadioButton;

import io.github.parubok.button.group.KButtonGroup;
import io.github.parubok.button.group.KButtonGroupListener;
import io.github.parubok.button.group.KButtonGroupEvent;

var radio1 = new JRadioButton("Option A");
var radio2 = new JRadioButton("Option B");
var radio3 = new JRadioButton("Option C");
var group = new KButtonGroup<>(radio1, radio2, radio3);
group.addListener(new KButtonGroupListener<JRadioButton>() {
            @Override
            public void onSelectionChange(KButtonGroupEvent<JRadioButton> e) {
                System.out.println("Button selected: " + e.getSelectedButton());
                System.out.println("Button unselected: " + e.getPrevSelectedButton());
            }
        });
```

This library is packaged as a Java 9 module `io.github.parubok.button.group` (with a single dependency on a system module `java.desktop`). 

This project has no external dependencies (except JUnit 5, for testing).

Requires Java 11 or later.

#### Maven

Add this snippet to the pom.xml `dependencies` section:

```xml
<dependency>
    <groupId>io.github.parubok</groupId>
    <artifactId>button-group</artifactId>
    <version>1.7</version>
</dependency>
```