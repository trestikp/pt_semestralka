package graphics;

import delivery.Truck;
import generation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import simulation.Simulation;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class GuiController {
    /** Simulation speed in [ms]. Default value = 20ms */
    private int simulation_speed;

    //private PrintStream ps;

    private ObservableList<Truck> trucks;

    private ObservableList<AMansion> mansions;

    private boolean mansionsImported = false, matrixesImported = false, dataGenerated = false;

    @FXML
    private TextArea consoleLog;

    @FXML
    private TextArea infoLog;

    @FXML
    private TextField manCount;

    @FXML
    private TextField simSpeed;

    @FXML
    private Button startBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button resumeBtn;

    @FXML
    private ComboBox<Truck> truckCombo;

    @FXML
    private ComboBox<AMansion> manCombo;

    public GuiController(){
    }

    @FXML
    private void initialize(){
        //ps = new PrintStream(new Console(consoleLog), false);
        //System.setOut(ps);
        //System.setErr(ps);

        stopBtn.setDisable(true);
        pauseBtn.setDisable(true);
        resumeBtn.setDisable(true);

        TextAreaConsole tac = new TextAreaConsole(consoleLog);
        tac.start();
    }

    @FXML
    public void startAction(){
        boolean run = false;
        boolean canRun = false;
        setManComboBox();

        if(mansionsImported){
            if(matrixesImported){
                PathFinder.pathFinding(GUI.distanceMatrix, GUI.timeMatrix);
                System.out.println("\n--------------End of generating Paths--------------\n");
                canRun = true;
            } else {
                GUI.p = new PathGenerator(GUI.mansions);
                System.out.println("Starting generating roads.");
                long start = System.nanoTime();
                GUI.p.generatePaths();
                long end = System.nanoTime();
                System.out.println("\n Finished generating roads in: " + (end/1000000 -start/1000000) + "ms\n");
                GUI.distanceMatrix = GUI.p.getDistanceMatrix();
                GUI.timeMatrix = GUI.p.getTimeMatrix();
                PathFinder.pathFinding(GUI.distanceMatrix, GUI.timeMatrix);
                System.out.println("\n--------------End of generating Paths--------------\n");
                canRun = true;
            }
            GUI.sim = new Simulation(GUI.mansions,new Pomocna(GUI.distanceMatrix,GUI.timeMatrix),
                    PathFinder.getDistancePaths(),PathFinder.getTimePaths());
        } else {
            if(!matrixesImported){
                canRun = true;
            }
        }

        if(!(simSpeed.getText().equals(""))){
            if(Integer.parseInt(simSpeed.getText()) < 15){
                System.out.println("Speed of the simulation must be 15 or more ms!");
            } else {
                simulation_speed = Integer.parseInt(simSpeed.getText());
                if(canRun){
                    run = true;
                }
            }
            //System.out.println("CAS SIM: " + simulation_speed);
        } else {
            simulation_speed = 20;
            if(canRun){
                run = true;
            }

            //System.out.println("CAS SIM: " + simulation_speed);
        }

        if(run){
            Thread sim = new Thread(() -> {
                GUI.sim.runSimulation(simulation_speed);
                //System.setOut(ps);
                //System.setErr(ps);
                try{
                    Thread.sleep(2);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            });
            sim.start();
        }

        stopBtn.setDisable(false);
        pauseBtn.setDisable(false);
        startBtn.setDisable(true);
    }

    @FXML
    public void resumeAction() {
        GUI.sim.resumeSim();
        pauseBtn.setDisable(false);
        stopBtn.setDisable(false);
        resumeBtn.setDisable(true);
    }

    @FXML
    public void stopAction() {
        GUI.sim.endSimulation();
        setTruckComboBox();
        resumeBtn.setDisable(true);
        pauseBtn.setDisable(true);
        stopBtn.setDisable(true);
    }

    @FXML
    public void pauseAction() {
        GUI.sim.pauseSim();
        setTruckComboBox();
        resumeBtn.setDisable(false);
        startBtn.setDisable(true);
        stopBtn.setDisable(false);
        pauseBtn.setDisable(true);
    }

    @FXML
    public void generate(){
        startBtn.setDisable(false);
        if(manCount.getText().equals("")){
            Thread gen = new Thread(() -> GUI.runGeneration(2000));
            gen.start();
            //GUI.runGeneration(2000);
        } else {
            if(Integer.parseInt(manCount.getText()) < 500 || Integer.parseInt(manCount.getText()) > 2000){
                System.out.println("Please enter a number between 500 and 2000");
            } else {
                Thread gen = new Thread(() -> GUI.runGeneration(Integer.parseInt(manCount.getText())));
                gen.start();
                //GUI.runGeneration(Integer.parseInt(manCount.getText()));
            }
        }
        dataGenerated = true;
    }

    private void setTruckComboBox(){
        trucks = FXCollections.observableArrayList(Truck.trucksOnRoad);
        truckCombo.setItems(trucks);
        truckCombo.setConverter(new StringConverter<Truck>(){
            @Override
            public String toString(Truck t){
                return "Truck id: " + t.numOfTruck;
            }

            @Override
            public Truck fromString(String s){
                return null;
            }
        });
        truckCombo.setOnAction(event -> {
            Truck t = truckCombo.getValue();
            infoLog.setText(t.infoAboutTruck());
        });
    }

    private void setManComboBox(){
        mansions = FXCollections.observableArrayList(GUI.mansions);
        manCombo.setItems(mansions);
        manCombo.setConverter(new StringConverter<AMansion>() {
            @Override
            public String toString(AMansion object) {
                if(object instanceof HQ){
                    return "HQ";
                } else {
                    return ((Mansion)object).name;
                }
            }

            @Override
            public AMansion fromString(String string) {
                return null;
            }
        });
        manCombo.setOnAction(event -> {
            AMansion man = manCombo.getValue();
            if(man instanceof HQ){
                infoLog.setText(((HQ)man).HQInfo());
            } else{
                infoLog.setText(((Mansion)man).mansionInfo());
            }
        });
    }

    public void importMansions() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose mansion source");
        File f = fc.showOpenDialog(consoleLog.getScene().getWindow());

        if(f == null){
            System.out.println("File not chosen! Terminating import!");
        } else {
            long start = System.nanoTime();
            GUI.mansions = FileIO.importFromFile(f);
            long end = System.nanoTime();
            System.out.println("Imported mansions from file in " + (end/1000000 -start/1000000) +"ms.");
            mansionsImported = true;
        }
    }

    public void importMatrixes() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose matrixes source");
        File f = fc.showOpenDialog(consoleLog.getScene().getWindow());

        if(f == null){
            System.out.println("File not chosen! Terminating import!");
        } else {
            long start = System.nanoTime();
            Pomocna p = FileIO.importMatrix(f);
            long end = System.nanoTime();
            GUI.distanceMatrix = p.getDistanceMatrix();
            GUI.timeMatrix = p.getTimeMatrix();
            System.out.println("Imported matrixes from file in " + (end/1000000 -start/1000000) +"ms.");
            matrixesImported = true;
        }
    }

    public void exportToFiles() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        dc.setTitle("Choose destination");
        File f = dc.showDialog(consoleLog.getScene().getWindow());

        if(f == null){
            System.out.println("Directory not chosen! Terminating export!");
        } else {
            System.out.println("Chosen dir: " + f.getPath());
            if(GUI.distanceMatrix == null || GUI.timeMatrix == null){
                System.out.println("Error. Matrixes are corrupted or don't exist! Please generate or import new ones.");
            } else {
                if(GUI.mansions == null){
                    System.out.println("Error. Mansions don't exist or can't be read! Please generate or import new ones.");
                } else {
                    FileIO.exportToFile(GUI.mansions, f);
                    FileIO.exportMatrix(GUI.distanceMatrix, GUI.timeMatrix, f);
                    System.out.println("Finished exporting files. Names: matrixes.txt and mansions.txt");
                }
            }
        }
    }

    public void exportStatistics() {
    }

    public void instructions() {
    }

    public void about() {
    }

    /**
     * Class used to redirect console output stream to the GUI TextArea.
     */
    class TextAreaConsole {
        private static final int STRING_BUFFER = 4096;
        private static final int FLUSH_INT = 200;
        private static final int MAX_TEXT_LEN = 256 * 2048; //256 znaku na radce, 2048 radek - dal se deli 2, takze realne jen 1024 radek (lepsi, rychlejsi)

        private final TextArea textArea;
        private final StringBuffer write = new StringBuffer();


        private final Thread writeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running){
                    try{
                        Thread.sleep(FLUSH_INT);
                        appendText();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        private final OutputStream out = new OutputStream() {
            private final byte buffer[] = new byte[STRING_BUFFER];

            @Override
            public void write(int b) throws IOException {
                if(pos == STRING_BUFFER){
                    flush();
                }
                buffer[pos] = (byte)b;
                pos++;
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                if(pos + len < STRING_BUFFER){
                    System.arraycopy(b, off, buffer, pos, len);
                    pos += len;
                } else {
                    flush();
                    if(len < STRING_BUFFER){
                        System.arraycopy(b, off, buffer, 0 /*pos - je taky 0*/, len);
                    } else {
                        write.append(new String(b, off, len));
                    }
                }
            }

            @Override
            public void flush() throws IOException {
                synchronized (write){
                    write.append(new String(buffer, 0, pos));
                    pos = 0;
                }
            }
        };

        private boolean running = false;
        private int pos = 0;

        private PrintStream defErr, defOut;

        public TextAreaConsole(TextArea textArea){
            this.textArea = textArea;
            defErr = System.err;
            defOut = System.out;
            writeThread.setDaemon(true);
        }

        public void start() {
            PrintStream ps = new PrintStream(out, true);
            System.setErr(ps);
            System.setOut(ps);
            running = true;
            writeThread.start();
        }

        public void stop(){
            running = false;
            System.setOut(defOut);
            System.setErr(defErr);
            try{
                writeThread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        private void appendText(){
            synchronized (write){
                if(write.length() > 0){
                    final String s = write.toString();
                    write.setLength(0);
                    Platform.runLater(() ->{
                        textArea.appendText(s);
                        int textLen = textArea.getText().length();
                        if(textLen > MAX_TEXT_LEN){
                            textArea.setText(textArea.getText(textLen - MAX_TEXT_LEN / 2, textLen));
                        }
                    });
                }
            }
        }
    }
/*
    public class Console extends OutputStream {

        private TextArea consoleLog;

        public Console(TextArea consoleLog){
            this.consoleLog = consoleLog;
        }

        public void appendText(String valueOf){

            Platform.runLater(() -> {
                consoleLog.appendText(valueOf);
                if(valueOf.equals("\n")) try {
                    flush();
                } catch (IOException e) {}

          //  try{
          //     if(valueOf.equals("\n")) {
          //          flush();
          //          Thread.sleep(10);
          //      }
          //  } catch (InterruptedException e) {}
          //  catch(IOException e){}
          //  });
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }*/
}
