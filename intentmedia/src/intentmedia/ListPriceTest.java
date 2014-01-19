package intentmedia;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/*
* JUnit test for ListPrice
* 1. Checking we correctly save all the item prices to a file
* 2. Checking that correct inputs are stored properly in the ListPrice object
* 3. Checking that ill-formed inputs are not stored in the ListPrice object
* 
* To remember file format allowed is the following:
* ItemName SingleQuantityPrice ComboQuantity(Optional) ComboPrice(Required if ComboQuantity is present)
* Ex. A $1
* Ex. B $0.5 4 $1.5
* Ex. C 0.5
* It is allowed not to have $ before the pricing
* 
*/

public class ListPriceTest {

	String inputFileName;
	String outputFileName; 
	String refFileName; 
	ListPrice listPrice;
	
	@Before
	public void setUp() {
		inputFileName = Utilities.getResourceFN("res/listprice.txt");
		outputFileName = Utilities.getResourceFN("res/listpriceUpdate.txt");
		refFileName = Utilities.getResourceFN("res/listpricessavedref.txt");
		listPrice = new ListPrice(); 

    }

	@Test
	public void testListPriceIO() {
		try {
			listPrice.loadListPrice(inputFileName);
			listPrice.saveListPrice(outputFileName,true);
			String md1 = Utilities.getMessageDigest(refFileName);
			String md2 = Utilities.getMessageDigest(outputFileName);
			assertEquals(md1,md2);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Error");
		}

	}

	@Test
	// 	
	public void testAddListPriceString() {
		String badCases[] = {"A $1.25 3","&&","B $1.25 3 &&","C $1.25 4 $4 &&","&& &&"};
		String goodCases[] = {"A 1.25", "B $0.25 6 $1"};
		ListPrice newListPrice = new ListPrice();
		for (Integer i=0; i < badCases.length; i++){
			assertFalse(newListPrice.addListPrice(badCases[i]));
		}
		for (Integer i=0; i < goodCases.length; i++){
			assertTrue(newListPrice.addListPrice(goodCases[i]));
		}
	}


}
