import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class FindLinkNode {

	static HashMap<Integer, Set<Integer>> hmap = new HashMap<Integer, Set<Integer>>();
	static HashMap<Integer, Set<Integer>> secondMap = new HashMap<Integer, Set<Integer>>();

	public static void addLink (String[] line){
			int node1 = Integer.parseInt(line[0]);
			int node2 = Integer.parseInt(line[1]);
			Set<Integer> set;
			if(hmap.get(node1) == null){
				set = new HashSet<Integer>();
			}
			else{
				set = hmap.get(node1);
			}
			set.add(node2);
			hmap.put(node1, set);
	}

	public static void writeSecondLinks(String dest) throws Exception{
		PrintWriter writer = new PrintWriter(dest, "UTF-8");
		for ( int key : hmap.keySet() ) {
			Set<Integer> secondLinks = new HashSet<Integer>();
		    for(int sKey : hmap.get(key)){
		    	if(hmap.get(sKey) != null){
		    		secondLinks.addAll(hmap.get(sKey));
		    	}
		    }
				secondLinks.addAll(hmap.get(key));
		    secondLinks.remove(key);
		    writer.println(key + " " + secondLinks.toString());
		}
		 writer.close();
	}

	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(new File(args[0]));
		while (input.hasNext()) {
			String str = input.nextLine();
			String[] splited = str.split("	");
			addLink(splited);
		}
		input.close();

		writeSecondLinks(args[1]);
	}
}
