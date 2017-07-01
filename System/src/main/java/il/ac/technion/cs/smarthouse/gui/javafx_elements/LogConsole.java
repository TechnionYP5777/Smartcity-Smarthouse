package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * This is an appender which will be used in order to present the LOG in a GUI
 * console
 * 
 * @author Inbal Zukerman
 * @since 25-06-2017
 */
public class LogConsole extends WriterAppender {

    private static TextArea console;
    private static Logger log = LoggerFactory.getLogger(LogConsole.class);

    public static void setLogConsole(TextArea a) {
        LogConsole.console = a;
    }

    @Override
    public void append(final LoggingEvent loggingEvent) {
        final String newLine = this.layout.format(loggingEvent);
        try {
            Platform.runLater(new Runnable() {

                @Override
                @SuppressWarnings("synthetic-access")
                public void run() {
                    if (console != null) {
                        if (console.getText().length() == 0)
                            console.setText(newLine);
                        else {
                            console.selectEnd();
                            console.insertText(console.getText().length(), newLine);
                        }
                    }
                }
            });
        } catch (IllegalStateException e) {
            // ignore - the relevant GUI is not initialized!
            log.error("No relevant GUI element was initialized", e);
        }
    }

}
