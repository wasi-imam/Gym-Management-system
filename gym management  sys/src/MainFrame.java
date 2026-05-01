import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Gym Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabs banao
        JTabbedPane tabs = new JTabbedPane();

        // Panels add karo (abhi ke liye empty)
        tabs.addTab("Members", new MemberPanel());
        tabs.addTab("Payments",new PaymentPanel());

        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}