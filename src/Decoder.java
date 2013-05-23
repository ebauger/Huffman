import java.io.ByteArrayOutputStream;
import java.util.Hashtable;


public class Decoder {
	String data;
	int flushLength;
	Hashtable <String,Character> table;

	public Decoder(ByteArrayOutputStream data, Hashtable <String,Character> table, int flushLength){

		this.data = this.toString(data.toByteArray());
		this.data = this.data.substring(0, this.data.length()-flushLength);
		this.table = table;
		this.flushLength = flushLength;
	}

	public StringBuffer decode(){

		StringBuffer strDecode = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i < data.length(); i++){
			buffer.append(data.charAt(i));
			if(table.containsKey(buffer.toString())){
				strDecode.append(table.get(buffer.toString()));
				buffer.setLength(0);
			}
		}
		return strDecode;
	}

	public String toString(byte[] bytes){
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
			int val = b;
			for (int i = 0; i < 8; i++)
			{
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}

}
