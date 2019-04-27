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
import novopay.in.batchautomation.utils.EmailUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class AxisIMPSRefund extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "AxisIMPSRefund", fileName, vanNumber, batchSheetName,
			batchConfigSection = "axisimpsrefund", agentWalletRefNo, agentWalletRemainingBal;
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
//		agentWalletRemainingBal = apiUtils.getWalletBalance(agentWalletRefNo);
		apiUtils.depositAmountToWallet(agentWalletRefNo, usrData.get("Amount"));

		//money transfer
		moneyTransferBatch = new MoneyTransferBatch();
		moneyTransferBatch.moneyTransferTest(usrData);
		String paymentRefCode = new JsonPath(moneyTransferBatch.moneyTransferResponse).get("txnResponses[0].response.novopayRefNumber").toString();
		payementCode = dbUtils.getPaymentStatusAndRRNNo(paymentRefCode);
		Assert.assertEquals(payementCode[0],"UNKNOWN","FAIL !!! Payment Status not unkown");
		
		batchSheetName = batchFileConfig.get("batch.sheet.name");
		fileName = batchFileConfig.get("batch.file.name");
		generateFile();
		databaseConfig = dbUtils.getConfig(batchFileConfig);
		String mailedFileName = new EmailUtils().emailTopUpFile(databaseConfig, fileName);
		batchFileConfig.put("batch.execution.summary", "SUCCESS(.*)"+mailedFileName
		+ " written successfully..");
		executeBatch(batchFileConfig);
		
		
		
		//get rrn number

		
	}

	public void generateFile() {
		String[][] data = new String[][] { 
			{ "RRN", payementCode[1] }, 
			{ "AMOUNT/100", usrData.get("Amount")},
			{ "RESPONSECODE", "91" } };

		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data, 0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
