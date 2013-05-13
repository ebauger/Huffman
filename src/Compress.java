import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Compress {
	
	private FileReader _file;
	public Compress(String fileTarget) throws FileNotFoundException{
		_file = new FileReaderASCII(fileTarget);
		_file.read();
	}
	
	
	public void writeCompressedFile(ArrayList<String> tokens)
	{
		///NOTE: The character separator length should ideally be adapted to the number of
		///tokens in the list. A separator of length 1, for instance, would result in a file looking like
		///101011011101111 (tokens 1, 2, 3 and 4), which is fine for few tokens... but would result in the 
		///twentieth token becoming 11111111111111111111, which is... uh... not good. 
		
		
		//STEP 1: Write the token list in the file, so the file can be decompressed later
		
		//STEP 2: write the tokens as they are linked in a treeset. 
		
		//Should this method be here? Awkward. 
		//Also, remember this is Java 6, so we can't use the notation 0b00101, which is java 7.
	}
}
