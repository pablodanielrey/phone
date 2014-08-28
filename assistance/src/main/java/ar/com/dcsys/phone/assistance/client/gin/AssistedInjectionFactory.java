package ar.com.dcsys.phone.assistance.client.gin;

import ar.com.dcsys.phone.assistance.client.activity.ManagePersonsActivity;
import ar.com.dcsys.phone.assistance.client.activity.PersonDataActivity;
import ar.com.dcsys.phone.assistance.client.place.PersonDataPlace;
import ar.com.dcsys.phone.assistance.client.place.UsersPlace;



public interface AssistedInjectionFactory {
	
	public ManagePersonsActivity usersActivity(UsersPlace place);
	public PersonDataActivity personDataActivity(PersonDataPlace place);
	
}
