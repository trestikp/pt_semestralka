package graphics;

import functions.PathFinder;
import generation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.AMansion;
import objects.Pomocna;
import simulation.Simulation;

import java.io.FileInputStream;
import java.util.List;

public class GUI extends Application {

    public static Simulation sim = null;

    public static Generator g = null;

    public static PathGenerator p = null;

    public static List<AMansion> mansions = null;

    public static short[][] distanceMatrix = null, timeMatrix = null;

    public static void main(String [] args){

        /*
        List<AMansion> toPaint;
        int numberToGenerate = 500;
        PathGenerator p;
        Generator g;

        long start = System.nanoTime();
        g = new Generator(numberToGenerate);
        long end = System.nanoTime();
        System.out.println("\n Generating mansions time: " + (end/1000000 -start/1000000) + "ms\n");
        toPaint = g.getMansions();

        start = System.nanoTime();
        p = new PathGenerator(toPaint);
        end = System.nanoTime();
        System.out.println("\n Generating matrix time: " + (end/1000000 -start/1000000) + "ms\n");

        System.out.println("Path generating");
        start = System.nanoTime();
        PathFinder.pathFinding(p.getDistanceMatrix(),p.getTimeMatrix());
        //  PathFinder.pathFinding(pole, pole);
        end = System.nanoTime();
        System.out.println("Path generation time: "+ (end/1000000000 -start/1000000000) +"s\n");
        System.out.println("----------------------------");

        sim= new Simulation(toPaint,new Pomocna(p.getDistanceMatrix(),p.getTimeMatrix()),
                PathFinder.getDistancePaths(),PathFinder.getTimePaths());
        */
        Thread ap = new Thread(() -> Application.launch(args));
        ap.start();
        //Application.launch(args);
    }

/*
    public static void generateMansions(int numberToGenerate){
        System.out.println("--------------Start of generating--------------");
        System.out.println("Starting generating mansions.");
        long start = System.nanoTime();
        g = new Generator(numberToGenerate);
        long end = System.nanoTime();
        System.out.println("\n Finished generating mansions in: " + (end/1000000 -start/1000000) + "ms\n");
    }

    public static void generatePaths(){
        System.out.println("Starting generating roads.");
        long start = System.nanoTime();
        p = new PathGenerator(g.getMansions());
        long end = System.nanoTime();
        System.out.println("\n Finished generating roads in: " + (end/1000000 -start/1000000) + "ms\n");
    }

    public static void findPaths(){
        System.out.println("Starting generating paths.");
        long start = System.nanoTime();
        PathFinder.pathFinding(p.getDistanceMatrix(),p.getTimeMatrix());
        long end = System.nanoTime();
        System.out.println("\nFinished generating paths in: "+ (end/1000000000 -start/1000000000) +"s\n");
        System.out.println("--------------End of generating--------------");
    }
*/
    public static void runGeneration(int numberToGenerate){

        System.out.println("--------------Start of generating--------------");
        System.out.println("Starting generating mansions.");
        long start = System.nanoTime();
        g = new Generator(numberToGenerate);
        long end = System.nanoTime();
        System.out.println("\n Finished generating mansions in: " + (end/1000000 -start/1000000) + "ms\n");
        mansions = g.getMansions();
        System.out.flush();

        System.out.println("Starting generating roads.");
        start = System.nanoTime();
        p = new PathGenerator(g.getMansions());
        end = System.nanoTime();
        System.out.println("\n Finished generating roads in: " + (end/1000000 -start/1000000) + "ms\n");
        distanceMatrix = p.getDistanceMatrix();
        timeMatrix = p.getTimeMatrix();
        System.out.flush();

        System.out.println("Starting generating paths.");
        start = System.nanoTime();
        PathFinder.pathFinding(p.getDistanceMatrix(),p.getTimeMatrix());
        end = System.nanoTime();
        System.out.println("\nFinished generating paths in: "+ (end/1000000000 -start/1000000000) +"s\n");
        System.out.println("--------------End of generating--------------");
        System.out.flush();

        //generateMansions(numberToGenerate);
        //generatePaths();
        //findPaths();

        if(mansions != null && distanceMatrix != null && timeMatrix != null){
            sim= new Simulation(g.getMansions(),new Pomocna(p.getDistanceMatrix(),p.getTimeMatrix()),
                    PathFinder.getDistancePaths(),PathFinder.getTimePaths());
        } else {
            System.out.println("Missing some data necessary to start simulation!");
        }
    }

    public static void dataReset(){
        mansions = null;
        distanceMatrix = null;
        timeMatrix = null;
        g = null;
        p = null;
        sim = null;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(800); //muze byt 600
        primaryStage.setMinHeight(600); //muze byt 400
        FXMLLoader loader = new FXMLLoader();
        String fxmlPath = "src/graphics/gui.fxml";
        FileInputStream fio = new FileInputStream(fxmlPath);

        Parent root = loader.load(fio);

        Scene scene = new Scene(root);

        primaryStage.setOnCloseRequest(event -> Platform.exit());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mr Pallet, son and grandsons");
        primaryStage.show();
    }

    public static void aboutUsStage() throws Exception{
        Stage stage = new Stage();
        TextArea txtAr = new TextArea();
        txtAr.setText("Authors: Pavel Třeštík\n" + "\t\t Tomáš Ott\n" + "Work is to serve as term program.\n");
        txtAr.setEditable(false);
        Scene scene = new Scene(new AnchorPane(txtAr), stage.getWidth(), stage.getHeight());
        AnchorPane.setBottomAnchor(txtAr, 0.0);
        AnchorPane.setLeftAnchor(txtAr, 0.0);
        AnchorPane.setRightAnchor(txtAr, 0.0);
        AnchorPane.setTopAnchor(txtAr, 0.0);

        stage.setScene(scene);
        stage.setTitle("About");
        stage.show();
    }
}
