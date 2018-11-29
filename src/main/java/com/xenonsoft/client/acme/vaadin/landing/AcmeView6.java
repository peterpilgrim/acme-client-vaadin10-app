package com.xenonsoft.client.acme.vaadin.landing;

import com.vaadin.flow.router.Route;
import com.xenonsoft.client.acme.vaadin.AcmeAbstractView;
import com.xenonsoft.client.acme.vaadin.AcmeMainView;

@Route(value = "view6", layout = AcmeMainView.class)
public class AcmeView6 extends AcmeAbstractView {
	@Override
	public String getViewName() {
		return getClass().getName();
	}
}