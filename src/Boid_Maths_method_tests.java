/**
 * Created by pc on 3/23/2015.
 */
public class Boid_Maths_method_tests {

    public static void main(String[] args)
    {
        /*
        testing the calculate_opposite and calculate_adjacent
         */

        System.out.println("testing the calculate_opposite method");
        System.out.println("expected 1.74 : " + Boid_Maths.calculate_opposite(Math.PI/18,10));//expected 1.74
        System.out.println("expected 7.07 : " + Boid_Maths.calculate_opposite(Math.PI/4,10));//expected  7.07
        System.out.println("expected 7.99 : " + Boid_Maths.calculate_opposite((Math.PI*53)/180,10));//expected 7.99
        System.out.println("expected-3.09 : " + Boid_Maths.calculate_opposite(Math.PI/10,-10));//expected -3.09
        System.out.println("\n");

        System.out.println("testing the calculate_adjacent method");
        System.out.println("expected 9.85 : " + Boid_Maths.calculate_adjacent(Math.PI / 18, 10));//expected 9.85
        System.out.println("expected 7.07 : " + Boid_Maths.calculate_adjacent(Math.PI / 4, 10));//expected 7.07
        System.out.println("expected 6.01 : " + Boid_Maths.calculate_adjacent((Math.PI * 53) / 180, 10));//expected 6.01
        System.out.println("expected -9.85 : " + Boid_Maths.calculate_adjacent(Math.PI/10,-10));//expected -9.85
        System.out.println("\n");

        /*
        testing the Vector_Polar_to_Cartesian function
         */
        System.out.println("testing the Vector_Polar_to_Cartesian method");
        //expected x = 7.07 y = 7.07
        cartesian_point point = Boid_Maths.Vector_Polar_to_Cartesian(10,Math.PI/4);
        System.out.println("X [expected 7.07]: "+ point.Get_X_double() + " Y [expected 7.07]: " + point.Get_Y_double());

        //expected x = 0 y = 9.8
        point = Boid_Maths.Vector_Polar_to_Cartesian(9.8,Math.PI/2);
        System.out.println("X [expected 0.00]: "+ point.Get_X_double() + " Y [expected 9.8]: " + point.Get_Y_double());

        //expected x = 0.92 y = -0.92
        point = Boid_Maths.Vector_Polar_to_Cartesian(-1.3,(3*Math.PI)/4);
        System.out.println("X [expected 0.92]: "+ point.Get_X_double() + " Y [expected -0.92]: " + point.Get_Y_double());
        System.out.println("\n");

        /*
        calculate_magnitude method test
         */
        System.out.println("testing the calculate_magnitude method");
        System.out.println("[expected 14.14]" + Boid_Maths.calculate_magnitude(10,10));
        System.out.println("[expected 11.34]" + Boid_Maths.calculate_magnitude(5.7,9.8));
        System.out.println("[expected 03.05]" + Boid_Maths.calculate_magnitude(0.55,-3));
        System.out.println("\n");

        /*
        calculate_magnitude method test
         */
        System.out.println("testing the find_anticlockwise_angle method");
        System.out.println("[expected 0.00]" + Boid_Maths.find_anticlockwise_angle(0,0));
        System.out.println("[expected 0.78]" + Boid_Maths.find_anticlockwise_angle(1,1));
        System.out.println("[expected 1.57]" + Boid_Maths.find_anticlockwise_angle(0,1));
        System.out.println("[expected 2.35]" + Boid_Maths.find_anticlockwise_angle(-1,1));
        System.out.println("[expected 3.14]" + Boid_Maths.find_anticlockwise_angle(-1,0));
        System.out.println("[expected 3.92]" + Boid_Maths.find_anticlockwise_angle(-1,-1));
        System.out.println("[expected 4.71]" + Boid_Maths.find_anticlockwise_angle(0,-1));
        System.out.println("[expected 5.49]" + Boid_Maths.find_anticlockwise_angle(1,-1));
        System.out.println("\n");

    }
}
