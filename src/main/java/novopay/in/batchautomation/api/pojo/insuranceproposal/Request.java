package novopay.in.batchautomation.api.pojo.insuranceproposal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"operationMode",
"insuranceType",
"partnerId",
"quoteNumber",
"vehicleDetails",
"previousPolicyDetails",
"customerDetails",
"customerNomineeDetails",
"assistorDetails"
})
public class Request {

@JsonProperty("operationMode")
private String operationMode;
@JsonProperty("insuranceType")
private String insuranceType;
@JsonProperty("partnerId")
private String partnerId;
@JsonProperty("quoteNumber")
private String quoteNumber;
@JsonProperty("vehicleDetails")
private VehicleDetails vehicleDetails;
@JsonProperty("previousPolicyDetails")
private PreviousPolicyDetails previousPolicyDetails;
@JsonProperty("customerDetails")
private CustomerDetails customerDetails;
@JsonProperty("customerNomineeDetails")
private CustomerNomineeDetails customerNomineeDetails;
@JsonProperty("assistorDetails")
private AssistorDetails assistorDetails;

@JsonProperty("operationMode")
public String getOperationMode() {
return operationMode;
}

@JsonProperty("operationMode")
public void setOperationMode(String operationMode) {
this.operationMode = operationMode;
}

@JsonProperty("insuranceType")
public String getInsuranceType() {
return insuranceType;
}

@JsonProperty("insuranceType")
public void setInsuranceType(String insuranceType) {
this.insuranceType = insuranceType;
}

@JsonProperty("partnerId")
public String getPartnerId() {
return partnerId;
}

@JsonProperty("partnerId")
public void setPartnerId(String partnerId) {
this.partnerId = partnerId;
}

@JsonProperty("quoteNumber")
public String getQuoteNumber() {
return quoteNumber;
}

@JsonProperty("quoteNumber")
public void setQuoteNumber(String quoteNumber) {
this.quoteNumber = quoteNumber;
}

@JsonProperty("vehicleDetails")
public VehicleDetails getVehicleDetails() {
return vehicleDetails;
}

@JsonProperty("vehicleDetails")
public void setVehicleDetails(VehicleDetails vehicleDetails) {
this.vehicleDetails = vehicleDetails;
}

@JsonProperty("previousPolicyDetails")
public PreviousPolicyDetails getPreviousPolicyDetails() {
return previousPolicyDetails;
}

@JsonProperty("previousPolicyDetails")
public void setPreviousPolicyDetails(PreviousPolicyDetails previousPolicyDetails) {
this.previousPolicyDetails = previousPolicyDetails;
}

@JsonProperty("customerDetails")
public CustomerDetails getCustomerDetails() {
return customerDetails;
}

@JsonProperty("customerDetails")
public void setCustomerDetails(CustomerDetails customerDetails) {
this.customerDetails = customerDetails;
}

@JsonProperty("customerNomineeDetails")
public CustomerNomineeDetails getCustomerNomineeDetails() {
return customerNomineeDetails;
}

@JsonProperty("customerNomineeDetails")
public void setCustomerNomineeDetails(CustomerNomineeDetails customerNomineeDetails) {
this.customerNomineeDetails = customerNomineeDetails;
}

@JsonProperty("assistorDetails")
public AssistorDetails getAssistorDetails() {
return assistorDetails;
}

@JsonProperty("assistorDetails")
public void setAssistorDetails(AssistorDetails assistorDetails) {
this.assistorDetails = assistorDetails;
}

}
