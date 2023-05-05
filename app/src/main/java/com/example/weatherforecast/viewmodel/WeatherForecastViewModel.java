package com.example.weatherforecast.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecast.Utils.Constants;
import com.example.weatherforecast.model.WeatherForeCastResponse;
import com.example.weatherforecast.network.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecastViewModel extends AndroidViewModel {

    public MutableLiveData<WeatherForeCastResponse> weatherResponse = new MutableLiveData<>();
    private Context context;

    public WeatherForecastViewModel(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
    }


    public void searchCity(String city) {
        WeatherService weatherService = WeatherService.Factory.create(context);
        Call<WeatherForeCastResponse> call = weatherService.searchCityByName(city, Constants.API_KEY);
        Log.d("ViewModel", call.request().url().toString());
        call.enqueue(new Callback<WeatherForeCastResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForeCastResponse> call, @NonNull Response<WeatherForeCastResponse> response) {
                weatherResponse.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<WeatherForeCastResponse> call, @NonNull Throwable t) {
                Log.d("Error", t.toString());
            }
        });
    }
}
