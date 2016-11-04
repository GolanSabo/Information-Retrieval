package il.ac.shenkar.Utils;

import il.ac.shenkar.Details.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtils {
	/**
	 * merges map of the temporary inverted file into the map of the main inverted file (contains all inverted files)
	 * @param map - the map to merge into the main map
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public static void MergeMaps(Map<String,Node> map) {
		File file = new File("./conf/invertedFile.ser");
		HashMap<String,ArrayList<Node>> invertedFile;
		
		if(!file.exists())
			invertedFile = new HashMap<String, ArrayList<Node>>();
		else
			invertedFile = (HashMap<String, ArrayList<Node>>) FileUtils.LoadFromFile(file.getAbsolutePath());
		Iterator<Entry<String, Node>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			String s = it.next().getKey();
			if(invertedFile.containsKey(s)){
				invertedFile.get(s).add((Node)map.get(s));
			}
			else{
				ArrayList<Node> tmp = new ArrayList<Node>();
				tmp.add((Node)map.get(s));
				invertedFile.put((String)s, tmp);
			}
		}
		try {
			PrinterUtils.PrintMap(invertedFile);
			FileUtils.SaveToFile(invertedFile, "invertedFile.ser", "./conf/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
