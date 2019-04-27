package novopay.in.batchautomation.api.pojo.insurancepayment;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"partnerId",
"operationMode",
"paymentMode",
"clientRefNumber",
"transactionValueDate",
"transactionAmount",
"remarks",
"assistorDetails",
"actorDetails"
})
public class Request {

@JsonProperty("partnerId")
private String partnerId;
@JsonProperty("operationMode")
private String operationMode;
@JsonProperty("paymentMode")
private String paymentMode;
@JsonProperty("clientRefNumber")
private String clientRefNumber;
@JsonProperty("transactionValueDate")
private String transactionValueDate;
@JsonProperty("transactionAmount")
private String transactionAmount;
@JsonProperty("remarks")
private String remarks;
@JsonProperty("assistorDetails")
private AssistorDetails assistorDetails;
@JsonProperty("actorDetails")
private ActorDetails actorDetails;

@JsonProperty("partnerId")
public String getPartnerId() {
return partnerId;
}

@JsonProperty("partnerId")
public void setPartnerId(String partnerId) {
this.partnerId = partnerId;
}

@JsonProperty("operationMode")
public String getOperationMode() {
return operationMode;
}

@JsonProperty("operationMode")
public void setOperationMode(String operationMode) {
this.operationMode = operationMode;
}

@JsonProperty("paymentMode")
public String getPaymentMode() {
return paymentMode;
}

@JsonProperty("paymentMode")
public void setPaymentMode(String paymentMode) {
this.paymentMode = paymentMode;
}

@JsonProperty("clientRefNumber")
public String getClientRefNumber() {
return clientRefNumber;
}

@JsonProperty("clientRefNumber")
public void setClientRefNumber(String clientRefNumber) {
this.clientRefNumber = clientRefNumber;
}

@JsonProperty("transactionValueDate")
public String getTransactionValueDate() {
return transactionValueDate;
}

@JsonProperty("transactionValueDate")
public void setTransactionValueDate(String transactionValueDate) {
this.transactionValueDate = transactionValueDate;
}

@JsonProperty("transactionAmount")
public String getTransactionAmount() {
return transactionAmount;
}

@JsonProperty("transactionAmount")
public void setTransactionAmount(String transactionAmount) {
this.transactionAmount = transactionAmount;
}

@JsonProperty("remarks")
public String getRemarks() {
return remarks;
}

@JsonProperty("remarks")
public void setRemarks(String remarks) {
this.remarks = remarks;
}

@JsonProperty("assistorDetails")
public AssistorDetails getAssistorDetails() {
return assistorDetails;
}

@JsonProperty("assistorDetails")
public void setAssistorDetails(AssistorDetails assistorDetails) {
this.assistorDetails = assistorDetails;
}

@JsonProperty("actorDetails")
public ActorDetails getActorDetails() {
return actorDetails;
}

@JsonProperty("actorDetails")
public void setActorDetails(ActorDetails actorDetails) {
this.actorDetails = actorDetails;
}

}
