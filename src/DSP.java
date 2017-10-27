import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class DSP {

	boolean exitLoop = false;
	ArrayList<ArrayList<double[]>> buffer;
	ArrayList<String> outputs;
	
	int max = 0;
	int numC = 0;
	int sizeF = 0;
	
	public DSP() throws IOException, NumberFormatException, WavFileException{
	
		Scanner scanner = new Scanner(System.in);
		
		buffer = new ArrayList<ArrayList<double[]>>();
		outputs = new ArrayList<String >();
		
		createTrainerFromFile();

	}
	
	public void printOptions(){
		
		System.out.println("");
		System.out.println("Option 1: Open a .wav file");
		System.out.println("Option 2: Convert current .wav file into training file");
		System.out.println("Option 3: Return to original interface.");
		System.out.println("Option 4: Prebuild");
		System.out.print("Option?: ");
		
	}
	


	

	
	public double[] normalizeFunction(double[] window) throws ArithmeticException
	{
		double r[] = new double[128];
		double max = .01;
		
		
		/*for(int i = 0; i < fSize; i++)
		{
			window[i] = Math.abs(window[i]);
		}*/
		
		for(int i = 0; i < 128; i++)
		{
			if(max < Math.abs(window[i]))
				max = Math.abs(window[i]);
		}
		
		double mult = 1/max;
		
		int p = 0;
		
		for(int i = 0; i < 128; i++)
		{
			r[i] = window[i] * mult; /// max;
			
			if(Float.isNaN((float) r[i]))
				r[i] = 0;
		}

		return r;
	}
	
	public double[] window(double [] arg, int scale)
	{
		double r[] = new double[arg.length/scale];
		
		int c = 0;
		int p = 0;
		double max = 0;
		
		for(int i = 0; i < arg.length; i++, c++)
		{
			if(c == scale)
			{
				c = 0;
				r[p] = max;
				p++;
				max = 0;
			}
			
			if(arg[i] > max)
				max = arg[i];
			
			
		}
		
		return r;
	}
	
	
	public static double[] getFirstChannel(double[] frame, int channelSize)
	{
		double[] newFrame = new double[channelSize];
		
		for(int i = 0; i < channelSize; i++)
		{
			newFrame[i] = frame[i];
		}
		
		return newFrame;
	}
	
	public void returnToMain()
	{
		exitLoop = true;
	}
	

	
	
	
	public void openWav(String loc, String outputsI, int windowSize, int fftSize)
	{
		
		 try
	      {
			 ArrayList<double[]> tempBuffer = new ArrayList<double[]>();
			 
			 
			 String stringNew = loc.replace("\\", "/");
			 
	         // Open the wav file specified as the first argument
	         WavFile wavFile = WavFile.openWavFile(new File(stringNew));

	         // Display information about the wav file
	         wavFile.display();

	         // Get the number of audio channels in the wav file
	         int numChannels = wavFile.getNumChannels();
	         
	         // Create a buffer of 100 frames
	         int framesRead;
	         
	         double tempBuff[] = new double[numChannels*windowSize];
	         
	         do
	         {
	        	
	            // Read frames into buffer
	        	
	            framesRead = wavFile.readFrames(tempBuff, windowSize);
	     
	            double newBuffer[] = getFirstChannel(tempBuff, windowSize);
	            tempBuffer.add(newBuffer);
	         
	            
	         }
	         while (framesRead != 0);

	         // Close the wavFile
	         wavFile.close();
	         
				FFT fft = new FFT(fftSize, -1);
				ArrayList<double[]> newBuffer = new ArrayList<double[]>();
				sizeF+= tempBuffer.size();
				
				for(int i = 0; i < tempBuffer.size(); i++)
				{
						fft.transform(tempBuffer.get(i));
						double [] buf = new double[fftSize/2];
						
						for(int j = fftSize/2; j < fftSize; j++)
						{
							buf[j-fftSize/2] = (double) tempBuffer.get(i)[j];
						}
						double temp1[] = normalizeFunction(buf);
						System.out.println(buf[0]);
						
						//double temp2[] = window(temp1, 8);
					
							
						newBuffer.add(temp1);
				}
				
	         
	         buffer.add(newBuffer);
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
			
			
			outputs.add(outputsI);
	}
	
	public void createTrainerO(Scanner scanner) throws IOException
	{
		
		System.out.println("Please enter a location to save this file: ");
		
		String location = scanner.nextLine();
		
		
		
		File file = new File(location);
		
		PrintWriter writer = new PrintWriter(new FileWriter(file), true);
		
		int totalSize = 0;
		
		int sizes[] = new int[buffer.size()];
		
		for(int i = 0; i < buffer.size(); i++)
			sizes[i] = buffer.get(i).size();
		
		
		writer.println(sizeF);
		
		for(int i = 0; i < buffer.size(); i++)
		{
			for(int j = 0; j < buffer.get(i).size(); j++)
			{
				for(int k = 0; k < buffer.get(i).get(j).length; k++)
					writer.println(buffer.get(i).get(j)[k]);
				
				for(int k = 0; k < outputs.size(); k++)
					writer.println(outputs.get(i).charAt(i));
			}
		}
		writer.close();
	}
	
	public void createTrainerFromFile() throws NumberFormatException, IOException, WavFileException, FileNotFoundException {
		
		System.out.println("Please enter your list file to convert to training file:");
		Scanner scanner = new Scanner(System.in);
		
		ArrayList<String> fileLocs = new ArrayList<String>();
		ArrayList<String> outputs = new ArrayList<String>();
		
		String fileLoc = scanner.nextLine();
		
		File file = new File(fileLoc);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int windowSize = Integer.parseInt(reader.readLine());
		
		int fftRes = Integer.parseInt(reader.readLine());
		
		int numFiles = Integer.parseInt(reader.readLine());
		
		for(int i = 0; i < numFiles; i++){
			
			fileLocs.add(reader.readLine());
			outputs.add(reader.readLine());
			System.out.println("" + fileLocs.get(i));
			openWav(fileLocs.get(i), outputs.get(i), windowSize, fftRes);
		}
		
		createTrainerO(scanner);
		reader.close();
		
	}
}
