package ar.com.dcsys.phone.assistance.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.ui.person.ManagePersonsView;
import ar.com.dcsys.phone.assistance.client.ui.person.PersonDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ManagePersonsActivity extends AbstractActivity implements ManagePersonsView.Presenter, PersonDataView.Presenter {

	private static final Logger logger = Logger.getLogger(ManagePersonsActivity.class.getName());
	
	private final ManagePersonsView view;
	
	/*
	private final PersonsManager personsManager;
	private final PersonDataManager personDataManager;
	*/
	private final SingleSelectionModel<Person> selection = new SingleSelectionModel<Person>();
	private final PlaceController placeController;
	
	@Inject
//	public ManagePersonsActivity(ManagePersonsView view, PersonDataView personDataView, PersonsManager personsManager, PersonDataManager personDataManager) {
	public ManagePersonsActivity(ManagePersonsView view, PlaceController placeController, EventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		this.placeController = placeController;
		
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
	
	
	private Person createPerson(String n, String l, String d) {
		Person p = new Person();
		p.setName(n);
		p.setLastName(l);
		p.setDni(d);
		return p;
	}
	
	@Override
	public void updateUsers() {
		selection.clear();
		
		
		
		// genero usuarios de prueba.
		List<Person> l = new ArrayList<Person>();
		l.add(createPerson("Pablo", "Rey", "27294557"));
		l.add(createPerson("Martin", "Gomez", "27485499"));
		l.add(createPerson("Ptt", "Morat", "29795084"));

		view.setPersons(l);
		
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
	
	@Override
	public void enroll() {
		Person person = selection.getSelectedObject();
		if (person == null) {
			logger.log(Level.SEVERE,"selection.person == null");
			return;
		}
		/*
		personDataManager.enroll(person.getId(), new Receiver<String>() {
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
			@Override
			public void onSuccess(String t) {
				logger.log(Level.INFO, "huella : " + t);
				if (personDataView != null) {
					personDataView.showMessage(t);
				}
			}
		});
		*/
	}
	
	@Override
	public void persist() {
		Person person = selection.getSelectedObject();
		if (person == null) {
			logger.log(Level.SEVERE,"selection.person == null");
			return;
		}
		/*
		personDataManager.persist(person, new Receiver<String>() {
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE, error);
			}
			@Override
			public void onSuccess(String t) {
				logger.log(Level.INFO,"OK : " + t);
				if (personDataView != null) {
					personDataView.showMessage(t);
				}				
			}
		});
		*/
	}
	
	@Override
	public void transferFingerprints() {
		Person person = selection.getSelectedObject();
		if (person == null) {
			logger.log(Level.SEVERE,"selection.person == null");
			//personDataView.showMessage("Error: debe seleccionar una persona");
			return;
		}
		
		if (person.getId() == null) {
			logger.log(Level.SEVERE,"selection.person.id == null");
			return;
		}
		String personId = person.getId();
		/*
		personDataManager.transferFingerprints(personId, new Receiver<Boolean>() {
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE, error);
			}
			@Override
			public void onSuccess(Boolean t) {
				logger.log(Level.INFO,"OK");
				if (personDataView != null) {
					personDataView.showMessage("La tranferencia de huellas se ha realizado exitosamente");
				}				
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

		
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				updateUsers();
			}
			@Override
			public void onFailure(Throwable reason) {
			}
		});
		
		
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
