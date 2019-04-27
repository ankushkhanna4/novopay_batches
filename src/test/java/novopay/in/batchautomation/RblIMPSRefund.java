package novopay.in.batchautomation;

import java.util.HashMap;
import java.util.List;

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

public class RblIMPSRefund extends BatchUtils {

	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "RblIMPSRefund", fileName, vanNumber,
			batchConfigSection = "rblimpsrefund", agentWalletRefNo, agentWalletRemainingBal;
	HashMap<String, String> databaseConfig, batchFileConfig;
	private HashMap<String, String> usrData;
	MoneyTransferBatch moneyTransferBatch;
	private String[] paymentCode;

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

		moneyTransferBatch = new MoneyTransferBatch();
		moneyTransferBatch.moneyTransferTest(usrData);

		JsonPath path = new JsonPath(moneyTransferBatch.moneyTransferResponse);
		Assert.assertTrue(path.get("responseStatus.status").toString().equalsIgnoreCase("SUCCESS"),
				"FAIL ! Transaction failed.");
		String paymentRefCode = path.get("txnResponses[0].response.novopayRefNumber").toString();
		List<HashMap<String, String>> response = new JsonPath(moneyTransferBatch.moneyTransferResponse)
				.get("txnResponses[0].response.printData");
		HashMap<String, String> responseData = new HashMap<String, String>();
		response.stream().forEach(s -> {
			if ((s.get("printLabel").equalsIgnoreCase("Bene A/c"))
					|| (s.get("printLabel").equalsIgnoreCase("Bene IFSC"))
					|| (s.get("printLabel").equalsIgnoreCase("Bene Name"))
					|| (s.get("printLabel").equalsIgnoreCase("Date"))) {
				responseData.put(s.get("printLabel"), s.get("printValue"));
			}
		});
		paymentCode = dbUtils.getPaymentStatusAndRRNNo(paymentRefCode);
		databaseConfig = dbUtils.getConfig(batchFileConfig);
		fileName = batchFileConfig.get("batch.file.name");
		generateFile(responseData);
		if (usrData.get("TransactionType").equalsIgnoreCase("Late Refund")) {
			dbUtils.updateRBLTransactionStatus(paymentCode[1]);
		} else {
			dbUtils.updateNovopayRBLTransactionStatus(paymentRefCode);
		}
		String mailedFileName = new EmailUtils().emailTopUpFile(databaseConfig, fileName);
		batchFileConfig.put("batch.execution.summary", mailedFileName + " written successfully..");
		executeBatch(batchFileConfig);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paymentCode = dbUtils.getPaymentStatusAndRRNNo(paymentRefCode);
		Assert.assertEquals(paymentCode[0],usrData.get("FinalStatus").toUpperCase(),
				"FAIL! Transaction status not updated to fail.");

	}

	public void generateFile(HashMap<String, String> responseData) {
		String[][] data = new String[][] { { "RRN", paymentCode[1] },
				{ "Beneficiary Name", responseData.get("Bene Name") },
				{ "Beneficiary IFSC code", responseData.get("Bene IFSC") },
				{ "Beneficiary Account No", responseData.get("Bene A/c") }, { "AMOUNT/100", usrData.get("Amount") },
				{ "Date", responseData.get("Date") } };

		generateBatchFile(batchFileConfig.get("batch.sheet.name"),
				JavaUtils.configProperties.get("batchfiledir") + fileName, data, 0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}
}
