# Assignment 1

In this assignment, you extend the doubly linked list class DList given in the textbook. The subclass is named MyDlist. You need to implement the following constructors and methods of MyDlist:

1. ```public MyDlist()``` This constructor creates an empty doubly linked list.

2. ```public MyDlist(String f)``` This constructor creates a doubly linked list by reading all strings from a text file named f. Assume that adjacent strings in the file f are separated by one or more white space characters. If f is “stdin”, ```MyDlist(“stdin”)``` creates a doubly linked list by reading all strings from the standard input. Assume that each input line is a string and an empty line denotes end of input.

3. ```public void printList()``` This instance method prints all elements of a list on the standard output, one element per line.

4. ```public static MyDlist cloneList(MyDlist u)``` This class method creates an identical copy of a doubly linked list u and returns the resulting doubly linked list.

5. ```public static MyDlist union(MyDlist u, MyDlist v)``` This class method computes the union of the two sets that are stored in the doubly linked lists u and v, respectively, and returns a doubly linked list that stores the union. Each element of a set is stored in a node of the corresponding doubly linked list. Given two sets A and B, the union of A and B is a set that contains all the distinct element of A and B. Include the detailed time complexity analysis of this method in big O notation immediately above the source code of this method as comments.

6. ```public static MyDlist intersection(MyDlist u, MyDlist v)``` This class method computes the intersection of the two sets that are stored in the doubly linked lists u and v, respectively, and returns a doubly linked list that stores the intersection. Each element of a set is stored in a node of the corresponding doubly linked list. Given two sets A and B, the intersection of A and B is a set that contains all the elements of A that are also in B. Include the detailed time complexity analysis of this method in big O notation immediately above the source code of this method as comments.


- We assume that all the elements of a set are distinct.
- You are not allowed to use any data structures of Java you have not learned so far.
