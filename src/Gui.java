import utils.UtilsImage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public class Gui extends Canvas {

    private JButton performSteganographyButton;
    private JPanel jPanel;
    private JTextArea messageTextArea;
    private JButton selectFileButton;
    private JTextField filePathTextArea;
    private JLabel selectLabel;
    private JLabel iconLabel;
    private String path = "https://picsum.photos/400/200";

    public Gui() {
        performSteganographyButton.addActionListener(e -> {
            Main main = new Main();
            try {
                if (messageTextArea.getText().startsWith("|") || messageTextArea.getText().endsWith("°")) {
                    messageTextArea.setText("");
                    JOptionPane.showMessageDialog(jPanel, "This is not allowed character to start/end your message");
                } else {
                    if(filePathTextArea.getText().isEmpty()) {
                        main.performSteganography(path, messageTextArea.getText());
                    } else {
                        main.performSteganography(filePathTextArea.getText(), messageTextArea.getText());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        selectFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int action = jFileChooser.showOpenDialog(null);
            if (action == JFileChooser.APPROVE_OPTION) {
                filePathTextArea.setText(jFileChooser.getSelectedFile().getAbsolutePath());

            } else {
                filePathTextArea.setText("");
            }
        });

        UtilsImage utilsImage = new UtilsImage();
        ImageIcon placeHolder = new ImageIcon(utilsImage.readImageURL(path));
        iconLabel.setIcon(placeHolder);
    }

    public JPanel getjPanel() {
        return jPanel;
    }
}
