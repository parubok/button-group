package io.github.parubok.button.group;

import javax.swing.AbstractButton;
import java.util.EventListener;

@FunctionalInterface
public interface KButtonGroupListener<K extends AbstractButton> extends EventListener {
    void onSelection(KButtonGroupEvent<K> e);
}
