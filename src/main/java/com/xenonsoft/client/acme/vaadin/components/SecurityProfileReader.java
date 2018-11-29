package com.xenonsoft.client.acme.vaadin.components;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringComponent
@UIScope
public class SecurityProfileReader {


    public String retrieveUsername(VaadinServletRequest request) {
        String username = "__UNKNOWN__";

        if (request.getUserPrincipal() != null) {
            if (request.getUserPrincipal().getName() != null) {
                username = request.getUserPrincipal().getName();
            }
        }

        return username;
    }

    public Set<String> retrieveRealmRoles(VaadinServletRequest request) {
        Set<String> roles = new HashSet<>();
        if ( request.getUserPrincipal() != null ) {
            if (request.getUserPrincipal() instanceof KeycloakAuthenticationToken) {
                KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
                roles = new HashSet<>(((RefreshableKeycloakSecurityContext) keycloakAuthenticationToken.getCredentials()).getToken().getRealmAccess().getRoles());
            }
        }

        return roles;
    }


    public void dumpEnvironment(VaadinServletRequest request)
    {
        System.out.printf("\n=============== Security ============\n");
        System.out.printf("\trequest.userPrincipal = %s\n", request.getUserPrincipal() );

        reportKeycloakPrincipalUserDetails(request);

        System.out.printf("\n=============== Request Attributes ============\n");
        Collections.list(request.getAttributeNames()).stream()
                .sorted()
                .forEach(
                        name -> System.out.printf("\tAttribute: %s -> [%s]\n", name, request.getAttribute(name)));

        System.out.printf("\n=============== Session Attributes ============\n");
        Collections.list(request.getSession().getAttributeNames()).stream()
                .sorted()
                .forEach(
                        name -> System.out.printf("\tSession Attribute: %s -> [%s]\n", name, request.getSession().getAttribute(name)));

        System.out.printf("\n=============== Cookies    ============\n");
        String rawCookie = request.getHeader("Cookie");
        if (rawCookie != null ) {
            String[] rawCookieParams = rawCookie.split(";");
            for(String rawCookieNameAndValue :rawCookieParams)
            {
                String[] rawCookieNameAndValuePair = rawCookieNameAndValue.split("=");
                System.out.printf("\tCookie: %s=%s\n", rawCookieNameAndValuePair[0], rawCookieNameAndValuePair[1]);
            }
        }
    }

    public void reportKeycloakPrincipalUserDetails(VaadinServletRequest request ) {

        if ( request.getUserPrincipal() != null ) {
            if ( request.getUserPrincipal() instanceof KeycloakAuthenticationToken ) {
                KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();

                System.out.printf("\n========== Security details ==========\n");
                System.out.printf("\tkeycloakAuthenticationToken=%s\n", keycloakAuthenticationToken );

                System.out.println("\nKeycloak IdToken\n");

                final IDToken idToken = ((KeycloakPrincipal) keycloakAuthenticationToken.getPrincipal()).getKeycloakSecurityContext().getIdToken();
                System.out.printf("\tidToken.name = %s\n", idToken.getName() );
                System.out.printf("\tidToken.preferredUsername = %s\n", idToken.getPreferredUsername() );
                System.out.printf("\tidToken.email = %s\n", idToken.getEmail() );
                System.out.printf("\tidToken.gender = %s\n", idToken.getGender() );
                System.out.printf("\tidToken.givenName = %s\n", idToken.getGivenName() );
                System.out.printf("\tidToken.middleName = %s\n", idToken.getMiddleName() );
                System.out.printf("\tidToken.familyName = %s\n", idToken.getFamilyName() );
                System.out.printf("\tidToken.birthDate = %s\n", idToken.getBirthdate() );

                System.out.println("\nOther Claims\n");
                final Map<String, Object> otherClaims = idToken.getOtherClaims();
                otherClaims.keySet().stream().forEach(
                        key -> System.out.printf("\tkey: [%s] -> value: [%s]\n", key, otherClaims.get(key)));

                System.out.println("\nRealm Roles\n");

                ((RefreshableKeycloakSecurityContext) keycloakAuthenticationToken.getCredentials()).getToken().getRealmAccess().getRoles()
                        .stream().forEach( role -> System.out.printf("\t: Role: [%s]\n", role));

                System.out.println("\nClient Roles\n");

                Map<String, AccessToken.Access> resourceAccess = ((RefreshableKeycloakSecurityContext) keycloakAuthenticationToken.getCredentials()).getToken().getResourceAccess();

                resourceAccess.keySet().stream().forEach( key -> {
                    System.out.printf("\t:key = [%s]\n", key);
                    resourceAccess.get(key).getRoles()
                        .stream().forEach( role -> System.out.printf("\t: Role: [%s]\n", role));
                });

            }

        }
    }
}

