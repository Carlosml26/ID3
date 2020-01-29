package id3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ID3Manager {
	
	private static ID3Table examples;
	private static DecisionTree <String> decisionTree;


	public static void main(String[] args) {
		examples = new ID3Table (args [0] , Integer.parseInt(args[1]));
		List<List<String>> table = examples.getTable();
		
		List<String> attributes = new ArrayList<>();
		
		for (int i = 0; i < table.size() - 1;i++) {
			attributes.add(table.get(i).get(0));
		}
		
		String outputAttribute = table.get(table.size()-1).get(0);
		decisionTree = ID3(attributes,outputAttribute,examples);
		
		System.out.println(decisionTree);	
	}
	
	private static DecisionTree <String> ID3 (List <String> attributes, String outputAttribute , ID3Table examples) {

		if (examples.isEmpty()) 
			return new DecisionTree<String>("Failure",null,null);
		
		String sameValue = allRecordsSameValue(examples);
		if (sameValue != null)
			return new DecisionTree<String>(sameValue,null,null); 
		
		if (attributes.isEmpty()) 
			return new DecisionTree<String>(getMostFrequentValue(examples),null,null); 
	
		String attributeMajor = getAttributeMajor(examples,attributes);
		List<String> valuesAttributeMajor = getValuesAttributeMajor(attributeMajor,examples);
		List <ID3Table> partitionedExamples = getPartitionedExamples(attributeMajor,valuesAttributeMajor,examples); 
		attributes.remove(attributes.indexOf(attributeMajor));

		List <DecisionTree <String>> subDecisionTreeNode = new ArrayList<DecisionTree <String>>();	

		for (ID3Table pExample: partitionedExamples) 
			subDecisionTreeNode.add(ID3(attributes,outputAttribute ,pExample));
		
		return  new DecisionTree <String> (attributeMajor,valuesAttributeMajor,subDecisionTreeNode);
	}


	private static String getMostFrequentValue(ID3Table examples) {
		List<String> valueList = new ArrayList<>();
		List<Integer> countValueList = new ArrayList<>();
		String mostFrequentValue = null;
		
		List<List<String>> examplesTable = examples.getTable();
		List<String> examplesClassColumn = examplesTable.get(examples.getClassColumn());


		for (int i = 1; i < examplesClassColumn.size();i++) {
			String value = examplesClassColumn.get(i);
			if (!valueList.contains(value)) {
				valueList.add(value);
				countValueList.add(1);
			}else {
				countValueList.set(valueList.indexOf(value), countValueList.get(valueList.indexOf(value)) + 1);
			}
		}
		
		mostFrequentValue = valueList.get(0);
		for (int i = 1; i < valueList.size();i++) {
			if (countValueList.get(i-1) < countValueList.get(i)) {
				mostFrequentValue = valueList.get(i);
			}
		}
		
		return mostFrequentValue;
	}



	private static List<ID3Table> getPartitionedExamples(String attributeMajor, List<String> valuesAttributeMajor, ID3Table examples) {
		return ID3Table.createSubTables(attributeMajor, valuesAttributeMajor, examples);
	}


	private static List<String> getValuesAttributeMajor(String attributeMajor, ID3Table examples) {
		Set<String> setValues = new HashSet<>();
		List<String> valueList = new ArrayList<>();
		List<List<String>> examplesTable = examples.getTable();
		
		for (int i = 0; i < examplesTable.size();i++) {
			if (examplesTable.get(i).get(0).equals(attributeMajor)) {
				for (int j = 1; j < examplesTable.get(i).size(); j++) {
					setValues.add(examplesTable.get(i).get(j));
				}
				break;
			}
		}

		valueList.addAll(setValues);

		return valueList;
	}


	private static String getAttributeMajor(ID3Table examples, List<String> attributes) {
		double entropy = calculateEntropy(examples);
		List<Double> entropySplitList = calculateEntropySplit(examples,attributes);
		List<Double> gainList = calculateGain(entropy,entropySplitList);
		return getGainAttribute(gainList,examples,attributes);
	}


	private static String getGainAttribute(List<Double> gainList, ID3Table examples, List<String> attributes) {
		int gainAttribute = -1;
		double currentGain = -100;
		for (int i = 0; i < gainList.size(); i++) {
			if (gainList.get(i) != null && currentGain < gainList.get(i)) {
				currentGain = gainList.get(i);
				gainAttribute = i;
			}
		}
		return examples.getTable().get(gainAttribute).get(0);
	}

	private static List<Double> calculateGain(double entropy, List<Double> entropySplitList) {
		List<Double> gainList = new ArrayList<>();
		for (Double entropySplit : entropySplitList) {
			if (entropySplit != null) {
				gainList.add(entropy - entropySplit);	
			}else {
				gainList.add(null);	
			}	
		}
		return gainList; 
	}

	private static List<Double> calculateEntropySplit(ID3Table examples, List<String> attributes) {
		// TODO Auto-generated method stub
		List<List<String>> examplesTable = examples.getTable();
		List<String> examplesClassColumn = examplesTable.get(examples.getClassColumn());
		List<Double> entropySplitList = new ArrayList<>();
		
		for (int i = 0; i < examplesTable.size()-1;i++) {
			if (attributes.contains(examplesTable.get(i).get(0))) {
				List<String> exampleTableColumn = examplesTable.get(i);
				List<String> attributesList = new ArrayList<>();
				List<Double> countAttributesList = new ArrayList<>();
				
				double entropySplit = 0;
				double totalNumber = 0;
				double logBase = 2;

				for (int j = 1; j < exampleTableColumn.size();j++) {
					boolean isNewValue = true;
					String currentValue = exampleTableColumn.get(j);
					
					for (int k = 0; k < attributesList.size();k++) {
						if (currentValue.equals(attributesList.get(k))) {
							isNewValue = false;
							totalNumber++;
							countAttributesList.set(attributesList.indexOf(attributesList.get(k)), countAttributesList.get(attributesList.indexOf(attributesList.get(k))) + 1);
						}
					}
					
					if (isNewValue) {
						attributesList.add(exampleTableColumn.get(j));
						countAttributesList.add(1.0);
						totalNumber++;
					}
				}
				List<Double> entropyList = new ArrayList<>();
				for (String attribute : attributesList) {
					List<String> valueList = new ArrayList<>();
					List<Double> countValueList = new ArrayList<>();
					double entropy = 0;
					double totalNumberValues = 0;

					for (int j = 1; j < examplesClassColumn.size();j++) {
						if (exampleTableColumn.get(j).equals(attribute)) {
							String value = examplesClassColumn.get(j);
							if (!valueList.contains(value)) {
								valueList.add(value);
								countValueList.add(1.0);
							}else {
								countValueList.set(valueList.indexOf(value), countValueList.get(valueList.indexOf(value)) + 1);
							}
							totalNumberValues++;
						}
					}
					
					for (Double countValue : countValueList) {
						entropy += -((countValue/totalNumberValues)*log(logBase,countValue/totalNumberValues));
					}
					entropyList.add(entropy);
				}
				
				for (Double countAttribute : countAttributesList) {
					entropySplit += -((countAttribute/totalNumber)*log(logBase,countAttribute/totalNumber));
				}
							
				entropySplitList.add(entropySplit);
				
			}else {
				entropySplitList.add(null);
			}
			
		}
		
		
		return entropySplitList;
	}

	private static double calculateEntropy(ID3Table examples) {
		
		List<String> valueList = new ArrayList<>();
		List<Double> countValueList = new ArrayList<>();
		
		List<List<String>> examplesTable = examples.getTable();
		List<String> examplesClassColumn = examplesTable.get(examples.getClassColumn());
		double logBase = 2.0;
		double numberTotalValues = 0;
		double entropy= 0;
		
		for (int i = 1; i < examplesClassColumn.size();i++) {
			String value = examplesClassColumn.get(i);
			if (!valueList.contains(value)) {
				valueList.add(value);
				countValueList.add(1.0);
			}else {
				countValueList.set(valueList.indexOf(value), countValueList.get(valueList.indexOf(value)) + 1);
			}
			numberTotalValues++;
		}

		
		for (Double countValue : countValueList) {
			entropy += -((countValue/numberTotalValues)*log(logBase,countValue/numberTotalValues));
		}

		return entropy;
	}
	
	public static double log(double	 base, double number) {
		return Math.log(number)/Math.log(base);
	}

	private static String allRecordsSameValue(ID3Table examples) {
		List<List<String>> examplesTable = examples.getTable();
		List<String> examplesClassColumn = examplesTable.get(examples.getClassColumn());
		String value = null;
		value = examplesClassColumn.get(1);
		
		for (int i = 2; i < examplesClassColumn.size();i++) 	
			if (!value.equals(examplesClassColumn.get(i))) 
				return null;
			
		return value;
	}
	




}
