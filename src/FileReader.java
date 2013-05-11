import java.io.File;
import java.io.FileNotFoundException;



public abstract class FileReader {
	protected File target;
	
	public FileReader(String targetPath) throws FileNotFoundException{
		this.setTarget(targetPath);
	}
	public void setTarget(String targetPath) throws FileNotFoundException
	{
		File result = new File(targetPath);
		if(!result.exists())
		{
			throw new FileNotFoundException();
		}
		target = result;
	}
	
	public abstract void read();

}
