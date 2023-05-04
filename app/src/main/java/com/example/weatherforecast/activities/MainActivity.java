package com.example.weatherforecast.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherforecast.R;
import com.example.weatherforecast.Utils.AppUtils;
import com.example.weatherforecast.Utils.Constants;
import com.example.weatherforecast.databinding.ActivityMainBinding;
import com.example.weatherforecast.model.WeatherForeCastResponse;
import com.example.weatherforecast.viewmodel.WeatherForecastViewModel;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WeatherForecastViewModel weatherForecastViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Using databinding to access widgets in xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        weatherForecastViewModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);
        initUI();

        binding.imgSearch.setOnClickListener(v -> {
            if (!binding.cityName.getText().toString().isEmpty()) {
                AppUtils.hideKeyboard(this, getCurrentFocus());
                weatherForecastViewModel.searchCity(binding.cityName.getText().toString());
            }
        });


        weatherForecastViewModel.weatherResponse.observe(this, weatherForeCastResponse -> {
            if (weatherForeCastResponse != null && weatherForeCastResponse.getCod() == 200) {
                setData(weatherForeCastResponse);
            } else {
                AppUtils.customErrorAlert(this, "city not found");
            }

        });

    }

    private void initUI() {
        sharedPreferences = getPreferences(this);
        editor = sharedPreferences.edit();
        gson = new Gson();
        getData();
    }

    public SharedPreferences getPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    @SuppressLint("SetTextI18n")
    public void setData(WeatherForeCastResponse weatherForeCastResponse) {
        double temp = weatherForeCastResponse.getMain().getTemp() - 273.15;
        double temp_feel_like = weatherForeCastResponse.getMain().getFeelsLike() - 273.15;
        double temp_high = weatherForeCastResponse.getMain().getTempMax() - 273.15;
        double temp_min = weatherForeCastResponse.getMain().getTempMin() - 273.15;
        int temperature = (int) Math.round(temp);
        int temperature_high = (int) Math.round(temp_high);
        int temperature_low = (int) Math.round(temp_min);
        int feel_like = (int) Math.round(temp_feel_like);
        String des = weatherForeCastResponse.getWeather().get(0).getDescription();
        String description = des.substring(0, 1).toUpperCase()+ des.substring(1);
        binding.todayTemperature.setText(temperature + " " + getString(R.string.temp_unit));
        binding.tempHighLow.setText(getDayOfWeek() + " " +temperature_high+"/"+temperature_low+" " +getString(R.string.temp_unit));
        binding.city.setText(weatherForeCastResponse.getName());
        binding.tvTempFeelsLike.setText(feel_like + " " + getString(R.string.temp_unit));
        binding.tvWind.setText(weatherForeCastResponse.getWind().getSpeed() + " " + getString(R.string.wind_unit));
        binding.tvHumidity.setText(weatherForeCastResponse.getMain().getHumidity() + " " + getString(R.string.humidity_unit));
        binding.tvPressure.setText(weatherForeCastResponse.getMain().getPressure() + " " + getString(R.string.pressure_unit));
        binding.todayDescription.setText(description);
        String sunRiseTime = getTime(weatherForeCastResponse.getSys().getSunrise());
        String sunSetTime = getTime(weatherForeCastResponse.getSys().getSunset());
        int visibility = weatherForeCastResponse.getVisibility() / 1000;
        binding.sunRise.setText(sunRiseTime + " AM");
        binding.sunSet.setText(sunSetTime + " PM");
        binding.tvVisibility.setText(visibility + " km");
        switch (weatherForeCastResponse.getWeather().get(0).getIcon()) {
            case "01d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_clear_sky);
                   /* binding.toolbar.setBackgroundResource(R.color.color_clear_and_sunny);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_clear_and_sunny));*/
                break;
            case "01n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_clear_sky);
                    /*binding.toolbar.setBackgroundResource(R.color.color_clear_and_sunny);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_clear_and_sunny));*/
                break;
            case "02d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_few_cloud);
                    /*binding.toolbar.setBackgroundResource(R.color.color_partly_cloudy);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_partly_cloudy));*/
                break;
            case "02n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_few_cloud);
                    /*binding.toolbar.setBackgroundResource(R.color.color_partly_cloudy);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_partly_cloudy));*/
                break;
            case "03d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_scattered_clouds);
                   /* binding.toolbar.setBackgroundResource(R.color.color_gusty_winds);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_gusty_winds));*/
                break;
            case "03n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_scattered_clouds);
                    /*binding.toolbar.setBackgroundResource(R.color.color_gusty_winds);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_gusty_winds));*/
                break;
            case "04d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_broken_clouds);
                   /* binding.toolbar.setBackgroundResource(R.color.color_cloudy_overnight);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_cloudy_overnight));*/
                break;
            case "04n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_broken_clouds);
                   /* binding.toolbar.setBackgroundResource(R.color.color_cloudy_overnight);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_cloudy_overnight));*/
                break;
            case "09d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_shower_rain);
                    /*binding.toolbar.setBackgroundResource(R.color.color_hail_stroms);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_hail_stroms));*/
                break;
            case "09n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_shower_rain);
                    /*binding.toolbar.setBackgroundResource(R.color.color_hail_stroms);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_hail_stroms));*/
                break;
            case "10d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_rain);
                    /*binding.toolbar.setBackgroundResource(R.color.color_heavy_rain);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_heavy_rain));*/
                break;
            case "10n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_rain);
                    /*binding.toolbar.setBackgroundResource(R.color.color_heavy_rain);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_heavy_rain));*/
                break;
            case "11d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_thunderstorm);
                    /*binding.toolbar.setBackgroundResource(R.color.color_thunderstroms);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_thunderstroms));*/
                break;
            case "11n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_thunderstorm);
                    /*binding.toolbar.setBackgroundResource(R.color.color_thunderstroms);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_thunderstroms));*/
                break;
            case "13d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_snow);
                    /*binding.toolbar.setBackgroundResource(R.color.color_snow);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_snow));*/
                break;
            case "13n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_snow);
                    /*binding.toolbar.setBackgroundResource(R.color.color_snow);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_snow));*/
                break;
            case "15d":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_mist);
                    /*binding.toolbar.setBackgroundResource(R.color.color_mix_snow_and_rain);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_mix_snow_and_rain));*/
                break;
            case "15n":
                binding.imageViewWeather.setImageResource(R.drawable.ic_weather_mist);
                    /*binding.toolbar.setBackgroundResource(R.color.color_mix_snow_and_rain);
                   binding.layoutWeather.setBackgroundColor(getResources().getColor(R.color.color_mix_snow_and_rain));*/
                break;
        }
        storePreferences(weatherForeCastResponse);
    }

    public void storePreferences(WeatherForeCastResponse response) {
        editor.putString(Constants.RESPONSE, gson.toJson(response));
        editor.apply();
    }

    public void getData() {
        try {
            String gsonobj = sharedPreferences.getString(Constants.RESPONSE, "");
            if (!gsonobj.isEmpty()) {
                WeatherForeCastResponse weatherForeCastResponse = gson.fromJson(gsonobj, WeatherForeCastResponse.class);
                setData(weatherForeCastResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat obj = new SimpleDateFormat("hh:mm");
        return obj.format(date);
    }

    public String getDayOfWeek(){
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

}