import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


public class Encoder {

	String data;							 
	Hashtable <Character,String> codesTable;
	int flushLength = 0;					 	

	private int octet = 0;
	private int offset = 0;
	private ByteArrayOutputStream bytesBuffer;
	private final String LBYTES = "1";
	private final String RBYTES = "0";


	public Encoder(String data)
	{
		bytesBuffer = new ByteArrayOutputStream();
		this.data = data;

		Hashtable <Character,Integer> freqTable = creerTableFrequences(data);

		ArrayList<Node> nodesList = this.getNodeList(freqTable);
		Comparator<Node> c  = new CharacterComparator();
		Collections.sort(nodesList,c);

		Node arbreHuffman = makeTokenTree(nodesList);

		codesTable = new Hashtable <Character,String>();
		String code = new String();
		codesTable = getHuffmanCodes(codesTable, arbreHuffman, code);
	}

	/**
	 * Compresser le data
	 * @param fichier de destination
	 */
	public ByteArrayOutputStream encode(){
		return this.dataEncode(data, codesTable);
	}

	/**
	 * Retourne la table de décodage
	 * @return
	 */
	public Hashtable <String,Character> getTableDecodage(){
		Hashtable <String,Character> decodeTable = new Hashtable <String,Character>();
		Enumeration<Character> keys = codesTable.keys();
		Character value;
		String key;

		//Inverser la table (key <---> value)
		while(keys.hasMoreElements()){
			value = keys.nextElement();
			key = codesTable.get(value);
			decodeTable.put(key, value);
		}
		return decodeTable;
	}
	/**
	 * Retourne la table de d'encodage
	 * @return
	 */
	public Hashtable <Character,String> getTableEncodage(){
		return codesTable;
	}
	/**
	 * Retourne le nombre de bit qui ont été flushés �  zéro. 
	 * @return
	 */
	public int getFlushLength(){
		return this.flushLength;
	}


	/**
	 * Permet de compter l'occurence de chaque type de caractère.
	 * Retourne un tableau associatif (Caractère/fréquence).
	 * Fonction originale: http://www.developer.com/java/other/article.php/3603066/Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 * @param Data qui doit être compressé
	 */
	private Hashtable <Character,Integer> creerTableFrequences(String rawData)
	{
		Hashtable <Character,Integer>frequencyData = new Hashtable<Character,Integer>();
		for(int cnt = 0;cnt < rawData.length();cnt++)
		{
			char key = rawData.charAt(cnt);
			if(frequencyData.containsKey(key))
			{
				int value = frequencyData.get(key);
				value += 1;
				frequencyData.put(key,value);
			}
			else
			{
				frequencyData.put(key,1);
			}
		}
		return frequencyData;
	}

	/**
	 * Transforme le hashTable en liste de HuffmanNodes.
	 * @return
	 */
	private ArrayList<Node> getNodeList(Hashtable <Character,Integer> hashTable)
	{
		ArrayList<Node> nodesList = new ArrayList<Node>();
		Character selectedCharacter;
		Set<Character> set = hashTable.keySet();
		Iterator<Character> itr = set.iterator();
		while (itr.hasNext()) {
			selectedCharacter = itr.next();
			Node node = new Node(hashTable.get(selectedCharacter), selectedCharacter);
			nodesList.add(node);
		}
		return nodesList;
	}

	/**
	 * Construit l'arbre binaire de Huffman �   partir des caractères/fréquences (nodeList) fournit en paramètre.
	 * @param nodesList
	 * @return Huffman Tree
	 */
	private Node makeTokenTree(ArrayList<Node> nodesList)
	{
		//limit case
		if(nodesList.size() <= 1)
			return nodesList.get(0);

		// nodes with low frequency
		Node nodeMinLeft = nodesList.get(nodesList.size()-1);
		Node nodeMinRight = nodesList.get(nodesList.size()-2);


		Node huffmanNode = new Node(nodeMinLeft, nodeMinRight);

		nodesList.remove(nodeMinLeft);
		nodesList.remove(nodeMinRight);

		nodesList.add(huffmanNode);
		Collections.sort(nodesList, new CharacterComparator());

		return makeTokenTree(nodesList);
	}

	/*
	 * Contruct node with Huffman algorithm
	 * in recursive way:
	 * Source: http://www.developer.com/java/other/article.php/3603066/Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	private Hashtable <Character,String> getHuffmanCodes(Hashtable <Character,String> codesTable, Node currentNode, String huffmanCode)
	{

		if(currentNode.left != null)
		{
			String tempCode = huffmanCode;
			tempCode += LBYTES;
			codesTable = getHuffmanCodes(codesTable, currentNode.left, tempCode);
		}

		if(currentNode.right != null)
		{
			String tempCode = huffmanCode;
			tempCode += RBYTES;
			codesTable = getHuffmanCodes(codesTable, currentNode.right, tempCode);
		}

		if(currentNode.value != null){
			codesTable.put(currentNode.value, huffmanCode);
		}

		return codesTable;
	}

	/*
	 * Encode data with hashtable
	 */
	private ByteArrayOutputStream dataEncode(String data, Hashtable <Character,String> table){

		for(int i=0;i<data.length();i++){
			this.writeByte(table.get(data.charAt(i)));
		}
		this.flushLength = this.bitFlush();
		return this.getByteArrayOutputStream();    
	}


	/*
	 * 
	 */
	private class CharacterComparator implements Comparator<Node>{

		public int compare(Node n1, Node n2) {
			if(n1.frequence > n2.frequence)
				return -1;
			else if(n1.frequence < n2.frequence)
				return 1;
			else
				return 0;
		}
	}



	public ByteArrayOutputStream getByteArrayOutputStream(){
		return this.bytesBuffer;
	}

	/**
	 * fonction qui ecrit bit a bit une chaine de caractere representant un octet dans le tampon ecrivain
	 * @param octet byte a ecrire bit a bit sur le buffer
	 */

	public void writeByte(String octet){ //fonction qui ecrit bit a bit un octet
		for(int i=0;i<octet.length();i++){
			if(octet.charAt(i)=='0'){
				this.writeBit(0);
			}
			else{
				this.writeBit(1);
			}
		}
	}


	/**
	 * méthode qui stocke les bits dans des octets
	 * @param bit bit a ecrire sur le buffer
	 */
	public void writeBit(int bit) { // on attrape l exception si il y a un probleme

		if (bit==0)
			this.octet<<=1;
		//si le bit lu est 0 on fait un decalage a droite
		else
			this.octet= this.octet<<1|1;
		//sinon on ecrit un 1 et on decale a droite

		this.offset++; //on incremente le compteur de bit
		if (this.offset==8) { //si le compteur de bit arrive a 8
			this.bytesBuffer.write(this.octet); //on ecrit l octet obtenu
			this.octet=0; //et on reinitialise l octet
			this.offset=0; //ainsi que son compteur
		}
	} 

	/**
	 * méthode qui ecrit les octets obtenu sur le disque et met des 0 en fin d octet non termin�
	 * @return renvoi le nombre d octet rajout�
	 */
	public int bitFlush(){
		int fillOctet = 0; // on va rajouté le nombre de bit non ecrit
		if (this.offset != 0) { //si le compteur n ai pas arriver jusqu a 8
			this.octet <<= (8 - this.offset);//on remplit le reste
			fillOctet = 8-this.offset;
			this.offset = 0;//on initialise le compteur
			this.bytesBuffer.write(this.octet);//on ecrit l octet
		}
		return fillOctet;
	} //on lance l ecriture sur le disque





}
