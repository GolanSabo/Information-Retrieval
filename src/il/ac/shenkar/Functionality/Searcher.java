package il.ac.shenkar.Functionality;

import il.ac.shenkar.Details.Node;
import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.Utils.FileUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class Searcher {

	private final static String fileName = "./conf/invertedFile.ser";
	private static int nextWordIndex = 0;
	public static ArrayList<SearchResult> SearchWord(String query) throws Exception{
		Map invertedFile = FileUtils.LoadFromFile(fileName);
		query = query.toLowerCase();
		while(FileMonitor.isActive()){
			System.out.println("Wait for Batch Job To End");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ArrayList<Node> tmp = new ArrayList<>();
		ArrayList<SearchResult> result = new ArrayList<>();

		tmp = findList(query);
		result = calculateResult(tmp);

		return result;
	}


	private static ArrayList<Node> findList(String query) throws Exception{
		Map invertedFile = FileUtils.LoadFromFile(fileName);
		String[] words = query.split(" ");
		ArrayList<Node> list = new ArrayList<Node>();
		ArrayList<Node> tmp;
		ArrayList<Integer> count;
		String nextWord;
		for(int i = 0; i < words.length; ++i){
			++nextWordIndex;
			switch(words[i]){
			case "and":
				nextWord = words[++i];
				if(!checkNextWord(nextWord))
					throw new Exception("invalid logical sentence");
				if(words[i].startsWith("{") || words[i].startsWith("[") || words[i].startsWith("(")){
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
					i = words.length - t.length - 1;
				}
				else
					tmp = (ArrayList<Node>) invertedFile.get(nextWord);
				count = matchLists(list,tmp,true);

				for(int j = count.size() - 1 ; j >= 0; --j)
					list.remove((int)count.get(j));

				break;
			case "or":
				nextWord = words[++i];
				if(!checkNextWord(nextWord))
					throw new Exception("invalid logical sentence");
				if(words[i].startsWith("{") || words[i].startsWith("[") || words[i].startsWith("(")){
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
					i = words.length - t.length - 1;
				}
				else
					tmp = (ArrayList<Node>) invertedFile.get(nextWord);
				count = matchLists(list,tmp,false);

				for(int j = 0 ; j < count.size(); ++j)
					list.add(tmp.get(count.get(j)));
				break;
			case "not":
				nextWord = words[++i];
				if(!checkNextWord(nextWord))
					throw new Exception("invalid logical sentence");
				if(words[i].startsWith("{") || words[i].startsWith("[") || words[i].startsWith("(")){
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
					i = words.length - t.length - 1;
				}
				else {
					tmp = (ArrayList<Node>) invertedFile.get(words[i]);
				}
				list.addAll(logicalNot(tmp,invertedFile));
				break;
			default:
				tmp = (ArrayList<Node>) invertedFile.get(words[i]);
				return tmp;
				/*count = matchLists(list,tmp,false);

				for(int j = 0 ; j < count.size(); ++j)
					list.add(tmp.get(count.get(j)));*/
			}
		}

		return list;
	}

	private static ArrayList<Node> logicalNot(ArrayList<Node> tmp , Map invertedFile){
		int x = FileUtils.readIndex();
		ArrayList<Integer> fileList = new ArrayList<>();
		for(int j = 0; j < x; ++j)
			fileList.add(j);
		if(tmp != null){
			for(Node n : tmp){
				if(fileList.contains(n.getFileIndex()))
					fileList.remove((Object)n.getFileIndex());	
			}
		}

		Iterator itr = invertedFile.values().iterator();
		ArrayList<Node> list = new ArrayList<>();
		while(itr.hasNext()){
			ArrayList<Node> n = (ArrayList<Node>)itr.next();
			for(int i = 0; i < n.size(); ++i){
				if(fileList.contains(n.get(i).getFileIndex())){
					list.add(n.get(i));
				}
			}
		}
		return list;
	}

	private static boolean checkNextWord(String word){
		switch(word){
		case "and":
			return false;
		case "or":
			return false;
		default:
			return true;	
		}
	}

	private static ArrayList<Integer> matchLists(ArrayList<Node> list, ArrayList<Node> tmp, boolean isAnd){
		ArrayList<Integer> count = new ArrayList<>();
		if(tmp == null)
			return count;

		for(int j = 0; j < list.size(); ++j){
			boolean match = false;
			int fileIndex = list.get(j).getFileIndex();
			for(int k = 0; k < tmp.size(); ++k){
				if(tmp.get(k).getFileIndex() == fileIndex){
					match = true;
				}
				else{
					if(!isAnd && j == 0)
						count.add(k);
				}
			}
			if(isAnd)
				if(!match)
					count.add(j);

		}
		return count;
	}

	private static ArrayList<Node> doParenthensies(String s) throws Exception {
		Stack<Character> stack  = new Stack<Character>();
		ArrayList<Node> list = new ArrayList<>();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '[' || c == '(' || c == '{' ) {

				stack.push(c);
			}else if(c == ']') {
				if(stack.isEmpty()) throw new Exception("invalid parenthesis!");
				if(stack.pop() == '['){
					if(stack.isEmpty()){
						String query = s.substring(1, i);
						list.addAll(findList(query));
						nextWordIndex = i + 1;
						break;
					}
				}

			}else if(c == ')') {
				if(stack.isEmpty()) throw new Exception("invalid parenthesis!");;
				if(stack.pop() == '('){
					if(stack.isEmpty()){
						String query = s.substring(1, i);
						list.addAll(findList(query));
						nextWordIndex = i + 1;
						break;
					}
				}

			}else if(c == '}') {
				if(stack.isEmpty()) throw new Exception("invalid parenthesis!");;
				if(stack.pop() == '{'){
					if(stack.isEmpty()){
						String query = s.substring(1, i);
						list.addAll(findList(query));
						nextWordIndex = i + 1;
						break;
					}
				}
			}

		}
		return list;
	}

	private static ArrayList<SearchResult> calculateResult(ArrayList<Node> list){

		ArrayList<SearchResult> result = new ArrayList<>();
		boolean match;
		for(int i = 0; i < list.size(); ++i){
			Node tmp = list.get(i);
			SearchResult sr = new SearchResult(tmp.getFileDetails());
			match = false;
			for(int j = 0; j < result.size(); ++j){
				if(tmp.getFileDetails() == result.get(j).getFileDetails()){
					result.get(j).addLocations(tmp.getLocations());
					match = true;
				}
			}
			if(!match){
				result.add(new SearchResult(tmp.getFileDetails(), tmp.getLocations()));
			}
		}
		return result;
	}
}