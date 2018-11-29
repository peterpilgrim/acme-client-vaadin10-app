package com.xenonsoft.client.acme.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.*;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import com.xenonsoft.client.acme.model.Supplier;
import com.xenonsoft.client.acme.model.SupplierRepository;
import com.xenonsoft.client.acme.vaadin.components.Greeter;
import com.xenonsoft.client.acme.vaadin.components.SecurityProfileReader;
import com.xenonsoft.client.acme.vaadin.components.SupplierDataEditor;

import java.util.Collections;

@Route(value = "", layout = AcmeMainView.class)
public class AcmeHome extends VerticalLayout {

    private final static Logger logger = LoggerFactory.getLogger(AcmeHome.class);
    public static final int UI_POLLING_INTERVAL = 3000;

    private final SupplierRepository supplierRepository;
    private final SupplierDataEditor supplierDataEditor;

    final Grid<Supplier> grid;
    final TextField filter;

    private final Button addNewBtn;

    @Autowired
    public AcmeHome(SupplierRepository supplierRepository, SupplierDataEditor supplierDataEditor, Greeter greeter, SecurityProfileReader securityProfileReader) {

        this.supplierRepository = supplierRepository;
        this.supplierDataEditor = supplierDataEditor;
        this.grid = new Grid<>(Supplier.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New Supplier", VaadinIcon.PLUS.create());

        add(new Paragraph(greeter.sayHello()));

        VaadinService vaadinService = VaadinService.getCurrent();
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        VaadinResponse vaadinResponse = VaadinService.getCurrentResponse();

        VaadinServletRequest vaadinServletRequest = (VaadinServletRequest) vaadinRequest;
        VaadinServletResponse vaadinServletResponse = (VaadinServletResponse) vaadinResponse;

        securityProfileReader.dumpEnvironment(vaadinServletRequest);
        securityProfileReader.reportKeycloakPrincipalUserDetails( vaadinServletRequest );

        String username = securityProfileReader.retrieveUsername(vaadinServletRequest);
        logger.debug("username = {}", username );
        add(new Paragraph("Username: " + username ));


        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, supplierDataEditor);

        grid.setHeight("200px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);

        filter.addValueChangeListener(e -> listSuppliers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            supplierDataEditor.editEmployee(e.getValue());
        });

        addNewBtn.addClickListener(e -> supplierDataEditor.editEmployee(new Supplier("", "")));

        supplierDataEditor.setChangeHandler(() -> {
            supplierDataEditor.setVisible(false);
            listSuppliers(filter.getValue());
        });

        listSuppliers(null);

        UI ui = UI.getCurrent().getUI().get();
        Button button = new Button("Logout", event -> {
            // Redirect this page immediately

            logger.debug("++++++++++++++++  LOGGING OUT BUTTON  ++++++++++++++");
            logger.debug("++++++++++++++++  LOGGING OUT BUTTON  ++++++++++++++");
            logger.debug("++++++++++++++++  LOGGING OUT BUTTON  ++++++++++++++");

            ui.getPage().executeJavaScript(
                    "window.location.href='offline-page.html'");

            SecurityContext securityContext = SecurityContextHolder.getContext();
//            securityContext.getAuthentication()
            VaadinServletRequest vaadinRequest2 = (VaadinServletRequest)  VaadinService.getCurrentRequest();
            System.out.printf("vaadinRequest2=%s\n", vaadinRequest2);
//            try {
//                vaadinRequest2.logout();
//            } catch (ServletException e) {
//                logger.error("Failure to logout with VaadinServletRequest", e);
//            }

            makeKeycloakLogoutRESTCall();

            // Close the session
            ui.getSession().close();
        });

        ui.setPollInterval(UI_POLLING_INTERVAL);

        add(button);
        add(getLabel());
        add(getLabel());
        add(getLabel());
        add(getLabel());
    }

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    // *PP* - This is probably a bad idea, but we need to access the secret credentials value in the application properties
    @Value("${keycloak.credentials.secret}")
    private String keycloakCredentialsSecret;

    public void makeKeycloakLogoutRESTCall() {
        VaadinServletRequest request = (VaadinServletRequest) VaadinService.getCurrentRequest();
        VaadinServletResponse response = (VaadinServletResponse) VaadinService.getCurrentResponse();

        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();

        // You can log out of a web application in multiple ways. For Java EE servlet containers, you can call HttpServletRequest.logout().
        // For other browser applications, you can redirect the browser to
        // http://auth-server/auth/realms/{realm-name}/protocol/openid-connect/logout?redirect_uri=encodedRedirectUri, which logs you out if you have an SSO session with your browser.

        final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal)keycloakAuthenticationToken.getPrincipal();
        final RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) keycloakPrincipal.getKeycloakSecurityContext();
        final AccessToken accessToken = context.getToken();
        final IDToken idToken = context.getIdToken();

        final String scheme = request.getScheme();             // http
        final String serverName = request.getServerName();     // hostname.com
        final int serverPort = request.getServerPort();        // 80
        final String contextPath = request.getContextPath();   // /mywebapp
        final String servletPath = request.getServletPath();   // /servlet/MyServlet
        final String pathInfo = request.getPathInfo();         // /a/b;c=123
        final String queryString = request.getQueryString();          // d=789

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append("/offline-page.html");

        final String logoutURI = idToken.getIssuer() +"/protocol/openid-connect/logout?"+
                "redirect_uri="+response.encodeRedirectURL(url.toString());


//        ((RefreshableKeycloakSecurityContext) ((KeycloakPrincipal) keycloakAuthenticationToken.getPrincipal()).getKeycloakSecurityContext()).getDeployment().getRealmInfoUrl()
//        accessToken.get


        // Build the body content according to the site: https://stackoverflow.com/questions/46689034/logout-user-via-keycloak-rest-api-doesnt-work
        //
        KeycloakRestTemplate keycloakRestTemplate = new KeycloakRestTemplate(keycloakClientRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        // headers.put("Authorization", Collections.singletonList("Bearer "+idToken.getId()));
        headers.put("Authorization", Collections.singletonList("Bearer "+accessToken.getId()));
        headers.put("Content-Type", Collections.singletonList("application/x-www-form-urlencoded"));

        final StringBuilder bodyContent = new StringBuilder();
        bodyContent.append("client_id=").append(keycloakCredentialsSecret)
                .append("&")
                .append("client_secret=").append(keycloakCredentialsSecret)
                .append("&")
                .append("username=").append(keycloakPrincipal.getName())
                .append("&")
                .append("user_id=").append(idToken.getId())
                .append("&")
                .append("refresh_token=").append(context.getRefreshToken())
                .append("&")
                .append("token=").append(accessToken.getId());
        HttpEntity<String> entity = new HttpEntity<>(bodyContent.toString(), headers);
        try {
            logger.debug("++++ making a REST "+HttpMethod.POST+" call to the Keycloak server logoutURI = {}", logoutURI );
            ResponseEntity<String> forEntity = keycloakRestTemplate.exchange(logoutURI, HttpMethod.POST, entity, String.class);
            logger.debug("++++ RESULT: forEntity = {}", forEntity);
        }
        catch (HttpClientErrorException e) {
            logger.error("++++ Sadly, REST "+HttpMethod.POST+" call to the Keycloak server failed\nbodyContent={}\n", e.getResponseBodyAsString(), e);
        }
    }

    Paragraph getLabel() {
        Paragraph label = new Paragraph(
                "........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ........................................ ");
        label.setWidth("100%");
        return label;
    }

    void listSuppliers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(supplierRepository.findAll());
        } else {
            grid.setItems(supplierRepository.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }

}