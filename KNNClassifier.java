import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class KNNClassifier{
	

	
	Patient[] trainData;
	private int kNeighbours;
	public LinkedList<Integer> relevantDataIndex = new LinkedList();
	public int classNum;
	private int separations =5;
	
	//Set how many neighbours to pick
	public KNNClassifier(int k)
	{
		kNeighbours = k;
	}
	//Take in 
	public void train(Patient[] trainingPatients, int classNum)
	{
		this.classNum = classNum;
		trainData = trainingPatients;	
		if(classNum ==0)
			classifyUsefulData(trainingPatients);
		else
			classifyUsefulDataContinuous(trainingPatients);
	}
	
	public double classify(Patient p){
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
			Iterator<Integer> relevantIndex =relevantDataIndex.iterator();
			while(relevantIndex.hasNext())
			{
				int indexUse = relevantIndex.next();
				distance+= Math.pow((train.testResults[indexUse] - p.testResults[indexUse]),2);
			}
			/*
			for(int data =0; data<train.testResults.length;data++){
				distance += Math.pow((train.testResults[data] - p.testResults[data]),2);
			}*/
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

	public void classifyUsefulData(Patient[] train)
	{
		//We split the patients into two classes
		HashMap<Boolean, LinkedList<Patient>> resistanceMap = new HashMap();
		resistanceMap.put(true,new LinkedList<Patient>());
		resistanceMap.put(false,new LinkedList<Patient>());
		for(Patient p: train)
		{
				resistanceMap.get(p.getClass(0)>0).push(p);
		}
		float[][] avgs = new float[2][train[0].testResults.length];
		float[][] variance = new float[2][avgs[0].length];
		float[] maxValue = new float[avgs[0].length];
		for(int d =0; d<maxValue.length;d++)
		{
			maxValue[d] = Float.MIN_VALUE;
		}
		for(int d =0; d<maxValue.length;d++)
		{
			for(Patient p:train)
				if(p.testResults[d] > maxValue[d])
					maxValue[d] = p.testResults[d];
		}

		for(int c=0;c<2;c++){
		//We calculate the standard deviation of all data types for each class and pick small ones
		//Calculate average
		
		for(int i=0; i<avgs[c].length;i++)
		{
			avgs[c][i] =0;
		}
		
		for(int d=0; d<avgs[c].length;d++)
		{
			for(Patient p:resistanceMap.get(c==1))
			{
				avgs[c][d] += p.testResults[d];
			}
			avgs[c][d] /= train.length;
		}
		//Calculate variance
		for(int i=0; i<avgs[c].length;i++)
		{
			variance[c][i] =0;
		}
		for(int d=0; d<avgs[c].length;d++)
		{
			for(Patient p:resistanceMap.get(c==1))
			{
				variance[c][d] += Math.pow((p.testResults[d]-avgs[c][d]),2);
			}
			variance[c][d] /= train.length;
			variance[c][d] = (float)Math.sqrt(variance[c][d]);
			variance[c][d] /= maxValue[d];
		}
		
		}
		
		for(int i=0; i<variance[0].length;i++)
		{
			if(variance[0][i] <=0.15 && variance[1][i] <=0.15)
			{
				relevantDataIndex.push(i);
			}
		}
	}
	
	public void classifyUsefulDataContinuous(Patient[] train)
	{
		//We split the patients into two classes
		HashMap<Double, LinkedList<Patient>> resultMap = new HashMap();
		
		float[][] avgs = new float[separations][train[0].testResults.length];
		float[][] variance = new float[separations][avgs[0].length];
		float[] maxValue = new float[avgs[0].length];
		double maxClass =Float.MIN_VALUE;
		
		
		
		for(int d=0; d<train.length;d++){
			if(train[d].getClass(classNum)>maxClass)
				maxClass = (float)train[d].getClass(classNum);
		}
		double delta = Math.round(maxClass/separations);
		for(int s=0; s<separations;s++){
			resultMap.put(delta*(s+1), new LinkedList<Patient>());
		}
		
		for(int d =0; d<maxValue.length;d++){
			maxValue[d] = Float.MIN_VALUE;
		}
		for(int d =0; d<maxValue.length;d++){
			for(Patient p:train)
				if(p.testResults[d] > maxValue[d])
					maxValue[d] = p.testResults[d];
		}
		
		
		
		for(Patient p: train){
			double distance = p.getClass(classNum);
			int index =(int)(distance/(delta));
			if(index ==0)
				resultMap.get(delta).push(p);
			else
				resultMap.get(index*(delta)).push(p);
		}
		
		

		for(int c=0;c<separations;c++){
		//We calculate the standard deviation of all data types for each class and pick small ones
		//Calculate average
		
		for(int i=0; i<avgs[c].length;i++)
		{
			avgs[c][i] =0;
		}
		
		for(int d=0; d<avgs[c].length;d++)
		{
			for(Patient p:resultMap.get((c+1)*(delta)))
			{
				avgs[c][d] += p.testResults[d];
			}
			avgs[c][d] /= train.length;
		}
		//Calculate variance
		for(int i=0; i<avgs[c].length;i++)
		{
			variance[c][i] =0;
		}
		for(int d=0; d<avgs[c].length;d++)
		{
			for(Patient p:resultMap.get((c+1)*(delta)))
			{
				variance[c][d] += Math.pow((p.testResults[d]-avgs[c][d]),2);
			}
			variance[c][d] /= train.length;
			variance[c][d] = (float)Math.sqrt(variance[c][d]);
			variance[c][d] /= maxValue[d];
		}
		
		}
		
		for(int i=0; i<variance[0].length;i++)
		{
			if(variance[0][i] <=0.15 && variance[1][i] <=0.15)
			{
				relevantDataIndex.push(i);
			}
		}
	}
}
