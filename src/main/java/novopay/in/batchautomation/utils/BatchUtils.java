package novopay.in.batchautomation.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.jcraft.jsch.Session;

public class BatchUtils extends ApiUtils {

	DBUtils dbUtils = new DBUtils();
	ServerUtils svrUtils = new ServerUtils();
	Session session;
	public HashMap<String, String> agentWalletRefBal;
	public String retailerWalletNo, retailerBalance;
	public String cmsWalletNo, cmsWalletBal;

	public void redeployWar() {
		session = svrUtils.connectServer(JavaUtils.configProperties);
		svrUtils.redeployWar(session);
	}

	public String executeBatch(HashMap<String, String> batchConfig) {
		session = svrUtils.connectServer(JavaUtils.configProperties);
		System.out.println("batch code : [" + batchConfig.get("batch.code") + "]  " + "  batch execution summary : ["
				+ batchConfig.get("batch.execution.summary") + "]");
		 return svrUtils.tailLogs(session, batchConfig.get("batch.execution.summary"), dbUtils.getOrganizationId(),batchConfig);
		
	}
		
	public void setRetailerRefBal(HashMap<String, String> workFlowDataMap) {
		agentWalletRefBal = getAgentsWalletBalance(workFlowDataMap.get("RETAILER"));
		retailerWalletNo = agentWalletRefBal.get((workFlowDataMap.get("WALLET_TYPE") + "_ref").toUpperCase());
		retailerBalance = agentWalletRefBal.get((workFlowDataMap.get("WALLET_TYPE") + "_wallet").toUpperCase());
	}

	public void uploadFile(HashMap<String, String> batchConfig, String status) {
		session = svrUtils.connectServer(batchConfig);
		System.out.println();
		svrUtils.uploadFile(session, configProperties.get("testdata.dir") + batchConfig.get("batch.file." + status.toLowerCase()),
				batchConfig.get("batch.destination.file.name"), batchConfig.get("batch.destination.tmp.dir"),
				batchConfig.get("batch.destination.config.dir"));
	}

	public String getWalletRefNo() {
		return retailerWalletNo;
	}

	public String getCashOutWalletNo() {
		return agentWalletRefBal.get("CASHOUT_REF");
	}

	public String getAgentWalletNo() {
		return agentWalletRefBal.get("AGENT_REF");
	}

	public String getMerchantWalletNo() {
		return agentWalletRefBal.get("MERCHANT_REF");
	}

	public void generateBatchFile(String sheetName, String fileName, String[][] data, int rowToUpdate) {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		Row headers = sheet.createRow(rowToUpdate);
		Row value = sheet.createRow(++rowToUpdate);
		for (int i = 0; i < data.length; i++) {
			Cell headersCell = headers.createCell(i);
			Cell valueCell = value.createCell(i);
			headersCell.setCellValue(data[i][0]);
			valueCell.setCellValue(data[i][1]);
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(fileName);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
