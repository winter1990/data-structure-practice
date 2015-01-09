package qualiTestSolution;

import java.util.*;

public class WordObject {
	private String name;
	private int frequency;
	private List<Integer> rowIndex;
	
	public WordObject(String s) {
		this.name = s;
		this.rowIndex = new ArrayList<Integer>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public List<Integer> getRowIndexes() {
		return this.rowIndex;
	}
	
	public void add_Index(int index) {
		this.rowIndex.add(index);
	}
	
	public void frequency_Increase() {
		this.frequency++;
	}
}
