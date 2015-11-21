import java.util.HashMap;
import java.util.Map;

public class LinearClassifier {

	private static int NUM_PASSES = 10;
	private static double LEARNING_RATE = 20;
	
	private int dimension;
	private String[] classes;
	private double[] theta;
	
	
	public void train(Patient[] data){
		
		//initialize theta with 0.5 in every value
		for(int i=0; i<theta.length; i++){
			theta[i] = 0.5f;
		}
		
		double cost = 0;
		
		for(int i = 0; i < NUM_PASSES; i++) { //Do it n times to make sure it converged
			for(int j=0; j<data.length; j++){		//for every patient
				for(int k=0; k<data[j].testResults.length; k++){	//for every test result of this patient
					
					double current = theta[k];
					double gradient = 0, tempCost = 0;
					
					//calculate the gradient
					data[j].testResults[k];
					//Jerome pls halp!
					
					gradient /= data.length;
					
					theta[k] = (current - LEARNING_RATE * gradient);
				}
			}
		}
		
	}
	
	
	public float classify(Patient[] data) {
		
	}
}
