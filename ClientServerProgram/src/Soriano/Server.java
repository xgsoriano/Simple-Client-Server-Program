package Soriano;

/**
*
* @author gsoriano
*/

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application{
	
	private ObjectInputStream inputFromClient;
    private ObjectOutputStream outputToClient;


    @Override
    public void start(Stage primaryStage) throws Exception {

        TextArea ta = new TextArea();


        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> {
                    ta.appendText("Server started at " + new Date() + '\n');

                    //ta.appendText(serverSocket.getInetAddress().getHostAddress() + '\n');
                });


                while (true) {

                    // Listen for a connection request
                    Socket socket = serverSocket.accept();

                    ta.appendText(serverSocket.getInetAddress().getHostAddress() + '\n');

                    // Create object input streams
                    inputFromClient = new ObjectInputStream(socket.getInputStream());
                    int[] clientArray = (int[]) inputFromClient.readObject();

                    parallelMergeSort(clientArray);

                    // Create object output streams
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(clientArray);


                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }).start();
    }


    public static void parallelMergeSort(int[] list) {
        RecursiveAction mainTask = new SortTask(list);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
    }


    private static class SortTask extends RecursiveAction {
        private final int THRESHOLD = 500;
        private int[] list;

        SortTask(int[] list) {
            this.list = list;
        }

        @Override
        protected void compute() {
            if (list.length < THRESHOLD)
                java.util.Arrays.sort(list);
            else {
                // Obtain the first half
                int[] firstHalf = new int[list.length / 2];
                System.arraycopy(list, 0, firstHalf, 0, list.length / 2);

                // Obtain the second half
                int secondHalfLength = list.length - list.length / 2;
                int[] secondHalf = new int[secondHalfLength];
                System.arraycopy(list, list.length / 2,
                        secondHalf, 0, secondHalfLength);

                // Recursively sort the two halves
                invokeAll(new SortTask(firstHalf),
                        new SortTask(secondHalf));

                // Merge firstHalf with secondHalf into list
                merge(firstHalf, secondHalf, list);
            }
        }
    }

    /**
     * Merge two sorted lists
     */
    public static void merge(int[] list1, int[] list2, int[] temp) {
        int current1 = 0; // Current index in list1
        int current2 = 0; // Current index in list2
        int current3 = 0; // Current index in temp

        while (current1 < list1.length && current2 < list2.length) {
            if (list1[current1] < list2[current2])
                temp[current3++] = list1[current1++];
            else
                temp[current3++] = list2[current2++];
        }

        while (current1 < list1.length)
            temp[current3++] = list1[current1++];

        while (current2 < list2.length)
            temp[current3++] = list2[current2++];
    }

public static void main(String[] args) {
        launch(args);
    }
}

		
