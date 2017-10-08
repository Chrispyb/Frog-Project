import java.io.PrintWriter;

public class VerilogFile {

	public VerilogFile(PrintWriter writer, Network network)
	{
		/*writer.print("module Network(clk, reset, microphone, ");
			
		for(int i = 0; i < network.outputLayer.size(); i++)
		{
			if(i < network.outputLayer.size() - 1)
				writer.print("output" + (i+1) + ", ");
			else
				writer.print("output" + (i+1) + ");");
		}
		
		
		writer.println("");
		writer.println("");
	
		for(int i = 0; i < network.outputLayer.size(); i++)
		{
			if(network.outputLayer.size() == 1)
			{
				writer.print("	output output" + (i+1) + ";");
				continue;
			}
		
			if(i == 0)
				writer.print("	output output" + (i+1) + ", ");
			else if(i < network.outputLayer.size() - 1)
				writer.print("output" + (i+1) + ", ");
			else 
				writer.print("output" + (i+1) + ";");
		}
		
		writer.println("");
		writer.println("");
		
		writer.println("	input clk = 0;");
		
		writer.println("");
		
		writer.println("	input microphone;");
		
		writer.println("");
		
		writer.println("	input reset = 0;");
		
		writer.println("");
		
		writer.println("	reg[1:0] cycle = 0;");
		
		writer.println("");
		
		writer.println("	reg[16:0] inputCycle = 0;");
		
		writer.println("");
		
		writer.println("	real[31:0] inputLayer[" + (network.inputLayer.size() - 1) + ":0];");
		
		writer.println("");
		
		writer.println("	real[31:0] hiddenLayer[" + (network.hiddenLayer.size() - 1) + ":0];");
		
		writer.println("");
		
		writer.println("	real[31:0] outputLayer[" + (network.outputLayer.size() - 1) + ":0];");
		
		writer.println("	real[31:0] e;");
		
		writer.println("");
		
		writer.println("	real[31:0] iToHConnections[" + (network.inputLayer.size()*network.hiddenLayer.size() - 1) + ":0];");
		
		writer.println("");
		
		writer.println("	real[31:0] hToOConnections[" + (network.hiddenLayer.size()*network.outputLayer.size() - 1) + ":0];");
		
		writer.println("");
		
		writer.println("	initial begin");
		
		writer.println("");
		
		writer.println("	e = 2.71828182845904;");
		
		writer.println("");
		
		int p = 0;
		
		for(int i = 0; i < network.inputLayer.size()*network.hiddenLayer.size(); i++)
		{
			if(i < network.inputLayer.size()*network.hiddenLayer.size() - 1)
				writer.print("	iToHConnections[" + i + "]<=" + network.connections.get(i).weight + ", ");
			else
				writer.print("	iToHConnections[" + i + "]<=" + network.connections.get(i).weight + ";");
			
			p++;
		}
		
		writer.println("");

		for(int i = 0; i < network.hiddenLayer.size()*network.outputLayer.size(); i++)
		{
			if(i < network.hiddenLayer.size()*network.outputLayer.size() - 1)
				writer.print("	hToOConnections[" + i + "]<=" + network.connections.get(p).weight + ", ");
			else
				writer.print("	hToOConnections[" + i + "]<=" + network.connections.get(p).weight + ";");
			
			p++;
		}
		
		writer.println("");
		
		/*for(int i = 0; i < network.inputLayer.size(); i++)
		{
			if(i < network.inputLayer.size() - 1)
				writer.print("		inputLayer[" + i + "]<=input" + (i + 1) + ", ");
			else
				writer.print("		inputLayer[" + i + "]<=input" + (i + 1) + ";");
		}
		
		writer.println("");
		
		writer.println("	end");
		
		writer.println("	always @(posedge clk) begin");
		
		writer.println("");
		
		writer.println("		if(reset == 1 || cycle == 3)");
		writer.println("			cycle <= 0;");
		writer.println("			inputCycle <= 0;");
		writer.println("		end");
		
		writer.println("		inputLayer[inputCycle] <= microphone;");
		writer.println("		inputCycle <= inputCycle + 1;");
		writer.println("");
		writer.println("		if(inputCycle == " + network.inputLayer.size() + ") begin ");
		writer.println("			inputCycle <= 0;");
		writer.println("			cycle <= 1;");
		writer.println("		end");
		
		writer.println("");
		writer.println("		end");
		
		
		writer.println("		if(cycle == 1) begin");
		
		p = 0;
		
		for(int i = 0; i < network.hiddenLayer.size(); i++, p++)
		{
			if(network.hiddenLayer.size() > 1)
				writer.print("			hiddenLayer[" + i + "] <= (1/(1 + e ** -inputLayer[" + 0 + "])) * iToHConnections[" + i + "] +");
			else
				writer.print("			hiddenLayer[" + i + "] <= (1/(1 + e ** -inputLayer[" + 0 + "])) * iToHConnections[" + i + "]; ");
			
			
			for(int j = 1; j < network.inputLayer.size(); j++)
			{
				if(j < network.hiddenLayer.size() - 1)
					writer.print("(1/(1 + e ** -inputLayer[" + j + "])) * iToHConnections["  + (network.inputLayer.size() * j + i) + "] +");
				else
					writer.print("(1/(1 + e ** -inputLayer[" + j + "])) * iToHConnections["  + (network.inputLayer.size() * j * i) + "];");
			}
			
			writer.println("");
		}
		
		writer.println("			cycle <= 2;");
		writer.println("		end");
		
		writer.println("		if(cycle == 2) begin");
			
		p = 0;
		
		for(int i = 0; i < network.outputLayer.size(); i++)
		{
			if(network.outputLayer.size() > 1)
				writer.print("			outputLayer[" + i + "] <= (1/(1 + e ** -hiddenLayer[" + 0 + "])) * hToOConnections[" + i + "] +");
			else
				writer.print("			outputLayer[" + i + "] <= (1/(1 + e ** -hiddenLayer[" + 0 + "])) * hToOConnections[" + i + "] +");
			
			p++;
			
			for(int j = 1; j < network.hiddenLayer.size(); j++, p++)
			{
				if(j < network.hiddenLayer.size() - 1)
					writer.print("(1/(1 + e ** -hiddenLayer[" + j + "])) * hToOConnections["  + (network.hiddenLayer.size() * j + i) + "] +");
				else
					writer.print("(1/(1 + e ** -hiddenLayer[" + j + "])) * hToOConnections["  + (network.hiddenLayer.size() * j + i) + "];");
			}
			
			writer.println("");
		}
		
		writer.println("");
		writer.println("			cycle <= 3;");
		writer.println("");
		
		writer.println("		end");
		writer.println("	end");
		writer.println("end module");
		writer.close();*/
	}
	
}
