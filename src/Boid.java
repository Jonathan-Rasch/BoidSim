import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class Boid implements Drawable{

    /*
    the boid has a position and a vector which are used to determine the behaviour of the sourounding boids . the update functions use those.
    after the update function is done the boid has a new position and a new vector which are used to alter the position of the boid in the next frame.
     */
    private cartesian_point boid_position;
    private cartesian_point new_position;//the next position of the boid after the update. this is so that all boids change position simultaneously

    private polar_vector boid_vector;
    private polar_vector new_boid_vector;//used to find the new position.

    //TODO refactor "allignment" to "alignment" , spelling reasons
    private polar_vector allignment_vector = new polar_vector(0,0,true);//the vector due to allignment
    private polar_vector seperation_vector = new polar_vector(0,0,true);//the vector due to seperation
    private polar_vector cohesion_vector = new polar_vector(0,0,true);//the vector due to cohesion
    private polar_vector obstacle_vector = new polar_vector(0,0,true);// vector to avoid obstacles and simulation borders
    private polar_vector food_vector = new polar_vector(0,0,true);//vector to stear towards food (optional)

    //the minimum desired seperation between the boids.
    private static double Desired_Seperation = 10;//all boids share this value. it is set via settings in the gui or uses default values
    private static double Detection_distance = 50;//the distance for which the boid detects other boids.
    private static double Boid_radius = 5 ; // the radius of the circle representing the boid.
    private static Settings SimSettings;

    //TODO write a function to update statics

    List<Boid> Detected_List = new ArrayList<Boid>();
    //TODO obstacle list and functions.

    public cartesian_point getBoid_position() {
        return boid_position;
    }
    public polar_vector getBoid_vector() {
        return boid_vector;
    }

    /*
    allignment is when the boid takes the average vector of all the boids in its detection radius and adds it to its own vector.
     */
    private void calculate_allignment()
    {
        //reset alignment vector so that if boid list is empty, alignment has no effect.
        allignment_vector = new polar_vector(0,0,true);
        //add vectors of all nearby boids
        for(Boid boid: Detected_List)
        {
            allignment_vector = Boid_Maths.vector_addition(boid.getBoid_vector(),allignment_vector);
        }
        //divide resultant vector by the number of boids to obtain the average vector
        //TODO make this a method in Boid_Maths
        allignment_vector.setXcomponent(allignment_vector.getXcomponent() / Detected_List.size());
        allignment_vector.setYcomponent(allignment_vector.getYcomponent()/ Detected_List.size());
        //add the resulting vector
    }

    /*
    add a vector to the boids vector which points away from boids that are too close.
     */
    private void calculate_seperation()
    {

        //calculating the numerator of the equation used to calculate the separation vector, same for all boids
        this.seperation_vector = new polar_vector(0,0,true);//resetting separation vector
        double numerator = 2*Boid_radius + Desired_Seperation;
        for(Boid boid:this.Detected_List)
        {
            //calculate separation between this boid and the current boid ( denominator of equation ):
            double actual_separation = Boid_Maths.Distance_between_points(this.boid_position,boid.boid_position);

            //TODO make this a Boid_Maths method (find angle between points )
            /*
            to find the angle of boid relative to this boid i use this boid as a reference. I archive this by substracting the position of this.boid from boid.
            this will give me the x and y difference between the boids. i can then use these to calculate the angle.the angle will be used later.
             */
            double boidXseparation = boid.getBoid_position().Get_X_double() - this.getBoid_position().Get_X_double();
            double boidYseparation = boid.getBoid_position().Get_Y_double() - this.getBoid_position().Get_Y_double();
            double angle_between_boids = Boid_Maths.find_anticlockwise_angle(boidXseparation,boidYseparation);
            //calculating the magnitude of the separation vector.
            /*
            numerator contains the desired seperation between boids. if actual separation < numerator , the fraction will be >> 1
            this is further amplified by raising the fraction to the third power and taking the exponential of that.
            this results in a larger magnitude the closer the boids get.
             */
            double seperation_magnitude = Math.pow(numerator / actual_separation, 5);
            //using the magnitude and angle to create a vector
            polar_vector Temp_seperation_vector = new polar_vector(seperation_magnitude,angle_between_boids,true);
            //at the moment the temp seperation vector is pointing towards the other boid, so i have to invert it,
            Temp_seperation_vector.Invert_Vector();
            //Now i can add it to the separation vector
            this.seperation_vector = Boid_Maths.vector_addition(seperation_vector,Temp_seperation_vector);
        }
    }

    private void calculate_cohesion()
    {
        cohesion_vector = new polar_vector(0,0,true);//reset  cohesion vector
        //find the average position of nearby boids
        cartesian_point average_position = Boid_Maths.Calculate_average_boid_position(Detected_List);
        //find the distance between this boid and the average position
        double distance = Boid_Maths.Distance_between_points(this.boid_position,average_position);
        //TODO (find angle between points )
        double angle_between_points = Boid_Maths.find_anticlockwise_angle(average_position.Get_X_double() - this.boid_position.Get_X_double(),average_position.Get_Y_double()-this.boid_position.Get_Y_double());
        //create vector
        cohesion_vector = new polar_vector(distance,angle_between_points,true);
    }

    private void calculate_obstacle_vector()
    {

    }

    private void calculate_food_vector()
    {

    }

    @Override
    public void Draw(Graphics2D g) {

        //<editor-fold desc="Instantiate variables">
        int Xpos = this.getBoid_position().Get_X_int();
        int Ypos = this.getBoid_position().Get_Y_int();
        /*
        this draws an oval which fits into a rectangle starting at pos x,y with side lengths width and heigh.
        this means some adjustment has to be made so it is centred on the boid position
         */
        Ellipse2D.Double circle = new Ellipse2D.Double(boid_position.Get_X_int() - (int)Boid_radius,boid_position.Get_Y_int()- (int)Boid_radius,2*(int)Boid_radius,2*(int)Boid_radius);
        //</editor-fold>

        //<editor-fold desc="Drawing the boid and vectors">
        //drawing the boid
        if(SimSettings.Show_Boid)
        {
            if(this.Detected_List.size() == 0){
                g.setColor(Color.red);
            }
            else{
                g.setColor(Color.green);
            }
            g.fill(circle);
        }
        //drawing the boid vector
        if(SimSettings.Show_Boid_vector) {
            //boid vector end point pos
            int bvecX = Xpos + (int)this.getBoid_vector().getXcomponent();
            int bvecY = Ypos + (int)this.getBoid_vector().getYcomponent();
            g.setColor(Color.red);
            g.drawLine(Xpos,Ypos,bvecX,bvecY);
        }
        if(SimSettings.Show_cohesion_vector) {
            //boid cohesion vector end point pos;
            int CvecX = Xpos + (int)this.cohesion_vector.getXcomponent();
            int CvecY = Ypos + (int)this.cohesion_vector.getYcomponent();
            //draw cohesion vector
            g.setColor(Color.blue);
            g.drawLine(Xpos,Ypos,CvecX,CvecY);
        }
        if(SimSettings.Show_allignment_vector) {
            //boid allignment vector end point pos
            int AvecX = Xpos + (int)this.allignment_vector.getXcomponent();
            int AvecY = Ypos + (int)this.allignment_vector.getYcomponent();
            //draw allignment vector
            g.setColor(Color.green);
            g.drawLine(Xpos,Ypos,AvecX,AvecY);
        }
        if(SimSettings.Show_seperation_vector) {
            //boid sparation vector end point pos
            int SvecX = Xpos + (int)this.seperation_vector.getXcomponent();
            int SvecY = Ypos + (int)this.seperation_vector.getYcomponent();
            //draw Separation vector
            g.setColor(Color.black);
            g.drawLine(Xpos,Ypos,SvecX,SvecY);
        }
        if(SimSettings.Show_Boids_nearby) {
            int xposition = this.getBoid_position().Get_X_int();
            int yposition = this.getBoid_position().Get_Y_int();
            g.setColor(Color.orange);
            for(Boid Boidnearby: Detected_List)
            {
                g.drawLine(xposition,yposition,Boidnearby.getBoid_position().Get_X_int(),Boidnearby.getBoid_position().Get_Y_int());
            }
        }
        if(SimSettings.Show_Detection_circle){
            Ellipse2D.Double Detection_circle = new Ellipse2D.Double(boid_position.Get_X_int() - this.Detection_distance,boid_position.Get_Y_int()- this.Detection_distance,2*this.Detection_distance,2*this.Detection_distance);
            g.setColor(Color.black);
            g.draw(Detection_circle);
        }
        //</editor-fold>

    }

    //update the boid
    public void Update(double deltaT , List<Boid> Boid_list) {

        //Update Boid values: e.g. detection distance
        this.Detection_distance = SimSettings.getDetection_Distance();

        //<editor-fold desc="Check which boids are in the detection region and ad them to Detected_List">
        this.Detected_List.clear();
        for(Boid boid1:Boid_list)
        {
           //check which boids are in the detection distance
           double distance_between_boids = Boid_Maths.Distance_between_points(this.boid_position,boid1.getBoid_position());
           if(distance_between_boids < this.Detection_distance)
           {

               //preventing the boid adding itself to the boid_nearby list
               if(boid1 != this)
               {
                   //TODO make math function BROKEN
                   double Xdistance = this.getBoid_position().Get_X_double() - boid1.getBoid_position().Get_X_double();
                   double Ydistance = this.getBoid_position().Get_Y_double() - boid1.getBoid_position().Get_Y_double();
                   polar_vector vector_between_points = new polar_vector(Xdistance,Ydistance,false);
                   vector_between_points.Invert_Vector();
                   double angle_between_boids = Math.abs(this.boid_vector.getAngle_rad() - vector_between_points.getAngle_rad() );
                   if(angle_between_boids <= 2*Math.PI*((double)SimSettings.getDetectionAngle()/100))//TODO something is wrong with angle between boids
                   {
                       Detected_List.add(boid1);
                   }

               }
           }
        }
        //</editor-fold>

        //<editor-fold desc="Vector calculations">
        //do vector calculations

        if(!SimSettings.Flocking_Enabled)
        {
            cohesion_vector = new polar_vector(0,0,false);
            allignment_vector = new polar_vector(0,0,false);
        }
        else
        {
            calculate_allignment();
            calculate_cohesion();
        }
        calculate_seperation();
        //debuff vectors;
        cohesion_vector = new polar_vector(cohesion_vector.getXcomponent()*SimSettings.getCohesion_multiplier()/this.Detected_List.size(),cohesion_vector.getYcomponent()*SimSettings.getCohesion_multiplier()/this.Detected_List.size(),false);//TODO add /this.Detected_List.size()
        allignment_vector = new polar_vector(allignment_vector.getXcomponent()*SimSettings.getAlignment_multiplier(),allignment_vector.getYcomponent()*SimSettings.getAlignment_multiplier(),false);
        //find the new boid vector
        polar_vector TempVec = new polar_vector(boid_vector.getXcomponent(),boid_vector.getYcomponent(),false);
        new_boid_vector = Boid_Maths.vector_addition(TempVec,seperation_vector,cohesion_vector,allignment_vector,obstacle_vector);
        //if the boid is not affected by other vectors it should maintain its velocity
        if (new_boid_vector.getMagnitude() > 200){
            new_boid_vector.setMagnitude(200);
        }
        //randomize heading
        if (Detected_List.size() == 0 && Boid_Maths.RandomDouble()>0.51){
            new_boid_vector.setAngle_rad(new_boid_vector.getAngle_rad()-0.01);
        }
        else if(Detected_List.size() == 0 && Boid_Maths.RandomDouble()<0.49){
            new_boid_vector.setAngle_rad(new_boid_vector.getAngle_rad()+0.01);
        }
        //</editor-fold>

        //<editor-fold desc="set new boid position and ensure its not outside the screen">

        double new_X = new_boid_vector.getXcomponent()*deltaT + this.boid_position.Get_X_double();
        if (new_X > this.SimSettings.getScreenDimension().width)
        {
            new_X = new_X - this.SimSettings.getScreenDimension().width;
        }
        else if (new_X < 0)
        {
            new_X = this.SimSettings.getScreenDimension().width + new_X;
        }
        double new_Y = new_boid_vector.getYcomponent()*deltaT + this.boid_position.Get_Y_double();
        if (new_Y > this.SimSettings.getScreenDimension().height)
        {
            new_Y = new_Y - this.SimSettings.getScreenDimension().height;
        }
        else if (new_Y < 0)
        {
            new_Y = this.SimSettings.getScreenDimension().height + new_Y;
        }
        new_position = new cartesian_point(new_X,new_Y);
        //</editor-fold>

    }


    //once every boid has ran through update, the boids assume their new positions and vectors
    public void UpdateComplete()
    {
        this.boid_position = this.new_position;
        this.boid_vector = this.new_boid_vector;
    }

    //constructor creating boid with random position and vector
    public Boid(int maxVector_magnitude,Settings SimSett)
    {
        this.SimSettings = SimSett;
        boid_position = Boid_Maths.RandomPosition(SimSettings.getScreenDimension().width,SimSettings.getScreenDimension().height);//TODO dimensions
        boid_vector = Boid_Maths.RandomVector(maxVector_magnitude);
    }
}
