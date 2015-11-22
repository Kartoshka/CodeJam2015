
public class LinearClassifier {

	private static int NUM_PASSES = 1000;
	private static float LEARNING_RATE = 0.00000005f;
	
	private float[] theta = new float[265];
	
	
	public void train(Patient[] data){
		//initialize theta with 0.5 in every value
		for(int i=0; i<theta.length; i++){
			theta[i] = 0.5f;
		}
		
		for(int i = 0; i < NUM_PASSES; i++) { //Do it n times to make sure it converged
			double cost = 0;
			for(int j = 0; j < data.length; j++){ //for every patient	
				for(int k = 0; k < theta.length; k++){ //for every test result of this patient
					
					float current = theta[k];
					double gradient = 0;
					
					//calculate the gradient
					gradient = (classify(data[j]) - data[j].resistant) * data[j].testResults[k]; //TODO change data[j].resistant
					
					gradient /= data.length;
					
					cost += Math.pow((classify(data[j]) - data[i].resistant), 2);
					
					System.out.println(gradient + " " + classify(data[j]) + " " + data[j].resistant + " " + data[j].testResults[k]);
					
					//if(!Double.isNaN(gradient))
						//System.out.println(gradient);
					
					theta[k] = (float) (current - LEARNING_RATE * gradient);
				}
			}
			
			//System.out.println(cost);
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
