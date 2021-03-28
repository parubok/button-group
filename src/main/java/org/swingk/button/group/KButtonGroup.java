package org.swingk.button.group;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Button group which, comparing to the standard {@link ButtonGroup}, adds the following functionality:
 * <ul>
 * <li>Type-safety via generics.</li>
 * <li>Auto-selects 1st added button.</li>
 * <li>Listeners.</li>
 * <li>Mnemonics.</li>
 * </ul>
 */
public class KButtonGroup<K extends AbstractButton> {

    private static class SwingButtonGroup extends ButtonGroup {
        Vector<AbstractButton> getButtons() {
            return buttons;
        }
    }

    private final SwingButtonGroup group;
    private final boolean autoSelectFirstButton;
    private final List<ItemListener> listeners = new ArrayList<>();
    private final ItemListener buttonListener = e -> {
        AbstractButton b = (AbstractButton) e.getSource();
        if (b.isSelected()) {
            listeners.forEach(listener -> listener.itemStateChanged(e));
        }
    };

    @SafeVarargs
    public static <K extends AbstractButton> void group(K... buttons) {
        of(buttons);
    }

    @SafeVarargs
    public static <K extends AbstractButton> KButtonGroup<K> of(K... buttons) {
        return new KButtonGroup<>(List.of(buttons));
    }

    /**
     * Constructs group with the specified buttons.
     *
     * @param buttons Buttons to add to the group.
     */
    public KButtonGroup(Collection<K> buttons) {
        this(true);
        buttons.forEach(this::addButton);
    }

    /**
     * Constructs an empty group.
     *
     * @param autoSelectFirstButton If true, the first added button will be automatically selected via
     * {@link AbstractButton#setSelected(boolean)}.
     */
    public KButtonGroup(boolean autoSelectFirstButton) {
        super();
        this.group = new SwingButtonGroup();
        this.autoSelectFirstButton = autoSelectFirstButton;
    }

    /**
     * @param button Button to add to the group. Not null.
     */
    public void addButton(K button) {
        Objects.requireNonNull(button);
        group.add(button);
        if (autoSelectFirstButton && group.getButtonCount() == 1) {
            button.setSelected(true);
        }
        button.addItemListener(buttonListener);
    }

    public void add(K button) {
        addButton(button);
    }

    /**
     * An event will be fired when a state of the items is changed.
     */
    public void addItemListener(ItemListener itemListener) {
        Objects.requireNonNull(itemListener);
        listeners.add(itemListener);
    }

    /**
     * TODO: Javadoc
     * TODO: migrate to using {@link AbstractButton#setMnemonic(int)}
     */
    public void setMnemonics() {
        Vector<AbstractButton> buttons = group.getButtons();
        var usedChars = new HashSet<Character>();
        for (int j = 0; j < buttons.size(); j++) {
            AbstractButton b = buttons.get(j);
            String text = b.getText();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                // mnemonics underscore visually conflicts with 'y' letter
                if (!usedChars.contains(c) && !Character.isWhitespace(c) && c != 'y') {
                    b.setMnemonic(c);
                    usedChars.add(c);
                    break;
                }
            }
        }
    }

    public K getButton(int index) {
        return (K) group.getButtons().get(index);
    }

    public K getSelectedButton() {
        return getButton(getSelectedIndex());
    }

    public int getButtonCount() {
        return group.getButtonCount();
    }

    public int getSelectedIndex() {
        Vector<AbstractButton> buttons = group.getButtons();
        for (int i = 0; i < buttons.size(); i++) {
            AbstractButton b = buttons.get(i);
            if (b.isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int index) {
        group.getButtons().get(index).setSelected(true);
    }

    public boolean isEnabled(int buttonIndex) {
        return group.getButtons().get(buttonIndex).isEnabled();
    }

    /**
     * @return Buttons of this group.
     */
    public List<K> getButtons() {
        Vector<AbstractButton> buttons = group.getButtons();
        var buttonList = new ArrayList<K>(buttons.size());
        for (int i = 0; i < buttons.size(); i++) {
            buttonList.add((K) buttons.get(i));
        }
        return buttonList;
    }
}
