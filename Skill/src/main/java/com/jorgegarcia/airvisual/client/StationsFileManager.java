package com.jorgegarcia.airvisual.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.jorgegarcia.airvisual.model.Station;
import com.jorgegarcia.airvisual.model.StationResult;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class StationsFileManager {
	

	
	public static List<Station> readAllStations(String filepath) throws IllegalStateException, FileNotFoundException  {
		List<Station> stations=null;
		CsvToBeanBuilder withType = new CsvToBeanBuilder(new FileReader(filepath))
			       .withType(Station.class);
		stations = withType.build().parse();
		
		return stations;
	}
	
	public static void writeStationResults(String filepath, List<StationResult> stationResults) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		final String[] CSV_HEADER = { "id", "country", "state", "city","aqiUS","aqiCN","temperature","lastPollutionUpdate","lastWeatherUpdate" };
		
		FileWriter fileWriter = new FileWriter("src/main/resources/stationsresults.csv");
		 
		// write List of Objects
		ColumnPositionMappingStrategy<StationResult> mappingStrategy = 
				new ColumnPositionMappingStrategy<StationResult>();
		
		mappingStrategy.setType(StationResult.class);
		mappingStrategy.setColumnMapping(CSV_HEADER);
		 
		StatefulBeanToCsv<StationResult> beanToCsv = new StatefulBeanToCsvBuilder<StationResult>(fileWriter)
				.withMappingStrategy(mappingStrategy)
		        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
		        .build();
		
		beanToCsv.write(stationResults);
		fileWriter.close();
        
	}
	
	

}
