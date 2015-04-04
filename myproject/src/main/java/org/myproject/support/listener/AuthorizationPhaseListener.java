package org.myproject.support.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class AuthorizationPhaseListener implements PhaseListener {

	private static final long serialVersionUID = -4702982677751837844L;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return null;
	}

//	em faces-config.xml colocar
//	<lifecicle>
//	<phase-listner>org.myproject.support.listener.AuthorizationPhaseListener</phase-listner>
//	</lifecicle>

}
