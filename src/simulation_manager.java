import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class simulation_manager implements ActionListener
{
    private double last_nano_Time = 0;
    private double new_nano_time = 0;

    //list containing all boids
    private List<Boid> Boid_List = new ArrayList<Boid>();

    private List<Drawable> Draw_List = new ArrayList<Drawable>();//list of stuff that has the Draw() function

    public List<Drawable> getDraw_List() {
        return Draw_List;
    }

    //TODO solve problem with getting first timestep
    public double elapsedTime()
    {
        new_nano_time = System.nanoTime();
        double elapsed_time = new_nano_time - last_nano_Time;
        last_nano_Time = new_nano_time;
        return elapsed_time * 0.000000001;//multiply by 1*10pow(-9) to return time in seconds
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
