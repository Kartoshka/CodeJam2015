import java.io.*;
import java.util.*;

public class TestClass {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("trainingData.txt");
		
		Scanner in = new Scanner(file);
		
		while(in.hasNextLine())
			System.out.println(in.nextLine().split("\\s").length);

	}

}
