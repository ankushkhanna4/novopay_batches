package novopay.in.batchautomation.api.pojo.wallettransferapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "assistorDetails", "clientRefNumber", "metaData", "operationMode", "partnerId", "payeeDetails",
		"payerDetails", "transactionAmount", "transferMode", "transferType" })
public class Request {

	@JsonProperty("assistorDetails")
	private AssistorDetails assistorDetails;
	@JsonProperty("clientRefNumber")
	private String clientRefNumber;
	@JsonProperty("metaData")
	private List<MetaData> metaData = null;
	@JsonProperty("operationMode")
	private String operationMode;
	@JsonProperty("partnerId")
	private String partnerId;
	@JsonProperty("payeeDetails")
	private PayeeDetails payeeDetails;
	@JsonProperty("payerDetails")
	private PayerDetails payerDetails;
	@JsonProperty("transactionAmount")
	private String transactionAmount;
	@JsonProperty("transferMode")
	private String transferMode;
	@JsonProperty("transferType")
	private String transferType;

	@JsonProperty("assistorDetails")
	public AssistorDetails getAssistorDetails() {
		return assistorDetails;
	}

	@JsonProperty("assistorDetails")
	public void setAssistorDetails(AssistorDetails assistorDetails) {
		this.assistorDetails = assistorDetails;
	}

	@JsonProperty("clientRefNumber")
	public String getClientRefNumber() {
		return clientRefNumber;
	}

	@JsonProperty("clientRefNumber")
	public void setClientRefNumber(String clientRefNumber) {
		this.clientRefNumber = clientRefNumber;
	}

	@JsonProperty("metaData")
	public List<MetaData> getMetaData() {
		return metaData;
	}

	@JsonProperty("metaData")
	public void setMetaData(List<MetaData> metaData) {
		this.metaData = metaData;
	}

	@JsonProperty("operationMode")
	public String getOperationMode() {
		return operationMode;
	}

	@JsonProperty("operationMode")
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	@JsonProperty("partnerId")
	public String getPartnerId() {
		return partnerId;
	}

	@JsonProperty("partnerId")
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@JsonProperty("payeeDetails")
	public PayeeDetails getPayeeDetails() {
		return payeeDetails;
	}

	@JsonProperty("payeeDetails")
	public void setPayeeDetails(PayeeDetails payeeDetails) {
		this.payeeDetails = payeeDetails;
	}

	@JsonProperty("payerDetails")
	public PayerDetails getPayerDetails() {
		return payerDetails;
	}

	@JsonProperty("payerDetails")
	public void setPayerDetails(PayerDetails payerDetails) {
		this.payerDetails = payerDetails;
	}

	@JsonProperty("transactionAmount")
	public String getTransactionAmount() {
		return transactionAmount;
	}

	@JsonProperty("transactionAmount")
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@JsonProperty("transferMode")
	public String getTransferMode() {
		return transferMode;
	}

	@JsonProperty("transferMode")
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	@JsonProperty("transferType")
	public String getTransferType() {
		return transferType;
	}

	@JsonProperty("transferType")
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}