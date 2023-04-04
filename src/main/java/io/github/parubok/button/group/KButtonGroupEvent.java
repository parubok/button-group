package io.github.parubok.button.group;

import javax.swing.AbstractButton;
import java.util.EventObject;

public class KButtonGroupEvent<K extends AbstractButton> extends EventObject {

    private final K selectedButton;
    private final K prevSelectedButton;

    public KButtonGroupEvent(KButtonGroup group, K selectedButton, K prevSelectedButton) {
        super(group);
        this.selectedButton = selectedButton;
        this.prevSelectedButton = prevSelectedButton;
    }

    public K getSelectedButton() {
        return selectedButton;
    }

    public K getPrevSelectedButton() {
        return prevSelectedButton;
    }
}
