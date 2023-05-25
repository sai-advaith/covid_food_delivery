package shield;

import static java.lang.StrictMath.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for the class ShieldingIndividualClientImp.
 */
public class ShieldingIndividualClientImpTest {
  /** Valid test CHI numbers. */
  private static final String[] TEST_CHIS = new String[] {
      "1612701515","0811867505","0606711110","2211028701","0701764600","2905132600","3007750802",
      "1008056203","0505817103","2702037011","2904701819","2606150702","2805712617","1605834712",
      "0809701503","1201743503","0107998308","2409015312","1410960407","2803814119","2511907405",
      "0703874308","2906041114","2308203406","2201797416","0906097809","1908000600","1310724102",
      "0812805919","1205202801","1508866514","2202184613","2505814200","1905080800","1406189410",
      "1805737408","1904085803","2107899109","0912807814","1402046710","0201218507","3009057508",
      "0801214117","3009032219","1310127001","3007772605","2908153002","1501772013","1411725809",
      "2911785402","0409111011","1701923810","2607114008","1904131809","2712797814","2501973919",
      "0106921105","0811109213","0705003410","0708200304","2912828912","1602763911","1610135908",
      "1909105108","0312944616","0408077611","2603147610","3008946214","1902127202","2308101816",
      "0505004010","2503945909","3105112411","0504781602","2209810415","1807069504","0809024608",
      "0302966119","3010012306","0710713919","1510110017","0306965417","2404156900","1912158217",
      "2002105506","2310749509","1001012507","1810103709","0108785204","1606150608","0108100903",
      "2210008400","1312779307","3110805110","0601772512","2202741209","1307936808","1303107900",
      "1504011810","1210732014","0305070508","1502772713","1306756109","2806109510","0301932918",
      "1803736404","2108130916","1709180101","2803962000","0510774609","2206976113","3011901907",
      "0211090516","0109747608","0110021500","2002719918","2512832005","1503128213","2110942209",
      "1608749018","2304182203","0810198814","2209720818","3009998500","1601192014","1812949417",
      "3010138313","3107055811","2808789007","1909863409","1102154103","3105136413","2012701204",
      "0510198001","1309056910","1206074207","1301885314","1308942513","1103939418","2905053307",
      "0407015519","1108818317","2102705819","0603106510","0411716412","0808131311","2604005813",
      "1301897313","2712955212","2402779700","1205055705","2210056918","0504185800","3006949200",
      "2010978105","0301032901","2309926301","1804129815","2307916415","0603989501","1808020517",
      "2201843018","2607195403","3004104408","0509074510","2909822514","0202147914","2106200406",
      "2101207818","1107053407","0212005702","1806197600","0702191906","1210046612","2605759415",
      "0901886110","1709882714","0304830017","2709098904","2108072918","0805049307","1009096507",
      "2001935312","1512112715","2304179008","0407939904","0612116716","2412798315","0705195310",
      "1704870303","2206028402","0712778509","3010908713","0111977407","2510788114","0103183216",
      "1605751403","2604203612","3010762111","1905024206","1112822400","2211761309","1801187809",
      "1203939100","0407157508","2104887616","2502166814","1311092504","1109809216","1911821908",
      "0901101507","1201022217","0306911200","2010873416","1507861007","0408796713","2203859908",
      "2510829605","2604120407","1903826115","0402994317","1111887119","0212732015","1502066904",
      "1402071318","2912205219","2405989905","2907787610","1007911119","0702739902","2603040700",
      "1610821313","0609137509","0603147019","2710189502","0602767414","2011765016","1907968813",
      "2110117009","2305800217","1102144813","3003835209","1605118705","2603768313","2105968602",
      "2107916113","0402936815","0208861110","2604772108","0307017916","0409897201","1008142211",
      "3103207107","2201972116","1906752602","1011041816","2606071001","0108151614","0410851117",
      "0412786917","1008728604","1204190410","0706779307","2401742503","2909830218","2504153611",
      "3105744113","2508929907","2502049205","1012970101","1406070508","2710823309","1409711204",
      "1209055902","2212196113","2104734509","2809852706","2810098703","0310045000","0803709217",
      "0112784712","0309782708","0706051803","0311869914","2005115617","2002776416","0206990209",
      "3103118414","1410713603","0507765114","1806002610","1707097510","2907004005","3003918506",
      "0802895913","1211903004","2606980404","1809862015","1707041915","1312089507","2011172013",
      "0206198009","0103094611","1012077513","1303934808","1101825714","2312009906","1010050914",
      "0806873011","0701734710","2505820501","1505067209","1504704704","2204885109","1512165715",
      "1502799812","1208974018","2103912816","3112190309","2309193409","2702778005","2508057415",
      "0203996005","2510050713","0512901303","2309119300","0109742810","0509147906","2012141106",
      "1706852905","0101802113","2212979719","1104024616","2012703314","0203924804","0510077208",
      "0912059715","1905102319","0812165110","1309719501","1802884015","2710146410","1006860910",
      "1910021416","1412184718","2302788107","0302863301","2208719613","1110885013","2808146202",
      "1304031214","1108997400","2503066405","1303112817","1702176711","2403063203","3101750602",
      "2604888411","0511895608","0612986802","1711038207","2502097700","0801064419","2304769012",
      "0804922504","1606194607","2105825118","2606750517","0608154117","2308799016","1605042009",
      "2203756205","2703939108","2602097418","2303066100","0109111402","1411150703","1112087016",
      "1308047319","2001018300","1403731109","1609839808","2911083315","0602824515","3110921002",
      "1501134018","2404971519","1604948900","1804961701","2002193719","2405736004","1708069600",
      "0509756612","1108707003","2705837604","1912931909","1406891307","1806796514","0403203711",
      "3012120304","2609020901","2801772615","3107070200","2704863711","0706935204","2410801819",
      "2412851008","2112159811","1808809401","1804978103","2511052302","2201197316","0305067705",
      "0711795118","2207088400","1102155519","1608940319","0407013806","0202764511","2602881212",
      "1408029617","1902189011","2101733200","1601884417","2001718302","1611996006","0711131002",
      "2306862318","3011838215","2207194402","0309023900","2005784414","1905961612","1803130001",
      "1602185103","1803073411","1612055006","0403203708","2506763707","0708157701","2802873411",
      "0112885108","0701761001","2410932007","1802206502","2408161112","1406021500","2411059214",
      "2602712901","0810824200","0402967506","0406008012","2001767712","1606781601","1006988610",
      "1807730914","0204892514","3108029809","0611828119","1107072911","2809105510","2804988418",
      "2802970702","0710853001","0910172403","3006178815","1109174614","2312084906","2711996210",
      "0402755017","2209024811","0908128811","1502864902","0201184719","1712096908","1608909510",
      "2509087904","2112787507","3008061814","2602732516","2809765201","0401821804","3101740415",
      "2809128604","0810774100","1511741108","2302968412","3011977106","0411710204","1008964608",
      "1602702204","0505017410","1703942509","1311892413","3004146105","2806856815","0305892207",
      "2005019712","2412115011","2304208600","2609113112","3101849608","1502140619","1207175518",
      "1107168004","1608208909","2507764016","0507970813","1103792107","0603916419","2110155419",
      "1708012217","2605788900","0409849917","2611119210","1001174502","2506180501","1406122807",
      "2603984702","1603745818","1708027712","1011710718","0703803004","1407744605","0205096007",
      "2810913302","2209007103","2302154810"
  };
  /** Name of the file containing properties related to the client. */
  private static final String CLIENT_PROPS_FILENAME = "client.cfg";
  /** Properties related to the client. */
  private static final Properties CLIENT_PROPS = loadProperties(CLIENT_PROPS_FILENAME);
  /** Invalid order number. */
  private static final int NONEXISTENT_ORDER_NUMBER = -1;
  /** Invalid item quantity. */
  private static final int INVALID_ITEM_QUANTITY = -1;
  /** Test counter. */
  private static int testNumber = 0;
  /** A registered shielding individual client. */
  private ShieldingIndividualClientImp unregisteredClient;
  /** An unregistered shielding individual client. */
  private ShieldingIndividualClientImp registeredClient;

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

  /**
   * Setting up objects before unit testing
   */
  @BeforeAll
  public static void initialSetup() {
    // Makes sure at least one catering company is registered so that all tests can run properly
    CateringCompanyClientImp cateringCompany;
    cateringCompany = new CateringCompanyClientImp(CLIENT_PROPS.getProperty("endpoint"));
    String name = "TestCateringCompany";
    String postCode = "EH12_1FP";
    try {
      String request = String.format(QueryStrings.REGISTER_CATERING_COMPANY.toString(),
          name, postCode);

      String response = ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint")
          + request);

      assertEquals(ServerResponse.REGISTRATION_SUCCESS.toString(), response,
          "Could not register catering company!");
      // Set up state
      cateringCompany.setRegistered(true);
      cateringCompany.setName(name);
      cateringCompany.setPostCode(postCode);
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Something went wrong with the registration";
    }
  }

  /**
   * Setting up required objects before unit testing
   */
  @BeforeEach
  public void setup() {
    unregisteredClient = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    registeredClient = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    assert testNumber < TEST_CHIS.length : "Ran out of CHI numbers for testing!";
    String chi = TEST_CHIS[testNumber];
    try {
      // parameters to be set
      String postCode;
      String name;
      String surname;
      String phoneNumber;

      // HTTP Request to register individual
      String request = String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(),chi);
      String response = ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint")
          + request);

      if (!response.equals(ServerResponse.ALREADY_REGISTERED.toString()) &&
          !response.equals(ServerResponse.NO_CHI.toString())) {

        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> details = new Gson().fromJson(response, type);
        boolean success = Objects.nonNull(details) && details.size() == 4 &&
            !details.contains(null);

        if (success) {
          postCode = details.get(0).replace(' ', '_');
          name = details.get(1);
          surname = details.get(2);
          phoneNumber = details.get(3);

          // Set up state
          registeredClient.setRegistered(true);
          registeredClient.setChi(chi);
          registeredClient.setName(name);
          registeredClient.setPostCode(postCode);
          registeredClient.setSurname(surname);
          registeredClient.setPhoneNumber(phoneNumber);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      assert false : "Shielding individual invalid registration";
    }
    testNumber++;
  }

  // --------------- REGISTER SHIELDING INDIVIDUAL ---------------

  /**
   * Tests new shielding individual registration.
   * A new registration using a valid CHI number should succeed.
   *
   * @param validChi an example of a valid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @ParameterizedTest
  @ValueSource(strings = {"1507852100", "0101001019", "2002062312", "2001726705", "0808026714"})
  public void testValidRegistration(String validChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    assertTrue(client.registerShieldingIndividual(validChi),
        "A shielding individual with a valid CHI number should be able to register.");
  }

  /**
   * Tests repeated shielding individual registration.
   * A repeated registration should succeed, whatever the input.
   *
   * @param validChi an example of a valid CHI number
   * @param invalidChi an example of an invalid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @ParameterizedTest
  @CsvSource({"0512989609,THIS_IS_NOT_A_CHI",
              "0912701702,0012701702",    // Invalid day (right)
              "0404860710,",              // Empty CSV entry is parsed to null
              "2707707312,2700707312",    // Invalid month (right)
              "0906858910,0906858960",    // Invalid gender digit (right)
              "1701795510,14129986110"})  // Invalid length (right)
  public void testRepeatedRegistration(String validChi, String invalidChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));

    // Same method so no need of direct HTTP Request
    // Initial registration
    client.registerShieldingIndividual(validChi);

    // Repeated registration
    assertTrue(client.registerShieldingIndividual(validChi),
        "An attempt at re-registration using valid CHI return true " +
            "for a registered shielding individual");

    assertTrue(client.registerShieldingIndividual(invalidChi),
        "An attempt at re-registration using an invalid CHI return true " +
            "for a registered shielding individual");
  }

  /**
   * Tests invalid shielding individual registration.
   * Registration using an invalid CHI should not be possible.
   *
   * @param invalidChi an example of an invalid CHI number
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: Extension - invalid CHI")
  @ParameterizedTest
  @ValueSource(strings = {"NOT_A_CHI",
                          "1699206312",  // Invalid month
                          "2413007106",  // Invalid month
                          "0007099812",  // Invalid day
                          "3205206202",  // Invalid day
                          "21111193043"}) // Invalid length
  public void testInvalidChiRegistration(String invalidChi) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(CLIENT_PROPS.getProperty("endpoint"));
    assertFalse(client.registerShieldingIndividual(invalidChi),
        "A shielding individual with an invalid CHI number should " +
            "not be able to register.");
  }

  /**
   * Tests invalid shielding individual registration.
   * Registration using an invalid endpoint should not be possible.
   *
   * @param invalidEndpoint an example of an invalid endpoint
   * @see ShieldingIndividualClientImp#registerShieldingIndividual
   */
  @DisplayName("Use case Register Shielding Individual: Extension - invalid CHI")
  @ParameterizedTest
  @ValueSource(strings = {"NOT_AN_ENDPOINT", "http://notahost:5000", "http://localhosttttt"})
  public void testInvalidEndpointRegistration(String invalidEndpoint) {
    ShieldingIndividualClientImp client;
    client = new ShieldingIndividualClientImp(invalidEndpoint);
    String validChi = "2010019018";
    assertFalse(client.registerShieldingIndividual(validChi),
        "A shielding individual with an invalid endpoint should not be able to " +
            "register even if a valid CHI is used.");
  }


  // --------------- PLACE ORDER ---------------

  /**
   * Tests placing a food box order for a registered shielding individual.
   * The shielding individual should not be able to place an order without having picked a food box.
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @Test
  public void testPlaceOrderBeforePickingFoodBox() {
    assertFalse(registeredClient.placeOrder(), "A shielding individual should not be " +
        "able to place an order without having picked a food box.");
  }

  /**
   * Tests placing a food box order for a registered shielding individual.
   * The shielding individual should be able to place an order if they pick a valid food box.
   *
   * @param validFoodBoxId is the food box being chosen by the shielding individual
   *                       before placing an order
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testPlaceOrderAfterPickingFoodBox(int validFoodBoxId) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    assertTrue(registeredClient.placeOrder(), "A shielding individual should be able " +
        "to place an order after picking a valid food box.");
  }

  /** Mocks the method {@link ShieldingIndividualClientImp#pickFoodBox} */
  public static void mockPickFoodBox(ShieldingIndividualClientImp client, int foodBoxId) {
    try {
      String request = CLIENT_PROPS.getProperty("endpoint") + QueryStrings.SHOW_FOOD_BOX.toString();
      String response = ClientIO.doGETRequest(request);
      Type listType = new TypeToken<List<FoodBox>>() {} .getType();
      List<FoodBox> responseBoxes = new Gson().fromJson(response, listType);
      String boxIdString = Integer.toString(foodBoxId);
      for (FoodBox box : responseBoxes) {
        if (Objects.nonNull(box) && boxIdString.equals(box.getId())) {
          client.setPickedFoodBox(box);
          return;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests placing a food box order for an unregistered shielding individual.
   * An unregistered shielding individual should not be able to perform this action.
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @Test
  public void testPlaceOrderUnregistered() {
    assertFalse(unregisteredClient.placeOrder(),
        "An unregistered shielding individual should not be able to place an order");
  }

  /**
   * Tests placing a food box repeatedly.
   * The shielding individual should not be able to place an order less than 168
   * hours from their last order.
   *
   * @param validFoodBoxId is the food box being chosen by the shielding
   *                       individual before placing an order
   *
   * @see ShieldingIndividualClientImp#placeOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testPlaceOrderRepeated(int validFoodBoxId) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);

    // Testing repeated placement
    registeredClient.placeOrder();
    assertFalse(registeredClient.placeOrder(), "A shielding individual should not " +
        "be able to place a new order within a week of their last order.");
  }


  // --------------- GET FOOD BOX NUMBER ---------------

  /**
   * Tests if food box numbers can be obtained by an unregistered shielding individual.
   * An unregistered shielding individual should not be able to perform this action.
   *
   * @see ShieldingIndividualClientImp#getFoodBoxNumber
   */
  @Test
  public void testGetFoodBoxNumberUnregistered() {
    int invalidFoodBoxNumber = -1;
    assertEquals(invalidFoodBoxNumber, unregisteredClient.getFoodBoxNumber(),
            "Unregistered users should not be able to get the number of food boxes");
  }

  /**
   * Tests if food box numbers can be obtained by a registered shielding individual.
   * A registered shielding individual should be able to perform this action and
   * acquire the number of boxes.
   *
   * @see ShieldingIndividualClientImp#getFoodBoxNumber
   */
  @Test
  public void testGetFoodBoxNumberRegistered() {
    int numberOfAvailableFoodBoxes = 5;
    assertNotEquals(-1, registeredClient.getFoodBoxNumber(), "A registered " +
        "shielding individual should be able to get the number of food boxes.");
    assertEquals(numberOfAvailableFoodBoxes, registeredClient.getFoodBoxNumber(),
        "Number of available food boxes should match that on the server.");
  }


  // --------------- CHANGE ITEM QUANTITY FOR PICKED FOOD BOX ---------------

  /**
   * Tests changing item quantity for a food box for an unregistered shielding individual.
   * An unregistered shielding individual should not be able to change the quantity
   * of an item in a picked food box.
   *
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @Test
  public void testChangeItemQuantityForPickedFoodBoxUnregistered() {
    int invalidItemId = 0;
    int invalidItemQuantity = -1;
    assertFalse(unregisteredClient.changeItemQuantityForPickedFoodBox(invalidItemId,
        invalidItemQuantity), "An unregistered shielding individual should " +
        "not be able to change item quantity for a picked food.");

    int validItemId = 1;
    int validItemQuantity = 0;
    assertFalse(unregisteredClient.changeItemQuantityForPickedFoodBox(validItemId,
        validItemQuantity), "An unregistered shielding individual should not be able to " +
        "change item quantity for a picked food even if they use a valid item id and quantity.");
  }

  /**
   * Tests that a registered shielding individual should not be able to change item quantity
   * for a food box item using an invalid item ID.
   *
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -100})
  public void testChangeItemQuantityForPickedFoodBoxInvalidItemId(int invalidItemId) {
    // Using setting instead of directly calling the method
    int validFoodBoxId = 1;
    mockPickFoodBox(registeredClient, validFoodBoxId);

    assertFalse(registeredClient.changeItemQuantityForPickedFoodBox(invalidItemId, 0),
        "A registered shielding individual with invalid item ID " +
            "should not be able to change item quantity for picked food box");

  }

  /**
   * Tests that a registered shielding individual cannot change item quantity for a food
   * box without having picked a food box first.
   *
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @Test
  public void testChangeItemQuantityForPickedFoodBoxNotPicked() {
    assertFalse(registeredClient.changeItemQuantityForPickedFoodBox(1, 1),
        "A registered shielding individual should not be able to set item quantity " +
            "for a food box without picking a food box first.");
  }

  /**
   * Tests that a registered shielding individual should not be able to change
   * item quantity to an invalid value for a food box item.
   *
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -100, -200})
  public void testChangeItemQuantityForPickedFoodBoxInvalidQuantity(int invalidQuantity) {
    assertFalse(registeredClient.changeItemQuantityForPickedFoodBox(0, invalidQuantity),
        "A registered shielding individual should not be able to change " +
            "food box item quantity to an invalid value.");
  }

  // Returns a 2D-array that represents the default food box items
  // available via the Scottish government's API
  // Each row is formatted as: {<food_box_id>, <food_box_item_id>, <food_box_item_quantity>}
  private static int[][] defaultFoodBoxItems() {
    return new int[][] {
        { 1, 1, 1 }, { 1, 2, 2 }, { 1, 6, 1 },
        { 2, 1, 2 }, { 2, 3, 1 }, { 2, 7, 1 },
        { 3, 3, 1 }, { 3, 4, 2 }, { 3, 8, 1 },
        { 4, 13, 1 }, { 4, 11, 1 }, { 4, 8, 1 }, { 4, 9, 1 },
        { 5, 9, 1 }, { 5, 11, 1 }, { 5, 12, 1 }
    };
  }

  /**
   * Tests that the quantity for a food box item cannot be increased beyond the original quantity
   * by a registered shielding individual.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testChangeItemQuantityForPickedFoodBoxIncreaseQuantity(int[] defaultFoodBoxItem) {
    int foodBoxId = defaultFoodBoxItem[0];
    int itemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];

    mockPickFoodBox(registeredClient, foodBoxId);

    int increasedQuantity = itemQuantity + 1;
    assertFalse(registeredClient.changeItemQuantityForPickedFoodBox(itemId, increasedQuantity),
        "A registered shielding individual should not be able to increase item quantity " +
            "for a picked food box.");
  }

  /**
   * Tests that the quantity for a food box item can be decreased then increased
   * back to its original value by a shielding individual.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void
  testChangeItemQuantityForPickedFoodBoxDecreaseIncreaseQuantity(int[] defaultFoodBoxItem) {
    int foodBoxId = defaultFoodBoxItem[0];
    int itemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];

    mockPickFoodBox(registeredClient, foodBoxId);

    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId,
        itemQuantity - 1), "A registered shielding individual should be " +
        "able to decrease item quantity for a picked food box.");

    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, itemQuantity),
        "A registered shielding individual should be able to increase item quantity " +
            "for a picked food box, as long as the resultant quantity is less than or equal to " +
            "the maximum quantity for that item.");

    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, 0),
        "A registered shielding individual should be able to decrease item quantity " +
            "for a picked food box to 0.");

    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, itemQuantity),
        "A registered shielding individual should be able to increase item quantity " +
            "for a picked food box, as long as the resultant quantity is <= the maximum " +
            "quantity for that item.");
  }

  /**
   * Tests that a registered shielding individual should be able to decrease the quantity
   * for a food box item as well as set it to 0.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testChangeItemQuantityForPickedFoodBoxDecreaseQuantity(int[] defaultFoodBoxItem) {
    int foodBoxId = defaultFoodBoxItem[0];
    int itemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];

    mockPickFoodBox(registeredClient, foodBoxId);

    int decreasedQuantity = itemQuantity - 1;
    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, decreasedQuantity),
        "A registered shielding individual should be able to decrease item quantity " +
            "for a picked food box.");
    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, 0),
        "A registered shielding individual should be able to decrease " +
            "item quantity to 0 for a picked food box.");
  }

  /**
   * Tests that a registered shielding individual should be able to update item
   * quantity to its current value.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#changeItemQuantityForPickedFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testChangeItemQuantityForPickedFoodBoxMaintainQuantity(int[] defaultFoodBoxItem) {
    int foodBoxId = defaultFoodBoxItem[0];
    int itemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];

    mockPickFoodBox(registeredClient, foodBoxId);

    assertTrue(registeredClient.changeItemQuantityForPickedFoodBox(itemId, itemQuantity),
        "A registered shielding individual should be able to " +
            "update item quantity to its current value.");
  }

  // --------------- GET DIETARY PREFERENCE FOR FOOD BOX ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get
   * the dietary preference for a food box.
   *
   * @param validFoodBoxId an example of a valid food box id
   * @see ShieldingIndividualClientImp#getDietaryPreferenceForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, 1, 2, 3, 4, 5})
  public void testGetDietaryPreferenceUnregistered(int validFoodBoxId) {
    assertNull(unregisteredClient.getDietaryPreferenceForFoodBox(validFoodBoxId), "An " +
        "unregistered shielding individual should not be able to get dietary preference");

  }

  /**
   * Tests that a registered shielding individual should not be able to get
   * the dietary preference for a food box given an invalid food box ID.
   *
   * @param invalidFoodBoxId an example of an invalid food box id
   * @see ShieldingIndividualClientImp#getDietaryPreferenceForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -3})  // Negative ID is considered invalid
  public void testGetDietaryPreferenceInvalidId(int invalidFoodBoxId) {
    assertNull(registeredClient.getDietaryPreferenceForFoodBox(invalidFoodBoxId), "A " +
        "registered shielding individual should not get dietary preference for using " +
        "an invalid food box ID");

  }

  /**
   * Tests that a registered shielding individual should be able to get the dietary preference
   * for a food box given a valid food box ID.
   *
   * @param validFoodBoxId an example of a valid food box id
   * @see ShieldingIndividualClientImp#getDietaryPreferenceForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"1, none", "2, pollotarian", "3, none", "4, none", "5, vegan"})
  public void testGetDietaryPreferenceValidId(int validFoodBoxId, String dietaryPreference) {
    String diet = registeredClient.getDietaryPreferenceForFoodBox(validFoodBoxId);
    assertEquals(diet, dietaryPreference, "A registered shielding individual " +
        "should be able to get the dietary preference for a food box using a valid food box ID");
  }

  // --------------- GET ITEMS NUMBER FOR FOOD BOX ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get
   * item numbers for valid food box ID
   *
   * @param validFoodBoxId an example of a valid food box id
   * @see ShieldingIndividualClientImp#getItemsNumberForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testGetItemsNumberForFoodBox(int validFoodBoxId) {
    assertEquals(-1, unregisteredClient.getItemsNumberForFoodBox(validFoodBoxId),
        "Unregistered users should not be able to access the number of items " +
            "for a food box");
  }

  /**
   * Tests that a registered shielding individual should not be able to get item
   * numbers for invalid food box ID
   *
   * @param invalidFoodBoxId an example of an invalid food box id
   * @see ShieldingIndividualClientImp#getItemsNumberForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -3})
  public void testGetItemsNumberFodFoodBoxInvalidId(int invalidFoodBoxId) {
    // Registered client should be able to access the functionality only if a valid
    // food box id is provided
    assertEquals(-1,
        registeredClient.getItemsNumberForFoodBox(invalidFoodBoxId), "After " +
            "registration, the individual should not get number of items with invalid ID");

  }

  /**
   * Tests that a registered shielding individual should be able to get item numbers
   * for valid food box ID
   *
   * @param validFoodBoxId an example of a valid food box id
   * @param itemsNumber an example of item numbers
   * @see ShieldingIndividualClientImp#getItemsNumberForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"1, 3", "2, 3", "3, 3", "4, 4", "5, 3"})
  public void testGetItemsNumberFodFoodBoxValidId(int validFoodBoxId, int itemsNumber) {
    // Registered client should be able to access the functionality only if a valid
    // food box id is provided
    assertEquals(itemsNumber, registeredClient.getItemsNumberForFoodBox(validFoodBoxId),
        "After registration, the individual should get number of items with valid ID");

  }

  // --------------- GET ITEMS IDs FOR FOOD BOX ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get item IDs
   * for valid food box ID
   *
   * @param validFoodBoxId an example of a valid food box id
   * @see ShieldingIndividualClientImp#getItemIdsForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testGetItemIdsForFoodBoxUnregistered(int validFoodBoxId) {
    // Unregistered or invalid client should not be able to access get item IDs for a food box
    assertNull(unregisteredClient.getItemIdsForFoodBox(validFoodBoxId),
            "An unregistered shielding individual should not be able to " +
                "access item ID's for a food box.");
  }

  /**
   * Tests that a registered shielding individual should not be able to get item IDs for
   * invalid food box ID
   *
   * @param invalidFoodBoxId an example of an invalid food box id
   * @see ShieldingIndividualClientImp#getItemIdsForFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -3})
  public void testGetItemIdsForFoodBoxInvalidId(int invalidFoodBoxId) {
    // Registered client should not order with invalid ID
    assertNull(registeredClient.getItemIdsForFoodBox(invalidFoodBoxId),
        "Accessing item ID's for a non-existent food box by a registered shielding " +
            "individual should return null");
  }

  /**
   * Tests that a registered shielding individual should be able to get item IDs for valid
   * food box ID
   *
   * @param validFoodBoxId an example of a valid food box id
   * @param expectedItemIdsString an example of valid item IDs
   * @see ShieldingIndividualClientImp#getItemIdsForFoodBox
   */
  @ParameterizedTest
  @CsvSource(value={"1;1,2,6", "2;1,3,7", "3;3,4,8", "4;13,11,8,9", "5;9,11,12"}, delimiter=';')
  public void testGetItemIdsForFoodBoxValidId(int validFoodBoxId,
                                              String expectedItemIdsString) {
    // Get the item IDs for a valid ID

    List<String> expectedStringItemIds;
    expectedStringItemIds = Arrays.asList(expectedItemIdsString.split(","));
    List<Integer> expectedItemIds;
    expectedItemIds =
        expectedStringItemIds.stream().map(Integer::parseInt).collect(Collectors.toList());

    assertEquals(expectedItemIds,
        registeredClient.getItemIdsForFoodBox(validFoodBoxId),
        "A registered shielding individual should be able to access item IDs using a " +
            "valid food box ID.");
  }

  // --------------- GET ITEMS NAME FOR FOOD BOX ---------------

  /**
   * Tests that a shielding individual should not be able to get item name if the item ID and
   * food box ID are valid but the individual is not registered
   *
   * @param validFoodBoxItemId an example of valid food box item ID
   * @param validFoodBoxId an example of valid food box ID
   * @see ShieldingIndividualClientImp#getItemNameForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"1,1", "2,1", "6,1", "1,2", "3,3", "7,2"})
  public void testGetItemNameForFoodBoxUnregistered(int validFoodBoxItemId, int validFoodBoxId) {
    // Unregistered or invalid client should not be able to access this functionality
    assertNull(unregisteredClient.getItemNameForFoodBox(validFoodBoxItemId, validFoodBoxId),
            "Unregistered client should not be able to get item name for a food box");
  }

  /**
   * Tests that a shielding individual should not be able to get item name
   * if the item ID is invalid but food box ID is valid
   *
   * @param invalidFoodBoxItemId an example of invalid food box item ID
   * @param validFoodBoxId an example of valid food box ID
   * @see ShieldingIndividualClientImp#getItemNameForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"-1,1", "-2,2"})
  public void testGetItemNameForFoodBoxInvalidItemId(int invalidFoodBoxItemId,
                                                     int validFoodBoxId) {
    // Registered client should be able to access the functionality only if a valid
    // food box id and item id are provided
    assertNull(registeredClient.getItemNameForFoodBox(invalidFoodBoxItemId, validFoodBoxId),
        "Registered client should not be able to get item name for invalid ID");
  }

  /**
   * Tests that a shielding individual should not be able to get item name if
   * the item ID is valid but food box ID is invalid
   *
   * @param validFoodBoxItemId an example of valid food box item ID
   * @param invalidFoodBoxId an example of invalid food box ID
   * @see ShieldingIndividualClientImp#getItemNameForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"1,-1", "2,-2"})
  public void testGetItemNameForFoodBoxInvalidFoodBoxId(int validFoodBoxItemId,
                                                        int invalidFoodBoxId) {
    // Registered client should be able to access the functionality only if a valid
    // food box id and item id are provided
    assertNull(registeredClient.getItemNameForFoodBox(validFoodBoxItemId, invalidFoodBoxId),
        "Registered client should not be able to get item name for invalid ID");
  }

  /**
   * Tests that a shielding individual should not be able to get item name if the item ID
   * and food box ID are invalid
   *
   * @param invalidFoodBoxItemId an example of invalid food box item ID
   * @param invalidFoodBoxId an example of invalid food box ID
   * @see ShieldingIndividualClientImp#getItemNameForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"-11,-1", "-12,-2"})
  public void testGetItemNameForFoodBoxInvalidIds(int invalidFoodBoxItemId, int invalidFoodBoxId) {
    // Registered client should be able to access the functionality only if a valid
    // food box id and item id are provided
    assertNull(registeredClient.getItemNameForFoodBox(invalidFoodBoxItemId, invalidFoodBoxId),
        "Registered client should not be able to get item name for invalid ID");
  }

  /**
   * Tests that a shielding individual should be able to get item name if the food box
   * and item ID are both valid
   *
   * @param validFoodBoxItemId an example of valid food box item ID
   * @param validFoodBoxId an example of valid food box ID
   * @param name an example of item name in food box
   * @see ShieldingIndividualClientImp#getItemNameForFoodBox
   */
  @ParameterizedTest
  @CsvSource({"1,1,cucumbers",
                      "2,1,tomatoes",
                      "6,1,pork",
                      "1,2,cucumbers",
                      "3,3,onions",
                      "7,2,chicken"})
  public void testGetItemNameForFoodBoxValidIds(int validFoodBoxItemId,
                                                int validFoodBoxId, String name) {
      String nameBox = registeredClient.getItemNameForFoodBox(validFoodBoxItemId, validFoodBoxId);
      assertEquals(nameBox, name, "Item name for food box should match " +
          "the default quantities");
  }


  // --------------- GET ITEMS QUANTITY FOR FOOD BOX ---------------

  /**
   * Tests that an shielding individual should not be able to get item quantity for a
   * food box if they use an invalid
   * food box ID or food box item ID, irrespective of whether they are registered or unregistered.
   *
   *
   * @param itemId an example of an item id
   * @param foodBoxId an example of a food box id
   * @see ShieldingIndividualClientImp#getItemQuantityForFoodBox
   */
  @ParameterizedTest
  @CsvSource({
      "-1,1",  // Invalid item ID
      "1,-1",  // Invalid food box ID
      "-1,-1"  // Both IDs invalid
  })
  public void testGetItemQuantityForFoodBoxInvalidIds(int itemId, int foodBoxId) {
    assertEquals(-1, unregisteredClient.getItemQuantityForFoodBox(itemId, foodBoxId),
        "An unregistered shielding individual should not be able to get item quantity " +
            "for a food box using an valid food box ID and/or an invalid food box item ID.");
    assertEquals(-1, registeredClient.getItemQuantityForFoodBox(itemId, foodBoxId),
        "An registered shielding individual should not be able to get item quantity " +
            "for a food box using an valid food box ID and/or invalid food box item ID.");
  }

  /**
   * Tests that an unregistered shielding individual should not be able to get item quantity
   * for a food box even if using a valid food box ID and a valid food box item ID.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#getItemQuantityForFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testGetItemQuantityForFoodBoxUnregisteredValidIds(int[] defaultFoodBoxItem) {
    int validFoodBoxId = defaultFoodBoxItem[0];
    int validItemId = defaultFoodBoxItem[1];
    assertEquals(-1,
        unregisteredClient.getItemQuantityForFoodBox(validItemId, validFoodBoxId),
        "An unregistered shielding individual " +
            "should not be able to get item quantity for invalid ID");
  }

  /**
   * Tests that a registered shielding individual should be able to get item quantity
   * for a food box using a valid food box ID and a valid food box item ID.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#getItemQuantityForFoodBox
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testGetItemQuantityForFoodBoxRegistered(int[] defaultFoodBoxItem) {
    int validFoodBoxId = defaultFoodBoxItem[0];
    int validItemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];
    assertNotEquals(-1,
        registeredClient.getItemQuantityForFoodBox(validItemId, validFoodBoxId),
        "A registered shielding individual should be able to get item " +
            "quantity for food box item using a valid food box ID and food box item ID");
    assertEquals(itemQuantity,
        registeredClient.getItemQuantityForFoodBox(validItemId, validFoodBoxId),
        "Item quantity for a food box should match the default quantity.");
  }


  // --------------- GET ORDER NUMBERS ---------------

  /**
   * Tests that an unregistered shielding individual should not be able
   * to get the list of orders numbers.
   *
   * @see ShieldingIndividualClientImp#getOrderNumbers
   */
  @Test
  public void testGetOrderNumbersUnregistered() {
    assertNull(unregisteredClient.getOrderNumbers(),
        "An unregistered shielding individual should not be able to " +
            "get the list of orders numbers.");
  }

  /**
   * Tests that a registered shielding individual should be able to get the list of orders numbers.
   *
   * @see ShieldingIndividualClientImp#getOrderNumbers
   */
  @Test
  public void testGetOrderNumbersRegistered() {
    assertNotNull(registeredClient.getOrderNumbers(), "For a registered shielding " +
        "individual, the list of orders should be defined even if no order was placed.");
    assertEquals(0, registeredClient.getOrderNumbers().size(),
        "Initially, the list of order numbers should be empty.");
    int validFoodBoxId = 1;

    mockPickFoodBox(registeredClient, validFoodBoxId);

    mockPlaceOrder(registeredClient);
    assertEquals(1, registeredClient.getOrderNumbers().size(),
        "After successfully placing an order, the number of orders should increase to 1.");
  }

  /** Mocks the method {@link ShieldingIndividualClientImp#getCateringCompanies} */
  public static List<String> mockGetCateringCompanies() {
    List<String> caterers = null;
    try {
      String requestCaterers = QueryStrings.GET_CATERERS.toString();
      String response =
          ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + requestCaterers);

      Type type = new TypeToken<List<String>>(){}.getType();
      caterers = new Gson().fromJson(response, type);
      caterers.removeIf(Objects::isNull);
      caterers.removeIf(String::isEmpty);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return caterers;
  }

  /** Mocks the method {@link ShieldingIndividualClientImp#getClosestCateringCompany} */
  public static String[] mockGetClosestCateringCompany(ShieldingIndividualClientImp client) {
    String[] closestCompany = null;
    try {
      // Get all catering companies
      List<String> caterers = mockGetCateringCompanies();

      // Find closest catering company
      float minDistance = Float.POSITIVE_INFINITY;
      String closestCompanyName = null;
      String closestCompanyPostCode = null;
      for (String company : caterers) {
        int firstIndex = company.indexOf(',');
        int lastIndex = company.lastIndexOf(',');
        String companyName = company.substring(firstIndex+1, lastIndex);
        String companyPostCode = company.substring(lastIndex+1);

        // Get distance
        String fPostCode1 = URLEncoder.encode(client.getPostCode(), StandardCharsets.UTF_8);
        String fPostCode2 = URLEncoder.encode(companyPostCode, StandardCharsets.UTF_8);

        String request =
            String.format(QueryStrings.GET_DISTANCE.toString(), fPostCode1, fPostCode2);

        String response =
            ClientIO.doGETRequest(CLIENT_PROPS.getProperty("endpoint") + request);

        float distance = Float.parseFloat(response);
        if (distance >= 0 && distance < minDistance) {
          minDistance = distance;
          closestCompanyName = companyName;
          closestCompanyPostCode = companyPostCode;
        }
      }
      closestCompany = new String[]{ closestCompanyName, closestCompanyPostCode };
    } catch (Exception e) {
        e.printStackTrace();
    }
    return closestCompany;
  }

  /** Mocks the method {@link ShieldingIndividualClientImp#placeOrder)} */
  public static int mockPlaceOrder(ShieldingIndividualClientImp client) {
    try {
      // Get closest catering company
      String[] closestCompany = mockGetClosestCateringCompany(client);

      // Request order
      String formattedChi = URLEncoder.encode(client.getChi(), StandardCharsets.UTF_8);
      String formattedCompanyName = URLEncoder.encode(closestCompany[0], StandardCharsets.UTF_8);
      String formattedPostCode = URLEncoder.encode(closestCompany[1], StandardCharsets.UTF_8);
      String requestPlaceOrder = String.format(
          QueryStrings.PLACE_ORDER.toString(),
          formattedChi,
          formattedCompanyName,
          formattedPostCode
      );

      String orderData = client.getPickedFoodBox().jsonify();

      String response = ClientIO.doPOSTRequest(CLIENT_PROPS.getProperty("endpoint")
          + requestPlaceOrder, orderData);

      if (!response.equals(ServerResponse.ORDER_PLACE_FAILURE.toString())) {
        int orderNumber = Integer.parseInt(response);
        LocalDateTime timeOrdered = LocalDateTime.now();
        CateringCompanyOrder newOrder;
        newOrder = new CateringCompanyOrder(orderNumber, client.getPickedFoodBox(), timeOrdered);
        client.addOrder(newOrder);
        client.setPickedFoodBox(null);  // Reset picked food boxes
        return orderNumber;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return NONEXISTENT_ORDER_NUMBER;
  }


  // --------------- GET STATUS FOR ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get status for an order.
   *
   * @see ShieldingIndividualClientImp#getStatusForOrder
   */
  @Test
  public void testGetStatusForOrderUnregistered() {
    int arbitraryOrderNumber = 13;
    assertNull(unregisteredClient.getStatusForOrder(arbitraryOrderNumber),
        "An unregistered shielding individual should not be able to get " +
            "the status for an order.");
  }

  /**
   * Tests that a registered shielding individual should be able to get status for an order.
   * Only treats the initial status of an order.
   *
   * @see ShieldingIndividualClientImp#getStatusForOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testGetStatusForOrderRegisteredInvalidOrderNumber(int validFoodBoxId) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertEquals(OrderStatus.PLACED.toString(),
        registeredClient.getStatusForOrder(currentOrderNumber),
        "Initially, the order status should be \"placed\".");
  }

  /**
   * Tests that a registered shielding individual should not be able to
   * get status for an order using an invalid order number.
   *
   * @see ShieldingIndividualClientImp#getStatusForOrder
   */
  @Test
  public void testGetStatusForOrderRegisteredInvalidOrderNumber() {
    int invalidOrderNumber = -1;
    assertNull(registeredClient.getStatusForOrder(invalidOrderNumber),
        "A registered shielding individual with an invalid order number should not " +
            "be able to get the status of an order.");
  }


  // --------------- REQUEST STATUS FOR ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to request
   * the status of an order.
   *
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @Test
  public void testRequestOrderStatusUnregistered() {
    int arbitraryOrderNumber = 7;
    assertFalse(unregisteredClient.requestOrderStatus(arbitraryOrderNumber), "An " +
        "unregistered shielding individual should not be able to request the status of an order.");
  }

  /**
   * Tests that a registered shielding individual should not be able to request
   * the status of an order using an invalid order number.
   *
   * @param invalidOrderNumber an example of an invalid order number
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @ParameterizedTest
  @ValueSource(ints = { -1, -2, Integer.MIN_VALUE })
  public void testRequestOrderStatusInvalidOrderNumber(int invalidOrderNumber) {
    assertFalse(registeredClient.requestOrderStatus(invalidOrderNumber), "A registered " +
        "shielding individual should not be able to request the status of an order " +
        "that has not been placed.");
  }

  /**
   * Tests that a registered shielding individual should be able to request
   * the status of an order using a valid order number.
   *
   * @param validFoodBoxId an example of an invalid order number
   * @see ShieldingIndividualClientImp#requestOrderStatus
   */
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testRequestOrderStatus(int validFoodBoxId) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertTrue(registeredClient.requestOrderStatus(currentOrderNumber),
            "A registered shielding individual should be able to access the status of an order " +
                "that has been placed");
  }


  // --------------- GET ITEM QUANTITY FOR ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able get item quantity
   * for an order.
   *
   * @see ShieldingIndividualClientImp#getItemQuantityForOrder
   */
  @Test
  public void testGetItemQuantityForOrderUnregistered() {
    int arbitraryOrderNum = 19;
    int arbitraryItemId = 1;
    assertEquals(INVALID_ITEM_QUANTITY,
        unregisteredClient.getItemQuantityForOrder(arbitraryItemId, arbitraryOrderNum),
        "An unregistered shielding individual should not be able to get " +
            "item quantity for an order.");
  }

  /**
   * Tests that a registered shielding individual should not be able get item quantity
   * for an order using an invalid order number and an invalid item ID.
   *
   * @param invalidItemId an example of an invalid item id
   * @param invalidOrderNumber an example of an invalid order number
   * @see ShieldingIndividualClientImp#getItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({ "-1,-2", "-100,-213", "-13,-11231" })
  public void testGetItemQuantityForOrderRegisteredInvalidIds(int invalidItemId,
                                                              int invalidOrderNumber) {
    assertEquals(INVALID_ITEM_QUANTITY,
        registeredClient.getItemQuantityForOrder(invalidItemId, invalidOrderNumber),
            "A registered shielding individual should not be able to get item quantity.");
  }

  /**
   * Tests that a registered shielding individual should be able get item quantity
   * for an order using a valid order number and a valid item ID.
   *
   * @param invalidItemId an example of an invalid food box item ID
   * @see ShieldingIndividualClientImp#getItemQuantityForOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { -1, -23, Integer.MIN_VALUE })
  public void testGetItemQuantityForOrderRegistered(int invalidItemId) {
    int validFoodBoxId = 1;
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertEquals(-1, registeredClient.getItemQuantityForOrder(invalidItemId, currentOrderNumber),
            "A registered shielding individual with a valid order number " +
                "but an invalid item ID should not be able to get item quantity for order.");
  }

  /**
   * Tests that a registered shielding individual should not be able get item quantity
   * for an order using a valid order number and an invalid item ID.
   *
   * @param defaultFoodBoxItem an example of a default food box item from the API
   * @see ShieldingIndividualClientImp#getItemQuantityForOrder
   */
  @ParameterizedTest
  @MethodSource("defaultFoodBoxItems")
  public void testGetItemQuantityForOrderRegisteredInvalidItemId(int[] defaultFoodBoxItem) {
    int foodBoxId = defaultFoodBoxItem[0];
    int itemId = defaultFoodBoxItem[1];
    int itemQuantity = defaultFoodBoxItem[2];
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, foodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertEquals(itemQuantity, registeredClient.getItemQuantityForOrder(itemId, currentOrderNumber),
        "A registered shielding individual with a valid order number and a valid " +
            "item ID should be able to get item quantity for order.");
  }



  // --------------- EDIT ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to edit an order
   * with valid order numbers.
   *
   * @param orderNumber a valid order number
   * @see ShieldingIndividualClientImp#editOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { 1, 3, 4 })
  public void testEditOrderUnregistered(int orderNumber) {
    // Unregistered or invalid client should not be able to access this functionality
    assertFalse(unregisteredClient.editOrder(orderNumber),
        "An unregistered shielding individual should not be able to edit an order.");
  }

  /**
   * Tests that a registered shielding individual should not be able to edit an order
   * with invalid order numbers.
   *
   * @param invalidOrderNumber an invalid order number
   * @see ShieldingIndividualClientImp#editOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { -1, -23, Integer.MIN_VALUE })
  public void testEditOrderInvalidOrder(int invalidOrderNumber) {
    // Unregistered or invalid client should not be able to access this functionality
    assertFalse(registeredClient.editOrder(invalidOrderNumber), "A registered " +
        "shielding individual should not be able to edit an order with Invalid Order.");
  }

  /**
   * Tests that a registered shielding individual should not be able to edit an order
   * with invalid food box numbers.
   *
   * @param invalidFoodBox a valid food box number
   * @see ShieldingIndividualClientImp#editOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, -2, -3, -34, -5})
  public void testEditOrderInvalidBox(int invalidFoodBox) {
    // Registered client with invalid food box should not be able to place order
    // and hence no edit order
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, invalidFoodBox);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertFalse(registeredClient.editOrder(currentOrderNumber),
        "A registered shielding individual without picking box should " +
            "not be able to edit order.");
  }

  /**
   * Tests that a registered shielding individual should be able to edit an order
   * with valid food box numbers.
   *
   * @param validFoodBoxId a valid food box ID
   * @see ShieldingIndividualClientImp#editOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testEditOrderValidOrder(int validFoodBoxId) {
    // Registered client should be able to access the functionality
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertTrue(registeredClient.editOrder(currentOrderNumber),
        "A registered shielding individual should be able to edit an order.");
  }


  // --------------- GET ITEM NAME FOR ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get
   * item name for order with valid ID's.
   *
   * @param validItemId a valid item ID
   * @param validOrderNumber a valid order number
   * @see ShieldingIndividualClientImp#getItemNameForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,1", "2,1", "6,1", "1,2", "3,3", "7,2"})
  public void testGetItemNameForOrderUnregistered(int validItemId, int validOrderNumber) {

    // Test for unregistered shielding individual
    assertNull(unregisteredClient.getItemNameForOrder(validItemId, validOrderNumber),
            "An unregistered shielding individual should not be able to " +
                "get item name for order");
  }

  /**
   * Tests that a registered shielding individual should not be able to get
   * item name with invalid item ID.
   *
   * @param invalidItemId an invalid item ID
   * @param validOrderNumber a valid order number
   * @see ShieldingIndividualClientImp#getItemNameForOrder
   */
  @ParameterizedTest
  @CsvSource({"-1,1", "-2,1", "-6,1", "-1,2", "-3,3", "-7,2"})
  public void testGetItemNameForOrderInvalidId(int invalidItemId, int validOrderNumber) {
    // Invalid item ID should not be accepted
    assertNull(registeredClient.getItemNameForOrder(invalidItemId, validOrderNumber), "A " +
        "registered shielding individual with invalid item ID not be able to get item " +
        "name for order");
  }

  /**
   * Tests that a registered shielding individual should not be able to get item name
   * with invalid order number.
   *
   * @param validItemId a valid item ID
   * @param invalidOrderNumber an invalid order number
   * @see ShieldingIndividualClientImp#getItemNameForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,-1", "2,-4", "6,-2", "1,-5", "3,-6", "7,-7"})
  public void testGetItemNameForOrderInvalidOrder(int validItemId, int invalidOrderNumber) {
    // Invalid order number should not be accepted
    assertNull(registeredClient.getItemNameForOrder(validItemId, invalidOrderNumber),
        "A registered shielding individual with invalid item ID not be able to " +
            "get item name for order");
  }

  /**
   * Tests that a registered shielding individual should be able to get item name with
   * valid order number and item ID.
   *
   * @param validItemId a valid item ID
   * @param validFoodBoxId a valid food box ID
   * @param itemName corresponding item name
   * @see ShieldingIndividualClientImp#getItemNameForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,1,cucumbers",
                      "2,1,tomatoes",
                      "6,1,pork",
                      "1,2,cucumbers",
                      "3,3,onions",
                      "7,2,chicken"
  })
  public void testGetItemNameForOrderValidOrder(int validItemId, int validFoodBoxId,
                                                String itemName) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    // Generating catering company from server details
    assertEquals(itemName, registeredClient.getItemNameForOrder(validItemId, currentOrderNumber),
        "A registered shielding individual with valid item ID should be able to " +
            "get item name for order");
  }


  // --------------- SET ITEM QUANTITY FOR ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to set item quantity
   * with valid order number, item ID, and valid quantity.
   *
   * @param validItemId an example of valid item ID
   * @param validOrderNumber an example of valid order number
   * @param validQuantity an example of corresponding item quantity
   * @see ShieldingIndividualClientImp#setItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,1,0", "2,1,0", "6,1,0", "1,2,0", "3,3,0", "7,2,0"})
  public void testSetItemQuantityForOrderUnregistered(int validItemId, int validOrderNumber,
                                                      int validQuantity) {
    assertFalse(unregisteredClient.setItemQuantityForOrder(validItemId, validOrderNumber,
        validQuantity), "An unregistered shielding individual should not be able to set " +
        "item quantity for order");
  }

  /**
   * Tests that a registered shielding individual should not be able to set item quantity
   * with invalid order number but valid item ID, and quantity.
   *
   * @param invalidItemId an example of invalid item ID
   * @param validOrderNumber an example of valid order number
   * @param validQuantity an example of corresponding item quantity
   * @see ShieldingIndividualClientImp#setItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({"-1,1,0", "-2,1,0", "-6,1,0", "-1,2,0", "-3,3,0", "-7,2,0"})
  public void testSetItemQuantityForOrderInvalidId(int invalidItemId, int validOrderNumber,
                                                   int validQuantity) {
    assertFalse(registeredClient.setItemQuantityForOrder(invalidItemId, validOrderNumber,
        validQuantity),
        "A registered shielding individual with invalid item ID" +
            " should not be able to set item quantity for order");
  }

  /**
   * Tests that a registered shielding individual should not be able to set item quantity
   * with invalid quantity but valid item ID, and order number.
   *
   * @param validItemId an example of valid item ID
   * @param validOrderNumber an example of valid order number
   * @param invalidQuantity an example of corresponding invalid item quantity
   * @see ShieldingIndividualClientImp#setItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,1,100", "2,1,110", "6,1,210", "1,2,301", "3,3,704", "7,2,10050"})
  public void testSetItemQuantityForOrderInvalidQuantity(int validItemId, int validOrderNumber,
                                                         int invalidQuantity) {
    assertFalse(registeredClient.setItemQuantityForOrder(validItemId, validOrderNumber,
        invalidQuantity),
        "A registered shielding individual with invalid item ID" +
            " should not be able to set item quantity for order");
  }

  /**
   * Tests that a registered shielding individual should not be able to set item quantity
   * with invalid order number but valid item ID, and quantity.
   *
   * @param validItemId an example of valid item ID
   * @param invalidOrderNumber an example of invalid order number
   * @param validQuantity an example of corresponding valid item quantity
   * @see ShieldingIndividualClientImp#setItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,-1,0", "2,-1,0", "6,-1,0", "1,-2,0", "3,-3,0", "7,-2,0"})
  public void testSetItemQuantityForOrderInvalidOrder(int validItemId, int invalidOrderNumber,
                                                      int validQuantity) {
    assertFalse(registeredClient.setItemQuantityForOrder(validItemId, invalidOrderNumber,
        validQuantity),
        "A registered shielding individual with invalid item ID" +
            " should not be able to set item quantity for order");
  }

  /**
   * Tests that a registered shielding individual should be able to set item quantity
   * with valid order number, item ID, and quantity.
   *
   * @param validItemId an example of valid item ID
   * @param foodBoxId an example of valid food box ID
   * @param validQuantity an example of corresponding valid item quantity
   * @see ShieldingIndividualClientImp#setItemQuantityForOrder
   */
  @ParameterizedTest
  @CsvSource({"1,1,0", "7,2,0", "8,3,0", "13,4,0", "9,5,0"})
  public void testSetItemQuantityForOrderValidOrder(int validItemId, int foodBoxId,
                                                    int validQuantity) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, foodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertTrue(registeredClient.setItemQuantityForOrder(validItemId, currentOrderNumber,
        validQuantity),
        "Registered Individual should be allowed to set the quantity");
  }

  // --------------- GET ITEM IDS FOR ORDER ---------------


  /**
   * Tests that an unregistered shielding individual should not be able to get item IDs
   * with valid order number.
   *
   * @param orderNumber an example of valid order number
   * @see ShieldingIndividualClientImp#getItemIdsForOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  public void testGetItemIdsForOrderUnregistered(int orderNumber) {
    // Unregistered client
    assertNull(unregisteredClient.getItemIdsForOrder(orderNumber),
            "An unregistered shielding individual should not be able to get item IDs.");
  }

  /**
   * Tests that a registered shielding individual should not be able to get item IDs
   * with invalid order number.
   *
   * @param invalidOrderNumber an example of invalid order number
   * @see ShieldingIndividualClientImp#getItemIdsForOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -3, -4, -5})
  public void testGetItemIdsForOrderInvalidID(int invalidOrderNumber) {
    assertNull(registeredClient.getItemIdsForOrder(invalidOrderNumber),
        "A registered shielding individual with invalid ID should not be able to " +
            "get item IDs.");
  }

  /**
   * Tests that a registered shielding individual should be able to get item IDs
   * with valid order number.
   *
   * @param validFoodBoxId an example of valid food box IDs
   * @param expectedItemIdsString an example of expected item IDs for a particular food box
   * @see ShieldingIndividualClientImp#getItemIdsForOrder
   */
  @ParameterizedTest
  @CsvSource(value={"1;1,2,6", "2;1,3,7", "3;3,4,8", "4;13,11,8,9", "5;9,11,12"}, delimiter=';')
  public void testGetItemIdsForOrderValidID(int validFoodBoxId,
                                            String expectedItemIdsString) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBoxId);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    // Get the item IDs for a valid ID
    List<String> expectedStringItemIds;
    expectedStringItemIds = Arrays.asList(expectedItemIdsString.split(","));
    List<Integer> expectedItemIds;
    expectedItemIds =
        expectedStringItemIds.stream().map(Integer::parseInt).collect(Collectors.toList());

    // Compare the IDs
    assertEquals(expectedItemIds,
        registeredClient.getItemIdsForOrder(currentOrderNumber),
        "A registered shielding individual should be able to access item IDs using " +
            "a valid food box ID.");
  }

    // --------------- GET DISTANCE ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get distance.
   *
   * @param postCode1 a valid post code
   * @param postCode2 a valid post code
   * @see ShieldingIndividualClientImp#getDistance
   */
  @ParameterizedTest
  @CsvSource({"EH8_6NY,EH4_9US", "EH1_2RQ,EH11_9UL"})
  public void testGetDistanceUnregistered(String postCode1, String postCode2) {
    // Unregistered or invalid client should not be able to access this functionality
    assertEquals(-1f, unregisteredClient.getDistance(postCode1, postCode2),
            "An unregistered shielding individual should not be able to get " +
                "distances between post codes");
  }

  /**
   * Tests that a registered shielding individual should not be able to get distance
   * with invalid post codes
   *
   * @param postCode1 an invalid post code
   * @param postCode2 an invalid post code
   * @see ShieldingIndividualClientImp#getDistance
   */
  @ParameterizedTest
  @CsvSource({",EH14_2QK", "EH8_9JF,", ","})
  public void testGetDistanceInvalidCode(String postCode1, String postCode2) {
    // invalid distance for invalid post codes
    float invalidDistance = -1f;

    // Registered client should be able to access the functionality
    assertEquals(invalidDistance, registeredClient.getDistance(postCode1, postCode2),
        "A registered shielding individual should not be able to get distance " +
            "with invalid post code");
  }

  /**
   * Tests that a registered shielding individual should be able to get distance
   * with valid post codes
   *
   * @param postCode1 a valid post code
   * @param postCode2 a valid post code
   * @see ShieldingIndividualClientImp#getDistance
   */
  @ParameterizedTest
  @CsvSource({"EH17_8OW,EH12_8WI", "EH14_2YE,EH8_5HD", "EH9_3WM,EH13_7WQ"})
  public void testGetDistanceValidCode(String postCode1, String postCode2) {
    // Use reverse engineered distance values and compare it
    float mockDistance = mockDistance(postCode1, postCode2);
    assertEquals(mockDistance, registeredClient.getDistance(postCode1, postCode2),
        "After registration, distance computed between two post codes should " +
            "match the mock distance");
  }

  // Calculates the distance between two postcodes
  // Reverse engineered using code from API-side - credit: Boris Mocialov,
  // method "distance" in server.py
  private static float mockDistance(String postCode1, String postCode2) {
    // Format:
    // - postCode1 = EH[x1]_[y1]
    // - postCode2 = EH[x2]_[y2]
    // Remove leading "EH"
    postCode1 = postCode1.substring(2);
    postCode2 = postCode2.substring(2);
    // Parse rest of postcode
    int split1 = postCode1.indexOf('_');
    int split2 = postCode2.indexOf('_');
    int x1 = Integer.parseInt(postCode1.substring(0, split1));
    int x2 = Integer.parseInt(postCode2.substring(0, split2));
    String y1 = postCode1.substring(split1 + 1);
    String y2 = postCode2.substring(split2 + 1);
    // Compute cost
    int totalCost = 10 * abs(x2 - x1);
    // Go through y1 and y2
    for (int i = 0; i < y1.length(); i++) {
      char c1 = y1.charAt(i);
      char c2 = y2.charAt(i);
      boolean bothLetters = Character.isLetter(c1) && Character.isLetter(c2);
      boolean bothDigits = Character.isDigit(c1) && Character.isDigit(c2);
      if (bothLetters || bothDigits) {
        totalCost += abs(Character.toLowerCase(c2) - Character.toLowerCase(c1));
      }
    }
    float edinburghDiameter = 18334;  // m
    float maxCost = 99*10 + 25*2 + 9;
    return totalCost * edinburghDiameter / maxCost;
  }

  // --------------- PICK FOOD BOX ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to pick food
   * box with valid post codes
   *
   * @param validFoodBoxId a valid food box ID
   * @see ShieldingIndividualClientImp#pickFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testPickFoodBox(int validFoodBoxId) {
    // Unregistered shielding should not be able to test food box
    assertFalse(unregisteredClient.pickFoodBox(validFoodBoxId), "Unregistered " +
        "shielding individual should not be able to pick a food box");
  }

  /**
   * Tests that a registered shielding individual should not be able to pick food box
   * with invalid post codes
   *
   * @param invalidFoodBoxId an invalid food box ID
   * @see ShieldingIndividualClientImp#pickFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = { -1, -22, -53, -4, -5 })
  public void testPickFoodBoxInvalid(int invalidFoodBoxId) {
    // Pick invalid food box ID
    assertFalse(registeredClient.pickFoodBox(invalidFoodBoxId), "Registered shielding " +
        "individual should not be able to pick a food box with invalid ID");

  }

  /**
   * Tests that a registered shielding individual should be able to pick food box
   * with valid post codes
   *
   * @param validFoodBoxId a valid food box ID
   * @see ShieldingIndividualClientImp#pickFoodBox
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testPickFoodBoxValid(int validFoodBoxId) {
    // Pick invalid food box ID
    assertTrue(registeredClient.pickFoodBox(validFoodBoxId), "Registered shielding " +
        "individual should be able to change picked food box");
  }

  // --------------- SHOW FOOD BOX ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to see the food boxes
   *
   * @param preference a valid dietary preference
   * @see ShieldingIndividualClientImp#showFoodBoxes
   */
  @ParameterizedTest
  @ValueSource(strings = {"none", "vegan", "pollotarian"})
  public void testShowFoodBoxesUnregistered(String preference) {
    assertNull(unregisteredClient.showFoodBoxes(preference),
        "An unregistered shielding individual should not be able to view food " +
            "boxes for any dietary preference.");
  }

  /**
   * Tests that a registered shielding individual should be able to see the food boxes
   *
   * @param dietaryPreference a valid dietary preference
   * @param expectedItemIdsString a valid dietary item ID
   * @see ShieldingIndividualClientImp#showFoodBoxes
   */
 @ParameterizedTest
 @CsvSource(value={";1,2,3,4,5", "'';1,2,3,4,5", "none;1,3,4", "vegan;5", "pollotarian;2"},
     delimiter=';')
 public void testShowFoodBoxes(String dietaryPreference, String expectedItemIdsString)
 {
    List<String> expectedStringItemIds;
    expectedStringItemIds = Arrays.asList(expectedItemIdsString.split(","));

    assertEquals(
        expectedStringItemIds, registeredClient.showFoodBoxes(dietaryPreference),
      "A registered shielding individual should be able to view " +
          "food boxes for any dietary preference.");
  }

  // --------------- GET CLOSEST CATERING COMPANY ---------------
  /**
   * Tests that an unregistered shielding individual should not be able to get catering companies
   *
   * @see ShieldingIndividualClientImp#getCateringCompanies
   */
  @Test
  public void testGetCateringCompaniesUnregistered() {
    // Unregistered shielding individual
    assertNull(unregisteredClient.getCateringCompanies(), "Unregistered shielding " +
        "individual should not be able to get catering companies");
  }


  // --------------- GET CLOSEST CATERING COMPANY ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to get
   * closest catering company
   *
   * @see ShieldingIndividualClientImp#getCateringCompanies
   */
  @Test
  public void testGetClosestCateringCompany() {
    assertNull(unregisteredClient.getClosestCateringCompany(), "Unregistered client " +
        "should not be able to get closest company");
  }

  // --------------- CANCEL ORDER ---------------

  /**
   * Tests that an unregistered shielding individual should not be able to cancel the order
   *
   * @param validOrderNumber an example of valid order number
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { 1, 22, 53, 4, 5 })
  public void testCancelOrderUnregistered(int validOrderNumber) {
    assertFalse(unregisteredClient.cancelOrder(validOrderNumber),
            "An unregistered shielding individual should not be able to cancel any order.");

  }

  /**
   * Tests that a registered shielding individual should not be able to cancel the order
   * with invalid order
   *
   * @param invalidOrderNumber example of invalid order numbers
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @ParameterizedTest
  @ValueSource(ints = { -1, -22, -53, -4, -5 })
  public void testCancelOrderInvalidOrder(int invalidOrderNumber) {
    assertFalse(registeredClient.cancelOrder(invalidOrderNumber), "A registered " +
        "shielding individual should not be able to cancel a nonexistent order.");
  }

  /**
   * Tests that a registered shielding individual should be able to cancel the order
   * with valid order
   *
   * @param validFoodBox a valid food box ID
   * @see ShieldingIndividualClientImp#cancelOrder
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void testCancelOrder(int validFoodBox) {
    // Using setting instead of directly calling the method
    mockPickFoodBox(registeredClient, validFoodBox);
    int currentOrderNumber = mockPlaceOrder(registeredClient);

    assertTrue(registeredClient.cancelOrder(currentOrderNumber),
        "A registered shielding individual should be able to cancel an order they " +
            "placed right after it was placed.");
  }
}
