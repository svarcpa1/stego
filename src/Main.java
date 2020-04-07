import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Gui gui = new Gui();
        JFrame jFrame = new JFrame("SMART StegGo");
        jFrame.setContentPane(gui.getjPanel());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
