package oneR;
import java.util.*;
public class Attribute {
	
	private String name;
	private ArrayList<String> options = new ArrayList<String>();
	private String value;
	private Boolean nominal;
	public Attribute(String name) {
		this.name = name;
		this.nominal = false;
	}
	
	//Checks whether attribute value is valid, then adds it to the
	//values list.  
	public void setVal(String val) {
		//if its a number
		if(!this.nominal) {
		value = val;
		}
		//check if valid entry
		else if(options.contains(val)) {
			value = val;
		}
		//if not fill it with blank
		else {
			value = "?";
		}
	}
	//Set whether the attribute is nominal or not
	public void setNominal() {
		this.nominal = true;
	}
	public String getName() {
		return name;
	}
	public String getVal() {
		return this.value;
	}
	public void addOption(String toAdd) {
		options.add(toAdd);
	}
	public String getAttribOption(int i) {
		return options.get(i);
	}
	public int numberOfOptions() {
		return options.size();
	}
	public String[] getOptions() {
		String[] toReturn = new String[options.size()];
		for(int i = 0; i < options.size(); i++) {
			toReturn[i] = options.get(i);
		}
		return toReturn;
	}
	
	
}
