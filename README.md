## Relevant articles:

- [Introduction to Vaadin](http://www.baeldung.com/vaadin)
- [Sample Application with Spring Boot and Vaadin](https://www.baeldung.com/spring-boot-vaadin)

I added *Keycloak* security configuration

```
    realm: demo
    client: vaadinapp
    client role: suppliers
    
    realm roles: [suppliers, red, blue]
    
    realm roles: [ suppliers, red ]
    username: jane
    email: jane@acme.com
    password: password
    Fullname: Jane Smith

    realm roles: [ suppliers, blue ]
    username: jake
    password: password
    email: jake@acme.com
    Fullname: Jake Smith
    
``` 

**STOP PRESS**: 

I think I do not need the Keycloak client role `suppliers`.
I just only need the realm role `suppliers`. This was part of my confusion with Keycloak 

See also the [application.properties](src/main/resources/application.properties), which is configured like this:

```
# Configure Vaadin Spring Boot application
server.port=9048

keycloak.realm = demo
keycloak.auth-server-url = http://127.0.0.1:9090/auth
keycloak.ssl-required = external
keycloak.resource = vaadinapp
keycloak.public-client = false
keycloak.principal-attribute = preferred_username
## Grab the security code from the `Broker' client inside Keycloak realm `demo'.
keycloak.credentials.secret = 5f35f961-fd05-4e02-9924-4d6768b800ad
keycloak.use-resource-role-mappings = true
```


Note that the keycloak server is configured to port 9090.
Also note the application URL is http://localhost:9048.

See also this article: https://github.com/chvndb/keycloak-spring-vaadin-demo/blob/master/src/main/java/demo/vaadin/config/SecurityConfiguration.java


See the source code: https://github.com/chvndb/keycloak-spring-vaadin-demo




## Logout Exercise

I still don't know how to logout of a Vaadin 10 application with Spring Boot and Spring Security.

I see on the Keycloak Server the following log messages:

```
10:43:01,742 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0051: Admin console listening on http://127.0.0.1:9990
10:43:01,742 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: Keycloak 4.6.0.Final (WildFly Core 6.0.2.Final) started in 9322ms - Started 577 of 834 services (557 services are lazy, passive or on-demand)
10:50:05,180 WARN  [org.keycloak.events] (default task-1) type=LOGIN_ERROR, realmId=master, clientId=null, userId=null, ipAddress=127.0.0.1, error=expired_code, restart_after_timeout=true
11:11:54,722 WARN  [org.keycloak.events] (default task-1) type=LOGOUT_ERROR, realmId=demo, clientId=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJqYTBjX18xMHJXZi1KTEpYSGNqNEdSNWViczRmQlpGS3NpSHItbDlud2F3In0.eyJqdGkiOiIwMjcwNGRjNC0zZWMwLTQwOTAtYTYyMy03Y2U2OTI5Mzk2ODciLCJleHAiOjE1NDM0OTAxNTcsIm5iZiI6MCwiaWF0IjoxNTQzNDg5ODU3LCJpc3MiOiJodHRwOi8vMTI3Lj, userId=null, ipAddress=127.0.0.1, error=invalid_client_credentials
```

From this [Stack Overflow page: Logout user via Keycloak REST API doesn't work](https://stackoverflow.com/questions/46689034/logout-user-via-keycloak-rest-api-doesnt-work/53517826#53517826), 
what is the `client_id` meant to be set to?


+PP+
29 November 2018
