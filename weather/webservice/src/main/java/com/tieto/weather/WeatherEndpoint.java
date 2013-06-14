package com.tieto.weather;

import com.tieto.weather.schema.WeatherRequest;
import com.tieto.weather.schema.WeatherResponse;

public interface WeatherEndpoint {

	/**
	 * Endpoint method for getting weather data.
	 * 
	 * @param weatherRequest Contains list of cities.
	 * @return Weather data for cities from request.
	 */
	public WeatherResponse handleWeatherRequest(WeatherRequest weatherRequest);
}
