package intentmedia;


import java.io.IOException;

/* 
 * Runs the test cases provided in the email
 */

public class TerminalDriver {

	public static void main(String[] args) {
		String inputFileName  = Utilities.getResourceFN("res/listprice.txt");
		String inputTest[]={"ABCDABAA","CCCCCCC","ABCD"};
		String outputTest[] = {"32.40","7.25","15.40"};

		Terminal terminal = new Terminal();
		try {
			terminal.setPricing(inputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}


		for (Integer i = 0; i < inputTest.length; i++){

			char [] chars=inputTest[i].toCharArray();
			for (Integer j=0;j<chars.length;j++) {	
				String str=Character.toString(chars[j]);
				terminal.scan(str);

			}
			System.out.println("Total for cart: " + inputTest[i] +" is $" +terminal.total.toPlainString());
			if ( outputTest[i].equals(terminal.total.toPlainString())) {
				System.out.println("TEST PASSED");
			}
			else {
				System.out.println("TEST FAILED");
			}
			terminal.resetTerminal();
		}
	}

}
