package ar.com.dcsys.phone.assistance.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.gwt.person.client.PersonSerializer;
import ar.com.dcsys.gwt.utils.client.GUID;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketState;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.ui.person.ManagePersonsView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class ManagePersonsActivity extends AbstractActivity implements ManagePersonsView.Presenter {

	private static final Logger logger = Logger.getLogger(ManagePersonsActivity.class.getName());
	
	private final ManagePersonsView view;
	
	/*
	private final PersonsManager personsManager;
	private final PersonDataManager personDataManager;
	*/
	private final SingleSelectionModel<Person> selection = new SingleSelectionModel<Person>();
	private final PlaceController placeController;
	private final PersonSerializer personSerializer;
	private final WebSocket ws;
	
	@Inject
//	public ManagePersonsActivity(ManagePersonsView view, PersonDataView personDataView, PersonsManager personsManager, PersonDataManager personDataManager) {
	public ManagePersonsActivity(ManagePersonsView view, PlaceController placeController, EventBus eventBus, PersonSerializer personSerializer, WebSocket ws) {
		this.view = view;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.personSerializer = personSerializer;
		this.ws = ws;
		
		selection.addSelectionChangeHandler(new Handler() {			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (selection == null) {
					return;
				}
				Person p = selection.getSelectedObject();
				if (p == null) {
					return;
				}
				ManagePersonsActivity.this.placeController.goTo(new PersonDataPlace(p));
			}
		});
	}
	
	
	private Person createPerson(String id, String n, String l, String d) {
		Person p = new Person();
		p.setId(id);
		p.setName(n);
		p.setLastName(l);
		p.setDni(d);
		return p;
	}

	
	
	private void decodeMessage(String msg) {
		List<Person> persons = new ArrayList<Person>();
		
		String sep = ";-;-;";
		String sper[] = msg.split(sep);
		
		if (sper == null || sper.length <= 0) {
			return;
		}
		
		for (String json : sper) {
			try {
				if (json == null || json.equals("")) {
					continue;
				}
				Person p = personSerializer.read(json);
				if (p != null) {
					persons.add(p);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		view.setPersons(persons);
	}
	
	
	private void generateTestUsers() {
		// genero usuarios de prueba.
		List<Person> l = new ArrayList<Person>();
		l.add(createPerson(GUID.get(),"Pablo", "Rey", "27294557"));
		l.add(createPerson(GUID.get(),"Martin", "Gomez", "27485499"));
		l.add(createPerson(GUID.get(),"Ptt", "Morat", "29795084"));

		view.setPersons(l);
	}
	
	
	private void updateUsersInteral() {
		String cmd = "userList";
		ws.send(cmd, new TransportReceiver() {
			@Override
			public void onSuccess(String message) {
				if (view == null) {
					return;
				}
				
				if (message.startsWith("OK")) {
					String message2 = message.replace("OK", "");
					decodeMessage(message2);	
				} else {
					Window.alert(message);
				}
				
			}
			
			@Override
			public void onFailure(String error) {
				Window.alert(error);
			}
		});
	}
	
	
	@Override
	public void updateUsers() {
		selection.clear();

		try {
			
			if (WebSocketState.CLOSED.equals(ws.getState())) {
				
				// abro la conexion y disparo el evento de busqueda
				final ResettableEventBus reb = new ResettableEventBus(eventBus);
				reb.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
					@Override
					public void onOpen() {
						updateUsersInteral();
						reb.removeHandlers();
					}
					@Override
					public void onClose() {
						reb.removeHandlers();
					}
				});
				ws.open();
				
			} else {
				updateUsersInteral();
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		}		
		
		
		
		
		
		/*
		personsManager.findAll(new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> t) {
				if (view == null) {
					return;
				}
				view.setPersons(t);
			}
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
		});
		*/
	}
	
	
	
	private EventBus eventBus;
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.clear();
		view.setSelectionModel(selection);
		
		panel.setWidget(view);

		/*
		this.eventBus = new ResettableEventBus(eventBus);
		this.eventBus.addHandler(MessageEvent.TYPE, new MessageEventHandler() {
			@Override
			public void onMessage(MessageEvent event) {
				if ("enroll".equals(event.getType())) {
					logger.log(Level.INFO,event.getMessage());
					if (personDataView != null) {
						personDataView.showMessage(event.getMessage());
					}
// TODO: ver por que no funca.					ManagePersonsActivity.this.eventBus.fireEvent(new MessageDialogEvent(event.getMessage()));
				}
			}
		});
		*/
	}
	
	@Override
	public void onStop() {
		view.clear();
		view.setPresenter(null);
	}

}
