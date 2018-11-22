package graphics;

import generation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import simulation.Simulation;

import java.io.FileInputStream;
import java.io.PrintStream;

public class GUI extends Application {

    public static Simulation sim;

    public static Generator g;

    public static PathGenerator p;

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

    public static void runGeneration(int numberToGenerate){
        System.out.println("--------------Start of generating--------------");
        System.out.println("Starting generating mansions.");
        long start = System.nanoTime();
        g = new Generator(numberToGenerate);
        long end = System.nanoTime();
        System.out.println("\n Finished generating mansions in: " + (end/1000000 -start/1000000) + "ms\n");

        System.out.println("Starting generating roads.");
        start = System.nanoTime();
        p = new PathGenerator(g.getMansions());
        end = System.nanoTime();
        System.out.println("\n Finished generating roads in: " + (end/1000000 -start/1000000) + "ms\n");

        System.out.println("Starting generating paths.");
        start = System.nanoTime();
        PathFinder.pathFinding(p.getDistanceMatrix(),p.getTimeMatrix());
        end = System.nanoTime();
        System.out.println("\nFinished generating paths in: "+ (end/1000000000 -start/1000000000) +"s\n");
        System.out.println("--------------End of generating--------------");

        sim= new Simulation(g.getMansions(),new Pomocna(p.getDistanceMatrix(),p.getTimeMatrix()),
                PathFinder.getDistancePaths(),PathFinder.getTimePaths());

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        String fxmlPath = "src/graphics/gui.fxml";
        FileInputStream fio = new FileInputStream(fxmlPath);

        Parent root = loader.load(fio);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mr Pallet, son and grandsons");
        primaryStage.show();
    }
}
