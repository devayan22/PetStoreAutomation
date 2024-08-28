package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.endpoints.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests2 {
     
	Faker faker;
	User userPayload;
	public Logger logger; 
	
	@BeforeClass
	public void setupData() 
	{
		faker=new Faker();
		userPayload=new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setUsername(faker.name().username());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		userPayload.setPassword(faker.internet().password(5, 10));
		
		//Logs
		logger=LogManager.getLogger(this.getClass());
		
		
		
	}
	
	@Test(priority=1)
	public void testPostUser()
	{
	  logger.info("Creating User");
	  Response response = UserEndpoints2.createUser(userPayload);
	  response.then().log().all();
	  
	  Assert.assertEquals(response.getStatusCode(), 200);
	  logger.info("User is created");
	}
	
	@Test(priority=2)
	public void testGetUserByName() 
	{
	
	logger.info("Reading User Input");
	Response response=UserEndpoints2.readUser(this.userPayload.getUsername());
	response.then().log().all();
	  
	Assert.assertEquals(response.getStatusCode(), 200);
	logger.info("User Info is displayed");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() 
	{
		// update data using payload 
		logger.info("Updating User");
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setLastName(faker.name().lastName());
		
		Response response = UserEndpoints2.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().all();
		response.then().log().body().statusCode(200);          //Chai Assertion 
		Assert.assertEquals(response.getStatusCode(), 200);    //TestNG Assertion
		logger.info("User is Updated");
		//Checking Data after Update
	
		Response responseAfterUpdate =UserEndpoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		  
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
	
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() 
	{
		// delete data using payload 
		logger.info("Deleting User");
		Response response = UserEndpoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("User Deleted");
	}
	
	
	
}
