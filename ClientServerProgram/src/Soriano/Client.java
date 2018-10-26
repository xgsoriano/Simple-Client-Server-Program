package Soriano;

/**
*
* @author gsoriano
*/

import java.io.*;
import java.net.*;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
	
	int[] numbers = new int[1000];
    @Override
    public void start(Stage primaryStage) throws Exception{




        // Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter Ip Address: "));


        TextField ip = new TextField();
            ip.setAlignment(Pos.BOTTOM_RIGHT);
             paneForTextField.setCenter(ip);

         BorderPane mainPane = new BorderPane();
              // Text area to display contents
         TextArea ta = new TextArea();
         mainPane.setCenter(new ScrollPane(ta));
              mainPane.setTop(paneForTextField);

              // Create a scene and place it in the stage
         Scene scene = new Scene(mainPane, 450, 200);
         primaryStage.setTitle("Client"); // Set the stage title
              primaryStage.setScene(scene); // Place the scene in the stage
              primaryStage.show(); // Display the stage

        ip.setOnAction(e->{
            try{
                String ipAddress = ip.getText();

                Socket socket = new Socket(ipAddress,8000);

                System.out.println("connected");




                System.out.println("connected");

                for(int i = 0; i < numbers.length; i++) {
                    numbers[i] = (int)(Math.random()*20 + 1);

                }
                System.out.println("connected");

                ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());

                toServer.writeObject(numbers);
                System.out.println("connected");


                ObjectInputStream fromServer= new ObjectInputStream(socket.getInputStream());
                numbers = (int[])fromServer.readObject();
                System.out.println("connected");

                Platform.runLater(()->{
                    ta.appendText("Received Array");
                });


                for(int i = 0;i<numbers.length;i++)
                {
                    int num = numbers[i];

                    Platform.runLater(()->{
                        ta.appendText(Integer.toString(num)+ '\n');
                    });
                }
                System.out.println("connected");

            }

            catch(IOException ex){
                System.err.println(ex);
            }

            catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }


        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}
