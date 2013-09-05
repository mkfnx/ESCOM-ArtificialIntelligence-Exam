package proyectoia;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class AStar {

    private ArrayList<WorldCell> openList;
    private ArrayList<WorldCell> closedList;
    private Point start;
    private Point finish;
//    private int costSum;
    private int[][] worldCosts;
    private boolean finished = true;
    private ArrayList<WorldCell> path;

    public AStar(int[][] worldCosts) {
        this.worldCosts = worldCosts;
    }

    public ArrayList<Point> getPath() {
        return new ArrayList<>();
    }

    public void setRoute(Point start, Point finish) {
        this.start = start;
        this.finish = finish;
        
        finished = false;
        
        openList = new ArrayList<>();
        closedList = new ArrayList<>();
        path = new ArrayList<>();
        
        openList.add(new WorldCell(this.start, new Point(-1, -1), 0, 0));
    }

    public ArrayList<WorldCell> step() {
        if (openList == null || openList.size() <= 0) {
//            throw new IllegalStateException("Open list is empty");
            return new ArrayList<>();
        }

        if (finished) {
            return new ArrayList<>();
        }

        int minimumCostCellIndex = 0;
        int f = calcH(openList.get(0).getPosition(), finish) + calcG(openList.get(0));
        WorldCell cell = openList.get(0);
        WorldCell currentCell = new WorldCell(new Point(cell.getX(), cell.getY()), new Point(0, 0), cell.getCost(), cell.getParentCost());

        for (int i = 1; i < openList.size(); i++) {
            cell = openList.get(i);

            int currentF = calcH(cell.getPosition(), finish) + calcG(cell);

            if (currentF <= f) {
                f = currentF;
                currentCell = new WorldCell(new Point(cell.getX(), cell.getY()), new Point(0, 0), cell.getCost(), cell.getParentCost());

                minimumCostCellIndex = i;
            }
        }

        WorldCell minimumCostCell = openList.remove(minimumCostCellIndex);
        if (minimumCostCell.getX() == finish.getX() && minimumCostCell.getY() == finish.getY()) {
            // already in goal, no need to expand more nodes
            finished = true;
//            printCurrentPath(minimumCostCell);
            return getCurrentPath(minimumCostCell);
        } else {
            closedList.add(minimumCostCell);
        }

        int d = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int xOffset = d + i;
                int yOffset = d + j;
                if (currentCell.getX() + xOffset < 0 || currentCell.getX() + xOffset > worldCosts.length-1
                        || currentCell.getY() + yOffset < 0 || currentCell.getY() + yOffset > worldCosts[0].length-1
                        || (i == 1 && j == 1)) {
                    continue;
                }

                Point neighborCellPos = new Point(currentCell.getPosition());
                neighborCellPos.x += xOffset;
                neighborCellPos.y += yOffset;
                // COST MINIMIZATION
                int g = currentCell.getParentCost() + worldCosts[neighborCellPos.x][neighborCellPos.y];
                // STEP MINIMIZATION
//                int g = currentCell.getParentCost() + calcH(currentCell.getPosition(), finish);
                WorldCell neighborCell = new WorldCell(neighborCellPos, currentCell.getPosition(), g, currentCell.getCost());

                boolean inClosedList = false;
                for (WorldCell closedListCell : closedList) {
                    if (closedListCell.getPosition().equals(neighborCellPos)) {
                        inClosedList = true;
                        break;
                    }
                }

                boolean inOpenList = false;
                for (WorldCell openListCell : openList) {
                    if (openListCell.getPosition().equals(neighborCellPos)) {
                        inOpenList = true;
                        break;
                    }
                }

                if (!inOpenList && !inClosedList && worldCosts[neighborCellPos.x][neighborCellPos.y] != -1) {
                    openList.add(neighborCell);
                }
            }
        }

        //return current path
//        printCurrentPath(minimumCostCell);
        return getCurrentPath(minimumCostCell);
    }

    private int calcH(Point source, Point destiny) {
        return Math.abs(source.x - destiny.x) + Math.abs(source.y - destiny.y);
    }

    private int calcG(WorldCell cell) {
        // COST MINIMIZATION
        return cell.getParentCost() + worldCosts[cell.getX()][cell.getY()];
        // STEP MINIMIZATION
//        return cell.getParentCost() + calcH(cell.getPosition(), finish);
    }

    public void printCurrentPath(WorldCell cell) {
        WorldCell cellCopy = new WorldCell(cell.getPosition(), cell.getParentPosition(), cell.getCost(), cell.getParentCost());
        int cost = 0;

        int x = 0;
        int y = 0;

        while (true) {
            x = cellCopy.getX();
            y = cellCopy.getY();

            System.out.println(x + ", " + y + ": " + worldCosts[x][y]);
            cost += worldCosts[x][y];

            if (cellCopy.getParentPosition().equals(new Point(-1, -1))) {
                System.out.println("costo: " + cost);
                break;
            }
            
            for (WorldCell closedListCell : closedList) {
                if (closedListCell.getPosition().equals(cellCopy.getParentPosition())) {
                    cellCopy.setX(closedListCell.getX());
                    cellCopy.setY(closedListCell.getY());
                    cellCopy.setParentX(closedListCell.getParentX());
                    cellCopy.setParentY(closedListCell.getParentY());
                    break;
                }
            }
        }
    }
    
    public ArrayList<WorldCell> getCurrentPath(WorldCell cell) {
        path = new ArrayList<>();
        
        WorldCell cellCopy = new WorldCell(cell.getPosition(), cell.getParentPosition(), cell.getCost(), cell.getParentCost());
        int cost = 0;

        int x = 0;
        int y = 0;

        while (true) {
            x = cellCopy.getX();
            y = cellCopy.getY();

            path.add(new WorldCell(cellCopy.getPosition(), cellCopy.getPosition(), cellCopy.getCost(), cellCopy.getParentCost()));
            System.out.println(x + ", " + y + ": " + worldCosts[x][y]);
            cost += worldCosts[x][y];

            if (cellCopy.getParentPosition().equals(new Point(-1, -1))) {
                System.out.println("costo: " + cost);
                break;
            }
            
            for (WorldCell closedListCell : closedList) {
                if (closedListCell.getPosition().equals(cellCopy.getParentPosition())) {
                    cellCopy.setX(closedListCell.getX());
                    cellCopy.setY(closedListCell.getY());
                    cellCopy.setParentX(closedListCell.getParentX());
                    cellCopy.setParentY(closedListCell.getParentY());
                    break;
                }
            }
        }
        
        return path;
    }
}
