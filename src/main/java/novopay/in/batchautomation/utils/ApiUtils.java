package novopay.in.batchautomation.utils;

import static com.jayway.restassured.RestAssured.given;

import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.testng.Assert;
import org.testng.Reporter;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;
import com.jayway.restassured.response.Response;

import groovy.json.JsonOutput;

public class ApiUtils extends JavaUtils {

	String stri;
	String fileName;

	Properties velocityProps;

		public String postRequest(String apiName, String requestInStringFormat, Map<String, String> headers) {

		String server = configProperties.get("server.host.name");
		String path = configProperties.get("server.path");
		String serverURL = "https://" + server + path + apiName;

		double startTime = System.currentTimeMillis();

		Response post = given().relaxedHTTPSValidation().headers(headers).body(requestInStringFormat).when()
				.post(serverURL);

		double stopTime = System.currentTimeMillis();

		String response = post.asString();
		Reporter.log("\nServer URL : " + serverURL, true);

		Reporter.log("\nTotal time taken for the post request is : " + ((stopTime - startTime) / 1000) + " Seconds ...",
				true);

		Reporter.log("\nRequest sent is \n" + requestInStringFormat, true);

		Reporter.log("\nResponse Obtained : " + response, true);
		return response;
	}

		public String responseString(Response response) {

		System.out.println("\nResponse received is ");
		String responseInString = response.prettyPrint().toString();
		return responseInString;
	}

	/*
	 * Creates mpin encrypted session Token using Mpin-encryption Key and plain
	 * session token
	 */

	/*
	 * Returns the mpin-encrypted session token by encrypting the sessionToken
	 * of the device with the mpin public key from the server
	 */
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

	/*
	 * Returns the auth value by encrypting the mpin with sha256 and then with
	 * the public key from server
	 */

	/*
	 * Returns the auth value by encrypting the mpin with sha256 and then with
	 * the public key from server
	 */

	// generatessha256 for the data(mpinEncryptedSessionToken and payload)
	public String returnCheckSum(String data) {
		try {
			return new String(Hex.encodeHex(DigestUtils.sha256(data)));
		} catch (Exception var3) {
			var3.printStackTrace();
			return null;
		}
	}

	public byte[] encryptUsingPublicKey(PublicKey key, byte[] data) {
		byte[] cipherText = null;

		try {
			Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			e.init(1, key);
			cipherText = e.doFinal(data);
		} catch (Exception var4) {
			Reporter.log("Error while encrypting data using public key" + data, true);
		}

		return Base64.encodeBase64(cipherText);
	}

	public byte[] encryptUsingPublicKey(byte[] key, byte[] data) {
		PublicKey publicKey = null;

		try {
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
		} catch (Exception var4) {
			Reporter.log("Error while forming public key from byte array" + data, true);
		}

		return encryptUsingPublicKey(publicKey, data);
	}

	


	public HashMap<String, String> getAgentsWalletBalance(String mobileNumber) {
		DBUtils dbutils = new DBUtils();
		HashMap<String, String> walletRefNumber = new HashMap<String, String>();
		walletRefNumber = dbutils.getAgentsWalletRefNo(mobileNumber);

		HashMap<String, String> walletRefBalance = new HashMap<String, String>();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Basic bWlmb3M6cGFzc3dvcmQ=");
		map.put("X-Mifos-Platform-TenantId", "qa");

		// merchantWalletRefNo cashOutWalletRefNo agentWalletRefNo
		Iterator it = walletRefNumber.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			Response get = given().headers(map).contentType("application/json")
					.get(configProperties.get("walletServer") + "balanceinquiry/" + pair.getValue());
			JsonPath jsonPath = new JsonPath(get.prettyPrint().toString());
			if (pair.getKey().equals("merchantWalletRefNo")) {
				walletRefBalance.put("MERCHANT_WALLET", jsonPath.get("accountBalance").toString());
				walletRefBalance.put("MERCHANT_REF", pair.getValue().toString());
			} else if (pair.getKey().equals("cashOutWalletRefNo")) {
				walletRefBalance.put("CASHOUT_WALLET", jsonPath.get("accountBalance").toString());
				walletRefBalance.put("CASHOUT_REF", pair.getValue().toString());
			} else if (pair.getKey().equals("agentWalletRefNo")) {
				walletRefBalance.put("AGENT_WALLET", jsonPath.get("accountBalance").toString());
				walletRefBalance.put("AGENT_REF", pair.getValue().toString());
			}
			it.remove(); // avoids a ConcurrentModificationException
		}
		System.out.println(walletRefBalance);
		return walletRefBalance;
	}

	public String getWalletBalance(String walletNumber) {
		System.out.println(configProperties.get("wallet.server") + walletNumber);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Basic bWlmb3M6cGFzc3dvcmQ=");
		map.put("X-Mifos-Platform-TenantId", "qa");
		Response get = given().headers(map).contentType("application/json")
				.get(configProperties.get("wallet.server") + "balanceinquiry/" + walletNumber);
		JsonPath jsonPath = new JsonPath(get.prettyPrint().toString())
				.using(new JsonPathConfig(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
		System.out.println(String.valueOf(jsonPath.getDouble("accountBalance")));
		return String.valueOf(jsonPath.getDouble("accountBalance"));

	}

	
	public void depositAmountToWallet(String walletNumber, String amount) {
		String url =configProperties.get("wallet.server") + "batches";
		System.out.println(url);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic bWlmb3M6cGFzc3dvcmQ=");
		headers.put("X-Mifos-Platform-TenantId", "qa");
		headers.put("Content-Type", "application/json");
		System.out.println(headers);
		String requestInStringFormat = "[{ " + "\"relativeUrl\": \"transfertransaction\", " + "\"method\": \"POST\",  "
				+ "\"body\": {  " + "\"paymentTypeName\": \"ADJUSTMENT\", " + "\"externalRefNo\": \""
				+ generateRandomNo(10) + "\", " + "\"transactionAmount\": \"" + amount + "\",  "
				+ "\"transactionDate\": \"" + getTodaysDate("dd MMMM yyyy") + "\", "
				+ "\"dateFormat\": \"dd MMMM yyyy\",  " + "\"locale\": \"en\", " + "\"creditAccount\": { "
				+ "\"accountNo\": \"" + walletNumber + "\", " + "\"narration\": \"Deposit Through Automation\" " + "}  "
				+ "} " + "}]";

		System.out.println(requestInStringFormat);

		Response response = given().headers(headers).body(requestInStringFormat).when()
				.post(url);

		response.prettyPrint().toString();
	}

	public void withdrawAmountFromWallet(String walletNumber, String amount) {

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic bWlmb3M6cGFzc3dvcmQ=");
		headers.put("X-Mifos-Platform-TenantId", "qa");

		String requestInStringFormat = "[{ " + "\"relativeUrl\": \"transfertransaction\", " + "\"method\": \"POST\",  "
				+ "\"body\": {  " + "\"paymentTypeName\": \"ADJUSTMENT\", " + "\"externalRefNo\": \""
				+ generateRandomNo(10) + "\", " + "\"transactionAmount\": \"" + amount + "\",  "
				+ "\"transactionDate\": \"" + getTodaysDate("dd MMMM yyyy") + "\", "
				+ "\"dateFormat\": \"dd MMMM yyyy\",  " + "\"locale\": \"en\", " + "\"debitAccount\": { "
				+ "\"accountNo\": \"" + walletNumber + "\", " + "\"narration\": \"Withdrawal Through Automation\" "
				+ "}  " + "} " + "}]";

		System.out.println(requestInStringFormat);

		Response response = given().headers(headers).contentType("application/json").body(requestInStringFormat).when()
				.post(configProperties.get("walletServer") + "batches");

		response.prettyPrint().toString();
	}
	public String getOTP(String remitterMobileNumber) {
		DBUtils dbUtils = new DBUtils();

		String message = null;

		message = dbUtils.getOTPMessage(remitterMobileNumber);

		String pattern = "(\\d{6})";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(message);
		if (m.find()) {
			return m.group(0);

		}
		return null;
	}
	
	public String returnAesOrMpinKey(String response) {// keyValue

		JsonPath jsonPath = new JsonPath(response);
		String aesKey = jsonPath.get("keyValue");
		return aesKey;
	}
	public String[] generateRandomDeviceIdAndSimValue() {

		Random random = new Random();
		int deviceId = random.nextInt(999999999);
		int simNo = random.nextInt(999999999);

		String deviceID = "I911334" + deviceId;
		String simNumber = "89910341110" + simNo;

		return new String[] { deviceID, simNumber };
	}
	

}