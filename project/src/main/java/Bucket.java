import bagel.Image;
import bagel.Window;
import bagel.util.Vector2;


public class Bucket extends Piece {

    private static final Vector2 BUCKET_INITIAL_VELOCITY = new Vector2(-1,0).mul(4) ;
    private static final Vector2 BUCKET_INITIAL_POSITION = new Vector2(Window.getWidth()/2, Window.getHeight()-24);
    private static final String BUCKET_IMAGE = "res/bucket.png";


    /**
     * Constructor for bucket, initialing image, position and velocity
     */
    public Bucket(){
        super(new Image(BUCKET_IMAGE));
        setVelocity(BUCKET_INITIAL_VELOCITY);
        setPosition(BUCKET_INITIAL_POSITION);
    }

    /**
     * Overrides the move method of piece, to reverse direction on hitting walls
     */
    @Override
    public void move(){
        super.move();
        if(getPieceRectangle().left() < 0 || getPieceRectangle().right() > Window.getWidth()){
            setVelocity(getVelocity().sub(new Vector2(2*(getVelocity().x),0)));
        }
    }


    /**
     * Overrides the intersects method of piece, to ensure it gives true only once when the ball hits the bucket
     * @param piece The piece to check with
     * @return Returns tru if ball hit the bucket
     */
    @Override
    public boolean intersects(Piece piece){            // Checks if the ball intersects with the bucket and in the next frame it doesn't
        if(getPieceRectangle().intersects(piece.getPosition().asPoint())  && !(getPieceRectangle().intersects(piece.getPosition().add(piece.getVelocity()).asPoint()))){
            return true;
        }
        return  false;
    }

    /**
     * Overrides the destroy method of piece, to not set the state to false
     * @param ball The ball that is hitting the bucket
     * @return Returns nothing
     */
    public void destroy(Ball ball){
    }

}
