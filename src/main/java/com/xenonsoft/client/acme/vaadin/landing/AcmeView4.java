package com.xenonsoft.client.acme.vaadin.landing;

import com.storedobject.vaadin.Card;
import com.storedobject.vaadin.PDFViewer;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.xenonsoft.client.acme.vaadin.AcmeMainView;

@Route(value = "view4", layout = AcmeMainView.class)
public class AcmeView4 extends VerticalLayout {

	public AcmeView4() {

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		Label label = new Label("Card");
		label.setWidth("100%");
		Card card = new Card(label);

		layout.add(card);
		layout.setAlignItems(Alignment.CENTER);
		add(layout);

		setSizeFull();
		getElement().getStyle().set("overflow", "auto");

		PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
		pdfViewer.setWidth("800px");
		pdfViewer.setHeight("600px");
		add(pdfViewer);
	}
}