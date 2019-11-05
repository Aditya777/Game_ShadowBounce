import bagel.Image;
import bagel.util.Vector2;


public class Peg extends Piece{

    /**
     * Enumerates the different peg shapes
     */
    public enum PegShape {
        NORMAL,HORIZONTAL, VERTICAL;
    }

    private PegType pegType;
    private PegShape pegShape;

    /**
     * Constructor for peg
     * @param pegType The peg type to initialise with
     * @param pegShape The peg shape to initialise with
     * @param x The x coordinate of the initial position
     * @param y The y coordinate of the initial position
     * @return Returns nothing
     */
    public Peg(String pegType, String pegShape, String x, String y) {
        super(new  Image(("res/"+pegType+"-"+pegShape+"-peg.png").replace("normal-", "")));
        setPosition(new Vector2(Double.parseDouble(x), Double.parseDouble(y)));
        this.pegType = PegType.valueOf(pegType.toUpperCase());
        this.pegShape = PegShape.valueOf(pegShape.toUpperCase());
    }

    /**
     * Returns the peg type
     * @return Returns the peg type
     */
    public PegType getPegType(){
        return pegType;
    }

    /**
     * Sets the state of the peg false if it is not grey and makes the ball bounce
     * @param ball The Ball which destroyed the peg
     * @return Returns nothing
     */
    @Override
    public void destroy(Ball ball){
        super.destroy(ball);
        if(pegType == PegType.GREY){
            setState(true);
        }
        ball.bounce(this);
    }

    /**
     * Change the peg type
     * @param pegType The peg type to change to
     * @returns Returns nothing
     */
    public void changePegType(PegType pegType){         // changes the image of the peg as well
        this.pegType = pegType;
        setPieceImage(new Image(("res/"+pegType.toString().toLowerCase()+"-"+pegShape.toString().toLowerCase()+"-peg.png").replace("normal-", "")));
    }

}

