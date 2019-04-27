package novopay.in.batchautomation.api.pojo.wallettransferapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "dataLabel", "dataValue" })
public class MetaData {

	@JsonProperty("dataLabel")
	private String dataLabel;
	@JsonProperty("dataValue")
	private String dataValue;

	@JsonProperty("dataLabel")
	public String getDataLabel() {
		return dataLabel;
	}

	@JsonProperty("dataLabel")
	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}

	@JsonProperty("dataValue")
	public String getDataValue() {
		return dataValue;
	}

	@JsonProperty("dataValue")
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

}