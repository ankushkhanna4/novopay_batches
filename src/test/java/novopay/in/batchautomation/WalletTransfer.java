package novopay.in.batchautomation;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import novopay.in.batchautomation.api.MoneyTransferBatch;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.EmailUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class WalletTransfer extends BatchUtils {
	
	
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "WalletTransfer", fileName, vanNumber, batchSheetName, batchConfigSection="wallettransfer",
			agentWalletRefNo, agentWalletRemainingBal;
	HashMap<String, String> databaseConfig,batchFileConfig;
	private HashMap<String, String> usrData;
	private String sourceWalletRefNo;
	private String destWalletRefNo;
	private String targetBeforeWalletRemainingBal;
	private String targetAfterWalletRemainingBal;


	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider="getTestData")
	public void axisAgentWalletTopupTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		databaseConfig = dbUtils.getConfig(batchFileConfig);
		vanNumber = dbUtils.getVANNumber(usrData.get("Retailer"));
		fileName = batchFileConfig.get("batch.file.name");
		batchSheetName = batchFileConfig.get("batch.sheet.name");
		sourceWalletRefNo = dbUtils.getAgentsWalletRefNo(usrData.get("SourceMobileNumber")).get(usrData.get("SourceWalletType").toLowerCase()+"WalletRefNo");
		destWalletRefNo = dbUtils.getAgentsWalletRefNo(usrData.get("TargetMobileNumber")).get(usrData.get("TargetWalletType").toLowerCase()+"WalletRefNo");
		targetBeforeWalletRemainingBal = apiUtils.getWalletBalance(destWalletRefNo);
		generateFile();
		apiUtils.depositAmountToWallet(sourceWalletRefNo, usrData.get("Amount"));
//		redeployWar();
		EmailUtils emailUtils = new EmailUtils();
		emailUtils.emailTopUpFile(databaseConfig, fileName);
		batchFileConfig.put("batch.execution.summary", emailUtils.newFileName+ " written successfully..");
		executeBatch(batchFileConfig);
		targetAfterWalletRemainingBal = apiUtils.getWalletBalance(destWalletRefNo);
	    float remainingbal = Float.parseFloat(targetAfterWalletRemainingBal) - Float.parseFloat(targetBeforeWalletRemainingBal);
		//assertion for balance 
		Assert.assertEquals(String.valueOf(Math.round(remainingbal)),usrData.get("Amount"));
		
	}

	public void generateFile() {
		String[][] data = new String[][]{
		{"Source Wallet Number",sourceWalletRefNo},
		{"Target Wallet Number",destWalletRefNo},
		{"Payment Type","TRANSFER"},
		{"Amount",usrData.get("Amount")},
		{"Remarks","a"},
		};		
		
		

		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data,0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}

}
