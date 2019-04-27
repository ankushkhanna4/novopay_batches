package novopay.in.batchautomation.api.pojo.wallettransferapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "accountType", "accountValue", "actorType", "authType", "authValue", "handleType", "handleValue" })
public class AssistorDetails {

	@JsonProperty("accountType")
	private String accountType;
	@JsonProperty("accountValue")
	private String accountValue;
	@JsonProperty("actorType")
	private String actorType;
	@JsonProperty("authType")
	private String authType;
	@JsonProperty("authValue")
	private String authValue;
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

	@JsonProperty("authType")
	public String getAuthType() {
		return authType;
	}

	@JsonProperty("authType")
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	@JsonProperty("authValue")
	public String getAuthValue() {
		return authValue;
	}

	@JsonProperty("authValue")
	public void setAuthValue(String authValue) {
		this.authValue = authValue;
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