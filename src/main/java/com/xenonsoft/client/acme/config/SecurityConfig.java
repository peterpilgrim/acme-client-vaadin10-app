package com.xenonsoft.client.acme.config;


import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.xenonsoft.client.acme.model.Supplier;
import com.xenonsoft.client.acme.model.SupplierRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Submits the KeycloakAuthenticationProvider to the AuthenticationManager

    @PostConstruct
    public void yelp() {
        logger.debug("YELP! +++++++++ INITIALISATION -> {} ++++++++++", this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this)));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // https://github.com/dasniko/kc-sb-sec-demo/blob/master/src/main/java/dasniko/keycloak/demo/KeyclaokSpringSecurityConfig.java
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;


    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    // Specifies the session authentication strategy
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public IDToken getIDToken() {
        //
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getAttribute(KeycloakSecurityContext.class.getName()) != null) {
            IDToken idToken = ((RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName())).getIdToken();
            return idToken;
        }
        return null;
    }


//    @Autowired
//    CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.httpBasic().disable();
        http.formLogin().disable();
        http.anonymous().disable();
        http.csrf().disable();
        // http.csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher());
        http
                .sessionManagement()
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy());

        http
                .authorizeRequests()
                .antMatchers("/offline-page.html").permitAll()
                .antMatchers("/vaadinServlet/UIDL/**").permitAll()
                .antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll()
                .anyRequest().authenticated();

        http
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/sso/logout").permitAll()
                .logoutSuccessUrl("/offline-page.html");

        http
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
                .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    @Bean
    public KeycloakRestTemplate createKeycloakRestTemplate( ) {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    @Bean
    public CommandLineRunner loadData(SupplierRepository repository) {
        return (args) -> {
            repository.save(new Supplier("Bill", "Gates"));
            repository.save(new Supplier("Mark", "Zuckerberg"));
            repository.save(new Supplier("Sundar", "Pichai"));
            repository.save(new Supplier("Jeff", "Bezos"));
            repository.save(new Supplier("Tim", "Cook"));
            repository.save(new Supplier("Satya", "Nadella"));
        };
    }
}
