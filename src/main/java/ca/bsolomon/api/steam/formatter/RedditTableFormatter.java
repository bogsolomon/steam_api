package ca.bsolomon.api.steam.formatter;

import java.util.List;

import ca.bsolomon.api.steam.dao.AppDetails;

public class RedditTableFormatter {

	private static String streamAppURL = "http://store.steampowered.com/app/"; 
	private static String headerRow = "|Game Name|Genre|Release Date|Players|Meta Score|Price";
	private static String alignmentRow = "|:-:|:-:|:-:|:-:|:-:|:-:";

	public static String formatTable(List<AppDetails> details) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(headerRow);
		buffer.append("\n");
		buffer.append(alignmentRow);
		buffer.append("\n");
		
		for (AppDetails detail:details) {
			if (detail.isSuccess()) {
				buffer.append("|");
				buffer.append("["+detail.getName()+"]("+streamAppURL+detail.getAppId()+")");
				buffer.append("|");
				buffer.append(detail.getGenre());
				buffer.append("|");
				buffer.append(detail.getReleaseDate());
				buffer.append("|");
				buffer.append(detail.getCategory());
				buffer.append("|");
				buffer.append(detail.getScore());
				buffer.append("|");
				buffer.append("$"+detail.getPrice());
				buffer.append("\n");
			} else {
				buffer.append("|");
				buffer.append(detail.getAppId());
				buffer.append("|");
				buffer.append("FAILED");
				buffer.append("|");
				buffer.append("FAILED");
				buffer.append("|");
				buffer.append("FAILED");
				buffer.append("|");
				buffer.append("FAILED");
				buffer.append("|");
				buffer.append("FAILED");
				buffer.append("\n");
			}
		}
		
		return buffer.toString();
	}
}
