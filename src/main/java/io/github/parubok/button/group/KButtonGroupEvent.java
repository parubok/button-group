package io.github.parubok.button.group;

import javax.swing.AbstractButton;
import java.util.EventObject;
import java.util.Objects;

/**
 * @param <K> Type of the buttons in the group.
 */
public class KButtonGroupEvent<K extends AbstractButton> extends EventObject {

    private final K selectedButton;
    private final K prevSelectedButton;

    public KButtonGroupEvent(KButtonGroup group, K selectedButton, K prevSelectedButton) {
        super(group);
        this.selectedButton = Objects.requireNonNull(selectedButton);
        this.prevSelectedButton = prevSelectedButton;
    }

    /**
     * @return Currently selected button. Not {@code null}.
     */
    public K getSelectedButton() {
        return selectedButton;
    }

    /**
     * @return Previously selected button. May be {@code null}.
     */
    public K getPrevSelectedButton() {
        return prevSelectedButton;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[selectedButton=" + getSelectedButton()
                + ",prevSelectedButton=" + getPrevSelectedButton() + "]";
    }
}
