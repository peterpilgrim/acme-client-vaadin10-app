package com.xenonsoft.client.acme.vaadin;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.function.Consumer;

public class BehaviourSelector extends Dialog {
    private static final long serialVersionUID = 1L;

    public BehaviourSelector(Behaviour current, Consumer<Behaviour> consumer) {
        VerticalLayout layout = new VerticalLayout();
        add(layout);
        RadioButtonGroup<Behaviour> group = new RadioButtonGroup<>();
        group.getElement().getStyle().set("display", "flex");
        group.getElement().getStyle().set("flexDirection", "column");
        group.setItems(Behaviour.LEFT, Behaviour.LEFT_OVERLAY, Behaviour.LEFT_RESPONSIVE, Behaviour.LEFT_HYBRID, Behaviour.LEFT_HYBRID_SMALL, Behaviour.LEFT_RESPONSIVE_HYBRID, Behaviour.LEFT_RESPONSIVE_HYBRID_NO_APP_BAR,
                Behaviour.LEFT_RESPONSIVE_HYBRID_OVERLAY_NO_APP_BAR, Behaviour.LEFT_RESPONSIVE_OVERLAY, Behaviour.LEFT_RESPONSIVE_OVERLAY_NO_APP_BAR, Behaviour.LEFT_RESPONSIVE_SMALL, Behaviour.LEFT_RESPONSIVE_SMALL_NO_APP_BAR,
                Behaviour.TOP, Behaviour.TOP_LARGE);
        group.setValue(current);
        layout.add(group);
        group.addValueChangeListener(singleSelectionEvent -> {
            consumer.accept(singleSelectionEvent.getValue());
            close();
        });
    }
}
