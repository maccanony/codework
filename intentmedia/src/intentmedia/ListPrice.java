package intentmedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;

/*
* Class to hold the list of prices(PriceSpec objects). 
* Can read/write list from file.
* File format allowed is the following:
* ItemName SingleQuantityPrice ComboQuantity(Optional) ComboPrice(if and only if ComboQuantity is present)
* Ex. A $1
* Ex. B $0.5 4 $1.5
* Ex. C 0.5
* It is allowed not to have $ before the pricing
* 
* comboQuantity and comboPrice are set to zero in the following cases:
*  1. values are not given by the user
*  2. the comboPrice is greater than singleUnitCost*comboQuantity
* 
*/

public class ListPrice {

	private Map<String, PriceSpec> prices;
	private static Logger log;


	public ListPrice(){
		prices = new HashMap<String, PriceSpec>();
		log=Logger.getLogger("PriceListLog");
	}

	private String stripDollarSign(String s){
		return s.replace("$","");
	}
	//Check if itemName has a price entry
	public Boolean hasItem(String itemName){
		return prices.containsKey(itemName);
	}
	// Return price for itemName. null if no price available
	public PriceSpec getPriceSpec(String itemName){
		return prices.get(itemName);
	}

	private BigDecimal convertToBigDecimal(String s){	
		return new BigDecimal(stripDollarSign(s));
	}

	public void loadListPrice(String listPriceFN) throws IOException{
		try {
			BufferedReader br = new BufferedReader(new FileReader(listPriceFN));
			String line;

			while((line = br.readLine()) != null) {
				addListPrice(line); 
			}
			br.close();
		}
		catch (IOException ioe){
			log.severe("Not able to read file:" + listPriceFN);

			throw ioe;
		}
	}

	// Save price list to file with optional sorting by itemName
	public void saveListPrice(String listPriceFN) throws IOException{
		saveListPrice(listPriceFN, false);
	}

	public void saveListPrice(String listPriceFN,Boolean saveSorted) throws IOException{
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(listPriceFN), "utf-8"));
			String [] keyAr =(String[]) prices.keySet().toArray(new String[prices.keySet().size()]);
			if (saveSorted) 
			{
				Arrays.sort(keyAr);
			}
			for (Integer i = 0; i < keyAr.length ;i++){
				String itemName = keyAr[i];
				PriceSpec priceSpec = prices.get(itemName);
				writer.write(itemName + " " + priceSpec.toString()+"\n");

			}
		} catch (IOException ioe) {
			// report
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
	}

/* Add a item to the pricelist from a string. The format of the string is same 
 * as format of entries in priceList file 
 * comboQuantity and comboPrice are set to zero in the following cases:
 
		*  1. values are not given by the user
		*  2. the comboPrice is greater than singleUnitCost*comboQuantity
*/	
	public Boolean addListPrice(String strPrice){
		String[] tokens = strPrice.split("\\s+");
		String itemName;
		PriceSpec priceSpec;

		if (tokens.length == 2 || tokens.length == 4) {
			try {
				itemName = tokens[0];
				if (tokens.length == 2) {
					priceSpec = new PriceSpec(convertToBigDecimal(tokens[1]));
				}
				else {
					priceSpec = new PriceSpec(convertToBigDecimal(tokens[1]),Integer.parseInt(tokens[2]),convertToBigDecimal(tokens[3]));
					if (!priceSpec.checkValid()) {
						priceSpec.comboQuantity = 0;
						priceSpec.comboPrice = BigDecimal.ZERO;
						log.info("Ignoring Combo Price since SingleUnitPrice is cheaper: "+ strPrice);
					}
				}
			}
			catch (NumberFormatException e) {
				log.warning("Skipping non conforming line: " + strPrice);
				return false;
			}
			return addListPrice(itemName,priceSpec);
		}
		else {
			log.warning("Skipping non conforming line: "+ strPrice);
			return false;
		}
	}


	public Boolean addListPrice(String itemName,PriceSpec priceSpec,Boolean overridePriceCombo){

		if (!overridePriceCombo && prices.containsKey(itemName)){

			log.warning("Skipping duplicate item: " + itemName);
			return false;
		}
		else {
			prices.put(itemName, priceSpec);
			log.info("Added price: " + itemName+" "+priceSpec.toString());
			return true;
		}

	}
	public Boolean addListPrice(String itemName,PriceSpec priceSpec){
		return addListPrice(itemName,priceSpec,false);
	}

	public Boolean updateListPrice(String itemName,PriceSpec priceSpec){
		return addListPrice(itemName,priceSpec,true);
	}


}
