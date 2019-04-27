package novopay.in.batchautomation.api.pojo.insurancequote;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "effectiveTo", "ncbApplicable", "ncbPercentage" })
public class PreviousPolicyDetails {

	@JsonProperty("effectiveTo")
	private String effectiveTo;
	@JsonProperty("ncbApplicable")
	private Boolean ncbApplicable;
	@JsonProperty("ncbPercentage")
	private String ncbPercentage;

	@JsonProperty("effectiveTo")
	public String getEffectiveTo() {
		return effectiveTo;
	}

	@JsonProperty("effectiveTo")
	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	@JsonProperty("ncbApplicable")
	public Boolean getNcbApplicable() {
		return ncbApplicable;
	}

	@JsonProperty("ncbApplicable")
	public void setNcbApplicable(Boolean ncbApplicable) {
		this.ncbApplicable = ncbApplicable;
	}

	@JsonProperty("ncbPercentage")
	public String getNcbPercentage() {
		return ncbPercentage;
	}

	@JsonProperty("ncbPercentage")
	public void setNcbPercentage(String ncbPercentage) {
		this.ncbPercentage = ncbPercentage;
	}

}