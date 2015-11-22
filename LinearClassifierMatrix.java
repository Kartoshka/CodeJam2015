import java.util.Arrays;

import Jama.Matrix;

public class LinearClassifierMatrix {
	
	//arguments
	private double[][] theta;
	
	
	
	
	//public methods
	public void train(Patient[] data, int category){
		
		//calculate r2 for every result
		
		double[] x = new double[data.length];
		double[] y = new double[data.length];
		double[] r2 = new double[data[0].testResults.length];
		
		//fill y with the right category: 0=resistance, 1=remission duration, 2=survival
		for(int i=0; i<y.length; i++){
			switch(category){
			case 0:
				y[i] = data[i].resistant;
				break;
			case 1:
				y[i] = data[i].remissionDuration;
				break;
			case 2:
				y[i] = data[i].survival;
				break;
			default:
				y[i] = 0;
			}
		}
		
		for(int i=0; i<r2.length; i++){
			//fill x with the corresponding test results
			for(int j=0; j<x.length; j++){
				x[j] = data[j].testResults[i];
			}
			
			r2[i] = linearRegressionR2(x, y);
		}
		
		
		//we can only calculate as many thetas as we have patients, the remaining thetas will be free variables
		//the test results with the lowest r2 do not matter, so they will have a theta of 0
		
		//determine the most relevant results from their r2
		int[] relevantResults = new int[data.length];
		int l=0;
		for(int i=0; i<r2.length; i++){
			int k=0;
			//count how many results have larger r2
			for(int j=0; j<r2.length; j++){
				if(r2[i] < r2[j])
					k++;
			}
			//if this result is in the top (data.length) r2, add it to the relevant results
			if(k<data.length && l<relevantResults.length){
				relevantResults[l] = i;
				l++;
			}
		}
		
		//create the coefficient matrix. The matrix corresponding to the constants has already been made: it is y
		double[][] matrix = new double[data.length][data.length];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				matrix[i][j] = data[i].testResults[relevantResults[j]];
			}
		}
		
		//solve the matrix
		Matrix m = new Matrix(matrix);
		Matrix b = new Matrix(y, y.length);
		double[] solution = (m.solve(b)).getColumnPackedCopy();
		
		//distribute the solution in theta and make the irrelevant thetas 0
		this.theta = new double[3][r2.length];
		int solCounter = 0;
		for(int i=0; i<theta[category].length; i++){
			if(relevantResults[solCounter] == i){
				theta[category][i] = solution[solCounter];
				solCounter++;
			}
		}
		System.out.println("Theta: " + Arrays.toString(theta[category]));
		
	}
	
	
	public double classify(Patient p, int category){
		double result = 0;
		for(int i=0; i<theta[category].length; i++){
			result += theta[category][i] * p.testResults[i];
		}
		return result;
	}
	
	
	
	//private methods
	private static double linearRegressionR2(double[] x, double[] y){
		//find average
		double xbar = 0, ybar = 0;
		
		for(int i=0; i<x.length; i++){
			xbar += x[i];
			ybar += y[i];
		}
		xbar /= x.length;
		ybar /= y.length;
		
		//find formula y=ax+b
		double xxbar = 0, yybar = 0, xybar = 0;
		
		for(int i=0; i<x.length; i++){
			xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		double a = xybar / xxbar;
        double b = ybar - a * xbar;
        
        //calculate the sum of squares
        double ssr = 0;
        for(int i=0; i<x.length; i++){
            double fit = a*x[i] + b;
            ssr += (fit - ybar) * (fit - ybar);
        }
        
        //calculate r2
        double r2 = ssr / yybar;
        return r2;
	}
	
	
	/*
	private static double[] solve(double[][] matrix, double[] vector){
		
		double[] solution = new double[matrix.length];
		double det = determinant(matrix);
		System.out.println("First determinant done.");
		
		for(int i=0; i<matrix.length; i++){
			
			//copy the matrix array
			double[][] newMatrix = new double[matrix.length][matrix.length];
			for(int m=0; m<matrix.length; m++){
				for(int n=0; n<matrix.length; n++){
					newMatrix[m][n] = matrix[m][n];
				}
			}
			//replace the j'th column in matrix by vector
			for(int j=0; j<matrix.length; j++){
				newMatrix[j][i] = vector[j];
			}
			
			//find the i'th solution by Cramer's rule
			solution[i] = determinant(newMatrix) / det;
		}
		
		return solution;
	}
	
	private static double determinant(double[][] matrix){
		//base case
		if(matrix.length == 1)
			return matrix[0][0];
		if(matrix.length == 2)
			return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
		
		//recursion by cofactor expansion
		double det = 0;
		for(int i=0; i<matrix.length; i++){
			
			double[][] newMatrix = new double[matrix.length-1][matrix.length-1];
			
			int m=0;
			for(int j=0; j<matrix.length; j++){
				int n=0;
				if(j == i)
					continue;
				for(int k=1; k<matrix.length; k++){
					newMatrix[n][m] = matrix[k][j];
					n++;
				}
				m++;
			}
			det += matrix[0][i] * determinant(newMatrix) * Math.pow(-1, i);
		}
		
		return det;
	}
	*/
}
