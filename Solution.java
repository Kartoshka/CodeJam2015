import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {

	public static void main(String[] args) throws FileNotFoundException {
		
		Patient[] trainPatients = loadPatients(new File("trainingData.txt"));
		
		KNNClassifier classifier = new KNNClassifier(10);
		classifier.train(trainPatients);
		
		//LinearClassifier classifier = new LinearClassifier();
		//classifier.train(patients);
		
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			String s = "";
			Patient p = loadPatient(in.nextLine());
			
			s += "train_id_" + p.ID + "\t";
			
			double a = classifier.classify(p, 0);
			if(1 - a > 0)
				s += "RESISTANT\t";
			else
				s += "COMPLETE_REMISSION\t";
			
			s += classifier.classify(p, 1) + "\n";
			s += classifier.classify(p, 2);
			
			System.out.println(s);
		}
		in.close();
		
		/*int good = 0, bad = 0; 
		for(int i = 0; i < trainPatients.length; i++) {
			if(trainPatients[i].resistant == classifier.classify(trainPatients[i], 0))
				good++;
			else
				bad++;
		}
		
		System.out.println("Goods: " + good + ". Bads: " + bad + ". Accuracy: " + ((double)good/(good+bad) * 100) + "%");
		*/
	}

	public static Patient[] loadPatients(File file) throws FileNotFoundException {

		Scanner in = new Scanner(file);
		in.nextLine(); // Skip the first two lines
		in.nextLine();

		Patient[] result = new Patient[166];

		for (int i = 0; i < result.length; i++) {
			result[i] = loadPatient(in.nextLine());
		}
		
		in.close();
		
		return result;
	}
	
	public static Patient loadPatient(String line) {
		String[] input = line.split("\\s");

		int id = Integer.parseInt(input[0].substring(input[0].length() - 3, input[0].length()));
		float[] finalInput = new float[input.length - 4];
		double resistant = 0;
		double remissionDuration = 0;
		double survival = 0; // Survival time

		for (int j = 0; j < input.length - 1; j++) {
			char a = input[j].charAt(0);
			if (j < 265 && '0' <= a && a <= '9')
				finalInput[j] = Float.parseFloat(input[j]);
			else {
				switch (j) {
				case 0:
					finalInput[j] = (input[j + 1].charAt(0) == 'F') ? 1 : 0;
					break;
				case 3:
				case 4:
				case 5:
				case 6:
					char c = input[j + 1].charAt(0);
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
					c = input[j + 1].charAt(1);
					if (c == 'O')
						finalInput[j] = 1;
					else if (c == 'E')
						finalInput[j] = 0;
					else // TODO NotDone (DNE)
						finalInput[j] = 0;
					break;
				case 10:
					c = input[j + 1].charAt(0); // TODO shouldnt be
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
					resistant = (input[j + 1].charAt(0) == 'R') ? 1 : -1;
					break;
				case 266:
					if (input[j + 1].charAt(0) == 'N')
						remissionDuration = -1;
					else
						remissionDuration = Double.parseDouble(input[j + 1]);
					break;
				case 267:
					survival = Double.parseDouble(input[j + 1]);
					break;
				}
			}
		}

		return new Patient(id, finalInput, resistant, remissionDuration, survival);
	}

}
