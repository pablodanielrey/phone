package ar.com.dcsys.phone.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class UsersPlace extends Place {
	
	public UsersPlace(String token) { }
	
	public UsersPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<UsersPlace> {

		@Override
		public UsersPlace getPlace(String token) {
			return new UsersPlace();
		}

		@Override
		public String getToken(UsersPlace place) {
			return null;
		}
		
	}

}
