package shield;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the class CateringCompanyClientImp.
 */
public class CateringCompanyClientImpTest {
  /** Name of the file containing properties related to the client. */
  private static final String CLIENT_PROPS_FILENAME = "client.cfg";
  /** Properties related to the client. */
  private static final Properties CLIENT_PROPS = loadProperties(CLIENT_PROPS_FILENAME);
  /** The value that indicates a non-existent order number. */
  private static final int NONEXISTENT_ORDER_NUMBER = -1;
  /** The catering company client. */
  private CateringCompanyClientImp cateringCompanyClient;

  @BeforeEach
  public void setup() {
    cateringCompanyClient = new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));
  }

  private static Properties loadProperties(String propsFilename) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Properties props = new Properties();
    try {
      InputStream propsStream = loader.getResourceAsStream(propsFilename);
      props.load(propsStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return props;
  }

  // --------------- CATERING COMPANY REGISTRATION ---------------

  /**
   * Tests an invalid catering company registration.
   * A new registration using a combination of valid and invalid post code and company names.
   *
   * @param name an example of a valid catering company name
   * @param postCode an example of a valid catering company post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @ParameterizedTest
  @CsvSource({"testCompany,EH17_1UB",
              "testCompany2,EH17_2GR",
              "testCompany3,EH11_9IW",
              "testCompany4,EH1_9NS",
              "testCompany5,EH15_7NX"})
  public void testCateringCompanyInvalidRegistration(String name, String postCode) {
    // Test invalid registration
    assertFalse(cateringCompanyClient.registerCateringCompany(null, null),
            "Registration using invalid invalid name and post code should fail.");
    assertFalse(cateringCompanyClient.registerCateringCompany(null, postCode),
            "Registration using invalid invalid name should fail.");
    assertFalse(cateringCompanyClient.registerCateringCompany(name, null),
            "Registration using invalid invalid post code should fail.");
  }

  /**
   * Tests a valid catering company registration.
   * A new registration using a set of valid names and post codes.
   *
   * @param name an example of a valid catering company name
   * @param postCode an example of a valid catering company post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @ParameterizedTest
  @CsvSource({"testCompany123,EH4_7AR",
              "testCompany299,EH8_7YF",
              "testCompany343,EH4_1CU",
              "testCompany465,EH12_8AR",
              "testCompany53,EH16_8BO"})
  public void testCateringCompanyNewRegistration(String name, String postCode) {
    // Test valid registration
    assertTrue(cateringCompanyClient.registerCateringCompany(name, postCode),
        "Registration using valid name and post code should not fail.");
  }

  /**
   * Tests repeated catering company registration.
   * Repeated registration should succeed, whatever company name or post code is
   * used to attempt re-registration.
   *
   * @param name an example of a valid name
   * @param postCode an example of a valid post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @ParameterizedTest
  @CsvSource({"testCompany12,EH5_1BY",
              "testCompany42,EH4_7QX",
              "testCompany33,EH7_9HQ",
              "testCompany94,EH2_9LS",
              "testCompany75,EH5_9UG"})
  public void testCateringCompanyRepeatedRegistration(String name, String postCode) {
    // Test repeated registration
    assertTrue(cateringCompanyClient.registerCateringCompany(name, postCode),
        "Repeated registration should return true.");
    assertTrue(cateringCompanyClient.registerCateringCompany(null, null),
        "Repeated registration should return true even if invalid post code " +
            "and name is passed.");
    assertTrue(cateringCompanyClient.registerCateringCompany(null, postCode),
        "Repeated registration should return true even if an invalid name is passed.");
    assertTrue(cateringCompanyClient.registerCateringCompany(name, null),
        "Repeated registration should return true even if an invalid post code is passed.");
  }

  /**
   * Tests catering company registration with an invalid endpoint.
   * Registration with invalid endpoint but valid name and post code should fail.
   *
   * @param name an example of a valid name
   * @param postCode an example of a valid post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @ParameterizedTest
  @CsvSource({"testCompany,EH10_9XD",
              "testCompany2,EH17_8BW",
              "testCompany3,EH6_6LB",
              "testCompany4,EH15_8LS",
              "testCompany5,EH8_9EU"})
  public void testCateringCompanyInvalidEndpoint(String name, String postCode) {
    // Test invalid endpoint
    CateringCompanyClientImp invalidClient;
    invalidClient = new CateringCompanyClientImp(null);
    assertFalse(invalidClient.registerCateringCompany(name, postCode), "A catering " +
        "company registered using an invalid endpoint should not be able to register.");
  }

  /**
   * A method to mocks picking a food box and placing an order with the help of
   * simple HTTP Requests to the server
   *
   * @param foodBoxId ID to which the order is to be placed
   * @param name name of the catering company to be registered
   * @param postCode postCode of the catering company to be registered
   * @param chi CHI of the shielding individual to be registered
   * @return order number of the order recently placed
   */
  private int mockSetup(int foodBoxId, String name, String postCode, String chi) {
    ShieldingIndividualClientImp registeredShieldingIndividual;
    registeredShieldingIndividual = new
        ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // registering
    mockShieldingIndividualRegistration(registeredShieldingIndividual, chi);
    mockCateringCompanyRegistration(cateringCompanyClient, name, postCode);

    // Pick a food box and place an order
    ShieldingIndividualClientImpTest.mockPickFoodBox(registeredShieldingIndividual, foodBoxId);
    return ShieldingIndividualClientImpTest.mockPlaceOrder(registeredShieldingIndividual);
  }

  // Mock registration process for a catering company
  private void mockCateringCompanyRegistration(CateringCompanyClientImp cateringCompanyClient,
                                               String name, String postCode) {
    // Register the catering company and shielding individual
    try {
      String request = String.format(QueryStrings.REGISTER_CATERING_COMPANY.toString(),
          name, postCode);
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Unsuccessful catering company registration!";
    }

    // Setting parameters
    cateringCompanyClient.setRegistered(true);
    cateringCompanyClient.setPostCode(postCode);
    cateringCompanyClient.setName(name);

  }

  // Mock registration process for a shielding individual
  private void
  mockShieldingIndividualRegistration(ShieldingIndividualClientImp registeredShieldingIndividual,
                                      String chi) {
    // Register the catering company and shielding individual
    try {
      // Register the catering company and Shielding individual using HTTP Request
      String nameShielding;
      String postCodeShielding;
      String surnameShielding;
      String phoneNumberShielding;
      boolean registeredShielding;

      String requestShielding =
          String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(), chi);

      String response =
          ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + requestShielding);

      if (!response.equals(ServerResponse.ALREADY_REGISTERED.toString())
          && !response.equals(ServerResponse.NO_CHI.toString())) {

        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> details = new Gson().fromJson(response, type);
        boolean success =
            Objects.nonNull(details) && details.size() == 4 && !details.contains(null);

        if (success) {
          // Parse the response from the server
          postCodeShielding = details.get(0).replace(' ', '_');
          nameShielding = details.get(1);
          surnameShielding = details.get(2);
          phoneNumberShielding = details.get(3);
          registeredShielding = true;

          // Setting parameters
          registeredShieldingIndividual.setRegistered(registeredShielding);
          registeredShieldingIndividual.setChi(chi);
          registeredShieldingIndividual.setName(nameShielding);
          registeredShieldingIndividual.setPostCode(postCodeShielding);
          registeredShieldingIndividual.setSurname(surnameShielding);
          registeredShieldingIndividual.setPhoneNumber(phoneNumberShielding);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Unsuccessful shielding individual registration!";
    }
  }

  // --------------- UPDATE CATERING COMPANY ORDER STATUS ---------------

  /**
   * Tests that an unregistered catering company should not be able
   * to update status for any catering company order.
   *
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @Test
  public void testCateringCompanyUpdateOrderStatusUnregistered() {
    assertFalse(cateringCompanyClient.updateOrderStatus(1, "placed"),
            "An unregistered catering company should not be able to update order status.");
  }

  /**
   * Tests that a registered catering company should not be able to update status If the order
   * number and/or the order status are invalid
   *
   * @param foodBoxId an example of valid food box ID
   * @param name an example of valid catering company name
   * @param postCode an example of valid catering company post code
   * @param chi an example of valid shielding individual CHI
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({
    "1,testCompany7,EH7_8YQ,1612151514",
    "2,testCompany8,EH13_9DK,0101115501",
    "3,testCompany9,EH11_1WN,0302115615",
    "4,testCompany11,EH14_2ZJ,0409123510",
    "5,testCompany12,EH11_3RT,1909038418"
  })
  public void testCateringCompanyUpdateOrderStatusInvalidInput(
      int foodBoxId, String name, String postCode, String chi) {

    // Mock registration of catering company and shielding individual using HTTP Request
    int currentOrderNumber = mockSetup(foodBoxId, name, postCode, chi);

    assertFalse(cateringCompanyClient.updateOrderStatus(currentOrderNumber, null),
        "A registered catering company should not be able to " +
            "update order status to an invalid status.");

    assertFalse(cateringCompanyClient.updateOrderStatus(NONEXISTENT_ORDER_NUMBER, "packed"),
        "A registered catering company should not be able to " +
            "update order status using an invalid order number.");

    assertFalse(cateringCompanyClient.updateOrderStatus(NONEXISTENT_ORDER_NUMBER, null),
        "A registered catering company should not be able to " +
            "update order status to an invalid input.");

    assertFalse(cateringCompanyClient.updateOrderStatus(currentOrderNumber,
        "YANCHICK_OFFICIAL"),
        "A registered catering company should not be able to " +
            "update order status for an existing order to an invalid status.");
  }

  /**
   * Tests that a registered catering company should be able to
   * update status with valid input parameters
   *
   * @param foodBoxId an example of valid food box ID
   * @param name an example of valid catering company name
   * @param postCode an example of valid catering company post code
   * @param chi an example of valid shielding individual CHI
   * @param invalidStatus an example of possible order status
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"1,testCompany4,EH17_5YO,1008882105,WHAT_A_STATUS",
              "2,testCompany5,EH11_8AO,2607163105,Спасибо",
              "3,testCompany6,EH7_6GV,1010823214,goodbye"})
  public void testCateringCompanyUpdateOrderStatusInvalidStatus(int foodBoxId, String name,
                                                             String postCode, String chi,
                                                                String invalidStatus) {

    // Mock registration of catering company and shielding individual using HTTP Request
    int currentOrderNumber = mockSetup(foodBoxId, name, postCode, chi);

    assertFalse(cateringCompanyClient.updateOrderStatus(currentOrderNumber, invalidStatus),
        "A registered catering company should not be able update to non-existent status.");

  }

  /**
   * Tests that a registered catering company should be able to
   * update status with valid input parameters
   *
   * @param foodBoxId an example of valid food box ID
   * @param name an example of valid catering company name
   * @param postCode an example of valid catering company post code
   * @param chi an example of valid shielding individual CHI
   * @param status an example of possible order status
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"1,testCompany48,EH17_4JC,2307054709,packed",
              "2,testCompany5123,EH11_6DP,1705989418,dispatched",
              "3,testCompany5356,EH11_7RZ,0707769007,delivered"})
  public void testCateringCompanyUpdateOrderStatusSuccessful(int foodBoxId, String name,
                                                             String postCode, String chi,
                                                             String status) {

    // Mock registration of catering company and shielding individual using HTTP Request
    int currentOrderNumber = mockSetup(foodBoxId, name, postCode, chi);

    assertTrue(cateringCompanyClient.updateOrderStatus(currentOrderNumber, status),
        "A registered catering company should be able to " +
            "update order status to a latter status.");

    assertFalse(cateringCompanyClient.updateOrderStatus(currentOrderNumber, status),
        "A registered catering company should not be able to " +
            "update order status to the current status.");

    assertFalse(cateringCompanyClient.updateOrderStatus(currentOrderNumber, "cancelled"),
        "A registered catering company should not be able to cancel an order " +
            "at any stage.");

  }
}
