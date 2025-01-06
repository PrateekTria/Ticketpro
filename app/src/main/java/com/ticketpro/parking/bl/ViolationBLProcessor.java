package com.ticketpro.parking.bl;


import java.util.ArrayList;
import java.util.ArrayList;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Comment;
import com.ticketpro.model.EulaModel;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.Violation;

import org.json.JSONObject;

import java.util.ArrayList;

public class ViolationBLProcessor extends BLProcessorImpl{

	public ViolationBLProcessor(){
		setLogger(ViolationBLProcessor.class.getName());
	}

	public ArrayList<Violation> getViolations() throws TPException{
		return proxy.getViolations();
	}

	public ArrayList<Violation> getViolations(String violationGroups) throws TPException{
		return proxy.getViolations(violationGroups);
	}

	public ArrayList<Comment> getComments() throws TPException{
		return proxy.getComments();
	}

	public boolean updateTicketComments(int violationId, ArrayList<TicketComment> ticketComments) throws TPException{
		return proxy.updateTicketsComments(violationId, ticketComments);
	}

	public boolean deleteTicketComment(long citationNumber, int violationId, String comment) throws TPException{
		return proxy.deleteTicketComment(citationNumber, violationId, comment);
	}

	public ArrayList<Comment> getComments(String commentGroups) throws TPException{
		return proxy.getComments(commentGroups);
	}

	public ArrayList<Comment> getCommentsById(int commentId[]) throws TPException{
		return proxy.getCommentsById(commentId);
	}

	public RepeatOffender repeatOffenderFromService(int custId, String plate, int violId, String stateCode) throws TPException{
		return proxy.repeatOffenderFromService(custId, plate, violId, stateCode);
	}
	public RepeatOffender repeatOffenderFromService(RepeatOffendersFromService repeatOffendersFromService) throws TPException{
		return proxy.repeatOffenderFromService(repeatOffendersFromService);
	}
	public boolean lastRepeatOffenderService(ArrayList<RepeatOffender> repeatOffenders) throws TPException{

		return proxy.lastUpDateRepeatOffender(repeatOffenders);
	}
	public ArrayList<RepeatOffender> getlastRepeatOffenderService(int custid, String timestamp) throws TPException{
		return proxy.getlastUpDateRepeatOffender(custid,timestamp);
	}

	public EulaModel getEulaText(int custId,int moduleId) throws Exception{
		return proxy.getEULAText(custId,moduleId);
	}
	public JSONObject getResult(int custid,int recid,String isAccepted,int custId) throws Exception{
		return proxy.updateEULAAcceptance(custid,recid,isAccepted, custId);
	}


}
