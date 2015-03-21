

/**
 * stores a point as double values
 */
public class cartesian_point<T extends Number,T2 extends Number>
{
    private double X_coordinate;
    private double Y_coordinate;

    public cartesian_point(T x , T2 y)
    {
        this.X_coordinate = x.doubleValue();
        this.Y_coordinate = y.doubleValue();
    }

    public int Get_X_int()
    {
        return ((int) this.X_coordinate);
    }
    public double Get_X_double()
    {
        return this.X_coordinate;
    }
    public int Get_Y_int()
    {
        return ((int) this.Y_coordinate);
    }
    public double Get_Y_double()
    {
        return this.Y_coordinate;
    }

    public void setX_coordinate(double x_coordinate) {
        X_coordinate = x_coordinate;
    }

    public void setY_coordinate(double y_coordinate) {
        Y_coordinate = y_coordinate;
    }
}
