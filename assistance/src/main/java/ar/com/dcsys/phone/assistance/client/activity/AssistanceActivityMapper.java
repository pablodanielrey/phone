package ar.com.dcsys.phone.assistance.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.phone.assistance.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.place.UsersPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AssistanceActivityMapper implements ActivityMapper {

	private final AssistedInjectionFactory factory;
	
	@Inject
	public AssistanceActivityMapper(AssistedInjectionFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		
		if (place instanceof UsersPlace) {
			return factory.usersActivity((UsersPlace)place);
		}
		
		if (place instanceof PersonDataPlace) {
			return factory.personDataActivity((PersonDataPlace)place);
		}
		
		return null;
		
	}

}
