public class Boomer extends Enemy{

    private int blastRadius;

    public Boomer(int xPos, int yPos, int timer, int blastRadius) {
        super(xPos, yPos, timer);
        this.blastRadius = blastRadius;
    }

    public int getBlastRadius() {
        return blastRadius;
    }

    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }
}
