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
 * The class that builds a Singelton instance of the Config class.
 * This class is called by all other classes to get a reference to 
 * the only instance that represents the configuration file.
 * It only provides a reference to the configuration file in the form
 * of an IConfig interface. Thus the outside world mustn't mind the implementation
 * of how the configuration file is parsed and read.
 * @author Patrick Killeen
 *
 */
public class Configuration implements IConfig{

	private Properties properties;

	private String configFilePath;


	/**
	 * Constructor. {@link Config#init} should be called before using the {@code Object}.
	 * @param configFilePath The file path to the configuration file.
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
	public Configuration(String configFilePath) throws InvalidPropertiesFormatException, IOException {
		super();
		this.configFilePath = configFilePath;
		init();
	}

	public Configuration(){

	}


	public Properties getEntries() {
		return properties;
	}

	public void setEntries(Properties properties) {
		this.properties =properties;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	/**
	 * Returns the value of the entry with a given key. If the entry doesn't exist
	 * an {@code IllegalArgumentException} is thrown.
	 * @param key The key the entry has in the configuration File.
	 * @return The value of the specified entry.
	 */
	public String getPropertyAndValidate(String key){
		String result =  properties.getProperty(key);
		if(result==null){

			throw new RuntimeException("Could not find property: "+key+" in configuration file "+configFilePath);
		}
		return result;
	}

	/**
	 * Returns the value of the entry with a given key.
	 * @param key The key the entry has in the configuration File.
	 * @return The value of the specified entry.
	 */
	public String getProperty(String key){
		String result =  properties.getProperty(key);

		return result;
	}

	public void setProperty(String key,String value){
		properties.setProperty(key,value);
	}

	/**
	 * Initializes this {@code Config} object so entries can be read. 
	 * @throws InvalidPropertiesFormatException Thrown when the configuration file has the wrong format.
	 * @throws IOException Thrown when reading fails.
	 */
	private void init() throws InvalidPropertiesFormatException, IOException{

		File file = new File(configFilePath);

		if(!file.exists()){

			throw new FileNotFoundException("The configuration file couldn't be found: "+configFilePath);

		}

		//load the config file
		FileInputStream fileInput = null;
		try{
			fileInput = new FileInputStream(file);
			properties = new Properties();
			properties.loadFromXML(fileInput);
		}finally{
			try{fileInput.close();}catch(IOException e){};	
		}


	}
	/**
	 * Returns a variable number of entries.
	 * @param key The key that all entries start with. Example {@code "many.values."}.
	 * @return List of values.
	 */
	public List<String> getProperties(String key){
		List<String> keys = getPropertiesKeys(key);
		List<String> values = new ArrayList<String>(keys.size());
		for(String k : keys){
			String value = getProperty(k);
			values.add(value);
		}

		return values;
	}

	/**
	 * parses the list of keys found in the configuration file, using base key:
	 * @param key key The key that all entries start with. Example {@code "many.values."}.
	 * @return List of keys that exists
	 */
	private List<String> getPropertiesKeys(String key){

		List<String> keys = new ArrayList<String>(32);

		int i=0;
		do{
			String tmpKey = key+i;

			if(properties.containsKey(tmpKey)){
				keys.add(tmpKey);
			}else{
				break;
			}
			i++;
		}while(true);

		return keys;
	}

	/**
	 * prases a double from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to double
	 * @return double representation of property's value in configuration file. 
	 */
	public double getDoubleProperty(String key){

		double res = 0;

		String strNumber = this.getPropertyAndValidate(key);

		try{
			res = Double.parseDouble(strNumber);
		}catch(NumberFormatException e){
			throw new RuntimeException("could not parse value of property ("+key+") to double, expected a number but was ("+strNumber+").");
		}


		return res;

	}


	/**
	 * Reads all the double string entries and converts to doubles
	 * @param key the key to property to convert to doubles (see getProperties)
	 * @return doubles of property's value in configuration file. 
	 */
	public List<Double> getDoubleProperties(String key){
		List<String> keys = this.getPropertiesKeys(key);

		List<Double> res = new ArrayList<Double>(keys.size());

		for(String k : keys){
			Double d = this.getDoubleProperty(k);
			res.add(d);
		}

		return res;
	}


	/**
	 * prases an int from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to int
	 * @return integer representation of property's value in configuration file. 
	 */
	public int getIntProperty(String key){

		int res = 0;

		String strNumber = this.getPropertyAndValidate(key);


		try{
			res = Integer.parseInt(strNumber);
		}catch(NumberFormatException e){
			throw new RuntimeException("could not parse value of property ("+key+") to int, expected a number but was ("+strNumber+").");
		}

		return res;

	}

	/**
	 * Reads all the int string entries and converts to integers
	 * @param key the key to property to convert to ints (see getProperties)
	 * @return integers of property's value in configuration file. 
	 */
	public  List<Integer> getIntProperties(String key){
		List<String> keys = this.getPropertiesKeys(key);

		List<Integer> res = new ArrayList<Integer>(keys.size());

		for(String k : keys){
			Integer i = this.getIntProperty(k);
			res.add(i);
		}

		return res;
	}

	
	/**
	 * prases a boolean from the configuration xml properties file of a specific entry.
	 * @param key the key to property to convert to boolean
	 * @return boolean representation of property's value in configuration file. 
	 */
	public boolean getBooleanProperty(String key) {
		boolean res = false;

		String strBool = this.getPropertyAndValidate(key);


		try{
			res = Boolean.parseBoolean(strBool);
		}catch(NumberFormatException e){
			throw new RuntimeException("could not parse value of property ("+key+") to boolean, expected a boolean but was ("+strBool+").");
		}

		return res;

	}

	/**
	 * Loads a configuration object by retrieving the target config file 
	 * path found in this config properties. Then loads the configuration 
	 * file from the path and returns the config object.
	 * @param key the key to target config file path found in this config's properties 
	 * @return target configuration object
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
	public IConfig loadTargetConfigFile(String key){

		String targetConfigFilePath = this.getPropertyAndValidate(key);


		try {
			Configuration res = new Configuration(targetConfigFilePath);
			return res;
		} catch (IOException e) {
			throw new RuntimeException("could not load a configuraiton file from file ("+targetConfigFilePath+") defined by key ("+key+") from config file: "+this.getConfigFilePath());
		}

	}

	/**
	 * Reads all the config file path entries and converts to configuration object
	 * @param key the key to property to convert to ints (see getProperties)
	 * @return configuration files
	 */
	public List<IConfig> loadTargetConfigFiles(String key){
		List<String> keys = this.getPropertiesKeys(key);

		List<IConfig> res = new ArrayList<IConfig>(keys.size());

		for(String k : keys){
			IConfig config = this.loadTargetConfigFile(k);
			res.add(config);
		}

		return res;

	}


	/**
	 * Creates a list of configuration objects by loading all xml files in a directory
	 * @param directory the directory to search for xml files
	 * @return list of configuration objects of xml files
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public  List<IConfig> loadConfigFilesInDirectory(String directory) throws InvalidPropertiesFormatException, IOException{
		
		List<File> xmlFiles = featchAllXMLFiles(directory);
		List<IConfig> res = new ArrayList<IConfig>(xmlFiles.size());
		
		
		//iterate all xml config files
		for(File f : xmlFiles){
				IConfig config = new Configuration(f.getAbsolutePath());
				res.add(config);
		}
		
		return res;
		 
	}
	/**
	 * Searches a directory for xml files and returns file objects of xml files.
	 * @param directory the directory to search
	 * @return xml files
	 */
	private List<File> featchAllXMLFiles(String directory){
		
		
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		
		if(listOfFiles == null){
			throw new RuntimeException("there were no xml files in directory: "+directory);
		}
		List<File> xmlFiles = new ArrayList<File>(listOfFiles.length);
		
		for(int i = 0; i < listOfFiles.length; i++){
			File f =  listOfFiles[i];
			String filename = f.getName();
			if(filename.endsWith(".xml")||filename.endsWith(".XML"))
			{
				xmlFiles.add(f);
			}
		}
		
		return xmlFiles;
	}

}//end config class


