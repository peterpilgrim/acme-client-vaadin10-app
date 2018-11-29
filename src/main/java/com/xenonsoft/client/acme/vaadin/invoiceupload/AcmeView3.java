package com.xenonsoft.client.acme.vaadin.invoiceupload;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.xenonsoft.client.acme.vaadin.AcmeMainView;
import com.xenonsoft.client.acme.vaadin.AcmeSubContent;

import java.io.InputStream;

@Route(value = "view3", layout = AcmeMainView.class)
public class AcmeView3 extends VerticalLayout {

	private static final long serialVersionUID = 5194026958229413341L;

	private MemoryBuffer buffer = new MemoryBuffer();

	public AcmeView3() {

		add(new Label("<--1-->"));
		add(new Label("<--2-->"));

		add(new Label("Invoice upload"));
		Upload uploadControl = new Upload(buffer);

		ProgressBar progressBar = new ProgressBar();
		progressBar.setValue(0.0f);
		progressBar.setSizeFull();

		uploadControl.addProgressListener(event -> {
			long contentLength = event.getContentLength();
			long readBytes = event.getReadBytes();
			System.out.println(">>" + contentLength + " -- " + readBytes);
		});

		uploadControl.addFailedListener(event -> {
			System.out.println(">> failed " + event);
		});
		uploadControl.addFinishedListener(event -> {
			System.out.println(">> finished " + event);
		});
		uploadControl.addStartedListener(event -> {
			System.out.println(">> started " + event);
		});

		uploadControl.addSucceededListener(event -> {
			String mimeType = event.getMIMEType();
			String fileName = event.getFileName();
			InputStream inputStream = buffer.getInputStream();

			System.out.println(">1" + mimeType);
			System.out.println(">2" + fileName);
			System.out.println(">3" + inputStream);
			UI current = UI.getCurrent();
			System.out.println(">2" + current);

			Dialog dialog = new Dialog();
			dialog.add(new Label("Close me with the esc-key or an outside click"));

			dialog.setWidth("400px");
			dialog.setHeight("150px");

			dialog.open();

			progressBar.setVisible(true);
			progressBar.setIndeterminate(false);
		});

		add(uploadControl);

		add(progressBar);

		add(new Label("<--4-->"));

		add(new Button("pressme", buttonClickEvent -> {
			UI.getCurrent().navigate(AcmeSubContent.class);
		}));

	}

}