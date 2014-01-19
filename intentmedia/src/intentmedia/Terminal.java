package intentmedia;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class Terminal {
	public BigDecimal total;
	private ListPrice listPrice;
	private Map<String,Integer> cart;
	private Boolean errorState;
	private static Logger log;

	public Terminal(){
		log=Logger.getLogger("TerminalLog");
		cart=new HashMap<String,Integer>();
		total=BigDecimal.ZERO;
		resetError();
	}


	public void setPricing(String priceListFN) throws IOException{
		listPrice = new ListPrice();
		try {
			listPrice.loadListPrice(priceListFN);
		}
		catch (IOException ioe){
			log.severe("Error reading file: "+ioe.getMessage());
			throw ioe;
		}
	}

	public void resetError(){
		errorState=false;
	}

	public Boolean isGood(){
		return !errorState;
	}

	public void scan(String itemName){
		if (errorState){
			log.warning("Attempt to scan while in error state:");
			return;
		}
		if (listPrice.hasItem(itemName)){
			Integer itemQuantity = 0;
			if (cart.containsKey(itemName)) {
				itemQuantity = cart.get(itemName);
			}
			itemQuantity += 1;
			cart.put(itemName, itemQuantity);
			updateTotal(itemName);
		}
		else {
			log.warning("Item not in the price list: "+ itemName);
			log.warning("You need to call resetError to continue scanning: ");
			errorState=true;
		}

	}
	
	private void updateTotal(String itemName){

		PriceSpec priceSpec = listPrice.getPriceSpec(itemName);
		if ((priceSpec.comboQuantity!=0) && (cart.get(itemName) % priceSpec.comboQuantity)==0){

			BigDecimal tosubtract = priceSpec.singleUnitCost.multiply(new BigDecimal(priceSpec.comboQuantity-1));
			total = total.subtract(tosubtract);
			total = total.add(priceSpec.comboPrice);
		}
		else {
			total = total.add(priceSpec.singleUnitCost);
		}
	}

	public void resetTerminal(){
		total = BigDecimal.ZERO;
		cart = new HashMap<String,Integer>();
		resetError();
	}
	
}