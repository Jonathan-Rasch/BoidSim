import java.util.ArrayList;
import java.util.List;

public class Boid {

    private cartesian_point boid_position;

    private polar_vector boid_vector;
    private polar_vector allignment_vector;//the vector due to allignment
    private polar_vector seperation_vector;//the vector due to seperation
    private polar_vector cohesion_vector;//the vector due to cohesion
    private polar_vector obstacle_vector;// vector to avoid obstacles
    private polar_vector food_vector;//vector to stear towards food (optional)

    List<Boid> Boidnearby_List = new ArrayList<Boid>();
    //TODO obstacle list and functions.

    public cartesian_point getBoid_position() {
        return boid_position;
    }
    public polar_vector getBoid_vector() {
        return boid_vector;
    }

    private void calculate_allignment()
    {

    }
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
