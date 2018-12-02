package graphics;

import functions.PathFinder;
import generation.Generator;
import generation.PathGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import objects.AMansion;
import objects.HQ;
import objects.Mansion;
import objects.Pomocna;
import simulation.Simulation;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class creating the graphical user interface for easy use of the program.
 * @author Pavel Třeštík and Tomáš Ott
 */
public class GUI extends Application {

    /** Instance of simulation */
    public static Simulation sim = null;

    /** Instance of generator */
    public static Generator g = null;

    /** Instance of path(edges) generator */
    public static PathGenerator p = null;

    /** List of mansions */
    public static List<AMansion> mansions = null;

    /** Matrixes of distance and time */
    public static short[][] distanceMatrix = null, timeMatrix = null;


    /**
     * Main method. Runs the program
     * @param args arguments for the program - don't have any
     */
    public static void main(String [] args){
        Thread ap = new Thread(() -> Application.launch(args));
        ap.start();
    }

    /**
     * Method runs generation of new data. Generates mansions, edges and finds paths.
     * @param numberToGenerate number of mansions that is to be generated
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

        if(mansions != null && distanceMatrix != null && timeMatrix != null){
            sim= new Simulation(g.getMansions(),new Pomocna(p.getDistanceMatrix(),p.getTimeMatrix()),
                    PathFinder.getDistancePaths(),PathFinder.getTimePaths());
        } else {
            System.out.println("Missing some data necessary to start simulation!");
        }
    }

    /**
     * Resets attributes of the class
     */
    public static void dataReset(){
        mansions = null;
        distanceMatrix = null;
        timeMatrix = null;
        g = null;
        p = null;
        sim = null;
    }



    /**
     * Method starts the GUI
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(800); //muze byt 600
        primaryStage.setMinHeight(600); //muze byt 400
        FXMLLoader loader = new FXMLLoader(/*getClass().getResource("resource/gui.fxml")*/);
        String fxmlPath = "resources/gui.fxml";
        FileInputStream fio = new FileInputStream(fxmlPath);

        Parent root = loader.load(fio);

        Scene scene = new Scene(root);

        primaryStage.setOnCloseRequest(event ->{
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mr Pallet, son and grandsons");
        primaryStage.show();
    }

    /**
     * Method creates new stage to show information "About"
     */
    public static void aboutUsStage(){
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

    /**
     * Method creates new stage to show instruction of use
     */
    public static void instructionStage(){
        Stage stage = new Stage();
        TextArea txtAr = new TextArea();
        txtAr.setText("Manual:\n\n" +
                "To start the simulation you must first generate or import data.\n" +
                "To generate data use the Generation section.\n" +
                "To import data use the File option in menu.\n" +
                "Once you start the simulation you can pause the simulation \n" +
                "and from pause you can resume the simulation or you can stop \n" +
                "the simulation (either without pausing or from paused simulation).\n" +
                "If you want to view information about a mansion or a truck on road \n" +
                "select either a mansion or a truck from marked combo boxes.");
        txtAr.setEditable(false);
        Scene scene = new Scene(new AnchorPane(txtAr), stage.getWidth(), stage.getHeight());
        AnchorPane.setBottomAnchor(txtAr, 0.0);
        AnchorPane.setLeftAnchor(txtAr, 0.0);
        AnchorPane.setRightAnchor(txtAr, 0.0);
        AnchorPane.setTopAnchor(txtAr, 0.0);

        stage.setScene(scene);
        stage.setTitle("Instructions");
        stage.show();
    }

    /**
     * Method creating new stage, that displays scene for manual ordering
     */
    public static void manualOrderStage(){
        Stage stage = new Stage();
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setWidth(600);
        stage.setMaxHeight(500);
        stage.setMaxWidth(600);
        FXMLLoader loader = new FXMLLoader();
        String fxmlPath = "resources/manualOrder.fxml";
        FileInputStream fio;
        Parent root = null;
        try{
            fio = new FileInputStream(fxmlPath);
            root = loader.load(fio);
        } catch (IOException e){
            e.printStackTrace();
        }

        if(root == null){
            System.out.println("Error loading fxml!");
            return;
        }
        Scene scene = new Scene(root);
        stage.setTitle("Manual Order");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method that creates stage for visualization
     */
    public static void showVisualization(){
        Stage stage = new Stage();
        stage.setMinWidth(1600);
        stage.setMinHeight(900);
        stage.setTitle("Visualization");
        /*Scene scene =*/drawVisualization(stage); //prozatim nevyuzita, vyuzije se pri kresleni cest
    }

    /**
     * Method drawing the visualization
     * @param stage stage which shows the visualization
     * @return returns scene with the visualization
     */
    private static Scene drawVisualization(Stage stage){
        Pane root = new Pane();
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.setStyle("-fx-background-color: BLACK");

        if(mansions == null){
            System.out.println("Don't have any mansions to visualize! Terminating...");
            return null;
        }


        for(AMansion m: mansions){
            if(m instanceof Mansion){
                Mansion man = (Mansion)m;

                switch (man.size){
                    case 6:
                        Circle c6 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 8, Color.color(0, 0, 1,1));
                        root.getChildren().add(c6);
                        break;
                    case 5:
                        Circle c5 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 7, Color.YELLOW);
                        root.getChildren().add(c5);
                        break;
                    case 4:
                        Circle c4 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 5, Color.color(0, 1, 0, 1));
                        root.getChildren().add(c4);
                        break;
                    case 3:
                        Circle c3 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 4, Color.color(1, 0.2, 0.8, 1));
                        root.getChildren().add(c3);
                        break;
                    case 2:
                        Circle c2 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 3, Color.BLUE);
                        root.getChildren().add(c2);
                        break;
                    case 1:
                        Circle c1 = new Circle(man.position.getX() * (stage.getMinWidth()/Generator.windowWidth),
                                man.position.getY() * (stage.getMinHeight()/Generator.windowHeight), 2, Color.WHITE);
                        root.getChildren().add(c1);
                        break;
                        default: break;
                }
            }else{
                HQ hq = (HQ) m;
                Circle main = new Circle(hq.position.getX()  * (stage.getMinWidth()/Generator.windowWidth),
                        hq.position.getY()  * (stage.getMinHeight()/Generator.windowHeight), 12, Color.RED);
                root.getChildren().add(main);
            }
        }
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            Object arr[] = root.getChildren().toArray();
            double ratio;
            if(Double.isNaN((double)oldVal)){
                ratio = 1;
            } else {
                ratio = (double)newVal/(double)oldVal;
            }
            for(Object o: arr){
                Circle c = (Circle) o;
                c.setCenterX(c.getCenterX() * ratio);
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            Object arr[] = root.getChildren().toArray();
            double ratio;
            if(Double.isNaN((double)oldVal)){
                ratio = 1;
            } else {
                ratio = (double)newVal/(double)oldVal;
            }
            for(Object o: arr){
                Circle c = (Circle) o;
                c.setCenterY(c.getCenterY() * ratio);
            }
        });

        stage.setScene(scene);
        stage.show();
        return scene;
    }
}
