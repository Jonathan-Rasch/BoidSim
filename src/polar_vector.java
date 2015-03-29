

/**
 * used so that some functions can return multiple values
 */
public class polar_vector<T extends Number,T2 extends Number>
{
    private double magnitude;
    private double angle_rad;
    private double Xcomponent;
    private double Ycomponent;

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        Update_cartesian_data();
    }

    public double getAngle_rad() {
        return angle_rad;
    }

    public void setAngle_rad(double angle_rad) {
        this.angle_rad = angle_rad;
        Update_cartesian_data();
    }

    public double getYcomponent() {
        return Ycomponent;
    }

    public void setYcomponent(double ycomponent) {
        Ycomponent = ycomponent;
        Update_polar_data();
    }

    public double getXcomponent() {
        return Xcomponent;
    }

    public void setXcomponent(double xcomponent) {
        Xcomponent = xcomponent;
        Update_polar_data();
    }

    /*
    calculates a new magnitude and angle depending on the new x and or y data
     */
    private void Update_polar_data()
    {
        magnitude = Boid_Maths.calculate_magnitude(Xcomponent,Ycomponent);
        angle_rad = Boid_Maths.find_anticlockwise_angle(Xcomponent,Ycomponent);
        //error checking
        check_for_errors();
    }

    //makes sure the vector has values that are within the allowed limits
    private void check_for_errors() {
        boolean Xfinite = Double.isFinite(Xcomponent);
        boolean Yfinite = Double.isFinite(Ycomponent);
        boolean XNaN = Double.isNaN(Xcomponent);
        boolean YNaN = Double.isNaN(Ycomponent);
        if(!Xfinite || XNaN)//if X is infinite or NaN reset X
        {
            Xcomponent = 0;
            Update_polar_data();
        }
        if(!Yfinite || YNaN)//if Y is infinite or NaN reset Y
        {
            Ycomponent = 0;
            Update_polar_data();
        }
        //adjust angle so that it is not bigger than 2PI or smaller than 0PI
        if (angle_rad > 2*Math.PI || angle_rad < 0)
        {
            while(angle_rad > 2*Math.PI)
            {
                angle_rad -= 2*Math.PI;
            }
            while(angle_rad < 0)
            {
                angle_rad += 2*Math.PI;
            }
            Update_cartesian_data();
        }

    }

    private void Update_cartesian_data()
    {
        Xcomponent = Boid_Maths.calculate_adjacent(angle_rad,magnitude);
        Ycomponent = Boid_Maths.calculate_opposite(angle_rad,magnitude);
        check_for_errors();
    }


    /**
     * this is the constructor of the polar_Vector class. this class allows you to create a Boid_Vector object by either
     * passing the data in polar or in cartesian form to the constructor.if an exception is thrown in the constructor it will
     * create a vector with random magnitude and angle.
     * @param magnitude_or_Xcomponent the magnitude of the vector if polar_form = TRUE , or the x component of the vector if polar_form = FALSE
     * @param angleRad_or_Ycomponent the agle in radians if polar_form = TRUE , or the y component of the vector if polar_form = FALSE
     * @param polar_form boolean determining in what form the input parameters are. set to true if you are inputing the data for a vector in polar form ( magnitude and angle ) or to false if you are inputing x and y components of the vector.
     */
    public <T extends Number  ,T2 extends Number> polar_vector(T magnitude_or_Xcomponent,T2 angleRad_or_Ycomponent,boolean polar_form)
    {

        try {
            if(polar_form)
            {
                magnitude = magnitude_or_Xcomponent.doubleValue();
                angle_rad = angleRad_or_Ycomponent.doubleValue();
                Xcomponent = Boid_Maths.calculate_adjacent(angleRad_or_Ycomponent,magnitude_or_Xcomponent);
                Ycomponent = Boid_Maths.calculate_opposite(angleRad_or_Ycomponent,magnitude_or_Xcomponent);
            }
            else//input is in cartesian form.
            {
                Xcomponent = magnitude_or_Xcomponent.doubleValue();
                Ycomponent = angleRad_or_Ycomponent.doubleValue();
                magnitude = Boid_Maths.calculate_magnitude(magnitude_or_Xcomponent,angleRad_or_Ycomponent);
                angle_rad = Boid_Maths.calculate_Vector_angle(magnitude_or_Xcomponent,angleRad_or_Ycomponent);
            }
            check_for_errors();
        } catch (Exception e) {
            e.printStackTrace();
            check_for_errors();
        }
    }

    //function which inverts this vector
    public void Invert_Vector()
    {
        //inverting x and y
        this.setXcomponent(-this.getXcomponent());
        this.setYcomponent(-this.getYcomponent());
        //recalculating magnitude and angle
        this.magnitude = Boid_Maths.calculate_magnitude(this.getXcomponent(),this.getYcomponent());
        this.angle_rad = Boid_Maths.calculate_Vector_angle(this.getXcomponent(),this.getYcomponent());
    }

    /**
     * returns a polar vector that is exactly the same as this vector, but a different object. this is needed in the
     * getters and setters when no refrence to this vector should be passed.
     * @return cloned vector
     */
    public polar_vector cloneVector(){
        polar_vector clonedVector = new polar_vector(this.getXcomponent(),this.getYcomponent(),false);
        return  clonedVector;
    }
}
