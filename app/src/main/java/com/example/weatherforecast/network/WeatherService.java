package com.example.weatherforecast.network;

import android.content.Context;

import com.example.weatherforecast.Utils.AppUtils;
import com.example.weatherforecast.Utils.Constants;
import com.example.weatherforecast.model.WeatherForeCastResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WeatherService {

    class Factory {
        public static WeatherService create(Context context) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            httpClient.connectTimeout(60, TimeUnit.SECONDS);
            httpClient.writeTimeout(60, TimeUnit.SECONDS);
            httpClient.addNetworkInterceptor(AppUtils.REWRITE_RESPONSE_INTERCEPTOR);
            httpClient.addInterceptor(AppUtils.getOfflineInterceptor(context));
            httpClient.cache(AppUtils.getCache(context));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            return retrofit.create(WeatherService.class);
        }
    }

    @POST("weather?")
    Call<WeatherForeCastResponse> searchCityByName(@Query("q") String city, @Query("appid") String apiKey);
}
