import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestClass {

	public static void main(String[] args) throws FileNotFoundException {
		
		
		Patient[] trainPatients = loadPatients(new File("trainingData.txt"));
		
		//KNNClassifier classifier = new KNNClassifier(3);
		//classifier.train(trainPatients);
		
		LinearClassifierMatrix classifier = new LinearClassifierMatrix();
		classifier.train(trainPatients, 0);
		
		/*Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			String s = "";
			Patient p = loadPatient(in.nextLine(), false);
			
			s += "train_id_" + p.ID + "\t";
			
			double a = classifier.classify(p);
			if(a > 0)
				s += "RESISTANT\t";
			else
				s += "COMPLETE_REMISSION\t";
			
			//s += classifier.classify(p, 1) + "\n";
			//s += classifier.classify(p, 2);
			
			System.out.println(s);
		}
		in.close();*/
		
	
		
		int good = 0, bad = 0; 
		for(int i = 0; i < trainPatients.length; i++) {
			if(trainPatients[i].resistant < 0 ^ classifier.classify(trainPatients[i], 0) > 0)
				good++;
			else
				bad++;
			//System.out.println(trainPatients[i]);
		}
		
		System.out.println("Goods: " + good + ". Bads: " + bad + ". Accuracy: " + ((double)good/(good+bad) * 100) + "%");
		
		
		//classifier.classifyUsefulData(trainPatients);
		//System.out.println(classifier.relevantDataIndex.get(2));
	}

	public static Patient[] loadPatients(File file) throws FileNotFoundException {

		Scanner in = new Scanner(file);
		in.nextLine(); // Skip the first two lines
		in.nextLine();

		Patient[] result = new Patient[166];

		for (int i = 0; i < result.length; i++) {
			result[i] = loadPatient(in.nextLine(), true);
		}
		
		in.close();
		
		return result;
	}
	
	public static Patient loadPatient(String line, boolean isTrain) {
		String[] input = line.split("\\s");

		int id = Integer.parseInt(input[0].substring(input[0].length() - 3, input[0].length()));
		float[] finalInput = new float[input.length - ((isTrain)? 4: 1)];
		double resistant = 0;
		double remissionDuration = 0;
		double survival = 0; // Survival time

		for (int j = 1; j < input.length; j++) {
			if(input[j].length() == 0)
				continue;
			else if (j <= finalInput.length && '0' <= input[j].charAt(0) && input[j].charAt(0) <= '9')
				finalInput[j - 1] = Float.parseFloat(input[j]);
			else {
				switch (j - 1) {
				case 0:
					finalInput[j - 1] = (input[j].charAt(0) == 'F') ? 1 : 0;
					break;
				case 3:
				case 4:
				case 5:
				case 6:
					char c = input[j].charAt(0);
					if (c == 'Y')
						finalInput[j - 1] = 1;
					else if (c == 'N')
						finalInput[j - 1] = 0;
					else // TODO NotDone (DNE)
						finalInput[j - 1] = 0;
					break;
				case 7:
				case 8:
				case 9:
					c = input[j].charAt(1);
					if (c == 'O')
						finalInput[j - 1] = 1;
					else if (c == 'E')
						finalInput[j - 1] = 0;
					else // TODO NotDone (DNE)
						finalInput[j - 1] = 0;
					break;
				case 10:
					c = input[j].charAt(0); // TODO shouldnt be values like 1, 2, 3, 4
					switch (c) {
					case 'A':
						finalInput[j - 1] = 1;
						break;
					case 'S':
						finalInput[j - 1] = 2;
						break;
					case 'F':
						finalInput[j - 1] = 3;
						break;
					case 'H':
						finalInput[j - 1] = 4;
						break;
					}
					break;
				case 265:
					resistant = (input[j].charAt(0) == 'R') ? 1 : -1;
					break;
				case 266:
					if (input[j].charAt(0) == 'N')
						remissionDuration = -1;
					else
						remissionDuration = Double.parseDouble(input[j]);
					break;
				case 267:
					survival = Double.parseDouble(input[j]);
					break;
				}
			}
		}

		return new Patient(id, finalInput, resistant, remissionDuration, survival);
	}

}
