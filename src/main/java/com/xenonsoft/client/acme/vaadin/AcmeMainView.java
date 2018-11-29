package com.xenonsoft.client.acme.vaadin;

import com.github.appreciated.app.layout.behaviour.AppLayout;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.appmenu.MenuHeaderComponent;
import com.github.appreciated.app.layout.component.appmenu.left.LeftClickableComponent;
import com.github.appreciated.app.layout.component.appmenu.left.LeftNavigationComponent;
import com.github.appreciated.app.layout.component.appmenu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.appmenu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.appmenu.top.TopClickableComponent;
import com.github.appreciated.app.layout.component.appmenu.top.TopNavigationComponent;
import com.github.appreciated.app.layout.component.appmenu.top.builder.TopAppMenuBuilder;
import com.github.appreciated.app.layout.design.AppLayoutDesign;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.notification.entitiy.DefaultNotification;
import com.github.appreciated.app.layout.notification.entitiy.Priority;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView3;
import com.xenonsoft.client.acme.vaadin.landing.AcmeView6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xenonsoft.client.acme.security.SecurityUtils;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView1;
import com.xenonsoft.client.acme.vaadin.invoiceupload.AcmeView2;
import com.xenonsoft.client.acme.vaadin.landing.AcmeView4;
import com.xenonsoft.client.acme.vaadin.landing.AcmeView5;

import javax.servlet.ServletException;
import java.util.Collections;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;
import static com.github.appreciated.app.layout.notification.entitiy.Priority.MEDIUM;

/**
 * The main view contains a button and a template element.
 */

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class AcmeMainView extends AppLayoutRouterLayout {

	private final static Logger logger = LoggerFactory.getLogger(AcmeMainView.class);

	private static final long serialVersionUID = 1L;
	public static final int RELOAD_NOTIFICATIONS_TIMEOUT_MILLIS = 30000;

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
		reloadNotifications();

		logger.debug("+++++++++++++++++++ ALBERT ++++++++++++");
		logger.debug("username: {}", SecurityUtils.getUsername() );
		logger.debug("isLoggedIn: {}", SecurityUtils.isUserLoggedIn());

		logger.debug("Is this user client role access [red] privileged?  {}", SecurityUtils.isClientRoleAccessGranted(RedView.class));
		logger.debug("Is this user client role access [blue] privileged?  {}", SecurityUtils.isClientRoleAccessGranted(BlueView.class));

		logger.debug("Is this user realm role access [red] privileged?  {}", SecurityUtils.isRealmRoleAccessGranted(RedView.class));
		logger.debug("Is this user realm role access [blue] privileged?  {}", SecurityUtils.isRealmRoleAccessGranted(BlueView.class));

		if (!variant.isTop()) {
			return AppLayoutBuilder.get(variant) //
					.withTitle("App Layout") //
					.withAppBar( //
							AppBarBuilder.get().add(new AppBarNotificationButton(VaadinIcon.BELL, notifications)).build()) //
					.withDesign(AppLayoutDesign.MATERIAL) //
					.withAppMenu( //
							LeftAppMenuBuilder.get() //
									.addToSection(new MenuHeaderComponent("XXX", "Version 2.0.1", "frontend/images/logo.png"), HEADER) //
									.addToSection(new LeftClickableComponent("Set Behaviour HEADER", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), HEADER) //
									.add(new LeftNavigationComponent("Home", VaadinIcon.HOME.create(), AcmeHome.class)) //
									.add(new LeftNavigationComponent("Grid", VaadinIcon.TABLE.create(), AcmeGridTest.class)) //
									.add(LeftSubMenuBuilder.get("Protected Submenu", VaadinIcon.PLUS.create()) //
											.add(new LeftNavigationComponent("RED VIEW", VaadinIcon.AUTOMATION.create(), RedView.class))
											.add(new LeftNavigationComponent("BLUE VIEW", VaadinIcon.BARCODE.create(), BlueView.class))
											.build())//
									.add(LeftSubMenuBuilder.get("My Submenu A", VaadinIcon.PLUS.create()) //
											.add(new LeftNavigationComponent("View 1", VaadinIcon.SPLINE_CHART.create(), AcmeView1.class)) //
											.add(new LeftNavigationComponent("View 2", VaadinIcon.CONNECT.create(), AcmeView2.class)) //
											.add(new LeftNavigationComponent("View 3", VaadinIcon.ADOBE_FLASH.create(), AcmeView3.class)) //
											.build()) //
									.add(LeftSubMenuBuilder.get("My Submenu B", VaadinIcon.PLUS.create()) //
											.add(new LeftNavigationComponent("View 4", VaadinIcon.WRENCH.create(), AcmeView4.class)) //
											.add(new LeftNavigationComponent("View 5", VaadinIcon.COG.create(), AcmeView5.class)) //
											.add(new LeftNavigationComponent("View 6", VaadinIcon.AIRPLANE.create(), AcmeView6.class)) //
											.build()) //
									.addToSection(new LeftClickableComponent("Set Behaviour FOOTER", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), FOOTER) //
									.add(new LeftClickableComponent("Logout", VaadinIcon.USER.create(), clickEvent -> {

										logger.debug("+++++++++++++++++++++++++++++++++++++++");
										logger.debug("++++++     LOG FROM MENU ITEM     +++++");
										logger.debug("+++++++++++++++++++++++++++++++++++++++");

										// https://vaadin.com/docs/v10/flow/routing/tutorial-routing-navigation.html
										final UI ui = UI.getCurrent();
										ui.navigate("offline-page.html");
										ui.getSession().close();

										logout( (VaadinServletRequest) VaadinService.getCurrentRequest() );
									}))
									.build() //
					).build();
		} else {
			return AppLayoutBuilder.get(variant).withTitle("App Layout").withAppBar(AppBarBuilder.get().add(new AppBarNotificationButton(VaadinIcon.BELL, notifications)).build()).withDesign(AppLayoutDesign.MATERIAL)
					.withAppMenu(TopAppMenuBuilder.get().addToSection(new TopClickableComponent("Set Behaviour 1", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), HEADER)
							.add(new TopNavigationComponent("Home", VaadinIcon.HOME.create(), AcmeView1.class)).add(new TopNavigationComponent("Contact", VaadinIcon.SPLINE_CHART.create(), AcmeView2.class))
							.addToSection(new TopClickableComponent("Set Behaviour 2", VaadinIcon.COG.create(), clickEvent -> openModeSelector(variant)), FOOTER)
							.addToSection(new TopNavigationComponent("More", VaadinIcon.CONNECT.create(), AcmeView3.class), FOOTER).build())
					.build();
		}
	}

	private void logout(VaadinServletRequest request) {
		logger.debug("++++ LOGGING OUT ++++");

		// https://www.keycloak.org/docs/3.3/securing_apps/topics/oidc/java/logout.html
		try {
			// http://auth-server/auth/realms/{realm-name}/protocol/openid-connect/logout?redirect_uri=encodedRedirectUri
			request.logout();
		} catch (ServletException e) {
			logger.error("*ERROR* : Failure to logout",e);
		}

		Collections.list(request.getAttributeNames()).stream()
				.filter( attribute -> attribute.startsWith("org.keycloak")).
				forEach( name -> {
					logger.debug(String.format("\t+++++ Removing request scope attribute [%s]", name ));
					request.removeAttribute(name);
				});

		Collections.list(request.getSession().getAttributeNames()).stream()
				.filter( attribute -> attribute.startsWith("org.keycloak")).
				forEach( name -> {
					logger.debug(String.format("\t+++++ Removing session scope attribute [%s]", name ));
					request.getSession().removeAttribute(name);
				});

		request.getSession().invalidate();
	}

	private void reloadNotifications() {
		if (currentThread != null && !currentThread.isInterrupted()) {
			currentThread.interrupt();
		}
		badge.clearCount();
		notifications.clearNotifications();
		currentThread = new Thread(() -> {
			try {
				Thread.sleep(RELOAD_NOTIFICATIONS_TIMEOUT_MILLIS);
				for (int i = 0; i < 10; i++) {
					Thread.sleep(RELOAD_NOTIFICATIONS_TIMEOUT_MILLIS);
					addNotification(MEDIUM);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		currentThread.start();
	}

	private void addNotification(Priority priority) {
		getUI().ifPresent(ui -> {
			// check if the UI is detached or closing from view
		    if (!ui.isClosing()) {
                ui.accessSynchronously(() -> {
                    badge.increase();
                    notifications.addNotification(new DefaultNotification("Title" + badge.getCount(), "Description" + badge.getCount(), priority));
                });
            }
        });
	}

	private void setDrawerVariant(Behaviour variant) {
		this.variant = variant;
		reloadConfiguration();
	}

	private void openModeSelector(Behaviour variant) {
		new BehaviourSelector(variant, variant1 -> setDrawerVariant(variant1)).open();
	}
}
