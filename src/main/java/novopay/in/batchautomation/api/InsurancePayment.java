package novopay.in.batchautomation.api;

import java.util.HashMap;

import org.json.JSONObject;

import novopay.in.batchautomation.api.pojo.insurancepayment.*;
public class InsurancePayment {
	
	
	public String getRequestBody(HashMap<String, String> usrData, String encryptedMpinValue) throws Exception {
		
		InsurancePaymentPojo insurancePaymentPojo = new InsurancePaymentPojo();
		ActorDetails actorDetails = new ActorDetails();
		actorDetails.setActorType("AGENT");
		actorDetails.setHandleType("MSISDN");
		actorDetails.setHandleValue("");
		AssistorDetails assistorDetails = new AssistorDetails();
		assistorDetails.setActorType("");
		assistorDetails.setAuthType("");
		assistorDetails.setAuthValue("");
		assistorDetails.setHandleType("");
		assistorDetails.setHandleValue("");
		Request request = new Request();
		request.setActorDetails(actorDetails);
		request.setAssistorDetails(assistorDetails);
		request.setClientRefNumber("");
		request.setOperationMode("");
		request.setPartnerId("");
		request.setPaymentMode("");
		request.setRemarks("");
		request.setTransactionAmount("");
		request.setTransactionValueDate("");
		insurancePaymentPojo.setRequest(request);
		
		
		return new JSONObject(insurancePaymentPojo).toString();
		
	}

}
