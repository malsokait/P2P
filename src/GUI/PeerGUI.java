package GUI;

import com.google.common.base.Splitter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by malsokait on 2015-12-01.
 */
public class PeerGUI {
    public static final String REQUEST = "request";
    public static final String REGISTER = "register";

    private JScrollPane scrollPane;
    private JTextArea textArea1;
    private JPanel jp;
    private JTextField textField1;
    private PeerManager peerManager;

    public PeerGUI() {
        textField1.addActionListener(action);
    }

    public static class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    Action action = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            processInput(textField1.getText());
            textField1.setText("");
        }
    };

    private void processInput(String text) {
        String command = Splitter.onPattern("\\s").split(text).iterator().next();
        String arg = text.substring(command.length()).trim();
        switch (command.toLowerCase()) {
            case REQUEST:
                if (arg.length() > 0)
                    peerManager.requestFile(arg);
                else
                    System.out.println("Please supply file name.");
                break;
            case REGISTER:
                if (arg.length() > 0)
                    peerManager.registerFile(arg);
                else
                    System.out.println("Please supply file name.");
                break;
            default:
                System.out.println("Unknown command.");
                break;
        }

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("gui");
        PeerGUI g = new PeerGUI();
        g.peerManager = new PeerManager();
        frame.setTitle(g.peerManager.getPeer().getId().toString());
        frame.setContentPane(g.jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        PrintStream printStream = new PrintStream(new CustomOutputStream(g.textArea1));
        System.setOut(printStream);
        System.setErr(printStream);
        frame.setVisible(true);
        g.peerManager.joinNetwork();

    }

}
