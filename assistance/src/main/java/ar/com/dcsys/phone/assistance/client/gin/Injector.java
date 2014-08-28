package ar.com.dcsys.phone.assistance.client.gin;

import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;
import ar.com.dcsys.phone.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.phone.assistance.client.phone.PhoneAnimationMapper;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;



@GinModules(value = {AssistanceGWTGinModule.class,
					WsGinModule.class})
public interface Injector extends Ginjector {
	
	public AssistedInjectionFactory factory();
	
	public EventBus eventbus();
	public PlaceController placeController();
	
	public AssistanceActivityMapper assistanceActivityMapper();
	public PhoneAnimationMapper phoneAnimationMapper();
	
//	public WebSocket ws();

	
	/// modulos de person ///
	
//	public EnrollModule enrollModule();
	
}
