package ar.com.dcsys.phone.assistance.client.place;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PersonDataPlace extends Place {
	
	private Person person;
	
	public PersonDataPlace(Person person) {
		this.person = person;
	}
	
	public PersonDataPlace(String token) { 
		person = new Person();
		person.setDni(token);
	}
	
	public PersonDataPlace() { }
	
	public Person getPerson() {
		return person;
	}
	
	public static class Tokenizer implements PlaceTokenizer<PersonDataPlace> {

		@Override
		public PersonDataPlace getPlace(String token) {
			return new PersonDataPlace(token);
		}

		@Override
		public String getToken(PersonDataPlace place) {
			return place.getPerson().getDni();
		}
		
	}

}
