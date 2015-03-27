import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class GUI extends JFrame implements Runnable
{
    //<editor-fold desc="variable instantiation">
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
    //</editor-fold>

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
        SimPanel.GetJPanel().setMinimumSize(new Dimension(600, 800));//TODO the fuck did i do here ?
        SimPanel.GetJPanel().setBackground(Color.black);
        this.add(SimPanel.GetJPanel(), BorderLayout.CENTER);

        SimulationMenu SimMenu = new SimulationMenu();


        //<editor-fold desc="setting up timers">
        //initial timer setup
        Draw_Timer = new Timer(draw_deltaT,SimPanel);
        Update_Timer = new Timer(simulation_deltaT,simM);
            //make timers repeat
        Draw_Timer.setRepeats(true);
        Update_Timer.setRepeats(true);
            //start timers
        Draw_Timer.setActionCommand(""+draw_deltaT);
        Update_Timer.setActionCommand("" + simulation_deltaT);
        Draw_Timer.start();
        Update_Timer.start();
        //
        //</editor-fold>
    }

    public class SimulationMenu
    {

        //<editor-fold desc="variable instantiation">
        JPanel BoidOptions;
        JFrame Frame;
        JTabbedPane TabPane;
        JButton EnableFlocking_BoidOptions;
        JButton ShowBoidVector_BoidOptions;
        JButton ShowCohesionVector_BoidOptions;
        JButton ShowSeparationVector_BoidOptions;
        JButton ShowAlignmentVector_BoidOptions;
        JButton ShowDetectionCircle_BoidOptions;
        JButton ShowDetectedBoids_BoidOptions;
        JButton ShowBoids_BoidOptions;
        JSlider DetectionDistance_Slider_BoidOptions;
        JSlider DetectionAngle_Slider_BoidOptions;
        JLabel DetectionAngle_Slider_Label;
        JLabel DetectionDistance_Slider_Label;
        SimulationMenuActionListener Action_Listener = new SimulationMenuActionListener();
        SimulationMenuChangeListener Change_Listener = new SimulationMenuChangeListener();
        //</editor-fold>

        public SimulationMenu()
        {

            //<editor-fold desc="Setting Up Menu Frame and buttons etc">
            //the menu frame
            this.Frame = new JFrame();
            this.Frame.setSize(250, 500);
            this.Frame.setVisible(true);
            this.Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.Frame.setResizable(false);
            this.TabPane = new JTabbedPane();
            this.Frame.add(TabPane);

            Dimension ButtonDimension = new Dimension(200,25);
            //<editor-fold desc="Boid Options Tab">
            this.BoidOptions = new JPanel();
            this.TabPane.add("Boid Options",BoidOptions);
            //enable flocking button
            this.EnableFlocking_BoidOptions = new JButton("Enable Flocking");
            this.BoidOptions.add(EnableFlocking_BoidOptions);
            this.EnableFlocking_BoidOptions.addActionListener(Action_Listener);
            this.EnableFlocking_BoidOptions.setActionCommand("EnableFlocking_BoidOptions");
            this.EnableFlocking_BoidOptions.setPreferredSize(ButtonDimension);
            this.EnableFlocking_BoidOptions.setBackground(Color.red);
            //draw ShowBoids_BoidOptions button
            this.ShowBoids_BoidOptions = new JButton("Hide Boids");
            this.BoidOptions.add(ShowBoids_BoidOptions);
            this.ShowBoids_BoidOptions.addActionListener(Action_Listener);
            this.ShowBoids_BoidOptions.setActionCommand("ShowBoids_BoidOptions");
            this.ShowBoids_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowBoids_BoidOptions.setBackground(Color.green);
            //enable ShowBoidVector_BoidOptions button
            this.ShowBoidVector_BoidOptions = new JButton("Hide Boid Vector");
            this.BoidOptions.add(ShowBoidVector_BoidOptions);
            this.ShowBoidVector_BoidOptions.addActionListener(Action_Listener);
            this.ShowBoidVector_BoidOptions.setActionCommand("ShowBoidVector_BoidOptions");
            this.ShowBoidVector_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowBoidVector_BoidOptions.setBackground(Color.green);
            //enable Show_cohesion_vector
            this.ShowCohesionVector_BoidOptions = new JButton("Show Cohesion Vector");
            this.BoidOptions.add(ShowCohesionVector_BoidOptions);
            this.ShowCohesionVector_BoidOptions.addActionListener(Action_Listener);
            this.ShowCohesionVector_BoidOptions.setActionCommand("ShowCohesionVector_BoidOptions");
            this.ShowCohesionVector_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowCohesionVector_BoidOptions.setBackground(Color.red);
            //enable ShowSeparationVector_BoidOptions
            this.ShowSeparationVector_BoidOptions = new JButton("Show Separation Vector");
            this.BoidOptions.add(ShowSeparationVector_BoidOptions);
            this.ShowSeparationVector_BoidOptions.addActionListener(Action_Listener);
            this.ShowSeparationVector_BoidOptions.setActionCommand("ShowSeparationVector_BoidOptions");
            this.ShowSeparationVector_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowSeparationVector_BoidOptions.setBackground(Color.red);
             //enable ShowAlignmentVector_BoidOptions
            this.ShowAlignmentVector_BoidOptions = new JButton("Show Alignment Vector");
            this.BoidOptions.add(ShowAlignmentVector_BoidOptions);
            this.ShowAlignmentVector_BoidOptions.addActionListener(Action_Listener);
            this.ShowAlignmentVector_BoidOptions.setActionCommand("ShowAlignmentVector_BoidOptions");
            this.ShowAlignmentVector_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowAlignmentVector_BoidOptions.setBackground(Color.red);

            //Adding label for this section:
            JLabel DetectionRegionLabel = new JLabel();
            DetectionRegionLabel.setFont(new Font(DetectionRegionLabel.getFont().toString(),Font.BOLD,14));//make label font bold
            DetectionRegionLabel.setText("Boid Detection Settings:");
            this.BoidOptions.add(DetectionRegionLabel);
            //enable ShowDetectionCircle_BoidOptions
            this.ShowDetectionCircle_BoidOptions = new JButton("Show Detection Circle");
            this.BoidOptions.add(ShowDetectionCircle_BoidOptions);
            this.ShowDetectionCircle_BoidOptions.addActionListener(Action_Listener);
            this.ShowDetectionCircle_BoidOptions.setActionCommand("ShowDetectionCircle_BoidOptions");
            this.ShowDetectionCircle_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowDetectionCircle_BoidOptions.setBackground(Color.red);
            //boid detection distance slider
            DetectionDistance_Slider_Label = new JLabel("Detection Distance (in pixel): " + SimSettings.getDetection_Distance(),JLabel.LEFT);
            this.BoidOptions.add(DetectionDistance_Slider_Label);
            this.DetectionDistance_Slider_BoidOptions = new JSlider(JSlider.HORIZONTAL,0,200,SimSettings.getDetection_Distance());
            this.DetectionDistance_Slider_BoidOptions.addChangeListener(Change_Listener);
            this.DetectionDistance_Slider_BoidOptions.setName("DetectionDistanceSlider");
            this.DetectionDistance_Slider_BoidOptions.setMajorTickSpacing(50);
            this.DetectionDistance_Slider_BoidOptions.setMinorTickSpacing(10);
            this.DetectionDistance_Slider_BoidOptions.setPaintTicks(true);
            this.DetectionDistance_Slider_BoidOptions.setPaintLabels(true);
            this.BoidOptions.add(DetectionDistance_Slider_BoidOptions);
            //boid DetectionAngle_Slider_BoidOptions
            this.DetectionAngle_Slider_BoidOptions = new JSlider(JSlider.HORIZONTAL,0,100,SimSettings.getDetectionAngle());
            DetectionAngle_Slider_Label = new JLabel("Detection Angle in rad: " + String.format("%.2f",(double)2*Math.PI*((double)SimSettings.getDetectionAngle()/100)),JLabel.LEFT);
            this.BoidOptions.add(DetectionAngle_Slider_Label);
            this.BoidOptions.add(DetectionAngle_Slider_BoidOptions);
            this.DetectionAngle_Slider_BoidOptions.addChangeListener(Change_Listener);
            this.DetectionAngle_Slider_BoidOptions.setName("DetectionAngleSlider");

            //enable Show_Boids_nearby
            this.ShowDetectedBoids_BoidOptions = new JButton("Hide Detected Boids");
            this.BoidOptions.add(ShowDetectedBoids_BoidOptions);
            this.ShowDetectedBoids_BoidOptions.addActionListener(Action_Listener);
            this.ShowDetectedBoids_BoidOptions.setActionCommand("ShowDetectedBoids_BoidOptions");
            this.ShowDetectedBoids_BoidOptions.setPreferredSize(ButtonDimension);
            this.ShowDetectedBoids_BoidOptions.setBackground(Color.green);

            //</editor-fold>



            //</editor-fold>

        }
        //TODO enter defaults in switch statemetns
        public class SimulationMenuActionListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().contains("BoidOptions"))
                {
                    //<editor-fold desc="BoidOptions components switch">
                    switch (e.getActionCommand())
                    {
                        case "ShowBoids_BoidOptions":
                        {
                            if(SimSettings.Show_Boid)
                            {
                                SimSettings.Show_Boid = false;
                                ShowBoids_BoidOptions.setText("Show Boid");
                                ShowBoids_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_Boid = true;
                                ShowBoids_BoidOptions.setText("Hide Boid");
                                ShowBoids_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }
                        case "ShowDetectedBoids_BoidOptions":{
                            if(SimSettings.Show_Boids_nearby)
                            {
                                SimSettings.Show_Boids_nearby = false;
                                ShowDetectedBoids_BoidOptions.setText("Show Detected Boids");
                                ShowDetectedBoids_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_Boids_nearby = true;
                                ShowDetectedBoids_BoidOptions.setText("Hide Detected Boids");
                                ShowDetectedBoids_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }
                        case "ShowDetectionCircle_BoidOptions":{
                            if(SimSettings.Show_Detection_circle)
                            {
                                SimSettings.Show_Detection_circle = false;
                                ShowDetectionCircle_BoidOptions.setText("Show Detection Circle");
                                ShowDetectionCircle_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_Detection_circle = true;
                                ShowDetectionCircle_BoidOptions.setText("Hide Detection Circle");
                                ShowDetectionCircle_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }
                        case "EnableFlocking_BoidOptions": {
                            if(SimSettings.Flocking_Enabled)
                            {
                                SimSettings.Flocking_Enabled = false;
                                EnableFlocking_BoidOptions.setText("Enable Flocking");
                                EnableFlocking_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Flocking_Enabled = true;
                                EnableFlocking_BoidOptions.setText("Disable Flocking");
                                EnableFlocking_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }
                        case "ShowBoidVector_BoidOptions": {
                            if(SimSettings.Show_Boid_vector)
                            {
                                SimSettings.Show_Boid_vector = false;
                                ShowBoidVector_BoidOptions.setText("Show Boid Vector");
                                ShowBoidVector_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_Boid_vector = true;
                                ShowBoidVector_BoidOptions.setText("Hide Boid Vector");
                                ShowBoidVector_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }

                        case "ShowCohesionVector_BoidOptions": {
                            if(SimSettings.Show_cohesion_vector)
                            {
                                SimSettings.Show_cohesion_vector = false;
                                ShowCohesionVector_BoidOptions.setText("Show Cohesion Vector");
                                ShowCohesionVector_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_cohesion_vector = true;
                                ShowCohesionVector_BoidOptions.setText("Hide Cohesion Vector");
                                ShowCohesionVector_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }

                        case "ShowSeparationVector_BoidOptions":{
                            if(SimSettings.Show_seperation_vector)
                            {
                                SimSettings.Show_seperation_vector = false;
                                ShowSeparationVector_BoidOptions.setText("Show Separation Vector");
                                ShowSeparationVector_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_seperation_vector = true;
                                ShowSeparationVector_BoidOptions.setText("Hide Separation Vector");
                                ShowSeparationVector_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }

                        case "ShowAlignmentVector_BoidOptions":{
                            if(SimSettings.Show_allignment_vector)
                            {
                                SimSettings.Show_allignment_vector = false;
                                ShowAlignmentVector_BoidOptions.setText("Show Alignment Vector");
                                ShowAlignmentVector_BoidOptions.setBackground(Color.red);
                            }
                            else
                            {
                                SimSettings.Show_allignment_vector = true;
                                ShowAlignmentVector_BoidOptions.setText("Hide Alignment Vector");
                                ShowAlignmentVector_BoidOptions.setBackground(Color.green);
                            }
                            break;
                        }

                    }
                    //</editor-fold>
                }
                else if (true)
                {

                }
            }
        }


        public class SimulationMenuChangeListener implements ChangeListener
        {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(e.getSource() instanceof JSlider)
                {
                    JSlider source = (JSlider)e.getSource();
                    switch (source.getName())
                    {
                        case "DetectionDistanceSlider":
                        {
                            DetectionDistance_Slider_Label.setText("Detection Distance (in pixel): " + SimSettings.getDetection_Distance());
                            SimSettings.setDetection_Distance(source.getValue());
                            break;
                        }
                        case "DetectionAngleSlider":
                        {
                            DetectionAngle_Slider_Label.setText("Detection Angle in rad: " + String.format("%.2f",(double)2*Math.PI*((double)SimSettings.getDetectionAngle()/100)));
                            SimSettings.setDetectionAngle(source.getValue());
                            break;
                        }

                    }
                }
            }
        }

    }


    @Override //never gets called, since its overwritten when gui object is created
    public void run() {

    }
}
