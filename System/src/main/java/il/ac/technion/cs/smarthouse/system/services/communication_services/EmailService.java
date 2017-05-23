package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public class EmailService extends CommunicationService {

	public EmailService(final SystemCore $) {
		super($);
	}

	@SuppressWarnings("static-method")
	public void sendMsg(final String destEmail, final String msg) {
		Communicate.throughEmailFromHere(destEmail, msg);
	}

}
