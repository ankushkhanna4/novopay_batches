package novopay.in.batchautomation.api.pojo.wallettransferapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "accountType", "accountValue", "actorType", "handleType", "handleValue" })
public class PayeeDetails {

	@JsonProperty("accountType")
	private String accountType;
	@JsonProperty("accountValue")
	private String accountValue;
	@JsonProperty("actorType")
	private String actorType;
	@JsonProperty("handleType")
	private String handleType;
	@JsonProperty("handleValue")
	private String handleValue;

	@JsonProperty("accountType")
	public String getAccountType() {
		return accountType;
	}

	@JsonProperty("accountType")
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@JsonProperty("accountValue")
	public String getAccountValue() {
		return accountValue;
	}

	@JsonProperty("accountValue")
	public void setAccountValue(String accountValue) {
		this.accountValue = accountValue;
	}

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