package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import com.xenonsoft.client.acme.vaadin.components.SecurityProfileReader;

import java.util.Set;

@Route(value = "red", layout = AcmeMainView.class)
@Secured("red")
public class RedView  extends VerticalLayout {

    private final static Logger logger = LoggerFactory.getLogger(RedView.class);

    @Autowired
    public RedView(SecurityProfileReader securityProfileReader) {

        VaadinServletRequest vaadinServletRequest = (VaadinServletRequest) VaadinService.getCurrentRequest();

        Set<String> realmRoles = securityProfileReader.retrieveRealmRoles(vaadinServletRequest);
        logger.debug( "****  realmRoles = {}", realmRoles);
        if (!realmRoles.contains("red")) {
            throw new AccessDeniedException("Unauthorised access to RED view!");
        }

        for (int j=1; j<=5; ++j ) {
            add(new Paragraph("I am red "+j));
        }
    }
}
