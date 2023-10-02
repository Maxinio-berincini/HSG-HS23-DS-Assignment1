Assignment 1
------------

# Team Members
- Leon Luca Klaus Muscat
- Felix Kappeler
- Max Beringer

# GitHub link to your (forked) repository

   >https://github.com/Maxinio-berincini/HSG-HS23-DS-Assignment1

# Task 4

1. (0.25pt) What are Interface Definition Languages (IDL) used for? Name and explain the IDL that you use for this task.
   >Ans: 
   > 
   > Interface Definition Languages are used to communicate between two programs, where the sender and receiver are not written in the same programming language.
   > For the tasks above we used gRPC as the framework, which in turn uses Protocol Buffers as the IDL. We used it to define message types and then generate parts of the server and client code.  
2. (0.25pt) In this implementation of gRPC, you use channels. What are they used for?
   >Ans: 
   > 
   > They are used to establish a connection between the client and the server. It also lets you manage error handling and load balancing.
   > In task 3 it allowed the client to stream the file chunks to the MapServer and the ReduceServer. 
3. (0.5pt)
   (0.25) Describe how the MapReduce algorithm works. Do you agree that the MapReduce programming model may have latency issues? What could be the cause of this?
   (0.25) Can this programming model be suitable (recommended) for iterative machine learning or data analysis applications? Please explain your argument.
   >Ans: 
   > 
   > During the Map part, the input is divided into multiple chunks. Each chunk is then process by a mapping function, that creates a list of all words and the occurrence of said word.
   > The produced map is then saved in a temporary file. In the Reduce part, the previously created maps are summed up and saved as a single map. 
   >
   >Yes I think every program that relies on a connection can have some latency depending on the network load and speed and the overhead that is created when scheduling tasks and splitting the data.
   > 
   >I dont think that it is suitable for big iterative tasks in general, as there is a lot of data, that has to be processed multiple times, while there is each time some latency from the overhead mentioned above and the network setup.
