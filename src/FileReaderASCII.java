import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


public class FileReaderASCII extends FileReader {
	
	private final int bufferSize = 1; //the size of the character buffer to use (IE: how many characters to read)

	public FileReaderASCII(String targetPath) throws FileNotFoundException {
		super(targetPath);
	}
	
	@Override
	public void read() {
		if(super.target == null)
			throw new IllegalStateException("Call setTarget before calling read");
		
		//HashMap<String, Integer> Tokens = new HashMap<String, Integer>();
		HashMap<String, Integer> tokens = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new java.io.FileReader(target));
			char[] buffer = new char[bufferSize];
			while(reader.read(buffer) != -1)
			{
				String key = new String(buffer);
				Integer value = tokens.remove(key); 
				//remove the token; this method returns the value that was present in the key, or null if it's a new token.
				//Use the value and key to create a new entry in the Treemap
				if(value == null)
					value = 0;
				tokens.put(key, ++value); //Increment value; a first entry will contain 1.
			}
			//End of file, close reader
			reader.close();
		} catch (FileNotFoundException e) {
			//This is already managed by setTarget, so this catch block will never happen.
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Tokens now full of each character string, and how many times they're used in the file
		//IE: [e, 6], [a,18], [z,3]
		//...now how do I order a Hashmap by value...
		
	}



}
