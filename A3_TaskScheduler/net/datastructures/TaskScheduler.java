/* ------------------------------------------------------------ */

/*      Written by Aditya Singgih for COMP9024 Assignment 3     */

/* ------------------------------------------------------------ */


package net.datastructures;

import java.util.*;
import java.io.*;

public class TaskScheduler{

	static void scheduler(String file1, String file2, int m){

	/*------------------------------------------------------------------------------

			Reading the file and inserting to heapPriorityQueue using 
			release time as the key. Inserting 1 item to the heapPriorityQueue
			takes O(log n) time, therefore, inserting the whole tasks into
			the heapPriorityQueue takes O(n log n) time
	
	-------------------------------------------------------------------------------*/
		
		String line, parts[];
		HeapPriorityQueue<Integer, Task> releaseQ = new HeapPriorityQueue<Integer, Task>();		// create a new heapPriorityQueue for appending
																								//		the tasks from target file.
		try{
			
			FileReader file = new FileReader(file1);
			BufferedReader bufRead = new BufferedReader(file);
			int lineCount = 1;																	// counting the number of lines inside the file for clear exception messages
			
			while ((line = bufRead.readLine()) != null){										// run this loop while there is are still lines in the file

				if (line.isEmpty()){															// skip empty lines in the file
					lineCount += 1;
					continue;
				}
				else{	
					parts = line.split(" +");													// split words inside the line using regex, and store in parts
					int i = 0;																	// index of the parts
					int counter = 1;															// item counter for clear exception messages
					
					while (i < parts.length){													// keep looping to the end of the parts
						
						Task task = new Task();													// create new task object
						
						if (parts[i].equals(" ")){												// condition to check if the task attribute is blank											
							
							System.out.println("Input error when reading the attributes of Tasks at line " + lineCount + " in " + file1);
							return;
						
						}

						try{

							task.setTaskName(parts[i]);											// set task name according to the 1st item of the task set (name, releasetime, deadline)
							task.setRelease(Integer.parseInt(parts[i+1]));						// set release time according to the 2nd item
							task.setDeadline(Integer.parseInt(parts[i+2]));						// set deadline time according to the 3rd item
			
							i += 3;																// increase the index by 3 to check the next set
							counter += 1;														// tasks counter 

						}
						catch(Exception e){

							System.out.println("Input error when reading the attributes of item " + counter +" line " + lineCount + " in " + file1);	// checking the input file is easier this way, 
																																						// 	instead of using the task name. e.g,
																																						//	when there is no task name in the input file
							return;

						}

						releaseQ.insert(task.getRelease(), task);								// insert the tasks into the heap priority queue according using release time as the key,
																								//		and the task itself as the value. The whole insertion process for all the tasks
																								//		will take O(n log n) time complexity.
					}

				}

				lineCount += 1;				// line counter increment

			}

			bufRead.close();				// close buffered readoer to save memory

		}

		catch(FileNotFoundException e){
			System.out.println("Cannot locate the file");
			return;
		}

		catch(IOException e){
			System.out.println("Cannot open the file");
			return;
			
		}

	/*------------------------------------------------------------------------------

			The code below removes the tasks with smallest release time 
			from releaseQ, and insert it to a new heap priority queue called
			deadlineQ, using each deadline as the key. removing from releaseQ
			will take O(log n) time per task, and inserting to deadlineQ will
			take another O(log n) time per task. 

			Therefore, the whole removal and insertion process will take 
			O(n log n) time.
	
	-------------------------------------------------------------------------------*/

		HeapPriorityQueue<Integer, Task> deadlineQ = new HeapPriorityQueue<Integer, Task>();	// create a new heapPriorityQueue for appending removed 
																								//		tasks from releaseQ

		ArrayList<String> taskList = new ArrayList<String>();									// new arrayList to contain all the processed tasks later on
		
		int time = 0;																			// start time of the scheduler
		
		while (releaseQ.isEmpty() == false){													

			while (releaseQ.isEmpty() == false && releaseQ.min().getKey() == time){				// while the releaseQ is not empty, and the smallest release time of
																								//		the task is equal to the time of the scheduler
				
				int dead = releaseQ.min().getValue().getDeadline();								// store deadline in dead variable
				deadlineQ.insert(dead, releaseQ.min().getValue());								// insert the task to deadlineQ from the minimum key of releaseQ, 
																								//		using deadline as the key
				releaseQ.removeMin();															// remove the item from releaseQ

			}
			
			int coreAvail = m;																	// available computer cores according to the input
			while (coreAvail > 0 && deadlineQ.isEmpty() == false){								// loop when we still have available core and deadlineQ is not empty

				if (deadlineQ.min().getKey() <= time){											// stop if the deadline of the task that is currently processed is already
																								//		equal or larger of the current scheduler time
					System.out.println("No feasible schdeule exists");
					return;

				}
				else{																	

					String addedTask = "";														// create a string variable for the task set (taskname, processed time)
					String addedTaskName = deadlineQ.min().getValue().getTaskName();	
					addedTask = addedTaskName + " " + time + " ";
					taskList.add(addedTask);													// append task set into the taskList, this is O(n) time
					deadlineQ.removeMin();														// remove the appended task from the deadlineQ, since it has been processed

				}
				coreAvail -= 1;																	// decrease available cores by 1

			}
			time += 1;																			// increase the scheduler time

		}

		printToFile(taskList, file2);															// printing the scheduler into a text file, using printToFile method

	}

	/*------------------------------------------------------------------------------

			This is just a printing method to a file. It takes O(n) time 
			to loop through each of the items and print it to
			the output file
	
	-------------------------------------------------------------------------------*/

	private static void printToFile(ArrayList<String> tasklist, String filename){

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
				fw = new FileWriter(filename, false);											// rewrite the file, instead of appending to the previous file if exists
			}

			for (String stuff : tasklist){
				fw.write(stuff);																// write the tasks into the file
			}

			fw.close();																			// close the FileWriter to save memory

		}
		catch(IOException e){
			e.printStackTrace();
			return;
		}

	}

	public static void main(String[] args) throws Exception{

	    TaskScheduler.scheduler("sample_files/samplefile1.txt", "sample_files/feasibleschedule1", 4);
	   /** There is a feasible schedule on 4 cores */      

	    TaskScheduler.scheduler("sample_files/samplefile1.txt", "sample_files/feasibleschedule2", 3);
	   /** There is no feasible schedule on 3 cores */

	    TaskScheduler.scheduler("sample_files/samplefile2.txt", "sample_files/feasibleschedule3", 5);
	   /** There is a feasible scheduler on 5 cores */ 

	    TaskScheduler.scheduler("sample_files/samplefile2.txt", "sample_files/feasibleschedule4", 4);
	   /** There is no feasible schedule on 4 cores */

	   /** The sample task sets are sorted. You can shuffle the tasks and test your program again */  

  	}	

}

/*------------------------------------------------------------------------------

		Below is the Task class, to make my life easier when working on 
		the scheduler, since the Task object wraps task name, release time,
		and deadline time into 1 object.

-------------------------------------------------------------------------------*/

class Task{
	protected String taskName;
	protected int releaseTime;
	protected int deadline;

	public Task(){			// default empty constructor
	}

	public Task(String taskName, int releaseTime, int deadline){
		this.taskName = taskName;
		this.releaseTime = releaseTime;
		this.deadline = deadline;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskName(){
		return taskName;
	}

	public void setRelease(int releaseTime){
		this.releaseTime = releaseTime;
	}

	public int getRelease(){
		return releaseTime;
	}

	public void setDeadline(int deadline){
		this.deadline = deadline;
	}

	public int getDeadline(){
		return deadline;
	}

}


