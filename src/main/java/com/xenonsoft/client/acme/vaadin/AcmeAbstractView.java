package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

abstract public class AcmeAbstractView extends HorizontalLayout {
	public AcmeAbstractView() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		setMargin(false);
		Label label = new Label("< " + getViewName() + " >");
		layout.add(label);
		layout.setAlignItems(Alignment.CENTER);
		add(layout);
		setSizeFull();
		getElement().getStyle().set("overflow", "auto");
	}

	public abstract String getViewName();
}