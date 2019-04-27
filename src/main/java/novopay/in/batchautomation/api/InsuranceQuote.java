package novopay.in.batchautomation.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import novopay.in.batchautomation.api.pojo.insurancequote.AssistorDetails;
import novopay.in.batchautomation.api.pojo.insurancequote.InsuranceQuotePojo;
import novopay.in.batchautomation.api.pojo.insurancequote.PreviousPolicyDetails;
import novopay.in.batchautomation.api.pojo.insurancequote.Request;
import novopay.in.batchautomation.api.pojo.insurancequote.VehicleDetails;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class InsuranceQuote extends ApiUtils {
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	GetEncryptionKey getEncryptionKey;
	public String response;
	public String getRequestBody(HashMap<String, String> usrData, String encryptedMpinValue) throws Exception {
		
		InsuranceQuotePojo insurancequote = new InsuranceQuotePojo();
		Request request = new Request();
		AssistorDetails assistorDetails = new AssistorDetails();
		assistorDetails.setActorType("AGENT");
		assistorDetails.setHandleType("MSISDN");
		assistorDetails.setHandleValue(usrData.get("Retailer"));
		VehicleDetails vehicleDetails = new VehicleDetails();
		vehicleDetails.setRegistrationDate(javaUtils.epochConvertor(usrData.get("VehicleRegistrationDate")));
		vehicleDetails.setRegistrationNumber1(usrData.get("RegistrationNumber1"));
		vehicleDetails.setRegistrationNumber2(usrData.get("RegistrationNumber2"));
		vehicleDetails.setRegistrationNumber3(usrData.get("RegistrationNumber3"));
		vehicleDetails.setRegistrationNumber4(usrData.get("RegistrationNumber4"));
		vehicleDetails.setVariant(dbUtils.getVariantId(usrData.get("Variant")));
		vehicleDetails.setYearOfManufacture(usrData.get("YearOfManufacture"));
		vehicleDetails.setManufacturer(dbUtils.getManufacturerId(usrData.get("Manufacturer")));
		vehicleDetails.setModel(dbUtils.getModelId(usrData.get("VehicleModel")));
		vehicleDetails.setCityUsed(usrData.get("CityUsed"));
		PreviousPolicyDetails previousPolicyDetails = new PreviousPolicyDetails();
		previousPolicyDetails.setEffectiveTo(javaUtils.epochConvertor(usrData.get("PolicyEffectiveTo")));
		previousPolicyDetails.setNcbApplicable(false);
		previousPolicyDetails.setNcbPercentage("0");
		request.setApplicableNCBPercentage("20");
		request.setAssistorDetails(assistorDetails);
		request.setEffectiveDate(javaUtils.epochConvertor(usrData.get("EffectiveDate")));
		request.setInsuranceType("TWO_WHEELER");
		request.setOperationMode("ASSISTED");
		request.setPartnerId("AIG");
		request.setPreviousPolicyDetails(previousPolicyDetails);
		request.setTerm(usrData.get("Term"));
		request.setVehicleDetails(vehicleDetails);
		insurancequote.setRequest(request);
		
		return new JSONObject(insurancequote).toString();
	}
	
	public HashMap<String, String> getHeaders(HashMap<String, String> usrData, String sessionToken, String checksum) {// request
		String mobileNumber = usrData.get("Retailer");
		DBUtils dbutils = new DBUtils();
		String[] deviceDetails = dbutils.returnDeviceIdAndSimNoFromMaster(mobileNumber);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		headers.put("clientid", "NOVOPAY");
		headers.put("authorization", sessionToken);
		headers.put("checksum", checksum);
		headers.put("stan", returnRandomNumber());
		headers.put("transmissiondate", getTodaysDate("yyyy-MM-dd HH:mm:ss"));
		headers.put("deviceid", deviceDetails[0]);
		headers.put("simnumber", deviceDetails[1]);
		headers.put("useridtype", "MSISDN");
		headers.put("useridvalue", usrData.get("Retailer"));
		headers.put("functioncode", "DEFAULT");
		headers.put("functionsubcode", "DEFAULT");
		headers.put("channelId", "AGENTAPP");
		headers.put("runmode", "REAL");
		System.out.println(headers);
		return headers;
	}
	
	public void insuranceQuoteTest(HashMap<String, String> usrData) {
		getEncryptionKey = new GetEncryptionKey();
		String encryptedMpinValue;
		try {
			encryptedMpinValue = getEncryptionKey.getEncryptionKey(usrData);
		
		String sessionToken = returnSessionTokenForAuthentication(usrData);

		String request = getRequestBody(usrData, encryptedMpinValue);
		String checksum = returnCheckSum(sessionToken + request);

		Map<String, String> headers = getHeaders(usrData, sessionToken, checksum);

		response = postRequest("insuranceQuote", request,
				headers);
		System.out.println(response);
		// Assert transaction successful with the help of wallet API's
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String returnSessionTokenForAuthentication(HashMap<String, String> usrData) {

		DBUtils dbutils = new DBUtils();
		String uuid = dbutils.returnUuidForMaster(usrData.get("Retailer"));
		String sessionToken = dbutils.returnSessionTokenFromAuthentication(uuid);
		String authSessionToken = getEncryptedSessionToken(sessionToken, getEncryptionKey.mpinKeyFromTheApi);

		return authSessionToken;
	}

	public String getEncryptedSessionToken(String sessionToken, String mpinEncryptionKeyFromTheApi) {

		byte[] encryptedMpinKey = new byte[0];
		String encryptedMpinKeyString;
		try {
			byte[] publicKey = Base64.decodeBase64(mpinEncryptionKeyFromTheApi);

			encryptedMpinKey = encryptUsingPublicKey(publicKey, sessionToken.getBytes());
			encryptedMpinKeyString = Base64.encodeBase64String(encryptedMpinKey);
			return encryptedMpinKeyString;
		} catch (Exception e) {
		}
		return null;
	}
	
	
}
