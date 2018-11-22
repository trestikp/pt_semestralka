package graphics;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.applet.Applet;
import java.io.InputStream;
import java.io.PrintStream;

public class GuiController {
    /** Simulation speed in [ms]. Default value = 50ms */
    private int simulation_speed = 50;

    private PrintStream ps;

    @FXML
    private TextArea consoleLog;

    @FXML
    private TextArea truckLog;

    @FXML
    private Button startBtn;

    @FXML
    private Button resumeBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button genBtn;

    @FXML
    private TextField manCount;

    @FXML
    private TextField simSpeed;

    //@FXML
    //private URL location;

    //@FXML
    //private ResourceBundle resources;

    public GuiController(){
    }

    @FXML
    private void initialize(){
        //PrintStream orig = System.out;
        //TextAreaToPrintStream ta = new TextAreaToPrintStream(consoleLog, orig);
        ps = new PrintStream(new Console(consoleLog), false);
        System.setOut(ps);
        System.setErr(ps);

    }

    @FXML
    public void startAction(){
        if(!(simSpeed.getText().equals(""))){
            simulation_speed = Integer.parseInt(simSpeed.getText());
            //System.out.println("CAS SIM: " + simulation_speed);
        }

        Thread sim = new Thread(() -> {GUI.sim.runSimulation(simulation_speed);
            //System.setOut(ps);
            //System.setErr(ps);
            //try{Thread.sleep(5);}
            //catch (InterruptedException e){}
        });
        sim.start();
    }

    @FXML
    public void resumeAction() {
        GUI.sim.resumeSim();
    }

    @FXML
    public void stopAction() {
        GUI.sim.endSimulation();
    }

    @FXML
    public void pauseAction() {
        GUI.sim.pauseSim();
    }

    @FXML
    public void generate(){
        if(manCount.getText().equals("")){
            GUI.runGeneration(2000);
        } else {
            if(Integer.parseInt(manCount.getText()) < 500 || Integer.parseInt(manCount.getText()) > 2000){
                System.out.println("Please enter a number between 500 and 2000");
            } else {
                GUI.runGeneration(Integer.parseInt(manCount.getText()));
            }
        }
    }

    class TextAreaToPrintStream extends PrintStream{
        TextArea ta;
        public TextAreaToPrintStream(TextArea ta, PrintStream out){
            super(out);
            this.ta = ta;
        }

        public void print(String s){
            ta.appendText(s);
        }

        public void println(String s){
            ta.appendText(s+"\n");
        }
    }

    /*
    public class Console extends OutputStream{
        private TextArea consoleLog;

        public Console(TextArea consoleLog){
            this.consoleLog = consoleLog;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> consoleLog.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }*/
}
