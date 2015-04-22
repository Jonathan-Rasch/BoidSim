import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class simulation_manager
{
    private double deltaT = 0;
    private boolean ExitSimulation = false;
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

    public void Update() {
        while (!ExitSimulation) {

            if(SimSettings.LoadSimulation){
                SimSettings.LoadSimulation = false;
                LoadSimulation();
            }
            if(SimSettings.SaveSimulation){
                SimSettings.SaveSimulation = false;
                SaveSimulation();
            }
            if(SimSettings.ClearSimulation){
                SimSettings.ClearSimulation = false;
                Clear();
            }
            if (SimSettings.SimulationRunning) {
                Draw_List = new ArrayList<Drawable>();
                Draw_List.addAll(Boid_List);
                SimSettings.SetDrawList(Draw_List);
                for (BetterBoid boid : Boid_List) {
                    boid.Update(10 * Math.pow(10, -4), Boid_List);
                }
                for (BetterBoid boid : Boid_List) {
                    boid.UpdatePositionAndVector();
                }
            }
            try {
                Thread.sleep(0, 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Clear(){
        Boid_List.clear();
    }

    public void EXIT(){
        ExitSimulation = true;
    }

    public void LoadSimulation(){
        SimSettings.SimulationRunning = false;//Stopping simulation from running to prevent concurrent modification of list
        JFileChooser Chooser = new JFileChooser();
        Chooser.showOpenDialog(null);
        if(Chooser.getSelectedFile() != null ){
           try(BufferedReader filereader = Files.newBufferedReader(Paths.get(Chooser.getSelectedFile().getPath()), Charset.defaultCharset())){
               String Line = filereader.readLine();
               Boid_List.clear();//clear list so new boids can be added
               while(!Line.equals("END")){
                   if(Line.equals("SETTINGS:") && Line != null){
                       while(!Line.equals("END SETTINGS:") && Line != null){
                           Line = filereader.readLine();
                           if(Line.contains("BorderedSimulation[")){
                               Line = Line.replace("BorderedSimulation[","");
                               SimSettings.BorderedSimulation = Boolean.parseBoolean(Line);
                           }
                           if(Line.contains("Flocking_Enabled[")){
                               Line = Line.replace("Flocking_Enabled[","");
                               SimSettings.Flocking_Enabled = Boolean.parseBoolean(filereader.readLine());
                           }
                           if(Line.contains("Enforce_Minimum_Speed[")){
                               Line = Line.replace("Enforce_Minimum_Speed[","");
                               SimSettings.Enforce_Minimum_Speed = Boolean.parseBoolean(filereader.readLine());
                           }
                           if(Line.contains("Show_Boid_vector[")){
                               Line = Line.replace("Show_Boid_vector[","");
                               SimSettings.Show_Boid_vector = Boolean.parseBoolean(filereader.readLine());
                           }
                           if(Line.contains("Show_Boids_nearby[")){
                               Line = Line.replace("Show_Boids_nearby[","");
                               SimSettings.Show_Boids_nearby = Boolean.parseBoolean(filereader.readLine());
                           }
                           if(Line.contains("Show_Boid[")){
                               Line = Line.replace("Show_Boid[","");
                               SimSettings.Show_Boid = Boolean.parseBoolean(filereader.readLine());
                           }
                       }
                   }
                   if(Line.equals("BOID DATA:") && Line != null){//TODO why adding more then actual boids ?????
                       try {
                           while(!Line.equals("END BOID DATA")) {
                               Line = filereader.readLine();
                               if(Line == null){
                                   Line = "";
                               }
                               cartesian_point point = new cartesian_point(0, 0);
                               polar_vector vector = new polar_vector(0, 0, true);
                               if (Line.contains("X position[")) {
                                   Line = Line.replace("X position[", "");
                                   point.setX_coordinate(Double.parseDouble(Line));
                               }
                               if (Line.contains("Y position[")) {
                                   Line = Line.replace("Y position[", "");
                                   point.setY_coordinate(Double.parseDouble(Line));
                               }
                               if (Line.contains("Vector Magnitude[")) {
                                   Line = Line.replace("Vector Magnitude[", "");
                                   vector.setMagnitude(Double.parseDouble(Line));
                               }
                               if (Line.contains("Vector angle[")) {
                                   Line = Line.replace("Vector angle[", "");
                                   vector.setAngle_rad(Double.parseDouble(Line));
                               }
                               Boid_List.add(new BetterBoid(SimSettings, point, vector));
                           }
                       } catch (IOException e) {
                           e.printStackTrace();
                       } catch (NumberFormatException e) {
                           e.printStackTrace();
                       }

                   }
                   Line = filereader.readLine();
               }
               filereader.close();
           } catch (IOException e) {
               e.printStackTrace();
           }

        }
        else{
            JOptionPane.showMessageDialog(null,"Loading canceled");
        }

    }

    public void SaveSimulation(){
        SimSettings.SimulationRunning = false;//Stopping simulation from running to prevent concurrent modification of list
        JFileChooser Chooser = new JFileChooser();
        Chooser.showSaveDialog(null);
        if(Chooser.getSelectedFile() != null ){
            //TODO
            try {
                PrintWriter BoidSaveFileWriter = new PrintWriter(Chooser.getSelectedFile(),"UTF-8");
                BoidSaveFileWriter.println("SETTINGS:");
                BoidSaveFileWriter.println("BorderedSimulation[" + SimSettings.BorderedSimulation);
                BoidSaveFileWriter.println("Flocking_Enabled[" + SimSettings.Flocking_Enabled);
                BoidSaveFileWriter.println("Enforce_Minimum_Speed[" + SimSettings.Enforce_Minimum_Speed);
                BoidSaveFileWriter.println("Show_Boid_vector[" + SimSettings.Show_Boid_vector);
                BoidSaveFileWriter.println("Show_Boids_nearby[" + SimSettings.Show_Boids_nearby);
                BoidSaveFileWriter.println("Screen Dimension X[" + SimSettings.getScreenDimension().getWidth());
                BoidSaveFileWriter.println("Screen Dimension Y[" + SimSettings.getScreenDimension().getHeight());
                BoidSaveFileWriter.println("END SETTINGS:");
                //writing boid data
                for(BetterBoid Boid:Boid_List){
                    BoidSaveFileWriter.println("BOID DATA:");
                    //writing boid position
                    BoidSaveFileWriter.println("X position[" + Boid.getBoid_position().Get_X_double());
                    BoidSaveFileWriter.println("Y position[" + Boid.getBoid_position().Get_Y_double());
                    //writing boid vector
                    BoidSaveFileWriter.println("Vector Magnitude[" + Boid.getNew_boid_movement_Vector().getMagnitude());
                    BoidSaveFileWriter.println("Vector angle[" + Boid.getNew_boid_movement_Vector().getAngle_rad());
                    BoidSaveFileWriter.println("END BOID DATA");
                }
                BoidSaveFileWriter.println("END");
                BoidSaveFileWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"saving canceled");
        }
    }

    public List<Drawable> getDraw_List() {
        return Draw_List;
    }


}
