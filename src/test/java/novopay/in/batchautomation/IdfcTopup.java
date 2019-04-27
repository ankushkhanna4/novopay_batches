package novopay.in.batchautomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.internal.MultiValueEntity;

import novopay.in.batchautomation.utils.ApiUtils;
import novopay.in.batchautomation.utils.BatchUtils;
import novopay.in.batchautomation.utils.DBUtils;
import novopay.in.batchautomation.utils.EmailUtils;
import novopay.in.batchautomation.utils.JavaUtils;
import novopay.in.batchautomation.utils.ZipUtils;

public class IdfcTopup extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "IDFCTopup", fileName,
			vanNumber, batchSheetName,
			batchConfigSection = "idfcagentwallettopup", agentWalletRefNo,
			agentWalletRemainingBal;
	HashMap<String, String> databaseConfig, batchFileConfig;
	private HashMap<String, String> usrData;

	@BeforeSuite
	public void readProperties() {
		javaUtils.readConfigProperties();
	}

	@Test(dataProvider = "getTestData")
	public void idfcAgentWalletTopupTest(HashMap<String, String> usrData) {
		this.usrData = usrData;
		batchFileConfig = javaUtils.readSectionFromIni(batchConfigSection);
		databaseConfig = dbUtils.getConfig(batchFileConfig);
		if (usrData.get("FirstWalletLoadCheck").equalsIgnoreCase("Yes")) {
			dbUtils.getTransactiontype(usrData.get("Retailer"));
		}
		vanNumber = "1110" + usrData.get("Retailer");
		fileName = batchFileConfig.get("batch.file.name");
		batchSheetName = batchFileConfig.get("batch.sheet.name");
		generateFile();

		agentWalletRefNo = dbUtils
				.getAgentsWalletRefNo(usrData.get("Retailer")).get(
						"agentWalletRefNo");

		// start/stop the batch

		agentWalletRemainingBal = apiUtils.getWalletBalance(agentWalletRefNo);

		// apiUtils.withdrawAmountFromWallet(agentWalletRefNo,
		// agentWalletRemainingBal);

		// apiUtils.depositAmountToWallet(agentWalletRefNo,
		// usrData.get("Amount"));
		// mail file

		// trail the logs
		// redeployWar();

		String zipfileName = new ZipUtils().zipfiles(javaUtils.configProperties
				.get("batchfiledir") + fileName);

		String filename = new EmailUtils().emailTopUpFile(databaseConfig,
				zipfileName);
		batchFileConfig.put("batch.execution.summary",
				"File Name: " + filename.substring(0, filename.length() - 4)
						+ ".xls, STATUS: SUCCESS");
		executeBatch(batchFileConfig);
	}

	public void generateFile() {
		String[][] data = new String[][] {

				{ "Customer Code", "NSPL" },
				{ "Customer Name", "NOVOPAY SOLUTIONS PRIVATE LIMITED" },
				{ "Product  Code", "IIMPS" },
				{ "Product Description", "INWARD IMPS" },
				{ "Pooling A/C Sl", "1" },
				{ "Pooling Account Number", "10000147653" },
				{ "Transaction Type", "Collections" },
				{ "Data Key", "751   10204     22-04-2016L00001" },
				{ "Deposit Slip Number/Batch Reference Number", "0" },
				{ "Amount(Slip/Batch)", "5.00" },
				{ "Currency Code", "INR" },
				{ "Batch Amount/Slip Amount(Credit AC Curr)", "0" },
				{ "Currency Code", "" },
				{ "Batch Amount/Slip Amount(Base Curr)", "5.00" },
				{ "Currency Code", "INR" },
				{ "Clearing Date", "22-04-2016" },
				{ "Deposit Date", "22-04-2016" },
				{ "Credit Date", "" },
				{ "Division Code", "" },
				{ "Hierarchy Code", "" },
				{ "Number of Instruments/Instructions", "1" },
				{ "Collection Location", "MUMBAI" },
				{ "Drawn on/Debit Location Name", "MUMBAI" },
				{ "Instrument Number/Mandate Reference No.", "0" },
				{ "Instrument Date/Instruction Date", "22-04-2016" },
				{ "Amount(Instrument/Instruction)", "5.00" },
				{ "Currency Code", "INR" },
				{ "Collection Amount (Credit AC currency)", "5.00" },
				{ "Currency Code", "INR" },
				{ "Credit/Debit Amount(Base Currency)", usrData.get("Amount") },
				{ "Currency Code", "INR" },
				{ "Recovery Amount (Credit AC Curr)", "0.00" },
				{ "Currency Code", "" },
				{ "Recovery Amount(Base Curr)", "0.00" },
				{ "Currency Code", "" }, { "Returned Amount", "0.00" },
				{ "Currency Code", "" }, { "Return Date", "" },
				{ "Adjustment Amount", "0" }, { "Adjustment Date", "0.00" },
				{ "Remarks1", "" }, { "Remarks2", "" }, { "Remarks3 ", "" },
				{ "Mode of Pooling", "Credit to Account" },
				{ "Deposit Branch Code", "10204" },
				{ "Deposit Branch Name", "CMS Hub" },
				{ "Pickup Point Code", "" },
				{ "Pick Up Point Description", "" },
				{ "VA Number", vanNumber },
				{ "UTR No", "ICI" + System.currentTimeMillis() },

				{ "Drawer Name", "POONAMPRAKASHVALEC" },
				{ "Drawn On Bank", "IDFC BANK LTD" },
				{ "Drawn On Branch", "Clearing Hub Mumbai" },
				{ "Subcustomer Code", "" }, { "Additional Information1", "" },
				{ "Additional Information2", "" },
				{ "Additional Information3", "" },
				{ "Additional Information4", "" },
				{ "Additional Information5", "" }, };
		generateBatchFile(batchSheetName,
				JavaUtils.configProperties.get("batchfiledir") + fileName,
				data, 5);

		/*
		 * {"Customer Code","NSPL","NSPL"},
		 * {"Customer Name","NOVOPAY SOLUTIONS PRIVATE LIMITED"
		 * ,"NOVOPAY SOLUTIONS PRIVATE LIMITED"},
		 * {"Product  Code","IIMPS","IIMPS"},
		 * {"Product Description","INWARD IMPS","INWARD IMPS"},
		 * {"Pooling A/C Sl","1","1"},
		 * {"Pooling Account Number","10000147653","10000147653"},
		 * {"Transaction Type","Collections","Collections"},
		 * {"Data Key","751   10204     22-04-2016L00001"
		 * ,"751   10204     22-04-2016L00001"},
		 * {"Deposit Slip Number/Batch Reference Number","0","0"},
		 * {"Amount(Slip/Batch)","5.00","5.00"}, {"Currency Code","INR","INR"},
		 * {"Batch Amount/Slip Amount(Credit AC Curr)","0","0"},
		 * {"Currency Code","",""},
		 * {"Batch Amount/Slip Amount(Base Curr)","5.00","5.00"},
		 * {"Currency Code","INR","INR"},
		 * {"Clearing Date","22-04-2016","22-04-2016"},
		 * {"Deposit Date","22-04-2016","22-04-2016"}, {"Credit Date","",""},
		 * {"Division Code","",""}, {"Hierarchy Code","",""},
		 * {"Number of Instruments/Instructions","1","1"},
		 * {"Collection Location", "MUMBAI", "MUMBAI"},
		 * {"Drawn on/Debit Location Name", "MUMBAI", "MUMBAI"},
		 * {"Instrument Number/Mandate Reference No.", "0", "0"},
		 * {"Instrument Date/Instruction Date", "22-04-2016", "22-04-2016"},
		 * {"Amount(Instrument/Instruction)", "5.00", "5.00"}, {"Currency Code",
		 * "INR", "INR"}, {"Collection Amount (Credit AC currency)", "5.00",
		 * "5.00"}, {"Currency Code", "INR", "INR"},
		 * {"Credit/Debit Amount(Base Currency)", usrData.get("Amount"),
		 * usrData.get("Amount")}, {"Currency Code", "INR", "INR"},
		 * {"Recovery Amount (Credit AC Curr)", "0.00", "0.00"},
		 * {"Currency Code", "", ""}, {"Recovery Amount(Base Curr)", "0.00",
		 * "0.00"}, {"Currency Code", "", ""}, {"Returned Amount", "0.00",
		 * "0.00"}, {"Currency Code", "", ""}, {"Return Date", "", ""},
		 * {"Adjustment Amount", "0", "0"}, {"Adjustment Date", "0.00", "0.00"},
		 * {"Remarks1", "", ""}, {"Remarks2", "", ""}, {"Remarks3 ", "", ""},
		 * {"Mode of Pooling", "Credit to Account", "Credit to Account"},
		 * {"Deposit Branch Code", "10204", "10204"}, {"Deposit Branch Name",
		 * "CMS Hub", "CMS Hub"}, {"Pickup Point Code", "", ""},
		 * {"Pick Up Point Description", "", ""},
		 * {"VA Number",vanNumber,vanNumber}, {"UTR No",
		 * "ICI"+System.currentTimeMillis(), "ICI"+System.currentTimeMillis()},
		 * // {"UTR No", "IDFCBATCHUTR1306","IDFCBATCHUTR1306"}, {"Drawer Name",
		 * "POONAMPRAKASHVALEC", "POONAMPRAKASHVALEC"}, {"Drawn On Bank",
		 * "IDFC BANK LTD", "IDFC BANK LTD"}, {"Drawn On Branch",
		 * "Clearing Hub Mumbai", "Clearing Hub Mumbai"}, {"Subcustomer Code",
		 * "", ""}, {"Additional Information1", "", ""},
		 * {"Additional Information2", "", ""}, {"Additional Information3", "",
		 * ""}, {"Additional Information4", "", ""}, {"Additional Information5",
		 * "", ""},
		 */
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook,
				testDataSheetName);
		return allValues;

	}

}
