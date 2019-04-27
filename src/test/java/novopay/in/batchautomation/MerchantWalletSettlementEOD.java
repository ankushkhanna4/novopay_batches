package novopay.in.batchautomation;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.EmailUtils;
import novopay.in.batchautomation.utils.JavaUtils;

public class MerchantWalletSettlementEOD extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "MerchantWalletSettlementEOD", fileName, vanNumber,
			batchSheetName, batchConfigSection = "merchantwalletsettlementeod",
			 agentWalletRefNo, agentWalletRemainingBal;
	String prevSettlementMode, currSettlementMode, prevRetailer, currRetailer;
	String responseCallbackConfigSection = "rblresponsecallbackinvocation";
	HashMap<String, String> databaseConfig, batchFileConfig,responseBatchConfig;
	private HashMap<String, String> usrData;
	private String settlementRefNumber;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider = "getTestData")
	public void merchantWalletSettlementEODTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		// databaseConfig = dbUtils.getConfig(batchFileConfig);

		agentWalletRefNo = dbUtils.getAgentsWalletRefNo(usrData.get("Retailer")).get("merchantWalletRefNo");
		agentWalletRemainingBal = apiUtils.getWalletBalance(agentWalletRefNo);
		apiUtils.depositAmountToWallet(agentWalletRefNo, usrData.get("Amount"));
		// batch start merchant settlement
		dbUtils.updateBatchConfigurationToSuccess(batchFileConfig.get("batch.code"));
		executeBatch(batchFileConfig);
		dbUtils.stopBatchConfiguration(batchFileConfig.get("batch.code"));
		// start the batch
		
		
		// batch response callback
		responseBatchConfig = javaUtils.readSectionFromIni("responsecallbackinvocation");
		fileName = responseBatchConfig.get("batch.file.name");
		batchSheetName = responseBatchConfig.get("batch.sheet.name");
		dbUtils.updateBatchConfigurationToSuccess(responseBatchConfig.get("batch.code"));
		settlementRefNumber = dbUtils.getSettlementRefNumber();
		generateFile();
		databaseConfig = dbUtils.getConfig(responseBatchConfig);
		databaseConfig.put("subject", "CMS DAILY REPORT");
		new EmailUtils().emailTopUpFile(databaseConfig, fileName);
		if (usrData.get("Settlement Status").equalsIgnoreCase("PAID")) {
			responseBatchConfig.put("batch.execution.summary", " is updated as '" + usrData.get("Settlement Status").toUpperCase()
					+ "' for stlmntRefNo:" + dbUtils.getSettlementRefNumber());
		} else if (usrData.get("Settlement Status").equalsIgnoreCase("RETURNED")) {
			responseBatchConfig.put("batch.execution.summary",
					dbUtils.getSettlementRefNumber() + " updated as [FAILED]");
		}

		executeBatch(responseBatchConfig);
		dbUtils.stopBatchConfiguration(responseBatchConfig.get("batch.code"));
		String settlementStatus = dbUtils.getSettlementStatus(settlementRefNumber);
		if (((usrData.get("Settlement Status").equalsIgnoreCase("PAID")))
				&& (!settlementStatus.equalsIgnoreCase("PAID"))) {
			Assert.fail("Settlement status not paid");

		} else if (usrData.get("Settlement Status").equalsIgnoreCase("RETURNED")) {
			if (!settlementStatus.equalsIgnoreCase("PENDING_FOR_NEFT")) {
				Assert.fail("Settlement status not PENDING_FOR_NEFT");
			}
			
			else {
				dbUtils.updateNextTryDate(settlementRefNumber);
				// start pending for neft batch
				// execute the batch
				HashMap<String, String> mertStlmntPendingNeftConfig = javaUtils
						.readSectionFromIni("mertstlmntpendingneft");
				dbUtils.updateBatchConfigurationToSuccess(mertStlmntPendingNeftConfig.get("batch.code"));
				executeBatch(mertStlmntPendingNeftConfig);
			}
		}
		
		// pending for neft
		
		

	}

	public void generateFile() {
		String[][] data = new String[][]{
			{"Business Partner", "2080003"},
			{"Product", "NEFT_INDIVIDUAL"},
			{"Issuing Branch", "PUNB0721100"},
			{"UTR No.", "RATNN"+System.currentTimeMillis()},
			{"Debit Account No.", "405405405405"},
			{"Value Date", "08/09/2017"},
			{"Instrument Ref", "070920171802007098"},
			{"Amount", usrData.get("Amount")},
			{"Charge Amount", "0.0"},
			{"Currency", "INR"},
			{"Beneficiary Name", "ramesh ram"},
			{"Credit Bank", "PUNJAB NATIONAL BANK"},
			{"Credit Bank Identification Code", "PUNB0721100"},
			{"Credit Account", "7211000100050928"},
			{"Credit Bank Branch | Name", "PUNB0721100 | CHAKIA"},
			{"Batch Ref", "070920171802007172"},
			{"Customer Ref", dbUtils.getSettlementRefNumber()},
			{"Payment Input Date", "07/09/2017"},
			{"Liquidation Status", "PAID"},
			{"Current Status", "PAID"},
			{"Liquidation Date", "08/09/2017"},
			{"Channel Source", "HOST TO HOST"},
			{"Service Provider", ""},
			{"Error Code", ""},
			{"Error Description", ""},
			{"Enrichment1", "Punjab National Bank"},
			{"Enrichment2", ""},
			{"Enrichment3", ""},
			{"Enrichment4", ""},
			{"Enrichment5", ""},
			{"Authorization Type", "STP"},
			{"Debit Time", "9/8/17 12:41 PM"},
			{"Statutory Payment Type", ""},
			{"Tax Payment Location", ""},
			{"Source", "File Upload"},
			{"Rejection Remarks", ""},
			{"Treasury Authorized", "NA"},
			{"PO Ref Number", "000021451341"},
		};
		
		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data,0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
