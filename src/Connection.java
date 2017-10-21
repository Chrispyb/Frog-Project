import java.util.Random;

public class Connection {

	Neuron to;
	Neuron from;
	

	float weight;
	float learningConstant;
	float gradienDescent;
	Random random;
	
	//Constructor takes a "from" neuron, a "to" neuron, a weight, a learning constant,
	//and a flag which will randomize the weight and learning constant if the value is 1.
	public Connection(Neuron f, Neuron t, float w, float l, int r)
	{
		to = t;
		from = f;
		learningConstant = l;
		weight = w;
		random = new Random();
		
		//randomize the weight and learning constant if the flag parameter r is == 1.
		if(r == 1)
		{
			learningConstant = random.nextFloat();
			
			if(learningConstant<.2)
				learningConstant = (float) 0.2;
			
			//learningConstant = (float) .01;
			weight = random.nextFloat();
			
			if(weight < .2)
				weight = (float) .2;
			
		}
		
		weight = (float) (random.nextFloat() / Math.sqrt(128));
		learningConstant /= 1000;

	}
}
