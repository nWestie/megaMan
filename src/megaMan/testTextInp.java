package megaMan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class testTextInp {

	public static void main(String[] args) {
	int ct = 0;
		try {
			File myObj = new File("src/megaMan/genEnv");
			FileOutputStream outStream = new FileOutputStream(myObj);
			PrintWriter out = new PrintWriter()
			
		} catch (FileNotFoundException e) {
	    	System.out.println(String.format("Error on line", ct));
	    	e.printStackTrace();
	    }
	}

}
