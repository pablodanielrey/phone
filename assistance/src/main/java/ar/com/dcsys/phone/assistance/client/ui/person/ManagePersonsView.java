package ar.com.dcsys.phone.assistance.client.ui.person;

import java.util.List;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;


public interface ManagePersonsView extends IsWidget {

	void setPresenter(Presenter p);
	
	void clear();
	void setSelectionModel(SingleSelectionModel<Person> selection);
	void setPersons(List<Person> persons);
	
	
	interface Presenter {
		public void updateUsers();
		public void enroll();
		public void persist();
		public void transferFingerprints();
		
	}
	
}
