/* ------------------------------------------------------------ */

/* 		Written by Aditya Singgih for COMP9024 Assignment 1 	*/

/* ------------------------------------------------------------ */



import java.util.*;
import java.io.*;

public class MyDlist extends DList{
	Scanner input;
	String parts[], line;
	int count;

	/*------------------------------------------------------------

		Q1. create an empty doubly linked list
	
	-------------------------------------------------------------*/
	public MyDlist(){
		super();
	}


	/*------------------------------------------------------------

		Q2. create doubly linked list from 2 different source
	
	-------------------------------------------------------------*/
	public MyDlist(String f){
		super();

		if (f.equals("stdin")){
			while(true){
				input = new Scanner(System.in);											// setting up Scanner for typing input
				line = input.nextLine();
				parts = line.split(" +");												// regex for line splitting
				if(line.equals("")){													// 
					break;
				}
				else{
					for (String item : parts){	
						if (item.equals("")){
							continue;
						}
						else{										
							DNode thisNode = new DNode(item, null, null);
							if(super.header.next == super.trailer){
								super.addAfter(super.header, thisNode);
							}
							else{
								super.addLast(thisNode);
							}
						}
					}
				}	
			}
		}
		else {
			line = null;
			try{
				FileReader file = new FileReader(f);									// open File
				BufferedReader bufread = new BufferedReader(file);						// setup BUfferedReader to read line by line after this
				while ((line = bufread.readLine()) != null){
					parts = line.split(" +");
					for (String item : parts){
						DNode thisNode = new DNode(item, null, null);
						if(super.header.next == super.trailer){
							super.addAfter(super.header, thisNode);
						}
						else{
							super.addLast(thisNode);
						}
					}
				}
				bufread.close();
			}
			catch(FileNotFoundException e){												// exceptions handler
				System.out.println("Cannot locate the file");
			}
			catch(IOException e){														// exceptions handler
				System.out.println("Cannot open the file");
			}
		}
	}
	

	/*------------------------------------------------------------

		Q3. print elements inside the list
	
	-------------------------------------------------------------*/
	public void printList(){
		DNode current = super.header.next;

		while (current != super.trailer){
			System.out.println(current.getElement());
			current = current.next;
		}
		System.out.println();
	}

	/*------------------------------------------------------------

		Q4. create an identical copy of a doubly linked list
	
	-------------------------------------------------------------*/

	public static MyDlist cloneList(MyDlist u){
		MyDlist copiedList = new MyDlist();
		DNode thisNode = u.header.next;

		while(copiedList.size() < u.size()){										// a loop to add the MyDList u, to a new empty MyDList
			DNode tempNode = new DNode(null, null, null);							// create a new empty DNode as a place holder for the looped item
			
			tempNode.setElement(thisNode.getElement());
			tempNode.setNext(thisNode.getNext());
			tempNode.setPrev(thisNode.getPrev());

			copiedList.addLast(tempNode);											// add the looped item into 
			thisNode = thisNode.next;
		}
		return copiedList;
	}

	/*------------------------------------------------------------

		Q5. create a union of list u and list v 
	
	The time complexity for creating my union list is O(mn),
	where m is the number of items in list u and n is the number
	of elements in list v.
	
	We have to check every single item from both lists to prevent
	duplicate items. 

	-------------------------------------------------------------*/

	public static MyDlist union(MyDlist u, MyDlist v){
		MyDlist unionList = cloneList(v); 																					
		DNode targetNode = u.header.next, unionNode = unionList.header.next;
		
		// this while loop below checks whether the items in MyDlist v are 
		// already inside the unionList
		while (targetNode != u.trailer){
			int sameFlag = 0;
			DNode tempNode = new DNode(targetNode.getElement(), null, null);
			while (unionNode != unionList.trailer){
				if (targetNode.getElement().equals(unionNode.getElement())){
					sameFlag = 1;														// sameFlag == 1 means the item is identical
					break;																// break out of the loop when identical element is found
				}
				unionNode = unionNode.next;
			}
			
			if (sameFlag == 0){															// flag == 0 means that the item distinct
				unionList.addLast(tempNode);											// therefore we can append it to the unionList
			}

			unionNode = unionList.header.next;											// reset pointer of the unionNode to the beginning of the list
			targetNode = targetNode.next;
		}
		return unionList;
	}


	/*------------------------------------------------------------

		Q6. create an intersection of list u and list v 
	
	The time complexity for creating a new list consisting the
	intersection of list u and list v is O(mn), where m and n 
	are the number of elements in list u and v respectively.

	we have to check every single item from both lists to find
	the identical items, and then append it to a new list.

	-------------------------------------------------------------*/
	
	public static MyDlist intersection(MyDlist u, MyDlist v){
		MyDlist interList = new MyDlist(); 																					
		DNode vNode = v.header.next, uNode = u.header.next;

		// this while loop below checks whether there are identical items
		// in list v that is in list u. 
		while (vNode != v.trailer){
			uNode = u.header.next;
			DNode tempNode = new DNode(vNode.getElement(), null, null);
			while (uNode != u.trailer){
				if (vNode.getElement().equals(uNode.getElement())){							// identical elements are appended to interList
					interList.addLast(tempNode);
					uNode = uNode.next;
				}
				else{
					uNode = uNode.next;
				}
			}
			vNode = vNode.next;
		}
		return interList;
	}


	public static void main(String[] args) throws Exception{

		System.out.println("please type some strings, one string each line and an empty line for the end of input:");
		
		/** Create the first doubly linked list
		by reading all the strings from the standard input. */
		MyDlist firstList = new MyDlist("stdin");

		// * Print all elememts in firstList
		System.out.println("--------------------------------------------");
		System.out.println("Q1. Read and print from stdin");
		System.out.println("--------------------------------------------");

		firstList.printList();

		/** Create the second doubly linked list                         
		by reading all the strings from the file myfile that contains some strings. */
		/** Replace the argument by the full path name of the text file */  
		MyDlist secondList=new MyDlist("myfile.txt");

		/** Print all elememts in secondList */                     
		System.out.println("--------------------------------------------");
		System.out.println("Q2. Read and print from given text file");
		System.out.println("--------------------------------------------");

		secondList.printList();

		/** Clone firstList */
		MyDlist thirdList = cloneList(firstList);

		// * Print all elements in thirdList. 
		System.out.println("--------------------------------------------");
		System.out.println("Q3. Cloning the list from Q1");
		System.out.println("--------------------------------------------");

		thirdList.printList();

		/** Clone secondList */
		MyDlist fourthList = cloneList(secondList);

		/** Print all elements in fourthList. */
		System.out.println("--------------------------------------------");
		System.out.println("Q3. Cloning the list from Q2");
		System.out.println("--------------------------------------------");

		fourthList.printList();

		/** Compute the union of firstList and secondList */
		MyDlist fifthList = union(firstList, secondList);

		/** Print all elements in thirdList. */ 
		System.out.println("--------------------------------------------");
		System.out.println("Q4. Printing the union of Q1 and Q2");
		System.out.println("--------------------------------------------");

		fifthList.printList(); 

		// /** Compute the intersection of thirdList and fourthList */
		MyDlist sixthList = intersection(thirdList, fourthList);

		// /** Print all elements in fourthList. */
		System.out.println("--------------------------------------------");
		System.out.println("Q5. Printing the intersection of Q3 and Q4");
		System.out.println("--------------------------------------------");

		sixthList.printList();

	}
}




