package novopay.in.batchautomation.api.pojo.insuranceproposal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"insurerName",
"policyNumber"
})
public class PreviousPolicyDetails {

@JsonProperty("insurerName")
private String insurerName;
@JsonProperty("policyNumber")
private String policyNumber;

@JsonProperty("insurerName")
public String getInsurerName() {
return insurerName;
}

@JsonProperty("insurerName")
public void setInsurerName(String insurerName) {
this.insurerName = insurerName;
}

@JsonProperty("policyNumber")
public String getPolicyNumber() {
return policyNumber;
}

@JsonProperty("policyNumber")
public void setPolicyNumber(String policyNumber) {
this.policyNumber = policyNumber;
}

}

