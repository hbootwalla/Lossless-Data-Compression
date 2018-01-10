 import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class HuffmanNode {
	int data;
	int frequency;
	HuffmanNode leftChild, rightChild;
	
	HuffmanNode(){
		this.data = 0;
		this.frequency = 0;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	HuffmanNode(int data, int frequency, HuffmanNode leftChild, HuffmanNode rightChild){
		this.data = data;
		this.frequency = frequency;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
				
	}
}

class PairingHeap{
	class Node{
		HuffmanNode data;
		Node child;
		Node leftSibling, rightSibling;
		
		public Node(HuffmanNode data, Node child, Node leftSibling, Node rightSibling){
			this.data = data;
			this.child = child;
			this.leftSibling = leftSibling;
			this.rightSibling = rightSibling;
		}
		public String toString(){
			return " "+ data +" ";
		}
	}
	
	public Node head = null;
	int size = 0;
	
	private Node meldPairingHeap(Node p1, Node p2){
		if(p1 == null)
			return p2;
		if(p2 == null)
			return p1;
		Node smallHeap = p1.data.frequency < p2.data.frequency ? p1 : p2;
		Node bigHeap = p1.data.frequency >= p2.data.frequency? p1 : p2;
		Node tempChild = smallHeap.child;
		smallHeap.child = bigHeap;
		bigHeap.rightSibling = tempChild;
		bigHeap.leftSibling = smallHeap; 
		if(tempChild != null)
			tempChild.leftSibling = bigHeap;
		return smallHeap;
	}
	
	private void insert(HuffmanNode data){
		Node newTree = new Node(data, null, null, null);
		size++;
		if(head == null){
			head = newTree;
		}
		else
			head = meldPairingHeap(head, newTree);
	}
	
	private Node removeMin(){
		Node headTemp = head;
		Node tempChild = head.child;
		head.child = null;
		LinkedList<Node> vector = new LinkedList<Node>();
		if(tempChild == null)
		{
			size--;
			head = null;
			return headTemp;
			
		}
		while(tempChild != null){
			tempChild.leftSibling = null;
			Node nextSibling = tempChild.rightSibling;
			tempChild.rightSibling = null;
			vector.addLast(tempChild);
			tempChild = nextSibling;
		}
		
		while(vector.size() > 1){
			Node p1 = vector.removeFirst();
			Node p2 = vector.removeFirst();
			Node merged = this.meldPairingHeap(p1, p2);
			vector.addLast(merged);
		}
		size--;
		head = vector.removeLast();
		return headTemp;
	}
	
	public void buildHeap(int[] frequency){
		for(int i = 0 ; i < frequency.length; i++){
			if(frequency[i] > 0){
				insert(new HuffmanNode(i, frequency[i], null, null));
			}
		}
	}
	
	public void createHuffmanTree() {
		// TODO Auto-generated method stub
		while(size >= 2){
			Node small = removeMin();
			Node big = removeMin();
			HuffmanNode huffmanNode = new HuffmanNode(-1, small.data.frequency + big.data.frequency, small.data, big.data);
			insert(huffmanNode);
		}
		head = removeMin();
	}
	
}

class BinaryHeap{

	int size;
	HuffmanNode[] heap;
	
	public BinaryHeap() {
		heap = new HuffmanNode[1000004];
		size = 0;
	}

	private HuffmanNode removeMin() {
		if(size<0)
			return null;
		HuffmanNode temp = heap[0];
		heap[0] = heap[size];
		heap[size] = null;
		size--;
		heapify(0);
		return temp;
	}

	private void heapify(int index) {
		int minChild = peekChild(index);
		if(minChild == index)
			return;
		else {
			HuffmanNode temp = heap[index];
			heap[index] = heap[minChild];
			heap[minChild] = temp;
			heapify(minChild);
		}
		
	}

	public HuffmanNode peek() {
		if(size >= 0){
			return heap[0];
		}
		return null;
	}

	private int getParent(int index) {
		return ((index+1)/2)-1;
	}

	private int getChild(int i, int j) {
		return 2*(j+1)+i-1;
	}

	private int peekChild(int index) {
		int smallest = index;
		for(int i=0; i < 2; i++){
			if(getChild(i,index) <= size){
					if(heap[getChild(i,index)].frequency < heap[smallest].frequency){
						smallest = getChild(i, index);
					}
			}
		}
		return smallest;
	}

	public void buildHeap(int[] frequencyArray) {
		if(frequencyArray == null)
			return;

		for(int i = 0; i < frequencyArray.length; i++){
			
			if(frequencyArray[i]>0){
				heap[size++]=new HuffmanNode(i, frequencyArray[i], null, null);
			}
		}

		if(size > 0)
			size--;

		for(int i = getParent(size); i>=0; i--){
			heapify(i);
		}
		
	}

	public void createHuffmanTree(){
		while(size > 0){
			HuffmanNode minNode = removeMin();
			HuffmanNode head = peek(); 
			HuffmanNode newNode = new HuffmanNode(-1, minNode.frequency + head.frequency, minNode, head);
			heap[0] = newNode;
			heapify(0);
		}
	}
}

class FourWayHeap {

	int size;
	HuffmanNode[] heap;
	
	public FourWayHeap() {
		size = 3;
		heap = new HuffmanNode[1000004];
	}
	
	private void heapify(int parent) {
		int minChild = peekChild(parent);
		if(minChild == parent)
			return;
		else {
			HuffmanNode temp = heap[parent];
			heap[parent] = heap[minChild];
			heap[minChild] = temp;
			heapify(minChild);
		}
	}
	
	private HuffmanNode removeMin() {
		if(size < 3)
			return null;
		HuffmanNode temp = heap[3];
		heap[3] = heap[size];
		heap[size] = null;
		size--;
		heapify(3);
		return temp;
	}

	public HuffmanNode peek() {
		if(size >= 3){
			return heap[3];
		}
		return null;
	}

	private int getParent(int index) {
		if(index > 3)
			return ((index - 3- 1)/4) + 3;
		return -1;
	}

	private int getChild(int i, int j) {
	 	return ((j - 3) * 4) + i + 3;
	}
	
	private int peekChild(int index) {
		int smallest = index;
		for(int i = 1; i <= 4; i++){
			if(getChild(i,index) <= size){
					if(heap[getChild(i,index)].frequency < heap[smallest].frequency){
						smallest = getChild(i, index);
					}
			}
		}
		return smallest;
	}
	
	public void buildHeap(int[] frequency) {
		if(frequency == null)
			return;
		
		for(int i = 0; i < frequency.length; i++){
			if(frequency[i] > 0){
				heap[size++] = new HuffmanNode(i, frequency[i], null, null);
			}
		}
		
		if(size > 3)
			size--;
		
		for(int i = getParent(size); i >= 3; i--){
			heapify(i);
		}
	}
	
	public void createHuffmanTree(){
		while(size > 3){
			HuffmanNode minNode = removeMin();
			HuffmanNode head = peek(); 
			HuffmanNode newNode = new HuffmanNode(-1, minNode.frequency + head.frequency, minNode, head);
			heap[3] = newNode;
			heapify(3);
		}
	}
}


public class encoder {

	HashMap<Integer, String> codeTable = new HashMap<Integer,String>();
	
	private void generateCode(HuffmanNode root, StringBuilder strBuf){
		if(root != null && root.leftChild == null && root.rightChild == null){
			codeTable.put(root.data, strBuf.toString());
			return;
		}
		if(root.leftChild != null){
			strBuf.append('0');
			generateCode(root.leftChild, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
		if(root.rightChild != null){
			strBuf.append('1');
			generateCode(root.rightChild, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		int[] frequencyTable = new int[1000000];
		File codeFile = new File("code_table.txt");
		File inputFile = new File(args[0]);
		
		BufferedWriter codeFileWriter = null;
		BufferedReader inputReader = null;
		BufferedOutputStream encodedBinOutput = null;
		
		encoder enc = new encoder();
		FourWayHeap tree = null;
		try {
		    System.out.println("Performance Analysis on File of Size: " + inputFile.length() + "bytes");
			
			inputReader = new BufferedReader(new FileReader(inputFile));
		    readDataFromFile(inputReader, frequencyTable);
		    System.out.println("Reading Time: " + (System.currentTimeMillis()-start)+"ms");
		    
		    
		    // If you wish to test the performance of the three data structures,
		    // uncomment the following three lines of function calls.
		    // testBinaryHeap(frequencyTable);
		    // testFourWayHeap(frequencyTable);
		    // testPairingHeap(frequencyTable);
			
			
		    start = System.currentTimeMillis();
		    tree = new FourWayHeap(); 
		    tree.buildHeap(frequencyTable);
		    tree.createHuffmanTree();
		    System.out.println("Time to Build the heap: " + (System.currentTimeMillis()-start)+"ms");
		    
		    
		    start = System.currentTimeMillis();
		    enc.generateCode(tree.peek(), new StringBuilder());
		    codeFileWriter = new BufferedWriter(new FileWriter(codeFile));
		    enc.writeCodeTable(codeFileWriter);
			codeFileWriter.close();
			System.out.println("Time to write Code File: " +(System.currentTimeMillis()-start)+"ms");
			
			start = System.currentTimeMillis();
	    	encodedBinOutput = new BufferedOutputStream(new FileOutputStream("encoded.bin"));
	    	inputReader = new BufferedReader(new FileReader(inputFile));
	    	enc.encodeInput(inputReader, encodedBinOutput);
	    	System.out.println("Encoding Time: " +(System.currentTimeMillis()-start)+"ms");
		}
		catch(IOException ioE){
			System.out.println("Exception occured  " + ioE.getMessage());
			ioE.printStackTrace();
		}
		finally{
			if(encodedBinOutput != null){
				encodedBinOutput.close();
		    }
		    if (codeFileWriter != null){
		        codeFileWriter.close();
		    }		    
		}
	}
	
	public static void testPairingHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			PairingHeap pairingHeap = new PairingHeap();
			pairingHeap.buildHeap(frequencyTable);
			pairingHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Average Pairing Heap Performance: " + ((end - start)/10.0f) + "ms");
	}

	public static void testFourWayHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			FourWayHeap fourWayHeap = new FourWayHeap();
			fourWayHeap.buildHeap(frequencyTable);
			fourWayHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Average Four Way Heap Performance: " + ((end - start)/10.0f) + "ms");
	}

	public static void testBinaryHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			BinaryHeap binaryHeap = new BinaryHeap();
			binaryHeap.buildHeap(frequencyTable);
			binaryHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Average Binary Heap Performance: " + ((end - start)/10.0f) + "ms");
		
	}

	public void writeCodeTable(BufferedWriter codeFileWriter) throws IOException{
		Iterator<Integer> it = codeTable.keySet().iterator();
		while(it.hasNext()){
			Integer temp = it.next();
			codeFileWriter.write(temp + " " + codeTable.get(temp));
			codeFileWriter.newLine();
		}
	}
	
	public static void readDataFromFile(BufferedReader inputReader, int[] frequencyTable) throws IOException{
		String str; 
		while((str = inputReader.readLine()) != null){
			if(str.length() > 0){
		        frequencyTable[(Integer.parseInt(str))]++;
			}
		}
	}
	
	public void encodeInput(BufferedReader inputReader, BufferedOutputStream encodeFileWriter) throws IOException{
			StringBuilder strBuf = new StringBuilder(); 
			String line;
			BitSet bitSet = new BitSet();
			int length = 0;
			while((line = inputReader.readLine()) != null){
				if(line.length() > 0){
				strBuf.append(codeTable.get(Integer.parseInt(line)));
				
				for(int i = 0 ; i < strBuf.length(); i++){
					if(strBuf.charAt(i) == '1'){
						bitSet.set(length + i);
					}
				}
				
				length += strBuf.length();
				strBuf.setLength(0);
				}
			}	
			encodeFileWriter.write(bitSet.toByteArray());	
		}
}
