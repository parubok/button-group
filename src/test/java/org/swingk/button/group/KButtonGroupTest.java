package org.swingk.button.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JRadioButton;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

        rb2.setSelected(true);
        Assertions.assertFalse(rb1.isSelected());
        Assertions.assertTrue(rb2.isSelected());

        group.setSelectedIndex(0);
        Assertions.assertTrue(rb1.isSelected());
        Assertions.assertFalse(rb2.isSelected());
    }

    @Test
    public void allSelected() {
        JRadioButton rb1 = new JRadioButton("rb1", true);
        JRadioButton rb2 = new JRadioButton("rb2", true);
        var group = new KButtonGroup<>(false);
        group.addButton(rb1);
        group.addButton(rb2);
        Assertions.assertTrue(rb1.isSelected());
        Assertions.assertFalse(rb2.isSelected());
    }

    @Test
    public void allUnselected() {
        JRadioButton rb1 = new JRadioButton("rb1", false);
        JRadioButton rb2 = new JRadioButton("rb2", false);
        var group = new KButtonGroup<>(false);
        group.addButtons(rb1, rb2);
        Assertions.assertFalse(rb1.isSelected());
        Assertions.assertFalse(rb2.isSelected());
        Assertions.assertEquals(-1, group.getSelectedIndex());
        Assertions.assertNull(group.getSelectedButton());
    }

    @Test
    public void addItemListener() {
        JRadioButton rb1 = new JRadioButton("rb1");
        JRadioButton rb2 = new JRadioButton("rb2");
        JRadioButton rb3 = new JRadioButton("rb3");
        var group = new KButtonGroup<>(rb1, rb2, rb3);
        var events = new ArrayList<ItemEvent>();
        group.addItemListener(events::add);
        rb2.setSelected(true);
        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals(rb2, events.get(0).getItem());
        Assertions.assertEquals(rb2, events.get(0).getSource());
        Assertions.assertEquals(ItemEvent.SELECTED, events.get(0).getStateChange());

        rb2.setSelected(true);
        Assertions.assertEquals(1, events.size());

        rb3.setSelected(true);
        Assertions.assertEquals(2, events.size());
        Assertions.assertEquals(rb3, events.get(1).getItem());
        Assertions.assertEquals(rb3, events.get(1).getSource());
        Assertions.assertEquals(ItemEvent.SELECTED, events.get(1).getStateChange());
    }

    @Test
    public void setMnemonics() {
        JRadioButton rb1 = new JRadioButton("abc");
        JRadioButton rb2 = new JRadioButton("abc");
        JRadioButton rb3 = new JRadioButton("abc");
        var group = new KButtonGroup<>(rb1, rb2, rb3);
        group.setMnemonics();
        Assertions.assertEquals(KeyEvent.VK_A, rb1.getMnemonic());
        Assertions.assertEquals(KeyEvent.VK_B, rb2.getMnemonic());
        Assertions.assertEquals(KeyEvent.VK_C, rb3.getMnemonic());
    }
}
