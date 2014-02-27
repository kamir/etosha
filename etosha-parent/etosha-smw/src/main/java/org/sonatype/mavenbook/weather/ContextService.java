package org.sonatype.mavenbook.weather;

import java.io.InputStream;

import org.etosha.model.Weather;

public class ContextService {

	private SMWRetriever yahooRetriever;
	private SMWJsonParser yahooParser;

	public ContextService() {
	}

	public Weather retrieveForecast(String zip) throws Exception {
		// Retrieve Data
		InputStream dataIn = yahooRetriever.retrieve(zip);

		// Parse DataS
		Weather weather = yahooParser.parse(zip, dataIn);

		return weather;
	}

	public SMWRetriever getYahooRetriever() {
		return yahooRetriever;
	}

	public void setYahooRetriever(SMWRetriever yahooRetriever) {
		this.yahooRetriever = yahooRetriever;
	}

	public SMWJsonParser getYahooParser() {
		return yahooParser;
	}

	public void setYahooParser(SMWJsonParser yahooParser) {
		this.yahooParser = yahooParser;
	}

}
