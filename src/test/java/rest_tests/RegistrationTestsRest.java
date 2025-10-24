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
                .username("sima_solomon"+i+"@gmail.com")//Sima_solomon"+i+"@gmail.com
                .password("Solomon124!")
                .firstName("Sima")
                .lastName("Solomon")
                .build();
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL)
                .getStatusCode(), 200);
    }

    @Test
    public void registrationNegativeTest_WrongEmail() {
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("sima_solomon" + i + "gmail.com")//min = q@q.q; @gmail.com; sima@; sima@.com; sima@gmail;
                // sima@gmail.; sima@@gmail.com; sima@gmail..com; [space]sima@gmail.com;sima@gmail.com[space];
                //sima[space]@gmail.com; sima@яя.com;
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
        @Test
        public void registrationNegativeTest_WrongPassword() {
            int i = new Random().nextInt(1000);
            RegistrationBodyDto user = RegistrationBodyDto.builder()
                    .username("sima_solomon" + i + "gmail.com")
                    .password("olomon124!")//DEAS124$; Deastyu*; Deastyu7; Deast1!; Deast[spase]1!; DEas123.;
                    //ААяя123!
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
                    .contains("Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number"), "validate message");
            softAssert.assertAll();
        }
            @Test
            public void registrationNegativeTest_EmptyField() {
                int i = new Random().nextInt(1000);
                RegistrationBodyDto user = RegistrationBodyDto.builder()
                        .username("sima_solomon" + i + "@gmail.com")
                        .password("Solomon124!")
                        .firstName("Sima")
                        .lastName("")
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
                        .contains("must not be blank"), "validate message");
                softAssert.assertAll();
            }
    }

