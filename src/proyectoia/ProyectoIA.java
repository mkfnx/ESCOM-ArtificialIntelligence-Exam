package proyectoia;

import javax.swing.SwingUtilities;

/**
 *
 * @author Miguel
 */
public class ProyectoIA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                WorldGUI worldGUI = new WorldGUI();
                worldGUI.setVisible(true);
            }
        });
    }
}
