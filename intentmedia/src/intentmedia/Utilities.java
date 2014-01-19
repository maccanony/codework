package intentmedia;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
 * Simple utility class with two functions.
 * 
 */

public class Utilities {
// Get the pathname to files included in the package for testing and TerminalDriver.
	static public String getResourceFN(String fileName){
		String currDirName = intentmedia.Utilities.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		//fileName = currDirName.replaceAll("\\/bin\\/$", "\\/") + fileName;
		fileName = currDirName.replaceAll("\\/intentmedia.jar", "\\/") + fileName;
		return fileName;
	}

/* Utility function to get Md5 checksum of a file. Md5 sums of saved pricelist
    is compared against a reference in the test suite
*/
	static public String getMessageDigest(String fn) throws IOException {
		try {

			InputStream is = new FileInputStream(fn);
			BufferedInputStream bis = new BufferedInputStream(is);

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			DigestInputStream ids = new DigestInputStream(bis, md);
			while (ids.read() != -1);
			ids.close();
			return Arrays.toString(md.digest());
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new String();
	}

}
