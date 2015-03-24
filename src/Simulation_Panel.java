import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Simulation_Panel implements ActionListener
{

    private JPanel panel = new JPanel(){
        @Override
        //every object that is supposed to be drawn in the sim panel implements the Drawable interface and hence has the Draw() function
        public void paint(Graphics g)//paintcomponent does not seem to work, paint however does
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            this.setBackground(Color.white);
            for(Drawable D:SimManager.getDraw_List())
            {
                D.Draw(g2);
            }
        }


    };
    public JPanel GetJPanel()
    {
        return panel;
    }


    private simulation_manager SimManager;
    public Simulation_Panel(simulation_manager SimManag)
    {
        super();
        SimManager = SimManag;//reference to simmanager. used to acess boid list nedded for update.
    }




    @Override
    public void actionPerformed(ActionEvent e) {
       panel.repaint();
    }
}
