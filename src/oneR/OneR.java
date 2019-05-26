package oneR;
import java.util.*;
import java.io.*;
public class OneR {
	public static DataSet dataSet = new DataSet();
	public static DataSet testData = new DataSet();
	
	public static void main(String[] args) throws IOException {
	Scanner in = new Scanner(System.in);
	System.out.print("Enter the location of the training data file: ");
	String filename = in.nextLine();
	System.out.print("Enter the location of the testing data file: ");
	String testFile = in.nextLine();
	parseSet(filename);
	setupTestData(testFile);
	
	analyzeData();
	System.out.println("Data set relation name: " + dataSet.getName());
	int classifier = analyzeData();
	System.out.println("Attribute to classify on: "+ dataSet.getAttrib(classifier));
	classifyTestingData(classifier);
	}
	
	private static void classifyTestingData(int classifier) {
		//Print out classification rules
		String[] toPrint = dataSet.getRules();
		System.out.println("Testing data classified on the following rules: ");
		
		for(int k = 0; k < toPrint.length / 2; k++) {
			System.out.println(toPrint[k]);
		}
		int numCorrect = 0;
		int numIncorrect = 0;
		//For each instance, classify it and respectively print out the class
		for(int i = 0; i < testData.getSize(); i++) {
			boolean correct = false;
			String instVal = testData.getInstance(i).getAttrib(classifier).getVal();
			String classVal = testData.getInstance(i).getAttrib(dataSet.numAttributes()-1).getVal();
			String[] rules = dataSet.getRules();
			int rule = -1;
			for(int j = 0; j < rules.length; j++) {
				if(rules[j].contains(instVal)) {
					rule = j;
				}
				if(rules[j].contains(classVal)) {
					correct = true;
				}
			}
		
		String[] splitRule = rules[rule].split(":");
		System.out.println("Instance " + i + " classified as " + splitRule[1]);
		if(correct) {
			//System.out.println("This classification was correct.");
			numCorrect++;
		}
		else
			numIncorrect++;
			//System.out.println("This classification was incorrect.");
		}
		System.out.println("Correct predictions: " + numCorrect);
		System.out.println("Incorrect prediictions: " + numIncorrect);
		double percentCorrect = (numCorrect / testData.getSize()) * 100;
		System.out.println("Percentage of correct predictions: " + percentCorrect);
	}

	private static void parseSet(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
	    String line = br.readLine();
	    //Convert the file into a string to make splitting easier I guess
	    while (line != null) {
	        sb.append(line);
	        sb.append(System.lineSeparator());
	        line = br.readLine();
	    }
	    String DATA = sb.toString();
	    //This loops through the data set, we now just have to respectively set
	    //values of things according to the @ symbols that appear before the text
		Scanner sc = new Scanner(DATA);
		while(sc.hasNextLine()) {	
		    String currentLine = sc.nextLine();
		    if(currentLine.toLowerCase().startsWith("@relation")) {
		    	//since "@relation " is 10 characters long, set the relation name to the substring that begins there.
		    	dataSet.setRelationName(currentLine.substring(10));
		    }

		    if(currentLine.toLowerCase().startsWith("@attribute")) {
		    	//Add attribute to data set attribute list
		    	String split[] = currentLine.split(" ");
		    	Attribute toAdd = new Attribute(split[1]);
		    	//add the number of options an attribute has to the data set
		    	//this will be used in the oneR function
		    	int numOptions = split.length - 2;
		    	dataSet.incrementOptions(numOptions);
		    	for(int i = 2; i < split.length; i++) {
		    		toAdd.addOption(split[i]);
		    	}
		    	dataSet.addAttribute(toAdd);
		    	
		    	
		    }
		    if(currentLine.startsWith("@data")) {
		    	currentLine = sc.nextLine();
		    	while(sc.hasNextLine()) {
		    		if(currentLine.startsWith("%")) {
		    			currentLine = sc.nextLine();
		    			continue;
		    		}
		    		Instance toAdd = new Instance();
		    		String splitByComma[] = currentLine.split(",");
		    		for(int i = 0; i < dataSet.numAttributes(); i++) {
		    			Attribute attrib = new Attribute(dataSet.getAttrib(i));
		    			attrib.setVal(splitByComma[i]);
		    			//Not checking whether nominal or not here. No bueno but parsing sucks
		    			toAdd.addAttribute(attrib);
		    		}
		    		dataSet.addInstance(toAdd);
		    		currentLine = sc.nextLine();
		    	}
		    	
		    }
		    
		    }
	}
	//similar to previous, but it looks at a file of strictly data instances
	public static void setupTestData(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
	    String line = br.readLine();
	    //Convert the file into a string to make splitting easier I guess
	    while (line != null) {
	        sb.append(line);
	        sb.append(System.lineSeparator());
	        line = br.readLine();
	    }
	    String DATA = sb.toString();
	    Scanner sc = new Scanner(DATA);
    	while(sc.hasNextLine()) {
    		String currentLine = sc.nextLine();
    		if(currentLine.startsWith("end")) {
    			break;
    		}
    		Instance toAdd = new Instance();
    		String splitByComma[] = currentLine.split(",");
    		for(int i = 0; i < dataSet.numAttributes(); i++) {
    			Attribute attrib = new Attribute(dataSet.getAttrib(i));
    			attrib.setVal(splitByComma[i]);
    			//Not checking whether nominal or not here. No bueno but parsing sucks
    			toAdd.addAttribute(attrib);
    		}
    		testData.addInstance(toAdd);
    	}
	}
	
	//Assuming last attribute is the relation
	//we will use oneR to classify the data set
	public static int analyzeData() {
		int numInstances = dataSet.getSize();
		int numAttributes = dataSet.numAttributes() - 1;
		int numclassOptions = dataSet.getNumOptionsForElement(numAttributes);
		int bestAttrib = 0;
		int minError = Integer.MAX_VALUE;
		int minAttr = Integer.MAX_VALUE;
		int minLoc = Integer.MAX_VALUE;
		ArrayList<String> attributeOptions = new ArrayList<String>();
		ArrayList<String> classOptions = new ArrayList<String>();
		//loop through each attribute to build the classifier
		
		int arraySize = 0;
		for(int i = 0; i < numAttributes; i++) {
			arraySize += dataSet.getNumOptionsForElement(i);
		}
		
		//Get the attribute options for the class
		for(int i = 0; i < numclassOptions; i++) {
			classOptions.add(dataSet.getAttribOption(numAttributes, i));
		}
		
		//Get the attribute options for each attribute
		for(int i = 0; i < numAttributes; i++) {
			String[] toAdd = dataSet.attribByNum(i).getOptions();
			for(int j = 0; j< toAdd.length; j++) {
				attributeOptions.add(toAdd[j]);
			}
		}
		
		int[][] correct = new int[numclassOptions][attributeOptions.size()];
		//Loop through each instance, looking at the value of each attribute, and increasing that element in our 2d array
		for(int i = 0; i < numInstances; i++) {
			for(int j = 0; j < numAttributes; j++) {
				int xloc = classOptions.indexOf(dataSet.getInstance(i).getAttrib(numAttributes).getVal());
				int yloc = attributeOptions.indexOf(dataSet.getInstance(i).getAttrib(j).getVal());
				correct[xloc][yloc]++;
			}
		}
		//Get the attribute with the least amount of errors
		int count = 0;
		for(int i = 0; i < numAttributes; i++) {
			int error = 0;
			int max = 0;
			for(int j = 0; j < dataSet.getNumOptionsForElement(i); j++) {
				
				for(int k = 0; k < dataSet.getNumOptionsForElement(numAttributes); k++) {
					//maxLoc[j + count] = Integer.toString(j) + "," + Integer.toString(k);
					error+=correct[k][j];
					int x = correct[k][j];
					if(max < x) {
						max = x;
					}
				}
			}
			error = error -  max;
			if(error < minError) {
				minError = error;
				minAttr = i;
				minLoc = count;
			}
			count += dataSet.getNumOptionsForElement(i);
		}
	//Used to create the rules
	for(int i = minLoc; i < minLoc + dataSet.getNumOptionsForElement(minAttr); i++) {
		int max = Integer.MIN_VALUE;
		int maxRow = 0;
		for(int j = 0; j < dataSet.getNumOptionsForElement(numAttributes); j++) {
			int x = correct[j][i];
			if(max < x) {
				max = x;
				maxRow = j;
			}
		}
		String rule = attributeOptions.get(i) + ":" + classOptions.get(maxRow); 
		dataSet.addRule(rule);
	}
		
		
		return minAttr;
	}
	
}
