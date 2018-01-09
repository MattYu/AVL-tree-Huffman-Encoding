
public class SplayTree extends GeneralTree{

	boolean[] array = new boolean[1000];
	
	
	private node RL_zig_zag (node A){
		return A;
	}
	private node RR_zig_zag (node A){
		return A;
	}
	
	
	private node R_zig_zig (node A){
		return A;
	}
	
	private node L_zig_zig (node A){
		return A;
	}
	
	public void add(int A){
		
	}
	
	protected node Add(node A, node Root){
		return A;
	}
	
	public void remove(int A){
		
	}
	
	protected node remove(node A, node Root){
		return A;
		
	}
	
	public boolean find(int A){
		return true;
	}
	
	public boolean find (node A, node Root){
		
		return true;
	}
	
	protected node fixTree(node root){
		
		
		if (height(root.right) > height(root.left)){
			node t1 = root.right;
			if (height(t1.left) > height(t1.right)){
				t1 = R_Rotate(t1);
				return L_Rotate(root);
			}
			node t2 = t1.right;
			if (height(t2.right) > height(t2.left)){
				return L_Rotate(root.right);
			}
			t2= R_Rotate(t2.left);
			return L_Rotate(root.right);
			
		}
		
		
		if (height(root.left) > height(root.right)){
			node t1 = root.left;
			if (height(t1.right) > height(t1.left)){
				t1 = L_Rotate(t1);
				return R_Rotate(root);
			}
			node t2 = t1.left;
			if (height(t2.left) > height(t2.right)){
				return R_Rotate(root.left);
			}
			t2= L_Rotate(t2.right);
			return R_Rotate(root.left);
		}
		
		return root;
		
	}
	
	
	/*if (height(root.left)> height(root.right)) {
		if(height(root.left.right) > height(root.left.left)){
			root.left = L_Rotate(root.left);
			return R_Rotate(root);
		}
		if (root.left.left != null){
			if (height(root.left.left.left) > height(root.left.left.right)) return R_Rotate(root);
			else if (height(root.left.left.left) < height(root.left.left.right)){
				root.left.left = L_Rotate(root.left.left);
				return root = R_Rotate(root);
			}
		}
		 
	}	
	if (height(root.right)> height(root.left)){
		
		if(height(root.right.left) > height(root.right.right)){
			root.right = R_Rotate(root.right);
			return L_Rotate(root);
		}
		
		if ((root.right.right) != null){
		if (height(root.right.right.right) > height(root.right.right.left)) return L_Rotate(root);
		else if (height(root.right.right.right) < height(root.right.right.left)){
			root.right.right = R_Rotate(root.right.right);
			return root = L_Rotate(root);
		}
		}
		}
	return root;*/
	
}
