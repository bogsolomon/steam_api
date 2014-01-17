package ca.bsolomon.api.steam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bsolomon.api.steam.dao.AppDetails;

public class SteamAPI {

	private static final String SUCCESS = "success";
	private static final String DATA = "data";
	private static final String NAME = "name";
	private static final String PRICE = "price_overview";
	private static final String FINAL = "final";
	private static final String RELEASE = "release_date";
	private static final String DATE = "date";
	private static final String GENRE = "genres";
	private static final String DESC = "description";
	private static final String CATEGORIES = "categories";
	private static final String METACRITIC = "metacritic";
	private static final String SCORE = "score";

	private static final String STEAM_API_APPDETAILS_URL = "http://store.steampowered.com/api/appdetails/?appids=";
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static List<AppDetails> getAppDetails(List<String> appIds) {
		List<AppDetails> appDetails = new ArrayList<>();

		HttpClient httpclient = new DefaultHttpClient();

		StringBuffer url = new StringBuffer(STEAM_API_APPDETAILS_URL);

		for (String appId : appIds) {
			url.append(appId).append(",");
		}

		HttpGet httppost = new HttpGet(url.substring(0, url.length() - 1));

		try {
			// Add your data
			HttpResponse response = httpclient.execute(httppost);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer longline = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				longline.append(line);
			}
			
			List<Object> result = objectMapper.readValue("["+longline.toString()+"]", 
    				objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));
			
			appDetails = parseAppDetails(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httppost.releaseConnection();
		}
		
		return appDetails;
	}

	private static List<AppDetails> parseAppDetails(List<Object> results) {
		List<AppDetails> details = new ArrayList<>();
		
		for (Object result:results) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> listMap = (LinkedHashMap<String, Object>)result;
			
			for (String appId:listMap.keySet()) {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)listMap.get(appId);
				
				AppDetails detail = new AppDetails();
				detail.setAppId(appId);
				detail.setSuccess((Boolean)map.get(SUCCESS));
				
				if (detail.isSuccess()) {
				
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>)map.get(DATA);
					
					detail.setName((String)data.get(NAME));
					
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> priceInfo = (LinkedHashMap<String, Object>)data.get(PRICE);
					Integer priceValue = (Integer)priceInfo.get(FINAL);
					
					detail.setPrice(priceValue/100+"."+priceValue%100);
					
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> releaseInfo = (LinkedHashMap<String, Object>)data.get(RELEASE);
					String date = (String)releaseInfo.get(DATE);
					
					detail.setReleaseDate(date);
					
					@SuppressWarnings("unchecked")
					List<LinkedHashMap<String, String>> genres = (List<LinkedHashMap<String, String>>)data.get(GENRE);
					StringBuffer genreStr = new StringBuffer();
					
					for (int i=0;i<genres.size();i++) {
						genreStr.append(genres.get(i).get(DESC)).append(", ");
					}
					
					detail.setGenre(genreStr.substring(0, genreStr.length()-2));
					
					@SuppressWarnings("unchecked")
					List<LinkedHashMap<String, String>> categories = (List<LinkedHashMap<String, String>>)data.get(CATEGORIES);
					StringBuffer categoryStr = new StringBuffer();
					
					for (int i=0;i<categories.size();i++) {
						categoryStr.append(categories.get(i).get(DESC)).append(", ");
					}
					
					detail.setCategory(categoryStr.substring(0, categoryStr.length()-2));
					
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> scoreInfo = (LinkedHashMap<String, Object>)data.get(METACRITIC);
					if (scoreInfo != null) {
						Integer score = (Integer)scoreInfo.get(SCORE);
						
						detail.setScore(score+"/100");
					} else {
						detail.setScore("N/A");
					}
				}
				
				details.add(detail);
			}
		}
		
		return details;
	}

}
