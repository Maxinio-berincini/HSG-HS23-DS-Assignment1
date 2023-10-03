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
   > Interface Definition Languages are used for programming language agnostic communication between programs.
   > For this assignment we used gRPC as the framework, which uses Protocol Buffers as the IDL. We used it to define message types and then generate parts of the server and client code.  
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
   > As the name implies the MapReduce algorithm is seperated into 2 parts: Map and Reduce. During the map phase, the input is divided into chunks by a mapping function. This intermediary output is stored in temporary files. During the reduce phase, the data from the files is consolidated and the result is the final output.
   >
   >Yes we think every program that relies on a connection can have some latency depending on the network load and the overhead that is created when scheduling tasks and splitting the data. Saving the chunks as an intermediary step between map and reduce also introduces additional delay.
   > 
   > When it comes to iterative tasks, especially in domains like machine learning or complex data analysis, MapReduce is not optimal. With each iteration, the overhead of MapReduce explained previously compounds, which makes it impractical on a large scale.
