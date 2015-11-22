import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Solution {

public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		
		Patient[] trainPatients = loadPatients(Solution.class.getResourceAsStream("trainingData.txt"), true);
		Patient[] remissionPatients = filterPatients(trainPatients, false);
		Patient[] resistantPatients = filterPatients(trainPatients, true);	
		
		KNNClassifier classifier1 = new KNNClassifier(3);
		classifier1.train(trainPatients);
		
		//Remission patients
		KNNClassifier classifier2Rem = new KNNClassifier(10);
		classifier2Rem.train(remissionPatients);
		//Resistant patients
		KNNClassifier classifier3Rem = new KNNClassifier(10);
		classifier3Rem.train(remissionPatients);
		KNNClassifier classifier3Res = new KNNClassifier(10);
		classifier3Res.train(resistantPatients);
		
		System.out.print("Enter the absolute path to the test file: ");
		Scanner in = new Scanner(System.in);
		Patient[] testPatients = loadPatients(new FileInputStream(new File(in.nextLine())), false);
		
		for(Patient p: testPatients) {
			String s = "";
			s += "train_id_" + p.ID + "\t";
			
			double a = classifier1.classify(p, 0);
			if(a > 0){
				s += "RESISTANT\t";
				s += "NA" + "\t";
				s += (classifier3Res.classify(p, 2)/11.5);
			}
			else
			{
				s += "COMPLETE_REMISSION\t";
				s += (classifier2Rem.classify(p, 1)/5.0) + "\t";
				s += (classifier3Rem.classify(p, 2)/11.5);
				
			}
			
			System.out.println(s);
		}
		
		System.out.println("Press a key to exit...");
		in.next();
		in.close();
		
	}
	private static Patient[] filterPatients(Patient[] trainPatients, boolean b) {
		
		HashMap<Boolean, ArrayList<Patient>> filter = new HashMap<Boolean, ArrayList<Patient>>();
		filter.put(true,new ArrayList<Patient>());
		filter.put(false,new ArrayList<Patient>());
		for(Patient p: trainPatients)
			filter.get(p.getClass(0)>0).add(p);

		return filter.get(b).toArray(new Patient[filter.get(b).size()]);
	}

	public static Patient[] loadPatients(InputStream is, boolean isTrain) throws FileNotFoundException {
		
		Scanner in = new Scanner(is);
		//in.nextLine();
		//in.nextLine();
		
		ArrayList<Patient> result = new ArrayList<Patient>();
		
		while(in.hasNextLine()) {
			String l = in.nextLine();
			if(l.length() > 100)
				result.add(loadPatient(l, isTrain));
		}
		
		in.close();
		return result.toArray(new Patient[result.size()]);
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
