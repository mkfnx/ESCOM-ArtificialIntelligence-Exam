package proyectoia;

import java.awt.Point;

/**
 *
 * @author Miguel
 */
public class WorldCell {
    private int g;
    private int parentG;
    private Point position = new Point();
    private Point parentPosition = new Point();

    WorldCell(Point position, Point parentPosition) {
        this.position.x = position.x;
        this.position.y = position.y;
        
        this.parentPosition.x = parentPosition.x;
        this.parentPosition.y = parentPosition.y;
        
        this.g = 0;
        this.parentG = 0;
    }
    
    WorldCell(Point position, Point parentPosition, int cost, int parentCost) {
        this.position.x = position.x;
        this.position.y = position.y;
        
        this.parentPosition.x = parentPosition.x;
        this.parentPosition.y = parentPosition.y;
        
        this.g = cost;
        this.parentG = parentCost;
    }

    public int getX() {
        return (int) position.x;
    }

    public void setX(int x) {
        position.x = x;
    }

    public int getY() {
        return (int) position.y;
    }

    public void setY(int y) {
        position.y = y;
    }

    public int getParentX() {
        return (int) parentPosition.x;
    }

    public void setParentX(int parentX) {
        parentPosition.x = parentX;
    }

    public int getParentY() {
        return (int) parentPosition.y;
    }

    public void setParentY(int parentY) {
        parentPosition.y = parentY;
    }

    public int getCost() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getParentCost() {
        return parentG;
    }

    public void setParentG(int parentG) {
        this.parentG = parentG;
    }

    public Point getPosition() {
        return position;
    }

//    public void setPosition(Point position) {
//        this.position = position;
//    }

    public Point getParentPosition() {
        return parentPosition;
    }

//    public void setParentPosition(Point parentPosition) {
//        this.parentPosition = parentPosition;
//    }
}
