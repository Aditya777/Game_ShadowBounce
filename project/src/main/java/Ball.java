import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Side;
import bagel.util.Vector2;



public class Ball extends Piece{

    /**
     * Constant  initial position of ball
     */
    private static final Vector2 INIT_POSITION = new Vector2(Window.getWidth()/2, 32);

    /**
     * constant initial velocity magnitude
     */
    public static final double INIT_VELOCITY_MAG = 10;

    /**
     * constant gravity
     */
    public static final Vector2 GRAVITY = new Vector2(0,0.15);

    private static final String BALL_IMAGE = "res/ball.png";
    private static final String FIREBALL_IMAGE = "res/fireball.png";
    private Boolean onFire;

    /**
     * Constructor for ball: initialises with the image, sets state and onFire false
     * @return Returns nothing
     */
    public Ball(){

       super(new Image(BALL_IMAGE));
       setState(false);           // to not draw the ball on the screen until the mouse click
       onFire = false;
    }

    /**
     * Initialises the ball based on the input: with a  velocity towards the input position
     * @param input The mouse input
     * @return Returns nothing
     */
    public void initialise(Input input){

        setPosition(INIT_POSITION);
        setVelocity(input.directionToMouse(getPosition().asPoint()).mul(INIT_VELOCITY_MAG));    // initial velocity = unit vector towards mouse times initial velocity magnitude
        setPieceImage(new Image(BALL_IMAGE));
        setState(true);
        setFire(false);
    }

    /**
     * Initialises the ball at a given position with a given velocity
     * @param position The position of the ball to initialise with
     * @param velocity The velocity of the ball to initialise with
     * @return Returns nothing
     */
    public void initialise(Vector2 position, Vector2 velocity){

        setVelocity(velocity);
        setPosition(position);
        setState(true);
        setPieceImage(new Image(BALL_IMAGE));
    }

    /**
     * Sets onFire True, and change the image to fireball also, if true
     * @param fire Boolean whether to set on fire or not
     * @return Returns nothing
     */
    public void setFire(Boolean fire){
        if(fire == true) {
            onFire = true;
            setPieceImage(new Image(FIREBALL_IMAGE));
        }
        else{
            onFire = false;
        }
    }

    /**
     * Return whether ball on fire
     * @return Returns true if ball on fire
     */
    public Boolean isOnFire(){
        return onFire;
    }

    /**
     * Adds Gravity to velocity
     * @return Returns nothing
     */
    public void simulateGravity(){
        setVelocity(getVelocity().add(GRAVITY));
    }


    /**
     * <p>Moves the ball, by adding velocity to position and simulating gravity, checks bounce off wall also, and sets state false if goes down the screen
     * Overrides move() of piece
     * </p>
     * @return Returns nothing
     */
    @Override
    public void move(){
        super.move();
        simulateGravity();


        if(getPosition().x <= 0 || getPosition().x >= Window.getWidth()){     // checking rebound from the  walls
            setVelocity(getVelocity().sub(new Vector2(2*(getVelocity().x),0)));    // subtracting twice the x-component of velocity
        }

        if(getPosition().y > Window.getHeight()){                 // checks if the ball goes off the screen
            setState(false);
        }

        if(getPosition().y < 0){                                  // Rebound off the ceiling
            setVelocity(getVelocity().add(Vector2.down.mul(2*Math.abs(getVelocity().y))));
        }
    }

    /**
     * Bounces off the pegs
     * @param peg The peg to bounce off of
     * @return Returns nothing
     */
    public void bounce(Peg peg){
        Side side = peg.getPieceRectangle().intersectedAt(getPosition().asPoint(), getVelocity());
        if (side == Side.LEFT || side == Side.RIGHT) {
            setVelocity(getVelocity().sub(new Vector2(2 * (getVelocity().x), 0)));
        }
        else{
            setVelocity(getVelocity().sub(new Vector2(0, 2 * (getVelocity().y))));
        }
    }

    /**
     * Hits the given piece (i.e calls the destroy method of the piece if it intersects)
     * @param piece The piece to hit
     * @return Returns whether hit or not
     */
    public boolean hit(Piece piece) {
        if (piece.intersects(this)){
            piece.destroy(this);
            return true;
        }
        return false;
    }

}
