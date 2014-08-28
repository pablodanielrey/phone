package ar.com.dcsys.phone.assistance.server;

import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.messages.server.MessageContext;
import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;
import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;
import ar.com.dcsys.gwt.messages.shared.TransportEvent;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.device.DevicesManager;
import ar.com.dcsys.model.device.EnrollAction;
import ar.com.dcsys.model.device.EnrollManager;
import ar.com.dcsys.person.server.PersonSerializer;
import ar.com.dcsys.security.Fingerprint;



public class PhoneMessageHandler implements MessageHandler {

	
	private final Event<TransportEvent> cdibus;
	private final DevicesManager devicesManager;
	private final PersonsManager personsManager;
	private final PersonSerializer personSerializer;
	
	/**
	 * Se registra dentro de los habdlers de los mensajes.
	 * Asi cuanod llegan mensajes es llamado para manejarlos.
	 * Si detecta mensajes válidos entonces llama a cada uno de los handlers de los métodos.
	 * @param handlers
	 */
	private void register(@Observes HandlersContainer<MessageHandler> handlers) {
		handlers.add(this);
	}	
	
	
	@Inject
	public PhoneMessageHandler(Event<TransportEvent> bus, DevicesManager devicesManager, PersonsManager personsManager, PersonSerializer personSerializer) {
		this.cdibus = bus;
		this.devicesManager = devicesManager;
		this.personsManager = personsManager;
		this.personSerializer = personSerializer;
	}
	
	
	private void sendEvent(String msg) {
		TransportEvent tr = new TransportEvent();
		tr.setMessage(msg);
		tr.setTransportReceiver(new TransportReceiver() {
			@Override
			public void onSuccess(String ok) {
			}
			@Override
			public void onFailure(String error) {
			}
		});
		tr.setType("enroll");
		cdibus.fire(tr);		
	}
	
	
	private void sendResponse(String id, MessageContext ctx, String msg) {
		ctx.getTransport().send(id, msg, new TransportReceiver() {
			@Override
			public void onSuccess(String message) {
			}
			@Override
			public void onFailure(String error) {
			}
		});
	}
	
	@Override
	public boolean handle(final String id, String msg, final MessageContext ctx) {

		if (msg.startsWith("userList")) {
			handleUserList(id,msg,ctx);
			return true;
		}
		
		if (msg.startsWith("enroll;")) {
			handleEnroll(id, msg, ctx);
			return true;
		}

		if (msg.startsWith("persistPerson;")) {
			handlePersistPerson(id, msg, ctx);
			return true;
		}

		if (msg.startsWith("transferFingerprints;")) {
			handleTransferFingerprints(id, msg, ctx);
			return true;
		}
		
		return false;
	}
	
	
	// TODO: mejorar el formato de mensaje.
	// TODO: armar un serializador de listas. o incluir en los serializers las listas.
	private void handleUserList(final String id, String msg, final MessageContext ctx) {

		try {
			List<Person> persons = personsManager.findAll();
			StringBuilder sb = new StringBuilder();
			sb.append("OK");
			for (Person p : persons) {
				String json = personSerializer.toJson(p);
				sb.append(json).append(";-;-;");
			}
			String resp = sb.toString();
			sendResponse(id, ctx, resp);
		
		} catch (PersonException e) {
			sendResponse(id, ctx, "ERROR" + e.getMessage());
		}		
		
		
	}	
	
	
	
	private void handleEnroll(final String id, String msg, final MessageContext ctx) {

		String personId = msg.substring("enroll;".length());
		
		try {
			EnrollManager em = new EnrollManager() {
				@Override
				public void onMessage(EnrollAction action) {
					sendEvent(action.toString());
				}
				
				@Override
				public void onSuccess(Fingerprint fingerprint) {
					sendResponse(id, ctx, fingerprint.getId());			
				}
			};
			
			devicesManager.enroll(personId, em);
		
		} catch (PersonException | DeviceException e) {
			sendResponse(id, ctx, e.getMessage());
		}		
		
		
	}	
	
	private void handlePersistPerson(final String id, String msg, final MessageContext ctx) {

		String personId = msg.substring("persistPerson;".length());
		
		try {
			Person p = personsManager.findById(personId);
			devicesManager.persist(p);
			sendResponse(id, ctx, "Usuario actualizado correctamente");			
		
		} catch (PersonException | DeviceException e) {
			sendResponse(id, ctx, e.getMessage());
		}		
		
		
	}
	
	private void handleTransferFingerprints(final String id, String msg, final MessageContext ctx) {

		String personId = msg.substring("transferFingerprints;".length());
		
		try {
			devicesManager.transferFingerprints(personId);
			sendResponse(id, ctx, "Transferencia exitosa");
		
		} catch (PersonException | DeviceException e) {
			sendResponse(id, ctx, e.getMessage());
		}		
		
		
	}
	

}
