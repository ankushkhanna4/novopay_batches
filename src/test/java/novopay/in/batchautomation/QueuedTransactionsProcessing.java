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

public class QueuedTransactionsProcessing extends BatchUtils{

	
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "EnableRemittanceQueuingBatch", batchConfigSection="queuedtransactionprocessing";
	HashMap<String, String> databaseConfig,batchFileConfig;
	private HashMap<String, String> usrData;


	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider="getTestData")
	public void axisAgentWalletTopupTest(HashMap<String, String> usrData) throws InterruptedException {
		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		 uploadFile(batchFileConfig, "fail");
		 EnableRemittanceQueuing enableRemittanceQueuing = new EnableRemittanceQueuing();
		 enableRemittanceQueuing.enableRemittanceQueuingBatchTest(usrData);
		 String paymentRefCode =enableRemittanceQueuing.path.get("txnResponses[0].response.novopayRefNumber").toString();
		 uploadFile(batchFileConfig, "success");
		 batchFileConfig.put("batch.execution.summary","Request: .*request.*paymentRefCode.*"+paymentRefCode);
		 executeBatch(batchFileConfig);
		 Thread.sleep(3000);
		Assert.assertEquals(dbUtils.getStatus(paymentRefCode), "PROCESSED");
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
