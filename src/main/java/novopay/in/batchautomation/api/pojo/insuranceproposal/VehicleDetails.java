package novopay.in.batchautomation.api.pojo.insuranceproposal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"engineNumber",
"chassisNumber",
"registeredAddressLine1",
"registeredAddressLine2",
"registeredAddressLine3",
"photo"
})
public class VehicleDetails {

@JsonProperty("engineNumber")
private String engineNumber;
@JsonProperty("chassisNumber")
private String chassisNumber;
@JsonProperty("registeredAddressLine1")
private String registeredAddressLine1;
@JsonProperty("registeredAddressLine2")
private String registeredAddressLine2;
@JsonProperty("registeredAddressLine3")
private String registeredAddressLine3;
@JsonProperty("photo")
private String photo;

@JsonProperty("engineNumber")
public String getEngineNumber() {
return engineNumber;
}

@JsonProperty("engineNumber")
public void setEngineNumber(String engineNumber) {
this.engineNumber = engineNumber;
}

@JsonProperty("chassisNumber")
public String getChassisNumber() {
return chassisNumber;
}

@JsonProperty("chassisNumber")
public void setChassisNumber(String chassisNumber) {
this.chassisNumber = chassisNumber;
}

@JsonProperty("registeredAddressLine1")
public String getRegisteredAddressLine1() {
return registeredAddressLine1;
}

@JsonProperty("registeredAddressLine1")
public void setRegisteredAddressLine1(String registeredAddressLine1) {
this.registeredAddressLine1 = registeredAddressLine1;
}

@JsonProperty("registeredAddressLine2")
public String getRegisteredAddressLine2() {
return registeredAddressLine2;
}

@JsonProperty("registeredAddressLine2")
public void setRegisteredAddressLine2(String registeredAddressLine2) {
this.registeredAddressLine2 = registeredAddressLine2;
}

@JsonProperty("registeredAddressLine3")
public String getRegisteredAddressLine3() {
return registeredAddressLine3;
}

@JsonProperty("registeredAddressLine3")
public void setRegisteredAddressLine3(String registeredAddressLine3) {
this.registeredAddressLine3 = registeredAddressLine3;
}

@JsonProperty("photo")
public String getPhoto() {
return photo;
}

@JsonProperty("photo")
public void setPhoto(String photo) {
this.photo = photo;
}

}
