package megaMan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testTextInp {

	public static void main(String[] args) {
	int ct = 0;
	try {
		File myObj = new File("src/megaMan/genEnv");
		Scanner inp = new Scanner(myObj);
		while (inp.hasNextLine()) {
			ct++;
			switch(inp.next()) {
			case "B":
				System.out.printf("Brick (%d, %d)\n", inp.nextInt(), inp.nextInt());
				break;
			case "G":
				System.out.printf("Goumba(%d, %d, %d)\n", inp.nextInt(), inp.nextInt(), inp.nextInt());
				break;
			default:
				throw new IllegalArgumentException(String.format("Syntax Error on genEnv line %d", ct));
			}
	    }
	    inp.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println(String.format("Error on line", ct));
	    	e.printStackTrace();
	    }
	}

}
