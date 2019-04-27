package novopay.in.batchautomation.api.pojo.insurancequote;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "operationMode", "partnerId", "insuranceType", "effectiveDate", "term", "applicableNCBPercentage",
		"vehicleDetails", "previousPolicyDetails", "assistorDetails" })
public class Request {

	@JsonProperty("operationMode")
	private String operationMode;
	@JsonProperty("partnerId")
	private String partnerId;
	@JsonProperty("insuranceType")
	private String insuranceType;
	@JsonProperty("effectiveDate")
	private String effectiveDate;
	@JsonProperty("term")
	private String term;
	@JsonProperty("applicableNCBPercentage")
	private String applicableNCBPercentage;
	@JsonProperty("vehicleDetails")
	private VehicleDetails vehicleDetails;
	@JsonProperty("previousPolicyDetails")
	private PreviousPolicyDetails previousPolicyDetails;
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

	@JsonProperty("partnerId")
	public String getPartnerId() {
		return partnerId;
	}

	@JsonProperty("partnerId")
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@JsonProperty("insuranceType")
	public String getInsuranceType() {
		return insuranceType;
	}

	@JsonProperty("insuranceType")
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	@JsonProperty("effectiveDate")
	public String getEffectiveDate() {
		return effectiveDate;
	}

	@JsonProperty("effectiveDate")
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@JsonProperty("term")
	public String getTerm() {
		return term;
	}

	@JsonProperty("term")
	public void setTerm(String term) {
		this.term = term;
	}

	@JsonProperty("applicableNCBPercentage")
	public String getApplicableNCBPercentage() {
		return applicableNCBPercentage;
	}

	@JsonProperty("applicableNCBPercentage")
	public void setApplicableNCBPercentage(String applicableNCBPercentage) {
		this.applicableNCBPercentage = applicableNCBPercentage;
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

	@JsonProperty("assistorDetails")
	public AssistorDetails getAssistorDetails() {
		return assistorDetails;
	}

	@JsonProperty("assistorDetails")
	public void setAssistorDetails(AssistorDetails assistorDetails) {
		this.assistorDetails = assistorDetails;
	}

}
