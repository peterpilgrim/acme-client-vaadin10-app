package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import javax.servlet.http.HttpServletResponse;

// https://vaadin.com/docs/v10/flow/routing/tutorial-routing-exception-handling.html

@Tag(Tag.DIV)
public class RouteNotFoundError extends Component implements HasErrorParameter<NotFoundException> {
    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<NotFoundException> errorParameter) {
        getElement().setText("Application failure with exception ["+errorParameter.getException().getMessage()+"]");
        errorParameter.getException().printStackTrace(System.err);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
