# Map Reduce and gRPC

The repository contains a gradle applications project template for completing task 2 and 3 for Assignment number 1.

> **_NOTE:_**
> After loading the project depending on which IDE (if using), you might see some file not found errors. However, they will disappear after you have successfully built the project using following command from the task folder.

### Project Structure

```bash
src
└── main
    ├── java
    │   └── io
    │       └── grpc
    │               └── filesystem
    │                   ├── task2
    │                   │   ├── MapReduce.java # map and reduce on a given input file
    │                   │   └── Mapper.java # map object class
    │                   └── task3
    │                   │   ├── MrClient.java # client implementation for requesting map and reduce jobs
    │                   │   ├── MrMapServer.java # server implementation to accept and perform map request 
    │                   │   └── MrReduceServer.java # server implementation to accept and perform reduce request
    │                   └── test
    │                       └── CheckOutput.java # compare results against the expected answer using suggested word filters in MapReduce.java file  
    └── proto
        └── communicate.proto  # gRPC service and message formats

```

## Task 2

### Run the program from Tasks folder

On MacOS:

```bash
./gradlew build
./gradlew run -PchooseMain=io.grpc.filesystem.task2.MapReduce --args="input/pigs.txt output/output-task2.txt"
```

Check your output:

```bash
./gradlew run -PchooseMain=io.grpc.filesystem.test.CheckOutput --args="output/output-task2.txt"
```

On Windows:

```bash
.\gradlew build
.\gradlew run -PchooseMain="io.grpc.filesystem.task2.MapReduce" --args="input/pigs.txt output/output-task2.txt"
```

Check your output:

```bash
.\gradlew run -PchooseMain="io.grpc.filesystem.test.CheckOutput" --args="output/output-task2.txt"
```

## Task 3

### Run client and servers

From the Task3 directory, execute the following command to install the project.

#### On MacOS

```bash
./gradlew build
```

After a successful build, you are ready to start communication using gRPC through the following commands:

Map Server:

```bash
./gradlew run -PchooseMain=io.grpc.filesystem.task3.MrMapServer --args="50551"
```

Reduce Server:

```bash
./gradlew run -PchooseMain=io.grpc.filesystem.task3.MrReduceServer --args="50552"
```

Client:

```bash
./gradlew run -PchooseMain=io.grpc.filesystem.task3.MrClient --args="127.0.0.1 50551 50552 input/pigs.txt output/output-task3.txt"
```

Check your output:

```bash
./gradlew run -PchooseMain=io.grpc.filesystem.test.CheckOutput --args="output/output-task3.txt"
```

#### On Windows

```bash
.\gradlew build
```

Map Server:

```bash
.\gradlew run -PchooseMain="io.grpc.filesystem.task3.MrMapServer" --args="50551"
```

Reduce Server:

```bash
.\gradlew run -PchooseMain="io.grpc.filesystem.task3.MrReduceServer" --args="50552"
```

Client:

```bash
.\gradlew run -PchooseMain="io.grpc.filesystem.task3.MrClient" --args="127.0.0.1 50551 50552 input/pigs.txt output/output-task3.txt"
```

Check your output:

```bash
.\gradlew run -PchooseMain="io.grpc.filesystem.test.CheckOutput" --args="output/output-task3.txt"
```
