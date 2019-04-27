package novopay.in.batchautomation.api;

import java.util.HashMap;

import org.json.JSONObject;

import novopay.in.batchautomation.api.pojo.insuranceproposal.AssistorDetails;
import novopay.in.batchautomation.api.pojo.insuranceproposal.CustomerDetails;
import novopay.in.batchautomation.api.pojo.insuranceproposal.CustomerNomineeDetails;
import novopay.in.batchautomation.api.pojo.insuranceproposal.InsuranceProposalPojo;
import novopay.in.batchautomation.api.pojo.insuranceproposal.PreviousPolicyDetails;
import novopay.in.batchautomation.api.pojo.insuranceproposal.Request;
import novopay.in.batchautomation.api.pojo.insuranceproposal.VehicleDetails;
import novopay.in.batchautomation.utils.DBUtils;

public class InsuranceProposal {
	DBUtils dbUtils = new DBUtils();
	public String getRequestBody(HashMap<String, String> usrData, String encryptedMpinValue) throws Exception {
		
		InsuranceProposalPojo insuranceproposal = new InsuranceProposalPojo();
		AssistorDetails assistorDetails = new AssistorDetails();
		assistorDetails.setActorType("AGENT");
		assistorDetails.setHandleType("MSISDN");
		assistorDetails.setHandleValue(usrData.get("Retailer"));
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAddressLine1(usrData.get("AddressLine1"));
		customerDetails.setAddressLine2(usrData.get("AddressLine2"));
		customerDetails.setAddressLine3(usrData.get("AddressLine3"));
		customerDetails.setLandmark(usrData.get("Landmark"));
		customerDetails.setCity(dbUtils.getCityCode(usrData.get("Pincode")));
		customerDetails.setDistrict(dbUtils.getDistrictCode(usrData.get("Pincode")));
		customerDetails.setState(dbUtils.getStateCode(usrData.get("Pincode")));
		customerDetails.setFirstName(usrData.get("FirstName"));
		customerDetails.setLastName(usrData.get("LastName"));
		customerDetails.setMiddleName(usrData.get("MiddleName"));
		customerDetails.setMobileNumber(usrData.get("Mobilenumber"));
		customerDetails.setPincode(usrData.get("Pincode"));
		customerDetails.setSalutation(usrData.get("Salutation"));
		CustomerNomineeDetails customerNomineeDetails = new CustomerNomineeDetails();
		customerNomineeDetails.setAge(usrData.get("NomineeAge"));
		customerNomineeDetails.setName(usrData.get("NomineeName"));
		customerNomineeDetails.setRelationship(usrData.get("NomineeRelationship"));
		VehicleDetails vehicleDetails = new VehicleDetails();
		
		PreviousPolicyDetails previousPolicyDetails = new PreviousPolicyDetails();
		previousPolicyDetails.setInsurerName(usrData.get("InsurerName"));
		previousPolicyDetails.setPolicyNumber(usrData.get("PolicyNumber"));
		Request request = new Request();
		request.setAssistorDetails(assistorDetails);
		request.setCustomerDetails(customerDetails);
		request.setCustomerNomineeDetails(customerNomineeDetails);
		request.setInsuranceType("TWO_WHEELER");
		request.setOperationMode("ASSISTED");
		request.setPartnerId("AIG");
		request.setPreviousPolicyDetails(previousPolicyDetails);
		request.setQuoteNumber("");
		request.setVehicleDetails(vehicleDetails);
		insuranceproposal.setRequest(request);
		return new JSONObject(insuranceproposal).toString();
	}

}
