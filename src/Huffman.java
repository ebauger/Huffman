import java.io.FileNotFoundException;


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
		try {
			FileReaderASCII reader = new FileReaderASCII(fileName);
			reader.read();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void decompress(){
		
	}

}
