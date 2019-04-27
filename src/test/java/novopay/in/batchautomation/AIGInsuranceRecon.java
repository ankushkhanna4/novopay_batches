package novopay.in.batchautomation;

import java.util.HashMap;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import novopay.in.batchautomation.api.InsuranceQuote;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class AIGInsuranceRecon extends BatchUtils{

	
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "AIGInsuranceReconBatch", batchConfigSection="queuedtransactionprocessing";
	HashMap<String, String> databaseConfig,batchFileConfig;
	private HashMap<String, String> usrData;


	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider="getTestData")
	public void axisAgentWalletTopupTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		
		InsuranceQuote insuranceQuote = new InsuranceQuote();
		insuranceQuote.insuranceQuoteTest(usrData);
		
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
