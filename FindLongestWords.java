package zz_Aspera;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ======================
 * Programming Problem - Find Longest Word Made of Other Words
 * ======================
 * basic idea:
 * 
 * read the large file and store all the strings in a HashMap
 * 
 * sort the strings based on length, from longest to shortest
 * 
 * recursively check whether the (substring of) word can be constructed by others (depth first search)
 * 
 * store the "valid" strings in result set, from longest to shortest, same length in same sub-set
 * ===============================================================================================
 * output
 * 
 * 1. longest and 2nd longest word(s) that can be found in the list
 * 
 * 2. total count of how many of the words in the list can be constructed of others
 * 
 * 3. the run time (unit: ms) of the program
 * ===============================================================================================
 * other:
 * 
 * can change value of n in method printLongestWord() to get any valid amount of subsets in result. 
 * example: 
 * n=1, longest word;  n=3, 1st 2nd and 3rd longest sets of words;  n=100 or -1, "invalid number."
 * 
 * runtime: varies between 540 and 640 (ms) 
 * ===============================================================================================
 * @author Zihan
 *
 */

public class FindLongestWords {
	
	// sort the strings from longest to shortest
	private static class LengthComparator implements Comparator<String> {  
        public int compare(String s1, String s2) {  
            return s2.length() - s1.length();
        }  
    }
	
	/*
	 * 1. add all elements in String array into HashMap
	 * 
	 * 2. sort the String array based on length
	 * 
	 * 3. recursively check whether the String can be split/constructed by other words
	 * 
	 * 4. if the word can be constructed by concatenating others in the map, then
	 * 	  check the length:
	 * 	  1) if same length : add to the sub-list in result
	 * 	  2) if not the same: create a new list and add to result
	 * 
	 * 5. total count of words in the list that can be constructed of other words:
	 *    number of elements in result
	 * 
	 * 6. about the arguments
	 *    String[] arr : the array from the method getLargeData()
	 *    int n : number of result sets we want to get 
	 */
    private static void printLongestWord(String[] arr, int n) {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();  
        for (String str : arr) {  
            map.put(str, true);  
        }
        
        Arrays.sort(arr, new LengthComparator());

        int maxL = arr[0].length();	// because the array is sorted
        int index = 0;
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        res.add(new ArrayList<String>());

        for (String s : arr) {
        	if (canSplit(s, true, map)) {
                if (s.length() == maxL) {
                	res.get(index).add(s);
                } else if (s.length() < maxL) {
                	ArrayList<String> tmp = new ArrayList<String>();
                	tmp.add(s);
                	res.add(tmp);
                	
                	maxL = s.length();
                	index = res.size() - 1;
                } else {
                	
                }
            }  
        }
        
        // total count of words can be constructed
        int total = 0;
        for (int i = 0; i < res.size(); i++) {
        	total += res.get(i).size();
        }
        // check whether n is valid number
        if (n < 1 || n >= res.size()) {
        	System.out.println("invalid number. n can only between 1 and " + res.size());
        	return;
        }
        // print the result we want
        for (int i = 0; i < n; i++) {
        	System.out.println((i + 1) + " largest words: " + res.get(i));
        	
        }
        System.out.println("there are totally " + total + " words that can be constructed by others");
         
    }  
  
    // DFS search  
    public static boolean canSplit(String str, boolean isOriginalWord, HashMap<String, Boolean> map) {
        if (map.containsKey(str) && !isOriginalWord) {	// cannot be the word itself  
            return map.get(str);  
        }  
        for (int i = 1; i < str.length(); i++) {  
            String left = str.substring(0, i);  
            String right = str.substring(i);  
            if (map.containsKey(left) && map.get(left) && canSplit(right, false, map)) { 
                return true;  
            }  
        }  
        map.put(str, false);	// record the str that cannot be constructed by others  
        return false;  
    }  
    
    // read file and store in the HashSet
    public static String[] getLargeData(String fileDirectory) throws IOException {  
    	HashSet<String> set = new HashSet<String>(1740000);
		String s = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileDirectory))) {
			while ((s = br.readLine()) != null) {
				if (s.length() == 0) {
					continue;
				}
				set.add(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return set.toArray(new String[set.size()]);
    }
    
    public static void main(String[] args) throws IOException {
    	long startTime = System.currentTimeMillis();
    	// type in the directory of the file
    	String fileDirectory = "";
    	
    	String[] stringArray = getLargeData(fileDirectory);
        
    	printLongestWord(stringArray, 2); 
        
        long endTime = System.currentTimeMillis();
		System.out.println("run time: " + (endTime - startTime) + " ms");
    }
}