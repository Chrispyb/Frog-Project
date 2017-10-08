
public class MemoryNeuron extends Neuron {

	float previous;
	
	public MemoryNeuron(int xP, int yP, String n){
	
		super(xP, yP, n, 0);
		previous = 0;
	}
	
	public float feedForward(){
		
		float fT = (float) (1 / (1 + Math.pow(Math.E, -z)));
		float iT = fT;
		
		fT = fT * previous;
		
		float cT = (float) Math.tanh(z);
		
		fT = fT + cT * iT;
		
		previous = fT;
		
		for(int i = 0; i < outputs.size(); i++)
		{
			outputs.get(i).to.addWeightedInput((float) (Math.tanh(fT) * iT));
		}
		
		return 0;
	}
}
