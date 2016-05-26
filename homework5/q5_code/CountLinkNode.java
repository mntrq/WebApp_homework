import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CountLinkNode {
	
	static HashMap<String, Integer> hmap = new HashMap<String, Integer>();
	
	public static void mapNode (String[] line){
			String start = line[0];
			if(hmap.get(start) == null){
				hmap.put(start, 0);
			}
		
		
			String dest = line[1];
			if(hmap.get(dest) == null){
				hmap.put(dest, 1);
			}
			else{
				hmap.put(dest, hmap.get(dest)+1);
			}
	}
	
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(new File(args[0]));
		while (input.hasNext()) {
			String str = input.nextLine();
			String[] splited = str.split("	");
			mapNode(splited);
		}
		input.close();
		
		PrintWriter writer = new PrintWriter("args[1]", "UTF-8");
		Set set = hmap.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	       Map.Entry mentry = (Map.Entry)iterator.next();
	      writer.println(mentry.getKey() + " " + mentry.getValue());
	    }
	    writer.close();
	}
}

