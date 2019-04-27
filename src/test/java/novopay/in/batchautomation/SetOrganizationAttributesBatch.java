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

public class SetOrganizationAttributesBatch extends BatchUtils {
	
	
	JavaUtils javaUtils = new JavaUtils();
	DBUtils dbUtils = new DBUtils();
	ApiUtils apiUtils = new ApiUtils();
	String workbook = "workbook", testDataSheetName = "SetOrganizationAttributesBatch", fileName, vanNumber, batchSheetName, batchConfigSection="setorganizationattributes";
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
		fileName = batchFileConfig.get("batch.file.name");
		batchSheetName = batchFileConfig.get("batch.sheet.name");
		generateFile();
//		redeployWar();
		EmailUtils emailUtils = new EmailUtils();
		emailUtils.emailTopUpFile(databaseConfig, fileName);
		batchFileConfig.put("batch.execution.summary", emailUtils.newFileName+ " processing completed");

		executeBatch(batchFileConfig);
		
		
	}

	public void generateFile() {
		String[][] data = new String[][]{
		{"handle_type",usrData.get("handle_type")},
		{"handle_value",usrData.get("handle_value")},
		{"attribute_key",usrData.get("attribute_key")},
		{"attribute_value",usrData.get("attribute_value")},
		};		
		
		

		generateBatchFile(batchSheetName, JavaUtils.configProperties.get("batchfiledir") + fileName, data,0);
	}

	@DataProvider
	public Object[][] getTestData() {
		Object[][] allValues = javaUtils.returnAllUniqueValuesInMap(workbook, testDataSheetName);
		return allValues;

	}

}
