/*
course: CSC310
project: Assignment 7
date: Oct. 24, 2020
author: Josiah Luikham
purpose: The Implementation of an avl tree.
*/
import java.util.Scanner;

class Customer {        //data for each node
    String name = "";
    int accNum = 0;
    float balance = 0;
    
    public Customer(String n, int num, float b) {
        name = n;
        accNum = num;
        balance = b;
    }
    
}
class Tree {  //the tree
    Node root;
    int balance = 0;
    Tree() {
        root = null;
    }
    
    public Node leftRot(Node curr) {   
        Node t1 = curr.rightChild; //data to be used is put in variables
        Node t2 = t1.leftChild;
        
        t1.leftChild = curr; //connections are made
        curr.rightChild = t2;
        
        curr.height = 1 + max(getHeight(curr.leftChild), getHeight(curr.rightChild)); //new heights
        t1.height = 1 + max(getHeight(t1.leftChild), getHeight(t1.rightChild));
        
        if (curr == root)  //checks if root and sets as root if so
            root = t1;
        return t1;
    }
    
    public Node rightRot(Node curr) {
        Node t1 = curr.leftChild;
        Node t2 = t1.rightChild;
        
        t1.rightChild = curr;
        curr.leftChild = t2;
        
        curr.height = 1 + max(getHeight(curr.leftChild), getHeight(curr.rightChild));
        t1.height = 1 + max(getHeight(t1.leftChild), getHeight(t1.rightChild));
        
        if (curr == root)
            root = t1;
        return t1;
                
        
    }
    
    
    public Node search(String n, Node root) { 
        if (root == null)
            return null;
        
        if (root.cus.name.equals(n))
            return root;
        
        Node leftRes = search(n, root.leftChild);  //recurive call is in variable
        
        if (leftRes != null)  //only returns if not null so that nulls do not continue up recursive chain
            return leftRes;
        
        Node rightRes = search(n, root.rightChild);
        
        if (rightRes != null)
            return rightRes;
        
        return null; //returns null and if the node being searched for is not in the tree
    }
     
    int max(int a , int b) {
        return Math.max(a, b);
    }
    
    public void insert(String n, int num, float b) { //intermediate function insert
        Node newNode = new Node();
        
        newNode.cus = new Customer(n, num, b); //creates node to be inserted
        
        if (root == null)
            root = newNode;
        else
            insertRec(newNode, root); //recursive call to insert
        
    }
    public int getHeight(Node n) {
        if (n == null)
            return 0;
       
        return n.height;
    }
    public int getBal(Node curr) {
        if (curr == null)
            return 0;
        
        return getHeight(curr.leftChild)-getHeight(curr.rightChild);
        
    }
    
    public void print(Node curr, int n) {
        if (curr != null) {
            for (int i = 1; i < n; i++) //puts appropriate spaces
                System.out.print("     ");
            System.out.print(n + ". ");
            curr.display();
            
            n++;
            print(curr.leftChild, n);  //preorder traversal
            print(curr.rightChild, n);
        }
    }
    
    public Node insertRec(Node newNode, Node curr) {
        if (curr == null)    //if a leaf is reached then then new node is placed here
            return newNode;
        
        if (newNode.cus.accNum < curr.cus.accNum)     //if going left then recurse in put inside curr.left child for traversal back up tree
            curr.leftChild = insertRec(newNode, curr.leftChild);
        else if (newNode.cus.accNum > curr.cus.accNum) 
            curr.rightChild = insertRec(newNode, curr.rightChild);
        else 
            return curr; //no duplicate keys
        
        curr.height = 1 + max(getHeight(curr.leftChild), getHeight(curr.rightChild)); //update heights
        
        int bal = getBal(curr);
        
        if (bal < -1) {  //do rotations if unbalanced
            if (curr.rightChild.rightChild != null)
                curr = leftRot(curr);
            else {
                curr.rightChild = rightRot(curr.rightChild);
                curr = leftRot(curr);
            }
        }
        
        if (bal > 1) {
            if (curr.leftChild.leftChild != null)
                curr = rightRot(curr);
            else {
                curr.leftChild = leftRot(curr.leftChild);
                curr = rightRot(curr);
            }
        }
        
        return curr; //return current node to traverse back up tree
    }
    
    public Node delete(int delAcc, Node curr) {
        if (curr == null) {
            System.out.println("node not found");
            return curr;
        }
            
        if (curr.cus.accNum == delAcc) {
            if (curr.rightChild == null){ //cases are checked
                if (curr.leftChild == null) {
                    if (curr == root) //sets root as null if root is curr
                        root = null;
                    return null;
                }
                else {
                    if (curr == root)
                        root = curr.leftChild;
                    return curr.leftChild;
                }
            }
            else if (curr.leftChild == null) {
                Node min = findMinDel(curr.rightChild); //minimum node on right side is found
                if (curr.rightChild == min)
                    return min;
                else
                    min.rightChild = curr.rightChild;
                if (curr == root)
                    root = min;
                return min;
            }
            else {
                Node min = findMinDel(curr.rightChild); //if node to be deleted has two children
                if (curr.rightChild != min) //if the minimum node on the right is not the rightchild of deleted node, 
                    min.rightChild = curr.rightChild; //then it is attached to both of the deleted nodes children
                min.leftChild = curr.leftChild;
                if (curr == root)
                    root = min;
                return min; //node is returned to move back up the tree
            }
        }
        else if (delAcc > curr.cus.accNum)
            curr.rightChild = delete(delAcc, curr.rightChild); //go left or right depending on size of node to be deleted's key
        else 
            curr.leftChild = delete(delAcc, curr.leftChild);
        
        curr.height = 1 + max(getHeight(curr.leftChild), getHeight(curr.rightChild));
        
        int bal = getBal(curr);
        
        if (bal < -1) {
            if (curr.rightChild.rightChild != null)
                curr = leftRot(curr);
            else {
                curr.rightChild = rightRot(curr.rightChild);
                curr = leftRot(curr);
            }
        }
        
        if (bal > 1) {
            if (curr.leftChild.leftChild != null)
                curr = rightRot(curr);
            else {
                curr.leftChild = leftRot(curr.leftChild);
                curr = rightRot(curr);
            }
        }
        
        return curr;
    }
    
    public Node findMinDel(Node curr) {
        Node del;
        if (curr.leftChild == null) //if no left child then this is the minimum node
            return curr;
        else {
            del = findMinDel(curr.leftChild);
        }
        
        if (curr.leftChild == del) //attach the right child of the minimal node to its parent
            curr.leftChild = del.rightChild;
        
        curr.height = 1 + max(getHeight(curr.leftChild), getHeight(curr.rightChild));
        
        int bal = getBal(curr);
        
        if (bal < -1) {
            if (curr.rightChild.rightChild != null)
                curr = leftRot(curr);
            else {
                curr.rightChild = rightRot(curr.rightChild);
                curr = leftRot(curr);
            }
        }
        
        if (bal > 1) {
            if (curr.leftChild.leftChild != null)
                curr = rightRot(curr);
            else {
                curr.leftChild = leftRot(curr.leftChild);
                curr = rightRot(curr);
            }
        }
        return del;
    }

}
class Node {
   Customer cus;
   int height;
   Node() {
       height = 1;
   }
   Node leftChild;
   Node rightChild;
   
   public void display() {
       System.out.println(cus.name + "(" + cus.accNum + ", $" + cus.balance + ")");
   }
   
    
}

public class CSC310Assgn7 {
    public static void main(String[] args) {
        
        Tree myTree = new Tree();
        
        Scanner in = new Scanner(System.in);
        
        String res, name = "";
        int accNum = 0;
        float balance = 0;
        do { 
            System.out.println("1. Search for customer by name.");
            System.out.println("2. Insert a new customer.");
            System.out.println("3. Delete existing Customer.");
            System.out.println("4. Print entire AVL tree.");
            System.out.println("5. Exit the program.");
            
            System.out.println("select: ");
            res = in.next();
            in.nextLine();
            System.out.println();
            switch (res.toLowerCase().charAt(0)) {
                case '1': 
                    System.out.print("Enter the name: ");
                    name = in.nextLine();
                    
                    Node n = myTree.search(name, myTree.root);
                    if (n == null)
                        System.out.println("Not found");
                    else
                        n.display();
                    
                    break;
                case '2': 
                    System.out.print("Enter the customer's name: ");
                    name = in.nextLine();
                    System.out.println();
                    System.out.print("Enter the customer's account number: ");
                    accNum = in.nextInt();
                    System.out.println();
                    System.out.print("Enter the customer's balance: ");
                    balance = in.nextFloat();
                    
                    myTree.insert(name, accNum, balance);
                    break;
                case '3': 
                    System.out.print("Enter the customer to be deleted's account number: ");
                    accNum = in.nextInt();
                    
                    myTree.delete(accNum, myTree.root);
                    
                    break;
                case '4': 
                    myTree.print(myTree.root, 1);
                    break;
                case '5':    
                    res = "q";
                    break;
                default:
                    System.out.println("invalid option.  Try it again.");
            }
        } while (res.toLowerCase().charAt(0) != 'q'); 
        
    }
    
}
