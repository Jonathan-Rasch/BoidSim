import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings implements ActionListener {

    //setting values with default values
    private Dimension ScreenDimension;

    public Dimension getScreenDimension() {
        return ScreenDimension;
    }

    private int Simulation_timestep = 1; //how long in ms until the simulation is updated 1ms = 1000 updates/sec
    private int Max_FPS = 30;//used to calculate draw delay ( draw_timestep = (1/Max_FPS )*1000
    private int Draw_timestep = 33;//delay between repaint calls

    private double cohesion_multiplier = 1;//by what value the cohesion vector is multiplied
    private double alignment_multiplier = 1;//by what value the alignment vector is multiplied
    private double separation_multiplier = 1;//by what value the alignment vector is multiplied

    public boolean Flocking_Enabled = false;
    public boolean Enforce_Minimum_Speed = true;
    public boolean Show_Boid_vector = true;
    public boolean Show_cohesion_vector = false;
    public boolean Show_allignment_vector = false;
    public boolean Show_seperation_vector = false;
    public boolean Show_Boids_nearby = true ;
    public boolean Show_Detection_circle = false;

    public Settings()
    {
        int choice = JOptionPane.showConfirmDialog(null,"would you like to use the whole screen for the simulation ?","Sim Window Size",JOptionPane.YES_NO_CANCEL_OPTION);
        if(choice == JOptionPane.YES_OPTION)
        {
            ScreenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        }
        else if (choice == JOptionPane.NO_OPTION)
        {
            //TODO finish start stuff
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