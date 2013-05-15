
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;



public class FileReaderBinary extends FileReader {

	private final int bufferSize = 1; //the size of the character buffer to use (IE: how many characters to read)

	public FileReaderBinary(String targetPath) throws FileNotFoundException {
		super(targetPath);
	}

	@Override
	public ArrayList<Byte> read() {
		
		int[] tokens = new int[256]; //The list of tokens. Each item represents a byte, the value of which is the frequency this byte occurs.
		
		try {
			FileInputStream reader = new FileInputStream(target); 
			byte[] buffer = new byte[bufferSize];
			while(reader.read(buffer) != -1)
			{
				Byte loadedToken = buffer[0]; //Note: Java bytes go from -128 to 127. 
				//DEBUG
				System.out.println("Managing " + loadedToken);
				
				int arrayIndex = loadedToken + 128; //hence this. 
				tokens[arrayIndex] += 1; //Add +1 frequency to the token. 
				
			}
			//End of file, close reader
			reader.close();

		} catch (FileNotFoundException e) {
			//This is already managed by setTarget, so this catch block will never happen.
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Tokens now full of each character string, and how many times they're used in the file
		//Order tokens based on the number of times they occur; the token (array index) ends up in the array. 
		ArrayList<Byte> result = new ArrayList<Byte>();
		for(int i = 0; i < tokens.length; i++)
		{
			int max = 0;
			int maxIndex = 0;
			
			for(int n = 0; n < tokens.length; n++)//Note: NOT O(n²)! The array has a fixed length of 256, making this O(1)! 
			{
				if(tokens[n] > max) //find the index of the highest frequency byte in the array
				{
					max = tokens [n];
					maxIndex = n;
				}
			}
			if(max == 0)//Highest value had a 0 frequency = there are no more tokens to read. Break the loop.
				break;
			tokens[maxIndex] = 0;   
			result.add((byte) (maxIndex - 128)); //Have I mentioned how much I *hate* Java bytes? 
		}

		//Ergo, the array [240, 0, 210, 4, 614] will turn into [4, 0, 2, 3], or at least would if Java bytes were unsigned. 
		
		return result;
	}



}
