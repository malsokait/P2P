package GUI;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by malsokait on 2015-12-03.
 */
public class TrackerGUI {
    private JPanel jp;
    private JTextArea textArea1;

    public TrackerGUI() {

    }

    public static void main(String[] args) {
        TrackerGUI trackerGUI = new TrackerGUI();
        JFrame frame = new JFrame("P2P Tracker");
        frame.setContentPane(trackerGUI.jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        PrintStream printStream = new PrintStream(new CustomOutputStream(trackerGUI.textArea1));
        System.setOut(printStream);
        System.setErr(printStream);
        frame.setVisible(true);
        Tracker tracker = new Tracker();

    }

    public static class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea1) {
            this.textArea = textArea1;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
