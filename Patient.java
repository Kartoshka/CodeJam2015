import java.util.Arrays;

public class Patient {
	
	public int ID;
	public float[] testResults; //268 data results
	
	public boolean resistant;
	public double remissionDuration;
	public double survival; //Survival time
	
	public Patient(int ID, float[] tests, boolean resistant, double remissionDuration, double survival){
		this.ID =ID;
		this.testResults = tests;
		this.resistant = resistant;
		this.remissionDuration = remissionDuration;
		this.survival = survival;
	}
	
	public String toString() {
		return "ID: " + ID + ": " + Arrays.toString(testResults);
	}

}
