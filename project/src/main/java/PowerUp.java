import bagel.Image;
import bagel.Window;
import bagel.util.Vector2;

import java.util.Random;

public class PowerUp extends Piece {


    private static final int VELOCITY_MAG = 3;
    private static final int MIN_DISTANCE_TO_DESTINATION = 5;
    private static final double PROBABILITY_TO_GENERATE_POWERUP = 0.1;

    private static final String POWER_UP_IMAGE = "res/powerup.png";

    private Vector2 destination; // The destination to move towards

    /**
     * The constructor for powerup, initialise image, destination and set state to false
     */
    public PowerUp() {
        super(new Image(POWER_UP_IMAGE));
        destination = new Vector2();
        setState(false);
    }

    /**
     * Generates powerup at a random position
     *
     * @return Returns nothing
     */
    public void generate() {
        Random rand = new Random();

        if (rand.nextDouble() < PROBABILITY_TO_GENERATE_POWERUP) {
            setPosition(createRandomPosition());
            destination = createRandomPosition();
            setVelocity(destination.sub(getPosition()).normalised().mul(VELOCITY_MAG));
            setState(true);
        }
    }

    /**
     * Chooes random position
     *
     * @return returns the random position
     */

    private Vector2 createRandomPosition() {
        Random rand = new Random();
        return new Vector2(rand.nextDouble() * Window.getWidth(), rand.nextDouble() * Window.getHeight());
    }


    /**
     * Overrides the move() of piece. Chooses new random destination if in minimum radius
     *
     * @return Returns nothing
     */
    @Override
    public void move() {
        super.move();
        if (isAlive()) {
            if (getPosition().sub(destination).length() <= MIN_DISTANCE_TO_DESTINATION) {
                destination = createRandomPosition();
                setVelocity(destination.sub(getPosition()).normalised().mul(VELOCITY_MAG));
            }
        }
    }

    /**
     * Sets the ball on fire
     * @param ball The ball which hit the powerup
     * @return Return nothing
     */
    public void destroy(Ball ball){
        super.destroy(ball);
        ball.setFire(true);
    }


}
