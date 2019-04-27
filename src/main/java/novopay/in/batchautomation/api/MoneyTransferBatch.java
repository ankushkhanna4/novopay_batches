package novopay.in.batchautomation.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import novopay.in.batchautomation.api.pojo.wallettransferapi.AssistorDetails;
import novopay.in.batchautomation.api.pojo.wallettransferapi.MetaData;
import novopay.in.batchautomation.api.pojo.wallettransferapi.MoneyTransferBatchPOJO;
import novopay.in.batchautomation.api.pojo.wallettransferapi.PayeeDetails;
import novopay.in.batchautomation.api.pojo.wallettransferapi.PayerDetails;
import novopay.in.batchautomation.api.pojo.wallettransferapi.Request;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.DBUtils;

public class MoneyTransferBatch extends ApiUtils {
	GetEncryptionKey getEncryptionKey;
	public String moneyTransferResponse;

	public String getRequestBody(HashMap<String, String> usrData, String encryptedMpinValue) throws Exception {
		DBUtils dbUtils = new DBUtils();
		MoneyTransferBatchPOJO moneyTransferBatchPOJO = new MoneyTransferBatchPOJO();
		Request request = new Request();
		PayerDetails payerdetails = new PayerDetails();
		payerdetails.setAccountType("CUSTOMER");
		payerdetails.setAccountValue("");
		payerdetails.setActorType("CUSTOMER");
		payerdetails.setAuthType("");
		payerdetails.setAuthValue("");
		payerdetails.setHandleType("MSISDN");
		payerdetails.setHandleValue(usrData.get("Remitter"));

		PayeeDetails payeedetails = new PayeeDetails();
		payeedetails.setAccountType("BANK_ACCOUNT");
		payeedetails.setAccountValue(dbUtils.returnAccountDetailsFromDB(usrData.get("Beneficiary"))[1]);
		payeedetails.setActorType("CUSTOMER");
		payeedetails.setHandleType("MSISDN");
		payeedetails.setHandleValue(usrData.get("Beneficiary"));

		MetaData metadata = new MetaData();
		metadata.setDataLabel("remittanceType");
		metadata.setDataValue("C2A");
		List<MetaData> metadataList = new ArrayList<MetaData>();
		metadataList.add(metadata);

		AssistorDetails assitordetails = new AssistorDetails();
		assitordetails.setAccountType("");
		assitordetails.setAccountValue(usrData.get(""));
		assitordetails.setActorType("AGENT");
		assitordetails.setAuthType("MPIN");
		assitordetails.setAuthValue(encryptedMpinValue);
		assitordetails.setHandleType("MSISDN");
		assitordetails.setHandleValue(usrData.get("Retailer"));

		request.setClientRefNumber(dbUtils.generateRandomClientRefNumber());
		request.setOperationMode("ASSISTED");
		request.setPartnerId("NOVOPAY");
		request.setTransactionAmount(usrData.get("Amount"));
		request.setTransferMode(usrData.get("TransferMode"));
		request.setTransferType("C2A");
		request.setPayerDetails(payerdetails);
		request.setPayeeDetails(payeedetails);
		request.setMetaData(metadataList);
		request.setAssistorDetails(assitordetails);
		request.setPartnerId(usrData.get("Bank"));
		
		moneyTransferBatchPOJO.setRequest(request);
		return new JSONObject(moneyTransferBatchPOJO).toString();

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

	public void moneyTransferTest(HashMap<String, String> usrData) {
		getEncryptionKey = new GetEncryptionKey();
		String encryptedMpinValue;
		try {
			encryptedMpinValue = getEncryptionKey.getEncryptionKey(usrData);
		
		String sessionToken = returnSessionTokenForAuthentication(usrData);

		String moneyTransferInitiateRequest = getRequestBody(usrData, encryptedMpinValue);
		String checksum = returnCheckSum(sessionToken + moneyTransferInitiateRequest);

		Map<String, String> moneyTransferInitiateHeaders = getHeaders(usrData, sessionToken, checksum);

		 moneyTransferResponse = postRequest("moneyTransferBatch", moneyTransferInitiateRequest,
				moneyTransferInitiateHeaders);

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
