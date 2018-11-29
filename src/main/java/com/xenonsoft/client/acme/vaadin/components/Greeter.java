package com.xenonsoft.client.acme.vaadin.components;


import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class Greeter {
    public String sayHello() {
        return "Hello from bean " + toString();
    }
}