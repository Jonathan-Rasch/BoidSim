import javax.swing.*;
import java.awt.*;

/**
 * Created by pc on 3/22/2015.
 */
public class Simulation_Panel extends JPanel
{
    private simulation_manager SimManager;
    public Simulation_Panel(simulation_manager SimManag)
    {
        super();
        SimManager = SimManag;//reference to simmanager. used to acess boid list nedded for update.
    }

    @Override
    /*
    every object that is supposed to be drawn in the sim panel implements the Drawable interface and hence has the Draw() function
     */
    public void paint(Graphics g)//paintcomponent does not seem to work, paint however does
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(Drawable D:SimManager.getDraw_List())
        {
            D.Draw(g2);
        }
        System.out.println("drew " + SimManager.getDraw_List().size());
    }
}
