import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class simulation_manager implements ActionListener
{
    private double last_nano_Time = 0;
    private double new_nano_time = 0;

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
