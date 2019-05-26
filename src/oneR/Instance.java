package oneR;
import java.util.*;
public class Instance {
	private ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
	
	public Instance() {
	}
	
	public void addAttribute(Attribute toAdd) {
		this.attributeList.add(toAdd);
	}
	public int getNumAttribs() {
		return attributeList.size();
	}
	public Attribute getAttrib(int i) {
		return attributeList.get(i);
	}
	
}
