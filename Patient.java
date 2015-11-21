
public class Patient {
	
	public int ID;
	public boolean sex; //False: Male True: Female
	public float[] testResults; //269 data results
	
	public Patient(int ID, String Sex, float[] tests){
		
		this.ID =ID;
		sex = Sex.equalsIgnoreCase("female");
		testResults = tests;
	}

}
