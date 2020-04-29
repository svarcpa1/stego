import crypto.CryptoMain;
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
    private JLabel performedMethodLabel;
    private String path = "https://picsum.photos/400/200";
    private BufferedImage placeholderBufferedImage;
    private CryptoMain cryptoMain = new CryptoMain();

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
            imageInfoLabel.setText("size: 200x200px");
        });

        performSteganographyButton.addActionListener(e -> {
            try {
                if (messageTextArea.getText().startsWith("|") || messageTextArea.getText().endsWith("°")) {
                    messageTextArea.setText("");
                    JOptionPane.showMessageDialog(jPanel,
                            "This is not allowed character to start/end your message");
                } else {

                    String decide = cryptoMain.decideCodeOrDecode(filePathTextArea.getText());
                    if (decide == "decode") {
                        if (!utilsGeneral.isImageLoadedFromURL(filePathTextArea.getText())) {
                            String message = cryptoMain.decode(filePathTextArea.getText());
                            messageTextArea.setText(message);
                            performedMethodLabel.setText(cryptoMain.getMessageLog());
                        } else {
                            System.out.println("URL address cannot be decoded yet");
                        }
                    }

                    else {
                        BufferedImage sourceImage;
                        if(!filePathTextArea.getText().isEmpty()) {

                            if (utilsGeneral.isImageLoadedFromURL(filePathTextArea.getText())) {
                                sourceImage = utilsImage.readImageURL(filePathTextArea.getText());
                            } else {
                                sourceImage = utilsImage.readImageFile(filePathTextArea.getText());
                            }
                            int cryptoMode = cryptoMain.codeMethod(sourceImage, messageTextArea.getText());

                            cryptoMain.code(cryptoMode, filePathTextArea.getText(), null,
                                    messageTextArea.getText());
                            performedMethodLabel.setText(cryptoMain.getMessageLog() + " length was "+
                                    cryptoMain.getMessageLength());

                        } else {
                            int cryptoMode = cryptoMain.codeMethod(placeholderBufferedImage, messageTextArea.getText());
                            cryptoMain.code(cryptoMode, filePathTextArea.getText(), placeholderBufferedImage,
                                    messageTextArea.getText());
                            performedMethodLabel.setText(cryptoMain.getMessageLog() + " length was "+
                                    cryptoMain.getMessageLength());
                        }
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
                        Image scaledImage = image.getScaledInstance(400, 200,
                                java.awt.Image.SCALE_SMOOTH);
                        iconLabel.setIcon(new ImageIcon(scaledImage));
                        imageInfoLabel.setText("size: "+ image.getWidth(jFileChooser) +"x"+
                                        image.getHeight(jFileChooser) +"px");

                        String decide = cryptoMain.decideCodeOrDecode(filePathTextArea.getText());
                        if (decide == "decode") {
                            if (!utilsGeneral.isImageLoadedFromURL(filePathTextArea.getText())) {
                                String message = cryptoMain.decode(filePathTextArea.getText());
                                messageTextArea.setText(message);
                                performedMethodLabel.setText(cryptoMain.getMessageLog() + " length was "+
                                        cryptoMain.getMessageLength());
                            } else {
                                System.out.println("URL address cannot be decoded yet");
                            }
                        }
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
