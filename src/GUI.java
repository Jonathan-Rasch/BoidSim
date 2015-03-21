import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements Runnable
{

    protected simulation_manager simM ;
    private JTabbedPane Tabs;
    private JPanel SimOptionsTab;
    private JPanel BoidOptionTab;
    private JPanel DrawOptionTab;
    private JPanel SimulationPanel;

    public static void main(String[] args)
    {
        final simulation_manager simmanager = new simulation_manager();
        SwingUtilities.invokeLater(new GUI()
        {
            @Override
            public void run()
            {
                this.simM = simmanager;
                CreateGUI();
            }
        });
    }

    public void CreateGUI()
    {
        this.setSize(1280,720);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        Tabs = new JTabbedPane();
        Tabs.setVisible(true);

        SimulationPanel = new JPanel();
        SimulationPanel.setSize(800,600);
        SimulationPanel.setMinimumSize(new Dimension(600,800));
        SimulationPanel.setBackground(Color.black);
        this.add(SimulationPanel,BorderLayout.CENTER);

        SimOptionsTab = new JPanel();
        BoidOptionTab = new JPanel();
        DrawOptionTab = new JPanel();
        SimOptionsTab.setMinimumSize(new Dimension(300,720));
        Tabs.setMinimumSize(new Dimension(300,720));
        Tabs.add(SimOptionsTab);
        Tabs.add(BoidOptionTab);
        Tabs.add(DrawOptionTab);
        this.add(Tabs,BorderLayout.EAST);

    }

    @Override //never gets called, since its overwritten when gui object is created
    public void run() {

    }
}
