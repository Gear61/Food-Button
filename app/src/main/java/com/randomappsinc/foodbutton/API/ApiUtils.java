package com.randomappsinc.foodbutton.API;

import android.util.Base64;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class ApiUtils {
    private static SecureRandom random = new SecureRandom();

    public static Map<String, String> getSearchQueryMap(String location) {
        Map<String, String> params = new LinkedHashMap<>();
        try {
            params.put("location", "San Francisco, CA");

            params.put("oauth_consumer_key", ApiConstants.CONSUMER_KEY);
            params.put("oauth_nonce", "KOwCDZ");
            params.put("oauth_signature_method", "HMAC-SHA1");
            params.put("oauth_timestamp", "1459057385");
            params.put("oauth_token", ApiConstants.TOKEN);
            params.put("oauth_version", "1.0");

            long unixTime = System.currentTimeMillis() / 1000L;

            params.put("term", "Food");

            Call<IgnoredResponse> call = RestClient.get().getYelpService().doSearch(params);
            String normalizedParams = call.request().url().toString().replace(ApiConstants.BASE_URL + "search?", "");

            String baseUrl = "GET&" + URLEncoder.encode(ApiConstants.BASE_URL + "search/", "UTF-8") + "&"
                    + URLEncoder.encode(normalizedParams, "UTF-8");
            System.out.println("NARNIA: " + baseUrl.replaceAll("%2C", "%252C"));
        }
        catch (Exception e) {}
        // params.put("oauth_signature", generateOauthSignature(currentUrl));

        return params;
    }

    public static String generateNonce() {
        return new BigInteger(130, random).toString(32);
    }

    public static String generateOauthSignature(String url) {
        try {
            String signingKeyString = ApiConstants.CONSUMER_SECRET + "&" + ApiConstants.TOKEN_SECRET;
            SecretKeySpec signingKey = new SecretKeySpec(signingKeyString.getBytes(), "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            String newUrl = "GET&https%3A%2F%2Fapi.yelp.com%2Fv2" +
                    "%2Fsearch%2F&location%3DSan%2520Francisco%252C%2520CA" +
                    "%26oauth_consumer_key%3DweyOGS3IceGD7WfXwsETdA%26oauth_nonce" +
                    "%3DKOwCDZ%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp" +
                    "%3D1459057385%26oauth_token%3D4rPzE_3oNCAALX1fE5FUmqLC5XZyIfHF" +
                    "%26oauth_version%3D1.0%26term%3DFood";

            byte[] rawHmac = mac.doFinal(newUrl.getBytes());
            byte[] result = Base64.encode(rawHmac, Base64.DEFAULT);
            System.out.println("NARNIA: " + new String(result));
            return new String(result);
        }
        catch (Exception e) {
            return "";
        }
    }
}
