import java.awt.*;
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
    private polar_vector allignment_vector;//the vector due to allignment
    private polar_vector seperation_vector;//the vector due to seperation
    private polar_vector cohesion_vector;//the vector due to cohesion
    private polar_vector obstacle_vector;// vector to avoid obstacles
    private polar_vector food_vector;//vector to stear towards food (optional)

    //the minimum desired seperation between the boids.
    private static double Desired_Seperation = 10;//all boids share this value. it is set via settings in the gui or uses default values
    private static double Detection_distance = 0;//the distance for which the boid detects other boids.
    private static double Boid_radius = 20 ; // the radius of the circle representing the boid.
    //TODO write a function to update statics

    List<Boid> Boidnearby_List = new ArrayList<Boid>();
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
        for(Boid boid:Boidnearby_List)
        {
            allignment_vector = Boid_Maths.vector_addition(boid.getBoid_vector(),allignment_vector);
        }
        //divide resultant vector by the number of boids to obtain the average vector
        //TODO make this a method in Boid_Maths
        allignment_vector.setXcomponent(allignment_vector.getXcomponent()/Boidnearby_List.size());
        allignment_vector.setYcomponent(allignment_vector.getYcomponent()/Boidnearby_List.size());
        //add the resulting vector
    }

    /*
    add a vector to the boids vector which points away from boids that are too close.
     */
    private void calculate_seperation()
    {

        //calculating the numerator of the equation used to calculate the separation vector, same for all boids
        seperation_vector = new polar_vector(0,0,true);//resetting separation vector
        double numerator = 2*Boid_radius + Desired_Seperation;
        for(Boid boid:Boidnearby_List)
        {
            //calculate separation between this boid and the current boid ( denominator of equation ):
            double actual_separation = Boid_Maths.Distance_between_points(this.boid_position,boid.boid_position);
            //TODO make this a Boid_Maths method (find angle between points )
            /*
            to find the angle of boid relative to this boid i use this boid as a reference. I archive this by substracting the position of this.boid from boid.
            this will give me the x and y difference between the boids. i can then use these to calculate the angle.the angle will be used later.
             */
            double angle_between_boids = Boid_Maths.find_anticlockwise_angle(boid.getBoid_position().Get_X_double() - this.getBoid_position().Get_X_double(),boid.getBoid_position().Get_Y_double() - this.getBoid_position().Get_Y_double());
            //calculating the magnitude of the separation vector.
            /*
            numerator contains the desired seperation between boids. if actual separation < numerator , the fraction will be >> 1
            this is further amplified by raising the fraction to the third power and taking the exponential of that.
            this results in a larger magnitude the closer the boids get.
             */
            double seperation_magnitude = Math.exp(Math.pow(numerator/actual_separation,3));
            //using the magnitude and angle to create a vector
            polar_vector Temp_seperation_vector = new polar_vector(seperation_magnitude,angle_between_boids,true);
            //at the moment the temp seperation vector is pointing towards the other boid, so i have to invert it,
            Temp_seperation_vector.Invert_Vector();
            //Now i can add it to the separation vector
            seperation_vector = Boid_Maths.vector_addition(seperation_vector,Temp_seperation_vector);
        }
    }

    private void calculate_cohesion()
    {
        cohesion_vector = new polar_vector(0,0,true);//reset  cohesion vector
        //find the average position of nearby boids
        cartesian_point average_position = Boid_Maths.Calculate_average_boid_position(Boidnearby_List);
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
        g.drawOval(boid_position.Get_X_int(),boid_position.Get_Y_int(),(int)Boid_radius,(int)Boid_radius);
    }

    //update the boid
    public void Update(double deltaT , List<Boid> Boid_list)
    {
       for(Boid boid1:Boid_list)
       {
           //check which boids are in the detection distance
           //DEBUG
           double i = Boid_Maths.Distance_between_points(this.boid_position,boid1.getBoid_position());
           //END DEBUG
           if(i <= Detection_distance);
           {
               Boidnearby_List.add(boid1);
           }
       }
        //do vector calculations
        calculate_allignment();
        calculate_seperation();
        calculate_cohesion();
        //find the new boid vector
        new_boid_vector = Boid_Maths.vector_addition(seperation_vector,cohesion_vector,allignment_vector);
        //find the new boid position
        double new_X = new_boid_vector.getXcomponent()*deltaT + this.boid_position.Get_X_double();
        double new_Y = new_boid_vector.getYcomponent()*deltaT + this.boid_position.Get_Y_double();
        new_position = new cartesian_point(new_X,new_Y);
    }
    //once every boid has ran through update, the boids assume their new positions and vectors
    public void UpdateComplete()
    {
        this.boid_position = this.new_position;
        this.boid_vector = this.new_boid_vector;
    }

    //constructor creating boid with random position and vector
    public Boid(int maxVector_magnitude,int detection_distance)
    {
        boid_position = Boid_Maths.RandomPosition(1280,720);
        boid_vector = Boid_Maths.RandomVector(maxVector_magnitude);
    }
}
