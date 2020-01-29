package id3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ID3Table {

	private List<List<String>> table;
	private int classColumn;
	

	
	public int getClassColumn() {
		return classColumn;
	}


	private void setClassColumn(int classColumn) {
		this.classColumn = classColumn;
	}


	public ID3Table (String csvFile, int classColumn) {
		CSVReaderTable(csvFile);
		setClassColumn(classColumn);
	}
	

	public ID3Table(List<List<String>> subTable, int classColumn) {
		// TODO Auto-generated constructor stub
		setTable(subTable);
		setClassColumn(classColumn);
	}


	public List<List<String>> getTable() {
		return table;
	}

	public void setTable(List<List<String>> table) {
		this.table = table;
	}
	

	public static List<ID3Table> createSubTables(String attributeMajor, List<String> valuesAttributeMajor, ID3Table origin){
		List <ID3Table> subTables = new ArrayList<ID3Table>();

		int numberOfValues = valuesAttributeMajor.size();
		
		List<List<String>> originTable = origin.getTable();
		int columnAttribute = getColumnAttribute(originTable,attributeMajor); 
	
		for (int i = 0; i < numberOfValues; i++) {
			List<Integer> indexOfRows;
			String valueAttribute = valuesAttributeMajor.get(i);
			
			List<List<String>> subTable	= new ArrayList<List<String>>();

			indexOfRows = getIndexOfRowsFrom(originTable.get(columnAttribute),valueAttribute);
			

			for (int j = 0; j < originTable.size();j++) {
				List<String> listValueAttribute = new ArrayList <String> ();
				listValueAttribute.add(originTable.get(j).get(0));
				for (Integer index : indexOfRows) {
					listValueAttribute.add(originTable.get(j).get(index));
				}
				subTable.add(listValueAttribute);	
			}
			
			subTables.add(new ID3Table (subTable, origin.getClassColumn()));
		}
		
		return subTables;
	}

	private static int getColumnAttribute(List<List<String>> origin, String attributeMajor) {
		// TODO Auto-generated method stub
		int columnAttribute = 0;

		for (columnAttribute = 0; columnAttribute < origin.size();columnAttribute++) 
			if (attributeMajor.equalsIgnoreCase(origin.get(columnAttribute).get(0))) 
				break;
					
		return columnAttribute;
	}

	private static List<Integer> getIndexOfRowsFrom(List<String> originColumn, String valueMajorAttribute) {
		// TODO Auto-generated method stub
		List<Integer> indexOfRows = new ArrayList<Integer> ();
		int i = 0;
		for (String valueAttribute : originColumn) {
			if (valueMajorAttribute.equalsIgnoreCase(valueAttribute)) {
				indexOfRows.add(i);
			}
			i++;
		}
		return indexOfRows;
	}
	
	public boolean isEmpty () {
		return table.isEmpty();
		
	}
	
	private void CSVReaderTable (String direction) {
		String csvFile = direction;
        String line = "";
        String cvsSplitBy = ",";
        table = new ArrayList<List<String>> ();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	
        	
        	if ((line = br.readLine()) != null) {
        		 String[] values = line.split(cvsSplitBy);
        		 
                 for (String value : values) {
                	List <String> valueList = new ArrayList<>();
                	valueList.add(value);
                 	table.add(valueList);
                 }
        	}

            while ((line = br.readLine()) != null) {

                String[] values = line.split(cvsSplitBy);
                
                for (int i = 0; i < table.size();i++) {
                	List<String> valueList = table.get(i);
                	valueList.add(values[i]);
                	table.set(i, valueList);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (List<String> row : table) {
			for (String cell : row) {
				res.append(cell).append(" ");
			}
			res.append("\n");
		}
		return res.toString();
	}
	
}

