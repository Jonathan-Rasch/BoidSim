import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

/**
 * This class is the same as the Boid class but with cleaner and more efficient code
 */
public class BetterBoid implements Drawable {

    //<editor-fold desc="boid variable instantiations and constructors">

    //<editor-fold desc="boid_position instantiation and getter , setter methods">
    private cartesian_point boid_position = new cartesian_point(0,0);
    private cartesian_point new_boid_position = new cartesian_point(0,0);
    public cartesian_point getBoid_position() {
        return this.boid_position.clonePoint();
    }
    public void setBoid_position(cartesian_point new_boid_position) {
        this.boid_position = new_boid_position.clonePoint();
    }
    public cartesian_point getNew_boid_position() {
        return new_boid_position.clonePoint();
    }
    public void setNew_boid_position(cartesian_point new_boid_position) {
        this.new_boid_position = new_boid_position.clonePoint();
    }
    //</editor-fold>

    //<editor-fold desc="boid_movement_Vector instantiation and getter , setter methods">
    private polar_vector boid_movement_Vector = new polar_vector(0,0,true);
    private polar_vector new_boid_movement_Vector = new polar_vector(0,0,true);
    public polar_vector getNew_boid_movement_Vector() {
        return new_boid_movement_Vector.cloneVector();
    }
    public void setNew_boid_movement_Vector(polar_vector new_boid_movement_Vector) {
        this.new_boid_movement_Vector = new_boid_movement_Vector.cloneVector();
    }
    public polar_vector getBoid_movement_Vector() {
        return this.boid_movement_Vector.cloneVector();
    }
    public void setBoid_movement_Vector(polar_vector new_boid_movement_Vector) {
        //cloning parameter
        this.boid_movement_Vector = new_boid_movement_Vector.cloneVector() ;
    }
    //</editor-fold>

    //<editor-fold desc="boid_cohesion_Vector instantiation and getter , setter methods ">
    private polar_vector boid_cohesion_Vector = new polar_vector(0,0,true);
    public polar_vector getBoid_cohesion_Vector() {
        return this.boid_cohesion_Vector.cloneVector();
    }
    public void setBoid_cohesion_Vector(polar_vector boid_cohesion_Vector) {
        this.boid_cohesion_Vector = boid_cohesion_Vector.cloneVector();
    }
    //</editor-fold>

    //<editor-fold desc="boid_alignment_Vector instantiation and getter , setter methods ">
    private polar_vector boid_alignment_Vector = new polar_vector(0,0,true);
    public polar_vector getboid_alignment_Vector() {
        return this.boid_alignment_Vector.cloneVector();
    }
    public void setboid_alignment_Vector(polar_vector alignment_Vector) {
        this.boid_alignment_Vector = alignment_Vector.cloneVector();
    }
    //</editor-fold>

    //<editor-fold desc="boid_separation_Vector instantiation and getter , setter methods ">
    private polar_vector boid_separation_Vector = new polar_vector(0,0,true);
    public polar_vector getBoid_separation_Vector() {
        return this.boid_separation_Vector.cloneVector();
    }
    public void setBoid_separation_Vector(polar_vector boid_separation_Vector) {
        this.boid_separation_Vector = boid_separation_Vector.cloneVector();
    }
    //</editor-fold>

    //<editor-fold desc="instantiating Lists">
    //list containing all the boids in the simulation
    private List<BetterBoid> BoidList = Collections.synchronizedList(new ArrayList<BetterBoid>());//
    //list of all boids within the detection distance of the boid
    private List<BetterBoid> NearbyBoidsList = Collections.synchronizedList(new ArrayList<BetterBoid>());
    //list of all boids that are within the detection distance and withing the detection angle;
    private List<BetterBoid> DetectedBoidsList = Collections.synchronizedList(new ArrayList<BetterBoid>());
    //list of all boids within collision distance
    private List<BetterBoid> BoidInCollisionDistance = Collections.synchronizedList(new ArrayList<BetterBoid>());
    //list containing all the vectors that are added up to produce the final new_boid_movement_vector
    private List<polar_vector> BoidVectorList = Collections.synchronizedList(new ArrayList<polar_vector>());
    //</editor-fold>

    //<editor-fold desc="Boid properties variables and their getters and setters">
    //the distance at which a boid gets added to the Boidsnearby list
    private static int detectionDistance = 20;
    //the angle at which a boid is detected
    private double detectionAngle = 2*Math.PI;
    //the separation between boids
    private static int separationDistance = 10;
    //the radius of the circle representing the boid
    private static int boidRadius = 5;

    public static int getDetectionDistance() {
        return detectionDistance;
    }
    public static void setDetectionDistance(int detectionDistance) {
        BetterBoid.detectionDistance = detectionDistance;
    }
    public double getDetectionAngle() {
        return detectionAngle;
    }
    public void setDetectionAngle(double detectionAngle) {
        this.detectionAngle = detectionAngle;
    }
    public static int getSeparationDistance() {
        return separationDistance;
    }
    public static void setSeparationDistance(int separationDistance) {
        BetterBoid.separationDistance = separationDistance;
    }
    public static int getBoidRadius() {
        return boidRadius;
    }
    public static void setBoidRadius(int boidRadius) {
        BetterBoid.boidRadius = boidRadius;
    }
    //</editor-fold>

    //<editor-fold desc="constructor">
    private Settings SimSettings;
    public BetterBoid(Settings par_SimSettings,cartesian_point par_boidPosition,polar_vector par_boidMovementVector){
        setBoid_movement_Vector(par_boidMovementVector);
        setBoid_position(par_boidPosition);
        this.SimSettings = par_SimSettings;// no cloning ! i need a refrence to the actual SimSettings object
    }
    //</editor-fold>


    //</editor-fold>

    //<editor-fold desc="Vector calculations">
    /**
     * This method calculates the cohesion vector of this boid depending on the boids that are around it.id does this by calculating a vector which points towards
     * the average position of nearby boids
     */
    private void calculateCohesionVector(){
        //finding average position of nearby boids
        cartesian_point average_position = Boid_Maths.Calculate_average_boid_position(this.NearbyBoidsList);
        //calculate a vector pointing towards that average position
        average_position = Boid_Maths.calculatePositionRelativeToPoint(this.getBoid_position(),average_position);
        polar_vector temp_cohesion_vector = new polar_vector(average_position.Get_X_double(),average_position.Get_Y_double(),false);
        //now apply the multiplier to the vector
        temp_cohesion_vector.setMagnitude(temp_cohesion_vector.getMagnitude()*SimSettings.getCohesion_multiplier());
        //decrease the effect of cohesion as the flock gets bigger to prevent it from getting too dense //TODO adjust
        temp_cohesion_vector.setMagnitude(temp_cohesion_vector.getMagnitude()/this.NearbyBoidsList.size());
        //set cohesion vector
        this.setBoid_cohesion_Vector(temp_cohesion_vector);

    }

    /**
     * IMPORTANT , this function should only be called when flocking is enabled. it is not used to calculate collisions!
     * collision calculations are done in a separate function. This function prevents the flock packing too close.
     */
    private void calculateSeparationVector(){

    }

    private void calculateAlignmentVector(){
        List<BetterBoid> TempDetectedBoidList = Boid_Maths.cloneList(this.DetectedBoidsList);
        //reset alignment vector so that if boid list is empty, alignment has no effect.
        this.setboid_alignment_Vector(new polar_vector(0,0,true));
        //add vectors of all nearby boids
        polar_vector TempAlignmentVector = new polar_vector(0,0,true);
        for(BetterBoid boid:TempDetectedBoidList)
        {
            TempAlignmentVector = Boid_Maths.vector_addition(boid.getBoid_movement_Vector(),TempAlignmentVector);
        }
        //divide resultant vector by the number of boids to obtain the average vector
        TempAlignmentVector.setXcomponent(TempAlignmentVector.getXcomponent()*SimSettings.getAlignment_multiplier() / TempDetectedBoidList.size());
        TempAlignmentVector.setYcomponent(TempAlignmentVector.getYcomponent()*SimSettings.getAlignment_multiplier()/ TempDetectedBoidList.size());
        //add the resulting vector
        this.setboid_alignment_Vector(TempAlignmentVector);
    }

    //calculates and sets the separation vector for all points in the collisionPoints list
    private void CalculateSeparationVector(List<cartesian_point> collisionPoints){
        //calculate the numerator of the separation equation
        double numerator = 2*this.getBoidRadius() + getSeparationDistance();
        for(cartesian_point point:collisionPoints){
            double distanceBetweenPoints = Boid_Maths.Distance_between_points(this.getBoid_position(),point);
            double seperation_magnitude = Math.pow(numerator / distanceBetweenPoints, 5);
            double Angle = Boid_Maths.angleBetweenPoints(this.getBoid_position(),point);
            polar_vector Temp_separationVector = new polar_vector(seperation_magnitude,Angle,true);
            //invert this vector since at the moment it is pointing towards the other boid
            Temp_separationVector.Invert_Vector();
            //add the temporary separation vector
            this.setBoid_separation_Vector(Boid_Maths.vector_addition(Temp_separationVector,this.getBoid_separation_Vector()));
        }

    }

    //</editor-fold>

    //<editor-fold desc="Boid Update functions">
    public void Update(double deltaT,List<BetterBoid> par_BoidList) {
        this.setDetectionAngle(SimSettings.getDetectionAngle_rad());
        this.detectionDistance = SimSettings.getDetection_Distance();
        updateLists(par_BoidList);
        calculateFlocking();
        calculateCollision();//TODO not yet fully done
        worldWrapAround();//active even if borders are active in the case that a boid escapes
        BoidVectorList.add(this.getBoid_movement_Vector());
        this.setNew_boid_movement_Vector(Boid_Maths.vector_addition(BoidVectorList));
        if(this.new_boid_movement_Vector.getMagnitude() > 500){
            this.new_boid_movement_Vector.setMagnitude(500);
        }
        calculateNew_Boid_Position(deltaT,this.getBoid_movement_Vector());
    }

    /**
     * called when all boids have been updated, sets new boid position and vector to the normal position and vector
     */
    public void UpdatePositionAndVector(){
        this.setBoid_position(this.getNew_boid_position());
        this.setBoid_movement_Vector(this.getNew_boid_movement_Vector());
    }

    //<editor-fold desc="Update related functions">
    //TODO extend this method for obstacle and predator lists
    /**
     * uses the provided boid list ( clones it ) , to calculate which boids belong in the other lists. these lists are then used for
     * all the other update functions
     * @param par_BoidList
     */
    public void updateLists(List<BetterBoid> par_BoidList){
        //cloning the boidlist, making it threadsafe and stuff ( so that the list does not change while it is being edited

        this.BoidList = Boid_Maths.cloneList(par_BoidList);
        //test which boids are within detection distance
        this.NearbyBoidsList.clear();
        this.BoidInCollisionDistance.clear();
        for(BetterBoid boid:this.BoidList){
            double distanceBetweenBoids = Boid_Maths.Distance_between_points(this.getBoid_position(),boid.getBoid_position());
            if( distanceBetweenBoids <= this.detectionDistance && boid != this){
                this.NearbyBoidsList.add(boid);
            }
            //test which boids are within collision distance
            if( distanceBetweenBoids <= this.separationDistance && boid != this){
                this.BoidInCollisionDistance.add(boid);
            }
        }
        //checking which of the boids within the detection distance are within the detection angle and therefore Detected
        this.DetectedBoidsList.clear();
        for(BetterBoid boid:this.NearbyBoidsList){
            double angleBetweenBoids = Boid_Maths.angleBetweenPoints(this.getBoid_position(), boid.getBoid_position());
            //finding angle between the vector between the boids and the movement vector of this boid
            angleBetweenBoids = Math.abs(angleBetweenBoids - this.getBoid_movement_Vector().getAngle_rad());
            if(angleBetweenBoids <= this.detectionAngle ){
                this.DetectedBoidsList.add(boid);
            }
        }
        //clearing the BoidVectorList to allow the newly calculated vectors to be added;
        this.BoidVectorList.clear();
    }

    /**
     * calculates the vectors necessary for flocking behaviour to emerge
     */
    private void calculateFlocking() {
        //if flocking is enabled find the cohesion and separation vectors
        if(SimSettings.isFlocking_Enabled()){
            calculateCohesionVector();
            this.BoidVectorList.add(this.getBoid_cohesion_Vector());
            calculateAlignmentVector();
            this.BoidVectorList.add(this.getboid_alignment_Vector());
        } else {//set the cohesion and allignment vectors to zero so they dont affect the boid
            setBoid_cohesion_Vector(new polar_vector(0,0,true));
            setboid_alignment_Vector(new polar_vector(0,0,true));
        }
    }


    /**
     * checks which borders are near, then checks if boid passed the borders.
     */
    private void worldWrapAround(){
        List<cartesian_point> BorderPoints = new ArrayList<>();
        String Borders = CalculateNearestBorderPoints(BorderPoints);
        if(Borders.contains("LEFT")){
            if(this.getBoid_position().Get_X_int() < BorderPoints.get(0).Get_X_int()){
                //boid outside of left border,set to right border
                this.setBoid_position(new cartesian_point(SimSettings.getScreenDimension().getWidth(),this.getBoid_position().Get_Y_int()));
            }
        }
        if(Borders.contains("RIGHT")){
            if(this.getBoid_position().Get_X_int() > BorderPoints.get(0).Get_X_int()){
                //boid outside of left border,set to left border
                this.setBoid_position(new cartesian_point(0,this.getBoid_position().Get_Y_int()));
            }
        }
        if(Borders.contains("TOP")){
            if(this.getBoid_position().Get_Y_int() < BorderPoints.get(1).Get_Y_int()){
                //boid outside of Top border,set to Bottom border
                this.setBoid_position(new cartesian_point(this.getBoid_position().Get_X_int(),SimSettings.getScreenDimension().getHeight()));
            }
        }
        if(Borders.contains("BOTTOM")){
            if(this.getBoid_position().Get_Y_int() > BorderPoints.get(1).Get_Y_int()){
                //boid outside of Bottom border,set to Top border
                this.setBoid_position(new cartesian_point(this.getBoid_position().Get_X_int(),0));
            }
        }
    }

    private void calculateNew_Boid_Position(double deltaT , polar_vector boidMovementVector){
        polar_vector TimeMovementVector = boidMovementVector.cloneVector();
        TimeMovementVector.setMagnitude(TimeMovementVector.getMagnitude()*deltaT);
        double Boidx = this.getBoid_position().Get_X_double();
        double Boidy = this.getBoid_position().Get_Y_double();
        double VectorXcomp = TimeMovementVector.getXcomponent();
        double VectorYcomp = TimeMovementVector.getYcomponent();
        cartesian_point newposition = new cartesian_point(Boidx + VectorXcomp,Boidy + VectorYcomp);
        this.setNew_boid_position(newposition);
    }

    //<editor-fold desc="calculateCollision and other related functions">
    private void calculateCollision(){
        //resetting the separation vector
        this.setBoid_separation_Vector(new polar_vector(0,0,true));
        //list containing all points that repell the boid
        List<cartesian_point> CollisionPoints = new ArrayList<>();
        //add the positions of the boids in collision distance to the CollisionPoints list
        for(BetterBoid boid:BoidInCollisionDistance){
            CollisionPoints.add(boid.getBoid_position());
        }
        //TODO add obstacles positions to CollisionPoints list
        if(SimSettings.BorderedSimulation) {
            CalculateNearestBorderPoints(CollisionPoints);
        }
        CalculateSeparationVector(CollisionPoints);
        //at the end of calculating all collisions, if the resulting vector is not 0, add it to the Vector list to be included in the new boid vector calculation
        if(this.getBoid_separation_Vector().getMagnitude() > 0) {
            this.BoidVectorList.add(getBoid_separation_Vector());
        }
    }

    //used for the collision and wraparound function. adds points on the nearest x and y axis to the list
    private String CalculateNearestBorderPoints(List<cartesian_point> BorderCollisionPoint){
        //get the size of the simulation window
        Dimension SimulationDimension = SimSettings.getScreenDimension();
        //return string indicating which borders are near
        String returnString = "";
        int Xlength = SimulationDimension.width;
        int Ylength = SimulationDimension.height;
        int xPos = this.getBoid_position().Get_X_int();
        int yPos = this.getBoid_position().Get_Y_int();
        int DistanceToLeft = Math.abs(xPos);
        int DistanceToRight = Math.abs(Xlength - xPos);
        int DistanceToTop = Math.abs(yPos);
        int DistanceToBottom = Math.abs(Ylength - yPos);
        //add points to list
        if(DistanceToLeft <= DistanceToRight){
            BorderCollisionPoint.add(new cartesian_point(0,this.boid_position.Get_Y_int()));
            returnString = returnString + " LEFT";
        }else {
            BorderCollisionPoint.add(new cartesian_point(Xlength,this.boid_position.Get_Y_int()));
            returnString = returnString + " RIGHT";
        }
        if(DistanceToTop <= DistanceToBottom){
            BorderCollisionPoint.add(new cartesian_point(this.boid_position.Get_X_int(),0));
            returnString = returnString + " TOP";
        }else{
            BorderCollisionPoint.add(new cartesian_point(this.boid_position.Get_X_int(),Ylength));
            returnString = returnString + " BOTTOM";
        }
        return returnString;
    }


    //</editor-fold>

    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="DRAW Function">
    @Override
    public void Draw(Graphics2D g) {
       //boid x and y position
        int XBoidpos = this.getBoid_position().Get_X_int();
        int YBoidpos = this.getBoid_position().Get_Y_int();
        /*
        this draws an oval which fits into a rectangle starting at pos x,y with side lengths width and heigh.
        this means some adjustment has to be made so it is centred on the boid position
         */
        Ellipse2D.Double circle = new Ellipse2D.Double(boid_position.Get_X_int() - this.getBoidRadius(),boid_position.Get_Y_int()- this.getBoidRadius(),2*this.getBoidRadius(),2*this.getBoidRadius());
        //draw the boid
        g.setColor(Color.black);
        int Xpos = this.getBoid_position().Get_X_int();
        int Ypos = this.getBoid_position().Get_Y_int();

        if(SimSettings.Show_Boid)
        {
            if(this.DetectedBoidsList.size() == 0){
                g.setColor(Color.red);
            }
            else{
                g.setColor(Color.green);
            }
            g.fill(circle);
        }
        if(SimSettings.Show_Boid_vector){
            //boid vector end point pos
            polar_vector movementVectorRepresentation = this.getBoid_movement_Vector();
            movementVectorRepresentation.setMagnitude(100);
            int bvecX = Xpos + (int)movementVectorRepresentation.getXcomponent();
            int bvecY = Ypos + (int)movementVectorRepresentation.getYcomponent();
            g.setColor(Color.red);
            g.drawLine(Xpos,Ypos,bvecX,bvecY);
        }
        if(SimSettings.Show_Boids_nearby){
            List<BetterBoid> TempDetectedBoidList = Boid_Maths.cloneList(this.DetectedBoidsList);
            for(BetterBoid boid:TempDetectedBoidList){
                    int boidx = boid.getBoid_position().Get_X_int();
                    int boidy = boid.getBoid_position().Get_Y_int();
                //prevent graphics error in world wrap
                double distanceBetweenBoids = Boid_Maths.Distance_between_points(this.getBoid_position(),boid.getBoid_position());
                if(distanceBetweenBoids < this.getDetectionDistance()){
                    g.setColor(Color.green);
                    g.drawLine(Xpos,Ypos,boidx,boidy);
                }
            }
        }

    }
    //</editor-fold>
}
