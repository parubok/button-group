package org.swingk.button.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JRadioButton;
import java.util.List;

public class KButtonGroupTest {
    @Test
    public void basicTest_1() {
        JRadioButton rb1 = new JRadioButton("rb1");
        JRadioButton rb2 = new JRadioButton("rb2");
        var group = new KButtonGroup<>(List.of(rb1, rb2));
        Assertions.assertTrue(rb1.isSelected());
        Assertions.assertFalse(rb2.isSelected());
        Assertions.assertEquals(0, group.getSelectedIndex());
        Assertions.assertEquals(2, group.getButtonCount());
        Assertions.assertEquals(rb1, group.getSelectedButton());
        Assertions.assertEquals(List.of(rb1, rb2), group.getButtons());
        Assertions.assertEquals(rb1, group.getButton(0));
        Assertions.assertEquals(rb2, group.getButton(1));
    }
}
