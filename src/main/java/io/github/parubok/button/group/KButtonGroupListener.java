package io.github.parubok.button.group;

import javax.swing.AbstractButton;
import java.util.EventListener;

@FunctionalInterface
public interface KButtonGroupListener<K extends AbstractButton> extends EventListener {

    /**
     * Called when selection in the group changes.
     */
    void onSelectionChange(KButtonGroupEvent<K> e);
}
