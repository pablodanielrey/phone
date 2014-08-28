package ar.com.dcsys.phone.assistance.client.place;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PersonDataPlace extends Place {
	
	private Person person;
	
	public PersonDataPlace(Person person) {
		this.person = person;
	}
	
	public PersonDataPlace(String id, String dni, String name, String lastname) {
		
		person = new Person();
		person.setId(id);
		person.setDni(dni);
		person.setName(name);
		person.setLastName(lastname);
		
	}
	
	public PersonDataPlace() { }
	
	public Person getPerson() {
		return person;
	}
	
	public static class Tokenizer implements PlaceTokenizer<PersonDataPlace> {

		@Override
		public PersonDataPlace getPlace(String token) {
			
			String decoded = URL.decode(token);
			String[] params = decoded.split(";");
			
			if (params.length < 4) {
				return new PersonDataPlace();
			} else {
				String id = params[0];
				String dni = params[1];
				String name = params[2];
				String lastname = params[3];
				
				return new PersonDataPlace(id,dni,name,lastname);
			}
		}

		@Override
		public String getToken(PersonDataPlace place) {
			
			Person person = place.getPerson();
			String id = person.getId();
			String dni = person.getDni();
			String name = person.getName();
			String lastname = person.getLastName();
			
			String decoded = id + ";" + dni + ";" + name + ";" + lastname;
			String token = URL.encode(decoded);
			
			return token;
		}
		
	}

}
