/* ------------------------------------------------------------ */

/*      Written by Aditya Singgih for COMP9024 Assignment 2     */

/* ------------------------------------------------------------ */

package net.datastructures;

import javax.swing.*;
import java.awt.*;

public class ExtendedAVLTree<K,V> extends AVLTree<K,V>{

    /*------------------------------------------------------------------------------------

                        Q1. create an identical copy of the AVL tree
    
            The time complexity for creating cloning the tree is O(n), where n is 
            the input size of the avl tree. 
            The complexity is O(n) because each of the node is visited only once,
            and each of the node is acts as a reference to the next one, when cloning
                
    -------------------------------------------------------------------------------------*/

	public static <K,V> AVLTree<K,V> clone(AVLTree<K,V> tree){
		
		AVLTree<K,V> treeCopy = new AVLTree<K,V>();			// creating new AVLTree
		
		K key = tree.root.element().getKey();						
		V val = tree.root.element().getValue();						
		
		treeCopy.insert(key, val);							// insert root into treeCopy
		subTreeCopy(treeCopy.root, tree.root);				// cloning the the rest of the tree using 
                                                            //    the root as a point of reference
		treeCopy.size = tree.size;
        treeCopy.numEntries = tree.numEntries;
		return treeCopy;

	}


	private static<K, V> void subTreeCopy(BTPosition<Entry<K,V>> clone, BTPosition<Entry<K,V>> current){

		AVLNode<K,V> nodeLeft = new AVLNode<K, V>();
		AVLNode<K,V> nodeRight = new AVLNode<K, V>();
		
		if (current.getLeft().element() != null){                 // check if the current node has left child
			nodeLeft.setElement(current.getLeft().element());     // obtain the element of left child from the current
                                                                  //    node to be set at the nodeLeft
			nodeLeft.setParent(clone);                            // set the parent of nodeLeft to the point of reference
			clone.setLeft(nodeLeft);                              // make the left node of the copy to point to nodeLeft
			subTreeCopy(nodeLeft, current.getLeft());             // do the same but now from the next left element
		}

		if (current.getRight().element() != null){
			nodeRight.setElement(current.getRight().element());
			nodeRight.setParent(clone);
			clone.setRight(nodeRight);
			subTreeCopy(nodeRight, current.getRight());
		}

   
	}

    /*-------------------------------------------------------------------------------------------

                    Q2. merges two AVLTrees, tree1 and tree2 into a new tree

            The time complexity to merge two AVLTrees using these methods below is
            O(m+n) where m is the number of nodes in tree1, and n is the number of 
            nodes from tree2. 

            converting the two trees into two sorted lists takes O(m+n) using the 
            .inorderPositions() from LinkedBinaryTree.

            Combining 2 sorted lists into one merged list also takes O(m+n) time,
            because each of the node is only visited once.

            Loading the merged list into a merged tree also takes O(m+n) time, due
            to the fact that nodes are only once in each loops.
    
    --------------------------------------------------------------------------------------------*/   

    public static<K,V> AVLTree<K,V> merge(AVLTree<K,V> tree1, AVLTree<K,V> tree2){

        PositionList<Position<Entry<K,V>>> listA = new NodePositionList<Position<Entry<K,V>>>();        // create an empty list
        tree1.inorderPositions(tree1.root, listA);                                                      // sort tree into the listA 

        PositionList<Position<Entry<K,V>>> listB = new NodePositionList<Position<Entry<K,V>>>();
        tree2.inorderPositions(tree2.root, listB);

        
        PositionList<Position<Entry<K,V>>> comboList = new NodePositionList<Position<Entry<K,V>>>();    // empty list for the merged items from listA and listB
       
        while(listA.isEmpty() == false && listB.isEmpty() == false){                                    // while both list are not empty

            Integer keyA = Integer.parseInt(listA.first().element().element().getKey().toString());     // store the key of the item of the listA into keyA
            Integer keyB = Integer.parseInt(listB.first().element().element().getKey().toString());     // store the key of the item of the listB into keyB


        // this is to sort the keys to make sure that we have a sorted list in the merged list
        // list merging start
            if (keyA <= keyB){                                                                             
                comboList.addLast(listA.remove(listA.first()));             // insert the keyA to the merged list, and remove that particular key from listA 
                                                                            //      if it's smaller or equal than keyB
            }
            else{
                comboList.addLast(listB.remove(listB.first()));             // otherwise, we append the keyB into the merged list
            }
        }
        // these two while loop is to append the leftover keys from the larger tree
        // we keep going until the list is empty
        while(listA.isEmpty() == false){                           
            comboList.addLast(listA.remove(listA.first()));         
        }

        while(listB.isEmpty() == false){
            comboList.addLast(listB.remove(listB.first()));
        }
        // list merging ends



        AVLTree<K,V> comboTree = new AVLTree<K,V>();                                    // create an empty tree to be loaded with keys from the merged list

        comboTree.root = loadTree(0, comboList.size()-1, comboList);
        comboTree.numEntries = tree1.size() + tree2.size();

        return comboTree;

    } 

    private static <K,V> BTPosition<Entry<K,V>> loadTree(int startPos, int endPos, PositionList<Position<Entry<K,V>>> list){
        
        if (startPos > endPos){
            return null;
        }

        int middle =  startPos+(endPos-startPos)/2;

        BTPosition<Entry<K,V>> theNode = new AVLNode<K,V>();     
        
        // filling the tree from the 1 spot from the left of the middle point
        BTPosition<Entry<K,V>> nodeLeft = new AVLNode<K,V>();
        nodeLeft = loadTree(startPos, middle-1, list);                  // call loadTree recursively
        if (nodeLeft != null){                                          
            nodeLeft.setParent(theNode);                                // set the parent of the leftNode 
        }
        theNode.setLeft(nodeLeft);                                      // set the left child of theNode

        theNode.setElement(list.remove(list.first()).element());

        // filling the tree from the middle to the right side
        BTPosition<Entry<K,V>> nodeRight = new AVLNode<K,V>();
        nodeRight = loadTree(middle+1, endPos, list);
        if (nodeRight != null){
            nodeRight.setParent(theNode);
        }
        theNode.setRight(nodeRight);

        return theNode;

    }

    /*-------------------------------------------------------------------------------------------

                   Q3. print the AVLtree specified by the parameter; in a new window
    
    --------------------------------------------------------------------------------------------*/   

    public static <K,V> void print(AVLTree<K,V> tree){

        JFrame window = new JFrame();                                                   // creates a new window for printing the tree.
        window.setTitle("AVLTree");                                                     
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();              // obtain screen resolution of the computer.
        int scrWidth = screenRes.width - 200;                                           // set the width and height from the obtained
        int scrHeight = screenRes.height - 200;                                         //      screen resolution, and subtracting it
                                                                                        //      by 200px so it won't cover the whole screen.
       
        window.setSize(scrWidth, scrHeight);                                            // set the size of the window.
        window.setResizable(false);
        window.getContentPane().setBackground(new Color( 204, 229, 255 ));              // custom colour background
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);                       // when several windows are openned, closing one of them
                                                                                        //      won't close the others (as opposed to EXIT_ON_CLOSE)

        printHelp(window, scrWidth/2, 0, scrWidth/2, 2, tree.root);                     // drawing the tree


    }


    private static <K, V> void printHelp(JFrame window, int xCoor, int yCoor, int gap, int h, BTPosition<Entry<K,V>> pos) {
        
        // Construction of the internal nodes of the tree
        int circleSize = 63 - h*3;                                    

        window.getContentPane().add(new Circle(xCoor, yCoor, circleSize));                                                      // creating circle for the internal node
        window.setVisible(true);                                                                                                // "printing" it on the screen
        window.getContentPane().add(new Text(pos.element().getKey().toString(), xCoor - h - 3, yCoor + circleSize/2 + 5));      // inserting the key into the circle
        window.setVisible(true);

        // Construction of the line on the LEFT side of the tree
        window.getContentPane().add(new Line(xCoor, yCoor + circleSize, xCoor - gap/2, yCoor + 100));
        window.setVisible(true);

        // Construction of the line on the RIGHT side of the tree
        window.getContentPane().add(new Line(xCoor, yCoor + circleSize, xCoor + gap/2, yCoor + 100));
        window.setVisible(true);


        

        int rectangleWid = 40 - h*2;        // specifying the width of the rectangle 
        int rectangleHgt = 30 - h*2;        // specifying the height of the rectangle 
        
        // This conditional below draws RIGHT part of the tree
        if (pos.getRight() != null && pos.getRight().element() != null) { 
            printHelp(window, xCoor + gap/2, yCoor + 100, gap/2, h + 1, pos.getRight());                                        // do a recursion until it hits null
        } 
        else { 
            window.getContentPane().add(new Rectangle(xCoor + gap/2, yCoor + 100, rectangleWid, rectangleHgt));                 // if the right side of the node is null, we
                                                                                                                                //      create a rectangle to represent external node
            window.setVisible(true);
        }

        // This conditional below draws LEFT part of the tree
        if (pos.getLeft() != null && pos.getLeft().element() != null) {                                                         
            printHelp(window, xCoor - gap/2, yCoor + 100, gap/2, h + 1, pos.getLeft());  
        } 
        else { 
            window.getContentPane().add(new Rectangle(xCoor - gap/2, yCoor + 100, rectangleWid, rectangleHgt));                 
            window.setVisible(true);
        }
           
        
    }


    /*--------------------------------------------------------------------------
            
        below are the classes for drawing the tree in the window
        that is extended from JComponent

    ---------------------------------------------------------------------------*/     
    private static class Text extends JComponent{

        String s;
        int x = 0;
        int y = 0;
        
        Text(String s, int x, int y){
            this.s = s;
            this.x = x;
            this.y = y;
        }  

        public void paintComponent(Graphics graphics){
            Font font = new Font("Arial", Font.PLAIN, 15);
            graphics.setFont(font);
            graphics.drawString(s, x, y);
        }

    }
    
    private static class Line extends JComponent{
        
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        
        Line(int x1, int y1, int x2, int y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void paintComponent(Graphics graphics){
            graphics.drawLine(x1, y1, x2, y2);
        }

    }

    private static class Circle extends JComponent{

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;

        Circle(int x, int y, int d){
            this.x = x-d/2;   
            this.y = y;
            this.w = d;                          
            this.h = d;                 
        }
  
        public void paintComponent(Graphics graphics){
            graphics.drawOval(x, y, w, h);
        }
    }

    private static class Rectangle extends JComponent{

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;

        Rectangle(int x, int y, int w, int h){
            this.x = x-w/2;
            this.y = y;
            this.w = w;
            this.h = h;

        }

        public void paintComponent(Graphics graphics){
            graphics.drawRect(x, y, w, h);
        }
    }
    
    public static void main(String[] args){ 
          String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
         "Athens", "Paris", "London", "Cairo"}; 
          int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
          String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"}; 
          int keys2[]={40, 7, 5, 32, 20, 30};
             
          /* Create the first AVL tree with an external node as the root and the
         default comparator */ 
             
            AVLTree<Integer, String> tree1=new AVLTree<Integer, String>();
  
          // Insert 10 nodes into the first tree
             
            for ( int i=0; i<10; i++)
                tree1.insert(keys1[i], values1[i]);
           
          /* Create the second AVL tree with an external node as the root and the
         default comparator */
             
            AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
           
          // Insert 6 nodes into the tree
             
            for ( int i=0; i<6; i++)
                tree2.insert(keys2[i], values2[i]);
             
            ExtendedAVLTree.print(tree1);
            ExtendedAVLTree.print(tree2); 
            ExtendedAVLTree.print(ExtendedAVLTree.clone(tree1));
            ExtendedAVLTree.print(ExtendedAVLTree.clone(tree2));
            
            ExtendedAVLTree.print(ExtendedAVLTree.merge(ExtendedAVLTree.clone(tree1), 
            ExtendedAVLTree.clone(tree2)));
          }


}

