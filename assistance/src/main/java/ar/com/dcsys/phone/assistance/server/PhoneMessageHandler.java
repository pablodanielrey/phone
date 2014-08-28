package ar.com.dcsys.phone.assistance.server;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.messages.server.MessageContext;
import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;
import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;
import ar.com.dcsys.gwt.messages.shared.TransportEvent;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.model.device.DevicesManager;
import ar.com.dcsys.model.device.EnrollAction;
import ar.com.dcsys.model.device.EnrollManager;
import ar.com.dcsys.security.Fingerprint;



public class PhoneMessageHandler implements MessageHandler {

	
	private final Event<TransportEvent> cdibus;
	private final DevicesManager devicesManager;
	
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
	public PhoneMessageHandler(Event<TransportEvent> bus, DevicesManager devicesManager) {
		this.cdibus = bus;
		this.devicesManager = devicesManager;
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

		if (!msg.startsWith("enroll;")) {
			return false;
		}

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
		
		return true;
	}

}
