package proyectoia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Miguel
 */
class Surface extends JPanel {

    int r, c;
    int x, y, s;
    int[][] world;
    int[][] worldCosts;
    ArrayList<WorldCell> path;

    public Surface(int[][] world, int[][] worldCosts) {
        this.world = world;
        this.worldCosts = worldCosts;
                
        r = world.length;
        c = world[0].length;

        x = y = 0;
        s = 40;
    }

    public void drawWorld(Graphics g) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                int terrainType = world[i][j];
                switch (terrainType) {
                    case WorldGUI.MO:
//                        System.out.print("S");
                        g.setColor(Color.black);
                        g.fillRect(x, y, s, s);
                        break;
                    case WorldGUI.EA:
//                        System.out.print("G");
                        g.setColor(new Color(0xFFA07A));
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    case WorldGUI.WA:
//                        System.out.print("F");
                        g.setColor(new Color(0x1E90FF));
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    case WorldGUI.SA:
//                        System.out.print("W");                       
                        g.setColor(new Color(0xDAA520));
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    case WorldGUI.FO:
//                        System.out.print("V");
                        g.setColor(new Color(0x9ACD32));
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    case WorldGUI.SN:
//                        System.out.print("PV");
                        g.setColor(Color.white);
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    case WorldGUI.SW:
//                        System.out.print("PV");
                        g.setColor(new Color(0xEE82EE));
                        g.fillRect(x, y, s, s);
                        
                        g.setColor(Color.black);
                        g.drawRect(x, y, s, s);
                        break;
                    default:
                        break;
                }
                x += s;
            }
            y += s;
            x = 0;
        }
        x = y = 0;
        
        char[] poi = {'1', '2', '3', 'K', 'D', 'S', 'F', 'P'};
        
        g.setColor(Color.red);
//        g.setFont(Font.);
        g.drawChars(poi, 0, 1, getTextX(1), getTextY(9));
        g.drawChars(poi, 1, 1, getTextX(10), getTextY(3));
        g.drawChars(poi, 2, 1, getTextX(4), getTextY(13));
        g.drawChars(poi, 3, 1, getTextX(13), getTextY(14));
        g.drawChars(poi, 4, 1, getTextX(2), getTextY(2));
        g.drawChars(poi, 5, 1, getTextX(12), getTextY(1));
        g.drawChars(poi, 6, 1, getTextX(14), getTextY(6));
        g.drawChars(poi, 7, 1, getTextX(10), getTextY(9));
    }
    
    private int getTextX(int column) {
        return 40*column+15;
    }
    
    private int getTextY(int row) {
        return 40*row+20;
    }
    
    public void showPath(ArrayList<WorldCell> path) {
        this.path = path;
        repaint();
//        this.path = null;
    }
    
    private void drawPath(Graphics g) {
        if (path == null) {
            return;
        }
        
        for (WorldCell cell : path) {
            int cellX = cell.getX();
            int cellY = cell.getY();
            
            y = cellX*40 + 10;
            x = cellY*40 + 10;
            g.setColor(new Color(0xDC143C));
            g.fillRect(x, y, 20, 20);
            
            g.setColor(Color.yellow);
            
            int terrainCost = worldCosts[cellX][cellY];
            
            if (terrainCost < 10){
                char[] letter = new char[1];
                letter[0] = (char) (48 + terrainCost);
                g.drawChars(letter, 0, 1, 40*cellY+20, 40*cellX+20);
            } else {
                char[] letter = new char[2];
                letter[1] = (char) (48 + terrainCost%10);
                letter[0] = (char) (48 + ((terrainCost-(terrainCost%10))%100)/10 );
                g.drawChars(letter, 0, 2, 40*cellY+10, 40*cellX+20);
            }
        }
        
        x = y = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawWorld(g);
        drawPath(g);
    }
}
