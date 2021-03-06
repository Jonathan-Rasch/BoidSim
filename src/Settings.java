import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Settings implements ActionListener {

    //<editor-fold desc="ScreenDimension getter and setter">
    //setting values with default values
    private Dimension ScreenDimension;
    public boolean SaveSimulation;
    public boolean LoadSimulation;
    public boolean ClearSimulation;

    public void setScreenDimension(Dimension screenDimension) {
        //clone the dimension
        this.ScreenDimension = new Dimension(screenDimension.width,screenDimension.height);
    }
    public Dimension getScreenDimension() {
        return new Dimension(this.ScreenDimension.width,this.ScreenDimension.height);
    }
    //</editor-fold>

    private int Simulation_timestep = 1; //how long in ms until the simulation is updated 1ms = 1000 updates/sec
    private int Max_FPS = 30;//used to calculate draw delay ( draw_timestep = (1/Max_FPS )*1000
    private int Draw_timestep = 33;//delay between repaint calls

    private int Detection_Distance = 50;
    public int getDetection_Distance() {
        return Detection_Distance;
    }
    public void setDetection_Distance(int detection_Distance) {
        Detection_Distance = detection_Distance;
    }


    private double cohesion_multiplier = 100;//by what value the cohesion vector is multiplied
    private double alignment_multiplier = 100;//by what value the alignment vector is multiplied
    private double separation_multiplier = 100;//by what value the alignment vector is multiplied

    private int DetectionAngle_percentage = 50;//percentage of 2PI : 2PI*1/DetectionAngle_percentage
    public int getDetectionAngle_percentage() {
        return DetectionAngle_percentage;
    }
    public void setDetectionAngle_percentage(int detectionAngle_percentage) {
        DetectionAngle_percentage = detectionAngle_percentage;
    }
    public double getDetectionAngle_rad(){
        double Angle = ((double)getDetectionAngle_percentage()/100) * 2 * Math.PI;
        return Angle;
    }

    public boolean BorderedSimulation = true;
    public boolean Flocking_Enabled = false;
    public boolean Enforce_Minimum_Speed = true;
    public boolean Show_Boid_vector = true;
    public boolean Show_Boids_nearby = true ;
    public boolean Show_Boid = true;
    public boolean SimulationRunning = true;

    private List<Drawable> Draw_List = Collections.synchronizedList(new ArrayList<Drawable>());
    public synchronized List<Drawable> GetDrawList(){
        List<Drawable> returnList = new ArrayList<Drawable>();
        returnList.addAll(Draw_List);
        return returnList;
    }
    public synchronized void SetDrawList(List<Drawable> List){
        this.Draw_List = Boid_Maths.cloneList(List);
    }

    public Settings()
    {
        int choice = JOptionPane.showConfirmDialog(null,"would you like to use the whole screen for the simulation ?","Sim Window Size",JOptionPane.YES_NO_OPTION);

        if(choice == JOptionPane.YES_OPTION)
        {
            ScreenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        }
        else if (choice == JOptionPane.NO_OPTION)
        {
            int tempScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int tempScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

            try {
                int Xdimension = Integer.parseInt(JOptionPane.showInputDialog("set width (minimum 200)"));
                if (Xdimension > tempScreenWidth || Xdimension < 200){
                    JOptionPane.showMessageDialog(null,"Invalid Input, setting width to " + tempScreenWidth );
                    Xdimension = tempScreenWidth;
                }

                int Ydimension = Integer.parseInt(JOptionPane.showInputDialog("set height (minimum 200)"));
                if (Ydimension > tempScreenHeight || Ydimension < 200){
                    JOptionPane.showMessageDialog(null,"Invalid Input, setting height to " + tempScreenHeight );
                    Ydimension = tempScreenHeight;
                }

                ScreenDimension = new Dimension(Xdimension,Ydimension);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,"Invalid Input, setting size to " + tempScreenWidth + "x" + tempScreenHeight );
                ScreenDimension = new Dimension(tempScreenWidth,tempScreenHeight);
            }
        }
        else{
            ScreenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        }

    }

    public double getCohesion_multiplier() {
        return cohesion_multiplier;
    }

    public void setCohesion_multiplier(double cohesion_multiplier) {
        this.cohesion_multiplier = cohesion_multiplier;
    }

    public double getAlignment_multiplier() {
        return alignment_multiplier;
    }

    public void setAlignment_multiplier(double alignment_multiplier) {
        this.alignment_multiplier = alignment_multiplier;
    }

    public double getSeparation_multiplier() {
        return separation_multiplier;
    }

    public void setSeparation_multiplier(double separation_multiplier) {
        this.separation_multiplier = separation_multiplier;
    }

    public int getDraw_timestep() {
        return Draw_timestep;
    }

    public void setDraw_timestep(int draw_timestep) {
        Draw_timestep = draw_timestep;
    }

    public int getMax_FPS() {
        return Max_FPS;
    }

    public void setMax_FPS(int max_FPS) {
        Max_FPS = max_FPS;
    }

    public int getSimulation_timestep() {
        return Simulation_timestep;
    }

    public void setSimulation_timestep(int simulation_timestep) {
        Simulation_timestep = simulation_timestep;
    }

    public boolean isFlocking_Enabled() {
        return Flocking_Enabled;
    }

    public void setFlocking_Enabled(boolean flocking_Enabled) {
        Flocking_Enabled = flocking_Enabled;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}
