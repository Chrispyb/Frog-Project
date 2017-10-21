import java.util.ArrayList;
import java.util.Random;

public class Neuron {

	ArrayList<Connection> outputs;
	ArrayList<Connection> backwardOutputs;
	ArrayList<Float> weightedInputs;
	
	int x;
	int y;
	float o;
	float z = 0;
	float bias;
	float zBP;
	float error;
	
	int typeA;
	String name;
	Random random;
	
	//The constuctor for neurons. Paramaters are an x position, a y position, and a name.
	public Neuron(int xP, int yP, String n, int type){
		
		random = new Random();
		x = xP;
		y = yP;
		bias = random.nextFloat();
		name = n;
		
		outputs = new ArrayList<Connection>();
		backwardOutputs = new ArrayList<Connection>();
		
		weightedInputs = new ArrayList<Float>();
		
		typeA = type;
		

		bias = (float) (random.nextFloat() * 0.125);
		System.out.println(bias);
	}
	
	//Function which adds a weighted input to the list of inputs for this neuron.
	public void addWeightedInput(float w)
	{
		z += w;	
	}
	
	//Adds a connection to the list of connections to which this neuron will output the value of it's action 
	//function times the weight of this connection.
	public void addOutputConnection(Connection c){
		outputs.add(c);
	}
	
	public void addBackwardConnection(Connection c){
		backwardOutputs.add(c);
	}
	
	public void clear()
	{
		zBP = 0;
		z = 0;
		o = 0;
		error = 0;
	}
	
	//This function feeds forward to neurons in the next layer.
	public float feedForward()
	{
		float r = 0;
	
		//Add the bias to the list of inputs.
		z += bias;
		
		//Keep track of z at the class level for use in backpropogation.

		
		//Clear the list of weighted inputs.
		weightedInputs.clear();
		
		//Calculate the activation function for feeding forward
		
		if(typeA == 0)
		{
			if( z < 0)
				z = (float).01*z;

			r = z;
		}
		else if(typeA == 1)
		{
			r = (float) (1 / (1 +(Math.pow(Math.E, -z))));
		}
		else if(typeA == 2)
		{
			r = z;
		}
		
		zBP = z;
		z = 0;
		//Keep track of the value of the activation function
		o = r;
		
		//Feed to the "to" neurons of each connection by adding the value of activation function times the 
		//weight of the respective connection to the weightInputs of the "to" neuron. Don't do this for the output
		//layer, as the output layer has no output connections.
		for(int i = 0; i < outputs.size(); i++)
		{
			outputs.get(i).to.addWeightedInput(r * outputs.get(i).weight);
		}
		
		//return the value of the activation function for this neuron.
		return r;
	}
	
	public float feedBackward(){
		
		float r = 0;
		
		//Add the bias to the list of inputs.
		z += bias;
		
		//Keep track of z at the class level for use in backpropogation.

		
		//Clear the list of weighted inputs.
		weightedInputs.clear();
		
		//Calculate the activation function for feeding forward
		
		if(typeA == 0)
		{
			if( z < 0)
				z = (float).01*z;

			r = z;
		}
		else if(typeA == 1)
		{
			r = (float) (1 / (1 +(Math.pow(Math.E, -z))));
		}
		else if(typeA == 2)
		{
			r = z;
		}
		
		zBP = z;
		z = 0;
		//Keep track of the value of the activation function
		o = r;
		
		//Feed to the "to" neurons of each connection by adding the value of activation function times the 
		//weight of the respective connection to the weightInputs of the "to" neuron. Don't do this for the output
		//layer, as the output layer has no output connections.
		for(int i = 0; i < backwardOutputs.size(); i++)
		{
			backwardOutputs.get(i).from.addWeightedInput(r * backwardOutputs.get(i).weight);
		}
		
		//return the value of the activation function for this neuron.
		return r;
	}
	

}
