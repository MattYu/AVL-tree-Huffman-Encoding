import java.io.FileReader;
import java.util.Scanner;

/*################################################
 * Ming T. Yu Concordia University
 * 
 * General project description: An abridged design for constructing an AVL tree and a Huffman encoder using share resources.
 * 
 *    
 * Class description:   
 * 
 * This is the section for the full AVL tree 
 * 
 * In brief, it adds operations not used by Huffman encoding to the general tree. Please refer to pdf for exhaustif description. 
 * 
 *################################################
 */

public class ATree extends GeneralTree{
	// ATree. 
	int remove_s = 0;
	int remove_f = 0;
	public int found = 0;
	public int not_found = 0;
	public int search_total_count = 0;
	int operation_counter = 0;
	boolean exception = false;
	

	
	public void reader(String a){
		// fnc specific for project enable reading of a text file passed as console argument
		try{  
			Scanner input = new Scanner(new FileReader(a));
			//Scanner input = new Scanner(a); swap when using fnc test()
        
        while (input.hasNextLine()) {
                String line = input.nextLine();
                char operation = line.charAt(0);
                //System.out.println("YES: " + operation);
                int number = Integer.parseInt(line.substring(1, line.length()));
                //System.out.println(number);
                operator(operation, number);
                if (printAt > 0){
                	if (operation_counter % printAt == 0){
                		System.out.println();
                		System.out.println("Traversal at " + operation_counter + ":");
                		postorder(root);
                	}
                }
                
            }
        	input.close();
		}
		catch (Exception ex) {
            ex.printStackTrace();
        }
		
		printStats();

	}
	
	private void operator(char Op, int number){
		// stat tracking each addition, substraction or find operation. (AVL for project requirement. If not for requirement, could simply implement a hash table instead of AVL tree for these operations otherwise)
		operation_counter++;
		if (Op == 'a'){
			addOp++;
			add(number);
		}
		if (Op == 'r'){
			removeOp++;
			remove(number);
		}
		if (Op == 'f'){
			search_total_count++;
			find(number);
		}
	}
	
	private void printStats(){
		//print the stats
		System.out.println();
		System.out.println(comparison + "  compares");
		System.out.println(parent_change + "  parent changes");
		System.out.println(found + " successful finds, " + not_found + " failed finds, " + search_total_count + " total searches");
		System.out.println(remove_s + "  successful removes, " + remove_f + "  failed removes, " + removeOp + " total removes" );
		System.out.println(addOp + " add operations");
	}
	
	private void postorder(node A){
		// post order traversal of nodes
		if (A == null) return;
		postorder(A.left);
		postorder(A.right);
		System.out.print(A.value);
		System.out.print(",");
		//System.out.print(A.id);
		//System.out.println();
	}
	
	public boolean find(int value){
		return find(value, root);
	}
	

	private boolean find(int value, node root){
		if (root == null){
			not_found++;
			return false;
		}
		if (root.value == value){
			found++;
			comparison++;
			return true;
		}
		else if (value < root.value){
			comparison++;
			return find(value, root.left);
		}
		else{
			comparison++;
			return find(value, root.right);
		}
	}
		
	
	public void remove(int value){
		node Node = new node(value);
		root = remove(Node, root);
	}
	
	private node remove(node Node, node root){
		// Post-order traversal, basically - except note every node is visited: direction is dictated by comparison results so only node that interest us get visited. Remove and then back up the tree to fix height recursively + rebalance. 
		//May find comparison that are not counted towards stats. Those are just comparison to make sure the parent change count are accurate. No other functional purpose.
		//System.out.println(root.value);
		//mix between add and find
		if (Node.value == root.value){//compare only values. No occurrence comparison with ATree removals
			comparison++;
			remove_s++;
			if (root.left == null){
				//System.out.println("case 1");
				if (root.right != null) {
					parent_change++;
					if (exception == true) {
						parent_change--;
						exception = false;
					}
				}
				//System.out.println("Case just leaf");
				return root.right;
			}
			if (root.right == null){
				//System.out.println("Case 2");
				if (root.left != null){ 
					parent_change++;
					if (exception == true) {
						parent_change--;
						exception = false;
					}
				}
				//System.out.println("Case just leaf");
				return root.left;
			}
			//System.out.println("Case 3");
			// Solution to: If to-be deleted node have two children. Find the values of the next in order successor. Copy-past its successor value/key to it without deleting any node. Then look for the successor and delete it instead
			// TODO: low priority; make the parent_change count patch more elegant. Remove when done**** 
			node B = findsuccessor(root.right); //inorder traverse at root.right. Stops at first visited node and returns it
			//System.out.println(B.value);
			root.id = B.id;
			root.value = B.value;
			if (B.value != root.right.value){//defense against weird case where the next inorder successor is a child of the to-be deleted node
				parent_change = parent_change + 2;
			}
			else{
				parent_change++;
				exception = true;
			}
			Node.value = B.value;
			remove_s--;
			remove_f--;
			root.right = remove(Node, root.right);
			//System.out.println("test");
		}
		if (Node.compareTo(root) == 1){
			comparison++;
			if (root.right != null)	root.right = remove(Node, root.right);
			else {
				remove_f++;//not found
			}
		}
		if (Node.compareTo(root) == -1){
			comparison++;
			if (root.left != null) root.left = remove(Node, root.left);
			else {
				remove_f++;//not found
			}
		}
		
		updateheight(root);
		if (Math.abs(compareHeight(root)) > 2)
		{
			root = fixTree(root);
		}

		return root;
	}
	
	
	private node findsuccessor(node A){
		// like removemin, but return more values. 
		if (A.left == null){
			return A;
		}
		return findsuccessor(A.left);
		
	}
	public static void test(){
		/*ATree a = new ATree();
		a.printAt=1;
		try{
			while (true){
			Scanner input = new Scanner(System.in); 
			System.out.print("Enter string:");
			String operation = input.nextLine();
			a.reader(operation);
			BTreePrinter.printNode(a.root);
			}
		}
			catch(Exception ex) {
	            ex.printStackTrace();
	        }*/
		//a.add(5);
		//a.add(2);
		//a.add(1);
		//a.add(4);
		//a.add(7);
		//a.add(8);
		//a.add(9);
		//a.add(10);
	}
	
	public static void main(String[] args){
		ATree a = new ATree();
		//test();
		
		if (args.length == 2){
		try{
		Scanner input = new Scanner(args[1]);
			String line = input.nextLine();
			a.printAt = Integer.parseInt(line);
			input.close();
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		}
		}
		try{
		a.reader(args[0]);
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
}