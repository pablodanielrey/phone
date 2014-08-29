package ar.com.dcsys.phone.assistance.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.messages.client.event.MessageEvent;
import ar.com.dcsys.gwt.messages.client.event.MessageEventHandler;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.ui.person.PersonDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class PersonDataActivity extends AbstractActivity implements PersonDataView.Presenter {

	private final Person person;
	private final PersonDataView view;
	private final PlaceController placeController;
	private final WebSocket ws;
	
	private HandlerRegistration hr;
	
	@Inject
	public PersonDataActivity(PersonDataView view, PlaceController placeController, WebSocket ws, @Assisted PersonDataPlace place) {
		this.person = place.getPerson();
		this.view = view;
		this.placeController = placeController;
		this.ws = ws;
	}
	
	
	private void deregisterEvent() {
		if (hr != null) {
			try {
				hr.removeHandler();
			} finally {
				hr = null;
			}
		}
	}

	private void registerEvent(EventBus eventBus) {
		hr = eventBus.addHandler(MessageEvent.TYPE, new MessageEventHandler() {
			@Override
			public void onMessage(MessageEvent event) {
				if (view == null || !"enroll".equals(event.getType())) {
					return;
				}
				String msg = event.getMessage();
				view.showMessage(msg);
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.setPersonData(person);
		panel.setWidget(view);
		
		deregisterEvent();
		registerEvent(eventBus);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
		deregisterEvent();
	}
	
	@Override
	public void enroll() {
		
		clearMessage();
		
		if (person == null || person.getId() == null) {
			if (view != null) {
				view.showMessage("person.id == null");
			}
			return;
		}

		String personId = person.getId();
		String cmd = "enroll;" + personId;
		
		try {
			ws.open();
			ws.send(cmd, new TransportReceiver() {
				@Override
				public void onSuccess(String message) {
					if (view != null) {
						//view.showMessage(message == null ? "null" : message);
						view.showMessage("Transfiriendo la persona la reloj");
						
						// TODO: ver como atomizar estas operaciones.
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								persist();
							}
						});
						
						
					}
				}
				
				@Override
				public void onFailure(String error) {
					if (view != null) {
						view.showMessage(error == null ? "null" : error);
					}
				}
			});
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void clearMessage() {
		if (view != null) {
			view.showMessage("");
		}
	}
	
	
	@Override
	public void persist() {

		clearMessage();
		
		if (person == null || person.getId() == null) {
			if (view != null) {
				view.showMessage("person.id == null");
			}
			return;
		}

		String personId = person.getId();
		String cmd = "persistPerson;" + personId;
		
		try {
			ws.open();
			ws.send(cmd, new TransportReceiver() {
				@Override
				public void onSuccess(String message) {
					if (view != null) {
						//view.showMessage(message == null ? "null" : message);
						view.showMessage("Transfiriendo huellas hacia el lector.");
						
						// TODO: ver como atomizar estas operaciones.
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								transferFingerprints();
							}
						});
						
						
					}
				}
				
				@Override
				public void onFailure(String error) {
					if (view != null) {
						view.showMessage(error == null ? "null" : error);
					}
				}
			});
		} catch (SocketException e) {
			e.printStackTrace();
		}		
		
	}

	@Override
	public void transferFingerprints() {

		if (person == null || person.getId() == null) {
			if (view != null) {
				view.showMessage("person.id == null");
			}
			return;
		}

		String personId = person.getId();
		String cmd = "transferFingerprints;" + personId;
		
		try {
			ws.open();
			ws.send(cmd, new TransportReceiver() {
				@Override
				public void onSuccess(String message) {
					if (view != null) {
						view.showMessage(message == null ? "null" : message);
					}
				}
				
				@Override
				public void onFailure(String error) {
					if (view != null) {
						view.showMessage(error == null ? "null" : error);
					}
				}
			});
		} catch (SocketException e) {
			e.printStackTrace();
		}		
	
	
	}
	
	

}
