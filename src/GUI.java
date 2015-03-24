import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements Runnable
{
    Timer Draw_Timer;//the timer responsible for triggering the simulation panel draw.lower delay means more fps
    Timer Update_Timer;//how often the boids are updated, lower delay means more accurate simulation
    private int simulation_deltaT = 1; //simulation update step. default is 1ms
    private int draw_deltaT = 50;//simulation draw step. default is 16ms for 60 fps

    protected simulation_manager simM ;
    private JTabbedPane Tabs;
    private JPanel SimOptionsTab;
    private JPanel BoidOptionTab;
    private JPanel DrawOptionTab;
    private Simulation_Panel SimPanel;

    public static void main(String[] args)
    {
        final simulation_manager simmanager = new simulation_manager();
        //starts the Gui on a separate thread
        SwingUtilities.invokeLater(new GUI()
        {
            @Override
            public void run()
            {
                this.simM = simmanager;
                CreateGUI();
            }
        });
        //start the sim manager Update Loop

    }

    public void CreateGUI()
    {

        this.setSize(1280,720);//TODO get universal settings working to save this stuff
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        Tabs = new JTabbedPane();
        Tabs.setVisible(true);

        SimPanel = new Simulation_Panel(this.simM);
        SimPanel.GetJPanel().setSize(800, 600);
        SimPanel.GetJPanel().setMinimumSize(new Dimension(600, 800));
        SimPanel.GetJPanel().setBackground(Color.black);
        this.add(SimPanel.GetJPanel(), BorderLayout.CENTER);

        SimOptionsTab = new JPanel();
        BoidOptionTab = new JPanel();
        DrawOptionTab = new JPanel();
        SimOptionsTab.setMinimumSize(new Dimension(300,720));
        Tabs.setMinimumSize(new Dimension(300,720));
        Tabs.add(SimOptionsTab);
        Tabs.add(BoidOptionTab);
        Tabs.add(DrawOptionTab);
        this.add(Tabs,BorderLayout.EAST);

        //initial timer setup
        Draw_Timer = new Timer(draw_deltaT,SimPanel);
        Update_Timer = new Timer(simulation_deltaT,simM);
            //make timers repeat
        Draw_Timer.setRepeats(true);
        Update_Timer.setRepeats(true);
            //start timers
        Draw_Timer.setActionCommand(""+draw_deltaT);
        Update_Timer.setActionCommand(""+simulation_deltaT);
        Draw_Timer.start();
        Update_Timer.start();
        //
    }





    @Override //never gets called, since its overwritten when gui object is created
    public void run() {

    }
}
