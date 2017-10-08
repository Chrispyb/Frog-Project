import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Interface {

	static boolean quit = false;
	static Network network;
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to Basic Network Builder");
		System.out.println("");
		
		while(!quit)
		{
			printOptions();
			
			switch(scanner.nextLine())
			{
				case "1":
					create(scanner);
					break;
				case "2":
					load(scanner);
					break;
				case "3":
					save(scanner);
					break;
				case "4":
					delete();
					break;
				case "5":
					train(scanner);
					break;
				case "6":
					test(scanner);
					break;
				case "7":
					dsp();
					break;
				case "8":
					saveVerilog(scanner);
					break;
				case "9":
					quit();
					break;
				case "10":
					printAsC(scanner);
					break;
				case "11":
					printToFile(scanner);
					break;
				case "12":
					automateTraining(scanner);
					break;
				default:
					System.out.println("Invalid Input. Please Enter a valid option.");
					break;
			}
			
		}
	}
	
	public static void printOptions()
	{
		System.out.println("Choose an option:");
		System.out.println("");
		
		System.out.println("Option '1': Create a new network from scratch.");
		System.out.println("Option '2': Load a network from the file system.");
		System.out.println("Option '3': Save the current network.");
		System.out.println("Option '4': Delete the current neural network.");
		System.out.println("Option '5': Train this neural network using a training file.");
		System.out.println("Option '6': Test the network using a input file.");
		System.out.println("Option '7': Digital signal processing interface.");
		System.out.println("Option '8': Save Network as Verilog file.");
		System.out.println("Option '9': Quit this simulation.");
		System.out.println("Option '10': Print Connections As C code");
		System.out.println("Option '11': Print to a file");
		System.out.println("Option '12': Automate training/testing");
		System.out.print("Option?:");
	}
	
	public static void create(Scanner scanner){
		
		System.out.println("");
		
		System.out.print("Enter the number of input neurons for your network:");
		
		int inputSize = Integer.parseInt(scanner.nextLine());
		
		System.out.println("");
		
		System.out.print("Enter the number of hidden neurons for your network:");
		
		int hiddenSize = Integer.parseInt(scanner.nextLine());
		
		System.out.println("");
		
		System.out.print("Enter the number of output neurons for your network:");
		
		int outputSize = Integer.parseInt(scanner.nextLine());
		
		System.out.println("");
		
		network = new Network();
		network.setup(inputSize, hiddenSize, outputSize, 4);
		
		System.out.println("Network Created with ISize: " + inputSize + " HSize: " + hiddenSize + " OSize: " + outputSize);
		System.out.println("");
	}
	
	public static void load(Scanner scanner) throws NumberFormatException, IOException{
		
		if(network != null)
		{
			System.out.print("Enter the location of the network file to open: ");
			
			String string = scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
			
			File file = new File(stringNew);
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			network.load(reader);
			
			for(int i = 0; i < network.connections.size(); i++)
			{
				System.out.println("Weight " + i + " :" + network.connections.get(i).weight);
			}
			
			System.out.println("Network Succesfully Loaded.");
			
			reader.close();
		}
		else
			System.out.println("No Network exists. Create a network before loading.");
	}
	
	public static void save(Scanner scanner) throws IOException{
		
		if(network != null)
		{
			System.out.print("Enter a location to save the network: ");
			
			String string = scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
					
			File file = new File(stringNew);
		
			PrintWriter writer = new PrintWriter(new FileWriter(file), true);
			
			network.save(writer);
			
			System.out.println("Network Succesfully Saved.");
			
			writer.close();
		}
		else
			System.out.println("No Network exists. Create a network before saving.");
	}
	
	public static void delete(){
		
		network = null;
	}
	
	public static void train(Scanner scanner) throws IOException{
		
		if(network != null)
		{
			
			System.out.print("Number of training Epochs?: ");
			
			int iterations  = Integer.parseInt(scanner.nextLine());
			
			System.out.print("Enter the location of the trainer file to open: ");
			
			String string = scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
			
			File file = new File(stringNew);
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			int numTrainers = Integer.parseInt(reader.readLine());
			
			ArrayList<float []> inputs = new ArrayList<float []>();
			ArrayList<float []> outputs = new ArrayList<float []>();
			
			for(int k = 0; k < numTrainers; k++)
			{
				float in[] = new float[network.inputLayer.size()];
				float out[] = new float[network.outputLayer.size()];
			 
				for(int i = 0; i < network.inputLayer.size(); i++)
				{	
					in[i] = Float.parseFloat(reader.readLine());
				}
				
				for(int i = 0; i < network.outputLayer.size(); i++)
				{
					out[i] = Float.parseFloat(reader.readLine());
					
				}
				
				inputs.add(in);
				outputs.add(out);
				
			}
			
			Random random = new Random();
			
			for(int i = 0; i < iterations; i++)
			{
				System.out.println(i + " /" + iterations + " of training complete.");
				float c =  1; //random.nextFloat();
				for(int j = 0; j < numTrainers/10; j++)
				{
					int p = random.nextInt(numTrainers);
					network.trainNetwork(inputs.get(p), outputs.get(p), c, numTrainers/10,(float) 0.00001);
				}
			}
			
			reader.close();
			System.out.println("Network Succesfully Trained.");
		}
		else
			System.out.println("No Network exists. Create a network before training.");
	}
	
	public static void trainA(Scanner scanner, String loc, int numEpochs) throws IOException{
		
		if(network != null)
		{
			
			//System.out.print("Number of training Epochs?: ");
			
			int iterations  = numEpochs; //Integer.parseInt(scanner.nextLine());
			
			//System.out.print("Enter the location of the trainer file to open: ");
			
			String string = loc; //scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
			
			File file = new File(stringNew);
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			int numTrainers = Integer.parseInt(reader.readLine());
			
			ArrayList<float []> inputs = new ArrayList<float []>();
			ArrayList<float []> outputs = new ArrayList<float []>();
			
			for(int k = 0; k < numTrainers; k++)
			{
				float in[] = new float[network.inputLayer.size()];
				float out[] = new float[network.outputLayer.size()];
			 
				for(int i = 0; i < network.inputLayer.size(); i++)
				{	
					in[i] = Float.parseFloat(reader.readLine());
				}
				
				for(int i = 0; i < network.outputLayer.size(); i++)
				{
					out[i] = Float.parseFloat(reader.readLine());
					//System.out.println(out[i]);
					
				}
				
				//System.out.println("");
				inputs.add(in);
				outputs.add(out);
				
			}
			
			Random random = new Random();
			
			for(int i = 0; i < iterations; i++)
			{
				System.out.println(i + " /" + iterations + " of training complete.");
				float c =  1; //random.nextFloat();
				for(int j = 0; j < numTrainers/20; j++)
				{
					int p = random.nextInt(numTrainers);
					network.trainNetwork(inputs.get(p), outputs.get(p), c, numTrainers/20,(float) 0.01);
				}
			}
			
			reader.close();
			System.out.println("Network Succesfully Trained.");
		}
		else
			System.out.println("No Network exists. Create a network before training.");
	}
	
	public static void test(Scanner scanner) throws IOException{
		
		if(network != null)
		{
			
			File file2 = new File("C:/Users/Chris/Desktop/ATP Grant/SoundLog.txt");
			
			float avgError = 0;
			int p = 0;
			PrintWriter writer = new PrintWriter(new FileWriter(file2), true);
			
			System.out.print("Enter the location of the test file to open: ");
			
			String string = scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
			
			File file = new File(stringNew);
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			int numTrainers = Integer.parseInt(reader.readLine());
			
			ArrayList<float []> inputs = new ArrayList<float []>();
			ArrayList<float []> outputs = new ArrayList<float []>();
			
			ArrayList<float []> desired = new ArrayList<float []>();
			
			int answerCounter = 0;
			float [] answers = new float[15];
			
			for(int k = 0; k < numTrainers; k++, answerCounter++)
			{
				float in[] = new float[network.inputLayer.size()];
				float desiredO[] = new float[network.outputLayer.size()];
				
				for(int i = 0; i < network.inputLayer.size(); i++)
				{	
					in[i] = Float.parseFloat(reader.readLine());
				}
				
				for(int i = 0; i < network.outputLayer.size(); i++)
				{
					desiredO[i] = Float.parseFloat(reader.readLine());
					System.out.println("Desired output " + i + " : " + desiredO[i]);
				}
				
				inputs.add(in);
				desired.add(desiredO);
				
				float outP[] = network.feedForward(inputs.get(k));
				
				/*if(answerCounter == 10)
				{
					float max = 0;
					int element = 0;
					answerCounter = 0;
					
					for(int i = 0; i < answers.length; i++)
					{
						if(answers[i] > max)
							element = i;
					}
						answers = new float[15];
						
						switch(element){
							case 0:
								System.out.println("Bull Frog");
								break;
							case 1:
								System.out.println("American Toad");
								break;
							case 2:
								System.out.println("Eastern Spade Toad");
								break;
							case 3:
								System.out.println("Fowler's Toad");
								break;
							case 4:
								System.out.println("Gray Tree Frog");
								break;
							case 5:
								System.out.println("Green Frog");
								break;
							case 6:
								System.out.println("Northern Leopard Frog");
								break;
							case 7:
								System.out.println("Pickerel Frog");
								break;
							case 8:
								System.out.println("Spring Peeper");
								break;
							case 9:
								System.out.println("Wood Frog");
								break;
							case 10:
								System.out.println("Crickets Chirping");
								break;
							case 11:
								System.out.println("Birds Chirping");
								break;
							case 12:
								System.out.println("Owl");
								break;
							case 13:
								System.out.println("Humans Talking");
								break;
							case 14:
								System.out.println("Night Sounds");
								break;
						}
						
				}*/
				
				for(int i = 0; i < outP.length; i++)
					answers[i] += outP[i];
				
				float max = 0;
				int element = 0;
				int element2 = 0;
				
				for(int i = 0; i < desiredO.length; i++, p++)
				{
					if(outP[i] > max)
					{
						element = i;
						max = outP[i];
					}
				}
				
				max = 0;
				
				for(int i = 0; i < desiredO.length; i++, p++)
				{
					if(desiredO[i] > max)
					{
						element2 = i;
						max = desiredO[i];
					}
				}
				
				if(element != element2)
					avgError++;
				
				System.out.println("Output Neuron " + k + " output: ");
				
				for(int i = 0; i < network.outputLayer.size(); i++)
					System.out.println(" " + outP[i]);
					
				writer.println("Output Neuron " + k + " output: " + outP[0]);
			}
			
			avgError = avgError;
			
			System.out.println("Total Error = " + (float)(100 * avgError/numTrainers) + "%");
			
			writer.close();
			reader.close();
			
		}
		else
			System.out.println("No Network found. Please create a network before testing.");
		
	}
	
	public static float testA(Scanner scanner, String loc) throws IOException{
		
		if(network != null)
		{
			
			File file2 = new File("C:/Users/Chris/Desktop/ATP Grant/SoundLog.txt");
			
			float avgError = 0;
			int p = 0;
			PrintWriter writer = new PrintWriter(new FileWriter(file2), true);
			
			//System.out.print("Enter the location of the test file to open: ");
			
			String string = loc; //scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
			
			File file = new File(stringNew);
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			int numTrainers = Integer.parseInt(reader.readLine());
			
			ArrayList<float []> inputs = new ArrayList<float []>();
			ArrayList<float []> outputs = new ArrayList<float []>();
			
			ArrayList<float []> desired = new ArrayList<float []>();
			
			int answerCounter = 0;
			float [] answers = new float[15];
			
			for(int k = 0; k < numTrainers; k++, answerCounter++)
			{
				float in[] = new float[network.inputLayer.size()];
				float desiredO[] = new float[network.outputLayer.size()];
				
				for(int i = 0; i < network.inputLayer.size(); i++)
				{	
					in[i] = Float.parseFloat(reader.readLine());
				}
				
				for(int i = 0; i < network.outputLayer.size(); i++)
				{
					desiredO[i] = Float.parseFloat(reader.readLine());
					//System.out.println("Desired output " + i + " : " + desiredO[i]);
				}
				
				inputs.add(in);
				desired.add(desiredO);
				
				float outP[] = network.feedForward(inputs.get(k));
				
				/*if(answerCounter == 10)
				{
					float max = 0;
					int element = 0;
					answerCounter = 0;
					
					for(int i = 0; i < answers.length; i++)
					{
						if(answers[i] > max)
							element = i;
					}
						answers = new float[15];
						
						switch(element){
							case 0:
								System.out.println("Bull Frog");
								break;
							case 1:
								System.out.println("American Toad");
								break;
							case 2:
								System.out.println("Eastern Spade Toad");
								break;
							case 3:
								System.out.println("Fowler's Toad");
								break;
							case 4:
								System.out.println("Gray Tree Frog");
								break;
							case 5:
								System.out.println("Green Frog");
								break;
							case 6:
								System.out.println("Northern Leopard Frog");
								break;
							case 7:
								System.out.println("Pickerel Frog");
								break;
							case 8:
								System.out.println("Spring Peeper");
								break;
							case 9:
								System.out.println("Wood Frog");
								break;
							case 10:
								System.out.println("Crickets Chirping");
								break;
							case 11:
								System.out.println("Birds Chirping");
								break;
							case 12:
								System.out.println("Owl");
								break;
							case 13:
								System.out.println("Humans Talking");
								break;
							case 14:
								System.out.println("Night Sounds");
								break;
						}
						
				}*/
				
				for(int i = 0; i < outP.length; i++)
					answers[i] += outP[i];
				
				float max = 0;
				int element = 0;
				int element2 = 0;
				
				for(int i = 0; i < desiredO.length; i++, p++)
				{
					if(outP[i] > max)
					{
						element = i;
						max = outP[i];
					}
				}
				
				max = 0;
				
				for(int i = 0; i < desiredO.length; i++, p++)
				{
					if(desiredO[i] > max)
					{
						element2 = i;
						max = desiredO[i];
					}
				}
				
				if(element != element2)
					avgError++;
				
				//System.out.println("Output Neuron " + k + " output: ");
				
				//for(int i = 0; i < network.outputLayer.size(); i++){}
					//System.out.println(" " + outP[i]);
					
				writer.println("Output Neuron " + k + " output: " + outP[0]);
			}
			
			avgError = avgError;
			
			//System.out.println("Total Error = " + (float)(100 * avgError/numTrainers) + "%");
			
			writer.close();
			reader.close();
			return (float)(100*avgError/numTrainers);
			
		}
		else
		{
			System.out.println("No Network found. Please create a network before testing.");
			return 0;
		}
		
	}
	
	public static void saveVerilog(Scanner scanner) throws IOException{
		
		if(network != null)
		{
			System.out.print("Enter a location to save the network as a .v file: ");
			
			String string = scanner.nextLine();
			
			String stringNew = string.replace("\\", "/");
					
			File file = new File(stringNew);
		
			PrintWriter writer = new PrintWriter(new FileWriter(file), true);
			
			new VerilogFile(writer, network);
			
			System.out.println("Network Succesfully Saved.");
			
		}
		else
			System.out.println("No Network exists. Create a network before saving.");
	}
	
	public static void dsp() throws IOException{
		new DSP();
	}
	
	public static void quit(){
		quit = true;
	}
	
	public static void printAsC(Scanner scanner)
	{
		System.out.print("Enter object name for your neural network ");
		
		String name = scanner.nextLine();
		
		int connectionNum = 0;
		
		for(int i = 0; i < network.hiddenLayers.get(0).size(); i++)
		{
			for(int j = 0; j < network.inputLayer.size(); j++)
			{
				System.out.print(name + ".inputToHConnections[" + connectionNum + "] = " + network.inputLayer.get(j).outputs.get(i).weight + ";" );
				connectionNum++;
			}
		}
		
		connectionNum = 0;
		
		for(int i = 0; i < network.outputLayer.size(); i++)
		{
			for(int j = 0; j < network.hiddenLayers.get(0).size(); j++)
			{
				System.out.print(name + ".hiddenToOutputConnections[" + connectionNum + "] = " + network.hiddenLayers.get(0).get(j).outputs.get(i).weight + ";" );
				connectionNum++;
			}
		}
		
		System.out.println();
	}
	
	public static void automateTraining(Scanner scanner) throws IOException
	{

		ArrayList<String> testLocs = new ArrayList();
		
		System.out.println("Enter file with trainer file locations:");
		String trainerLoc = scanner.nextLine();
		
		System.out.println("Enter file with test file locations:");
		String testLoc = scanner.nextLine();
		
		File file = new File(testLoc);
		
		Scanner reader = new Scanner(file);
		
		while(reader.hasNext())
		{
			testLocs.add(reader.nextLine());
		}
		
		reader.close();
		
		float [] error = new float[testLocs.size()];
		float avgErr = 100;
		float lastError = 100;
		float minError = 100;
		
		int numLower = 0;
		
		while(numLower < 10)
		{
			lastError = avgErr;
			avgErr = 0;
			trainA(scanner, trainerLoc, 50);
			
			for(int i = 0; i < testLocs.size(); i++)
			{
				error[i] = testA(scanner,testLocs.get(i));
				avgErr += (float)error[i]/testLocs.size();
				System.out.println(error[i]);
			}
			
			if(avgErr < minError)
				minError = avgErr;
			
			System.out.println("Last Error = " + lastError);
			System.out.println("Average Error = " + avgErr);
			System.out.println("Minimum Error = " + minError);
			
			if(avgErr > lastError)
				numLower++;
			else
				numLower = 0;
			
			if(avgErr < 20)
				break;
		}
		
		
		
	}
	
	public static void printToFile(Scanner scanner) throws IOException
	{
		System.out.println("Enter Neural Network Name: ");
		String name = scanner.nextLine();
		
		System.out.print("Enter a location to save the network as a file: ");
		
		String string = scanner.nextLine();
		
		String stringNew = string.replace("\\", "/");
				
		File file = new File(stringNew);
	
		PrintWriter writer = new PrintWriter(new FileWriter(file), true);
		
		writer.println("Public class Loader{");
		writer.println("	Public Loader(Network network){");
		
		
		writer.println("		float [] conns = new float[" + network.connections.size() + "];");
		writer.println("		float [] inputs = new float[" + network.inputLayer.size() + "];");
		writer.println("		float [] h1b = new float[" + network.hiddenLayers.get(0).size() + "];");
		writer.println("		float [] h2b = new float[" + network.hiddenLayers.get(1).size() + "];");
		writer.println("		float [] outputb = new float[" + network.outputLayer.size() + "];");
		
		for(int i = 0; i < network.connections.size(); i++)
			writer.println("		conns[" + i + "] = (float)" + network.connections.get(i).weight + ";");
		
		for(int i = 0; i <network.inputLayer.size(); i++)
			writer.println("		inputs[" + i + "] = (float)" + network.inputLayer.get(i).bias + ";");
		
		for(int i = 0; i <network.hiddenLayers.get(0).size(); i++)
			writer.println("		h1b[" + i + "] = (float)" + network.hiddenLayers.get(0).get(i).bias + ";");
		
		for(int i = 0; i <network.hiddenLayers.get(1).size(); i++)
			writer.println("		h2b[" + i + "] = (float)" + network.hiddenLayers.get(1).get(i).bias + ";");
		
		for(int i = 0; i <network.outputLayer.size(); i++)
			writer.println("		outputb[" + i + "] = (float)" + network.outputLayer.get(i).bias + ";");
		
		writer.println("		network.copyData(conns, inputs, h1b, h2b, outputb);");
		writer.println("	}");
		writer.println("}");
		
		writer.close();
		
	}
}
