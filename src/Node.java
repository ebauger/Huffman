public class Node {

	public Node left;
	public Node right;
	public int frequence;
	public Character value;


	public Node(int frequence) {
		left = null;
		right = null;
		this.frequence = frequence;
	}
	public Node(int frequence, Character value) {
		left = null;
		right = null;
		this.frequence = frequence;
		this.value = value;
	}
	public Node(Node nodeL, Node nodeR) {
		left = nodeL;
		right = nodeR;
		this.frequence = nodeL.frequence + nodeR.frequence;
	}


	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public int getFrequence() {
		return frequence;
	}
	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}
	public int getValue() {
		return value;
	}
	public void setValue(Character value) {
		this.value = value;
	}

}
