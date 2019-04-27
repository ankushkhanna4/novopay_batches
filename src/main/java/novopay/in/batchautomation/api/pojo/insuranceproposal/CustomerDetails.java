package novopay.in.batchautomation.api.pojo.insuranceproposal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"salutation",
"firstName",
"middleName",
"lastName",
"mobileNumber",
"addressLine1",
"addressLine2",
"addressLine3",
"city",
"district",
"state",
"pincode",
"landmark"
})
public class CustomerDetails {

@JsonProperty("salutation")
private String salutation;
@JsonProperty("firstName")
private String firstName;
@JsonProperty("middleName")
private String middleName;
@JsonProperty("lastName")
private String lastName;
@JsonProperty("mobileNumber")
private String mobileNumber;
@JsonProperty("addressLine1")
private String addressLine1;
@JsonProperty("addressLine2")
private String addressLine2;
@JsonProperty("addressLine3")
private String addressLine3;
@JsonProperty("city")
private String city;
@JsonProperty("district")
private String district;
@JsonProperty("state")
private String state;
@JsonProperty("pincode")
private String pincode;
@JsonProperty("landmark")
private String landmark;

@JsonProperty("salutation")
public String getSalutation() {
return salutation;
}

@JsonProperty("salutation")
public void setSalutation(String salutation) {
this.salutation = salutation;
}

@JsonProperty("firstName")
public String getFirstName() {
return firstName;
}

@JsonProperty("firstName")
public void setFirstName(String firstName) {
this.firstName = firstName;
}

@JsonProperty("middleName")
public String getMiddleName() {
return middleName;
}

@JsonProperty("middleName")
public void setMiddleName(String middleName) {
this.middleName = middleName;
}

@JsonProperty("lastName")
public String getLastName() {
return lastName;
}

@JsonProperty("lastName")
public void setLastName(String lastName) {
this.lastName = lastName;
}

@JsonProperty("mobileNumber")
public String getMobileNumber() {
return mobileNumber;
}

@JsonProperty("mobileNumber")
public void setMobileNumber(String mobileNumber) {
this.mobileNumber = mobileNumber;
}

@JsonProperty("addressLine1")
public String getAddressLine1() {
return addressLine1;
}

@JsonProperty("addressLine1")
public void setAddressLine1(String addressLine1) {
this.addressLine1 = addressLine1;
}

@JsonProperty("addressLine2")
public String getAddressLine2() {
return addressLine2;
}

@JsonProperty("addressLine2")
public void setAddressLine2(String addressLine2) {
this.addressLine2 = addressLine2;
}

@JsonProperty("addressLine3")
public String getAddressLine3() {
return addressLine3;
}

@JsonProperty("addressLine3")
public void setAddressLine3(String addressLine3) {
this.addressLine3 = addressLine3;
}

@JsonProperty("city")
public String getCity() {
return city;
}

@JsonProperty("city")
public void setCity(String city) {
this.city = city;
}

@JsonProperty("district")
public String getDistrict() {
return district;
}

@JsonProperty("district")
public void setDistrict(String district) {
this.district = district;
}

@JsonProperty("state")
public String getState() {
return state;
}

@JsonProperty("state")
public void setState(String state) {
this.state = state;
}

@JsonProperty("pincode")
public String getPincode() {
return pincode;
}

@JsonProperty("pincode")
public void setPincode(String pincode) {
this.pincode = pincode;
}

@JsonProperty("landmark")
public String getLandmark() {
return landmark;
}

@JsonProperty("landmark")
public void setLandmark(String landmark) {
this.landmark = landmark;
}

}
