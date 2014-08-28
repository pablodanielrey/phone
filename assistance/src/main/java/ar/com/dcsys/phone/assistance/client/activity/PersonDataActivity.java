package ar.com.dcsys.phone.assistance.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.place.UsersPlace;
import ar.com.dcsys.phone.assistance.client.ui.person.PersonDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class PersonDataActivity extends AbstractActivity implements PersonDataView.Presenter {

	private final Person person;
	private final PersonDataView view;
	private final PlaceController placeController;
	
	@Inject
	public PersonDataActivity(PersonDataView view, PlaceController placeController, @Assisted PersonDataPlace place) {
		this.person = place.getPerson();
		this.view = view;
		this.placeController = placeController;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.setPersonData(person);
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void enroll() {
		placeController.goTo(new UsersPlace());
	}

	@Override
	public void persist() {
		placeController.goTo(new UsersPlace());
	}

	@Override
	public void transferFingerprints() {
		placeController.goTo(new UsersPlace());
	}
	
	

}
