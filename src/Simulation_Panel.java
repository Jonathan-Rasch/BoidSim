import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;
import java.util.List;


public class Simulation_Panel implements ActionListener
{
    private JPanel panel;


    public JPanel GetJPanel()
    {
        return panel;
    }


    private Settings SimSettings;
    public Simulation_Panel(Settings set)
    {
        super();
        SimSettings = set;//reference to simmanager. used to acess boid list nedded for update.
        panel = new JPanel(){
            @Override
            //every object that is supposed to be drawn in the sim panel implements the Drawable interface and hence has the Draw() function
            public void paint(Graphics g)//paintcomponent does not seem to work, paint however does
            {

                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                this.setBackground(Color.black);//TODO make variable color
                List<Drawable> drawList = SimSettings.GetDrawList();
                for(Drawable D:drawList)
                {
                    D.Draw(g2);
                }

            }
        };
        //adding listener to detect if the components Dimensions change
        panel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                SimSettings.setScreenDimension(e.getComponent().getSize());
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }



    //called when draw timer is over in the gui.
    @Override
    public void actionPerformed(ActionEvent e) {
       panel.repaint();
    }
}
