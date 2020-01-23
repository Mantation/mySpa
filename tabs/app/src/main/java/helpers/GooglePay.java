package helpers;

import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class GooglePay {

    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0);
    }

    private static JSONObject getTokenizationSpecification() throws JSONException {
        JSONObject tokenizationSpecification = new JSONObject();
        tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
        tokenizationSpecification.put(
                "parameters",
                new JSONObject()
                        .put("gateway", "example")
                        .put("gatewayMerchantId", "exampleGatewayMerchantId"));

        return tokenizationSpecification;
    }

    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA");
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS");
    }

    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");
        cardPaymentMethod.put(
                "parameters",
                new JSONObject()
                        .put("allowedAuthMethods", GooglePay.getAllowedCardAuthMethods())
                        .put("allowedCardNetworks", GooglePay.getAllowedCardNetworks()));

        return cardPaymentMethod;
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = GooglePay.getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", GooglePay.getTokenizationSpecification());

        return cardPaymentMethod;
    }

    private static JSONObject getTransactionInfo() throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", "12.34");
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("currencyCode", "USD");

        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject()
                .put("merchantName", "Example Merchant");
    }


    public static JSONObject getIsReadyToPayRequest() {
        JSONObject isReadyToPayRequest = null;
        try {
            isReadyToPayRequest = GooglePay.getBaseRequest();
            isReadyToPayRequest.put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
            return isReadyToPayRequest;
        } catch (JSONException e) {
            return isReadyToPayRequest;
        }
    }


    public static JSONObject getPaymentDataRequest() {
        JSONObject paymentDataRequest = null;
        try {
            paymentDataRequest = GooglePay.getBaseRequest();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(GooglePay.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", GooglePay.getTransactionInfo());
            paymentDataRequest.put("merchantInfo", GooglePay.getMerchantInfo());
            return paymentDataRequest;
        } catch (JSONException e) {
            return paymentDataRequest;
        }
    }

}
