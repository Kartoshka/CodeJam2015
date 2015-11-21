import java.util.Arrays;

public class Patient {
	
	public int ID;
	public float[] testResults; //268 data results

	public double resistant;
	public double remissionDuration;
	public double survival; //Survival time
	
	public Patient(int ID, float[] tests, double resistant, double remissionDuration, double survival){
		this.ID =ID;
		this.testResults = tests;
		this.resistant = resistant;
		this.remissionDuration = remissionDuration;
		this.survival = survival;
	}
	
	public double getClass(int i){
		switch(i){
		case 0:
			return resistant;
		case 1:
			return remissionDuration;
		case 2: 
			return survival;
		default:
			return 0.0;
		}
	}
	
	public String toString() {
		return "ID: " + ID + ": " + Arrays.toString(testResults);
	}

}
