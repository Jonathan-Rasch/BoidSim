import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class simulation_manager implements ActionListener
{
    private double start_nano_Time = 0;
    private double new_nano_time = 0;

    //list containing all boids
    private List<Boid> Boid_List = new ArrayList<Boid>();

    private List<Drawable> Draw_List = new ArrayList<Drawable>();//list of stuff that has the Draw() function

    public simulation_manager()
    {

    }

    public void UpdateLoop()
    {
        elapsedTimeStart();
    }

    public List<Drawable> getDraw_List() {
        return Draw_List;
    }

    //TODO solve problem with getting first timestep

    /**
     * the elapsedTimeStart() and elapsedTimeStop() enable me to tell the time that has passed between the elapsedTimeStart() and
     * the elapsedTimeStop() call. it is important that timestart is called before stop to ensure correct results.
     */
    private void elapsedTimeStart()
    {
        start_nano_Time = System.nanoTime();
    }
    private double elapsedTimeStop()
    {
        new_nano_time = System.nanoTime();
        double elapsed_time = new_nano_time - start_nano_Time;
        return elapsed_time * 0.000000001;//multiply by 1*10pow(-9) to return time in seconds
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
