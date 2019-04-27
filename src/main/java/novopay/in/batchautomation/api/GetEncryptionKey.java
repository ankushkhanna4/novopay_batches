package novopay.in.batchautomation.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import com.jayway.restassured.path.json.JsonPath;
import org.apache.commons.codec.binary.Base64;
import novopay.in.batchautomation.api.pojo.getencryption.GetEncryption;
import novopay.in.batchautomation.api.pojo.getencryption.Request;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.DBUtils;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class GetEncryptionKey extends ApiUtils {

	DBUtils dbUtils = new DBUtils();
	public String mpinKeyFromTheApi;

	public String getRequestBody(HashMap<String, String> usrData) throws Exception {

		Request request = new Request();
		request.setKeyType("MPIN-ENCRYPTION-KEY");
		request.setPartnerId(usrData.get("Bank"));
		GetEncryption getEncryption = new GetEncryption();
		getEncryption.setRequest(request);

		return new JSONObject(getEncryption).toString();
	}

	public HashMap<String, String> getHeaders(HashMap<String, String> usrData) {

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("clientid", "NOVOPAY");
		String[] deviceDetails = dbUtils.returnDeviceIdAndSimNoFromMaster(usrData.get("Retailer"));
		headers.put("deviceId", deviceDetails[0]);
		headers.put("simnumber", deviceDetails[1]);
		headers.put("channelId", "AGENTAPP");
		return headers;
	}

	public String getEncryptionKey(HashMap<String, String> usrData) throws Exception {

		Map<String, String> headers = getHeaders(usrData);
		String request = getRequestBody(usrData);
		String response = postRequest("getEncryptionKey", request, headers);
		String sha256hex = DigestUtils.sha256Hex(usrData.get("Mpin"));

		byte[] encryptedMpinKey = new byte[0];
		String encryptedMpinKeyString;
		String systemSha256Hash = System.currentTimeMillis() + "_" + sha256hex;
		try {
			JsonPath jsonPath = new JsonPath(response);
			mpinKeyFromTheApi = jsonPath.get("keyValue").toString();
			byte[] publicKey = Base64.decodeBase64(mpinKeyFromTheApi);
			encryptedMpinKey = encryptUsingPublicKey(publicKey, systemSha256Hash.getBytes());
			encryptedMpinKeyString = Base64.encodeBase64String(encryptedMpinKey);
			return encryptedMpinKeyString;
		} catch (Exception e) {
		}
		return null;
	}

}
