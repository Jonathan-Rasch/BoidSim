import java.util.ArrayList;
import java.util.List;

public class Boid {

    private cartesian_point boid_position;

    private polar_vector boid_vector;
    //TODO refactor "allignment" to "alignment" , spelling reasons
    private polar_vector allignment_vector;//the vector due to allignment
    private polar_vector seperation_vector;//the vector due to seperation
    private polar_vector cohesion_vector;//the vector due to cohesion
    private polar_vector obstacle_vector;// vector to avoid obstacles
    private polar_vector food_vector;//vector to stear towards food (optional)

    //the minimum desired seperation between the boids.
    private static double Desired_Seperation;//all boids share this value. it is set via settings in the gui or uses default values
    private static double Detection_distance;//the distance for which the boid detects other boids.

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
            allignment_vector = Boid_Maths.vector_addition(boid.getBoid_vector(),temp_allignment_vector);
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

    }
    private void calculate_cohesion()
    {

    }
    private void calculate_obstacle_vector()
    {

    }
    private void calculate_food_vector()
    {

    }
}
