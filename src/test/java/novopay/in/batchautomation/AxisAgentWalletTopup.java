package novopay.in.batchautomation;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.EmailUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class AxisAgentWalletTopup extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "AXISAgentWalletTopup", fileName, vanNumber, batchSheetName, batchConfigSection="axisagentwallettopup",
			agentWalletRefNo, agentWalletRemainingBal;
	HashMap<String, String> databaseConfig,batchFileConfig;
	private HashMap<String, String> usrData;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider="getTestData")
	public void axisAgentWalletTopupTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		databaseConfig = dbUtils.getConfig(batchFileConfig);
		if(usrData.get("FirstWalletLoadCheck").equalsIgnoreCase("Yes")){
			dbUtils.getTransactiontype(usrData.get("Retailer"));
		}
		vanNumber = dbUtils.getVANNumber(usrData.get("Retailer"));
		fileName = batchFileConfig.get("batch.file.name");
		batchSheetName = batchFileConfig.get("batch.sheet.name");
		generateFile();

		agentWalletRefNo = dbUtils.getAgentsWalletRefNo(usrData.get("Retailer")).get("agentWalletRefNo");
		
		// start/stop the batch
		agentWalletRemainingBal = apiUtils.getWalletBalance(agentWalletRefNo);
		
//		apiUtils.withdrawAmountFromWallet(agentWalletRefNo, agentWalletRemainingBal);
		
//		apiUtils.depositAmountToWallet(agentWalletRefNo, usrData.get("Amount"));
		// mail file
		
		// trail the logs
//		redeployWar();
		new EmailUtils().emailTopUpFile(databaseConfig, fileName);
		batchFileConfig.put("batch.execution.summary", "Wallet Topup Success for: "+ vanNumber);
		executeBatch(batchFileConfig);
	}

	public void generateFile() {
		String[][] data = new String[][]{
		{"Message Type","NEFT"},
		{"UTR Number","CONOB150"+System.currentTimeMillis()},
		{"Sender IFSC","IBKL0001022"},
		{"Sender Acc Type","Saving Account"},
		{"Sender Account Number","1022104000013688"},
		{"Sender Name","RAJEEV KUMAR GUPTA"},
		{"Sender Address 1","RAJEEV KUMAR GUPTA"},
		{"Beneficiary IFSC","UTIB0CCH274"},
		{"Beneficiary Account Number",vanNumber},
		{"Beneficiary Account Name","NOVOPAY SOLUTIONS PVT LTD"},
		{"Sender Information","NOVOPAY"},
		{"Amount",usrData.get("Amount")},
		{"Value Date - NEFT/RTGS","1900-01-00"},
		{"Transaction Date","1900-01-00"},
		{"Beneficiary Account Type","Current Account"},
		{"Related Reference",""},
		{"Beneficiary Address 1","BANGLOUR"},
		{"Corporate Code","1111"},
		{"Client Code - Master",""},
		{"Credit Acc Number - NEFT/RTGS","915020011744608"},
		{"Batch Time","1200"},
		{"Name - Master", ""}
		};		
		
		

		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data,0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
