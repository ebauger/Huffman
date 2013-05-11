
public class Huffman {
	
	public static String fileName;
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		fileName = args[1].toString();
		
		if(args[0].compareToIgnoreCase("C") == 0)
			compress();
			
		if(args[0].compareToIgnoreCase("D") == 0)
			decompress();
		
	}
	
	public static void compress(){
		
	}
	
	public static void decompress(){
		
	}

}
