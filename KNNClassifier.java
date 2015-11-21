import java.util.HashMap;
import java.util.LinkedList;

public class KNNClassifier{
	

	
	Patient[] trainData;
	private int kNeighbours;
	
	//Set how many neighbours to pick
	public KNNClassifier(int k)
	{
		kNeighbours = k;
	}
	//Take in 
	public void train(Patient[] trainingPatients)
	{
		trainData = trainingPatients;	
	}
	
	public boolean classify(Patient p){
		//Distances in first index, index of that neighbour in second index
		LinkedList<Object[]> distances = new LinkedList<Object[]>();
		
		
		for(int i=0; i<kNeighbours;i++)
		{
			distances.push(new Object[]{Double.MAX_VALUE,null});
		}
		
		for(Patient train: trainData)
		{
			//Calculate distance
			double distance =0.0;
			
			for(int data =0; data<train.testResults.length;data++){
				distance += Math.pow((train.testResults[data] - p.testResults[data]),2);
			}
			//Index at which the new data should be inserted
			int insertIndex =-1;
			//Compare the distance of the point with all the currently stored nearest neighbours
			//The linked list is sorted in ascending order by the order in which we insert values.
			for(int i=kNeighbours-1; i>=0;i--){
				Object[] compareArray =distances.get(i);
				double compareDist = (double)(compareArray[0]);
				if(distance<compareDist){
					insertIndex = i;
				}
			}
			//If we have found a distance that is smaller, add it.
			if(insertIndex!=-1){
				distances.add(insertIndex, new Object[]{distance,train});
				distances.removeLast();
			}		
		}
		
		HashMap<Boolean, LinkedList<Double>> classes = new HashMap();
		classes.put(Boolean.TRUE,new LinkedList<Double>());
		classes.put(Boolean.FALSE, new LinkedList<Double>());
		
		for(int i=kNeighbours-1; i>=0;i--){
			Object[] compareArray =distances.get(i);
			classes.get(((Patient)compareArray[1]).resistant).add((Double)compareArray[0]);
		}
		return classes.get(true).size() > classes.get(false).size();

	}

}
