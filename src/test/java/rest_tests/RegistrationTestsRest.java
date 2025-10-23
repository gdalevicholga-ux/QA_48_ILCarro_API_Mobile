package rest_tests;

import api_rest.AuthenticationController;
import dto.ErrorMessageDtoString;
import dto.RegistrationBodyDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

public class RegistrationTestsRest extends AuthenticationController {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void registrationPositiveTest(){
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("sima_solomon"+i+"@gmail.com")
                .password("Solomon124!")
                .firstName("Sima")
                .lastName("Solomon")
                .build();
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL)
                .getStatusCode(), 200);
    }

    @Test
    public void registrationNegativeTest_WrongEmail(){
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("sima_solomon"+i+"gmail.com")
                .password("Solomon124!")
                .firstName("Sima")
                .lastName("Solomon")
                .build();
        Response response = registrationLogin(user, REGISTRATION_URL);
        softAssert.assertEquals(response.getStatusCode(), 400,
                "validate status code");
        ErrorMessageDtoString errorMessageDtoString = response.getBody()
                .as(ErrorMessageDtoString.class);
        softAssert.assertEquals(errorMessageDtoString.getError(),
                "Bad Request", "validate error");
        System.out.println(errorMessageDtoString);
        softAssert.assertTrue(errorMessageDtoString.getMessage().toString()
                .contains("must be a well-formed"), "validate message");
        softAssert.assertAll();
    }
}
