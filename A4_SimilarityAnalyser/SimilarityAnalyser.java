/* ------------------------------------------------------------ */

/*      Written by Aditya Singgih for COMP9024 Assignment 4     */

/* ------------------------------------------------------------ */


import java.util.*;
import java.io.*;


public class SimilarityAnalyser{

	private static String readFile(String f){										// a method for reading file
		String line; 
		String word = "";

		try{
			
			FileReader file = new FileReader(f);
			BufferedReader bufRead = new BufferedReader(file);
															
			while ((line = bufRead.readLine()) != null){

				if (line.isEmpty()){												// skip empty lines
					continue;
				}
				else {
					word += line;													// append the word in line into the variable word
				}
			}
		}

		catch(FileNotFoundException e){
			System.out.println("Cannot locate the file");
			// return;
		}

		catch(IOException e){
			System.out.println("Cannot open the file");
			// return;
		}

		return word;
	}


	/*--------------------------------------------------------------------------------

			Q3. similarity Analyser ---> O(mn)
	
			This method takes O(mn) time complexity where m = size of word f1,
			and n = size of word f2. 

			Creating both axises using the 2 for loops the matrix depends
			on the size of the words

			filling the matrix where a nested loop section occured takes
			O(nm) time complexity. this is because the outer loop runs
			m times, and the inner loop runs n times.

			the getLCS method takes O(n+m). This method retrieves the longest
			common subsequence from 2 given strings.
	
	---------------------------------------------------------------------------------*/

	public static float similarityAnalyser(String f1, String f2, String f3){					// using the algorithm provided in the lecture notes


		String strX = readFile(f1);
		String strY = readFile(f2);

		int m = strX.length();
		int n = strY.length();

		int[][] L = new int[m+1][n+1];															// matrix storing the length of the subsequence 


		// ------ starts the matrix -------

		for (int i=0; i<m; i++){																// O(m) times												
			L[i][0] = 0;
		}

		for (int j=0; j<n; j++){																// O(n) times
			L[0][j] = 0;
		}

		// --------------------------------

		for (int a=1; a<=m; a++){																// fills the matrix with matched characters in words
			
			for(int b=1; b<=n; b++){
				
				if (strX.charAt(a-1) == strY.charAt(b-1)){										// if match is found, +1 for value at the upper left diagonal
					
					L[a][b] = L[a-1][b-1] + 1;
				
				}
				else{																			// otherwise, take the larger value from the above, or left side
																								// 		of the current location 
					L[a][b] = Math.max(L[a][b-1], L[a-1][b]);
				
				}
			
			}
		
		}

		String lcsResult = getTheLCS(strX, strY, L);											// it obtains the longest common subsequence from both strings

		printToFile(lcsResult, f3);																// outputs the lcs of both strings into a text file. 

		return similarityDegree(lcsResult, strX, strY);

	}

	private static float similarityDegree(String lcs, String string1, String string2){			// a method to calculate the similarity degree
		
		float out;

		out = (float)Math.abs(lcs.length()) / (float)Math.max(Math.abs(string1.length()), Math.abs(string2.length()));

		return out;

	}

	private static void printToFile(String output, String filename){

		if (filename.contains(".txt") == false){												// check if the output filename contains ".txt"
			filename = filename + ".txt";
		}

		File f = new File(filename);
		FileWriter fw = null;

		try{

			if (f.exists() == false){															// check if file exists in the directory
				fw = new FileWriter(filename);													// create new file if it doesn't exist
			}
			else{
				System.out.println("overriding the old " + filename);
				fw = new FileWriter(filename, false);											// rewrite the file, instead of appending to the previous file if exists
			}

			fw.write(output);																	// write the file with the Longest Common Subsequence of f1 and f2

			fw.close();																			// close the FileWriter to save memory

		}
		catch(IOException e){
			e.printStackTrace();
			return;
		}

	}

	private static String getTheLCS(String str1, String str2, int[][] matrix){					// this method retrieves the LCS from the matrix

		Stack<Character> temp = new Stack<>();													// need to use stack to reverse the characters
																								// 		because we read from the rightmost chars.
		int x = str1.length();
		int y = str2.length();
		
		int matrixlength = matrix[x][y];

		int k = matrixlength - 1;

		String out = "";

		if (matrixlength >= 0){
		 	while (x > 0 && y>0){

		 		if (str1.charAt(x-1) == str2.charAt(y-1)){										// condition when match is found
		 			temp.push(str1.charAt(x-1));												// push to the stack
		 			x--;																		
		 			y--;
		 			k--;
		 		}

		 		else if (matrix[x-1][y] > matrix[x][y-1]){
		 			x--;
		 		}

		 		else{
		 			y--;
		 		}
		 	}
		}

		else{

			return null;

		}

		while (temp.empty() == false){															// pop items from the stack, and append it to the 
																								//		variable out
			out += temp.pop();
		}

		return out;

	}

	public static void main(String args[]) throws Exception{       
		        
		System.out.println(SimilarityAnalyser.similarityAnalyser("sample_files/file2.txt", "sample_files/file3.txt", "sample_files/outputFile.txt"));
		
	 }

	
}


