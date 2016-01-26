package com.meruvian.pxc.selfservice.interceptor;

import android.util.Log;

import com.meruvian.pxc.selfservice.entity.Authentication;
import com.meruvian.pxc.selfservice.util.SocialAuthenticationUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;

/**
 * Created by akm on 06/11/15.
 */
public class SecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Authentication auth = SocialAuthenticationUtils.getCurrentAuthentication();

        if (auth != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + auth.getAccessToken())
                    .build();

        }

        return chain.proceed(request);
    }
}
