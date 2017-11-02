import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Network {

	ArrayList<Neuron> inputLayer;
	ArrayList<ArrayList<Neuron>> hiddenLayers;
	ArrayList<Neuron> outputLayer;
	
	float[] currentInputs;
	float[] currentOutputs;
	
	float[] summedError;
	float learningRate = (float).001;
	
	boolean isResidual = true;
	
	
	ArrayList<Connection> connections;
	
	char [] numLayerG = { '0', '0', '0', '0'};
	
	ArrayList<char []> neuronPerLayerG; 
	
	int numHLayers;
	
	//Allocates space for the input layer, hidden layer, output layer, and connections, which are all array lists.
	public Network()
	{
		inputLayer = new ArrayList<Neuron>();
		hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		outputLayer = new ArrayList<Neuron>();
		connections = new ArrayList<Connection>();
	}
	
	public Network(boolean isResidual)
	{
		inputLayer = new ArrayList<Neuron>();
		hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		outputLayer = new ArrayList<Neuron>();
		connections = new ArrayList<Connection>();
		
		this.isResidual = isResidual;
	}
	
	//Add neuron to the input layer.
	public void addNeuronIL(Neuron n)
	{
		inputLayer.add(n);
	}
	
	//Add neuron to the hidden layer.
	public void addNeuronHL(Neuron n, int i)
	{
		hiddenLayers.get(i).add(n);
	}
	
	//Add neuron to the output layer.
	public void addNeuronOL(Neuron n)
	{
		outputLayer.add(n);
	}
	
	//Setup the network taking as paramaters the number of input layer neurons, hidden layer neurons,
	//and output layer neurons. Setups the connections between the layers so you don't have to!
	public void setup(int iSize, int hSize, int oSize, int numHiddenLayers)
	{
		summedError = new float[oSize];
		currentInputs = new float[iSize];
		currentOutputs = new float[oSize];
		
		numHLayers = numHiddenLayers;
		
		Random random = new Random();
		
		for(int i = 0; i < 4; i++)
		{
			int c = random.nextInt(2);
			
			if(c == 0)
				numLayerG[i] = '0';
			else
				numLayerG[i] = '1';
			
		}
		
		numHLayers = getBinary(numLayerG);
		
		neuronPerLayerG = new ArrayList<char []>();
		for(int i = 0; i < numHLayers; i++)
		{
			char [] newChar = {'0', '0', '0', '0', '0', '0', '0'};
			for(int j = 0; j < 7; j++)
			{
				int c = random.nextInt(2);
				
				if(c == 0)
					newChar[j] = '0';
				else
					newChar[j] = '1';
				
			}
			
			neuronPerLayerG.add(newChar);
		}
		

		//Create the neurons for input, hidden, and output layers.
		for(int i = 0; i < iSize; i++)
		{
			addNeuronIL(new Neuron(0,0, "I" + i, 0));
		}
		
		for(int i = 0; i < numHiddenLayers; i++)
		{
			hiddenLayers.add(new ArrayList<Neuron>());
			for(int j = 0; j < hSize; j++)
			{
				addNeuronHL(new Neuron(0,0, "H" + j, 0), i);
			}
		}
		
		for(int i = 0; i < oSize; i++)
		{
			addNeuronOL(new Neuron(0,0, "O" + i, 2));
		}
		
		
		//Setup the connections from the input to hidden layer.
		for(int i = 0; i < iSize; i++)
		{
			for(int j = 0; j < hSize; j++)
			{
				Connection c = new Connection(inputLayer.get(i), hiddenLayers.get(0).get(j), 1, 1, 1);
				connections.add(c);
				inputLayer.get(i).addOutputConnection(c);
				hiddenLayers.get(0).get(j).addBackwardConnection(c);
			}
		}
		
		for(int c = 0; c < numHiddenLayers - 1; c++)
		{
			for(int i = 0; i < hSize; i++)
			{
				for(int j = 0; j < hSize; j++)
				{
					Connection p = new Connection(hiddenLayers.get(c).get(i), hiddenLayers.get(c + 1).get(j), 1, 1, 1);
					connections.add(p);
					hiddenLayers.get(c).get(i).addOutputConnection(p);
					hiddenLayers.get(c+1).get(j).addBackwardConnection(p);
				}
			}
		}
		
		for(int i = 0; i < hSize; i++)
		{
			for(int j = 0; j < oSize; j++)
			{
				Connection c = new Connection(hiddenLayers.get(numHiddenLayers-1).get(i), outputLayer.get(j), 1, 1, 1);
				connections.add(c);
				hiddenLayers.get(numHiddenLayers-1).get(i).addOutputConnection(c);
				outputLayer.get(j).addBackwardConnection(c);
			}
		}
			
	}
	
	public int getBinary(char [] in)
	{
		int r = 0;
		
		for(int i = 0, c = in.length - 1; i < in.length; i++, c--)
		{
			if(in[i] == '1')
				r+= Math.pow(2, c);
		}
		
		return r;
	}
	//This function takes an array of floats(the input variables) as a parameter. It then adds these "inputs" to the input layer
	//of the neural network. These inputs are then activated, multiplied by their respective weights and sent to the hidden layer.
	//The same is then done from the hidden to output layer, the values of the activation functions of the output layer is then return
	//-ed as an array of floats.
	public float[] feedForward(float[] inputs)
	{
		
		for(int i = 0; i < inputs.length; i++)
			currentInputs[i] = inputs[i];
		
		for(int i = 0; i < inputs.length; i++)
		{
			inputLayer.get(i).addWeightedInput(inputs[i]);
			inputLayer.get(i).feedForward();
		}
		
		if(isResidual){
			for(int i = 0; i < hiddenLayers.get(0).size(); i++)
				hiddenLayers.get(0).get(i).o += inputLayer.get(i).o;
		}
			
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for(int j = 0; j < hiddenLayers.get(0).size(); j++)
				hiddenLayers.get(i).get(j).feedForward();
			
			for(int j = 0; j < hiddenLayers.get(0).size(); j++)
			{
				if(isResidual && i < hiddenLayers.size()-1)
					hiddenLayers.get(i+1).get(j).o += hiddenLayers.get(i).get(j).o;
			}
		}
		
		float outputs[] = new float[outputLayer.size()];
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).feedForward();
			outputs[i] = outputLayer.get(i).zBP;
		}
		
		float out[] = new float[outputLayer.size()];
		
		
		out = softMax(outputs);
	
		for(int i = 0; i < outputLayer.size(); i++)
		{
			currentOutputs[i] = out[i];
		}
		return out;
	}
	
	//This is the function which trains the network using backpropagation. Takes an array of floats(input values) and desired outputs
	//as parameters.
	public void trainNetwork(float[] inputs, float[] desired, float alpha, int setSize, float regConst)
	{
		
		//Calculate L2 Regularization Value.
		float w = 0;
		for(int i = 0; i < connections.size(); i++)
			w+= Math.abs(connections.get(i).weight);
		
		
		float r = (2*regConst/setSize)*w;
		
		//Feedforward the inputs to the network and get the outputs.
		float[] outputs = feedForward(inputs);
		
		float[] ins = new float[outputs.length];
		
		for(int i = 0; i < ins.length; i++)
			ins[i] = outputLayer.get(i).zBP;
		
		float [] err = new float[desired.length];
		
		
		for(int i = 0; i < desired.length; i++)
		{
				if(Float.isNaN(outputs[i]))
					outputs[i] = 0;
				
				err[i] = (float)(outputs[i] - desired[i]);
		}
		
		//------------------------------BACKPROPAGATION------------------------------------------
		//---------------------------------------------------------------------------------------
		
		//Calculate the error in the ouput layer of neurons using the deriveCostFunction function and deriveSigmoid function.
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).error = err[i];//deriveCostFunction(outputs[i], desired[i]) * deriveSigmoid(outputLayer.get(i).zBP);
		}
		
		
		//Calculate the error in the hidden Layer.
		for(int i = 0; i < hiddenLayers.get(hiddenLayers.size() - 1).size(); i++)
		{
			float sum = 0;
			
			for(int j = 0; j < outputLayer.size(); j++)
			{
				sum += hiddenLayers.get(hiddenLayers.size() - 1).get(i).outputs.get(j).to.error * hiddenLayers.get(hiddenLayers.size() - 1).get(i).outputs.get(j).weight;
			}
			
			hiddenLayers.get(hiddenLayers.size() - 1).get(i).error = sum * deriveRelu(hiddenLayers.get(hiddenLayers.size() - 1).get(i).zBP) + r;
		}
		
		for(int i = hiddenLayers.size() - 1; i >0;  i--)
		{
			for(int j = 0; j < hiddenLayers.get(i - 1).size(); j++)
			{
				float sum = 0;
				
				for(int k = 0; k < hiddenLayers.get(i).size(); k++)
				{
					sum += hiddenLayers.get(i - 1).get(j).outputs.get(k).to.error * hiddenLayers.get(i - 1).get(j).outputs.get(k).weight;
				}
				
				hiddenLayers.get(i - 1).get(j).error = sum * deriveRelu(hiddenLayers.get(i - 1).get(j).zBP) + r;
			}
		}
		
		//Calculate the error in the input Layer
		for(int i = 0; i < inputLayer.size(); i++)
		{
			float sum = 0;
			
			for(int j = 0; j < hiddenLayers.get(0).size(); j++)
			{
				sum += inputLayer.get(i).outputs.get(j).to.error * inputLayer.get(i).outputs.get(j).weight;
			}
			
			inputLayer.get(i).error = sum * deriveRelu(inputLayer.get(i).zBP) + r;
		}
		//---------------------End BACKPROPAGATION---------------------------------------------
		//--------------------------------------------------------------------------------------
		
		//----------------------GRADIENT DESCENT------------------------------
		//--------------------------------------------------------------------
		
		//Alter Input Layer biases
		for (int i = 0; i < inputLayer.size(); i++)
		{
			//inputLayer.get(i).bias -= .5*inputLayer.get(i).error;
		}
		
		//Alter Hidden Layer Biases
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for (int j = 0; j < hiddenLayers.get(i).size(); j++)
			{
				hiddenLayers.get(i).get(j).bias -= learningRate*alpha*hiddenLayers.get(i).get(j).error;
			}
		}
		
		
		//Alter Output Layer Biases
		for (int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).bias -=  learningRate* alpha*outputLayer.get(i).error;
					
		}
		
		//Calculate and subtract gradient descent for input layer.
		for (int i = 0; i < inputLayer.size(); i++)
		{
			for(int j = 0; j < inputLayer.get(i).outputs.size(); j++)
			{
				inputLayer.get(i).outputs.get(j).weight -= learningRate * inputLayer.get(i).o * inputLayer.get(i).outputs.get(j).to.error;
			
			}
		
			
		}
		
		//Calculate and subtract gradient descent for hidden layer.
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for (int j = 0; j < hiddenLayers.get(i).size(); j++)
			{
				for(int k = 0; k < hiddenLayers.get(i).get(j).outputs.size(); k++)
				{
					hiddenLayers.get(i).get(j).outputs.get(k).weight -= learningRate * hiddenLayers.get(i).get(j).o * hiddenLayers.get(i).get(j).outputs.get(k).to.error;
					
				}
			}
		}
		
		//System.out.println(learningRate);
		
		//Calculate and subtract gradient descent for  layer.
		/*for (int i = 0; i < outputLayer.size(); i++)
		{
			for(int j = 0; j < outputLayer.get(i).outputs.size(); j++)
				outputLayer.get(i).outputs.get(j).weight -= outputLayer.get(i).o * outputLayer.get(i).error;
		}*/
		
		//------------------END GRADIENT DESCENT-------------------------------
		//---------------------------------------------------------------------
		clearInternals();
	}
	
	public float[] feedBackward(){
		
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).addWeightedInput(currentOutputs[i]);
			outputLayer.get(i).feedBackward();
		}
		
		
		for(int i = hiddenLayers.size() - 1; i >=0; i-- )
		{
			for(int j = 0; j < hiddenLayers.get(i).size(); j++)
			{
				hiddenLayers.get(i).get(j).feedBackward();
				System.out.println("" + j + " " + hiddenLayers.get(i).get(j).o);
			}
		}
		
		float [] finalZ = new float[inputLayer.size()];
		
		for(int i = 0; i < inputLayer.size(); i++)
			finalZ[i] = inputLayer.get(i).z;
		
		float [] out =softMax(finalZ);
		
		return out;
		
	}
	
	//Returns the value of the quadratic cost function.
	public float costFunction(float output, float desired)
	{
		float r = (float) (.5*(Math.pow(desired - output, 2)));
		return r;
	}
	
	public float[] softMax(float[] inputs)
	{
		float[] r = new float[inputs.length];
		
		float denom = (float) 0.01;
		
		for(int i = 0; i < inputs.length; i++)
			denom += Math.pow(Math.E, inputs[i]);
		
		for(int i = 0; i < inputs.length; i++)
		{
			if(denom != 0)
				r[i] = (float) (Math.pow(Math.E, inputs[i]) / denom);
		}
		
		
		return r;
	}
	
	public float deriveSoftMax(float [] softMax, int num)
	{
		
		float g = (float)softMax[num] * (1- softMax[num]);
		return g;
		
	}
	
	//The partial derivative of the quadratic cost function with respect to
	//the activation function of the neuron.
	/*public float deriveCostFunction(float output, float desired, float a)
	{
		float r = output - desired + a;
		return r;
	}*/
	
	public float deriveCostFunction(float[] output, float[] desired, float a)
	{
		float r = 0;
		for(int i = 0; i < desired.length; i++)
		{
			if(output[i] > 0)
				r += desired[i]*(1/output[i]);
		}
		
		r += a;
		return r;
	}
	//The partial derivative of the sigmoid function with resepct to the sum of weighted inputs.
	public float deriveRelu(float z)
	{
		float r = 1;
		
		if(z < 0)
			r  = (float).01;
		return r;
	}
	
	//Returns the value of the sigmoid(activation) function.
	public float relu(float z)
	{
		if(z >= 0)
			z = z;
		else
			z = (float).01 * z;
		
		float r =(float)Math.log(1 + Math.pow(Math.E, z));
		return r;
	} 
	
	public float deriveSigmoid(float z)
	{
		float r = (float)(Math.pow(Math.E, z)/ Math.pow((1 + Math.pow(Math.E, z)), 2));
		return r;
	}
	
	public float Sigmoid(float z)
	{
		
		float r =(float)(1/(1 + Math.pow(Math.E, -z)));
		return r;
	} 

	
	
	//Save the Neural Network to a text file, taking a printwriter as a parameter.
	public void save(PrintWriter writer){
			
		for(int i = 0; i < inputLayer.size(); i++)
		{
			writer.println(inputLayer.get(i).bias);
		}
		
		writer.println("");
		
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for(int j = 0; j < hiddenLayers.get(i).size(); j++)
			{
				writer.println(hiddenLayers.get(i).get(j).bias);
			}
		}
		
		writer.println("");
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			writer.println(outputLayer.get(i).bias);
		}
		
		writer.println("");
		
		for(int i = 0; i < connections.size(); i++)
		{
			writer.println(connections.get(i).weight);
		}
		
	}
	
	public void copyData(float [] cons, float [] inputs, float [] hidden1b, float [] hidden2b, float [] outputb) throws NumberFormatException, IOException
	{
		
		for(int i = 0; i < inputLayer.size(); i++)
		{
			inputLayer.get(i).bias = inputs[i];
		}
		
		
		for(int i = 0; i < hiddenLayers.get(0).size(); i++)
		{
			hiddenLayers.get(0).get(i).bias = hidden1b[i];
		}
		
		for(int i = 0; i < hiddenLayers.get(1).size(); i++)
		{
			hiddenLayers.get(1).get(i).bias = hidden2b[i];
		}
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).bias = outputb[i];
		}
		
		
		for(int i = 0; i < connections.size(); i++)
		{
			connections.get(i).weight = cons[i];
		}
	}
	
	public void load(BufferedReader reader) throws NumberFormatException, IOException
	{
		
		for(int i = 0; i < inputLayer.size(); i++)
		{
			inputLayer.get(i).bias = Float.parseFloat(reader.readLine());
		}
		
		reader.readLine();
		
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for(int j = 0; j < hiddenLayers.get(i).size(); j++)
			{
				hiddenLayers.get(i).get(j).bias = Float.parseFloat(reader.readLine());
			}
			
		}
		
		reader.readLine();
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).bias = Float.parseFloat(reader.readLine());
		}
		
		reader.readLine();
		
		for(int i = 0; i < connections.size(); i++)
		{
			connections.get(i).weight = Float.parseFloat(reader.readLine());
		}
	}
	
	public void clearInternals()
	{
		for(int i = 0; i < inputLayer.size(); i++)
		{
			inputLayer.get(i).clear();
		}
		
		for(int i = 0; i < hiddenLayers.size(); i++)
		{
			for(int j = 0; j < hiddenLayers.get(i).size(); j++)
				hiddenLayers.get(i).get(j).clear();
		}
		
		for(int i = 0; i < outputLayer.size(); i++)
		{
			outputLayer.get(i).clear();
		}
	}
}
