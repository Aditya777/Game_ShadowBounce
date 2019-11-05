import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
  An abstract class for a piece on board like ball and peg
 */

public abstract class Piece {

    private Vector2 position;
    private Vector2 velocity;
    private Image pieceImage;
    private boolean state;  //true => is Alive => is on screen, false => is Dead => out of screen or destroyed

    /**
     * Constructor for piece, initialise position, velocity , image and state
     * @param pieceImage The piece Image of thw piece
     */
    public Piece(Image pieceImage){
        position = new Vector2();
        velocity = new Vector2();
        this.pieceImage = pieceImage;  // initialises with given piece image
        state = true;
    }

    /**
     * Returns the position of the piece
     * @return Returns the position of the piece
     */
    public Vector2 getPosition(){
        return position;
    }


    /**
     *Sets the position, given a position vector
     */

    public void setPosition(Vector2 position){
        this.position = position;
    }

    /**
     * Returns the velocity
     * @return Returns the velocity
     */

    public Vector2 getVelocity(){
        return velocity;
    }

    /**
     * Sets the velocity
     * @param velocity The velocity to set to
     */

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    /**
     * Returns the state
     * @return
     */

    public boolean isAlive(){
        return state;
    }

    /**
     * Sets the state
     * @param state The state to set to
     */

    public void setState(Boolean state){
        this.state = state;
    }

    public void setPieceImage(Image pieceImage){
        this.pieceImage = pieceImage;
    }


    /**
     *  draws the piece on the screen
     */


    public void drawPiece(){
        if(state) {
            pieceImage.draw(position.x, position.y);
        }
    }

    /**
     * checks whether the piece intersects with another piece
     * @param piece The piece to check with
     * @return Return true of intersects
     */

    public boolean intersects(Piece piece){
        if(this.isAlive()) {
            return this.getPieceRectangle().intersects(piece.getPosition().asPoint());
        }
        return false;
    }

    public Rectangle getPieceRectangle(){

        return pieceImage.getBoundingBoxAt(position.asPoint());
    }

    /**
     * Adds position to velocity
     */
    public void move(){
        if(state) {
            position = position.add(velocity);
        }
    }

    /**
     * Sets state to false
     * @param ball
     */
    public void destroy(Ball ball){
        setState(false);
    }

}
