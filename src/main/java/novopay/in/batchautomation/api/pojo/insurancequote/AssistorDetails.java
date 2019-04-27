package novopay.in.batchautomation.api.pojo.insurancequote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "actorType", "handleType", "handleValue" })
public class AssistorDetails {

	@JsonProperty("actorType")
	private String actorType;
	@JsonProperty("handleType")
	private String handleType;
	@JsonProperty("handleValue")
	private String handleValue;

	@JsonProperty("actorType")
	public String getActorType() {
		return actorType;
	}

	@JsonProperty("actorType")
	public void setActorType(String actorType) {
		this.actorType = actorType;
	}

	@JsonProperty("handleType")
	public String getHandleType() {
		return handleType;
	}

	@JsonProperty("handleType")
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	@JsonProperty("handleValue")
	public String getHandleValue() {
		return handleValue;
	}

	@JsonProperty("handleValue")
	public void setHandleValue(String handleValue) {
		this.handleValue = handleValue;
	}

}
