package shield;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.util.Properties;

/**
 * Unit tests for the class SupermarketClientImp.
 */
public class SupermarketClientImpTest {
  /** Valid test post codes. */
  private static final String[] TEST_POST_CODES = new String[] {
      "EH6_5XL","EH6_4BL","EH2_9UQ","EH7_9LE","EH13_8RJ","EH10_5RJ","EH15_2MM","EH17_8IB","EH2_3WA",
      "EH7_2UR","EH15_7KX","EH10_7AD","EH3_5LN","EH17_9TO","EH1_6GU","EH8_8SD","EH6_6FM","EH16_2ZT",
      "EH15_8RZ","EH4_5HO","EH4_7VW","EH5_2RN","EH7_7XE","EH6_3UH","EH1_2CI","EH16_6RK","EH5_1HH",
      "EH16_2GG","EH7_6NT","EH13_1EA","EH4_4YQ","EH7_2VI","EH7_3XY","EH14_2UO","EH16_8GR",
      "EH12_5GM","EH9_5PR","EH16_4LI","EH2_9TI","EH4_3HQ","EH4_2DR","EH10_2VD","EH16_6QI",
      "EH16_4WJ","EH15_5SX","EH10_4ZG","EH5_5IT","EH11_7AY","EH16_9UG","EH13_1ID","EH1_1CC",
      "EH16_5PW","EH1_5SB","EH4_3EK","EH13_9TB","EH8_1UO","EH1_7YL","EH3_8ZS","EH1_5WD","EH7_9WB",
      "EH4_6XI","EH2_6SH","EH9_3BC","EH3_9NO","EH4_9MC","EH3_3BU","EH3_5OO","EH10_1VQ","EH5_1UR",
      "EH4_5VD","EH16_7ST","EH17_3TK","EH1_9BH","EH7_5SP","EH8_9HM","EH10_2AQ","EH7_4RJ","EH1_7CI",
      "EH7_7JE","EH15_2RX","EH16_4SK","EH7_2VY","EH17_1ZV","EH10_2FY","EH10_4BG","EH17_4ZG",
      "EH12_9JE","EH5_7UO","EH11_9QZ","EH11_2ED","EH11_2ID","EH5_4LP","EH17_4JX","EH7_8LX",
      "EH4_1IA","EH10_2GV","EH13_5LT","EH3_5XS","EH7_6EX","EH1_3HT","EH2_2BY","EH7_5LO","EH13_8JG",
      "EH12_9EZ"
  };
  /** Name of the file containing properties related to the client. */
  private static final String CLIENT_PROPS_FILENAME = "client.cfg";
  /** Properties related to the client. */
  private static final Properties CLIENT_PROPS = loadProperties(CLIENT_PROPS_FILENAME);
  /** Test counter. */
  private static int testNumber = 0;
  /** An unregistered supermarket client. */
  private SupermarketClientImp unregisteredClient;
  /** A registered supermarket client. */
  private SupermarketClientImp registeredClient;

  @BeforeEach
  public void setup() {
    unregisteredClient = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));
    registeredClient = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    String name = "SUPERMARKET_" + testNumber;
    assert testNumber < TEST_POST_CODES.length : "Ran out of post codes for testing!";
    String postCode = TEST_POST_CODES[testNumber];
    try {
      String request = String.format(QueryStrings.REGISTER_SUPERMARKET.toString(), name, postCode);

      String response = ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint")
          + request);
      assertEquals(ServerResponse.REGISTRATION_SUCCESS.toString(), response,
          "Could not register supermarket!");
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Something went wrong with the registration";
    }
    registeredClient.setRegistered(true);
    registeredClient.setName(name);
    registeredClient.setPostCode(postCode);

    testNumber++;
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

  // --------------- REGISTER SUPERMARKET ---------------

  /**
   * Tests a valid supermarket registration.
   * A new registration using a valid supermarket name and post code should succeed.
   *
   * @param validName an example of a valid supermarket name
   * @param validPostCode an example of a valid supermarket post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @ParameterizedTest
  @CsvSource({"CO OP,EH7_7AE", "TESCO,EH16_8YN", "LIDL,EH3_4JD", "CO OP,EH16_2BE", "LIDL,EH3_1AN"})
  public void testValidRegistration(String validName, String validPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));
    assertTrue(client.registerSupermarket(validName, validPostCode),
        "A supermarket should be able to register using a valid name and post code.");
  }

  /**
   * Tests repeated supermarket registration.
   * Repeated registration should succeed, whatever supermarket name or post code is used
   * to attempt re-registration.
   *
   * @param validName an example of a valid name
   * @param validPostCode an example of valid post code
   * @param arbitraryName is name of arbitrary supermarket
   * @param arbitraryPostCode is post code of arbitrary supermarket
   * @see SupermarketClientImp#registerSupermarket
   */
  @ParameterizedTest
  @CsvSource({  // First two fields are valid; latter two arbitrary
      "LIDL,EH6_5JX,,NOT_A_POSTCODE",
      "TESCO,EH11_3PE,TESCO,",
      "COOP,EH13_3FV,TESCO,EH11_3PE",
      "LIDL,EH9_3LL,LIDL,EH5_6CA",
  })
  public void testRepeatedRegistration(String validName, String validPostCode,
                                       String arbitraryName, String arbitraryPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Same method so no need of direct HTTP Request
    client.registerSupermarket(validName, validPostCode);
    assertTrue(client.registerSupermarket(arbitraryName, arbitraryPostCode),
        "An attempt at re-registration using should always return true " +
            "for a registered supermarket.");
  }

  /**
   * Tests invalid supermarket registration.
   * Registration using an invalid name and/or post code should not be possible.
   *
   * @param validName an example of a valid supermarket name
   * @param validPostCode an example of a valid supermarket post code
   * @param invalidPostCode an example of an invalid supermarket post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @ParameterizedTest
  @CsvSource({"LIDL,EH9_3UE,MN9_3UE",
              "TESCO,EH13_8DK,EH100_8DK",
              "COOP,EH9_6KA,EH9 6KA",
              "SAINSBURY'S,EH7_8BD,EH_8BD",
              "LIDL,EH6_5TF,EH6_5TFA"
  })
  public void testInvalidRegistration(String validName, String validPostCode,
                                      String invalidPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));
    assertFalse(client.registerSupermarket(validName, invalidPostCode),
        "A supermarket should not be able to register using a valid name if " +
            "the post code is invalid.");
    assertFalse(client.registerSupermarket(null, validPostCode),
        "A supermarket should not be able to register using a valid post code if " +
            "the name is invalid.");
    assertFalse(client.registerSupermarket(null, invalidPostCode),
        "A supermarket should not be able to register using if " +
            "both the name and the post code are invalid.");
  }

  /**
   * Tests supermarket registration using an invalid endpoint.
   * Registration using an invalid API endpoint should not be possible.
   *
   * @param invalidEndpoint an example of an invalid endpoint
   * @see SupermarketClientImp#registerSupermarket
   */
  @ParameterizedTest
  @ValueSource(strings = {"NOT_AN_ENDPOINT", "http://notahost:5000", "http://localhosttttt"})
  public void testInvalidEndpointRegistration(String invalidEndpoint) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(invalidEndpoint);
    String validName = "SAINSBURY'S";
    String validPostCode = "EH4_6AL";
    assertFalse(client.registerSupermarket(validName, validPostCode),
        "A supermarket with an invalid endpoint should not be able to register even " +
            "if a valid name and post code are used.");
  }


  // --------------- RECORD SUPERMARKET ORDER ---------------

  /**
   * Tests that an unregistered supermarket should not be able to record a new supermarket order.
   * This should not be possible irrespective of whether the CHI or order number are valid.
   *
   * @param arbitraryChi an arbitrary CHI number
   * @param arbitraryOrderNumber an arbitrary order number
   * @see SupermarketClientImp#recordSupermarketOrder
   */
  @ParameterizedTest
  @CsvSource({"2201980609,1", "1104158008,13", "2202976301,7"})
  public void testRecordSupermarketUnregistered(String arbitraryChi, int arbitraryOrderNumber) {
    assertFalse(unregisteredClient.recordSupermarketOrder(arbitraryChi, arbitraryOrderNumber),
        "A registered supermarket should be able to record a new supermarket order " +
            "using a valid CHI and order number.");
  }

  /**
   * Tests that a registered supermarket should be able to record a new supermarket order using a
   * valid order number and properly-formatted CHI number.
   *
   * @param validChi an example of a properly-formatted CHI number
   * @param validOrderNumber an example of a properly-formatted order number
   * @see SupermarketClientImp#recordSupermarketOrder
   */
  @ParameterizedTest
  @CsvSource({"2305110410,1", "2707910412,2",
              "0512021501,100",
              "1604000902,1312",
              "2012703718,977141"
  })
  public void testRecordSupermarketRegisteredValidParams(String validChi, int validOrderNumber) {
    assertFalse(registeredClient.recordSupermarketOrder(validChi, validOrderNumber),
        "A registered supermarket should be able to record a new supermarket order " +
            "if the CHI number does not  correspond to a registered shielding individual.");

    try {
      // HTTP Request to register individual
      String request = String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(),
                                     validChi);
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);

    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Shielding individual invalid registration";
    }
    assertTrue(registeredClient.recordSupermarketOrder(validChi, validOrderNumber),
        "A registered supermarket should be able to record a new supermarket " +
            "order if the CHI number corresponds to a registered shielding individual.");
  }

  /**
   * Tests that a registered supermarket should not be able to record a new supermarket order unless
   * bot a valid order number and a properly-formatted CHI number are used.
   *
   * @param validChi an example of a valid CHI number
   * @param validOrderNumber an example of a properly formatted order number
   * @param invalidChi an example of an invalid CHI number
   * @param invalidOrderNumber an example of an improperly formatted order number
   * @see SupermarketClientImp#recordSupermarketOrder
   */
  @ParameterizedTest
  @CsvSource({"2211836708,1,0007715306,-1",
              "2403766408,100,,-1000",
              "2802127918,341,0702789742,-2000"
  })
  public void testRecordSupermarketRegisteredInvalidParams(String validChi, int validOrderNumber,
                                                           String invalidChi,
                                                           int invalidOrderNumber) {
    assertFalse(registeredClient.recordSupermarketOrder(validChi, invalidOrderNumber),
        "A registered supermarket should not be able to record a new order using " +
            "a valid CHI number if the order number is invalid.");
    assertFalse(registeredClient.recordSupermarketOrder(invalidChi, invalidOrderNumber),
        "A registered supermarket should not be able to record a new order " +
            "if both the CHI number and order number are invalid.");
    assertFalse(registeredClient.recordSupermarketOrder(invalidChi, validOrderNumber),
        "A registered supermarket should not be able to record a new order using " +
            "a valid order number if the order number is invalid.");
  }


  // --------------- UPDATE SUPERMARKET ORDER STATUS ---------------

  /**
   * Tests that an unregistered supermarket should not be able to update status
   * for any supermarket order.
   *
   * @param arbitraryOrderNumber an arbitrary order number
   * @param arbitraryStatus an arbitrary order status
   * @see SupermarketClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"11233,placed", "14561,packed", "14122,dispatched", "21467,delivered", "28951,"})
  public void testUpdateOrderStatusUnregistered(int arbitraryOrderNumber, String arbitraryStatus) {
    assertFalse(unregisteredClient.updateOrderStatus(arbitraryOrderNumber,arbitraryStatus),
        "An unregistered supermarket should not be able to update the status of " +
            "any order.");
  }

  /**
   * Tests that a registered supermarket should not be able to update status for an
   * unrecorded supermarket order, but it should be able to do so for a recorded one.
   *
   * @param validChi a valid CHI number
   * @param validOrderNumber a valid order number
   * @param validOrderStatus a valid order Status
   * @see SupermarketClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"2101733819,8567564,packed",
              "1910764201,423456,dispatched",
              "0710996819,156789,delivered"
  })
  public void testUpdateOrderStatusRegisteredValidStatus(String validChi, int validOrderNumber,
                                                         String validOrderStatus) {
    assertFalse(registeredClient.updateOrderStatus(validOrderNumber, validOrderStatus),
        "A registered supermarket should not be able to update status for an order " +
            "that has not been recorded in the system.");

    try {
      // HTTP Request to register individual
      String request =
          String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(), validChi);

      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);

      // HTTP Request to record the order
      String requestRecord = String.format(QueryStrings.RECORD_SUPERMARKET_ORDER.toString(),
          validChi, validOrderNumber, registeredClient.getName(), registeredClient.getPostCode());

      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + requestRecord);
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Either shielding individual invalid registration or order record invalid";
    }
    assertTrue(registeredClient.updateOrderStatus(validOrderNumber, validOrderStatus),
        "A registered supermarket should be able to update status for an order that " +
            "has not been recorded in the system.");
  }

  /**
   * Tests that a registered supermarket should not be able to update the status
   * for a recorded or unrecorded order to an invalid status.
   *
   * @param validChi a valid CHI number
   * @param validOrderNumber a valid order number
   * @param invalidOrderStatus an invalid order Status
   * @see SupermarketClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"1303786400,3454675,placed",
      "0407962416,8655678,unknown_status",
      "0406146002,1123452,cancelled",
      "2103987102,1674521,"
  })
  public void testUpdateOrderStatusRegisteredInvalidStatus(String validChi, int validOrderNumber,
                                                           String invalidOrderStatus) {
    assertFalse(registeredClient.updateOrderStatus(validOrderNumber, invalidOrderStatus),
        "A registered supermarket should not be able to update status for an order " +
            "that has not been recorded in the system.");

    try {
      // HTTP Request to register individual
      String request = String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(),
                                     validChi);
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);

      // HTTP Request to record the order
      String requestRecord = String.format(QueryStrings.RECORD_SUPERMARKET_ORDER.toString(),
          validChi, validOrderNumber, registeredClient.getName(), registeredClient.getPostCode());
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + requestRecord);

    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Either shielding individual invalid registration or order record invalid";
    }
    assertFalse(registeredClient.updateOrderStatus(validOrderNumber, invalidOrderStatus),
        "A registered supermarket should not be able to update status for an order " +
            "that has not been recorded in the system.");
  }

  /**
   * Tests that a registered supermarket should not be able to update the status for an order
   * using an invalid order number.
   *
   * @param validChi a valid CHI number
   * @param invalidOrderNumber an invalid order number
   * @param validOrderStatus a valid order Status
   * @see SupermarketClientImp#updateOrderStatus
   */
  @ParameterizedTest
  @CsvSource({"1412054203,3454675,placed",
      "1610147306,8655678,unknown_status",
      "1310015315,1123452,cancelled",
      "1701131510,1674521,"
  })
  public void testUpdateOrderStatusRegisteredInvalidOrderNumber(String validChi,
                                                                int invalidOrderNumber,
                                                                String validOrderStatus) {
    try {
      // HTTP Request to register individual
      String request = String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(),
                                     validChi);
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);

      // HTTP Request to record the order
      String requestRecord = String.format(QueryStrings.RECORD_SUPERMARKET_ORDER.toString(),
          validChi, invalidOrderNumber, registeredClient.getName(), registeredClient.getPostCode());
      ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + requestRecord);
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Either shielding individual invalid registration or order record invalid";
    }
    assertFalse(registeredClient.updateOrderStatus(invalidOrderNumber, validOrderStatus),
        "A registered supermarket should not be able to update status for an order " +
            "using an invalid order number even if the order status is valid.");
  }
}
