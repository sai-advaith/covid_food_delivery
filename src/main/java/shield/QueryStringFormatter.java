package shield;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Formats query strings into the format expected by the Scottish governments server's API.
 *
 * <p>Ensures that query string parameters do not contain reserved characters, such as '&' or '?'.
 */
public final class QueryStringFormatter {
  private QueryStringFormatter() {
    // Private constructor essentially makes this a static class
  }

  /**
   * This method is used to format a request to register the shielding individual using the
   * individual's CHI. This format is specified by the server's API.
   *
   * @param chi is the community health index number of the individual required to register an
   *     individual
   * @return the formatted request to the server's API to register a shielding individual
   */
  public static String individualRegisterRequest(String chi) {
    String formattedChi = URLEncoder.encode(chi, StandardCharsets.UTF_8);
    return String.format(QueryStrings.REGISTER_SHIELDING_INDIVIDUAL.toString(), formattedChi);
  }

  /**
   * This method is used to format a request to edit an order using the order number of the order.
   * This format is specified by the server's API.
   *
   * @param orderNumber is the order number of the order which is to be edited
   * @return the formatted request to the server's API to edit an order
   */
  public static String editOrderRequest(int orderNumber) {
    return String.format(QueryStrings.EDIT_ORDER.toString(), orderNumber);
  }

  /**
   * This method is used to format a request to request an order status using the order number of
   * the order. This format is specified by the server's API.
   *
   * @param orderNumber is the order number of the order who's status will be requested
   * @return the formatted request to the server's API to get the order status of the order
   */
  public static String orderStatusRequest(int orderNumber) {
    return String.format(QueryStrings.ORDER_STATUS.toString(), orderNumber);
  }

  /**
   * This method is used to format a request to cancel an order using the order number of the order.
   * This format is specified by the server's API.
   *
   * @param orderNumber is the order number of the order which is to be cancelled
   * @return the formatted request to the server's API to cancel an order
   */
  public static String cancelOrderRequest(int orderNumber) {
    return String.format(QueryStrings.CANCEL_ORDER.toString(), orderNumber);
  }

  /**
   * This method is used to format a request to place an order using the CHI of the individual, and
   * details of the catering company. This format is specified by the server's API.
   *
   * @param chi is the community health index number of the individual required to register an
   *     individual
   * @param companyName is the name of the catering company with whom the order is placed
   * @param postCode is the post code of the catering company with whom the order is placed
   * @return the formatted request to the server's API to place an order
   */
  public static String placeOrderRequest(String chi, String companyName, String postCode) {
    String formattedChi = URLEncoder.encode(chi, StandardCharsets.UTF_8);
    String formattedCompanyName = URLEncoder.encode(companyName, StandardCharsets.UTF_8);
    String formattedPostCode = URLEncoder.encode(postCode, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.PLACE_ORDER.toString(), formattedChi, formattedCompanyName, formattedPostCode);
  }

  /**
   * This method is used to format a request to get the distance between two post codes.
   *
   * @param postCode1 is one of the post codes which will be used to calculate distance
   * @param postCode2 is another post code which will be used to calculate distance
   * @return the formatted request to the server's API to get distance between two post codes
   */
  public static String distanceRequest(String postCode1, String postCode2) {
    String formattedPostCode1 = URLEncoder.encode(postCode1, StandardCharsets.UTF_8);
    String formattedPostCode2 = URLEncoder.encode(postCode2, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.GET_DISTANCE.toString(), formattedPostCode1, formattedPostCode2);
  }

  /**
   * This method is used to format a request to register the catering company using the catering
   * companies details. This format is specified by the server's API.
   *
   * @param name is the name of the catering company which is to be registered
   * @param postCode is the post code of the catering company which is to be registered
   * @return the formatted request to the server's API to register a catering company
   */
  public static String cateringCompanyRegisterRequest(String name, String postCode) {
    String formattedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
    String formattedPostCode = URLEncoder.encode(postCode, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.REGISTER_CATERING_COMPANY.toString(), formattedName, formattedPostCode);
  }

  /**
   * This method is used to format a request to update the status of an order by a catering company
   * This format is specified by the server's API.
   *
   * @param orderNumber is the order number of the order who's status will be updated
   * @param status is the status to which the status of the order will be update
   * @return the formatted request to the server's API to update the order status by a catering
   *     company
   */
  public static String updateCateringOrderRequest(int orderNumber, String status) {
    String formattedStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.UPDATE_CATERING_ORDER_STATUS.toString(), orderNumber, formattedStatus);
  }

  /**
   * This method is used to format a request to register the supermarket using the supermarket's
   * details. This format is specified by the server's API.
   *
   * @param name is the name of the supermarket which is to be registered
   * @param postCode is the post code of the supermarket which is to be registered
   * @return the formatted request to the server's API to register a supermarket
   */
  public static String supermarketRegisterRequest(String name, String postCode) {
    String formattedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
    String formattedPostCode = URLEncoder.encode(postCode, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.REGISTER_SUPERMARKET.toString(), formattedName, formattedPostCode);
  }

  /**
   * This method is used to format a request to update the status of an order by a supermarket This
   * format is specified by the server's API.
   *
   * @param orderNumber is the order number of the order who's status will be updated
   * @param status is the status to which the status of the order will be update
   * @return the formatted request to the server's API to update the order status by a supermarket
   */
  public static String updateSupermarketOrderRequest(int orderNumber, String status) {
    String formattedStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.UPDATE_SUPERMARKET_STATUS.toString(), orderNumber, formattedStatus);
  }

  /**
   * This method is used to format a request to record an order with a supermarket on the server
   * This format is specified by the server's API.
   *
   * @param chi is the community health index number of the individual, using which an order will be
   *     recorded
   * @param orderNumber is the order number of the order being recorded
   * @param name is the name of the supermarket with whom the order will be recorded
   * @param postCode is the post code of the supermarket with whom the order will be recorded
   * @return the formatted request to the server's API to record a supermarket order
   */
  public static String recordSupermarketOrderRequest(
      String chi, int orderNumber, String name, String postCode) {
    String formattedChi = URLEncoder.encode(chi, StandardCharsets.UTF_8);
    String formattedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
    String formattedPostCode = URLEncoder.encode(postCode, StandardCharsets.UTF_8);
    return String.format(
        QueryStrings.RECORD_SUPERMARKET_ORDER.toString(),
        formattedChi,
        orderNumber,
        formattedName,
        formattedPostCode);
  }
}
