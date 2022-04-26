package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * All the configuration file keys are defined here as constants. It is
 * an interface to the for read values from the configration xml file. 
 * @author Patrick Killeen
 *
 */
public interface IConfig {
	
	public static final String PROPERTY_YP_INPUT_CSV ="yield-processor.input-csv";
	public static final String PROPERTY_YP_OUTPUT_CSV ="yield-processor.output-csv";
	
	public static final String PROPERTY_YP_CSV_SEPERATOR ="yield-processor.attribute.csv-seperator-character";
	public static final String PROPERTY_YP_TIMESTAMP_IX ="yield-processor.attribute.time-stamp-index";
	public static final String PROPERTY_YP_SPEED_IX ="yield-processor.attribute.speed-index";
	public static final String PROPERTY_YP_WIDTH_IX ="yield-processor.attribute.swath-width-index";
	public static final String PROPERTY_YP_MOISTURE_IX ="yield-processor.attribute.moisture-index";
	public static final String PROPERTY_YP_YIELD_IX ="yield-processor.attribute.yield-index";
	
	public static final String PROPERTY_YP_SEGMENT_DETERMINATION_MODE_SECS_MOD ="yield-processor.harvest-pass.segment-determination-seconds-mode-modifier";
	
	public static final String PROPERTY_YP_NUMBER_SECONDS_HARVEST_FILL_MODE ="yield-processor.harvest-pass-start-end-mode-error.start-fill-number-seconds";
	public static final String PROPERTY_YP_NUMBER_SECONDS_HARVEST_FINISH_MODE ="yield-processor.harvest-pass-start-end-mode-error.finish-fill-number-seconds";	
	public static final String PROPERTY_YP_SHORT_SEGMENT_SIZE ="yield-processor.harvest-pass-start-end-mode-error.short-segment-size";
	
	public static final String PROPERTY_YP_MOISTURE_LOWER_BOUND_THRESHOLD ="yield-processor.harvest-pass-filter-threshold.moisture-lower-bound";
	public static final String PROPERTY_YP_YIELD_UPPER_BOUND_THRESHOLD ="yield-processor.harvest-pass-filter.yield-biological-maximum-upper-bound";
	
	public static final String PROPERTY_YP_SPEED_MOVING_AVG_WINDOW_SIZE ="yield-processor.harvest-pass-filter.speed-moving-average-window-size-in-seconds";
	public static final String PROPERTY_YP_SPEED_FILTER_BOUNDS_MODIFIER ="yield-processor.harvest-pass-filter.speed-boundary-modifier";
	
	public static final String PROPERTY_YP_YIELD_MOVING_AVG_WINDOW_SIZE ="yield-processor.harvest-pass-filter.yield-moving-average-window-size-in-seconds";
	public static final String PROPERTY_YP_YIELD_FILTER_BOUNDS_MODIFIER ="yield-processor.harvest-pass-filter.yield-boundary-modifier";
	
	public static final String PROPERTY_YP_GPS_NOISE_MAX_ERROR ="yield-processor.coordinate-errors.gps-max-noise-offset-error";
	
	public static final String PROPERTY_YP_SPEED_MPH_FLAG ="yield-processor.units.speed-miles-per-hour-flag";
	
	public static final String PROPERTY_YP_OVERLAP_PREVENT_FILTER_IN_PASS_NUM ="yield-processor.overlaps.number-sequential-samples-to-prevent-overlap";
	public static final String PROPERTY_YP_OVERLAP_HARVEST_AREA_SIZE_MODIFIER="yield-processor.overlaps.harvest-area-size-modifier";
	public static final String PROPERTY_YP_OVERLAP_SWATH_WIDTH_FEET_FLAG="yield-processor.overlaps.swath-width-feet-flag";
	
	public static final String PROPERTY_YP_TURNING_ANGLE_EPSILON="yield-processor.turn-detection.max-acceptable-angle-change";
	public static final String PROPERTY_YP_TURNING_ANGLE_MOVING_AVG_WINDOW_SIZE="yield-processor.turn-detection.angle-moving-window-average-size-in-seconds";
	
	
	
	
	public Properties getEntries();

	public void setEntries(Properties properties);

	public String getConfigFilePath();

	public void setConfigFilePath(String configFilePath);
	
	/**
	 * Returns the value of the entry with a given key. If the entry doesn't exist
	 * an {@code IllegalArgumentException} is thrown.
	 * @param key The key the entry has in the configuration File.
	 * @return The value of the specified entry.
	 */
	public String getPropertyAndValidate(String key);
	
	/**
	 * Returns the value of the entry with a given key.
	 * @param key The key the entry has in the configuration File.
	 * @return The value of the specified entry.
	 */
	public String getProperty(String key);
	
	public void setProperty(String key,String value);
	

	/**
	 * Returns a variable number of entries.
	 * @param key The key that all entries start with. Example {@code "many.values."}.
	 * @return List of values.
	 */
	public List<String> getProperties(String key);
	
	/**
	 * prases a double from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to double
	 * @return double representation of property's value in configuration file. 
	 */
	public double getDoubleProperty(String key);
		
	/**
	 * Reads all the double string entries and converts to doubles
	 * @param key the key to property to convert to doubles (see getProperties)
	 * @return doubles of property's value in configuration file. 
	 */
	public List<Double> getDoubleProperties(String key);
	
	
	/**
	 * prases an int from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to int
	 * @return integer representation of property's value in configuration file. 
	 */
	public int getIntProperty(String key);
	
	
	/**
	 * prases a boolean from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to boolean
	 * @return boolean representation of property's value in configuration file. 
	 */
	public boolean getBooleanProperty(String key);
	
	/**
	 * 
	 * Reads all the int string entries and converts to integers
	 * @param key the key to property to convert to ints (see getProperties)
	 * @return integers of property's value in configuration file. 
	 */
	public List<Integer> getIntProperties(String key);
	
	/**
	 * Loads a configuration object by retrieving the target config file 
	 * path found in this config properties. Then loads the configuration 
	 * file from the path and returns the config object.
	 * @param key the key to target config file path found in this config's properties 
	 * @return target configuration object
	 */
	public IConfig loadTargetConfigFile(String key);
	
	
	/**
	 * Reads all the config file path entries and converts to configuration object
	 * @param key the key to property to convert to ints (see getProperties)
	 * @return configuration files
	 */
	public List<IConfig> loadTargetConfigFiles(String key);
	
	
	/**
	 * Creates a list of configuration objects by loading all xml files in a directory
	 * @param directory the directory to search for xml files
	 * @return list of configuration objects of xml files
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public  List<IConfig> loadConfigFilesInDirectory(String directory) throws InvalidPropertiesFormatException, IOException;
}
