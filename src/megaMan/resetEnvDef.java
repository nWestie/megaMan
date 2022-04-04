package megaMan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class resetEnvDef {

	public static void main(String[] args) {
		int ct = 0;
		try {
			File envDefFile = new File("src/megaMan/envDef.txt");
			FileOutputStream outFile = new FileOutputStream("src/megaMan/newEnvDef.txt", false);
			Scanner inp = new Scanner(envDefFile);
			PrintWriter out = new PrintWriter(outFile);
			while (inp.hasNextLine()) {
				if(inp.next().equals("B")) {
					
					out.printf("B %d %d\n", inp.nextInt()*Brick.w, inp.nextInt()*Brick.h);
				}
			}
			inp.close();
			out.close();
			
			
		} catch (FileNotFoundException e) {
	    	System.out.println(String.format("Error on line", ct));
	    	e.printStackTrace();
		}
		
	}
}
