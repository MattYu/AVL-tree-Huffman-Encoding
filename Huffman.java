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
 * This is the section for the Huffman API. 
 * 
 * In brief, instead of using the heap, I simply recycled the API of my already built custom AVL to perform encoding. 
 * Slower than heap huffman in terms of asymptotic complexity in theory, but for the task at hand (small encoding), difference is not measurable. 
 * Code length is reduced by +50% 
 * 
 * 
 * method reader: reads a string and encode it
 * method leaf: Refer to leaf node on AVL tree. For huffman, the leaf holds info (frequency) of each char
 * method mergedNode: Refer to node on AVL tree. Once two leaf is fused, it because a mergednode. See pdf documentations for more details.
 * 
 * 
 *################################################
 */

public class Huffman extends GeneralTree {
	
	
	private int[] frequency = new int[256];
	private int[] occurence = new int[256];
	private String[] Huffman_Code = new String[256];
	private String output = "";
	
	
	static public void builder(Huffman a, String address){
		//pushes node on custom avl
		a.reader(address);
		for (int i = 0; i < 256; i++){
			if (a.frequency[i] !=0){
				leaf temp = a.new leaf(i);
				a.add(temp);
			}
		}
		while (true){
			//System.out.print("Count: ");
			//System.out.println(++a.count);
			node A = a.removeMin();
			node B = a.removeMin();
			mergedNode C= a.new mergedNode(A,B);
			a.add(C);
			//a.inorder(a.root);
			//a.inorder2((mergedNode)a.root);
			if ((a.root.left == null) && (a.root.right == null)){
				break;
			}
		}

		a.Htraverse(a.root, "");
				
		//System.out.println(a.Huffman_Code[32]);
		//a.printHuffmantable();
		try{
		Scanner input = new Scanner(System.in); 
		System.out.print("Enter string:");
		String toEncode = input.nextLine();
		for (int i = 0; i <toEncode.length(); i++){
          	int index = toEncode.charAt(i);
          	a.output = a.output + a.Huffman_Code[index];
		}
		input.close();
		}
		catch(Exception ex) {
            ex.printStackTrace();
        }
		System.out.print(a.output);
	}
	
	private void reader(String a){
		//build frequency
		try{
		  Scanner input = new Scanner(new FileReader(a));
	        
	       while (input.hasNextLine()) {
	                String line = input.nextLine();
	                for (int i = 0; i <line.length(); i++){
	                	int index = line.charAt(i);
	                	if (frequency[index] == 0){
	                		occurence[index] = counter++;
	                		frequency[index] = 1;
	                	}
	                	else{
	                		frequency[index]++;
	                	}
	                	//System.out.println(index);
	                }
	       }
	       input.close();
		}
		catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	private void Htraverse(node A, String B){
		if (A == null) return;
		if (A.type == 1) A = (mergedNode) A;
		if (A.type == 2){
			leaf temp = (leaf) A;
			//temp.visit();
			Huffman_Code[temp.key] = B;
		}
		Htraverse(((mergedNode)A).L_Huffman, B+'0');
		Htraverse(((mergedNode)A).R_Huffman, B+'1');
	}
	
	/*private void printHuffmantable(){//for testing only
		for (int i = 0; i < 256; i++){
			if (frequency[i] != 0){
				char temp = (char) i;
				System.out.println(temp + "     Frequency: " + frequency[i] + "     Occurence: " + occurence[i] + "     index: " + i + "     Huffman: " + Huffman_Code[i]);
				totalchar = totalchar + frequency[i];
			}
		}
		System.out.println("     Total Frequency: ");
		System.out.println(totalchar);
	}*/
	
	private class mergedNode extends node{
		//boolean leaf = false;
		node L_Huffman;
		node R_Huffman;
		/*public void visit(){
			System.out.println("Internal" + "   Frequency:" + value + "   ID:" + id);
		}*/
		public mergedNode(){};
		public mergedNode(node A, node B){
			id = counter++;
			type = 1;
			if (A.compareTo(B) == -1){
				L_Huffman = A;
				R_Huffman = B;
			}
			else
			{
				L_Huffman = B;
				R_Huffman = A;
			}
			value = A.value + B.value;
		}
	}
	
	private class leaf extends mergedNode{
		//boolean leaf = true;
		char key;
		
		/*ublic void visit(){
			System.out.println(key + "   Frequency:" + value + "   ID:" + id);
		}*/
		
		public leaf(int index){
			value = frequency[index];
			id = occurence[index];
			key = (char) index;
			type = 2;
		}
	}
	
	
	
	public static void main(String[] args){
		
		Huffman a = new Huffman();
		String address = args[0];
		builder (a, address);
		//builder (a, "Jabberwock.txt");
	}
}