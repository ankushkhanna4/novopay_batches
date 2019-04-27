package novopay.in.batchautomation;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

import novopay.in.batchautomation.api.MoneyTransferBatch;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class AxisIMPSStatusEnquiry extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "AxisIMPSStatusEnquiry", fileName, vanNumber, batchSheetName,
			batchConfigSection = "axisimpsstatusenquiry", agentWalletRefNo, agentWalletRemainingBal;
	HashMap<String, String> databaseConfig, batchFileConfig;
	private HashMap<String, String> usrData;
	MoneyTransferBatch moneyTransferBatch;
	private String[] payementCode;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider = "getTestData")
	public void axisAgentWalletTopupTest(HashMap<String, String> usrData) {

		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		// databaseConfig = dbUtils.getConfig(batchFileConfig);
		agentWalletRefNo = dbUtils.getAgentsWalletRefNo(usrData.get("Retailer")).get("agentWalletRefNo");
		// agentWalletRemainingBal =
		// apiUtils.getWalletBalance(agentWalletRefNo);
		apiUtils.depositAmountToWallet(agentWalletRefNo, usrData.get("Amount"));

		// upload file based on type of transaction
		uploadFile(batchFileConfig, "default");

		//

		// money transfer
		moneyTransferBatch = new MoneyTransferBatch();
		moneyTransferBatch.moneyTransferTest(usrData);
		String paymentRefCode = new JsonPath(moneyTransferBatch.moneyTransferResponse)
				.get("txnResponses[0].response.novopayRefNumber").toString();
		payementCode = dbUtils.getPaymentStatusAndRRNNo(paymentRefCode);
		Assert.assertEquals(payementCode[0], "UNKNOWN", "FAIL !!! Payment Status not unknown");
		uploadFile(batchFileConfig, usrData.get("Status"));
		if(usrData.get("Status").equalsIgnoreCase("success")){
			batchFileConfig.put("batch.execution.summary","response string :00\\|Success\\|"+paymentRefCode);
		} else if(usrData.get("Status").equalsIgnoreCase("fail")){
			batchFileConfig.put("batch.execution.summary","response string :92\\|Success\\|"+paymentRefCode);
		} else if(usrData.get("Status").equalsIgnoreCase("unknown")){
			batchFileConfig.put("batch.execution.summary","response string :T1\\|Success\\|"+paymentRefCode);
		}
		executeBatch(batchFileConfig);
		Assert.assertEquals(dbUtils.getPaymentStatusAndRRNNo(paymentRefCode)[0].toLowerCase(),usrData.get("Status").toLowerCase(),"Fail :: Status not equal");
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
