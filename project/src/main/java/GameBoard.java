import bagel.Input;
import bagel.Window;
import bagel.util.Vector2;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;



public class GameBoard {

    private static final int MAX_BALLS = 3;
    private static final double FIREBALL_DESTROY_RADIUS = 70;
    private static final double FRACTION_OF_RED_PEGS = 0.2;

    private ArrayList<Peg> pegs = new ArrayList<Peg>();
    private Ball balls[] = new Ball[MAX_BALLS];
    private PowerUp powerUp;
    private Bucket bucket;

    private boolean turnFlag = false; // Turn in progress or not (i.e. ball on screen or not)

    private int greenPegLoc = -1;      // to store the index of the greenPeg in the array
    private final int mainBall = 0;    // index of the user-generated main ball


    /**
     * Constructor for the gameboard class:
     *      1. Initialises all the pieces
     *      2. Generates powerup
     *      3. Loads pegs and chooses red and green ones
     * @param filename The name of the csv file to load pegs from
     * @return Returns nothing
     */

    public GameBoard(String filename) {
        for (int i = 0; i < MAX_BALLS; i++) {
            balls[i] = new Ball();
        }

        powerUp = new PowerUp();
        bucket = new Bucket();

        powerUp.generate();

        loadPegs(filename);
        chooseRedPegs();
        chooseGreenPeg();

    }

    /**
     * Draws all the board pieces on the screen
     * @return Returns nothing
     */

    public void drawBoardPieces() {
        for (Ball ball : balls) {
            ball.drawPiece();
        }

        for (Peg peg : pegs) {
            peg.drawPiece();
        }

        powerUp.drawPiece();
        bucket.drawPiece();
    }

    /**
     * Starts a new turn: initialises a new ball, decrements the number of shots remaining and sets the turn flag true
     * @param game Instance of the shadowbounce game
     * @param input The mouse input
     * @retrun Returns nothing
     */

    public void newTurn(ShadowBounce game, Input input) {
        if (isTurnOver()) {
            balls[mainBall].initialise(input);             // initialise the main ball

            game.decrementShotsRemaining();
            turnFlag = true;
            System.out.println(String.format("Shots Remaining: %d", game.getShotsRemaining()));
        }
    }

    /**
     * Returns whether all the balls are dead or not (i.e. off the screen)
     * @return Returns true if turn is over(i.e. no ball on screen)
     */

    public boolean isTurnOver() {
        for (Ball ball : balls) {
            if (ball.isAlive()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates 2 Balls at a given position, with velocity diagonally upward towards left & right
     * @param position The position where the 2 Balls need to be created (i.e. the position of the peg hit)
     * @return Returns nothing
     */

    public void create2Balls(Vector2 position) {
        balls[1].initialise(position, new Vector2(-1, -1).normalised().mul(Ball.INIT_VELOCITY_MAG));
        balls[2].initialise(position, new Vector2(1, -1).normalised().mul(Ball.INIT_VELOCITY_MAG));
    }

    /**
     * Implements the behaviour of a fire ball, i.e. destroys all balls within a given radius around a given peg
     * @param ball The instance of the fireball
     * @param sourcePeg The peg from which the radius needs to be calculated
     * @returns Returns nothing
     */

    public void fireBallBehaviour(Ball ball, Peg sourcePeg) {
        for (Peg peg: pegs) {
            if (peg.isAlive()) {
                if ((peg.getPosition().sub(sourcePeg.getPosition())).length() <= FIREBALL_DESTROY_RADIUS) {
                    peg.destroy(ball);
                    if (peg.getPegType() == PegType.GREEN) {
                        create2Balls(peg.getPosition());
                    }
                }
            }
        }
    }

    /**
     * <p>Wraps all the features of the game:
     *    1. Moves all the pieces
     *    2. The ball hits the pieces
     *             i. if green ball is hit, Create2Balls is called
     *             ii. if powerup is hit, ball is set on fire
     *             ii. if bucket is hit, number of shots remaining is incremented
     *    3. if all red pegs destroyed, increments board
     *    4. if turn is over
     *          i.chooses a new green peg
     *          ii. generates powerup
     *          iii. sets turn flag false
     *     </>
     * @param game The instance of the shadowbounce game
     * @return Returns nothing
     */

    public void playGame(ShadowBounce game) {

        powerUp.move();
        bucket.move();

        for (Ball ball : balls) {
            if (ball.isAlive()) {
                ball.move();

                ball.hit(powerUp);

                if(ball.hit(bucket)){
                    game.incrementShotsRemaining();
                    System.out.println(String.format("Shots Remaining: %d", game.getShotsRemaining()));
                }

                for (Peg peg : pegs) {
                    if (ball.hit(peg)) {
                        if (peg.getPegType() == PegType.GREEN) {
                            create2Balls(peg.getPosition());
                        }
                        if (ball.isOnFire()) {
                            fireBallBehaviour(ball, peg);
                        }
                    }
                }
            }
        }

        if(allRedPegsCleared()){
            game.incrementBoardNumber();
        }

        if (isTurnOver() && turnFlag) {             // When the turn has ended and new one hasn't started yet
            if (game.getShotsRemaining() == 0) {
                Window.close();
            } else {
                chooseGreenPeg();
                powerUp.setState(false);
                powerUp.generate();
            }
            turnFlag = false;
        }
    }

    /**
     * Initiates all the pegs from the given csv file
     * @param fileName The string name of the csv file containing peg info
     * @return Returns nothing
     */

    public void loadPegs(String fileName){

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;
            int i = 0;
            while ((text = br.readLine()) != null) {
                String[] columns = text.split(",");
                ArrayList<String> pegFeatures = new ArrayList<String>(Arrays.asList(columns[0].split("_")));
                if(pegFeatures.size()==2){
                    pegFeatures.add("normal");
                }
                pegs.add(new Peg(pegFeatures.get(0), pegFeatures.get(2),columns[1], columns[2]));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a fraction of blue pegs into red pegs
     * @return Returns nothing
     */
    private void chooseRedPegs(){

        int numBluePegs = 0;
        for(Peg peg : pegs){
            if(peg.getPegType()== PegType.BLUE) {
                numBluePegs++;
            }
        }

        for(int i =0; i< numBluePegs*FRACTION_OF_RED_PEGS; i++){
            int randLoc;
            do {
                Random rand = new Random();
                 randLoc = rand.nextInt(pegs.size());
            } while(pegs.get(randLoc).getPegType()!=PegType.BLUE);

            pegs.get(randLoc).changePegType(PegType.RED);
        }
    }

    /**
     * Sets a random blue peg to green. If one already exists, converts it to blue and then chooses a new one.
     * @return Returns nothing
     */
    public void chooseGreenPeg(){

        if(greenPegLoc != -1){
            if(pegs.get(greenPegLoc).isAlive()){                               // to convert the peg back to blue if isn't destroyed
                pegs.get(greenPegLoc).changePegType(PegType.BLUE);
            }
        }

        int randLoc;
        do {
            Random rand = new Random();
            randLoc = rand.nextInt(pegs.size() - 1);
        } while((pegs.get(randLoc).getPegType() != PegType.BLUE) || !(pegs.get(randLoc).isAlive()));

        pegs.get(randLoc).changePegType(PegType.GREEN);
        greenPegLoc =  randLoc;
    }

    /**
     * Returns whether all pegs are cleared or not
     * @return Returns a true if no red peg on screen
     */

    public Boolean allRedPegsCleared(){
        for(Peg peg : pegs){
            if(peg.getPegType()==PegType.RED){
                if(peg.isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }

}


