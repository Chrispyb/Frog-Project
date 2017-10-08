import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class DSP {

	boolean exitLoop = false;
	int windowSize = 512;
	final int frameSize = 1024;
	ArrayList<ArrayList<double[]>> buffer;
	ArrayList<String[]> outputs;
	
	int max = 0;
	int numC = 0;
	int sizeF = 0;
	
	public DSP() throws IOException{
	
		Scanner scanner = new Scanner(System.in);
		
		buffer = new ArrayList<ArrayList<double[]>>();
		outputs = new ArrayList<String []>();
		System.out.println("");
		System.out.println("Welcome to DSP interface");
		System.out.println("");
		
		while(!exitLoop)
		{
			System.out.println("Please select an option:");
			printOptions();
			
			switch(scanner.nextLine())
			{
			
			case "1":
				openWav(scanner);
				break;
			case "2":
				createTrainerO();
				break;
			case "3":
				returnToMain();
				break;
			case "4":
				build(scanner);
				break;
			default:
				System.out.println("Invalid Option");
				break;
			}
		}
	}
	
	public void printOptions(){
		
		System.out.println("");
		System.out.println("Option 1: Open a .wav file");
		System.out.println("Option 2: Convert current .wav file into training file");
		System.out.println("Option 3: Return to original interface.");
		System.out.println("Option 4: Prebuild");
		
	}
	
	public void openWav(Scanner scanner)
	{
		
		 try
	      {
			 ArrayList<double[]> tempBuffer = new ArrayList<double[]>();
			 
			 System.out.print("Enter .wav file location: ");
			 String wavLocation = scanner.nextLine();
			 
			 String stringNew = wavLocation.replace("\\", "/");
			 
	         // Open the wav file specified as the first argument
	         WavFile wavFile = WavFile.openWavFile(new File(stringNew));

	         // Display information about the wav file
	         wavFile.display();

	         // Get the number of audio channels in the wav file
	         int numChannels = wavFile.getNumChannels();
	         
	        
	         System.out.println("1");
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
	         
				FFT fft = new FFT(256, -1);
				ArrayList<double[]> newBuffer = new ArrayList<double[]>();
				sizeF+= tempBuffer.size();
				
				for(int i = 0; i < tempBuffer.size(); i++)
				{
						fft.transform(tempBuffer.get(i));
						
						double [] buf = new double[128];
						
						for(int j = 128; j < 256; j++)
						{
							buf[j-128] = (double) tempBuffer.get(i)[j];
						}
						double temp1[] = normalizeFunction(tempBuffer.get(i), windowSize);
						
						//double temp2[] = window(temp1, 8);
					
							
						newBuffer.add(temp1);
				}
				
	         if(tempBuffer.size() > max)
	        	 max = tempBuffer.size();
	         
	         buffer.add(newBuffer);
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
		 
			System.out.println("How many outputs?: ");
			
			int numOut = Integer.parseInt(scanner.nextLine());
			
			String val[] = new String[numOut];
			
			for(int i = 0; i < numOut; i++)
			{
				System.out.println("Please enter a value between 0 and 1 for the desired output #" + i + ": ");
				val[i] = scanner.nextLine();
			}
			
			outputs.add(val);
	}
	
	public void createTrainer() throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter a location to save this file: ");
		
		String location = scanner.nextLine();
		
		
		
		File file = new File(location);
		
		PrintWriter writer = new PrintWriter(new FileWriter(file), true);
		
		int totalSize = 0;
		
		int sizes[] = new int[buffer.size()];
		
		for(int i = 0; i < buffer.size(); i++)
			sizes[i] = buffer.get(i).size();
		
		
		writer.println(max * buffer.size());
		
		for(int c = 0; c < max; c++)
		{	
			for(int i = 0; i < buffer.size(); i++)
			{
				for(int p = 0; p < buffer.get(i).get(c % buffer.get(i).size()).length; p++)
				{
					writer.println(buffer.get(i).get(c % buffer.get(i).size())[p]);
				}
				
				for(int p = 0; p <outputs.get(i).length; p++)
				{
					writer.println(outputs.get(i)[p]);
				}
			}
		}
		writer.close();
	}
	
	public void createTrainerO() throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		
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
				
				for(int k = 0; k < outputs.get(i).length; k++)
					writer.println(outputs.get(i)[k]);
			}
		}
		writer.close();
	}
	
	public double[] normalizeFunction(double[] window, int fSize)
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
	
	public double[] getChannelData(double[] buf, int start)
	{
		double r[] = new double[windowSize];
		
		for(int i = 0; i < windowSize; i++)
			r[i] = buf[start + i];
		
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
	
	public void build (Scanner scanner) throws IOException
	{
		String[] bullFrog = {"1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] americanToad = {"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] easternSpadeFoot = {"0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] fowlersToad = {"0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] grayTreeFrog = {"0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] greenFrog = {"0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0" };
		String[] northernLeopardFrog = {"0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0" };
		String[] pickerelFrog = {"0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0" };
		String[] springPeeper = {"0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0" };
		String[] woodFrog = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0" };
		String[] crickets = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0" };
		String[] silence = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0" };
		String[] nightSounds = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1" };
		//String[] people = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1" };
		
		for(int i = 0; i < 4; i++)
		{
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/BullFrogs/Bf" + (i+1) + ".wav", bullFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/AmericanToad/AT" + (i + 1) + ".wav", americanToad);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/EasternSpadeFoot/ESP" + (i + 1) + ".wav", easternSpadeFoot);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/FowlersToad/FT" + (i + 1) + ".wav", fowlersToad);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/GrayTreeFrog/GTF" + (i+1) + ".wav", grayTreeFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/GreenFrog/GF" + (i+1) + ".wav", greenFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/NLeopardFrog/NLF" + (i+1) + ".wav", northernLeopardFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/PickerelFrog/PF" + (i+1) + ".wav", pickerelFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/SpringPeepers/SP" + (i+1) + ".wav", springPeeper);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/WoodFrog/WF" + (i+1) + ".wav", woodFrog);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/Crickets/CR" + (i+1) + ".wav", crickets);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/Silence/SL" + (i+1) + ".wav", silence);
			openWav(scanner,"C:/Users/Chris/Desktop/ATP Grant/SoundFiles/Wind/WI" + (i+1) + ".wav", nightSounds);
			//openWav(scanner,"C:\\Users\\Chris\\Desktop\\ATP Grant\\SoundFiles\\Fw__Calls\\Crowd Talking [Free Sound Effects].wav", people);
		}
		
		createTrainerO();
		
	}
	
	
	
	public void openWav(Scanner scanner, String loc, String[] outputsI)
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
	         
				FFT fft = new FFT(256, -1);
				ArrayList<double[]> newBuffer = new ArrayList<double[]>();
				sizeF+= tempBuffer.size();
				
				for(int i = 0; i < tempBuffer.size(); i++)
				{
						fft.transform(tempBuffer.get(i));
						double [] buf = new double[128];
						
						for(int j = 128; j < 256; j++)
						{
							buf[j-128] = (double) tempBuffer.get(i)[j];
						}
						double temp1[] = normalizeFunction(tempBuffer.get(i), windowSize);
						
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
}
