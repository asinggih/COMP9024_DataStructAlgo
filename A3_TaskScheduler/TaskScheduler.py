#!/usr/bin/env python3
from MyPriorityQueue import MyPriorityQueue

class Task:
	'''Task class to wrap name, job release time, and job deadline'''
	
	def __init__(self):						# class constructor
		self.taskName = None;
		self.releaseTime = None;
		self.deadline = None;

class TaskScheduler:
	'''TaskScheduler class to determine whether a set of jobs are feasible, with the given machine core.
	'''
	def __init__(self):
		self.file1 = None;
		self.file2 = None;
		self.cores = None;

	# TODO: implement printing to file, if required
	# def __print(tasklist, filename):
	# 	pass

	def scheduler(self, file1, file2, m):
		# Reading the supplied text file and convert into list
		bigList = [];
		file = open(file1);
		while True:
			line = file.readline();
			if line:
				line = line.rstrip('\n');
				taskList = line.split(' ');
				for item in taskList:
					if item != '':
						bigList.append(item);
			else:
				break;
		file.close();


		# --------------------------------------------------------------------------------

		#		Store tasks into a priority queue (releasePQ) based on the release time. 
		#		Since Python's PriorityQueue is implemented using heapq algorithm
		#		at the backend, inserting an item into the priority queue takes
		#		O(log n) time. Therefore, inserting the whole tasks into the
		#		priority queue takes O(n log n) time.

		# -------------------------------------------------------------------------------

		releasePQ = MyPriorityQueue();

		counter = 1;
		i = 0;
		while i < len(bigList):
			if bigList[counter] == ' ':			# check if the first item is a blank space
				print("Input error when reading the task attribute at task {}".format(counter));
				return;
			else:
				try:
					task = Task()

					task.taskName = str(bigList[i])
					task.releaseTime = int(bigList[i+1])
					task.deadline = int(bigList[i+2])

					i += 3;
					counter += 1;

				except:
					print("Input Error when reading the attributes of task {}".format(counter));
					return;

				releasePQ.insert(task.releaseTime, task);


		# ---------------------------------------------------------------------------

		#		Store tasks into priority queue (deadlinePQ) with deadline 
		#		as the priority key. The order of insertion is based on
		#		the earlist release time from releasePQ priority queue.
		#		Removing an item from a priority queue will take O(log n) time, so 
		#		does the insertion into deadlinePQ.

		#		Therefore, the whole removal and insertion process will take
		#		O(2n log n), which can be considered as O(n log n) time.

		# ---------------------------------------------------------------------------

		deadlinePQ = MyPriorityQueue();

		taskList = ["Task Name"]
		timeList = ["Execute Time"]

		time = 0;

		fmtL = len(taskList[0])
		fmtR = len(timeList[0])
		space = '\t\t'

		while releasePQ.empty() == False:			
		
			while (releasePQ.empty() == False) and (releasePQ.peek().releaseTime == time):
				deadKey = releasePQ.peek().deadline;
				deadlinePQ.insert(deadKey, releasePQ.remove())

			cores = m;
			while (cores > 0) and (deadlinePQ.empty() == False):
				
				if (deadlinePQ.peek().deadline <= time):
					print("No feasible schedule exists on " + file1 +" with " + str(m) + " cores");
					return;
				
				else:
					addedTask = "";
					addedTaskName = deadlinePQ.remove().taskName
					
					addedTask = addedTaskName;
					if fmtL < len(addedTask):
						fmtL = len(addedTask)
					if fmtR < len(str(time)):
						fmtR = len(str(time))
					timeList.append(str(time))
					taskList.append(addedTask);

				cores -= 1;

			time += 1;

		# --------------------------------------------------------------------

		# 				Displaying the output on the console

		# --------------------------------------------------------------------

		print()
		print("Working on "+ file1 +" with " + str(m) + " cores")
		for taskname, executetime in zip(taskList, timeList):		# if we want to have numbers on the each item, we use 'for i' and enumerate before zip
			print(taskname.center(fmtL), space , executetime.center(fmtR))
		print()

		
if __name__ == '__main__':
    
    t = TaskScheduler()
    
    t.scheduler("sample_files/samplefile1.txt", "f1", 4)
    # There is a feasible schedule on 4 cores

    t.scheduler("sample_files/samplefile1.txt", "f2", 3)
    # There is no feasible schedule on 3 cores

    t.scheduler("sample_files/samplefile2.txt", "f3", 5)
    # There is a feasible scheduler on 5 cores

    t.scheduler("sample_files/samplefile2.txt", "f4", 4)
    # There is no feasible schedule on 4 cores




			


