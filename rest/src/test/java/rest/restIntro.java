package rest;
import static io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;




public class restIntro {
	String baseurl = "http://ergast.com/api";
	
	
	@Test (enabled = false)
	public void test_NumberOfCircuitsFor2017Season_ShouldBe20() {
	        
	    given().
	    	accept(ContentType.JSON).
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
		    prettyPrint();
	}
	
	@Test
	public void testJSON() {
	   
		given().
    	accept(ContentType.JSON).
    when().
        get("http://ergast.com/api/f1/2017/circuits.json").prettyPeek();
		
		
	   Response response= 
	given().
	    	accept(ContentType.JSON).
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json");//.prettyPrint();
	    	   
	   JsonPath json = response.jsonPath();
	   System.out.println("Print out");
	   
	   System.out.println(response.statusCode());
	   
	   
	  // System.out.println(json.);
	   
	   System.out.println(json.getString("MRData.CircuitTable.Circuits.circuitId"));
	   List<String> circuits =  json.getList("MRData.CircuitTable.Circuits.circuitId");
	
//	   for (String circuit : circuits) {
//		   System.out.println(circuit);
//	   }
	   
	   Map<String,Object> jsonmap = response.as(HashMap.class);
	   System.out.println("map");
	   System.out.println(jsonmap.get("MRData").toString());
	   
	}
	
	@Test (enabled = false)
	public void test_ResponseHeaderData_ShouldBeCorrect() {
	        
	    given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        statusCode(200).
	    and().
	        contentType(ContentType.JSON).
	    and().
	        header("Content-Length",equalTo("4551"));
	}
	
	
	@Test (enabled = false)
	public void test_NumberOfCircuits_ShouldBe20_Parameterized() {
	        
	    String season = "2017";
	    int numberOfRaces = 20;
	        
	    given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(numberOfRaces));
	}
	
	@Test (enabled = false)
	public void test_ScenarioRetrieveFirstCircuitFor2017SeasonAndGetCountry_ShouldBeAustralia() {
	        
	    // First, retrieve the circuit ID for the first circuit of the 2017 season
	    String circuitId = 
	    given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        extract().
	        path("MRData.CircuitTable.Circuits.circuitId[0]");
	        
	    // Then, retrieve the information known for that circuit and verify it is located in Australia
	    given().
	        pathParam("circuitId",circuitId).
	    when().
	        get("http://ergast.com/api/f1/circuits/{circuitId}.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.Location[0].country",equalTo("Australia"));
	}
	
	
	ResponseSpecification checkStatusCodeAndContentType = 
		    new ResponseSpecBuilder().
		        expectStatusCode(200).
		        expectContentType(ContentType.JSON).
		        build();

		@Test (enabled = false)
		public void test_NumberOfCircuits_ShouldBe20_UsingResponseSpec() {
		        
		    given().
		    when().
		        get("http://ergast.com/api/f1/2017/circuits.json").
		    then().
		        assertThat().
		        spec(checkStatusCodeAndContentType).
		    and().
		        body("MRData.CircuitTable.Circuits.circuitId",hasSize(20));
		}
}
