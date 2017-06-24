package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class LogConsole extends WriterAppender {

    private static TextArea console = null;

    public static void setLogConsole(TextArea textArea) {
        LogConsole.console = textArea;
    }

    @Override
    public void append(final LoggingEvent loggingEvent) {
        final String newLine = this.layout.format(loggingEvent);
        try {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    if (console != null) {
                        if (console.getText().length() == 0) {
                            console.setText(newLine);
                        } else {
                            console.selectEnd();
                            console.insertText(console.getText().length(), newLine);
                        }
                    }

                }
            });
        } catch (IllegalStateException e) {
            // ignore - the relevant GUI is not initialized!
        }
    }

}
