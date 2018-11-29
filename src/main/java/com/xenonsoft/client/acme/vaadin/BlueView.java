package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import com.xenonsoft.client.acme.vaadin.components.SecurityProfileReader;

import java.util.Set;

@Route(value = "blue", layout = AcmeMainView.class)
@Secured("blue")
public class BlueView extends VerticalLayout {

    private final static Logger logger = LoggerFactory.getLogger(BlueView.class);

    @Autowired
    public BlueView(SecurityProfileReader securityProfileReader) {

        VaadinServletRequest vaadinServletRequest = (VaadinServletRequest) VaadinService.getCurrentRequest();
        VaadinServletResponse vaadinServletResponse = (VaadinServletResponse)  VaadinService.getCurrentResponse();

        Set<String> realmRoles = securityProfileReader.retrieveRealmRoles(vaadinServletRequest);
        logger.debug( "****  realmRoles = {}", realmRoles);
        if (!realmRoles.contains("blue")) {
            throw new AccessDeniedException("Unauthorised access to BLUE view!");
        }

        for (int j=1; j<=5; ++j ) {
            add(new Paragraph("I am blue "+j));
        }
    }
}
