package oneR;

import java.util.*;
public class DataSet {
	private String relationName;
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private ArrayList<Attribute> attribs = new ArrayList<Attribute>();
	private ArrayList<Integer> numOptions = new ArrayList<Integer>();
	private ArrayList<String> rules = new ArrayList<String>();
	public DataSet() {}
	
	public void addInstance(Instance toAdd) {
		instances.add(toAdd);
	}
	public void setRelationName(String name) {
		this.relationName = name;
	}
	public String getName() {
		return relationName;
	}
	public void addAttribute(Attribute toAdd) {
		attribs.add(toAdd);
	}
	public Instance getInstance(int i) {
		return instances.get(i);
	}
	public int getSize() {
		return instances.size();
	}
	public int numAttributes() {
		return attribs.size();
	}
	public String getAttrib(int i) {
		return attribs.get(i).getName();
	}
	public void incrementOptions(int i) {
		numOptions.add(i);
	}
	public int getNumOptionsForElement(int i) {
		return numOptions.get(i);
	}
	//returns the attribute option for attribute i and option j
	public String getAttribOption(int i, int j) {
		return attribs.get(i).getAttribOption(j);
	}
	public Attribute attribByNum(int i) {
		return attribs.get(i);
	}
	public void addRule(String rule) {
		rules.add(rule);
	}
	public String[] getRules() {
		String[] toReturn = new String[rules.size()];
		for(int i = 0; i < rules.size(); i++) {
			toReturn[i] = rules.get(i);
		}
		return toReturn;
	}
}
