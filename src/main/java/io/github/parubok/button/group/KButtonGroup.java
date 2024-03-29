package io.github.parubok.button.group;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.event.EventListenerList;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Extension of Swing {@link ButtonGroup} which adds the following functionality:
 * <ul>
 * <li>Type-safety via generics.</li>
 * <li>Auto-selects 1st added button.</li>
 * <li>Group listeners.</li>
 * <li>Mnemonics.</li>
 * <li>Access to the buttons by index.</li>
 * <li>Implements {@link Iterable} to iterate over buttons in the group.</li>
 * </ul>
 *
 * @param <K> Type of the buttons in the group.
 * @implNote Adds {@link ItemListener} to the buttons in the group.
 * @see javax.swing.ButtonModel#setGroup(ButtonGroup)
 */
public class KButtonGroup<K extends AbstractButton> extends ButtonGroup implements Iterable<K> {

    private final boolean autoSelectFirstButton;
    private final EventListenerList listenerList = new EventListenerList();
    private K lastSelectedButton;

    private final ItemListener buttonListener = e -> {
        var b = (K) e.getSource();
        if (b.isSelected()) {

            // assert that only one button is selected:
            for (AbstractButton button : buttons) {
                assert button.equals(b) || !button.isSelected();
            }

            if (listenerList.getListenerCount() > 0) {
                var event = new KButtonGroupEvent<>(KButtonGroup.this, b, lastSelectedButton);
                for (KButtonGroupListener<K> listener : listenerList.getListeners(KButtonGroupListener.class)) {
                    listener.onSelectionChange(event);
                }
                for (ItemListener itemListener : listenerList.getListeners(ItemListener.class)) {
                    itemListener.itemStateChanged(e);
                }
            }
            lastSelectedButton = b;
        }
    };

    /**
     * Constructs group with the specified buttons. The first added button will be automatically selected.
     *
     * @param buttons Buttons to add to the group.
     */
    @SafeVarargs
    public KButtonGroup(K... buttons) {
        this(List.of(buttons));
    }

    /**
     * Constructs group with the specified buttons. The first added button will be automatically selected.
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

    /**
     * @param button Button to add to the group. Not null.
     */
    public void addButton(K button) {
        add(button);
    }

    @SafeVarargs
    public final void addButtons(K... buttons) {
        for (K button : buttons) {
            addButton(button);
        }
    }

    /**
     * @deprecated This method is not type-safe. Use {@link #addButton(AbstractButton)} instead.
     */
    @Deprecated
    @Override
    public void add(AbstractButton button) {
        Objects.requireNonNull(button);
        if (autoSelectFirstButton && getButtonCount() == 0) {
            button.setSelected(true);
        }
        super.add(button);
        button.addItemListener(buttonListener);
    }

    @Override
    public void remove(AbstractButton button) {
        button.removeItemListener(buttonListener);
        super.remove(button);
    }

    /**
     * {@link KButtonGroupEvent} will be fired when there is a new selected button in the group.
     * @implSpec No event is fired if the selection becomes empty.
     */
    public void addListener(KButtonGroupListener<K> listener) {
        listenerList.add(KButtonGroupListener.class, listener);
    }

    public void removeListener(KButtonGroupListener<K> listener) {
        listenerList.remove(KButtonGroupListener.class, listener);
    }

    /**
     * @param listener The group will delegate button's 'selected' {@link java.awt.event.ItemEvent} to this
     * listener.
     */
    public void addItemListener(ItemListener listener) {
        listenerList.add(ItemListener.class, listener);
    }

    public void removeItemListener(ItemListener listener) {
        listenerList.remove(ItemListener.class, listener);
    }

    /**
     * Auto-assigns mnemonic characters to the buttons of this group.
     *
     * @see AbstractButton#setMnemonic(char)
     */
    public void setMnemonics() {
        var usedChars = new HashSet<Character>();
        for (AbstractButton button : buttons) {
            String text = button.getText();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                // mnemonic's underscore visually conflicts with 'y' letter
                if (!usedChars.contains(c) && !Character.isWhitespace(c) && c != 'y') {
                    button.setMnemonic(c);
                    usedChars.add(c);
                    break;
                }
            }
        }
    }

    /**
     * @return Button at the specified index.
     */
    public K getButton(int index) {
        return (K) buttons.get(index);
    }

    /**
     * @return The group's selected button or {@code null} if no button is selected.
     */
    public K getSelectedButton() {
        int index = getSelectedIndex();
        return index > -1 ? getButton(index) : null;
    }

    /**
     * @return Index of the group's selected button or -1 if no button is selected.
     */
    public int getSelectedIndex() {
        for (int i = 0; i < buttons.size(); i++) {
            AbstractButton b = buttons.get(i);
            if (b.isSelected()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return Maximum button index in the group.
     * @throws NoSuchElementException If the group is empty.
     */
    public int getLastIndex() {
        if (buttons.isEmpty()) {
            throw new NoSuchElementException("Empty group");
        }
        return buttons.size() - 1;
    }

    /**
     * @param index Index of the button to select.
     * @see #getSelectedIndex()
     */
    public void setSelectedIndex(int index) {
        buttons.get(index).setSelected(true);
    }

    /**
     * @return Buttons of this group as unmodifiable {@link List}.
     */
    public List<K> getButtons() {
        return Collections.unmodifiableList((List<K>) buttons);
    }

    @Override
    public Iterator<K> iterator() {
        return getButtons().iterator();
    }

    public boolean isAutoSelectFirstButton() {
        return autoSelectFirstButton;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[autoSelectFirstButton=" + isAutoSelectFirstButton()
                + ",buttons=" + getButtons() + "]";
    }
}
