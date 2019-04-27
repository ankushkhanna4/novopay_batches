package novopay.in.batchautomation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DBUtils extends JavaUtils {
	protected Connection conn;
	protected Statement stmt;
	// private String codeId;
	private static String organizationId;

	public String getOrganizationId() {
		return DBUtils.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		DBUtils.organizationId = organizationId;
	}

	public Connection createConnection(String dbSchemaName) {

		String dbNpActor = configProperties.get("db.url") + dbSchemaName ;
		String jdbcDriver = configProperties.get("jdbc.driver");
		try {
			if ((null == conn) || (!conn.getCatalog().equalsIgnoreCase(dbSchemaName))) {
//				Class.forName(jdbcDriver);
				conn = DriverManager.getConnection(dbNpActor, configProperties.get("db.username"),
						configProperties.get("db.password"));
				stmt = conn.createStatement();
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {

		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection due to below error..!");
			e.printStackTrace();
		}
	}

	public String getOTPMessage(String remitterMobileNumber) {

		try {

			conn = createConnection(configProperties.get("smsLog"));
			stmt = conn.createStatement();
			String query = "SELECT message FROM sms_log WHERE " + "sent_to = '" + remitterMobileNumber
					+ "' AND message LIKE '%OTP%' ORDER BY delivered_date DESC LIMIT 1";

			// System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			rs.last();
			// System.out.println(rs.getString("message"));
			return rs.getString("message");
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! get OTP message failed..!");
			sqe.printStackTrace();
		}

		return null;
	}

	public HashMap<String, String> getAgentsWalletRefNo(String mobileNumber) {

		try {
			HashMap<String, String> walletRefNumber = new HashMap<String, String>();
			conn = createConnection(configProperties.get("master"));
			String query = "SELECT attr_key,attr_value,orgnization_id FROM master.organization_attribute "
					+ "WHERE attr_key IN ('WALLET_ACCOUNT_NUMBER','OTC_MERCHANT_WALLET_ACCOUNT_NUMBER','CASH_OUT_WALLET_ACCOUNT_NUMBER','CMS_WALLET_ACCOUNT_NUMBER') AND "
					+ "orgnization_id = (SELECT `organization` FROM `user_attribute` INNER JOIN `user` ON `user_attribute`.`user_id` = `user`.`id` "
					+ "WHERE `user_attribute`.`attr_value`='" + mobileNumber + "' AND `user`.`status`='ACTIVE');";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				setOrganizationId(rs.getString("orgnization_id"));
				if (rs.getString("attr_key").equals("OTC_MERCHANT_WALLET_ACCOUNT_NUMBER"))
					walletRefNumber.put("merchantWalletRefNo", rs.getString("attr_value"));
				else if (rs.getString("attr_key").equals("CASH_OUT_WALLET_ACCOUNT_NUMBER"))
					walletRefNumber.put("cashOutWalletRefNo", rs.getString("attr_value"));
				else if (rs.getString("attr_key").equals("WALLET_ACCOUNT_NUMBER"))
					walletRefNumber.put("agentWalletRefNo", rs.getString("attr_value"));
				else if (rs.getString("attr_key").equals("CMS_WALLET_ACCOUNT_NUMBER"))
					walletRefNumber.put("cmsWalletRefNo", rs.getString("attr_value"));
			}
			System.out.println(walletRefNumber);
			return walletRefNumber;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, String> getConfig(HashMap<String, String> batchConfig) {
		try {
			HashMap<String, String> configuration = new HashMap<String, String>();
			conn = createConnection(configProperties.get("config"));
			String sql;
			ResultSet rs;

			// String sql = "SELECT `prop_value` FROM `configuration` WHERE
			// `prop_key`='" + batchConfig.get("cron") + "'";
			// stmt = conn.createStatement();
			// ResultSet rs = stmt.executeQuery(sql);
			// rs.next();
			// configuration.put("cron", rs.getString("prop_value"));

			// sql = "SELECT `prop_value` FROM `configuration` WHERE
			// `prop_key`='" + batchConfig.get("mail.from") + "'";
			// System.out.println(sql);
			// rs = stmt.executeQuery(sql);
			// rs.next();
			// configuration.put("from", rs.getString("prop_value"));

			sql = "SELECT `prop_value` FROM `configuration` WHERE `prop_key`='" + batchConfig.get("mail.subject") + "'";
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			rs.next();
			configuration.put("subject", rs.getString("prop_value"));

			sql = "SELECT `prop_value` FROM `configuration` WHERE `prop_key`='" + batchConfig.get("mail.username")
					+ "'";
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			rs.next();
			configuration.put("username", rs.getString("prop_value"));

			// sql = "SELECT `prop_value` FROM `configuration` WHERE
			// `prop_key`='" + batchConfig.get("mail.password")
			// + "'";
			// System.out.println(sql);
			// rs = stmt.executeQuery(sql);
			// rs.next();
			// configuration.put("password", rs.getString("prop_value"));
			System.out.println(configuration);
			return configuration;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getVANNumber(String mobile) {
		try {
			String sql = "SELECT oa.attr_value AS van_number FROM `master`.`user` "
					+ "AS u JOIN `master`.`user_attribute` AS ua ON u.`id`=ua.`user_id`  "
					+ "JOIN `master`.organization o ON u.organization = o.id JOIN "
					+ "`master`.`organization_attribute` oa ON u.organization=oa.orgnization_id "
					+ "WHERE ua.`attr_value`='" + mobile
					+ "' AND oa.attr_key='VIRTUAL_ACC_NUM' ORDER BY oa.id DESC LIMIT 1";

			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString("van_number");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateBatchConfigurationToSuccess(String batchCode) {
		try {
			String sql = "UPDATE `batch_master` SET `last_run_status`='SUCCESS'"
					+ ", `last_run_end_time`= NOW() WHERE `batch_code`='" + batchCode + "'";
			conn = createConnection(configProperties.get("npops"));
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void stopBatchConfiguration(String batchCode) {
		conn = createConnection(configProperties.get("npops"));
		try {
			stmt = conn.createStatement();
			String query = "SELECT `last_run_status` FROM `batch_master` WHERE `batch_code` = '" + batchCode + "';";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			String runningStatus = rs.getString("last_run_status");
			if (runningStatus.toLowerCase().contains("fail")) {
				Assert.fail("FAILURE!!! Batch didn't executed successfully.");
			} else if (runningStatus.toLowerCase().contains("started")) {
				do {
					System.out.println("Batch isnt finished, Waiting for 2 sec untill batch is completed,");
					Thread.sleep(2000);
					query = "SELECT `last_run_status` FROM `batch_master` WHERE `batch_code` = '" + batchCode + "';";
					System.out.println(query);
					rs = stmt.executeQuery(query);
					rs.next();
					runningStatus = rs.getString("last_run_status");
				} while (runningStatus.toLowerCase().contains("started"));

				query = "UPDATE `batch_master` SET `last_run_status`='STARTED' WHERE `batch_code` = '" + batchCode
						+ "';";
				System.out.println(query);
				stmt.executeUpdate(query);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSettlementRefNumber() {
		try {
			String sql = "SELECT `settlement_ref_num` FROM `wallet_settlement_transaction` WHERE `entity_id`="
					+ getOrganizationId() + " ORDER BY `last_try_date` DESC LIMIT 1";
			System.out.println(sql);
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString("settlement_ref_num");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRrnNumber(String settlementRefNumber) {
		try {
			String sql = "SELECT `bank_ref_code` FROM `remittance_outward_table` WHERE `payment_ref_code`='';";
			System.out.println(sql);
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString("settlement_ref_num");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void updateRBLTransactionStatus(String bankRefCode) {
		try {
			String sql = "UPDATE `transaction` SET `paymentstatus`='3', `isrefundfile`='YES' WHERE `transactionid`='"
					+ bankRefCode + "';";

			System.out.println(sql);
			conn = createConnection(configProperties.get("rblSimulator"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getSettlementStatus(String settlementRefNumber) {
		try {
			String sql = "SELECT * FROM `wallet_settlement_transaction` WHERE `settlement_ref_num`="
					+ getSettlementRefNumber();
			System.out.println(sql);
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString("settlement_status");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateNextTryDate(String settlementRefNumber) {
		try {
			String sql = "UPDATE `wallet_settlement_transaction` SET `next_retry_date`=NOW() WHERE `settlement_ref_num`="
					+ settlementRefNumber;
			System.out.println(sql);
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void getTransactiontype(String retailerNumber) {
		try {
			String sql = "SELECT `tx_ref_no` FROM `wallet_load_tx` WHERE `tx_type`='FIRST_WALLET_LOAD' AND `cr_org_id` IN ("
					+ "SELECT organization FROM `user` WHERE id IN("
					+ "SELECT user_id FROM user_attribute WHERE attr_value='" + retailerNumber + "'))";
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sql = "DELETE FROM `wallet_load_tx` WHERE `tx_ref_no`=" + rs.getString("tx_ref_no");
				stmt = conn.createStatement();
				stmt.execute(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String[] returnDeviceIdAndSimNoFromMaster(String mobileNumber) {

		String deviceId = null, simNumber = null;
		try {
			conn = createConnection(configProperties.get("master"));
			String query1 = "select * from `organization_devices` where `msisdn` = '" + mobileNumber + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query1);

			while (rs.next()) {

				deviceId = rs.getString("device_id");
				simNumber = rs.getString("sim");
			}

			return new String[] { deviceId, simNumber };
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! returnSessionTokenFromAuthentication failed..!");
			sqe.printStackTrace();
		}

		return null;
	}

	public String returnUuidForMaster(String mobileNumber) {
		String uuid = null;
		try {
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();
			String query1 = "SELECT attr_value FROM master.user_attribute WHERE user_id = (SELECT ua.`user_id` FROM `master`.`user_attribute` AS ua "
					+ "WHERE ua.`attr_value`='" + mobileNumber + "' ORDER BY ua.id DESC LIMIT 1) AND attr_key='UUID'";
			System.out.println(query1);
			ResultSet rs = stmt.executeQuery(query1);
			while (rs.next()) {
				uuid = rs.getString("attr_value");
			}
			return uuid;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return uuid;
	}

	public String returnSessionTokenFromAuthentication(String uuid) {

		String sessionToken = null;
		try {
			String query1 = "select `session_token` from `session` where `user_id_value` ='" + uuid
					+ "' AND `client_id`='NOVOPAY' AND `channel_id`='AGENTAPP';";
			System.out.println(query1);
			conn = createConnection(configProperties.get("authentication"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query1);
			while (rs.next()) {
				sessionToken = rs.getString("session_token");
			}
			return sessionToken;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! returnSessionTokenFromAuthentication failed..!");
			sqe.printStackTrace();
		}
		return sessionToken;
	}

	public String[] returnAccountDetailsFromDB(String mobileNumber) {

		String accountType = null, accountValue = null;
		try {
			String query1 = "SELECT `type`,`pan` FROM `fin_inst_master` "
					+ "WHERE pan IN (SELECT fin_instrument_number FROM `customer_fin_instrument_mapping` "
					+ "WHERE `type` = 'BANK_ACCOUNT' AND customer_id IN (SELECT customer_id FROM customer_handle WHERE `status` ='ACTIVE' AND handle_value = '"
					+ mobileNumber + "')) ;";
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query1);

			while (rs.next()) {

				accountType = rs.getString("type");
				accountValue = rs.getString("pan");
			}
			return new String[] { accountType, accountValue };
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}


	public String[] getPaymentStatusAndRRNNo(String paymentRefCode) {
		try {
			String query = "SELECT `status`,`bank_ref_code` FROM `remittance_outward_table` WHERE `payment_ref_code`="
					+ paymentRefCode;
			System.out.println(query);
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return new String[] { rs.getString("status"), rs.getString("bank_ref_code") };
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void updateNovopayRBLTransactionStatus(String paymentRefCode) {
		try {
			String query = "UPDATE `remittance_outward_table` SET `status`='SENT_TO_BANK' WHERE `payment_ref_code`='"
					+ paymentRefCode+"';";
			System.out.println(query);
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(query);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	
	public void updateThreshold(String bank,String errorcode,String threshold){
		try{
			String query = "UPDATE `queuing_config` SET `threshold_error_count`='"+threshold+"' WHERE partner='"+bank+"' AND error_code='"+errorcode+"';";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(query);
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		
	}
	
	public String getManufacturerId(String data){
		try{
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			String query1 = "SELECT `code` FROM platform_master_data WHERE `value` ='"+data+"'";
			System.out.println(query1);
			ResultSet rs = stmt.executeQuery(query1);
			rs.next();
			return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}




	public String getModelId(String  value){
		try{
			String query = "SELECT `code` FROM platform_master_data WHERE `data_type`='Model' AND `value` ='"+value+"';";
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String getVariantId(String  value){
		try{
			String query = "SELECT `code` FROM platform_master_data WHERE `data_type`='Variant' AND `value` ='"+value+"';";
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}

	
	public String getCityCode(String pincode) {
		try{
		String query="SELECT `code` FROM platform_master_data WHERE data_sub_type = '"+pincode+"';";
		conn = createConnection(configProperties.get("npActor"));
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}
	
	public String getDistrictCode(String pincode){
		try{
			String query = "SELECT `code` FROM platform_master_data WHERE `data_sub_type` = (SELECT `code` FROM platform_master_data WHERE data_sub_type = '"+pincode+"')";
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
	}
	
	public String getStateCode(String pincode){
		try{
			String query="SELECT `code` FROM platform_master_data WHERE `data_sub_type` = (SELECT `code` FROM platform_master_data WHERE `data_sub_type` = (SELECT `code` FROM platform_master_data WHERE data_sub_type = '"+pincode+"'));";
			conn = createConnection(configProperties.get("npActor"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString("code");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
		return null;
		}
	
	
	public void insertIntoNpRemittanceConfig(String bank,String errorcode,String threshold){
		try{
			String query = "INSERT INTO `queuing_config` (`partner`, `error_type`, `error_code`, `threshold_error_count`, `enable_for_specific_partner`, `created_by`, `created_on`, `updated_by`, `updated_on`)"
					+" VALUES('"+bank+"','NPCI_ERROR','"+errorcode+"','"+threshold+"','1','Riya',NOW(),'Riya',NOW());";
			System.out.println(query);
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(query);
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
		}
	}
	
	public String getStatus(String paymentrefcode){
		try{
			String query = "SELECT `status` FROM `queued_remittance` WHERE payment_ref_code ='"+paymentrefcode+"';";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString("status");
		}catch(SQLException sqe){
			System.out.println("Error connecting DB!! returnAccountDetails failed..!");
			sqe.printStackTrace();
			
		}
		return null;
	}
	
}

