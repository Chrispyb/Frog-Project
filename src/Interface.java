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
import java.util.IllegalFormatException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Interface {

	static boolean quit = false;
	static Network network;
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		
		
		System.out.println("Welcome to Basic Neural Network Builder");
		System.out.println("");
		
		while(!quit)
		{
			Scanner scanner = new Scanner(System.in);
			printOptions();
			
			try {
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
						quit();
						break;
					/*case "9":
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
					case "13":
						feedBackward();*/
					default:
						System.out.println("Invalid Input. Please Enter a valid option.");
						break;
				}
			}
			catch(FileNotFoundException e){
				System.out.println(e.getMessage());
			}
			catch(NumberFormatException e){
				System.out.println(e.getMessage());
			}
			catch(Exception e){
				System.out.println(e.getMessage());
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
		System.out.println("Option '8': Quit This Program.");
		/*System.out.println("Option '9': Quit this simulation.");
		System.out.println("Option '10': Print Connections As C code");
		System.out.println("Option '11': Print to a file");
		System.out.println("Option '12': Automate training/testing");
		System.out.println("Option '13': Feed Backward");*/
		System.out.print("Option?: ");
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
					//System.out.println("Desired output " + i + " : " + desiredO[i]);
				}
				
				inputs.add(in);
				desired.add(desiredO);
				
				float outP[] = network.feedForward(inputs.get(k));
				
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
					//System.out.println(" " + outP[i]);
					
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
	
	public static void dsp() throws IOException, NumberFormatException, WavFileException{

			new DSP();

	}
	
	public static void quit(){
		quit = true;
	}
	
	public static void feedBackward(){
		network.clearInternals();
		float [] out = network.feedBackward();
		
		for(int i = 0; i < out.length; i++)
			System.out.println(out[i]);
	}
	
}
