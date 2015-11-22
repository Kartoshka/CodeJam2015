
public class LinearClassifier3 {
	private static int NUM_PASSES = 5000; //6
	private static float LEARNING_RATE = 0.15f; //3

	private int dimension;
	private float[] theta; // The Matrix we want to build which contains the unknowns
	
	private float bias = 0.005f;

	public LinearClassifier3(int dimension) {
		this.dimension = dimension;
		theta = new float[dimension]; // Create an empty matrix
	}

	public float classify(float[] data) {
		float result = 0;
		for(int i = 0; i < data.length; i++) {
			result += data[i] * theta[i];
		}
		
		return result + bias;
	}
	
	public void train(float[][] data, float[] labels) { //Stochastic Gradient descent
		
		//Initialize the theta Matrix to some random values
		for(int i = 0; i < dimension; i++) {
			theta[i] = 0.0005f;
		}
		
		double cost = 0;
		
		for(int i = 0; i < NUM_PASSES; i++) { //Do it n times to make sure it converged
			for(int j = 0; j < data.length; j++) { //For all training values
				
				float h = classify(data[j]);
				float currentBias = bias, biasGradient = 0;
				biasGradient = h / data.length;
				bias = currentBias - LEARNING_RATE * biasGradient;
				
				for(int k = 0; k < dimension; k++) { //A gradient per theta
						
					double current = theta[k];
					double gradient = 0, tempCost = 0;
						
					gradient = data[j][k] * h;
					
					tempCost = h;
						
					gradient -= labels[j] * data[j][k];
					tempCost -= labels[j];
					
					//System.out.println(h + " " + tempCost);
						
					gradient /= Math.pow(data.length, 4);
						
					theta[k] = (float) (current - LEARNING_RATE * gradient);
						
					cost += Math.pow(tempCost, 2);
				}
			}
			
			System.out.println("Pass: " + (i + 1) + " of " + NUM_PASSES + ", Cost: " + cost / 1000);
			cost = 0;
		}
	}

	public int getDimension() {
		return dimension;
	}

	public float[] getTheta() {
		return theta;
	}

}
