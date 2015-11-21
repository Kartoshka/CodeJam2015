
public class LinearClassifier2 {

	private static int NUM_PASSES = 10;
	private static float LEARNING_RATE = 0.00000005f;
	
	private float[] theta = new float[265];
	
	
	public void train(Patient[] data, int classNum){
		//initialize theta with 0.5 in every value
		for(int i=0; i<theta.length; i++){
			theta[i] = 0.5f;
		}
		
		int trainSize = data.length;
		double gradient =0;
		double tempTheta=0;
		double dimension = theta.length;
		
		for(int i = 0; i < NUM_PASSES; i++) { //Do it n times to make sure it converged
			for(int j = 0; j < data.length; j++){ //for every patient
				
				double cost = 0;
				
				for(int k = 0; k < theta.length; k++){ //for every test result of this patient
					//For every theta we want to do the following thetaj = thethaj - (theta1*x1+theta2*x2+...+thetan*xn -label)xj
					//We use the xs and label of the training data we're currently using 
					gradient = 0;
					tempTheta = theta[k];
					for(int r2=0; r2<dimension;r2++)
					{
						//thetai*xi added up
						gradient += data[j].testResults[r2]*theta[r2];
					}
					// - yi
					gradient-= data[j].getClass(classNum)*data[j].testResults[j];
                           
					//*xj
					gradient *= data[j].testResults[k];
					//*-learnRate
					gradient *=LEARNING_RATE;
                                            
					gradient /= data.length;
					tempTheta -= gradient;
					theta[k]= (float)tempTheta;
                                            
					
				}
			}
		}
		
	}
	
	
	public float classify(Patient p) {
		float result = 0;
		for(int i = 0; i < p.testResults.length; i++) {
			result += p.testResults[i] * theta[i];
		}
		
		return result;
	}
}
