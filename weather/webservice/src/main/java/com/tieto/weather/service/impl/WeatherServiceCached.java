package com.tieto.weather.service.impl;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;

import com.tieto.weather.error.ServerError;
import com.tieto.weather.mapper.Mapper;
import com.tieto.weather.service.WeatherService;
import com.tieto.weather.vo.CitiesVO;
import com.tieto.weather.vo.CityWeatherVO;
import com.tieto.weather.wunderground.schema.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherServiceCached implements WeatherService {

	private Mapper<Response, CityWeatherVO> mapper;
	private String apikey;
	private String urlString;
	private CitiesVO cities;
	private RestTemplate restTemplate;

	@Cacheable(value = "weatherCache")
	public CityWeatherVO getWeatherData(String city) throws ServerError {
		
		System.out.println("Not found in cache: "+city);
		return getCityWeather(city);
	}
	
	@CachePut(value = "weatherCache")
	public CityWeatherVO updateWeatherData(String city) throws ServerError {
		
		//System.out.println("Put to cache: "+city);
	    Logger log = LoggerFactory.getLogger(this.getClass());
	    log.info("Put to cache: "+city);
		return getCityWeather(city);

	}
	
	private CityWeatherVO getCityWeather(String city) throws ServerError {

		Response wundergroundResponse = restTemplate.getForObject(urlString, Response.class, apikey, cities.getCities().get(city), city);
		System.out.println("Call Wunderground for: "+city);		
		return mapper.map(wundergroundResponse, new CityWeatherVO());
	}
	
	public void setWundergroundResponseMapper(Mapper<Response, CityWeatherVO> mapper) {
		this.mapper = mapper;
	}

	public void setCities(CitiesVO cities) {
		this.cities = cities;
	}
	
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
}
