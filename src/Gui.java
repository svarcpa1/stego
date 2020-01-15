import utils.UtilsGeneral;
import utils.UtilsImage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gui extends Canvas {

    private JButton performSteganographyButton;
    private JPanel jPanel;
    private JTextArea messageTextArea;
    private JButton selectFileButton;
    private JTextField filePathTextArea;
    private JLabel selectLabel;
    private JLabel iconLabel;
    private JLabel imageInfoLabel;
    private JButton anotherImageButton;
    private String path = "https://picsum.photos/400/200";
    private BufferedImage placeholderBufferedImage;

    public Gui() {

        UtilsImage utilsImage = new UtilsImage();
        UtilsGeneral utilsGeneral = new UtilsGeneral();

        placeholderBufferedImage = utilsImage.readImageURL(path);
        ImageIcon placeHolder = new ImageIcon(placeholderBufferedImage);
        iconLabel.setIcon(placeHolder);

        anotherImageButton.addActionListener(e -> {
            placeholderBufferedImage = utilsImage.readImageURL(path);
            ImageIcon placeHolder1 = new ImageIcon(placeholderBufferedImage);
            filePathTextArea.setText("");
            iconLabel.setIcon(placeHolder1);
        });

        performSteganographyButton.addActionListener(e -> {
            Main main = new Main();
            try {
                if (messageTextArea.getText().startsWith("|") || messageTextArea.getText().endsWith("°")) {
                    messageTextArea.setText("");
                    JOptionPane.showMessageDialog(jPanel, "This is not allowed character to start/end your message");
                } else {
                    if(filePathTextArea.getText().isEmpty()) {
                        main.performSteganography(path, placeholderBufferedImage, messageTextArea.getText());
                    } else {
                        main.performSteganography(filePathTextArea.getText(),null, messageTextArea.getText());
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
                if (!utilsGeneral.isImageLoadedFromURL(filePathTextArea.getText())) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(utilsImage.readImageFile(filePathTextArea.getText()));
                        Image image = imageIcon.getImage();
                        Image scaledImage = image.getScaledInstance(400, 200,  java.awt.Image.SCALE_SMOOTH);
                        iconLabel.setIcon(new ImageIcon(scaledImage));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                filePathTextArea.setText("");
            }
        });
    }

    public JPanel getjPanel() {
        return jPanel;
    }
}
