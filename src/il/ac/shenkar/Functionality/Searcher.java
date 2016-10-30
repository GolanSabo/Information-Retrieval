package il.ac.shenkar.Functionality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Details.Node;
import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.controller.Controller;

public class Searcher {

	private final static String fileName = "./conf/invertedFile.ser";
	private static int nextWordIndex = 0;
	private static Map invertedFile = FileUtils.LoadFromFile(fileName);
	private static int notFlag = 0;

	public static ArrayList<SearchResult> SearchWord(String query) throws Exception {
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
					int x = notFlag;
					notFlag = 0;
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					notFlag = x;
					int ind = query.indexOf(words[i]) + nextWordIndex + 1;
					if(ind > query.length())
						i = query.length();
					else{
						String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
						i = words.length - t.length - 1;
					}
				}
				else
					if(nextWord.endsWith("*"))
						tmp = wildCard(nextWord);
					else
					tmp = (ArrayList<Node>) invertedFile.get(nextWord);
				if(notFlag % 2 == 1){
					nextWord = words[++i];
					if(nextWord.endsWith("*"))
						tmp = wildCard(nextWord);
					else
					tmp = (ArrayList<Node>) invertedFile.get(nextWord);
					tmp = logicalNot(tmp,invertedFile);
					notFlag = 0;
				}
				count = matchLists(list,tmp);

				for(int j = count.size() - 1 ; j >= 0; --j){
					for(int k = 0; k < tmp.size(); ++k)
						if(tmp.get(k).getFileIndex() == count.get(j))
							tmp.remove(k);
					for(int k = 0; k < list.size(); ++k)
						if(list.get(k).getFileIndex() == count.get(j))
							list.remove(k);
				}
				for(int j = 0; j < tmp.size(); ++j)
					list.add(tmp.get(j));

				break;
			case "not":
				notFlag++;
				nextWord = words[++i];
				if(!checkNextWord(nextWord))
					throw new Exception("invalid logical sentence");
				if(words[i].startsWith("{") || words[i].startsWith("[") || words[i].startsWith("(")){
					int x = notFlag;
					notFlag = 0;
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					notFlag = x;
					int ind = query.indexOf(words[i]) + nextWordIndex + 1;
					if(ind > query.length())
						i = query.length();
					else{
						String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
						i = words.length - t.length - 1;
					}
				}
				else {
					if(notFlag % 2 == 0){
						nextWordIndex++;
						notFlag = 0;
						tmp = (ArrayList<Node>) invertedFile.get(words[i]);
						list.addAll(tmp);
						break;
					}
					tmp = (ArrayList<Node>) invertedFile.get(words[i]);
				}

				list.addAll(logicalNot(tmp,invertedFile));
				break;
			default:

				if(words[i].equals("or"))
					nextWord = words[++i];
				else
					nextWord = words[i];
				if(!checkNextWord(nextWord))
					throw new Exception("invalid logical sentence");
				if(words[i].startsWith("{") || words[i].startsWith("[") || words[i].startsWith("(")){
					int x = notFlag;
					notFlag = 0;
					tmp = doParenthensies(query.substring(query.indexOf(words[i])));
					notFlag = x;
					int ind = query.indexOf(words[i]) + nextWordIndex + 1;
					if(ind > query.length())
						i = query.length();
					else{
						String[] t = query.substring(query.indexOf(words[i]) + nextWordIndex + 1).split(" ");
						i = words.length - t.length - 1;
					}
				}
				else{
					if(nextWord.endsWith("*"))
						tmp = wildCard(nextWord);
					else
						tmp = (ArrayList<Node>) invertedFile.get(nextWord);
				}
				if(notFlag % 2 == 1){
					nextWord = words[++i];
					if(nextWord.endsWith("*"))
						tmp = wildCard(nextWord);
					else
					tmp = (ArrayList<Node>) invertedFile.get(nextWord);
					tmp = logicalNot(tmp,invertedFile);
					notFlag = 0;
				}
				if(tmp!=null)
					list.addAll(tmp);
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
					Node t = new Node(n.get(i).getFileDetails());
					t.AddLocation(-1);
					list.add(t);
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
		case "not":
			notFlag++;
			return true;
		default:
			return true;	
		}
	}

	private static ArrayList<Integer> matchLists(ArrayList<Node> list, ArrayList<Node> tmp){
		ArrayList<Integer> count = new ArrayList<>();

		for(int j = 0; j < list.size(); ++j){
			boolean match = false;
			int fileIndex = list.get(j).getFileIndex();
			for(int k = 0; k < tmp.size(); ++k){
				if(tmp.get(k).getFileIndex() == fileIndex){
					match = true;
				}
			}
			if(!match)
				count.add(fileIndex);

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

	private static ArrayList<Node> wildCard(String word){
		word = word.replace("*", "");
		Set<String> set = invertedFile.keySet();
		ArrayList<Node> tmp = new ArrayList<>();
		Iterator itr = set.iterator();
		while(itr.hasNext()){
			String str = (String)itr.next();
			if(str.startsWith(word))
				tmp.addAll((ArrayList<Node>)invertedFile.get(str));
		}
		
		return tmp;
			
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
				if(Controller.getInstance().checkIfFileIsActive(tmp.getFileDetails().getIndex()))
					result.add(new SearchResult(tmp.getFileDetails(), tmp.getLocations()));
			}
		}
		System.out.println("Before sorting \n" + result);
		Collections.sort(result);
		for(SearchResult s: result)
			Collections.sort(s.getLocations());
		System.out.println("After sorting \n" + result);
		return result;
	}


	
}