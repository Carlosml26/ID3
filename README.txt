IMPORTANT!
	The first row of the csv file has to be a row with the name of all the attributes in the correct position.

What does this contains?:
	- Runnable jar (ID3.jar)
	- Csv File (CarData.csv)
	- Folder with Java classes
	- Folder with the project
	- README.txt

Classes:
	- ID3Manager.java:
		The main class that execute the ID3 algorithm.

	- ID3Table.java:
		The class that contains the table with values.

	- DecisionTree.java:
		The cclass that contains the output tree. 

Run:
	COMMAND:
	java -jar ID3.jar (path of the csv file) (position of the class column)

	EXAMPLE:
	java -jar ID3.jar .\CarData.csv 6

OUTPUT:

	NODE ( ATTRIBUTES ) : [ CHILDNODES ]

	ATTRIBUTES and CHILDNODES only appear if NODE is not a leaf.
	The order of the attributes define the order of the childnodes.

	EXAMPLE:
		persons ([2, 4, more] ): [ [unacc , lug_boot ([small, big, med] ): [ [safety ([high, low, med] ): [ [buying ([high, low, vhigh, med] ): [ [maint ([high, low, vhigh, med] ): [ [acc , acc , unacc , acc ] ], doors ([2, 3, 4, 5more] ): [ [acc , acc , acc , acc ] ], unacc , acc ] ], unacc , unacc ] ], unacc , good ] ], unacc ] ]

	

