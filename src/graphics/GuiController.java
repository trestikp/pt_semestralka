package graphics;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class GuiController {
    /** Simulation speed in [ms]. Default value = 20ms */
    private int simulation_speed;

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
        //ps = new PrintStream(new Console(consoleLog), false);
        //System.setOut(ps);
        //System.setErr(ps);

        TextAreaConsole tac = new TextAreaConsole(consoleLog);
        tac.start();
    }

    @FXML
    public void startAction(){
        boolean run = false;
        if(!(simSpeed.getText().equals(""))){
            if(Integer.parseInt(simSpeed.getText()) < 15){
                System.out.println("Speed of the simulation must be 15 or more ms!");
            } else {
                simulation_speed = Integer.parseInt(simSpeed.getText());
                run = true;
            }
            //System.out.println("CAS SIM: " + simulation_speed);
        } else {
            simulation_speed = 20;
            run = true;
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

    public class TextAreaConsole {
        private static final int STRING_BUFFER = 4096;
        private static final int FLUSH_INT = 200;
        private static final int MAX_TEXT_LEN = 256 * 2048; //256 znaku na radce, 2048 radek - dal se deli 2, takze realne jen 1024 radek (lepsi, rychlejsi)

        private final TextArea textArea;
        private  final StringBuffer write = new StringBuffer();


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
