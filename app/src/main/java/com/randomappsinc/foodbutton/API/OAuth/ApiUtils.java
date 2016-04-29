package com.randomappsinc.foodbutton.API.OAuth;

import android.util.Base64;

import com.randomappsinc.foodbutton.API.ApiConstants;
import com.randomappsinc.foodbutton.API.Models.SearchResponse;
import com.randomappsinc.foodbutton.API.RestClient;
import com.randomappsinc.foodbutton.R;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class ApiUtils {
    public static Map<String, String> getSearchQueryMap(String location) {
        Map<String, String> params = new LinkedHashMap<>();
        try {
            params.put("location", location);

            String nonce = generateNonce();
            String currentTime = String.valueOf(System.currentTimeMillis() / 1000L);
            addOauthFieldsAlphabetical(params, nonce, currentTime);

            params.put("term", "Food");

            Call<SearchResponse> call = RestClient.get().getYelpService().doSearch(params);
            String normalizedParams = call.request().url().toString().replace(ApiConstants.BASE_URL + "search/?", "");
            String baseUrl = ("GET&" + URLEncoder.encode(ApiConstants.BASE_URL + "search/", "UTF-8") + "&"
                    + URLEncoder.encode(normalizedParams, "UTF-8"))
                    .replaceAll("%2C", "%252C");

            String signature = generateOauthSignature(baseUrl);

            Map<String, String> finalParams = new LinkedHashMap<>();
            finalParams.put("term", "Food");
            finalParams.put("location", location);
            addOauthFieldsFinal(finalParams, nonce, currentTime, signature);
            finalParams.put("oauth_signature", signature);
            return finalParams;
        }
        catch (Exception ignored) {}

        return params;
    }

    public static void addOauthFieldsAlphabetical(Map<String, String> params, String nonce, String timestamp) {
        params.put("oauth_consumer_key", ApiConstants.CONSUMER_KEY);
        params.put("oauth_nonce", nonce);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", timestamp);
        params.put("oauth_token", ApiConstants.TOKEN);
        params.put("oauth_version", "1.0");
    }

    public static void addOauthFieldsFinal(Map<String, String> params, String nonce,
                                           String timestamp, String signature) {
        params.put("oauth_consumer_key", ApiConstants.CONSUMER_KEY);
        params.put("oauth_token", ApiConstants.TOKEN);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", timestamp);
        params.put("oauth_nonce", nonce);
        params.put("oauth_version", "1.0");
        params.put("oauth_signature", signature);
    }

    public static String generateNonce() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

    public static String generateOauthSignature(String url) {
        try {
            String signingKeyString = ApiConstants.CONSUMER_SECRET + "&" + ApiConstants.TOKEN_SECRET;
            SecretKeySpec signingKey = new SecretKeySpec(signingKeyString.getBytes(), "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(url.getBytes());
            byte[] result = Base64.encode(rawHmac, Base64.DEFAULT);

            return new String(result);
        }
        catch (Exception e) {
            return "";
        }
    }

    public static String getCategoryFromId(int categoryId) {
        switch (categoryId) {
            case R.id.american:
                return "newamerican,tradamerican";
            case R.id.chinese:
                return "chinese";
            case R.id.fast_food:
                return "hotdogs";
            case R.id.french:
                return "french";
            case R.id.indian:
                return "indpak";
            case R.id.japanese:
                return "japanese";
            case R.id.korean:
                return "korean";
            case R.id.mediterranean:
                return "mediterranean";
            case R.id.middle_eastern:
                return "mideastern";
            case R.id.mexican:
                return "mexican";
            case R.id.pizza:
                return "pizza";
            case R.id.thai:
                return "thai";
            default:
                return "";
        }
    }

    public static int getCheckboxId(int categoryId) {
        switch (categoryId) {
            case R.id.american:
                return R.id.american_toggle;
            case R.id.chinese:
                return R.id.chinese_toggle;
            case R.id.fast_food:
                return R.id.fast_food_toggle;
            case R.id.french:
                return R.id.french_toggle;
            case R.id.indian:
                return R.id.indian_toggle;
            case R.id.japanese:
                return R.id.japanese_toggle;
            case R.id.korean:
                return R.id.korean_toggle;
            case R.id.mediterranean:
                return R.id.mediterranean_toggle;
            case R.id.middle_eastern:
                return R.id.middle_eastern_toggle;
            case R.id.mexican:
                return R.id.mexican_toggle;
            case R.id.pizza:
                return R.id.pizza_toggle;
            case R.id.thai:
                return R.id.thai_toggle;
            default:
                return 0;
        }
    }
}
