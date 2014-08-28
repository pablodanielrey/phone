package ar.com.dcsys.phone.assistance.client.ui.person;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.phone.assistance.client.cell.PersonCell;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedHandler;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;

public class ManagePersons extends Composite implements ManagePersonsView {

//	private static ManagePersonsUiBinder uiBinder = GWT.create(ManagePersonsUiBinder.class);

//	interface ManagePersonsUiBinder extends UiBinder<Widget, ManagePersons> {
//	}
	
	
//	@UiField(provided=true) CellList<Person> users;
	private CellList<Person> users;
	private RootFlexPanel panel = new RootFlexPanel();

	
	private SingleSelectionModel<Person> selection;
	
	private final CellSelectedHandler handler = new CellSelectedHandler() {
		@Override
		public void onCellSelected(CellSelectedEvent event) {
			int i = event.getIndex();
			if (persons != null && selection != null && persons.size() > i) {
				Person selected = persons.get(i);
				if (selected != null) {
					selection.setSelected(selected, true);
				} else {
					selection.clear();
				}
			}
		}
	};
	
	
	private Presenter p;
	private List<Person> persons;
	
	private void createUsers() {
		users = new CellList<Person>(new PersonCell());
		users.addCellSelectedHandler(handler);
	}
	
	public ManagePersons() {
		createUsers();
		panel.add(users);
		
		initWidget(panel);
	}

	
	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		persons = new ArrayList<Person>();
		users.render(persons);
	}

	@Override
	public void setPersons(List<Person> persons) {
		this.persons = persons;
		users.render(this.persons);
	}
	
	@Override
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		this.selection = selection;
	}

}
