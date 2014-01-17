package ca.bsolomon.api.steam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.bsolomon.api.steam.SteamAPI;
import ca.bsolomon.api.steam.dao.AppDetails;
import ca.bsolomon.api.steam.formatter.RedditTableFormatter;

public class SteamAPITest {

	@Test
	public void testSingleData() {
		List<String> appIds = new ArrayList<>();
		appIds.add("245050");
		List<AppDetails> appDetails = SteamAPI.getAppDetails(appIds);
		
		assertTrue("Single result", appDetails.size() == 1);
		assertTrue("Success result", appDetails.get(0).isSuccess());
	}
	
	@Test
	public void testMultipleData() {
		List<String> appIds = new ArrayList<>();
		appIds.add("245050");
		appIds.add("225340");
		List<AppDetails> appDetails = SteamAPI.getAppDetails(appIds);
		
		assertTrue("Single result", appDetails.size() == 2);
		assertTrue("Success result", appDetails.get(0).isSuccess());
		assertTrue("Success result 2", appDetails.get(1).isSuccess());
	}

	@Test
	public void testFailedData() {
		List<String> appIds = new ArrayList<>();
		appIds.add("200000");
		appIds.add("225340");
		List<AppDetails> appDetails = SteamAPI.getAppDetails(appIds);
		
		assertTrue("Single result", appDetails.size() == 2);
		assertTrue("Fail result returned", !appDetails.get(0).isSuccess());
		assertTrue("Success result returned", appDetails.get(1).isSuccess());
	}
	
	@Test
	public void testFormatter() {
		List<String> appIds = new ArrayList<>();
		appIds.add("200000");
		appIds.add("225340");
		List<AppDetails> appDetails = SteamAPI.getAppDetails(appIds);
		String formattedString = RedditTableFormatter.formatTable(appDetails);
		
		assertTrue("Formatted String", formattedString.length() != 0);
	}

}
