import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class simulation_manager
{
    private double deltaT = 0;
    Settings SimSettings;


    //list containing all boids
    private List<BetterBoid> Boid_List = new ArrayList<>();

    private List<Drawable> Draw_List = new ArrayList<Drawable>();//list of stuff that has the Draw() function

    public simulation_manager(Settings setting)
    {
        SimSettings = setting;
        //TODO remove this test code
        int i = 200;
        while(i > 0)
        {
            i--;
            cartesian_point randomposition = Boid_Maths.RandomPosition(SimSettings.getScreenDimension().width,SimSettings.getScreenDimension().height);
            polar_vector randomvector = Boid_Maths.RandomVector(100);
            BetterBoid boid = new BetterBoid(this.SimSettings,randomposition,randomvector);
            Boid_List.add(boid);
        }

    }

    public void Update()
    {
        while(true)
        {
            Draw_List = new ArrayList<Drawable>();
            Draw_List.addAll(Boid_List);
            SimSettings.SetDrawList(Draw_List);
            for(BetterBoid boid:Boid_List)
            {
                boid.Update(10*Math.pow(10,-4),Boid_List);
            }
            for(BetterBoid boid:Boid_List)
            {
                boid.UpdatePositionAndVector();

            }
            try {
                Thread.sleep(0,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





    public List<Drawable> getDraw_List() {
        return Draw_List;
    }


}
