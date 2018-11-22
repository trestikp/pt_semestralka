package graphics;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
            /*
            try{
                if(valueOf.equals("\n")) {
                    flush();
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {}
            catch(IOException e){}*/
        });
    }

    public void write(int b) throws IOException {
        appendText(String.valueOf((char)b));
    }
}