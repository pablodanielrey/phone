package ar.com.dcsys.phone.assistance.client.cell;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.googlecode.mgwt.ui.client.widget.list.celllist.Cell;



public class PersonCell implements Cell<Person> {
	
	private static PersonTemplate TEMPLATE = GWT.create(PersonTemplate.class);
	
	public interface PersonTemplate extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div><div>{0}</div><div>{1}</div><div>{2}</div></div>")
		SafeHtml content(String name, String lastname, String dni);
	}	
	
	@Override
	public void render(SafeHtmlBuilder safeHtmlBuilder, Person model) {
		if (model == null) {
			return;
		}
		String name = model.getName() != null ? model.getName() : "";
		String lastname = model.getLastName() != null ? model.getLastName() : "";
		String dni = model.getDni() != null ? model.getDni() : "";
		
		SafeHtml content = TEMPLATE.content(name, lastname, dni);
		safeHtmlBuilder.append(content);
	}
	@Override
	public boolean canBeSelected(Person model) {
		return true;
	}
}
