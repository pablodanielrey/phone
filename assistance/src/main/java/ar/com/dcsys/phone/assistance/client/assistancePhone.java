package ar.com.dcsys.phone.assistance.client;

import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.phone.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.phone.assistance.client.gin.Injector;
import ar.com.dcsys.phone.assistance.client.phone.PhoneAnimationMapper;
import ar.com.dcsys.phone.assistance.client.place.AppPlaceHistoryMapper;
import ar.com.dcsys.phone.assistance.client.place.UsersPlace;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.util.SuperDevModeUtil;
import com.googlecode.mgwt.ui.client.widget.animation.AnimationWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class assistancePhone implements EntryPoint {

	
	private void configureViewPort() {
	    ViewPort viewPort = new MGWTSettings.ViewPort();
	    viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);

	    MGWTSettings settings = new MGWTSettings();
	    settings.setViewPort(viewPort);
//	    settings.setIconUrl("logo.png");
	    settings.setFullscreen(true);
	    settings.setFixIOS71BodyBug(true);
	    settings.setPreventScrolling(true);

	    MGWT.applySettings(settings);		
	}
	
	
	private void createPhoneDisplay() {
		AnimationWidget display = new AnimationWidget();		
		amanager.setDisplay(display);
		RootPanel.get().add(display);
	}
	
	private void configureActivities() {
		EventBus eventBus = injector.eventbus();
		amapper = injector.assistanceActivityMapper();
		pamapper = injector.phoneAnimationMapper();
		amanager = new AnimatingActivityManager(amapper, pamapper, eventBus);
	}
	
	
	private void createHistory() {
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(injector.placeController(), injector.eventbus(), new UsersPlace());
	}
	
	
	private void createWebSocket() {
		WebSocket ws = injector.webSocket();
		try {
			ws.open();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	private Injector injector = GWT.create(Injector.class);
	private AnimatingActivityManager amanager;
	private AssistanceActivityMapper amapper;
	private PhoneAnimationMapper pamapper;
	private PlaceHistoryHandler historyHandler;
	
	public void onModuleLoad() {

		SuperDevModeUtil.showDevMode();
		
		configureActivities();
		configureViewPort();
		createPhoneDisplay();
		createHistory();
	
		createWebSocket();
		
		historyHandler.handleCurrentHistory();
	}
	
}
