package shield;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * System tests for essential use cases.
 */
public class SystemTests {
  /** Valid test CHI numbers. */
  private static final String[] TEST_CHIS = new String[] {
      "2308064000","2602965508","0510853808","3003717517","2510721707","0902984705","1805208402",
      "0207060109","2506907015","1105183311","0512852817","0704880312","1108737300","2605843615",
      "1609748512","2611854208","1512045512","3108993008","0806168018","1209748809","0411980713",
      "0211056719","1501194812","0201043102","3009995311","0607790908","2107922115","1607955414",
      "0111829201","0405904219","1501973903","2211072501","1005973402","3006916614","2506742613",
      "1409810719","3110196216","1203099700","1102202017","2510134019","2608082900","2512966107",
      "1609809811","2802883617","0311025510","0707146311","1004726713","2711135615","0805193819",
      "0510931206","1711802208","2809973501","1001939107","0503156305","0310010816","1303927809",
      "2711755216","1007191308","1009096414","2611898207","1007881706","0312864119","0303811319",
      "1801215605","2007199508","1608779203","0911096905","3112829509","2307737405","1611869404",
      "0609898109","0703143303","1107160516","1705131919","0812117616","2812966101","1705010100",
      "1709018810","0602703403","0204059813","0507990018","2001198611","1708760108","2907205411",
      "1311734512","0503977902","2610805202","0601734500","1804851616","2307077717","2003119409",
      "1403742917","3008100301","2211973905","2806728103","0511023613","1103861613","1202996414",
      "1101835011","2411774312","1106119001","2802844409","1206949211","1409969810","2503997208",
      "0106743411","2312980112","1007992601","2301103203","1505982416","2605840504","2911010817",
      "0202050707","0109988501","0406075814","0803786703","0704873612","1705042105","2102039710",
      "1903723405","2003033108","0711094602","1703124419","0104016819","1711839905","2205792002",
      "2212063601","2507053117","2012895605","3003974104","2103038308","1312803400","3003977605",
      "0307957308","0509941915","1604833214","1711190115","0902120005","1705712906","2103163114",
      "1310955616","3112018116","0907822819","2805073319","1401924202","0312982601","1312813213",
      "2112161114","1006153005","1803176812","1802003617","3103940115","1404016814","2510827507",
      "2608864600","1307962101","2809897202","0907750601","2409837911","1305046410","0110874608",
      "0402903017","3107098209","2109153713","2511748800","2005092415","1109004215","1011146703",
      "2411816913","0903071017","3105955608","2712131304","0102982905","2404776202","2612024413",
      "2111924519","2603771501","0903216412","1408933403","1708795213","0105978712","0706070607",
      "2206028514","0909758206","0309883008","0909922100","1610752506","1703029015","1607880810",
      "0209702710","0212150214","0501038307","2610864007","2602727004","2305020003","1210171919",
      "2412870500","0802029108","2211843002","1404174500","1912717412","2304908008","2112916500",
      "3001840505","3001814219","0510016013","1301147800","1906898212","2805202514","0608158818",
      "2112822205","1707772502","2812012305","0206790907","2306854613","2312201514","2206954811",
      "2007031003","0911133604","2605070510","2210164215","0807935004","2506721419","0307734317",
      "0511125017","2309958308","0204721900","1105052403","0906067716","0205148114","3005155004",
      "0901184801","1009800316","2801771816","1701928709","1507141016","1802875208","0101731007",
      "1801875617","1210944201","2803045706","2312166306","2908176912","2208037802","0703701908",
      "0105718403","2211821204","0408123002","1305738917","2107128302","3010718808","3003861418",
      "1505899011","2010844511","2910951919","0608742915","0906055702","1811821316","0103756710",
      "0909204408","2508739106","1607916317","2503884703","0802028217","1912122711","0905993718",
      "1405100612","1411791805","2212093403","1405142005","2705859105","0107010706","1203767116",
      "0910011600","1801111017","1206031818","2009099407","0308938509","2910939815","1802864519",
      "0111105809","2206915208","1710170115","0712908712","0905810810","0307725812","1505091208",
      "0608934718","0601041008","2909967107","2111165104","0204812704","3009818214","1905720312",
      "1809054812","1303989116","0303894918","1503190901","1910934217","0406885904","0403143906",
      "2205010300","1505129819","1604174711","1609087401","0110968007","0209752606","0407007216",
      "0209179609","1403055210","2706055214","2904164513","1401963317","1503035917","2012963816",
      "2303082713","1106899814","2804953005","2009098511","0409893119","0101917813","2601106914",
      "2305955600","2202709115","0708202417","2005179114","3110891500","1204133311","1810866201",
      "3004793301","1201983914","1502912901","2105997218","1401778904","1002048416","0210740500",
      "0502810707","0610134301","1702804114","0907157204","1808173218","2904866915","0409164810",
      "1602175600","0402748405","2611032512","0405985205","0208948013","0812812501","1806703401",
      "1107915905","1804843202","0208845602","1405006206","1405706418","2708870706","1811027100",
      "0109948517","3012754013","0805147503","0504868912","0202720608","0807168403","1511064318",
      "2707765200","2704777615","2207964219","1908928502","2012814705","1012769815","2910877905",
      "1806007616","3107024009","1405765616","3101171811","0504109501","2611709915","2709101111",
      "2306725515","1705094517","0409881104","2511102908","0507185214","0804011406","1110089100",
      "1902057817","1609746710","2711789501","1507839804","1008769918","3107806102","2704882311",
      "2002796117","1704051807","0111019809","0511863508","1911826915","2709066714","1406145102",
      "1102197417","1910929716","0708875007","0702983204","1709780500","2204977505","0205891518",
      "1904996609","1611896614","3009770619","1905159704","2203964215","3101097408","1807890812",
      "0105727311","2809873715","0903836112"
  };
  /** Valid test post codes. */
  private static final String[] TEST_POST_CODES = new String[] {
      "EH17_8CH","EH10_5AY","EH15_4MC","EH1_3WT","EH17_8OY","EH1_1HX","EH16_2TM","EH4_9QO",
      "EH14_4QE","EH1_3AP","EH1_9BR","EH8_2NJ","EH9_4KN","EH17_9DY","EH1_3TI","EH8_3FF","EH8_3RP",
      "EH17_3CR","EH5_3AZ","EH9_9AA","EH1_3AG","EH10_3BE","EH9_4UT","EH8_8PV","EH17_2AT","EH5_9OT",
      "EH10_9TP","EH2_2NL","EH14_4IU","EH15_2EP","EH16_6QU","EH5_4EN","EH14_5RJ","EH7_4QH",
      "EH11_6NL","EH13_2JH","EH17_5KY","EH9_6OT","EH2_8TX","EH12_9SS","EH11_7RK","EH13_6HU",
      "EH15_8WX","EH14_2KQ","EH10_4DR","EH7_7RY","EH4_5CO","EH14_1BA","EH10_7ZK","EH6_3MN",
      "EH5_1JM","EH16_2CN","EH13_2LD","EH3_1QI","EH14_6HS","EH1_7WG","EH7_1SU","EH14_5GO","EH3_4RX",
      "EH15_1NR","EH4_6KO","EH16_7SG","EH12_1WV","EH1_8FN","EH3_8BJ","EH10_5HM","EH14_2KI",
      "EH8_9AP","EH6_6TK","EH4_8AL","EH11_2TW","EH3_1CE","EH9_6ZI","EH17_2CX","EH3_3QK","EH1_3FN",
      "EH3_7JQ","EH2_8MG","EH15_5ID","EH8_1EV","EH2_9RG","EH4_7WF","EH14_4SY","EH14_8OG",
      "EH7_4NI","EH11_3GM","EH8_1NA","EH13_6ZQ","EH6_6HW","EH15_6PA","EH8_6EI","EH8_4CQ",
      "EH9_8DF","EH10_1NP","EH14_6PM","EH13_6YA","EH7_5XE","EH2_3FX","EH14_4UC","EH15_5QV",
      "EH15_7XR","EH13_1TN","EH5_8EZ","EH3_1SZ","EH3_3YZ","EH9_8AJ","EH4_3XK","EH1_2DP","EH14_2OE",
      "EH4_4PM","EH4_6VZ","EH11_9UE","EH15_9CL","EH14_6EM","EH5_9CK","EH16_9GZ","EH3_8PU",
      "EH10_4OO","EH16_9LR","EH3_2HE","EH15_4FA","EH11_7VE","EH6_1RP","EH6_8XO","EH12_8ON",
      "EH5_3PX","EH12_9FQ","EH4_9AM","EH4_7LF","EH9_9DY","EH3_5ST","EH14_6OA","EH2_1IA",
      "EH14_4CB","EH9_1PG","EH6_8NN","EH6_5TI","EH10_9SS","EH14_6PZ","EH4_4LX","EH2_5HN",
      "EH12_4JD","EH12_9VM","EH3_9HD","EH10_7KI","EH8_3UW","EH4_5WQ","EH7_9NE","EH1_7JK",
      "EH12_5JU","EH3_1XN","EH16_8WA","EH12_3XX","EH8_2UO","EH6_9RC","EH14_5DU","EH1_1VH","EH1_5MB",
      "EH8_3PU","EH7_4XA","EH16_9HD","EH13_1QI","EH15_5CD","EH9_7NL","EH15_1OO","EH11_5AJ",
      "EH9_5EB","EH15_7MR","EH7_2FY","EH17_2HV","EH16_7KE","EH8_2EL","EH9_3VM","EH13_4LY",
      "EH13_6JQ","EH14_4HW","EH17_2SF","EH2_9RN","EH1_1RH","EH5_1YT","EH2_1RD","EH12_4WD",
      "EH4_4DJ","EH11_6IO","EH17_8HZ","EH7_9CK","EH15_2OV","EH11_5CJ","EH6_4YM","EH6_7YX",
      "EH10_1ON","EH16_8FU","EH17_3OC","EH15_7UN","EH13_7TT","EH15_8GX","EH6_3YY","EH7_2TT",
      "EH17_9QR","EH9_7XU","EH8_4BA","EH10_7MC","EH6_9XJ","EH7_4BT","EH7_4EP","EH16_1XX","EH4_6SJ",
      "EH2_2UT","EH1_8NC","EH4_2TX","EH12_4YS","EH5_4IA","EH3_5GX","EH9_7TB","EH15_6EI","EH14_1UY",
      "EH4_8SM","EH17_3KU","EH9_7ZG","EH10_4ZS","EH16_8IL","EH11_3RG","EH14_5AZ","EH16_9ES",
      "EH1_9IU","EH3_2DN","EH17_7BE","EH3_6FU","EH15_5NL","EH9_3ET","EH11_2CN","EH3_9PP",
      "EH15_9PA","EH2_8QY","EH15_7OV","EH13_5CC","EH11_1YS","EH12_5FP","EH14_8XJ","EH7_6GG",
      "EH16_3GF","EH12_7ZC","EH4_8LG","EH1_7KA","EH17_9JO","EH12_6AV","EH2_3NF","EH4_3SZ",
      "EH16_7RB","EH8_9WJ","EH1_4WI","EH10_8TZ","EH9_8ZG","EH11_8NY","EH5_4OX","EH2_2SZ","EH12_9VT",
      "EH14_4UA","EH3_3JT","EH10_6BA","EH4_6JP","EH7_2WY","EH14_4WO","EH7_4GT","EH5_6IH",
      "EH3_4JZ","EH15_4CB","EH11_9UV","EH11_1UD","EH8_1IF","EH10_6XY","EH3_7PQ","EH12_8JH",
      "EH11_9JA","EH2_2AT","EH16_2NF","EH16_5EB","EH13_6SR","EH11_8NI","EH17_3IV","EH17_3HU",
      "EH17_3XA","EH13_2NK","EH17_2TK","EH14_5KX","EH1_2OE","EH8_4CT","EH9_1EI","EH9_7GX",
      "EH16_3AK","EH17_1LG","EH4_9CE","EH12_2TX","EH16_8KJ","EH14_9VT","EH11_3NG","EH4_2CU",
      "EH10_6PH","EH12_7WC","EH9_8PC","EH10_5PF","EH13_4MV","EH8_8IS","EH3_8BC","EH13_1ZQ",
      "EH14_9TC","EH12_9FC","EH2_3MT","EH5_6SI","EH6_6AJ","EH5_1YB","EH11_4EA","EH7_2EP",
      "EH11_3IB","EH8_7ER","EH2_3AW","EH4_6ND","EH13_4DI","EH8_7GE","EH16_3RQ","EH13_2VM",
      "EH15_5JB","EH5_8JV","EH10_5QU","EH17_6FA","EH17_2XM","EH13_3HW","EH6_3HJ","EH1_5OP",
      "EH13_9YW","EH13_8VR","EH4_2JQ","EH10_1SQ","EH17_6RF","EH6_5ZU","EH5_2JM","EH13_4ZA",
      "EH4_2OE","EH11_7YV","EH15_4JN","EH12_3UW","EH16_3XV","EH1_8DB","EH7_1GA","EH5_4WO",
      "EH1_1PS","EH7_8NK","EH3_6SE","EH11_7QS","EH9_1DD","EH5_5NM","EH15_5BG","EH3_7EO",
      "EH12_6LK","EH15_6BV","EH11_3XA","EH1_3LT","EH11_5ZD","EH1_6HZ","EH9_7PR","EH5_9TU",
      "EH17_5LQ","EH15_7RG","EH8_1BI","EH9_4QF","EH14_4VU","EH10_8CH","EH2_3VB","EH12_2IF",
      "EH11_7HM","EH10_8UA","EH7_9ZY","EH13_4RT","EH1_6RH","EH11_4OU","EH9_2CT","EH10_4IH",
      "EH2_8SS","EH2_2RG","EH2_3HK","EH16_6II","EH4_5YB","EH4_7HN","EH10_9ZN","EH11_7BU",
      "EH5_2GU","EH13_7QN","EH5_4GU","EH8_6WH","EH7_8ZN","EH13_5BL","EH10_1QH","EH8_6HE",
      "EH17_3DO","EH3_1PB","EH1_1TD","EH14_4UU","EH16_4RS","EH15_1ZG","EH14_1GF","EH8_5FT",
      "EH17_3JB","EH4_6QL","EH10_9IJ","EH10_5PE","EH16_5EO","EH8_5WX","EH1_1JU","EH1_4TD"
  };
  /** Name of the file containing properties related to the client. */
  private static final String CLIENT_PROPS_FILENAME = "client.cfg";
  /** Properties related to the client. */
  private static final Properties CLIENT_PROPS = loadProperties(CLIENT_PROPS_FILENAME);
  /** Test counter. */
  private static int testNumber = 0;
  /** A registered shielding individual client. */
  private ShieldingIndividualClientImp registeredShieldingIndividual;
  /** An unregistered shielding individual client. */
  private ShieldingIndividualClientImp unregisteredShieldingIndividual;
  /** A registered supermarket client. */
  private SupermarketClientImp registeredSupermarket;
  /** An unregistered supermarket client. */
  private CateringCompanyClientImp registeredCateringCompany;

  @BeforeEach
  public void setup() {
    unregisteredShieldingIndividual =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    // Register shielding individual
    assert testNumber < TEST_CHIS.length : "Ran out of CHI numbers for testing!";
    String chi = TEST_CHIS[testNumber];
    registeredShieldingIndividual =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    registeredShieldingIndividual.registerShieldingIndividual(chi);

    // Register a catering company
    String name = "SYSTEM_TEST_CATERING_COMPANY_" + testNumber;
    assert testNumber < TEST_POST_CODES.length : "Ran out of post codes for testing!";
    String postCode = TEST_POST_CODES[testNumber];
    registeredCateringCompany =
        new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));
    registeredCateringCompany.registerCateringCompany(name, postCode);

    // Register a supermarket
    String nameSupermarket = "SYSTEM_TEST_SUPERMARKET_" + testNumber;
    String postCodeSupermarket = TEST_POST_CODES[testNumber];
    registeredSupermarket = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));
    registeredSupermarket.registerSupermarket(nameSupermarket, postCodeSupermarket);

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
   * Tests new supermarket registration.
   * A new registration using a valid name and post code should succeed.
   *
   * @param validName a valid supermarket name
   * @param validPostCode a valid supermarket post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @DisplayName("Use case Register Supermarket: MSS")
  @ParameterizedTest
  @CsvSource({
      "LIDL,EH13_8TS",
      "TESCO,EH8_9NE",
      "SAINSBURY's,EH13_6RP",
      "CO OP,EH9_6GH",
      "Jack's,EH12_1UR",
      "Marks & Spencer,EH13_4UP",
      "Premier,EH14_5PL"
  })
  public void testSupermarketValidRegistration(String validName, String validPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.isRegistered(), "Prior to registration, the supermarket " +
        "should appear as unregistered in the system.");

    assertNull(client.getName(), "Prior to registration, " +
        "the supermarket's name should not have been initialised.");

    assertNull(client.getPostCode(), "Prior to registration, " +
        "the supermarket's post code should not have been initialised.");

    assertTrue(client.registerSupermarket(validName, validPostCode),
        "A supermarket with a valid name and post code should be able to register.");

    assertTrue(client.isRegistered(), "After successful registration, " +
        "the supermarket should appear as registered in the system.");

    assertEquals(validName, client.getName(), "After successful " +
        "registration, the name number used for registration should be stored.");

    assertEquals(validPostCode, client.getPostCode(), "After successful " +
        "registration, the name number used for registration should be stored.");
  }

  /**
   * Tests repeated supermarket registration.
   * A repeated registration should succeed, whatever the input.
   * The name and postcode, as well as registration status should remain unchanged.
   *
   * @param validName an example of a valid supermarket name
   * @param validPostCode an example of an valid supermarket post code
   * @param invalidPostCode an example of an invalid supermarket post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @DisplayName("Use case Register Supermarket: Extension - repeated registration")
  @ParameterizedTest
  @CsvSource({
      "LIDL,EH4_7IB,GW13_8TS",
      "TESCO,EH2_6NX,GW8_9NE",
      "SAINSBURY's,EH10_2WD,GW13_6RP",
      "CO OP,EH4_8XJ,GW9_6GH",
      "Jack's,EH10_1KY,GW12_1UR",
      "Marks & Spencer,EH13_2QW,GW13_4UP",
      "Premier,EH11_5ZT,"
  })
  public void testSupermarketRepeatedRegistration(String validName,
                                                  String validPostCode, String invalidPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Initial registration
    client.registerSupermarket(validName, validPostCode);

    // Repeated registration
    assertTrue(client.registerSupermarket(validName, validPostCode),
        "An attempt at re-registration using valid parameters should return " +
            "true for a registered supermarket.");

    assertTrue(client.registerSupermarket(validName, invalidPostCode),
        "An attempt at re-registration should return true for a registered " +
            "supermarket even if an invalid post code is used.");

    // Compare state
    assertTrue(client.isRegistered(), "Once registered and attempting to " +
        "re-register, the supermarket should stay registered.");

    assertEquals(validName, client.getName(), "Once registered and " +
        "attempting to re-register using a different name, the supermarket's name should stay the" +
        " same.");

    assertEquals(validPostCode, client.getPostCode(), "Once registered " +
        "and attempting to re-register using a different post code, the supermarket's post code " +
        "should stay the same.");
  }

  /**
   * Tests invalid supermarket registration.
   * Registration using an invalid supermarket name should not be possible.
   * After a failed registration, the supermarket should not appear as registered, and its name
   * and post code should not be initialised.
   *
   * @param arbitraryName an arbitrary supermarket name
   * @param invalidPostCode an invalid post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @DisplayName("Use case Register Supermarket: Extension - invalid CHI")
  @ParameterizedTest
  @CsvSource({
      ",ABC_6XV",
      "NHS_SUPERMARKET,EH99_5BI",
      ",_6XV",
      "ALI_EXPRESS,EH9 8LQ",
      ",EH18_8SQ",
      ",84102",
      "ian's groceries,125009"  // Kremlin
  })
  public void testSupermarketInvalidParametersRegistration(String arbitraryName,
                                                           String invalidPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.registerSupermarket(arbitraryName, invalidPostCode),
        "A supermarket with an invalid CHI number should not be able to register.");

    assertFalse(client.isRegistered(), "After a failed registration, " +
        "the supermarket should not appear as registered in the system.");

    assertNull(client.getName(), "After a failed registration, " +
        "the supermarket's name should not have been initialised.");

    assertNull(client.getPostCode(), "After a failed registration, " +
        "the supermarket's post code should not have been initialised.");
  }

  /**
   * Tests invalid supermarket registration.
   * Registration using an invalid endpoint should not be possible. After a failed registration,
   * the supermarket should not appear as registered, and its name and post
   * code should not be initialised.
   *
   * @param invalidEndpoint an example invalid endpoint
   * @see SupermarketClientImp#registerSupermarket
   */
  @DisplayName("Use case Register Supermarket: Extension - invalid endpoint")
  @ParameterizedTest
  @ValueSource(strings = {
      "GOOD_LUCK_FINDING_THIS_ENDPOINT", "http://notahost:5000", "http://locahost:-5000"
  })
  public void testSupermarketInvalidEndpointRegistration(String invalidEndpoint) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(invalidEndpoint);

    String validName = "Tesco EXPRESS";
    String validPostCode = "EH3_3FG";
    assertFalse(client.registerSupermarket(validName, validPostCode),
        "A supermarket with an invalid endpoint should not be able to register " +
            "even if a valid name and post code is provided.");

    assertFalse(client.isRegistered(), "After a failed registration, the " +
        "supermarket should not appear as registered in the system.");
    assertNull(client.getName(), "After a failed registration, the " +
        "supermarket's name should not have been initialised.");
    assertNull(client.getPostCode(), "After a failed registration, the " +
        "supermarket's name should not have been initialised.");
  }


  // --------------- REGISTER CATERING COMPANY ---------------

  /**
   * Tests new catering company registration.
   * A new registration using a valid name and post code should succeed.
   *
   * @param validName a valid catering company name
   * @param validPostCode a valid catering company post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @DisplayName("Use case Register Catering Company: MSS")
  @ParameterizedTest
  @CsvSource({
      "FAT & CO,EH16_5LS",
      "BEST CC,EH5_5EA",
      "BEST FOOD INC,EH17_5ZU",
      "YUMMY MUMMY,EH8_7TJ",
      "Maxine's Kitchen,EH10_8EV"
  })
  public void testCateringCompanyValidRegistration(String validName, String validPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.isRegistered(), "Prior to registration, " +
        "the catering company should appear as unregistered in the system.");

    assertNull(client.getName(), "Prior to registration, " +
        "the catering company's name should not have been initialised.");

    assertNull(client.getPostCode(), "Prior to registration, " +
        "the catering company's post code should not have been initialised.");

    assertTrue(client.registerSupermarket(validName, validPostCode),
        "A catering company with a valid name and post code should be able to register.");

    assertTrue(client.isRegistered(), "After successful registration, " +
        "the catering company should appear as registered in the system.");

    assertEquals(validName, client.getName(), "After successful " +
        "registration, the name number used for registration should be stored.");

    assertEquals(validPostCode, client.getPostCode(), "After successful " +
        "registration, the name number used for registration should be stored.");
  }

  /**
   * Tests repeated catering company registration.
   * A repeated registration should succeed, whatever the input.
   * The name and postcode, as well as registration status should remain unchanged.
   *
   * @param validName an example of a valid catering company name
   * @param validPostCode an example of an valid catering company post code
   * @param invalidPostCode an example of an invalid catering company post code
   * @see SupermarketClientImp#registerSupermarket
   */
  @DisplayName("Use case Register Catering Company: Extension - repeated registration")
  @ParameterizedTest
  @CsvSource({
      "LIDL,EH11_9RR,GW11_9RR",
      "TESCO,EH8_7FG,ABC_7FG",
      "SAINSBURY's,EH8_2IV,GW 2IV",
      "CO OP,EH2_8KF,GW2_8KF",
      "Jack's,EH15_5HS,EH15 5HS",
      "Marks & Spencer,EH15_3GT,EH_3GT",
      "Premier,EH9_7CU,E1 6AN"
  })
  public void testCateringCompanyRepeatedRegistration(String validName, String validPostCode,
                                                      String invalidPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Initial registration
    client.registerSupermarket(validName, validPostCode);

    // Repeated registration
    assertTrue(client.registerSupermarket(validName, validPostCode),
        "An attempt at re-registration using valid parameters should return " +
        "true for a registered catering company.");

    assertTrue(client.registerSupermarket(validName, invalidPostCode),
        "An attempt at re-registration should return true for a registered catering " +
            "company even if an invalid post code is used.");

    // Compare state
    assertTrue(client.isRegistered(), "Once registered and attempting " +
        "to re-register, the catering company should stay registered.");

    assertEquals(validName, client.getName(), "Once registered and " +
        "attempting to re-register using a different name, the catering company's " +
        "name should stay the same.");

    assertEquals(validPostCode, client.getPostCode(), "Once registered " +
        "and attempting to re-register using a different post code, the catering company's post " +
        "code should stay the same.");
  }

  /**
   * Tests invalid company catering registration.
   * Registration using an invalid catering company post code should not be possible.
   * After a failed registration, the catering company should not appear as registered, and its
   * name and post code should not be initialised.
   *
   * @param arbitraryName an arbitrary catering company name
   * @param invalidPostCode an invalid post code
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @DisplayName("Use case Register Catering Company: Extension - invalid CHI")
  @ParameterizedTest
  @CsvSource({",GW10_2FP",
      "Some random name,EH1 8ID",
      "???@$@%W%^,_7CY",
      ",EH14_QII",
      "Your favourite catering company chain,HE13_4UX",
      ",EH18_4LQ",
      ",EH16_100TH"
  })
  public void testCateringCompanyInvalidParametersRegistration(String arbitraryName,
                                                               String invalidPostCode) {
    SupermarketClientImp client;
    client = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.registerSupermarket(arbitraryName, invalidPostCode),
        "A catering company with an invalid CHI number should not be able to register.");

    assertFalse(client.isRegistered(), "After a failed registration, " +
        "the catering company should not appear as registered in the system.");

    assertNull(client.getName(), "After a failed registration, " +
        "the catering company's name should not have been initialised.");

    assertNull(client.getPostCode(), "After a failed registration, " +
        "the catering company's post code should not have been initialised.");
  }

  /**
   * Tests invalid catering company registration.
   * Registration using an invalid endpoint should not be possible. After a failed registration,
   * the catering company should not appear as registered, and its name and post code should not
   * be initialised.
   *
   * @param invalidEndpoint an example invalid endpoint
   * @see CateringCompanyClientImp#registerCateringCompany
   */
  @DisplayName("Use case Register Catering Company: Extension - invalid endpoint")
  @ParameterizedTest
  @ValueSource(strings = {"GOOD_LUCK_FINDING_THIS_ENDPOINT",
                          "http://notahost:5000",
                          "http://locahost:-5000",
                          "http://recipe_database.co.uk"
  })
  public void testCateringCompanyInvalidEndpointRegistration(String invalidEndpoint) {
    CateringCompanyClientImp client;
    client = new CateringCompanyClientImp(invalidEndpoint);

    String validName = "A serious catering company";
    String validPostCode = "EH2_2WC";
    assertFalse(client.registerCateringCompany(validName, validPostCode),
        "A catering company with an invalid endpoint should not be able to register " +
            "even if a valid name and post code is provided.");

    assertFalse(client.isRegistered(), "After a failed registration, " +
        "the catering company should not appear as registered in the system.");
    assertNull(client.getName(), "After a failed registration, " +
        "the catering company's name should not have been initialised.");
    assertNull(client.getPostCode(), "After a failed registration, " +
        "the catering company's name should not have been initialised.");
  }


  // --------------- REGISTER SHIELDING INDIVIDUAL ---------------

  /**
   * Tests new shielding individual registration.
   * A new registration using a valid CHI number should succeed.
   *
   *
   * @param validChi an example of a valid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: MSS")
  @ParameterizedTest
  @ValueSource(strings = {"0601993914", "0709739310", "0607901319", "1701701805",
                          "0801144403", "1404954416", "0112915218", "2512863312",
                          "0709033716", "3103104910", "1302959608", "2006140501"
  })
  public void testShieldingIndividualValidRegistration(String validChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.isRegistered(), "Prior to registration, " +
        "the shielding individual should appear as unregistered in the system.");


    assertNull(client.getChi(), "Prior to registration, " +
        "the shielding individual's CHI should not have been initialised.");

    assertTrue(client.registerShieldingIndividual(validChi), "A " +
        "shielding individual with a valid CHI number should be able to register.");

    assertTrue(client.isRegistered(), "After successful registration, " +
        "the shielding individual should appear as registered in the system.");

    assertEquals(validChi, client.getChi(), "After successful " +
        "registration, the CHI number used for registration should be stored.");
  }

  /**
   * Tests repeated shielding individual registration.
   * A repeated registration should succeed, whatever the input. The CHI and
   * registration status should remain unchanged.
   *
   * @param validChi an example of a valid CHI number
   * @param invalidChi an example of an invalid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: Extension - repeated registration")
  @ParameterizedTest
  @CsvSource({"0604206312,9999937312",    // Invalid day (right)
              "2010917203,",              // Empty CSV entry is parsed to null
              "0611715605,2700707312",    // Invalid month (right)
              "2212197905,0906858960",    // Invalid gender digit (right)
              "0404758614,14129986110"})  // Invalid length (right)
  public void testShieldingIndividualRepeatedRegistration(String validChi, String invalidChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Initial registration
    client.registerShieldingIndividual(validChi);

    // Repeated registration
    assertTrue(client.registerShieldingIndividual(validChi), "An attempt " +
        "at re-registration using valid CHI return true for a registered shielding individual");

    assertTrue(client.registerShieldingIndividual(invalidChi), "An attempt at re-registration " +
        "using an invalid CHI return true for a registered shielding individual");

    // Compare state
    assertTrue(client.isRegistered(), "Once registered and attempting to " +
        "re-register, the shielding individual should stay registered.");

    assertEquals(validChi, client.getChi(), "Once registered and " +
        "attempting to re-register using a different CHI, the shielding individual's CHI should " +
        "stay the same.");
  }

  /**
   * Tests invalid shielding individual registration.
   * Registration using an invalid CHI should not be possible. After a failed registration, the
   * shielding individual should not appear as registered, and their CHI should not get updated.
   *
   * @param invalidChi an example of an invalid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: Extension - invalid CHI")
  @ParameterizedTest
  @ValueSource(strings = {"THIS_CHI_REALLY_DOES_NOT_EXIST",
                          "1413841303",  // Invalid month
                          "2299201807",  // Invalid month
                          "3211096513",  // Invalid day
                          "0008973915",  // Invalid day
                          "14122086101"}) // Invalid length
  public void testShieldingIndividualInvalidChiRegistration(String invalidChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertFalse(client.registerShieldingIndividual(invalidChi),
        "A shielding individual with an invalid CHI number should not be able to register.");

    assertFalse(client.isRegistered(), "After a failed registration, " +
        "the shielding individual should not appear as registered in the system.");

    assertNotEquals(invalidChi, client.getChi(), "After a failed " +
        "registration, the shielding individual's CHI should not be updated.");

  }

  /**
   * Tests invalid shielding individual registration.
   * Registration using an invalid endpoint should not be possible. After a failed registration,
   * the shielding individual should not appear as registered, and their CHI should not get updated.
   *
   * @param invalidEndpoint an example invalid endpoint
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: Extension - invalid endpoint")
  @ParameterizedTest
  @ValueSource(strings = {"GOOD_LUCK_FINDING_THIS_ENDPOINT",
                          "http://notahost:5000",
                          "http://locahost:-5000"
  })
  public void testShieldingIndividualInvalidEndpointRegistration(String invalidEndpoint) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(invalidEndpoint);

    String validChi = "2706196911";
    assertFalse(client.registerShieldingIndividual(validChi), "A shielding " +
        "individual with an invalid endpoint should not be able to register even if a valid CHI " +
        "is used.");

    assertFalse(client.isRegistered(), "After a failed registration, " +
        "the shielding individual should not appear as registered in the system.");

    assertNotEquals(validChi, client.getChi(), "After a failed " +
        "registration, the shielding individual's CHI should not be updated.");
  }


  // --------------- PLACE FOOD BOX ORDER ---------------
  /**
   * Tests the main success scenario of the Place Food Box Order use case.
   * The shielding individual picks a food box using a valid food box ID and places an order.
   *
   * We assume the shielding individual wishes to order from a catering company, so we omit the
   * choice between supermarket and catering company.
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: MSS")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testPlaceValidFoodBoxOrder(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A registered shielding individual must be registered before placing an order.");

    // Shielding individual chooses a pre-assembled food box based on their dietary restrictions
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick any of the available " +
            "food boxes.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers, "The past orders for a registered shielding" +
        "individual should not be null.");

    int prevOrderCount = orderNumbers.size();
    assertTrue(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should be able to place an order after they picked a valid food box.");
    List<Integer> placedOrders = registeredShieldingIndividual.getOrderNumbers();

    assertNotNull(placedOrders,
        "The past orders for a registered shielding individual should not be null.");
    int newOrderCount = placedOrders.size();
    assertEquals(prevOrderCount + 1, newOrderCount,
        "Placing a new order should increase the number of placed orders.");

    // Check that food box and order contents match
    int currentOrderNumber = placedOrders.get(placedOrders.size() - 1);
    List<Integer> foodBoxItemIds =
        registeredShieldingIndividual.getItemIdsForFoodBox(validFoodBoxId);

    List<Integer> orderItemIds =
        registeredShieldingIndividual.getItemIdsForOrder(currentOrderNumber);

    assertNotNull(foodBoxItemIds, "The list of food box item IDs should not be " +
        "null for a registered shielding individual who picked a valid food box.");

    assertNotNull(orderItemIds, "The list of order item IDs should not be null " +
        "for a registered shielding individual who successfully placed an order.");
    foodBoxItemIds = foodBoxItemIds.stream().sorted().collect(Collectors.toList());
    orderItemIds = orderItemIds.stream().sorted().collect(Collectors.toList());

    assertEquals(foodBoxItemIds, orderItemIds,
        "After an order is placed, the food box item IDs should match those in the order.");

    // Check that order contents match the food box contents
    for (int itemId : foodBoxItemIds) {
      int foodBoxItemQuantity =
          registeredShieldingIndividual.getItemQuantityForFoodBox(itemId,
              validFoodBoxId);

      int orderItemQuantity =
          registeredShieldingIndividual.getItemQuantityForOrder(itemId,
              currentOrderNumber);

      assertEquals(foodBoxItemQuantity, orderItemQuantity, "After " +
          "the order was placed, the order item quantities should match the corresponding " +
          "food box item quantities.");

      String foodBoxItemName = registeredShieldingIndividual.getItemNameForFoodBox(itemId,
          validFoodBoxId);

      String orderItemName = registeredShieldingIndividual.getItemNameForOrder(itemId,
          currentOrderNumber);
      assertEquals(foodBoxItemName, orderItemName,
          "After the order was placed, the order item names should match the " +
              "corresponding food box item names.");
    }

    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should remain registered after placing an order.");
  }

  /**
   * Tests an extension of the Place Order use case. The shielding individual be able to edit the
   * selected food box by reducing or maintaining item quantities. After placing and order, the
   * names and quantities of items in the order should match those of the edited food box.
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @param configurationString
   *        a food box configuration of the format [&lt;item_id&gt;,&lt;changed_item_quantity&gt;]
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - making " +
               "edits to food box before placing a new order")
  @ParameterizedTest
  @CsvSource(value={  // CSV format: <food_box_id>;[<item_id>,<changed_item_quantity>]
      "1;1,0,2,1,6,0",
      "2;1,1,3,0,7,0",
      "3;3,1,4,1,8,1",
      "4;13,1,11,0,8,1,9,0",
      "5;9,0,11,0,12,1"
  }, delimiter=';')
  public void testPlaceFoodBoxOrderWithEdits(int validFoodBoxId, String configurationString) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using " +
            "a valid food box ID.");

    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
                                                      .map(Integer::valueOf)
                                                      .collect(Collectors.toList());

    // Make changes to the picked food box
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the picked food box item
      assertTrue(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(itemId,
          quantity), "A registered shielding individual should be able to " +
          "reduce or keep the same the quantity of an item in the picked food box.");

      // Try to make an invalid change to the picked food box item
      int invalidQuantity = quantity + 10_000_000;
      assertFalse(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(itemId,
          invalidQuantity), "A registered shielding individual should not be " +
          "able to increase the quantity of an item in the picked food box.");

      int invalidQuantity2 = quantity - 10_000_000;
      assertFalse(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(itemId,
          invalidQuantity2), "A registered shielding individual should not be " +
          "able to decrease the quantity of an item in the picked food box below 0.");
    }

    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A registered shielding individual should be able to place an order after " +
            "making changes to the food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Check that order contents reflect the edits that have been made
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int foodBoxItemQuantity = validFoodBoxConfiguration.get(i + 1);
      int orderItemQuantity = registeredShieldingIndividual.getItemQuantityForOrder(itemId,
          currentOrderNumber);
      assertEquals(foodBoxItemQuantity, orderItemQuantity,
          "After the order was placed, the order item quantities should match the " +
              "edits made to them prior to the order.");
      String foodBoxItemName = registeredShieldingIndividual.getItemNameForFoodBox(itemId,
          validFoodBoxId);

      String orderItemName = registeredShieldingIndividual.getItemNameForOrder(itemId,
          currentOrderNumber);
      assertEquals(foodBoxItemName, orderItemName, "After the order was " +
          "placed, the order item names should match the food box item names.");
    }

    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should remain registered after placing an order with edits.");
  }

  /**
   * Tests an extension of the Place Order use case. The shielding individual not be able to edit
   * the selected food box by reducing all item quantities to zero. After placing an order,
   * quantities of items in the order should not match those of the edited food box if
   * the shielding individual intends to zero out all item quantities.
   * However, the item names should still match.
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @param configurationString
   *        a food box configuration of the format [&lt;item_id&gt;,&lt;changed_item_quantity&gt;]
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: " +
      "Extension - Shielding Individual cannot zero out all items before placing an order")
  @ParameterizedTest
  @CsvSource(value={  // CSV format: <food_box_id>;[<item_id>,<changed_item_quantity>]
      "1;1,0,2,0,6,0",
      "2;1,0,3,0,7,0",
      "3;3,0,4,0,8,0",
      "4;13,0,11,0,8,0,9,0",
      "5;9,0,11,0,12,0"
  }, delimiter=';')
  public void testPlaceFoodBoxOrderZeroOut(int validFoodBoxId, String configurationString) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using " +
            "a valid food box ID.");

    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
        .map(Integer::valueOf)
        .collect(Collectors.toList());

    // Make changes to the picked food box
    int i;
    for (i = 0; i < validFoodBoxConfiguration.size() - 2; i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the picked food box item
      assertTrue(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(itemId,
          quantity), "A registered shielding individual should be able " +
          "to reduce or keep the same the quantity of an item in the picked food box.");
    }
    // The changes to the last item should not be made
    int lastItemId = validFoodBoxConfiguration.get(i);
    int lastQuantity = validFoodBoxConfiguration.get(i + 1);
    assertFalse(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(lastItemId,
        lastQuantity), "A registered shielding individual should not " +
        "be able to reduce all item quantities to zero in the picked food box.");

    // Order should still be placed
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A registered shielding individual should be able to place an order after " +
            "making changes to the food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Check that order contents reflect the edits that have been made
    for (i = 0; i < validFoodBoxConfiguration.size() - 2; i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int foodBoxItemQuantity = validFoodBoxConfiguration.get(i + 1);
      int orderItemQuantity = registeredShieldingIndividual.getItemQuantityForOrder(itemId,
          currentOrderNumber);
      assertEquals(foodBoxItemQuantity, orderItemQuantity, "After the " +
          "order was placed, the order item quantities should match the edits made to " +
          "them prior to the order.");
    }
    // The changes to last item should not be made
    lastItemId = validFoodBoxConfiguration.get(i);
    lastQuantity = validFoodBoxConfiguration.get(i + 1);
    int orderItemQuantity = registeredShieldingIndividual.getItemQuantityForOrder(lastItemId,
        lastQuantity);

    assertNotEquals(lastItemId, orderItemQuantity,
        "After the order was placed, the order item quantities should match the edits " +
            "made to them prior to the order.");

    // Item name should still stay intact for last item change
    String foodBoxItemName = registeredShieldingIndividual.getItemNameForFoodBox(lastItemId,
        validFoodBoxId);

    String orderItemName = registeredShieldingIndividual.getItemNameForOrder(lastItemId,
        currentOrderNumber);
    assertEquals(foodBoxItemName, orderItemName, "After the order was " +
        "placed, the order item names should match the food box item names.");

    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should remain registered after placing an order with edits.");
  }

  /**
   * Tests an extension of the Place Order use case. The shielding individual be able to edit the
   * selected food box locally as long as the quantity is less than original item quantity in
   * food box. After placing an order, quantities of items in the order should not match
   * those of the last valid item quantity in the food box
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @param originalQuantity an example of original quantity of particular item in food box
   * @param configurationString a food box configuration of the format
   *                            [&lt;item_id&gt;,&lt;changed_item_quantity&gt;]
   * @param lastValidQuantity is the last valid quantity in the edited food box
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - Shielding Individual can " +
      "edit quantity locally as long as it is less than or equal to original item quantity")
  @ParameterizedTest
  @CsvSource(value={
      // CSV format: <food_box_id>;
      // <original_quantity>;[<item_id>,<changed_item_quantity>]; <last valid quantity>
      "1;2;2,0,2,1,2,2;2",
      "2;2;1,0,1,1,1,2;2",
      "3;2;4,1,4,0,4,2;2",
      "4;1;13,0,13,1,13,0,13,2;0",
      "5;1;12,0,12,2,12,1;1"
  }, delimiter=';')
  public void testPlaceFoodBoxOrderChangeQuantityLocally(int validFoodBoxId, int originalQuantity,
                                                         String configurationString,
                                                         int lastValidQuantity) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a valid " +
            "food box ID.");

    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
        .map(Integer::valueOf)
        .collect(Collectors.toList());

    // Make changes to the picked food box
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the picked food box item
      if (quantity <= originalQuantity) {
        assertTrue(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(itemId,
            quantity), "A registered shielding individual should be able to " +
            "reduce or keep the original quantity of an item in the picked food box.");
      } else {
        assertFalse(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(
            itemId, quantity), "A registered shielding individual should " +
            "not be able to increase quantity beyond original quantity in the picked food box.");
      }
    }

    // Order should still be placed
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A registered shielding individual should be able to place an order after making " +
            "changes to the food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Check that order contents reflect the edits that have been made
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int orderItemQuantity =
          registeredShieldingIndividual.getItemQuantityForOrder(itemId, currentOrderNumber);
      assertEquals(lastValidQuantity, orderItemQuantity, "After the " +
          "order was placed, the order item quantities should match the last valid edit made " +
          "prior to placing the order.");
    }

    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should remain registered after placing an order with edits.");
  }

  /**
   * Tests an extension of the Place Order use case.
   * The shielding individual should be able to place an order after a weeks time.
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - place order a week after last order")
  @ParameterizedTest
  @CsvSource({ "1,6", "2,7", "3,8", "4,9", "5,5" })
  public void testPlaceFoodBoxOrderAfterWeek(int validFoodBoxId, long timeSpan) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box " +
            "using a valid food box ID.");

    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after picking food box.");

    int prevOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertFalse(registeredShieldingIndividual.placeOrder(),
        "A shielding individual should not be able to place an order before a weeks time.");
    int newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertEquals(prevOrderCount, newOrderCount,
        "Failed place order should not increase the number of placed orders.");

    // Inject a weeks time
    registeredShieldingIndividual.setMostRecentOrderTimeOrderedBeforeDays(timeSpan);

    // Checking for order counts again
    prevOrderCount = registeredShieldingIndividual.getOrderNumbers().size();

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box " +
            "using a valid food box ID.");

    if (timeSpan >= 7) {
      assertTrue(registeredShieldingIndividual.placeOrder(),
          "A registered shielding individual should be able to place an order " +
              "after picking food box a week later.");

      newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
      assertTrue((newOrderCount - prevOrderCount == 1),
          "Order will be placed after a weeks time.");
    } else {
      assertFalse(registeredShieldingIndividual.placeOrder(),
          "A registered shielding individual should be able to place an order " +
              "after picking food box a week later.");

      newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
      assertEquals(newOrderCount, prevOrderCount,
          "Order will be placed after a weeks time.");

    }
    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should remain registered despite unsuccessfully placing an order.");
  }

  /**
   * Tests an extension of the Place Order use case.
   * The shielding individual should not be able to place an order without having picked a food box.
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - no food box picked")
  @Test
  public void testPlaceFoodBoxOrderNoFoodBoxPicked() {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    int prevOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertFalse(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should not be able to place an order without having picked a food box.");
    int newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();

    assertEquals(prevOrderCount, newOrderCount,
        "Failed place order should not increase the number of placed orders.");

    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding" +
        "individual should remain registered despite unsuccessfully placing an order.");
  }

  /**
   * Tests an extension of the Place Order use case.
   * The use case should fail if the shielding individual successfully placed an order
   * within the past week.
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - placing a new order within a week")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testPlaceFoodBoxOrderWithinWeek(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    registeredShieldingIndividual.pickFoodBox(validFoodBoxId);
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A shielding individual should be able to place an order to start with.");

    int prevOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertFalse(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should not be able to place an order within a week of the previous order.");
    int newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertEquals(prevOrderCount, newOrderCount,
        "Failed place order should not increase the number of placed orders.");
    
    // Test guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should remain registered after placing an order.");
  }

  /**
   * Tests an extension of the Place Order use case. The use case should fail if the shielding
   * individual did not pick a food box using an ID corresponding to a valid and available food
   * box. In addition, even if the user previously picked a valid food box, picking an invalid
   * food box afterwards should overwrite these changes, so that placing an order is not possible.
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @param invalidFoodBoxId an example of an invalid food box ID
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - invalid food box id picked")
  @ParameterizedTest
  @CsvSource({"1,-1", "2,-2031", "3," + Integer.MIN_VALUE, "4,1401124576",
      "5," + Integer.MAX_VALUE})
  public void testPlaceFoodBoxOrderInvalidFoodBoxNumber(int validFoodBoxId, int invalidFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual must be registered before placing an order.");

    assertFalse(registeredShieldingIndividual.pickFoodBox(invalidFoodBoxId),
        "A shielding individual should not be able to pick a food box using an invalid " +
            "food box ID.");
    assertFalse(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should not be able to place an order if they picked an invalid food box.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A shielding individual should be able to pick a food box using a valid food " +
            "box ID.");
    assertFalse(registeredShieldingIndividual.pickFoodBox(invalidFoodBoxId),
        "A shielding individual should not be able to pick a food box using an invalid " +
            "food box ID.");
    assertFalse(registeredShieldingIndividual.placeOrder(),
        "A shielding individual should not be able to place an order if they picked an " +
            "invalid food box after having previously picked a valid food box.");

    // Test guarantees
    int prevOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertFalse(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should not be able to place an order within a week of the previous order.");
    int newOrderCount = registeredShieldingIndividual.getOrderNumbers().size();
    assertEquals(prevOrderCount, newOrderCount, "Failed place order " +
        "should not increase the number of placed orders.");

  }

  /**
   * Tests placing a food box order with an invalid food box number.
   *
   * The shielding individual should not be able to pick an invalid food box. If they do not pick
   * a valid food box, they should not be able to place an order.
   *
   * @param invalidFoodBoxId an example of an invalid food box id
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @DisplayName("Use case Place Food Box Order: Extension - invalid food box ID used to make edits")
  @ParameterizedTest
  @ValueSource(ints = {-1, -3_000, -10_000, Integer.MIN_VALUE, Integer.MAX_VALUE})
  public void testPlaceInvalidFoodBoxOrder(int invalidFoodBoxId) {
    assertFalse(registeredShieldingIndividual.pickFoodBox(invalidFoodBoxId),
        "A shielding individual should not be able to pick a food boxes with an " +
            "invalid number.");
    assertFalse(registeredShieldingIndividual.placeOrder(), "A shielding " +
        "individual should not be able to place an order without having picked a valid food box.");
  }


  // --------------- EDIT FOOD BOX ORDER ---------------


  /**
   * Tests the main success scenario of the Edit Food Box Order use case. A registered shielding
   * individual should be able to change item quantity as long as they do not increase it or
   * reduce it below 0.
   *
   * @param chi valid CHI numbers for shielding individual two
   * @param validFoodBoxId an example of a valid food box ID
   * @param configurationString a food box configuration of the format
   *                            [&lt;item_id&gt;,&lt;changed_item_quantity&gt;]
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: MSS")
  @ParameterizedTest
  @CsvSource(value={  // CSV format: <food_box_id>;[<item_id>,<changed_item_quantity>]
      "0308010106;1;1,0,2,1,6,0",
      "0103153619;2;1,1,3,0,7,0",
      "2605796114;3;3,1,4,1,8,1",
      "0208946800;4;13,1,11,0,8,1,9,0",
      "0312838404;5;9,0,11,0,12,1"
  }, delimiter=';')
  public void testEditFoodBoxOrderMss(String chi, int validFoodBoxId, String configurationString) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must be registered before editing the food box for an order.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after picking a food box using a " +
        "valid food box ID.");

    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
                                                      .map(Integer::valueOf)
                                                      .collect(Collectors.toList());
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");

    assertTrue(
        registeredShieldingIndividual.editOrder(currentOrderNumber),
        "A registered shielding individual should be able to edit an order after " +
            "making valid changes to its food box food box ID.");

    ShieldingIndividualClientImp registeredShieldingIndividual2;
    registeredShieldingIndividual2 =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    registeredShieldingIndividual2.registerShieldingIndividual(chi);

    // Make changes to the picked food box
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the order's food box item
      assertTrue(registeredShieldingIndividual.setItemQuantityForOrder(
          itemId, currentOrderNumber, quantity), "A " +
          "registered shielding individual should be able to reduce or keep the same the " +
          "quantity of an item in the food box corresponding to an order.");

      // Try to make an invalid change to the order's food box item
      int invalidQuantity = quantity + 10_000_000;
      assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
          itemId, currentOrderNumber, invalidQuantity), "A " +
          "registered shielding individual should not be able to increase the quantity of an item" +
          " in the food box corresponding to an order.");

      int invalidQuantity2 = quantity - 10_000_000;
      assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
          itemId, currentOrderNumber, invalidQuantity2), "A " +
          "registered shielding individual should not be able to reduce below 0 the quantity of " +
          "an item in the food box corresponding to an order.");
    }

    assertFalse(registeredShieldingIndividual2.editOrder(currentOrderNumber),
        "Shielding individual two should not be able to edit the order without placing one");

    assertTrue(registeredShieldingIndividual.editOrder(currentOrderNumber),
        "A shielding individual should be able to edit an order after making valid changes to " +
            "its contents.");

    // Make changes to the picked food box
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the order's food box item
      assertEquals(quantity, registeredShieldingIndividual.getItemQuantityForOrder(
          itemId, currentOrderNumber),
          "The item quantity should be updated accordingly after editOrder is called.");
    }

    // Test guarantees and side-effects
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must should remain registered after editing the food box for an order.");
  }

  /**
   * Tests the zeroing out extension of the Edit Food Box Order use case. A registered shielding
   * individual should not be able to zero out all the item quantities in a food box.
   * This is invalid and the edit should not be allowed.
   *
   * @param chi valid CHI numbers for shielding individual two
   * @param validFoodBoxId an example of a valid food box ID
   * @param configurationString a food box configuration of the format
   *                            [&lt;item_id&gt;,&lt;changed_item_quantity&gt;]
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: Extension - Shielding Individual should " +
      "not be able to zero out order")
  @ParameterizedTest
  @CsvSource(value={  // CSV format: <food_box_id>;[<item_id>,<changed_item_quantity>]
      "1704035818;1;1,0,2,0,6,0",
      "2309003603;2;1,0,3,0,7,0",
      "2706134515;3;3,0,4,0,8,0",
      "1012746710;4;13,0,11,0,8,0,9,0",
      "2605059410;5;9,0,11,0,12,0"
  }, delimiter=';')
  public void testEditFoodBoxOrderZeroOutOrder(String chi, int validFoodBoxId,
                                               String configurationString) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must be registered before editing the food box for an order.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after picking a food box using a " +
        "valid food box ID.");

    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
        .map(Integer::valueOf)
        .collect(Collectors.toList());
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    assertTrue(registeredShieldingIndividual.editOrder(currentOrderNumber),
        "A registered shielding individual should be able to edit an order after " +
            "making valid changes to its food box food box ID.");

    ShieldingIndividualClientImp registeredShieldingIndividual2;
    registeredShieldingIndividual2 =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    registeredShieldingIndividual2.registerShieldingIndividual(chi);

    // Since used outside a loop
    int i;
    // Make changes to the picked food box
    for (i = 0; i < validFoodBoxConfiguration.size() - 2; i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the order's food box item
      assertTrue(registeredShieldingIndividual.setItemQuantityForOrder(
          itemId, currentOrderNumber, quantity),
          "A registered shielding individual should be able to reduce or keep the same the" +
              " quantity of an item in the food box corresponding to an order.");
    }
    int lastItem = validFoodBoxConfiguration.get(i);
    int lastQuantity = validFoodBoxConfiguration.get(i + 1);

    assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
        lastItem, currentOrderNumber, lastQuantity),
        "A registered shielding individual should not be able to zero out all " +
            "quantities in the food box corresponding to an order.");

    assertFalse(registeredShieldingIndividual2.editOrder(currentOrderNumber),
        "Shielding individual two should not be able to edit the order without placing one");

    assertTrue(registeredShieldingIndividual.editOrder(currentOrderNumber),
        "A shielding individual should be able to edit an order after making valid " +
            "changes to its contents.");

    // Make changes to the picked food box
    for (i = 0; i < validFoodBoxConfiguration.size() - 2; i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int quantity = validFoodBoxConfiguration.get(i + 1);
      // Make a valid change to the order's food box item
      assertEquals(quantity,
          registeredShieldingIndividual.getItemQuantityForOrder(itemId,
              currentOrderNumber),
          "The item quantity should be updated accordingly after editOrder is called.");
    }
    lastItem = validFoodBoxConfiguration.get(i);
    lastQuantity = validFoodBoxConfiguration.get(i + 1);

    assertNotEquals(lastQuantity,
        registeredShieldingIndividual.getItemQuantityForOrder(lastItem,
            currentOrderNumber),
        "The item quantity should be not updated accordingly after editOrder is called " +
            "because of zeroing out.");

    // Test guarantees and side-effects
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must should remain registered after editing the food box for an order.");
  }

  /**
   * Tests an extension of the Edit Food Box Order use case A registered shielding individual
   * should not be able to edit the quantities of items in an order after the
   * order's status is later than "placed".
   *
   * @param validFoodBoxId a valid food box ID
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: Extension - the order status is later than " +
               "\"placed\"")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testEditFoodBoxOrderAfterPlaced(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must be registered before editing the food box for an order.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using" +
            " a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after picking a food box using " +
        "a valid food box ID.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    for (String status : new String[]{"packed", "dispatched", "delivered"}) {
      assertTrue(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, status), "A registered catering company " +
          "should be able to set the status for a placed order.");

      assertFalse(registeredShieldingIndividual.editOrder(currentOrderNumber),
          "A registered shielding individual should not be able to edit an order after " +
              "its status is later than \"placed\"");
    }

    // Test guarantees and side-effects
    assertFalse(unregisteredShieldingIndividual.isRegistered(),
        "An unregistered shielding individual should remain registered even if they " +
            "fail to edit an order.");
  }

  /**
   * Tests an extension of the Edit Food Box Order use case. A registered shielding individual
   * should not be able to edit the quantities of items in an order after the order has been
   * cancelled.
   *
   * @param validFoodBoxId a valid food box ID
   * @param itemId a valid food box item id
   * @param quantity a valid food box item quantity
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: Extension - the order has been cancelled")
  @ParameterizedTest
  @CsvSource({"1,1,0", "2,3,1", "3,8,0", "4,13,1", "5,12,0"})
  public void testEditFoodBoxOrderAfterCancelled(int validFoodBoxId, int itemId, int quantity) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must be registered before editing the food box for an order.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a valid " +
            "food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after picking a food box " +
        "using a valid food box ID.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    assertTrue(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "A registered shielding individual should be able to cancel an order " +
            "right after it was placed.");

    assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
        itemId, validFoodBoxId, quantity), "A registered " +
        "shielding individual cannot reduce quantity after order has been cancelled.");

    assertFalse(registeredShieldingIndividual.editOrder(currentOrderNumber),
        "A registered shielding individual should not be able to edit the quantities of " +
            "items in an order after the order has been cancelled.");

    // Test guarantees and side-effects
    assertFalse(unregisteredShieldingIndividual.isRegistered(),
        "An unregistered shielding individual should remain registered even if they " +
            "fail to edit an order.");
  }

  /**
   * Tests an extension of the Edit Food Box Order use case.
   * An unregistered shielding individual should not be able to edit the quantities of items in
   * an order.
   *
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: Extension - the shielding individual is " +
               "not registered")
  @Test
  public void testEditFoodBoxOrderUnregistered() {
    assertFalse(unregisteredShieldingIndividual.editOrder(1), "An " +
        "unregistered shielding individual should not be able to edit the food box for an order.");
    // Test guarantees and side-effects
    assertFalse(unregisteredShieldingIndividual.isRegistered(),
        "An unregistered shielding individual should remain registered even if they fail " +
                "to edit an order.");
  }

  /**
   * Tests an extension of the Edit Food Box Order use case. A registered shielding individual
   * should be able to edit an order that is not the most recent one, as long as its
   * status permits this.
   *
   * @see ShieldingIndividualClientImp#editOrder
   */
  @DisplayName("Use case Edit Food Box Order: Extension - editing a previous order")
  @Test
  public void testEditFoodBoxOrderPastOrder() {
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must be registered before editing the food box for an order.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(1),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A registered shielding individual should be able to place an order after " +
            "picking a food box using a valid food box ID.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int oldOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Fast forward
    registeredShieldingIndividual.setMostRecentOrderTimeOrderedBeforeDays(8);
    assertTrue(registeredShieldingIndividual.pickFoodBox(5),
        "A registered shielding individual should be able to pick a food " +
            "box using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "A registered shielding individual should be able to place an order after a week.");

    registeredShieldingIndividual.editOrder(oldOrderNumber);

    int itemId = 2;
    int quantity = 1;
    assertTrue(registeredShieldingIndividual.setItemQuantityForOrder(
        itemId, oldOrderNumber, quantity), "A registered " +
        "shielding individual should be able to reduce or keep the same the quantity of an " +
        "item in the food box corresponding to an order.");

    int invalidQuantity = quantity + 10_000_000;
    assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
        itemId, oldOrderNumber, invalidQuantity), "A " +
        "registered shielding individual should not be able to increase the quantity of an item " +
        "in the food box corresponding to an order.");
    int invalidQuantity2 = quantity - 10_000_000;
    assertFalse(registeredShieldingIndividual.setItemQuantityForOrder(
        itemId, oldOrderNumber, invalidQuantity2),
        "A registered shielding individual should not be able to reduce below 0 " +
            "the quantity of an item in the food box corresponding to an order.");

    assertTrue(registeredShieldingIndividual.editOrder(oldOrderNumber),
        "A shielding individual should be able to edit an order after making valid " +
            "changes to its contents.");
    assertEquals(quantity, registeredShieldingIndividual.getItemQuantityForOrder(
        itemId, oldOrderNumber),
        "The item quantity should be updated accordingly after editOrder is called.");

    // Test guarantees and side-effects
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual must should remain registered after editing the food box for an order.");
  }


  // --------------- CANCEL FOOD BOX ORDER ---------------

  /**
   * Tests the main success scenario of the cancel food box order use case. A registered
   * shielding individual who satisfies all the preconditions should be able to successfully
   * cancel order
   *
   * @param foodBoxId a valid food box id
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @DisplayName("Use case Cancel Food Box Order: MSS")
  @ParameterizedTest
  @ValueSource(ints = {1,2,3,4,5})
  public void testCancelFoodBoxOrder(int foodBoxId) {

    assertTrue(registeredShieldingIndividual.isRegistered(), "Shielding " +
        "individual should be registered in order to cancel the food box");

    assertTrue(registeredShieldingIndividual.pickFoodBox(foodBoxId),
        "Registered individual should be able to pick a food box");

    assertTrue(registeredShieldingIndividual.placeOrder(), "Registered " +
        "shielding individual should be able to place an order after picking box");

    // Acquiring the current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    assertTrue(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "Registered shielding individual should be able to cancel the order");

    String status = registeredShieldingIndividual.getStatusForOrder(currentOrderNumber);
    assertEquals(OrderStatus.CANCELLED.toString(), status,
        "Order should be cancelled after requesting cancel order");

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should stay registered after successful cancellation");
  }

  /**
   * Tests the extension of cancel order which makes sure that the order
   * cannot be cancelled once dispatched.
   *
   * @param foodBoxId a valid food box id
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @DisplayName("Use case cancel food box order: Extension - Order dispatched extension")
  @ParameterizedTest
  @ValueSource(ints = {1,2,3,4,5})
  public void testCancelFoodBoxOrderAlreadyDispatched(int foodBoxId) {

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to cancel the food box");

    assertTrue(registeredShieldingIndividual.pickFoodBox(foodBoxId),
        "Registered individual should be able to pick a food box");

    assertTrue(registeredShieldingIndividual.placeOrder(), "Registered " +
        "shielding individual should be able to place an order after picking box");

    // Acquiring the current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    // Dispatched order cannot be cancelled
    assertTrue(registeredCateringCompany.updateOrderStatus(
        currentOrderNumber, "dispatched"),
        "Registered catering company should be able to update the current order status");

    assertFalse(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "Registered shielding individual should not be able to cancel an order");

    String status = registeredShieldingIndividual.getStatusForOrder(currentOrderNumber);
    assertNotEquals(OrderStatus.CANCELLED.toString(), status,
        "Order should not be cancelled after requesting cancel order");

    // Delivered order cannot be cancelled
    assertTrue(registeredCateringCompany.updateOrderStatus(
        currentOrderNumber, "delivered"),
        "Registered catering company should be able to update the current order status");

    assertFalse(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "Registered shielding individual should not be able to cancel an order");

    status = registeredShieldingIndividual.getStatusForOrder(currentOrderNumber);
    assertNotEquals(OrderStatus.CANCELLED.toString(), status,
        "Order should not be cancelled after requesting cancel order");

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should stay registered after test");
  }

  // NOTE: Cannot test unregistered case, because we cannot place an order
  // and hence cannot cancel the order either

  /**
   * Tests the extension of cancel order which makes sure that the order
   * cannot be cancelled by another shielding individual.
   *
   * @param chi CHI of another registered shielding individual
   * @param foodBoxId a valid food box id
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @DisplayName("Use case cancel food box order: Extension - Order number belongs " +
               "to shielding individual")
  @ParameterizedTest
  @CsvSource({"1811995703,1","0112131507,2","1401101111,3","0604804818,4","2511059118,5"})
  public void testCancelFoodBoxOrderNotPlaced(String chi, int foodBoxId) {
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to cancel the food box");

    assertTrue(registeredShieldingIndividual.pickFoodBox(foodBoxId),
        "Registered individual should be able to pick a food box");

    assertTrue(registeredShieldingIndividual.placeOrder(),
        "Registered shielding individual should be able to place an order after picking box");

    // Acquiring the current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
                  "individual should not be null.");

    assertFalse(unregisteredShieldingIndividual.cancelOrder(currentOrderNumber),
        "Unregistered shielding individual should not be able to cancel order");

    ShieldingIndividualClientImp anotherShieldingIndividual;
    anotherShieldingIndividual =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    anotherShieldingIndividual.registerShieldingIndividual(chi);

    assertFalse(anotherShieldingIndividual.cancelOrder(currentOrderNumber),
        "Shielding individual two should not be able to cancel the order " +
            "without placing one");

    assertTrue(registeredShieldingIndividual.isRegistered(), "Shielding " +
        "individual should stay registered after test");
  }

  /**
   * Tests the extension of cancel order which makes sure that a previous order
   * can be cancelled by the shielding individual if its status allows this.
   *
   * @param foodBoxId a valid food box id
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @DisplayName("Use case cancel food box order: Extension - Cancelling a previous order")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testCancelFoodBoxOrderPreviousOrder(int foodBoxId) {

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to cancel the food box");

    assertTrue(registeredShieldingIndividual.pickFoodBox(foodBoxId),
        "Registered individual should be able to pick a food box");

    assertTrue(registeredShieldingIndividual.placeOrder(), "Registered " +
        "shielding individual should be able to place an order after picking box");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    int oldOrderNumber = orderNumbers.get(orderNumbers.size() - 1);
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");

    // Place another order
    registeredShieldingIndividual.setMostRecentOrderTimeOrderedBeforeDays(8);
    assertTrue(registeredShieldingIndividual.pickFoodBox(foodBoxId),
        "Registered individual should be able to pick a food box");

    assertTrue(registeredShieldingIndividual.placeOrder(), "Registered " +
        "shielding individual should be able to place an order after picking box");


    assertTrue(registeredShieldingIndividual.cancelOrder(oldOrderNumber),
        "Registered shielding individual should be able to cancel an old order, " +
            "if its status allows this");

    String status = registeredShieldingIndividual.getStatusForOrder(oldOrderNumber);
    assertEquals(OrderStatus.CANCELLED.toString(), status, "The previous " +
        "order should appear as cancelled after it was cancelled successfully");

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should stay registered after successful cancellation");
  }

  // --------------- REQUEST ORDER STATUS ---------------

  /**
   * Tests the main success scenario of the Request Order Status use case. A registered shielding
   * individual should be able to request order status for a successfully placed order.
   * {@link ShieldingIndividualClientImp#getStatusForOrder} should reflect the value last fetched by
   * {@link ShieldingIndividualClientImp#requestOrderStatus}
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @DisplayName("Use case Request Order Status: MSS")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testRequestOrderStatusMss(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to request order status.");

    // Place an order
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "Shielding individual should be able to pick a food box using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "Shielding individual should be to place an order after picking a valid food box.");

    // Get current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Check initial order status
    assertTrue(registeredShieldingIndividual.requestOrderStatus(currentOrderNumber),
        "A registered shielding individual should be able to access the status of an order that " +
            "has been placed");
    assertEquals("placed", registeredShieldingIndividual.getStatusForOrder(
        currentOrderNumber), "Initially, the status of a newly-placed " +
        "order should be \"placed\".");

    // Simulate changes to order status
    for (String newStatus : new String[]{ "packed", "dispatched", "delivered" }) {
      assertTrue(registeredCateringCompany.updateOrderStatus(currentOrderNumber,
          newStatus), "A registered catering company should be able to update " +
          "the status for a placed order.");
      assertNotEquals(newStatus,
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "Initially, the status of a newly-placed order should be \"placed\".");
      assertTrue(
          registeredShieldingIndividual.requestOrderStatus(currentOrderNumber),
          "A registered shielding individual should be able to access the status of a " +
              "placed order.");
      assertEquals(newStatus,
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "After requesting order status, order status should match the update made " +
              "by the catering company.");
    }

    // Check guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should remain registered after successfully requesting order status.");
  }

  /**
   * Tests an extension of the Request Order Status use case.
   * A registered shielding individual should be able to request order status for a cancelled order.
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @DisplayName("Use case Request Order Status: Extension - order cancelled.")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testRequestOrderStatusCancelled(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to request order status.");

    // Place an order
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be to place an order after picking a valid food box.");

    // Get current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Cancel order
    assertTrue(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "A registered shielding individual should be able to cancel an order " +
            "right after placing it.");

    // Check initial order status
    assertEquals("cancelled",
        registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
        "After successful order cancellation, the shielding individual should not need " +
            "to request order status for the status to get updated.");
    assertTrue(registeredShieldingIndividual.requestOrderStatus(
        currentOrderNumber), "A registered shielding individual should be " +
        "able to access the status of an order that has been cancelled");

    assertEquals("cancelled",
        registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
        "After successful order cancellation and request of order status for said order," +
            " the status of the order should appear as \"cancelled\".");

    // Check guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "Shielding " +
        "individual should remain registered after successfully requesting order status.");
  }

  /**
   * Tests an extension of the Request Order Status use case. The status of a registered
   * shielding individual's order should only be visible to that individual.
   *
   * @param validChi CHI of another registered shielding individual
   * @param validFoodBoxId an example of a valid food box ID
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @DisplayName("Use case Request Order Status: Extension - trying to " +
      "request status for a different individual's order")
  @ParameterizedTest
  @CsvSource({"1811995703,1","0112131507,2","1401101111,3","0604804818,4","2511059118,5"})
  public void testCancelFoodBoxOrderPrivacy(String validChi, int validFoodBoxId) {
    // Place an order
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box " +
            "using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be to place an order after picking a valid food box.");

    // Get current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Try requesting order status for another individual's order
    assertFalse(unregisteredShieldingIndividual.requestOrderStatus(
        currentOrderNumber), "An unregistered shielding individual " +
        "should not be able to request the status for an order placed by another, registered " +
        "shielding shielding individual.");

    ShieldingIndividualClientImp anotherShieldingIndividual;
    anotherShieldingIndividual =
        new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    anotherShieldingIndividual.registerShieldingIndividual(validChi);
    assertFalse(anotherShieldingIndividual.requestOrderStatus(
        currentOrderNumber), "A registered shielding individual should not" +
        "be able to request the status for an order placed by another shielding shielding " +
        "individual.");
  }

  /**
   * Tests an extension of the Request Order Status use case. A registered shielding individual
   * should not be able to request the status of an order using invalid order number.
   *
   * @param invalidOrderNumber an example of an invalid order number
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @DisplayName("Use case Request Order Status: c")
  @ParameterizedTest
  @ValueSource(ints = { -1, -45_178_564, Integer.MIN_VALUE })
  public void testCancelFoodBoxOrderInvalidOrderNumber(int invalidOrderNumber) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to request order status.");

    assertFalse(unregisteredShieldingIndividual.requestOrderStatus(invalidOrderNumber),
        "A registered shielding individual should not be able to request the status " +
            "for an order using an invalid order number.");

    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should remain registered despite requesting the status " +
            "of an invalid order.");
  }

  /**
   * Tests the main success scenario of the Request Order Status use case. A registered shielding
   * individual should be able to request order status for a successfully placed order.
   * {@link ShieldingIndividualClientImp#getStatusForOrder} should reflect the value last fetched by
   * {@link ShieldingIndividualClientImp#requestOrderStatus}
   *
   * @param validFoodBoxId an example of a valid food box ID
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @DisplayName("Use case Request Order Status: Extension - requesting status for a previous order")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testRequestOrderStatusPreviousOrder(int validFoodBoxId) {
    // Check preconditions
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "Shielding individual should be registered in order to request order status.");

    // Place an order
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "Shielding individual should be able to pick a food box using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "Shielding individual should be to place an order after picking a valid food box.");

    // Get current order number
    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");
    int oldOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Place another order
    registeredShieldingIndividual.setMostRecentOrderTimeOrderedBeforeDays(8);
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "Shielding individual should be able to pick a food box using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(),
        "Shielding individual should be to place an order after picking a valid food box.");

    // Check initial order status for previous order
    assertTrue(registeredShieldingIndividual.requestOrderStatus(oldOrderNumber),
        "A registered shielding individual should be able to access the status of an " +
            "order that has been placed");
    assertEquals("placed", registeredShieldingIndividual.getStatusForOrder(
        oldOrderNumber), "Initially, the status of a newly-placed order " +
        "should be \"placed\".");

    // Simulate changes to order status for previous order
    for (String newStatus : new String[]{ "packed", "dispatched", "delivered" }) {
      assertTrue(registeredCateringCompany.updateOrderStatus(
          oldOrderNumber, newStatus), "A registered catering company " +
          "should be able to update the status for a placed order.");
      assertNotEquals(newStatus,
          registeredShieldingIndividual.getStatusForOrder(oldOrderNumber),
          "Initially, the status of a newly-placed order should be \"placed\".");
      assertTrue(registeredShieldingIndividual.requestOrderStatus(
          oldOrderNumber), "A registered shielding individual should be able" +
          " to access the status of a placed order.");
      assertEquals(newStatus, registeredShieldingIndividual.getStatusForOrder(
          oldOrderNumber), "After requesting order status, order status " +
          "should match the update made by the catering company.");
    }

    // Check guarantees
    assertTrue(registeredShieldingIndividual.isRegistered(), "Shielding " +
        "individual should remain registered after successfully requesting order status.");
  }

  // --------------- UPDATE ORDER STATUS ---------------

  /**
   * Tests the main success scenario of the Update Order Status use case. A registered catering
   * company should be able to update order status for a successfully placed order to a latter
   * status. The shielding individual whose order has been updated should be able to access
   * these changes accordingly.
   *
   * @param validFoodBoxId a valid food box ID
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: MSS (catering company)")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testUpdateOrderStatusMss(int validFoodBoxId) {
    assertTrue(registeredCateringCompany.isRegistered(),
        "A catering company should be registered in order to update order status.");

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should appear as registered in the system after registration.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after having picked a food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Simulate the catering company updating status
    OrderStatus[] statuses = { OrderStatus.PACKED, OrderStatus.DISPATCHED, OrderStatus.DELIVERED };
    for (OrderStatus newStatus : statuses) {
      assertTrue(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, newStatus.toString()), "A registered " +
          "catering company should be able to update order status to a latter status.");
      // Check that the changes propagated successfully
      assertTrue(registeredShieldingIndividual.requestOrderStatus(
          currentOrderNumber), "A registered shielding individual should " +
          "be able to request the status of an order they placed.");
      assertEquals(newStatus.toString(),
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "A registered shielding individual should be synced with the latest updates " +
              "made by the catering company after the status for an order is requested.");
    }
    assertTrue(registeredCateringCompany.isRegistered(),
        "A catering company should stay registered after updating order status.");
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order was updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case. An unregistered catering company
   * should not be able to update order status for a successfully placed order.
   *
   * @param validFoodBoxId a valid food box ID
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - catering company not registered")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testUpdateOrderStatusUnregistered(int validFoodBoxId) {
    CateringCompanyClientImp unregisteredCateringCompany;
    unregisteredCateringCompany =
        new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after having picked a food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    // Simulate the catering company updating status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(unregisteredCateringCompany.updateOrderStatus(
          currentOrderNumber, newStatus.toString()), "An unregistered " +
          "catering company should not be able to update order status to any status.");
      // Check that the changes propagated successfully
      assertTrue(registeredShieldingIndividual.requestOrderStatus(
          currentOrderNumber), "A registered shielding individual should " +
          "be able to request the status of an order they placed.");
      assertEquals(OrderStatus.PLACED.toString(),
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "After a failed attempt at updating order status by the catering company, " +
              "the status of the order should not change for the shielding individual.");
    }
    assertFalse(unregisteredCateringCompany.isRegistered(), "An unregistered " +
        "catering company should not become registered after a failed attempt at updating order " +
        "status.");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order was unsuccessfully " +
        "updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case. A registered catering company should
   * not be able to update order status for a nonexistent order.
   *
   * @param invalidOrderNumber an invalid order number
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - catering company registered " +
               "but invalid order status.")
  @ParameterizedTest
  @ValueSource(ints = { -1, -204, 124_012_487, -12_401_492 })
  public void testUpdateOrderStatusInvalidOrderNumber(int invalidOrderNumber) {
    // Simulate attempts at updating order status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(registeredCateringCompany.updateOrderStatus(
          invalidOrderNumber, newStatus.toString()),
          "A registered catering company should not be able to update order status using " +
          "an invalid order number.");
    }
    assertTrue(registeredCateringCompany.isRegistered(), "A registered " +
        "catering company should not become unregistered after attempting to update order status " +
        "using an invalid order number.");
  }

  /**
   * Tests an extension of the Update Order Status use case.
   * A registered catering company should not be able to update order status to its current status.
   *
   * @param validFoodBoxId a valid food box ID
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - trying to set status to current status.")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testUpdateOrderStatusCurrentStatus(int validFoodBoxId) {
    assertTrue(registeredCateringCompany.isRegistered(),
        "A catering company should be registered in order to update order status.");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after having picked a food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    assertFalse(registeredCateringCompany.updateOrderStatus(currentOrderNumber,
        OrderStatus.PLACED.toString()), "A registered catering " +
        "company should not be able to set order status to its current status.");

    // Simulate the catering company updating status
    OrderStatus[] statuses = { OrderStatus.PACKED, OrderStatus.DISPATCHED, OrderStatus.DELIVERED };
    for (OrderStatus newStatus : statuses) {
      assertTrue(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, newStatus.toString()), "A registered " +
          "catering company should be able to update order status to a latter status.");
      assertFalse(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, newStatus.toString()), "A registered " +
          "catering company should not be able to set order status to its current status.");

      // Check that the changes propagated successfully
      assertTrue(registeredShieldingIndividual.requestOrderStatus(currentOrderNumber),
          "A registered shielding individual should be able to request the status " +
              "of an order they placed.");
      assertEquals(newStatus.toString(),
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "A registered shielding individual should be synced with the latest updates " +
              "made by the catering company after the status for an order is requested.");
    }

    assertTrue(registeredCateringCompany.isRegistered(), "A catering company " +
        "should stay registered after updating order status and trying to re-update it.");
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order was updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case.
   * A registered catering company should not be able to update order status to "cancelled".
   *
   * @param validFoodBoxId a valid food box ID
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - trying to set status to current status.")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testUpdateOrderStatusCancelled(int validFoodBoxId) {
    assertTrue(registeredCateringCompany.isRegistered(),
        "A catering company should be registered in order to update order status.");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after having picked a food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");
    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    assertFalse(registeredCateringCompany.updateOrderStatus(currentOrderNumber,
        OrderStatus.CANCELLED.toString()), "A registered catering company should " +
        "not be able to set order status to \"cancelled\" at any stage.");

    // Simulate the catering company updating status
    OrderStatus[] statuses = { OrderStatus.PACKED, OrderStatus.DISPATCHED, OrderStatus.DELIVERED };
    for (OrderStatus newStatus : statuses) {
      assertTrue(registeredCateringCompany.updateOrderStatus(currentOrderNumber,
          newStatus.toString()), "A registered catering company should be able to " +
          "update order status to a latter status.");

      assertFalse(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, OrderStatus.CANCELLED.toString()), "A " +
          "registered catering company should not be able to set order status to \"cancelled\" " +
          "at any stage.");
      // Check that the changes propagated successfully
      assertTrue(registeredShieldingIndividual.requestOrderStatus(
          currentOrderNumber), "A registered shielding individual should " +
          "be able to request the status of an order they placed.");

      assertEquals(newStatus.toString(),
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "A registered shielding individual should be synced with the latest " +
              "updates made by the catering company, but it shouldn't appear as \"cancelled\".");
    }

    assertTrue(registeredCateringCompany.isRegistered(), "A catering company " +
        "should stay registered after updating order status and trying to cancel it.");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order was updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case.
   * A registered catering company should not be able to update order status for a cancelled order.
   *
   * @param validFoodBoxId a valid food box ID
   * @see CateringCompanyClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - shielding individual " +
      "cancelled their order")
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testUpdateOrderStatusAfterCancellation(int validFoodBoxId) {
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");
    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box " +
            "using a valid food box ID.");
    assertTrue(registeredShieldingIndividual.placeOrder(), "A registered " +
        "shielding individual should be able to place an order after having picked a food box.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers,
        "The past orders for a registered shielding individual should not be null.");

    int currentOrderNumber = orderNumbers.get(orderNumbers.size() - 1);

    assertTrue(registeredShieldingIndividual.cancelOrder(currentOrderNumber),
        "A registered shielding individual should be able to cancel their order right " +
            "after placing it.");

    // Simulate the catering company updating status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(registeredCateringCompany.updateOrderStatus(
          currentOrderNumber, newStatus.toString()), "A registered " +
          "catering company should not be able to update order status to any status after " +
          "the order was cancelled by the shielding individual.");

      assertTrue(registeredShieldingIndividual.requestOrderStatus(
          currentOrderNumber), "A registered shielding individual should be " +
          "able to request the status of an order they placed.");

      assertEquals(OrderStatus.CANCELLED.toString(),
          registeredShieldingIndividual.getStatusForOrder(currentOrderNumber),
          "After an order was cancelled by the shielding individual, it should appear " +
              "as such despite attempts by the catering company to update the status");
    }
    assertTrue(registeredCateringCompany.isRegistered(), "A registered " +
        "catering company should stay registered after a failed attempt at updating order status.");
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should stay registered after the status of their " +
            "order was unsuccessfully updated.");
  }

  // --------------- UPDATE ORDER STATUS SUPERMARKET ---------------


  /**
   * Tests the main success scenario of the Update Order Status use case for a supermarket. A
   * registered supermarket should be able to update order status for a successfully recorded
   * order to a latter status. The shielding individual whose order has been updated should be
   * able to access these changes accordingly.
   *
   * @param orderNumber a valid order number
   * @see SupermarketModelImp#updateOrder
   */
  @DisplayName("Use case Update Order Status: MSS")
  @ParameterizedTest
  @ValueSource(ints = { 11, 12, 13, 436, 51 })
  public void testUpdateOrderStatusSupermarketMss(int orderNumber) {
    assertTrue(registeredSupermarket.isRegistered(),
        "A supermarket should be registered in order to update order status.");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");

    assertTrue(registeredSupermarket.recordSupermarketOrder(
        registeredShieldingIndividual.getChi(), orderNumber),
        "A supermarket should be able to record an order.");

    List<Integer> orderNumbers = registeredShieldingIndividual.getOrderNumbers();
    assertNotNull(orderNumbers, "The past orders for a registered shielding " +
        "individual should not be null.");

    // Simulate the supermarket updating status
    OrderStatus[] statuses = { OrderStatus.PACKED, OrderStatus.DISPATCHED, OrderStatus.DELIVERED };
    for (OrderStatus newStatus : statuses) {
      assertTrue(registeredSupermarket.updateOrderStatus(
          orderNumber, newStatus.toString()), "A registered supermarket " +
          "should be able to update order status to a latter status.");
    }
    assertTrue(registeredSupermarket.isRegistered(),
        "A supermarket should stay registered after updating order status.");
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order was updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case. An unregistered supermarket should
   * not be able to update order status for a successfully placed order.
   *
   * @param orderNumber a valid food box ID
   * @see SupermarketClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - catering company not registered")
  @ParameterizedTest
  @ValueSource(ints = { 11, 12, 13, 1254, 254 })
  public void testUpdateOrderStatusSupermarketUnregistered(int orderNumber) {
    SupermarketClientImp unregisteredSupermarket;
    unregisteredSupermarket = new SupermarketClientImp(CLIENT_PROPS.getProperty("endpoint"));

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");
    assertFalse(unregisteredSupermarket.recordSupermarketOrder(
        registeredShieldingIndividual.getChi(), orderNumber), "An " +
        "unregistered supermarket should not be able to record an order.");

    // Simulate the supermarket updating status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(unregisteredSupermarket.updateOrderStatus(
          orderNumber, newStatus.toString()), "An unregistered " +
          "supermarket should not be able to update order status to any status.");
      }
    assertFalse(unregisteredSupermarket.isRegistered(),
        "An unregistered catering company should not become registered after a failed " +
            "attempt at updating order status.");
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after the status of their order " +
        "was unsuccessfully updated.");
  }

  /**
   * Tests an extension of the Update Order Status use case.
   * A registered supermarket should not be able to update order status for a nonexistent order.
   *
   * @param invalidOrderNumber an invalid order number
   * @see SupermarketClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - catering company registered but " +
      "invalid order status.")
  @ParameterizedTest
  @ValueSource(ints = { -1, -204, 124_012_487, -12_401_492 })
  public void testUpdateOrderStatusSupermarketInvalidOrderNumber(int invalidOrderNumber) {
    // Simulate attempts at updating order status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(registeredSupermarket.updateOrderStatus(
          invalidOrderNumber, newStatus.toString()), "A registered " +
          "supermarket should not be able to update order status using an invalid order number.");
    }
    assertTrue(registeredSupermarket.isRegistered(), "A registered supermarket " +
        "should not become unregistered after attempting to update order status using " +
        "an invalid order number.");
  }

  /**
   * Tests an extension of the Update Order Status use case.
   * A registered supermarket should not be able to update order status without placing an order.
   *
   * @param orderNumber a valid food box ID
   * @see SupermarketClientImp#updateOrderStatus
   */
  @DisplayName("Use case Update Order Status: Extension - catering company not registered")
  @ParameterizedTest
  @ValueSource(ints = { 11, 12, 13, 1254, 254 })
  public void testUpdateOrderStatusSupermarketWithoutRecording(int orderNumber) {
    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should appear as registered in the system after registration.");

    // Simulate the supermarket updating status
    for (OrderStatus newStatus : OrderStatus.values()) {
      assertFalse(registeredSupermarket.updateOrderStatus(
          orderNumber, newStatus.toString()), "A registered " +
          "supermarket should not be able to update order status to any status without recording " +
          "an order.");
    }
    assertTrue(registeredSupermarket.isRegistered(), "A registered catering " +
        "company should remain registered after a failed attempt at updating order status.");
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should stay registered after the status of their order " +
            "was unsuccessfully updated.");
  }


  // --------------- GET CLOSEST CATERING COMPANY ---------------

  /**
   * Tests the main success scenario of the Get Closest Catering Company use case. A registered
   * shielding individual should be able to get the closest catering company. If there are multiple
   * closest catering companies, one of them should be returned.
   *
   * @see ShieldingIndividualClientImp#getClosestCateringCompany
   */
  @DisplayName("Get Closest Catering Company: MSS")
  @Test
  public void testGetClosestCateringCompany() {
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should appear as registered in system after registration");

    List<String> caterers = registeredShieldingIndividual.getCateringCompanies();
    assertNotNull(caterers,
        "There should be some catering companies already registered");

    assertFalse(caterers.contains(null),
        "There should be no null in registered catering companies list");

    String postCode = registeredShieldingIndividual.getPostCode();
    assertNotNull(postCode,
        "A registered shielding individual should not have null post code");

    // Determine closest catering company
    float minDistance = Float.POSITIVE_INFINITY;
    List<String> closestCompanyNames = new ArrayList<>();
    for (String company : caterers) {
      int firstIndex = company.indexOf(',');
      int lastIndex = company.lastIndexOf(',');
      String companyName = company.substring(firstIndex+1, lastIndex);
      String companyPostCode = company.substring(lastIndex+1);

      float distance = registeredShieldingIndividual.getDistance(
          postCode, companyPostCode);

      assertNotEquals(-1, distance, "Distance between valid post " +
          "codes cannot be -1 when requested by a registered individual");

      if (0 <= distance && distance <= minDistance) {
        if (distance == minDistance) {
          closestCompanyNames = new ArrayList<>();
        }
        closestCompanyNames.add(companyName);
      }
    }

    String shieldingCompany = registeredShieldingIndividual.getClosestCateringCompany();

    // Extract name
    int firstIndex = shieldingCompany.indexOf(',');
    int lastIndex = shieldingCompany.lastIndexOf(',');
    String shieldingCompanyName = shieldingCompany.substring(firstIndex+1, lastIndex);

    // Note: do not check for post code, as the mapping from post codes to distances is many-to-one

    assertTrue(closestCompanyNames.contains(shieldingCompanyName), "Closest " +
        "catering companies should match post code without registering any new catering companies");

    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should stay registered after the status of their order " +
            "was unsuccessfully updated.");
  }

  /**
   * Tests an extension of the Get Closest Catering Company use case.
   * An unregistered shielding individual should not be able to get the closest catering company.
   *
   * @see ShieldingIndividualClientImp#getClosestCateringCompany
   */
  @DisplayName("Get Closest Catering Company: Extension - Unregistered Shielding Individual")
  @Test
  public void testGetClosestCateringCompanyUnregistered() {
    assertFalse(unregisteredShieldingIndividual.isRegistered(),
        "An  unregistered shielding individual should not appear as registered in system.");

    String name = "RossiyaCateringCompany";
    String postCode = "EH1_6EQ";

    CateringCompanyClientImp cateringCompanyTest =
        new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Registering catering company
    assertTrue(cateringCompanyTest.registerCateringCompany(name, postCode),
        "Catering Company should be able to register with valid name and post code.");

    List<String> caterers = unregisteredShieldingIndividual.getCateringCompanies();
    assertNull(caterers,
        "Catering Companies should be null with unregistered individual.");

    // Unregistered shielding individual test
    assertNull(unregisteredShieldingIndividual.getClosestCateringCompany(),
        "Unregistered shielding individual should not be able to get closest catering company.");

    assertFalse(unregisteredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay unregistered after unsuccessfully getting catering company.");
  }

  /**
   * Tests an extension of the Get Closest Catering Company use case. A catering company
   * registered at the shielding individual's post code should be at a distance of 0 from the
   * individual, and its name should appear in the list of catering companies after
   * successful registration.
   *
   * @see ShieldingIndividualClientImp#getClosestCateringCompany
   */
  @DisplayName(
      "Get Closest Catering Company: Extension - Catering company at individuals post code")
  @Test
  public void testGetClosestCateringCompanyShieldingPostCode() {
    assertTrue(registeredShieldingIndividual.isRegistered(),
        "A shielding individual should appear as registered in system after registration");

    // Registering a new catering company at the shielding individual's post code
    CateringCompanyClientImp cateringCompanyTest =
        new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));
    String name = "VranovNadToplouCatering";

    String shieldingPostCode = registeredShieldingIndividual.getPostCode();
    assertTrue(cateringCompanyTest.registerCateringCompany(
        name, shieldingPostCode), "Catering Company should be able to " +
        "register with valid name and post code");

    // Make sure new company is in the list of catering companies
    List<String> caterers = registeredShieldingIndividual.getCateringCompanies();

    assertNotNull(caterers, "The list of catering companies cannot be null " +
        "after a catering company was registered");

    boolean catererInList = false;
    for (String caterer : caterers) {
      if (caterer.contains(name) && caterer.contains(shieldingPostCode)) {
        catererInList = true;
        break;
      }
    }
    assertTrue(catererInList,
        "A newly-registered catering company should be present in list of companies");

    String company = registeredShieldingIndividual.getClosestCateringCompany();

    assertNotNull(company,
        "There should be at least one registered catering company in the system");

    String companyPostCode = company.substring(company.lastIndexOf(",")+1);
    assertEquals(shieldingPostCode, companyPostCode,
        "The closest company should be at the shielding individual's post code");
    float distance = registeredShieldingIndividual.getDistance(shieldingPostCode,
                                                               companyPostCode);
    assertEquals(0, distance,
        "Distance between valid post codes at same post code should be zero");

    assertTrue(registeredShieldingIndividual.isRegistered(), "A shielding " +
        "individual should stay registered after getting closest catering company.");
  }


  // --------------- OTHER TESTS ---------------

  /**
   * Tests that after the user picks a food box and changes one of its items' quantities,
   * the cache remains intact.
   *
   */
  @ParameterizedTest
  @CsvSource(value={  // CSV format: <food_box_id>;[<item_id>,<changed_item_quantity>]
      "1;1,0,2,1,6,0",
      "2;1,1,3,0,7,0",
      "3;3,1,4,1,8,1",
      "4;13,1,11,0,8,1,9,0",
      "5;9,0,11,0,12,1"
  }, delimiter=';')
  public void testLinkBetweenFoodBoxCacheAndPickedFoodBox(int validFoodBoxId,
                                                          String configurationString) {
    Pattern pattern = Pattern.compile(",");
    List<Integer> validFoodBoxConfiguration = pattern.splitAsStream(configurationString)
        .map(Integer::valueOf)
        .collect(Collectors.toList());

    assertTrue(registeredShieldingIndividual.pickFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to pick a food box using a " +
            "valid food box ID.");


    // Make changes to the picked food box
    for (int i = 0; i < validFoodBoxConfiguration.size(); i += 2) {
      int itemId = validFoodBoxConfiguration.get(i);
      int newQuantity = validFoodBoxConfiguration.get(i + 1);
      int quantity1 = registeredShieldingIndividual.getItemQuantityForFoodBox(
          itemId, validFoodBoxId);
      assertTrue(registeredShieldingIndividual.changeItemQuantityForPickedFoodBox(
          itemId, newQuantity), "A registered shielding individual should " +
          "be able to change item quantity using valid food box and item IDs.");

      int quantity2 = registeredShieldingIndividual.getItemQuantityForFoodBox(
          itemId, validFoodBoxId);

      assertEquals(quantity1, quantity2, "Any changes to item quantities " +
          "for a picked food box should not affect the food box cache.");
    }
  }
}
