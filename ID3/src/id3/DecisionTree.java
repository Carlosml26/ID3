package id3;

import java.util.List;

public class DecisionTree <T> {

    private T label;
    private List<T> attributes;
    private List<DecisionTree<T>> childNodes;
    
    public DecisionTree(T label, List<T> attributes, List<DecisionTree<T>> childNodes) {
		super();
		this.label = label;
		this.attributes = attributes;
		this.childNodes = childNodes;
	}

	public void addChild(DecisionTree <T> child) {
    	childNodes.add(child);
    }
	
	public T getLabel () {
		return label;
	}

	public List<T> getAttributes() {
		return attributes;
	}

	public List<DecisionTree<T>> getChildNodes() {
		return childNodes;
	}

	@Override
	public String toString() {
		String res = label + " ";
		if (!isLeaf()) {
			res+= "(" + attributes + " )" + ": [ " + childNodes +" ]";
		}
		return res;
	}
	
	 
	private boolean isLeaf () {
		return childNodes == null;
	}
	
}
