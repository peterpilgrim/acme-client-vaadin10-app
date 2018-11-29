package com.xenonsoft.client.acme.vaadin;


import com.github.appreciated.app.layout.behaviour.AppLayout;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.appmenu.MenuHeaderComponent;
import com.github.appreciated.app.layout.component.appmenu.left.LeftClickableComponent;
import com.github.appreciated.app.layout.component.appmenu.left.LeftNavigationComponent;
import com.github.appreciated.app.layout.component.appmenu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.appmenu.top.TopClickableComponent;
import com.github.appreciated.app.layout.component.appmenu.top.TopNavigationComponent;
import com.github.appreciated.app.layout.component.appmenu.top.builder.TopAppMenuBuilder;
import com.github.appreciated.app.layout.design.AppLayoutDesign;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView3;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView1;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView2;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
@Route("alternative")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class AlternativeMainView extends AppLayoutRouterLayout {


    private Behaviour variant;
    private DefaultNotificationHolder notifications;
    private DefaultBadgeHolder badge;
    private Thread currentThread;

    @Override
    public AppLayout getAppLayout() {

        if (variant == null) {
            variant = Behaviour.LEFT_HYBRID;
            notifications = new DefaultNotificationHolder(newStatus -> {
            });
            badge = new DefaultBadgeHolder();
        }

        if (!variant.isTop()) {
            return AppLayoutBuilder.get(variant) //
                    .withTitle("Alternative Layout") //
                    .withDesign(AppLayoutDesign.MATERIAL) //
                    .withAppMenu( //
                            LeftAppMenuBuilder.get() //
                                    .addToSection(new MenuHeaderComponent("XXX", "Version 2.0.1", "frontend/images/logo.png"), HEADER) //
                                    .addToSection(new LeftClickableComponent("Set Behaviour HEADER", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), HEADER) //
                                    .add(new LeftNavigationComponent("Home", VaadinIcon.HOME.create(), AcmeHome.class)) //
                                    .addToSection(new LeftClickableComponent("Set Behaviour FOOTER", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), FOOTER) //
                                    .build() //
                    ).build();
        } else {
            return AppLayoutBuilder.get(variant).withTitle("Alternative Layout")
                    .withAppBar(
                            AppBarBuilder.get()
                            .add(new AppBarNotificationButton(VaadinIcon.BELL, notifications)).build())
                    .withDesign(AppLayoutDesign.MATERIAL)
                    .withAppMenu(
                            TopAppMenuBuilder.get().addToSection(new TopClickableComponent("Set Behaviour 1", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), HEADER)
                            .add(new TopNavigationComponent("Home", VaadinIcon.HOME.create(), AcmeView1.class)).add(new TopNavigationComponent("Contact", VaadinIcon.SPLINE_CHART.create(), AcmeView2.class))
                            .addToSection(new TopClickableComponent("Set Behaviour 2", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), FOOTER)
                            .addToSection(new TopNavigationComponent("More", VaadinIcon.CONNECT.create(), AcmeView3.class), FOOTER).build())
                    .build();
        }
    }

    private void setDrawerVariant(Behaviour variant) {
        this.variant = variant;
        reloadConfiguration();
    }

    private void openModeSelector(Behaviour variant) {
        new BehaviourSelector(variant, variant1 -> setDrawerVariant(variant1)).open();
    }

}
