
/* 
* gRPC server node to accept calls from the clients and serve based on the method that has been requested
*/

package io.grpc.filesystem.task3;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.task3.proto.AssignJobGrpc;
import com.task3.proto.MapInput;
import com.task3.proto.ReduceInput;
import com.task3.proto.MapOutput;
import com.task3.proto.ReduceOutput;
import io.grpc.filesystem.task2.*;

public class MrReduceServer {

    private Server server;

    private void start(int port) throws IOException {
        server = ServerBuilder.forPort(port).addService(new MrReduceServerImpl()).build().start();
        System.out.println("Listening on: " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Terminating the server at port: " + port);
                try {
                    server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
    }

    static class MrReduceServerImpl extends AssignJobGrpc.AssignJobImplBase {
        MapReduce mr = new MapReduce();
        @Override
        public void reduce(ReduceInput request, StreamObserver<ReduceOutput> responseObserver) {
            try {
                mr.reduce(request.getInputfilepath(), request.getOutputfilepath());

                ReduceOutput response = ReduceOutput.newBuilder().setJobstatus(2).build();
                responseObserver.onNext(response);
            } catch (Exception e) {
                ReduceOutput response = ReduceOutput.newBuilder().setJobstatus(1).build();
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final MrReduceServer mrServer = new MrReduceServer();
        for (String i : args) {

            mrServer.start(Integer.parseInt(i));

        }
        mrServer.server.awaitTermination();
    }

}
