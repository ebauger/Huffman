import java.io.FileNotFoundException;


public class Compress {
	
	private FileReader _file;
	public Compress(String fileTarget) throws FileNotFoundException{
		_file = new FileReaderASCII(fileTarget);
		_file.read();
	}
}
