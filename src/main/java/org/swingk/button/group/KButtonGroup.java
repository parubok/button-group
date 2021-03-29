package org.swingk.button.group;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Extension of {@link ButtonGroup} which adds the following functionality:
 * <ul>
 * <li>Type-safety via generics.</li>
 * <li>Auto-selects 1st added button.</li>
 * <li>Listeners.</li>
 * <li>Mnemonics.</li>
 * </ul>
 */
public class KButtonGroup<K extends AbstractButton> extends ButtonGroup {

    private final boolean autoSelectFirstButton;
    private final List<ItemListener> listeners = new ArrayList<>();
    private final ItemListener buttonListener = e -> {
        AbstractButton b = (AbstractButton) e.getSource();
        if (b.isSelected()) {
            listeners.forEach(listener -> listener.itemStateChanged(e));
        }
    };

    @SafeVarargs
    public KButtonGroup(K... buttons) {
        this(List.of(buttons));
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
        this.autoSelectFirstButton = autoSelectFirstButton;
    }

    @SafeVarargs
    public static <K extends AbstractButton> void group(K... buttons) {
        of(buttons);
    }

    @SafeVarargs
    public static <K extends AbstractButton> KButtonGroup<K> of(K... buttons) {
        return new KButtonGroup<>(buttons);
    }

    /**
     * @param button Button to add to the group. Not null.
     */
    public void addButton(K button) {
        Objects.requireNonNull(button);
        add(button);
        if (autoSelectFirstButton && getButtonCount() == 1) {
            button.setSelected(true);
        }
        button.addItemListener(buttonListener);
    }

    /**
     * @deprecated This method is not type-safe. Use {@link #addButton(AbstractButton)} instead.
     */
    @Deprecated
    public void add(AbstractButton button) {
        super.add(button);
    }

    /**
     * An event will be fired when a state of the items is changed.
     */
    public void addItemListener(ItemListener itemListener) {
        Objects.requireNonNull(itemListener);
        listeners.add(itemListener);
    }

    /**
     * TODO: Javadoc.
     * TODO: Use {@link AbstractButton#setMnemonic(int)}.
     */
    public void setMnemonics() {
        var usedChars = new HashSet<Character>();
        for (AbstractButton button : buttons) {
            String text = button.getText();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                // mnemonics underscore visually conflicts with 'y' letter
                if (!usedChars.contains(c) && !Character.isWhitespace(c) && c != 'y') {
                    button.setMnemonic(c);
                    usedChars.add(c);
                    break;
                }
            }
        }
    }

    public K getButton(int index) {
        return (K) buttons.get(index);
    }

    public K getSelectedButton() {
        return getButton(getSelectedIndex());
    }

    public int getSelectedIndex() {
        for (int i = 0; i < buttons.size(); i++) {
            AbstractButton b = buttons.get(i);
            if (b.isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int index) {
        buttons.get(index).setSelected(true);
    }

    /**
     * @return Buttons of this group.
     */
    public List<K> getButtons() {
        final int s = buttons.size();
        var buttonList = new ArrayList<K>(s);
        for (AbstractButton button : buttons) {
            buttonList.add((K) button);
        }
        return buttonList;
    }
}
