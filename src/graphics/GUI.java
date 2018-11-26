package graphics;

import generation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
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


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(1296);
        primaryStage.setMinHeight(759);
        FXMLLoader loader = new FXMLLoader();
        String fxmlPath = "src/graphics/gui.fxml";
        FileInputStream fio = new FileInputStream(fxmlPath);

        AnchorPane root = loader.load(fio);

        Scene scene = new Scene(root);

        final double initWidth = root.getPrefWidth();
        final double initHeight = root.getPrefHeight();

        scene.widthProperty().addListener(new scaleListener(scene, initWidth, initHeight));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mr Pallet, son and grandsons");
        primaryStage.show();
    }

    class scaleListener implements ChangeListener<Number>{
        private final Scene scene;
        private final double initWidth;
        private final double initHeight;
        //private final AnchorPane root;

        public scaleListener(final Scene scene, final double initWidth, final double initHeight){
            this.scene = scene;
            this.initWidth = initWidth;
            this.initHeight = initHeight;
            //this.root = root;
        }


        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            final double newWidth = scene.getWidth();
            final double newHeight = scene.getHeight();

            //System.out.println(scene.getWidth()+ " -- " + initWidth);
            //System.out.println(scene.getHeight() + " -- " + initHeight);

            double ratio = Math.min(newHeight/initHeight, newWidth/initWidth);
            //System.out.println(ratio);

            if(ratio >= 1){
                Scale scale = new Scale(ratio, ratio);
                scale.setPivotX(0);
                scale.setPivotY(0);
                scene.getRoot().getTransforms().setAll(scale);
            }
        }
    }
}
