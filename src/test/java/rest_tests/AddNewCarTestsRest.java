package rest_tests;

import api_rest.CarController;
import dto.CarDto;
import dto.ErrorMessageDtoString;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

public class AddNewCarTestsRest extends CarController {
    SoftAssert softAssert = new SoftAssert();
    @Test
    public void addNewCarPositiveTest(){
        int i = new Random().nextInt(1000)+1000;
        CarDto car = CarDto.builder()
                .serialNumber("999-"+i)
                .manufacture("Volvo")
                .model("V200")
                .year("2020")
                .fuel("electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(38.0)
                .city("Haifa")
                .build();
        Response response = addNewCar(car);
        softAssert.assertEquals(response.getStatusCode(),200,"validate status code");
        System.out.println(response.getBody().print());
        softAssert.assertTrue(response.getBody().print().contains("added successfully"),"validate massage");
        softAssert.assertAll();
    }
    @Test
    public void addNewCarNegativeTest_WrongAuthorization(){
        int i = new Random().nextInt(1000)+1000;
        CarDto car = CarDto.builder()
                .serialNumber("999-"+i)
                .manufacture("Volvo")
                .model("V200")
                .year("2020") //-1 2026 1885 1884 0 (to chto nyzhno proverit)
                .fuel("electric") // 555555
                .seats(4) //0  21  not int
                .carClass("A")
                .pricePerDay(38.0)
                .city("Haifa")
                .build();
        Response response = addNewCarNegative_WrongToken(car,"ertvg45");
        softAssert.assertEquals(response.getStatusCode(),401,"validate status code");
        System.out.println(response.getBody().print());
        softAssert.assertTrue(response.getBody().print().contains("strings must contain exactly"),"validate massage");
        softAssert.assertAll();
    }
    @Test
    public void addNewCarNegativeTest_WOAuthorization(){
        int i = new Random().nextInt(1000)+1000;
        CarDto car = CarDto.builder()
                .serialNumber("999-"+i)
                .manufacture("Volvo")
                .model("V200")
                .year("2020") //-1  2026 1885 1884 0
                .fuel("Electric") // 555555555555
                .seats(4) // 0  21 not int
                .carClass("A")
                .pricePerDay(38.0)
                .city("Haifa")
                .build();
        Response response = addNewCarNegative_WOToken(car);
        softAssert.assertEquals(response.getStatusCode(),
                403, "validate status code" );
        softAssert.assertAll();
    }

    @Test
    public void addNewCarNegativeTest_WOSerialNumber(){
        CarDto car = CarDto.builder()
                .manufacture("Volvo")
                .model("V200")
                .year("2020") //-1  2026 1885 1884 0
                .fuel("Electric") // 555555555555
                .seats(4) // 0  21 not int
                .carClass("A")
                .pricePerDay(38.0)
                .city("Haifa")
                .build();
        Response response = addNewCar(car);
        softAssert.assertEquals(response.getStatusCode(),
                400, "validate status code" );
        System.out.println(response.getBody().print());
        softAssert.assertTrue(response.getBody().print()
                        .contains("must not be blank"),
                "validate message");
        ErrorMessageDtoString errorMessageDtoString = response.getBody()
                .as(ErrorMessageDtoString.class);
        softAssert.assertTrue(errorMessageDtoString.getError()
                .equals("Bad Request"), "validate error");
        softAssert.assertAll();
    }

}
