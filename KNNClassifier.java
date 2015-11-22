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
	
	public double classify(Patient p,int classNum){
		//Distances in first index, index of that neighbour in second index
		LinkedList<Object[]> distances = new LinkedList<Object[]>();
		
		
		for(int i=0; i<kNeighbours;i++)
		{
			distances.push(new Object[]{Double.MAX_VALUE,null});
		}
		
		for(Patient train: trainData)
		{
			//Calculate distance
			double distance = 0.0;
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
				if(distance<=compareDist){
					insertIndex = i;
				}
			}
			//If we have found a distance that is smaller, add it.
			if(insertIndex!=-1){
				distances.add(insertIndex, new Object[]{distance,train});
				distances.removeLast();
			}		
		}
		
		//Weighted Vote
		double result=0.0;
		double sumRecip = 0;
		for(int i=kNeighbours-1; i>=0;i--){
			Object[] compareArray =distances.get(i);
			double distance = (Double)compareArray[0];
			
			if(distance != 0){
				double recip = 1/distance; //reciprocal of the distance
				result += recip*((Patient)compareArray[1]).getClass(classNum);
				sumRecip += recip;
			}
			else
				return ((Patient)compareArray[1]).getClass(classNum);
		}
		
		return (result / sumRecip);

	}

}
