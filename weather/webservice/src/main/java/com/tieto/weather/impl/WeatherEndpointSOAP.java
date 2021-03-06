package com.tieto.weather.impl;

import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tieto.weather.error.ClientError;
import com.tieto.weather.error.ServerError;
import com.tieto.weather.mapper.impl.WeatherResponseMapper;
import com.tieto.weather.schema.ObjectFactory;
import com.tieto.weather.schema.WeatherRequest;
import com.tieto.weather.schema.WeatherResponse;
import com.tieto.weather.service.WeatherService;
import com.tieto.weather.vo.CitiesVOFactory;
import com.tieto.weather.vo.WeatherResponseVO;

/**
 * Endpoint for SOAP calls.
 */
@Endpoint
public class WeatherEndpointSOAP {

	private static final String NAMESPACE_URI = "http://weather.tieto.com/schemas";
	
	private final ObjectFactory factory;
	private WeatherResponseMapper responseMapper;
	private WeatherService service;
	private CitiesVOFactory cities;
	
	public WeatherEndpointSOAP(ObjectFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Endpoint method for getting weather data.
	 * If empty city list is provided in request then are returned data for supported cities.
	 * 
	 * @param weatherRequest Contains list of cities.
	 * @return Weather data for cities from request or for supported cities.
	 * @throws ServerError 
	 * @throws ClientError 
	 */
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "WeatherRequest")
	@ResponsePayload	
	public WeatherResponse handleWeatherRequest(@RequestPayload WeatherRequest weatherRequest) throws ServerError, ClientError {
		
		WeatherResponseVO response = new WeatherResponseVO();
		WeatherResponse result;
		
		if(weatherRequest.getCity().isEmpty()) {
			LoggerFactory.getLogger(WeatherEndpointSOAP.class).info("SOAP Request for all cities.");
			for (String city : cities.getCities().keySet()) {				
				response.getCityWeather().add(service.getWeatherData(city));			
			}
		} else {
			LoggerFactory.getLogger(WeatherEndpointSOAP.class).info("SOAP Request for city: " + weatherRequest.getCity());
			for (String city : weatherRequest.getCity()) {
				response.getCityWeather().add(service.getWeatherData(city));				
			}
		}
		
		result = responseMapper.mapWeatherResponse(response, factory.createWeatherResponse()); 
		
		LoggerFactory.getLogger(WeatherEndpointSOAP.class).info("SOAP Request completed.");
		
		return result;
	}
	
	
	public void setWeatherResponseMapper( WeatherResponseMapper responseMapper) {
		this.responseMapper = responseMapper;
	}

	public void setWeatherService(WeatherService service) {
		this.service = service;
	}
	
	public void setCities(CitiesVOFactory cities) {
		this.cities = cities;
	}

}
