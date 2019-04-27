package novopay.in.batchautomation.api.pojo.insurancequote;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "manufacturer", "model", "variant", "yearOfManufacture", "registrationDate", "registrationNumber1",
		"registrationNumber2", "registrationNumber3", "registrationNumber4", "cityUsed" })
public class VehicleDetails {

	@JsonProperty("manufacturer")
	private String manufacturer;
	@JsonProperty("model")
	private String model;
	@JsonProperty("variant")
	private String variant;
	@JsonProperty("yearOfManufacture")
	private String yearOfManufacture;
	@JsonProperty("registrationDate")
	private String registrationDate;
	@JsonProperty("registrationNumber1")
	private String registrationNumber1;
	@JsonProperty("registrationNumber2")
	private String registrationNumber2;
	@JsonProperty("registrationNumber3")
	private String registrationNumber3;
	@JsonProperty("registrationNumber4")
	private String registrationNumber4;
	@JsonProperty("cityUsed")
	private String cityUsed;

	@JsonProperty("manufacturer")
	public String getManufacturer() {
		return manufacturer;
	}

	@JsonProperty("manufacturer")
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@JsonProperty("model")
	public String getModel() {
		return model;
	}

	@JsonProperty("model")
	public void setModel(String model) {
		this.model = model;
	}

	@JsonProperty("variant")
	public String getVariant() {
		return variant;
	}

	@JsonProperty("variant")
	public void setVariant(String variant) {
		this.variant = variant;
	}

	@JsonProperty("yearOfManufacture")
	public String getYearOfManufacture() {
		return yearOfManufacture;
	}

	@JsonProperty("yearOfManufacture")
	public void setYearOfManufacture(String yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	@JsonProperty("registrationDate")
	public String getRegistrationDate() {
		return registrationDate;
	}

	@JsonProperty("registrationDate")
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	@JsonProperty("registrationNumber1")
	public String getRegistrationNumber1() {
		return registrationNumber1;
	}

	@JsonProperty("registrationNumber1")
	public void setRegistrationNumber1(String registrationNumber1) {
		this.registrationNumber1 = registrationNumber1;
	}

	@JsonProperty("registrationNumber2")
	public String getRegistrationNumber2() {
		return registrationNumber2;
	}

	@JsonProperty("registrationNumber2")
	public void setRegistrationNumber2(String registrationNumber2) {
		this.registrationNumber2 = registrationNumber2;
	}

	@JsonProperty("registrationNumber3")
	public String getRegistrationNumber3() {
		return registrationNumber3;
	}

	@JsonProperty("registrationNumber3")
	public void setRegistrationNumber3(String registrationNumber3) {
		this.registrationNumber3 = registrationNumber3;
	}

	@JsonProperty("registrationNumber4")
	public String getRegistrationNumber4() {
		return registrationNumber4;
	}

	@JsonProperty("registrationNumber4")
	public void setRegistrationNumber4(String registrationNumber4) {
		this.registrationNumber4 = registrationNumber4;
	}

	@JsonProperty("cityUsed")
	public String getCityUsed() {
		return cityUsed;
	}

	@JsonProperty("cityUsed")
	public void setCityUsed(String cityUsed) {
		this.cityUsed = cityUsed;
	}

}
