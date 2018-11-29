package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletResponse;

@Route(value = "access-denied", layout = AlternativeMainView.class)
@Tag(Tag.DIV)
public class CustomAccessDeniedView extends Component implements HasErrorParameter<AccessDeniedException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<AccessDeniedException> parameter) {
        getElement().setText(
                "Tried to navigate to a view without correct access rights");
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
