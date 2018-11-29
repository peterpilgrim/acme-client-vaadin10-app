package com.xenonsoft.client.acme.vaadin.invoiceupload;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.router.*;
import com.xenonsoft.client.acme.vaadin.AcmeMainView;
import com.xenonsoft.client.acme.vaadin.AcmeSubContent;
import com.xenonsoft.client.acme.vaadin.AcmeAbstractView;

@Route(value = "view2", layout = AcmeMainView.class)
public class AcmeView2 extends AcmeAbstractView implements BeforeEnterObserver, AfterNavigationObserver {

	private Anchor blog = new Anchor("blog", "Blog");

	public AcmeView2() {
		// Navigate to content that is not accessible from the menu directly. And see the results in the UI
		add(new Button("SubContent", buttonClickEvent -> UI.getCurrent().navigate(AcmeSubContent.class)));
		add(blog);

	}

	@Override
	public String getViewName() {
		return getClass().getName();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		System.out.println("Hello before");

		// event.rerouteTo(NoItemsView.class);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {

		System.out.println("Hello after nav");

		boolean active = event.getLocation().getFirstSegment().equals(blog.getHref());
		blog.getElement().getClassList().set("active", active);
	}

}