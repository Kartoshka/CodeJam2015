import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {
	
	static Patient[] patients = new Patient[166];
	static int index = 0;

	public static void main(String[] args) throws FileNotFoundException {
		
		loadPatients();
		
		KNNClassifier classifier = new KNNClassifier(10);
		classifier.train(patients);
		
		int good = 0, bad = 0; 
		for(int i = 0; i < patients.length; i++) {
			if(patients[i].resistant == classifier.classify(patients[i]))
				good++;
			else
				bad++;
		}
		
		System.out.println("Goods: " + good + ". Bads: " + bad + ". Accuracy: " + ((double)good/(good+bad) * 100) + "%");
	}

	public static void loadPatients() throws FileNotFoundException {

		File file = new File("trainingData.txt");

		Scanner in = new Scanner(file);
		in.nextLine(); // Skip the first two lines
		in.nextLine();

		String[][] input = new String[166][269];

		for (int i = 0; i < input.length; i++) {
			input[i] = in.nextLine().split("\\s");

			int id = Integer.parseInt(input[i][0].substring(input[i][0].length() - 3, input[i][0].length()));
			float[] finalInput = new float[input[i].length - 4];
			boolean resistant = false;
			double remissionDuration = 0;
			double survival = 0; // Survival time

			for (int j = 0; j < input[i].length - 1; j++) {
				char a = input[i][j].charAt(0);
				if (j < 265 && '0' <= a && a <= '9')
					finalInput[j] = Float.parseFloat(input[i][j]);
				else {
					switch (j) {
					case 0:
						finalInput[j] = (input[i][j + 1].charAt(0) == 'F') ? 1 : 0;
						break;
					case 3:
					case 4:
					case 5:
					case 6:
						char c = input[i][j + 1].charAt(0);
						if (c == 'Y')
							finalInput[j] = 1;
						else if (c == 'N')
							finalInput[j] = 0;
						else // TODO NotDone (DNE)
							finalInput[j] = 0;
						break;
					case 7:
					case 8:
					case 9:
						c = input[i][j + 1].charAt(1);
						if (c == 'O')
							finalInput[j] = 1;
						else if (c == 'E')
							finalInput[j] = 0;
						else // TODO NotDone (DNE)
							finalInput[j] = 0;
						break;
					case 10:
						c = input[i][j + 1].charAt(0); // TODO shouldnt be
														// values like 1, 2, 3,
														// 4
						switch (c) {
						case 'A':
							finalInput[j] = 1;
							break;
						case 'S':
							finalInput[j] = 2;
							break;
						case 'F':
							finalInput[j] = 3;
							break;
						case 'H':
							finalInput[j] = 4;
							break;
						}
						break;
					case 265:
						resistant = (input[i][j + 1].charAt(0) == 'R') ? true : false;
						break;
					case 266:
						if (input[i][j + 1].charAt(0) == 'N')
							remissionDuration = -1;
						else
							remissionDuration = Double.parseDouble(input[i][j + 1]);
						break;
					case 267:
						survival = Double.parseDouble(input[i][j + 1]);
						break;
					}
				}
			}

			patients[index++] = new Patient(id, finalInput, resistant, remissionDuration, survival);
		}
		
		in.close();
	}

}
