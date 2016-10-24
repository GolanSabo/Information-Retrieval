package il.ac.shenkar.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PrinterUtils {
	/**
	 * prints a map
	 * @param tmp
	 */
	public static void PrintMap(Map tmp){
		Map treeMap = new TreeMap(tmp);
		Iterator it = treeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(" ---------- " + pair.getKey() + " ---------- \n" + pair.getValue().toString());
			it.remove();
		}
	}
}
