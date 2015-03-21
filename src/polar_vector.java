

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
    }

    public double getAngle_rad() {
        return angle_rad;
    }

    public void setAngle_rad(double angle_rad) {
        this.angle_rad = angle_rad;
    }

    public double getYcomponent() {
        return Ycomponent;
    }

    public void setYcomponent(double ycomponent) {
        Ycomponent = ycomponent;
    }

    public double getXcomponent() {
        return Xcomponent;
    }

    public void setXcomponent(double xcomponent) {
        Xcomponent = xcomponent;
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
                Ycomponent = Boid_Maths.calculate_opposite(angleRad_or_Ycomponent,magnitude_or_Xcomponent);//TODO check if this works
            }
            else//input is in cartesian form.
            {
                Xcomponent = magnitude_or_Xcomponent.doubleValue();
                Ycomponent = angleRad_or_Ycomponent.doubleValue();
                magnitude = Boid_Maths.calculate_magnitude(magnitude_or_Xcomponent,angleRad_or_Ycomponent);
                angle_rad = Boid_Maths.calculate_Vector_angle(magnitude_or_Xcomponent,angleRad_or_Ycomponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO call random vector constructor
            //TODO print error to log file

        }
    }
}
