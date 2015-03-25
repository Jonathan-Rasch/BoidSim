import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class simulation_manager implements ActionListener
{
    private double deltaT = 0;
    Settings SimSettings;


    //list containing all boids
    private List<Boid> Boid_List = new ArrayList<Boid>();

    private List<Drawable> Draw_List = new ArrayList<Drawable>();//list of stuff that has the Draw() function

    public simulation_manager(Settings setting)
    {
        SimSettings = setting;
        //TODO remove this test code
        int i = 100;
        while(i > 0)
        {
            i--;
            Boid_List.add(new Boid(0,this.SimSettings));
        }

    }

    public void Update(double Time_in_s)
    {
        Draw_List.clear();
        Draw_List.addAll(Boid_List);
        for(Boid boid:Boid_List)
        {
            boid.Update(deltaT,Boid_List);
        }
        for(Boid boid:Boid_List)
        {
            boid.UpdateComplete();
        }

    }

    public List<Drawable> getDraw_List() {
        return Draw_List;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //getting the time that has passed. from ms to s
        deltaT = 0.001*(double)Integer.parseInt(e.getActionCommand());
        Update(deltaT);
    }
}
