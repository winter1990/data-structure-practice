package qualiTestSolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * problem:
 * Given an arbitrary text document written in English, 
 * write a program that will generate a concordance.
 * 
 * total work: 
 * logic and design main functions	 	0.5h
 * implementation						1h
 * debug and test, corner cases			2-3h
 * additional logic or explanations		1h
 * 
 ******************************************************
 * 
 * basic idea and problem-solving process:
 * 
 * 1. read the text file line by line and convert the file to a string
 * 
 * 2. use hashmap to store word, frequency and sentence index information
 * 	  name : String
 * 	  frequency : integer
 * 	  indexes : List<Integer>
 * 	  create an WordObject class to store the information
 * 
 * 3. use tokenizer to get each string and add to map:
 *		1) check the format of the string:
 *			if the end of string is '.':
 *				either special cases, "i.e." or "e.g." (keep the string format)
 *				or end of sentence, trim the string and update the sentence number
 *			else:
 *				trim the string and add to hashmap
 *				eliminate the non-digital and non-letter characters
 *		2) when add to map, check the map:
 *			if already existed:
 *				update the frequency and sentence index list
 *			else: 
 *				put into map
 * 
 * 4. print out the result set:
 *    	1) use sortedSet to order the result set based on Key
 *    	2) define a method to get the title, example: a b c...z aa ab ac... 
 *         the same format as Excel sheet column title 
 * 		3) trim the sentence index String, example:
 * 		   [1, 2, 2] => 1,2,2 
 * 
 * Others:
 * 
 * 1. assume '.' followed by a space means end of a sentence,
 *    so ab.c is one string "ab.c"
 *     
 * 2. some special cases:
 *  	for these cases: "i.e." and "e.g." include '.' in the string
 * 		for these cases: '.', '?' and '!'  treat as end of the sentence, update sentenceNumber 
 * 
 * 		if we want to include more special cases, modify the methods:
 *      	initialize_set();
 *			initialize_punctuation();
 *  
 * 3. the program will handle the multiple punctuations:
 * 		"hi!!!! there!"
 * 		the restult is: a. hi {1:1} b. there {1:2}
 *
 * 4. to print out the result:
 * 		call the printResult() method in main method and type in the file directory as a string
 * 
 ****************************************************** 
 * 
 * @author Zihan Wang
 */

public class GenerateConcordance {
	
	private static String str;
	
	private static Map<String, WordObject> map = new HashMap<String, WordObject>();
	
	private static int sentenceNumber = 1;
	
	private static Set<String> set_special_cases;
	
	private static Set<Character> set_punctuation;
	
	private static void readFile(String fileDirectory) throws IOException {
		initialize_set();
		initialize_punctuation();
		
		BufferedReader br = 
				new BufferedReader(new FileReader(fileDirectory));
		String line;
		StringBuilder sb = new StringBuilder();
		
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}		
		br.close();
		str = sb.toString();
	}
	
	private static void initialize_set() {
		set_special_cases = new HashSet<String>();
		set_special_cases.add("e.g.");
		set_special_cases.add("i.e.");
	}
	
	private static void initialize_punctuation() {
		set_punctuation = new HashSet<Character>();
		set_punctuation.add('.');
		set_punctuation.add('!');
		set_punctuation.add('?');
	}
	
	private static void tokenization() {
		StringTokenizer st = new StringTokenizer(str);
		String s;
		
		while (st.hasMoreTokens()) {
			s = st.nextToken().toLowerCase();
			int endIndex = s.length() - 1;
			boolean flag = false;
			
			while (!Character.isLetterOrDigit(s.charAt(endIndex))) {
				if (set_punctuation.contains(s.charAt(endIndex))) {
					if (set_special_cases.contains(s)) {
						break;
					} else {
						endIndex--;
						flag = true;
					}
				} else {
					endIndex--;
				}
			}
			s = s.substring(0, endIndex + 1);
			addToHashMap(s);
			
			if (flag) {
				sentenceNumber++;
			}
		}
	}
	
	private static void addToHashMap(String s) {
		if (!map.containsKey(s)) {
			WordObject obj = new WordObject(s);
			obj.frequency_Increase();
			obj.add_Index(sentenceNumber);
			map.put(s, obj);
		} else {
			WordObject tmp = map.get(s);
			tmp.frequency_Increase();
			tmp.add_Index(sentenceNumber);
			map.put(s, tmp);
		}
	}
	
	public static void printResult(String file) {
		try {
			readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tokenization();
		
		int n = 1;
		SortedSet<String> keys = new TreeSet<String>(map.keySet());
		
		for (String key : keys) {
			WordObject word = map.get(key);
			String title = convertToTitle(n);
			String sentenceIndex = word.getRowIndexes().toString();
			sentenceIndex = sentenceIndex.substring(1, sentenceIndex.length() - 1); 
			sentenceIndex = sentenceIndex.replaceAll("\\s", "");
			System.out.println(
					title + ". " 
					+ word.getName() 
					+ " {" + map.get(key).getFrequency() + ":" + sentenceIndex + "}");
			n++;
		}
	}
	
	public static String convertToTitle(int n) {
        StringBuilder res = new StringBuilder();
        
        while (n > 0) {
            n -= 1;
            res.insert(0, (char)('a' + n % 26));
            n /= 26;
        }
        return res.toString();
    }
	
	public static void main(String[] args) {
		printResult("C://Users//Zihan//Desktop//test file.txt");
	}
}
