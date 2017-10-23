				

Analysis of Frog Calls ReadME

-----------------------------
-----------------------------
NOTE:This program is still in early stages of development.

Overview:
	This Program allows users to create training, validation, and test set files, specifically for sound classification.
	These files can be used to train deep feed forward neural networks using the network and interface classes in the same program.

The program interface is a shell with the following commands/functions:

	Option '1': Create a new network from scratch.
	Option '2': Load a network from the file system.
	Option '3': Save the current network.
	Option '4': Delete the current neural network.
	Option '5': Train this neural network using a training file.
	Option '6': Test the network using a input file.
	Option '7': Digital signal processing interface.
	Option '8': Quit This Program.

Option '1': Create a new network from scratch.-

	This option will create a feed forward neural network, and replace any neural network that already exists. It will  prompt
the user to enter the number of neurons for each layer and the number of hidden layers to add and then create a neural network with those dimensions.

Option '2': Load a network from the file system.-

	This option allows a neural network file to be loaded from the file system into the program by the user, creating a network with the same dimensions,
weights, and biases. 

Option '3': Save the current network.-

	This option allows the the program's current neural network to be saved to a file, storing the dimension, weights, and biases.

Option '4': Delete the current neural network.

	This option deletes the program's current neural network.

Option '5': Train this neural network using a training file.-

	This option allows the user to enter the location of a training file to train the neural network with using stochastic gradient
descent.

Option '6': Test the network using a input file.
	
	This option allows the user to test the accuracy of the network using a test file.

Option '7': Digital signal processing interface.
	
	This option changes from the current interface to the DSP interface. The DSP interface allows the user to create training, test,
and validation sets from Wav files, these sets can then be saved as files and can be used to train and test a neural network.

Option '8': Quit This Program.
	
	This option exits the program.





