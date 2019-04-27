package novopay.in.batchautomation.api.pojo.getencryption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "partnerId", "keyType" })
public class Request {

	@JsonProperty("partnerId")
	private String partnerId;
	@JsonProperty("keyType")
	private String keyType;

	@JsonProperty("partnerId")
	public String getPartnerId() {
		return partnerId;
	}

	@JsonProperty("partnerId")
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@JsonProperty("keyType")
	public String getKeyType() {
		return keyType;
	}

	@JsonProperty("keyType")
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

}