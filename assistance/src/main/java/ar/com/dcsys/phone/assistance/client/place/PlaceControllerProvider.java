package ar.com.dcsys.phone.assistance.client.place;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;


public class PlaceControllerProvider implements Provider<PlaceController> {

	private final EventBus eventBus;
	private final PlaceController pc;
	
	@Inject
	public PlaceControllerProvider(EventBus eventBus) {
		this.eventBus = eventBus;
		pc = new PlaceController(eventBus);
	}
	
	@Override
	public PlaceController get() {
		return pc;
	}

}
