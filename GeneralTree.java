
/*################################################
 * Ming T. Yu Concordia University
 * 
 * General project description: An abridged design for constructing an AVL tree and a Huffman encoder using share resources.
 * 
 *    
 * Class description:   
 * 
 * This is the section for the general shared tree API, used by both the huffman and the avl tree. 
 * 
 * In brief, it's an abridged AVL tree supporting several common basic operations. Please refer to pdf for exhaustif description. 
 * 
 *################################################
 */


public  class GeneralTree {
	// General tree. Credit: idea of recursive backtracking a tree comes from previous courses I've taken previously Coursera: Princeton Algorithm I Red-Black tree & MITopencourse. 
	// FixAVL has 6 scenario when balancing  at height >2. (8 permutations possible, but 2 of them amount to the same when double rotations is performed)
	static int counter = 0;
	public node root = null;
	
	//for stats
	protected int comparison = 0;
	protected int addOp = 0;
	protected int removeOp= 0; //check if it matches found + no found
	protected int printAt = 0;
	protected int parent_change = 0;
	
	protected class node  implements Comparable<node> 
	
	{
		int height;
		int value;
		node left = null;
		node right = null;
		//node parent = null; not needed in the end
		int id;
		int type;
		
		public node(){
			height = -1;
		}
		
		
		public node(int value){
			id = counter++;
			this.value= value;
		}
		
		
		public node(node copy){
			this.height = copy.height;
			this.value = copy.value;
			this.left = copy.left;
			this.right = copy.right;
			
		}
		
		@Override
		public int compareTo(node B) {
			if (value < B.value) return -1;
			if (value > B.value) return 1;
			if (id < B.id) return -1; //tie breaker for Huffman
			if (id > B.id) return 1;
			return 0;
		}
	}
	
	protected node removeMin(node A){
		//Not used as much for question 3), but very useful for Huffman. ATree ended up with its own version of find min to locate inorder successor (I don't want to mess up my Huffman by changing things)
		node result = new node();
		root = removeMin(result, A);
		
		return result.left;
		
	}
	
	protected node removeMin(){
		return removeMin(root);
	}
	
	protected node removeMin(node Node, node root){
		if (root.left == null){
			Node.left = root;
			if (root.right != null){
				return root.right;
			}
			
			return null;
		}
		
		root.left = removeMin(Node, root.left);
		
		updateheight(root);
		
		int heightdifference = compareHeight(root);
		if (Math.abs(heightdifference) > 2)
		{
			root = fixTree(root);
		}
		
		return root;

	}
	
	
	protected int height(node A){
		// fnc returns height of a tree at node A
		if (A == null) {
			return -1;
		}
		return A.height;
	}
	
	public void add(int value){
		// function adds a new node of int value to tree
		node adding = new node(value);
		root = add(adding, root);
	}
	
	protected node add(node Node, node root){
		// fnc add a a new node equal to the node passed as argument
		// Post-order traversal, basically - except  direction is dictated by comparison results so only node that interests us get visited. Add, and then back track up to fix height recursively + rebalance. 
		//termination condition
		if (root == null){
			Node.height = 0;
			root = Node;
			return root;
		}
		//traversal, determined by comparisons
		if (Node.compareTo(root) == 1){
			comparison++;
			root.right = add(Node, root.right);
		}
		if (Node.compareTo(root) == -1){
			comparison++;
			root.left = add(Node, root.left);
		}
		//Occurrence is used as tie breaker. No key can be equal for huffman. Ensures removeMin() always removes the first occurance when building the huffman
		// for AVL, using node.value = comparison to check equal first, then compareTo
		
		//visiting the node here
		updateheight(root);
		
		if (Math.abs(compareHeight(root)) > 2)
		{
			root = fixTree(root);
		}

		return root;
	}
	
	protected void updateheight(node root){
		//fnc updates the heights of the tree
		if (height(root.left) >= height(root.right)) root.height = height(root.left) + 1;
		if (height(root.left) < height(root.right))  root.height = height(root.right) + 1;
	}
	
	protected int compareHeight(node root)
	{
		return (height(root.left) - height(root.right));
	}
	
	protected node fixTree(node root){
		// fnc rebalances the tree if height difference > 3
		// Based on assignment requirement: 4 scenario for each direction, but only 3 used as per discussion with teacher: 1 rotation. 2 zig-zag rotations: first rotation at point 'zig' exception in the omitted scenario
		if (height(root.left)> height(root.right)) {
			if(height(root.left.right) > height(root.left.left)){
				root.left = L_Rotate(root.left);
				root= R_Rotate(root);
				return root;
			}
			if (root.left.left != null){
				if (height(root.left.left.left) > height(root.left.left.right)) return R_Rotate(root);
				else if (height(root.left.left.left) < height(root.left.left.right)){
					root.left.left = L_Rotate(root.left.left);
					return root = R_Rotate(root);
				}
			}
			 
		}	//symmetry is nice to have in binary tree. Just swapping right with left and left with right
		if (height(root.right)> height(root.left)){
			
			if(height(root.right.left) > height(root.right.right)){
				root.right = R_Rotate(root.right);
				root = L_Rotate(root);
				return root;
			}
			
			if ((root.right.right) != null){
				if (height(root.right.right.right) > height(root.right.right.left)) return L_Rotate(root);
				else if (height(root.right.right.right) < height(root.right.right.left)){
					root.right.right = R_Rotate(root.right.right);
					return root = L_Rotate(root);
				}
			}
		}
		return root;
		
	}
	
	
	protected void Calculate_Parent_ChangeRL(node node){
		// O(1) Not actually used
		parent_change++;
		node root = node;
		node A = root.right;
		
		if (A != null){
		parent_change++;
		node B = A.left;
		
		if (B!= null){
		node d = B.left;
		node c = B.right;
			if (d != null){
				parent_change++;
			}
			if (c != null){
				parent_change++;
			}
		}
		}
	}
	
	protected void Calculate_Parent_ChangeLR(node node){
		// O(1) 
		parent_change++;
		node root = node;
		node A = root.left;
		
		if (A!= null){
			parent_change++;
			node B = A.right;
			if (B != null){
			node d = B.right;
			node c = B.left;
				if (d != null){
					parent_change++;
				}
				if (c != null){
					parent_change++;
				}
			}
		}
	}
	
	public void add(node adding){
		root = add(adding, root);
	}
	protected void inorder(node A){
		if (A == null) return;
		inorder(A.left);
		System.out.print(A.value);
		System.out.print("   ID:");
		System.out.print(A.id);
		System.out.println();
		inorder(A.right);
	}
	
	
	protected node R_Rotate(node node){
		// O(1) basic rotation operation used for rebalancing AVL tree
		// could be more optimized, but I'll keep it as if so to not accidentally mess up a perfectly working avl and huffman
		parent_change++;
		node A = node;
		node B = A.left;
		//node C = B.left; not needed actually
		node c = null;
		if (B != null){
		c = B.right;
		}
		//node d = A.right; // not need
		if (B != null){
			parent_change++;
			B.right = A;
		}
		if (c != null){
			parent_change++;
		}
		A.left = c;
		updateheight(A);
		if (B!= null){
			updateheight(B);
		}
		
		
		return B;
	}
	
	protected node L_Rotate(node node){
		// O(1) 
		parent_change++;
		node A = node;
		node B = A.right;
		node c = null;
		if (B != null){
		c = B.left;
		}
		if (B != null){
			parent_change++;
			B.left = A;
		}
		if (c != null){
			parent_change++;
		}
		A.right = c;
		
		updateheight(A);
		if (B!= null){
			updateheight(B);
		}
		
		
		return B;
	}

}