package intentmedia;

import java.math.BigDecimal;

/* Price specification. 
 * This class contains the price information about items. 
 * singleUnitCost is cost of one item. 
 * comboQuantity and comboPrice refer to  the price for a group (6pack beer, 4 pack of yogurt etc). 
 *  
 * BigDecimal class was chosen to represent prices based on suggestions on the web for 
 * handling monetary values in Java. See for example:
 * http://stackoverflow.com/questions/285680/representing-monetary-values-in-java
 *
 */

public class PriceSpec{
	public BigDecimal singleUnitCost;
	public Integer	comboQuantity;
	public BigDecimal comboPrice;

	public PriceSpec(BigDecimal singleunitcost){
		singleUnitCost = singleunitcost;
		comboQuantity = 0;
		comboPrice = BigDecimal.ZERO;

	}

	public PriceSpec(BigDecimal suc,Integer cq,BigDecimal cp){
		singleUnitCost = suc;
		comboQuantity = cq;
		comboPrice = cp;			
	}

	public Boolean checkValid(){
		BigDecimal t  = singleUnitCost.multiply(new BigDecimal(comboQuantity));
		return comboPrice.compareTo(t) < 0;
	}

	public String toString(){
		String str= singleUnitCost.toPlainString();
		if (comboQuantity > 0 ){
			str += " " + comboQuantity.toString() + " "  + comboPrice.toPlainString();
		}
		return str;

	}
}

