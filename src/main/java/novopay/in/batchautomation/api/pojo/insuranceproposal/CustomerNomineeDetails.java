package novopay.in.batchautomation.api.pojo.insuranceproposal;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"name",
"age",
"relationship"
})
public class CustomerNomineeDetails {

@JsonProperty("name")
private String name;
@JsonProperty("age")
private String age;
@JsonProperty("relationship")
private String relationship;

@JsonProperty("name")
public String getName() {
return name;
}

@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

@JsonProperty("age")
public String getAge() {
return age;
}

@JsonProperty("age")
public void setAge(String age) {
this.age = age;
}

@JsonProperty("relationship")
public String getRelationship() {
return relationship;
}

@JsonProperty("relationship")
public void setRelationship(String relationship) {
this.relationship = relationship;
}

}
