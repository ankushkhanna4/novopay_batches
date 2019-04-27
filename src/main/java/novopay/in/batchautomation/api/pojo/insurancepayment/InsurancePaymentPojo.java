package novopay.in.batchautomation.api.pojo.insurancepayment;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"request"
})
public class InsurancePaymentPojo {

@JsonProperty("request")
private Request request;

@JsonProperty("request")
public Request getRequest() {
return request;
}

@JsonProperty("request")
public void setRequest(Request request) {
this.request = request;
}

}
