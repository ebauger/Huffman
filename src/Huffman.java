import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Hashtable;


public class Huffman {

	public static String fileName ;
	public static String dataToCompress; // ASCII DATA
	public static ByteArrayOutputStream dataCompressed; //BINARY DATA
	public static Encoder encoder;
	public static Decoder decoder;


	public static void main(String[] args) throws IOException, ClassNotFoundException {

		fileName = args[1];
		
		// 'c' for compress
		if(args[0].compareToIgnoreCase("c") == 0){
			encode();
		}
		// 'd' for decompress
		if(args[0].compareToIgnoreCase("d") == 0){
			decode();
		}
	}

	
	public static void encode() throws IOException{
		dataToCompress = fileReader(fileName);
		encoder = new Encoder(dataToCompress);
		dataCompressed = encoder.encode();
		ByteArrayOutputStream header = new ByteArrayOutputStream();
		
		// serialized header
		ObjectOutputStream oos = new ObjectOutputStream(header);
		oos.writeObject(encoder.getTableDecodage());
		oos.flush();
		
		int headerSize = header.size();
		//header.write(encoder.getTableDecodage());
		fileWriter(fileName + ".huf", headerSize, header, dataCompressed);
	}
	public static void decode() throws ClassNotFoundException{

		dataCompressed = fileReaderBinary(fileName);
		//Hashtable <String,Character> decodeTable = encoder.getTableDecodage(); // test in memory
		decoder = new Decoder(dataCompressed, decodeTable, 0);
		StringBuffer textDecompresse = decoder.decode();
		fileWriter(fileName + ".txt", textDecompresse);
	}


	private static void fileWriter(String fileName, StringBuffer data){
		try {
			FileWriter fw = new FileWriter (fileName);
			BufferedWriter bw = new BufferedWriter (fw);
			
			PrintWriter file = new PrintWriter (bw);
			file.print(data);
			file.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}       
	}

	private static void fileWriter(String nomFichier, int headerSize, ByteArrayOutputStream header, ByteArrayOutputStream data)
	{
		try {

			DataOutputStream dataOutput =
				new DataOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(nomFichier))) ;
			
			dataOutput.write(toBytes(headerSize));
			dataOutput.write(header.toByteArray());
			dataOutput.write(data.toByteArray());
			dataOutput.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * In to Bytes Array method to bypass java overhead. 
	 * Source : http://stackoverflow.com/questions/1936857/convert-integer-into-byte-array-java
	 */
	public static byte[] toBytes(int i)
	{
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /*>> 0*/);

		return result;
	}

	/*
	 * 
	 */
	private static String fileReader(String fileName)
	{
		String result = null;
		DataInputStream in = null;

		try {
			File f = new File(fileName);
			in = new DataInputStream(new FileInputStream(f));
			
			byte[] buffer = new byte[(int) f.length()];
			
			in.readFully(buffer);
			
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("Bad bad", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) { /* ignore it */
			}
		}
		return result;

	}

	public static Hashtable <String,Character> decodeTable;
	
	/*
	 * Read huffman compressed file
	 */
	private static ByteArrayOutputStream fileReaderBinary(String fileName) throws ClassNotFoundException{
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName))) ;
			byte[] headerLengthBytes = new byte[4];
			
			ByteBuffer headerLengthBuffer = ByteBuffer.wrap(headerLengthBytes);
			reader.read(headerLengthBytes);
			int headerlength = headerLengthBuffer.getInt();
			byte[] headerBuffer = new byte[headerlength]; 
			reader.read(headerBuffer);
			
			ByteArrayInputStream input = new ByteArrayInputStream(headerBuffer);
			ObjectInputStream ois = new ObjectInputStream(input);
			decodeTable = (Hashtable<String, Character>) ois.readObject();
			
			int s;
			while(reader.available()>0){
				s = reader.readUnsignedByte();
				data.write((byte)s);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	/*
	public static String toString(byte b){
		StringBuilder binary = new StringBuilder();

		int val = b;
		for (int i = 0; i < 8; i++)
		{
			binary.append((val & 128) == 0 ? 0 : 1);
			val <<= 1;
		}

		return binary.toString();
	}*/
}
