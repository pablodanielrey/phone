package ar.com.dcsys.phone.assistance.client.gin;

import ar.com.dcsys.phone.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.phone.assistance.client.phone.PhoneAnimationMapper;
import ar.com.dcsys.phone.assistance.client.place.PlaceControllerProvider;
import ar.com.dcsys.phone.assistance.client.ui.person.ManagePersons;
import ar.com.dcsys.phone.assistance.client.ui.person.ManagePersonsView;
import ar.com.dcsys.phone.assistance.client.ui.person.PersonData;
import ar.com.dcsys.phone.assistance.client.ui.person.PersonDataView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;

public class AssistanceGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(AssistanceActivityMapper.class).in(Singleton.class);
		bind(PhoneAnimationMapper.class).in(Singleton.class);
		
		bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
		
		bind(PersonDataView.class).to(PersonData.class).in(Singleton.class);
		bind(ManagePersonsView.class).to(ManagePersons.class).in(Singleton.class);
		
		
		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(AssistedInjectionFactory.class));
	}

}
