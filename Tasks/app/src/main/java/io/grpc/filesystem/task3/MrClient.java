/* 
* Client program to request for map and reduce functions from the Server
*/

package io.grpc.filesystem.task3;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.task3.proto.AssignJobGrpc;
import com.task3.proto.MapInput;
import com.task3.proto.ReduceInput;
import com.task3.proto.MapOutput;
import com.task3.proto.ReduceOutput;
import io.grpc.filesystem.task2.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class MrClient {
   Map<String, Integer> jobStatus = new HashMap<String, Integer>();

   public void requestMap(String ip, Integer portnumber, String inputfilepath, String outputfilepath) throws InterruptedException {
      ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, portnumber).usePlaintext().build();

      AssignJobGrpc.AssignJobStub stub = AssignJobGrpc.newStub(channel);

      StreamObserver<MapInput> mapInputObserver = stub.map(new StreamObserver<MapOutput>() {
         @Override
         public void onNext(MapOutput value) {
            System.out.println("MapStatus: " + Integer.toString(value.getJobstatus()));
            if (value.getJobstatus() != -1) {
               System.out.println(outputfilepath + "/" + String.format("chunk%03d.txt", value.getJobstatus()));
               jobStatus.put(outputfilepath + "/" + String.format("chunk%03d.txt", value.getJobstatus()), 2);
            }
         }

         @Override
         public void onError(Throwable t) {
            System.out.println("Error received from server: " + t.getMessage());
         }

         @Override
         public void onCompleted () {
            channel.shutdownNow();
            System.out.println("Map task completed!");
         }
      });

      // Read files in /temp/ and send MapInput messages
      System.out.println(outputfilepath);
      File inputFile = new File(inputfilepath);

      System.out.println("Map: " + inputFile.getName());
      MapInput mapInput = MapInput.newBuilder()
                    .setInputfilepath(inputFile.getAbsolutePath())
                    .setOutputfilepath(outputfilepath)
                    .setIp(ip)
                    .setPort(portnumber)
                    .build();
            mapInputObserver.onNext(mapInput);

      mapInputObserver.onCompleted();
      channel.awaitTermination(5, TimeUnit.SECONDS);
   }

   public int requestReduce(String ip, Integer portnumber, String inputfilepath, String outputfilepath) {
       
      ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, portnumber).usePlaintext().build();

      AssignJobGrpc.AssignJobBlockingStub stub = AssignJobGrpc.newBlockingStub(channel);

      return stub.reduce(ReduceInput.newBuilder().setInputfilepath(inputfilepath).setOutputfilepath(outputfilepath).build()).getJobstatus();
   }
   public static void main(String[] args) throws Exception {// update main function if required

      String ip = args[0];
      Integer mapport = Integer.parseInt(args[1]);
      Integer reduceport = Integer.parseInt(args[2]);
      String inputfilepath = args[3];
      String outputfilepath = args[4];
      String jobtype = null;
      MrClient client = new MrClient();
      int response = 0;

      MapReduce mr = new MapReduce();
      String chunkpath = mr.makeChunks(inputfilepath);
      Integer noofjobs = 0;
      File dir = new File(chunkpath);
      File[] directoyListing = dir.listFiles();
      if (directoyListing != null) {
         for (File f : directoyListing) {
            // only allow filenames: chuck001.txt and chunk002.txt
            if (f.isFile()) {
               noofjobs += 1;
               System.out.println("Chunk: " + f.getPath());
               client.jobStatus.put(f.getPath(), 1);
               client.requestMap(ip, mapport, f.getPath(), dir.getPath());
            }

         }
      }

      Set<Integer> values = new HashSet<Integer>(client.jobStatus.values());

      // Print the unique values
      for (Integer value : values)
         System.out.println("Unique values: " + value);


      if (values.size() == 1 && client.jobStatus.containsValue(2)) {

         response = client.requestReduce(ip, reduceport, chunkpath, outputfilepath);
         if (response == 2) {

            System.out.println("Reduce task completed!");

         } else {
            System.out.println("Try again! " + response);
         }

      }

   }

}
