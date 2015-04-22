import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements Runnable
{
    //<editor-fold desc="variable instantiation">
    Settings SimSettings;
    simulation_manager simM ;

    Timer Draw_Timer;//the timer responsible for triggering the simulation panel draw.lower delay means more fps
    Timer Update_Timer;//how often the boids are updated, lower delay means more accurate simulation
    private int simulation_deltaT = 1; //simulation update step. default is 1ms
    private int draw_deltaT = 16;//simulation draw step. default is 16ms for 60 fps


    private Simulation_Panel SimPanel;
    //</editor-fold>

    public static void main(String[] args)
    {
        final Settings SimSetting = new Settings();
        final simulation_manager simmanager = new simulation_manager(SimSetting);

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
        simmanager.Update();
    }

    //closes this window
    public void EXIT(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void CreateGUI(Settings sett)
    {
        this.SimSettings = sett;

        this.setSize((int)SimSettings.getScreenDimension().getWidth(),(int)SimSettings.getScreenDimension().getHeight());//TODO get universal settings working to save this stuff
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());


        SimPanel = new Simulation_Panel(this.SimSettings);
        SimPanel.GetJPanel().setSize(SimSettings.getScreenDimension());
        SimPanel.GetJPanel().setMinimumSize(new Dimension(600, 800));//TODO the fuck did i do here ?
        SimPanel.GetJPanel().setBackground(Color.black);
        this.add(SimPanel.GetJPanel(), BorderLayout.CENTER);

        SimulationMenu SimMenu = new SimulationMenu();


        //<editor-fold desc="setting up timers">
        //initial timer setup
        Draw_Timer = new Timer(draw_deltaT,SimPanel);
         //make timers repeat
        Draw_Timer.setRepeats(true);
        //start timers
        Draw_Timer.setActionCommand("" + draw_deltaT);
        Draw_Timer.start();
        //
        //</editor-fold>
    }

    public class SimulationMenu
    {

        //<editor-fold desc="variable instantiation">
        JFrame Frame;
        JTabbedPane TabPane;

        //<editor-fold desc="BoidOptions">
        JPanel BoidOptions;
        JButton EnableFlocking_BoidOptions;
        JButton ShowBoidVector_BoidOptions;
        JButton ShowDetectedBoids_BoidOptions;
        JButton ShowBoids_BoidOptions;
        JSlider DetectionDistance_Slider_BoidOptions;
        JSlider DetectionAngle_Slider_BoidOptions;
        JLabel DetectionAngle_Slider_Label;
        JLabel DetectionDistance_Slider_Label;
        //</editor-fold>

        JPanel SimulationOptions;
        JButton StartPause;
        JButton Save;//file explorer
        JButton Load;
        JButton Clear;//show warning
        JButton SimulationBorder;
        JButton EXIT;//display warning before exit
        JSlider SimulationAccuracy;



        JRadioButton ScaryMouse = new JRadioButton("Scary Mouse");
        JRadioButton FriendlyMouse = new JRadioButton("Friendly Mouse");
        JRadioButton IgnoreMouse = new JRadioButton("Ignore Mouse");

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

            //Adding label for this section:
            JLabel DetectionRegionLabel = new JLabel();
            DetectionRegionLabel.setFont(new Font(DetectionRegionLabel.getFont().toString(),Font.BOLD,14));//make label font bold
            DetectionRegionLabel.setText("Boid Detection Settings:");
            this.BoidOptions.add(DetectionRegionLabel);

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
            this.DetectionAngle_Slider_BoidOptions = new JSlider(JSlider.HORIZONTAL,0,100,SimSettings.getDetectionAngle_percentage());
            DetectionAngle_Slider_Label = new JLabel("Detection Angle in rad: " + String.format("%.2f",(double)2*Math.PI*((double)SimSettings.getDetectionAngle_percentage()/100)),JLabel.LEFT);
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

            this.SimulationOptions = new JPanel();
            this.TabPane.add("Simulation",SimulationOptions);

            //create buttons
            this.StartPause = new JButton("Start");
            this.Save = new JButton("Save Simulation");
            this.Load = new JButton("Load Simulation");
            this.Clear = new JButton("Clear Simulation");
            this.EXIT = new JButton("EXIT");
            this.SimulationBorder = new JButton("Disable Simulation Border");
            //add buttons to panel
            this.SimulationOptions.add(StartPause);
            this.SimulationOptions.add(Save);
            this.SimulationOptions.add(Load);
            this.SimulationOptions.add(Clear);
            this.SimulationOptions.add(EXIT);
            this.SimulationOptions.add(SimulationBorder);
            //set button dimensions
            this.StartPause.setPreferredSize(ButtonDimension);
            this.Save.setPreferredSize(ButtonDimension);
            this.Load.setPreferredSize(ButtonDimension);
            this.Clear.setPreferredSize(ButtonDimension);
            this.EXIT.setPreferredSize(ButtonDimension);
            this.SimulationBorder.setPreferredSize(ButtonDimension);
            //add action lisener to buttons and setting action commands
            this.StartPause.addActionListener(Action_Listener);
            this.Save.addActionListener(Action_Listener);
            this.Load.addActionListener(Action_Listener);
            this.Clear.addActionListener(Action_Listener);
            this.EXIT.addActionListener(Action_Listener);
            this.SimulationBorder.addActionListener(Action_Listener);

            this.StartPause.setActionCommand("StartPause");
            this.Save.setActionCommand("Save");
            this.Load.setActionCommand("Load");
            this.Clear.setActionCommand("Clear");
            this.EXIT.setActionCommand("EXIT");
            this.SimulationBorder.setActionCommand("SimulationBorder");
            //</editor-fold>

        }
        //TODO enter defaults in switch statemetns
        public class SimulationMenuActionListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                    //<editor-fold desc="BoidOptions components switch">
                    switch (e.getActionCommand())
                    {
                        //<editor-fold desc="Boid Options Action events">
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
                        //</editor-fold>

                        case "StartPause" :{
                            if(SimSettings.SimulationRunning){
                                SimSettings.SimulationRunning = false;
                                StartPause.setText("Start Simulation");
                            }
                            else {
                                SimSettings.SimulationRunning = true;
                                StartPause.setText("Pause Simulation");
                            }
                            break;
                        }
                        case "Save" :{
                            SimSettings.SaveSimulation = true;
                            break;
                        }
                        case "Load" :{
                            SimSettings.LoadSimulation = true;
                            break;
                        }
                        case "Clear" :{
                            SimSettings.ClearSimulation = true;;//remove all boids
                            break;
                        }
                        case "EXIT" :{
                            //closes simulation manager
                            simM.EXIT();
                            //closes this window
                            EXIT();
                            break;
                        }
                        case "SimulationBorder" :{
                            if(SimSettings.BorderedSimulation){
                                SimSettings.BorderedSimulation = false;
                                SimulationBorder.setText("Enable Simulation Border");
                            }
                            else {
                                SimSettings.BorderedSimulation = true;
                                SimulationBorder.setText("Disable Simulation Border");
                            }
                            break;
                        }
                    }
                    //</editor-fold>
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
                            DetectionAngle_Slider_Label.setText("Detection Angle in rad: " + String.format("%.2f",(double)2*Math.PI*((double)SimSettings.getDetectionAngle_percentage()/100)));
                            SimSettings.setDetectionAngle_percentage(source.getValue());
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
