import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Boid_Maths
{
    /**
     * private empty constructor to prevent this class from being instantiated. it would not make sense to instantiate one maths.
     * this class is just a collection of methods for the calculations required by the boid simulator. Having all the methods
     * (such as calculating seperation in one class makes the code more readable.
     */
    private Boid_Maths()//do not modify , its supposed to be empty and private
    {

    }

    /*
    these methods return the X and Y component of a vector in polar form
     */
    public static <T extends Number,T2 extends Number> cartesian_point Vector_Polar_to_Cartesian(T magnitude,T2 input_angle_in_rad){//methods have to be static to be used without an object existing
        double X;
        double Y;
        X = calculate_adjacent(input_angle_in_rad, magnitude);
        Y = calculate_opposite(input_angle_in_rad, magnitude);
        return new cartesian_point(X,Y);
    }
    public static cartesian_point Vector_Polar_to_Cartesian(polar_vector vector){//methods have to be static to be used without an object existing
        double X;
        double Y;
        X = calculate_adjacent(vector.getAngle_rad(), vector.getMagnitude());
        Y = calculate_opposite(vector.getAngle_rad(), vector.getMagnitude());
        return new cartesian_point(X,Y);
    }


    /*
    These methods use x and y components and return a vector
     */
    public static <T extends Number,T2 extends Number> polar_vector calculate_vector(T X , T2 Y)
    {
        double magnitude = calculate_magnitude(X,Y);
        double angle = calculate_Vector_angle(X,Y);
        return new polar_vector(magnitude,angle,true);
    }
    public static polar_vector calculate_vector(cartesian_point point)
    {
        double magnitude = calculate_magnitude(point);
        double angle = calculate_Vector_angle(point);
        return new polar_vector(magnitude,angle,true);
    }

    /**
     * calculates the adjacent and opposite component and returns them as a double
     * @param angle angle in radians
     * @param Hypotenuse magnitude in pixel
     * @param <T>
     * @param <T2>
     * @return
     */
    public static <T extends Number,T2 extends Number> double calculate_opposite(T angle , T2 Hypotenuse)
    {
        return Hypotenuse.doubleValue() * Math.sin(angle.doubleValue());
    }
    public static <T extends Number,T2 extends Number> double calculate_adjacent(T angle , T2 Hypotenuse)
    {
        return Hypotenuse.doubleValue() * Math.cos(angle.doubleValue());
    }

    /*
    returns the magnitude of a vector given the x and y components or a cartesian point
     */
    public static <T extends Number,T2 extends Number> double calculate_magnitude(T x , T2 y)
    {
        return Math.sqrt(Math.pow(x.doubleValue(),2)+Math.pow(y.doubleValue(),2));
    }
    public static <T extends Number,T2 extends Number> double calculate_magnitude(cartesian_point point)
    {
        return Math.sqrt(Math.pow(point.Get_X_double(),2)+Math.pow(point.Get_Y_double(),2));
    }

    /*
    returns the angle of a vector given the x and y components or a cartesian point
     */
    public static <T extends Number,T2 extends Number> double calculate_Vector_angle(T x , T2 y)
    {
        return find_anticlockwise_angle(x,y);
    }
    public static <T extends Number,T2 extends Number> double calculate_Vector_angle(cartesian_point point)
    {
        return find_anticlockwise_angle(point.Get_X_double(),point.Get_Y_double());
    }

    /**
     * this method checks in which quadrant the vector is and then changes the provided angle so that it is the angle the vector makes with the x axis
     * @param x component of vector
     * @param y component of vector
     * @param <T>
     * @param <T2>
     * @return returns the angle the vector makes with the x axis
     */
    public static <T extends Number,T2 extends Number> double find_anticlockwise_angle(T x,T y )
    {
        double X = x.doubleValue();
        double Y = y.doubleValue();
        double angle; //= Math.atan(Y/X);
        if(X == 0 && Y  == 0)
        {
            angle = 0;
        }
        else if(X > 0 && Y  > 0)
        {
            angle = Math.atan(Y/X);
        }
        else if(X == 0 && Y  > 0)
        {
            angle = Math.PI/2;
        }
        else if(X < 0 && Y  > 0)
        {
            angle = Math.PI + Math.atan(Y/X);
        }
        else if(X < 0 && Y  == 0)
        {
            angle = Math.PI;
        }
        else if(X < 0 && Y  < 0)
        {
            angle =Math.PI + Math.atan(Y/X);
        }
        else if(X == 0 && Y  < 0)
        {
            angle = (3*Math.PI)/2;
        }
        else if(X > 0 && Y  < 0)
        {
            angle =2*Math.PI + Math.atan(Y/X);
        }
        else // either x or y or both are 0 , so angle is also 0
        {
            return 0;
        }
        return angle;
    }

    /*
    these methods add vectors and return the resultant, you can either provide multiple vectors as arguments, or provide a vector arraylist
     */
    public static polar_vector vector_addition(polar_vector... vectors)
    {
        polar_vector resultant_vector = new polar_vector(0,0,false);

        for (polar_vector vector : vectors)
        {
            resultant_vector.setXcomponent(resultant_vector.getXcomponent() + vector.getXcomponent());
            resultant_vector.setYcomponent(resultant_vector.getYcomponent() + vector.getYcomponent());
        }
        return  resultant_vector;
    }

    public static polar_vector vector_addition(List<polar_vector> VectorList)
    {
        polar_vector resultant_vector = new polar_vector(0,0,false);

        for (polar_vector vector : VectorList)
        {
            resultant_vector.setXcomponent(resultant_vector.getXcomponent() + vector.getXcomponent());
            resultant_vector.setYcomponent(resultant_vector.getYcomponent() + vector.getYcomponent());
        }
        return  resultant_vector;
    }

    /*
    addition of cartesian points
     */
    public static cartesian_point point_addition(cartesian_point... points)
    {
        cartesian_point resultant_point = new cartesian_point(0,0);
        for (cartesian_point point:points)
        {
            resultant_point.setX_coordinate(point.Get_X_double()+resultant_point.Get_X_double());
            resultant_point.setY_coordinate(point.Get_Y_double()+resultant_point.Get_Y_double());
        }
        return resultant_point;
    }

    /*
    these methods find the average position of the provided positions.
     */
    public static cartesian_point Calculate_average_boid_position(List<BetterBoid> Boid_List)
    {
        cartesian_point average_point = new cartesian_point(0,0);
        for (BetterBoid boid:Boid_List)
        {
            average_point = point_addition(boid.getBoid_position(),average_point);
        }
        average_point.setX_coordinate(average_point.Get_X_double()/Boid_List.size());
        average_point.setY_coordinate(average_point.Get_Y_double()/Boid_List.size());

        return average_point;
    }

    /*
    find distance between 2 cartesian points
     */
    public static double Distance_between_points(cartesian_point point1 , cartesian_point point2)
    {
        double xdistance = 0;
        double ydistance = 0;
        double distance_between_points;
        xdistance = Math.abs(point1.Get_X_double() - point2.Get_X_double());
        ydistance = Math.abs(point1.Get_Y_double() - point2.Get_Y_double());
        distance_between_points = calculate_magnitude(xdistance,ydistance);
        return distance_between_points;
    }

    /*
    returns a cartesian point with random x and y components.
     */
    public static cartesian_point RandomPosition(int maxX,int maxY)
    {
        try {
            Random R = new Random(System.nanoTime());
            cartesian_point point = new cartesian_point(R.nextInt(maxX),R.nextInt(maxY));
            return point;
        } catch (Exception e) {
            return new cartesian_point(0,0);
        }
    }

    public  static <T extends Number> polar_vector RandomVector(T Max_magnitude )
    {
        Random R = new Random(System.nanoTime());
        polar_vector vector = new polar_vector(R.nextDouble()* Max_magnitude.doubleValue(),R.nextDouble()*2*Math.PI,true);
        return vector;
    }

    public static double RandomDouble()
    {
        Random R = new Random(System.nanoTime());

        return R.nextDouble();
    }

    /**
     * clones and returns the provided list. this is used to prevent ConcurrentModificationException
     * @param list
     * @param <T>
     * @return
     */
    public static<T> List<T> cloneList(List<T> list){
        List<T> clonedList = new ArrayList<>();
        clonedList.addAll(list);
        return clonedList;
    }

    /**
     * finds the angle between 2 boids
     * @param Refrence_boid the boid that is used as refrence to calculate the angle
     * @param other_boid
     * @return
     */
    public static double angleBetweenPoints(cartesian_point Ref_point,cartesian_point other_point){
        //get the other_boid position with respect to Refrence_boid position
        cartesian_point refPoint = calculatePositionRelativeToPoint(Ref_point,other_point);
        //create a vector
        polar_vector vector_between_Points = new polar_vector(refPoint.Get_X_double(),refPoint.Get_Y_double(),false);
        return vector_between_Points.getAngle_rad();
    }

    public static cartesian_point calculatePositionRelativeToPoint(cartesian_point RefrencePoint,cartesian_point otherPoint){
        double Xcomponent = otherPoint.Get_X_double() - RefrencePoint.Get_X_double();
        double Ycomponent = otherPoint.Get_Y_double() - RefrencePoint.Get_Y_double();
        cartesian_point RelativePosition = new cartesian_point(Xcomponent,Ycomponent);
        return RelativePosition;
    }
}
