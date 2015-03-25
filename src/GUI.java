import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements Runnable
{
    Settings SimSettings;

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
        final Settings SimSetting = new Settings();
        //TODO final keyword ??? why did i put it here ? important ???
        final simulation_manager simmanager = new simulation_manager(SimSetting);
        //starts the Gui on a separate thread
        SwingUtilities.invokeLater(new GUI()
        {
            @Override
            public void run()
            {
                this.simM = simmanager;
                CreateGUI(SimSetting);
            }
        });
        //start the sim manager Update Loop

    }

    public void CreateGUI(Settings sett)
    {
        this.SimSettings = sett;
        this.setSize((int)SimSettings.getScreenDimension().getWidth(),(int)SimSettings.getScreenDimension().getHeight());//TODO get universal settings working to save this stuff
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());


        SimPanel = new Simulation_Panel(this.simM);
        SimPanel.GetJPanel().setSize(SimSettings.getScreenDimension());
        SimPanel.GetJPanel().setMinimumSize(new Dimension(600, 800));
        SimPanel.GetJPanel().setBackground(Color.black);
        this.add(SimPanel.GetJPanel(), BorderLayout.CENTER);

        SimulationMenu SimMenu = new SimulationMenu();


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

    public class SimulationMenu implements ActionListener
    {
        JButton EnableFlocking_BoidOptions;
        JPanel BoidOptions;
        JFrame Frame;
        JTabbedPane TabPane;

        public SimulationMenu()
        {
            //the menu frame
            this.Frame = new JFrame();
            this.Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.Frame.setResizable(false);
            this.TabPane = new JTabbedPane();
            this.Frame.add(TabPane);

            //world options tab
            this.BoidOptions = new JPanel();
            this.TabPane.add("Boid Options",BoidOptions);
                //enable flocking button
                this.EnableFlocking_BoidOptions = new JButton("Enable Flocking");
                this.BoidOptions.add(EnableFlocking_BoidOptions);
                this.EnableFlocking_BoidOptions.addActionListener(this);
                this.EnableFlocking_BoidOptions.setActionCommand("EnableFlocking_BoidOptions");


            this.Frame.setSize(200,500);
            this.Frame.setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand())
            {
                case "EnableFlocking_BoidOptions":
                {
                    if(SimSettings.Flocking_Enabled)
                    {
                        SimSettings.Flocking_Enabled = false;
                        this.EnableFlocking_BoidOptions.setText("Enable Flocking");
                    }
                    else
                    {
                        SimSettings.Flocking_Enabled = true;
                        this.EnableFlocking_BoidOptions.setText("Disable Flocking");
                    }
                    break;
                }

            }
        }
    }


    @Override //never gets called, since its overwritten when gui object is created
    public void run() {

    }
}
