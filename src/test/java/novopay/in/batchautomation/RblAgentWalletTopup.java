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

public class RblAgentWalletTopup extends BatchUtils {
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "RblAgentWalletTopup", fileName, vanNumber, batchSheetName, batchConfigSection="rblagentwallettopuup",
			agentWalletRefNo, agentWalletRemainingBal;
	HashMap<String, String> databaseConfig,batchFileConfig;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider="getTestData")
	public void rblAgentWalletTopupTest(HashMap<String, String> usrData) {
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
		executeBatch(batchFileConfig);
	}

	public void generateFile() {
		String[][] data = new String[][]{
			{"Txn Ref no(UTR No)", "CBINH"+System.currentTimeMillis()},
		//	{"Txn Ref no(UTR No)", "RBLBatchUtr125"},
			{"Product Code", "NEFT"},
			{"Inst No", "N_29258363"},
			{"Inst Amt", "3,005.00"},
			{"Virtual Account No(Received)", vanNumber},
			{"Txn Date", "10/01/2017"},
			{"Bank Name", "RBL BANK LTD"},
			{"Customer Account(Master Account)", "405405405405"},
			{"Channel", "Backoffice"},
			{"PARAMETER1", "00*"},
			{"PARAMETER2", "9467389244*"},
		};
		

		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data,0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}

}
