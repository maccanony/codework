package intentmedia;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/*
* JUnit test for terminal
* 1. Perform a test to check that the given scans produce the correct output total
* 2. Perform a test to verify that the terminal goes into a error state when scanning 
*    an unknown item 
*/

public class TerminalTest {
	String inputFileName;
	Terminal terminal;


	@Before
	public void setUp() {
		inputFileName = Utilities.getResourceFN("res/listprice.txt");
		terminal = new Terminal();
		setPricing();
	}

	private void setPricing(){

		try {
			terminal.resetTerminal();
			terminal.setPricing(inputFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTerminal() {
		assertEquals(terminal.total.toPlainString(),BigDecimal.ZERO.toPlainString());

	}

	@Test
	public void testTerminalBasic() {
		String inputTest[]={"ABCDABAA","CCCCCCC","ABCD"};
		String outputTest[] = {"32.40","7.25","15.40"};
		terminal.resetTerminal();
		for (Integer i = 0; i < inputTest.length; i++){

			char [] chars=inputTest[i].toCharArray();
			for (Integer j=0;j<chars.length;j++) {	
				String str=Character.toString(chars[j]);
				terminal.scan(str);

			}
			assertEquals(terminal.total.toPlainString(),outputTest[i]);
			terminal.resetTerminal();
		}

	}

	@Test
	public void testTerminalResetError() {
		terminal.resetTerminal();
		terminal.scan("X");
		terminal.scan("A");
		assertFalse(terminal.isGood());
		terminal.resetError();
		terminal.scan("A");
		assertTrue(terminal.isGood());
	}
}
