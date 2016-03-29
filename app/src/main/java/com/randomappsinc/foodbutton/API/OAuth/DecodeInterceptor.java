package com.randomappsinc.foodbutton.API.OAuth;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class DecodeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String originalUrl = originalRequest.url().toString();
        int signatureIndex = originalUrl.lastIndexOf("oauth_signature");
        String signature = URLDecoder.decode(originalUrl.substring(signatureIndex), "UTF-8").replaceAll(" ", "+");
        String finalUrlPrefix = URLDecoder.decode(originalUrl.substring(0, signatureIndex), "UTF-8");
        Request newRequest = originalRequest.newBuilder()
                .url(finalUrlPrefix + signature)
                .build();
        return chain.proceed(newRequest);
    }
}
