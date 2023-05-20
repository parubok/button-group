package io.github.parubok.button.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class KButtonGroupTest {
    @Test
    public void basicTest() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JRadioButton rb1 = new JRadioButton("rb1");
            JRadioButton rb2 = new JRadioButton("rb2");
            var group = new KButtonGroup<>(List.of(rb1, rb2));
            Assertions.assertTrue(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
            Assertions.assertEquals(group, rb1.getModel().getGroup());
            Assertions.assertEquals(group, rb2.getModel().getGroup());
            Assertions.assertEquals(0, group.getSelectedIndex());
            Assertions.assertEquals(2, group.getButtonCount());
            Assertions.assertEquals(rb1, group.getSelectedButton());
            Assertions.assertEquals(List.of(rb1, rb2), group.getButtons());
            Assertions.assertEquals(rb1, group.getButton(0));
            Assertions.assertEquals(rb2, group.getButton(1));
            Assertions.assertEquals(1, group.getLastIndex());

            rb2.setSelected(true);
            Assertions.assertFalse(rb1.isSelected());
            Assertions.assertTrue(rb2.isSelected());

            group.setSelectedIndex(0);
            Assertions.assertTrue(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
        });
    }

    @Test
    public void allSelected() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JRadioButton rb1 = new JRadioButton("rb1", true);
            JRadioButton rb2 = new JRadioButton("rb2", true);
            var group = new KButtonGroup<>(false);
            Assertions.assertEquals(Collections.emptyList(), group.getButtons());
            group.addButtons(rb1, rb2);
            Assertions.assertEquals(List.of(rb1, rb2), group.getButtons());
            Assertions.assertTrue(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
        });
    }

    @Test
    public void allUnselected() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("rb1", false);
            var rb2 = new JRadioButton("rb2", false);
            var group = new KButtonGroup<>(false);
            group.addButtons(rb1, rb2);
            Assertions.assertFalse(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
            Assertions.assertEquals(-1, group.getSelectedIndex());
            Assertions.assertNull(group.getSelectedButton());
        });
    }

    @Test
    public void addItemListener() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("rb1");
            var rb2 = new JRadioButton("rb2");
            var rb3 = new JRadioButton("rb3");
            var group = new KButtonGroup<>(rb1, rb2, rb3);
            var events = new ArrayList<KButtonGroupEvent<JRadioButton>>();
            group.addListener(events::add);
            rb2.setSelected(true);
            Assertions.assertEquals(1, events.size());
            KButtonGroupEvent<JRadioButton> event = events.get(0);
            Assertions.assertEquals(rb2, event.getSelectedButton());
            Assertions.assertNull(event.getPrevSelectedButton());
            Assertions.assertEquals(group, event.getSource());

            rb2.setSelected(true);
            Assertions.assertEquals(1, events.size());

            rb3.setSelected(true);
            Assertions.assertEquals(2, events.size());
            event = events.get(1);
            Assertions.assertEquals(rb3, event.getSelectedButton());
            Assertions.assertEquals(rb2, event.getPrevSelectedButton());
            Assertions.assertEquals(group, event.getSource());
        });
    }

    @Test
    public void addItemListener_validState() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("rb1");
            var rb2 = new JRadioButton("rb2");
            var rb3 = new JRadioButton("rb3");
            var group = new KButtonGroup<>(rb1, rb2, rb3);
            rb2.setSelected(true);
            var c = new AtomicInteger(0);
            KButtonGroupListener<JRadioButton> listener = new KButtonGroupListener<JRadioButton>() {
                @Override
                public void onSelectionChange(KButtonGroupEvent<JRadioButton> e) {
                    c.incrementAndGet();
                    Assertions.assertEquals(e.getSelectedButton(), rb1);
                    Assertions.assertEquals(e.getPrevSelectedButton(), rb2);
                    Assertions.assertTrue(rb1.isSelected());
                    Assertions.assertFalse(rb2.isSelected());
                    Assertions.assertFalse(rb3.isSelected());
                }
            };
            group.addListener(listener);
            rb1.setSelected(true);
            Assertions.assertEquals(1, c.get()); // make sure the listener has been called exactly once
            group.removeListener(listener);

            listener = new KButtonGroupListener<JRadioButton>() {
                @Override
                public void onSelectionChange(KButtonGroupEvent<JRadioButton> e) {
                    c.incrementAndGet();
                    Assertions.assertEquals(e.getSelectedButton(), rb3);
                    Assertions.assertEquals(e.getPrevSelectedButton(), rb1);
                    Assertions.assertFalse(rb1.isSelected());
                    Assertions.assertFalse(rb2.isSelected());
                    Assertions.assertTrue(rb3.isSelected());
                }
            };
            group.addListener(listener);
            rb3.setSelected(true);
            Assertions.assertEquals(2, c.get());
        });
    }

    @Test
    public void clearSelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("1");
            var rb2 = new JRadioButton("2");
            var group = new KButtonGroup<>(rb1, rb2);
            Assertions.assertTrue(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
            var events = new ArrayList<KButtonGroupEvent<JRadioButton>>();
            group.addListener(events::add);
            group.clearSelection();
            Assertions.assertFalse(rb1.isSelected());
            Assertions.assertFalse(rb2.isSelected());
            Assertions.assertEquals(0, events.size());
//            KButtonGroupEvent<JRadioButton> event = events.get(0);
//            Assertions.assertEquals(rb1, event.getPrevSelectedButton());
//            Assertions.assertNull(event.getSelectedButton());
        });
    }

    @Test
    public void setMnemonics() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("abc");
            var rb2 = new JRadioButton("abc");
            var rb3 = new JRadioButton("abc");
            var group = new KButtonGroup<>(rb1, rb2, rb3);
            group.setMnemonics();
            Assertions.assertEquals(KeyEvent.VK_A, rb1.getMnemonic());
            Assertions.assertEquals(KeyEvent.VK_B, rb2.getMnemonic());
            Assertions.assertEquals(KeyEvent.VK_C, rb3.getMnemonic());
        });
    }

    @Test
    public void iterator() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("1");
            var rb2 = new JRadioButton("2");
            var group = new KButtonGroup<>(rb1, rb2);
            Iterator<JRadioButton> iter = group.iterator();
            Assertions.assertTrue(iter.hasNext());
            Assertions.assertEquals(rb1, iter.next());
            Assertions.assertTrue(iter.hasNext());
            Assertions.assertEquals(rb2, iter.next());
            Assertions.assertFalse(iter.hasNext());
            Assertions.assertThrows(NoSuchElementException.class, iter::next); // 1
            Assertions.assertThrows(NoSuchElementException.class, iter::next); // 2
        });
    }

    @Test
    public void iteratorRemove() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var rb1 = new JRadioButton("1");
            var rb2 = new JRadioButton("2");
            var group = new KButtonGroup<>(rb1, rb2);
            Iterator<JRadioButton> iter = group.iterator();
            Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
        });
    }
}
