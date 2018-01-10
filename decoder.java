
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

class DecoderNode {

	Integer data;
	boolean isEnd;
	DecoderNode leftChild,rightChild;
	
	public DecoderNode() {
		data = null;
		isEnd = false;
		leftChild = null;
		rightChild = null;
	}
	
	public DecoderNode(Integer data, boolean isLeaf, DecoderNode leftChild, DecoderNode rightChild) {
		this.data = data;
		this.isEnd = isLeaf;
		this.leftChild = null;
		this.rightChild = null;
	}
	
}

public class decoder {
	
	DecoderNode root;
	
	public decoder(){
		root = new DecoderNode();
	}
	
	public static void main(String args[]) throws IOException{
		decoder dec = new decoder();
		File codeTableFile = new File(args[1]);
		File encodedBinaryFile = new File(args[0]);
		File decodedFile = new File("decoded.txt");

		FileInputStream fileinputStream=null;

		BufferedWriter decodeFileWriter=null;
		BufferedReader codeTableReader=null;
		try{
			
			long start = System.currentTimeMillis();
			codeTableReader = new BufferedReader(new FileReader(codeTableFile));
			
			dec.codeTableToDecoderTree(codeTableReader);
			
			decodeFileWriter = new BufferedWriter(new FileWriter(decodedFile));
			fileinputStream = new FileInputStream(encodedBinaryFile);
			
			dec.decodeData(decodeFileWriter,fileinputStream);
			
			System.out.println("Decoding Time: " + (System.currentTimeMillis()-start) + "ms");
		}
		finally{
			if (fileinputStream != null) {
				fileinputStream.close();
			}
			if(decodeFileWriter != null){
				decodeFileWriter.close();
			}
			if(codeTableReader != null){
				codeTableReader.close();
			}
		}
	}
	
	private void insertIntoTree(String code, DecoderNode node, Integer data){
		for(int i = 0; i < code.length(); i++){
			if(code.charAt(i) == '0'){
				if(node.leftChild == null){
					node.leftChild = new DecoderNode();
				}
				node = node.leftChild;
			}
			else{
				if(node.rightChild == null){
					node.rightChild = new DecoderNode();
				}
				node = node.rightChild;
			}
		}
		node.data = data;
		node.isEnd = true;
	}
	
	private void decodeData(BufferedWriter decodeFileWriter, FileInputStream encodedByteStream) throws IOException{
		DecoderNode temp = root;		
		byte[] b = new byte[encodedByteStream.available()];
		
		encodedByteStream.read(b);
		BitSet bitset=BitSet.valueOf(b);
		bitset.set((b.length) * 8);
		
		for(int i = 0;i < bitset.length()-1; i++){
			if(bitset.get(i) == false){
				temp = temp.leftChild;
			}
			else{
				temp = temp.rightChild;
			}
			if(temp.isEnd){	
				decodeFileWriter.write("" + temp.data);
				decodeFileWriter.write("\n");
				temp = root;
			}
		}
	}
	
	private void codeTableToDecoderTree(BufferedReader codeTableReader) throws IOException{
		String line = null;
		String[] dataValue = null;
		while ((line = codeTableReader.readLine()) != null) {
			dataValue=line.split(" ");
			insertIntoTree(dataValue[1], root, Integer.parseInt(dataValue[0]));
		}
	}
}
