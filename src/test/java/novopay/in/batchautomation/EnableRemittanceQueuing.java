package novopay.in.batchautomation;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

import novopay.in.batchautomation.api.MoneyTransferBatch;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class EnableRemittanceQueuing {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "EnableRemittanceQueuingBatch";
	HashMap<String, String> databaseConfig, batchFileConfig;
	private HashMap<String, String> usrData;
	MoneyTransferBatch moneyTransferBatch;
	String prevBank = null, currBank = null;
	public JsonPath path;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider = "getTestData")
	public void enableRemittanceQueuingBatchTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		MoneyTransferBatch moneyTransferBatch = new MoneyTransferBatch();
		currBank = usrData.get("Bank");
		if (prevBank == null) {
			dbUtils.insertIntoNpRemittanceConfig(usrData.get("Bank"),usrData.get("Errorcode"), usrData.get("Threshold"));
			dbUtils.updateThreshold(usrData.get("Bank"), usrData.get("Errorcode"), usrData.get("Threshold"));
			for (int i = 0; i < Integer.parseInt(usrData.get("Threshold")); i++) {
				moneyTransferBatch.moneyTransferTest(usrData);
			}
		} else if (prevBank != currBank) {
			dbUtils.insertIntoNpRemittanceConfig(usrData.get("Bank"),usrData.get("Errorcode"), usrData.get("Threshold"));
			dbUtils.updateThreshold(usrData.get("Bank"), usrData.get("Errorcode"), usrData.get("Threshold"));
			for (int i = 0; i < Integer.parseInt(usrData.get("Threshold")); i++) {
				moneyTransferBatch.moneyTransferTest(usrData);
			}
		}
		moneyTransferBatch.moneyTransferTest(usrData);
		path = new JsonPath(moneyTransferBatch.moneyTransferResponse);
		String statusCode = path.get("txnResponses[0].response.responseStatus.code").toString();
		Assert.assertEquals(statusCode, "007","Failed StatusCode Not Equal");
	}

	@AfterMethod
	public void bank() {
		prevBank = currBank;
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}

}
