package proyectoia;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Miguel
 */
public class WorldGUI
        extends JFrame
        implements ActionListener {

    public static final int MO = 0;
    public static final int EA = 1;
    public static final int WA = 2;
    public static final int SA = 3;
    public static final int FO = 4;
    public static final int SW = 5;
    public static final int SN = 6;
    
    public static final int HUMAN = 0;
    public static final int MONKEY = 1;
    public static final int OCTOPUS = 2;
    public static final int CROC = 3;
    public static final int SASQUATCH = 4;
    public static final int WEREWOLF = 5;
    
    private HashMap<Integer, Integer> humanCosts = new HashMap<>();
    private HashMap<Integer, Integer> monkeyCosts = new HashMap<>();
    private HashMap<Integer, Integer> octopusCosts = new HashMap<>();
    private HashMap<Integer, Integer> crocCosts = new HashMap<>();
    private HashMap<Integer, Integer> sasquatchCosts = new HashMap<>();
    private HashMap<Integer, Integer> werewolfCosts = new HashMap<>();
    
    private int[][] worldMap = {
        {WA, WA, WA, WA, WA, WA, MO, MO, WA, WA, FO, SN, SN, SN, FO},
        {WA, MO, MO, MO, MO, MO, MO, MO, WA, WA, FO, SN, SN, SN, FO},
        {WA, MO, SA, MO, SA, WA, MO, MO, WA, WA, FO, SN, SN, SN, FO},
        {WA, SA, SA, SA, SA, WA, WA, WA, WA, WA, FO, SN, SN, SN, FO},
        {WA, WA, SA, SA, WA, WA, EA, WA, WA, WA, FO, FO, FO, FO, FO},
        {EA, FO, FO, FO, FO, WA, EA, MO, MO, WA, FO, FO, FO, MO, MO},
        {EA, FO, FO, FO, FO, WA, EA, SN, WA, WA, FO, FO, FO, FO, FO},
        {WA, FO, FO, FO, FO, WA, EA, SN, WA, MO, WA, WA, WA, WA, WA},
        {WA, EA, EA, EA, EA, EA, EA, SN, WA, MO, EA, EA, WA, WA, WA},
        {WA, SN, SN, SN, SN, SN, SN, SN, WA, MO, EA, EA, WA, WA, WA},
        {WA, WA, WA, WA, WA, WA, WA, WA, WA, MO, MO, MO, WA, WA, WA},
        {WA, WA, WA, EA, WA, WA, WA, WA, WA, MO, MO, MO, EA, EA, EA},
        {EA, EA, EA, EA, EA, EA, EA, EA, WA, WA, WA, SW, MO, WA, WA},
        {EA, EA, EA, EA, SA, SA, SA, SA, WA, WA, SW, SW, SW, SW, SW},
        {EA, EA, EA, EA, SA, EA, WA, WA, WA, WA, WA, SW, SW, SW, SW}};
    
    private int[][] worldCost = new int[15][15];
    
    private JPanel buttonsPanel = new JPanel(new FlowLayout());
    private JButton startAndStopButton = new JButton("Iniciar");
    private JButton stepButton = new JButton("Siguiente");
    private JLabel costLabel = new JLabel();
    
    private ButtonGroup charSelectionGroup = new ButtonGroup();
    private JRadioButton humanRadio = new JRadioButton("Human");
    private JRadioButton monkeyRadio = new JRadioButton("Monkey");
    private JRadioButton octopusRadio = new JRadioButton("Octopus");
    private JRadioButton crocRadio = new JRadioButton("Croc");
    private JRadioButton sasquatchRadio = new JRadioButton("Sasquatch");
    private JRadioButton werewolfRadio = new JRadioButton("Werewolf");
    private String[] startPointOptions = {"1", "2", "3"};
    private JComboBox<String> startPointCombo = new JComboBox<>(startPointOptions);
    private String[] objectiveOptions = {"Portal key", "Dark temple", "Magic stones", "Rescue friend"};
    private JComboBox<String> objectiveCombo = new JComboBox<>(objectiveOptions);
    
    private Surface surface;
    
    private AStar aStar;
    
    boolean solving = false;
    Thread solvingThread;
    boolean solvingThreadRunning = false;
    
    int solvingStage = 0;
    
    Point startPoint = null;
    Point midPoint = null;
    Point endPoint = new Point(9, 10);
    
    ArrayList<WorldCell> missionPath;
    ArrayList<WorldCell> portalPath;
    int missionCostSum;
    int portalCostSum;

    public WorldGUI() {
        initCosts();
        initUI(worldMap);

        aStar = new AStar(worldCost);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!solving) {
            if (humanRadio.isSelected()) {
                initWorld(HUMAN);
            } else if (monkeyRadio.isSelected()) {
                initWorld(MONKEY);
            } else if (octopusRadio.isSelected()) {
                initWorld(OCTOPUS);
            } else if (crocRadio.isSelected()) {
                initWorld(CROC);
            } else if (sasquatchRadio.isSelected()) {
                initWorld(SASQUATCH);
            } else if (werewolfRadio.isSelected()) {
                initWorld(WEREWOLF);
            }            

            int startPointIndex = startPointCombo.getSelectedIndex();
            switch (startPointIndex) {
                case 0:
                    startPoint = new Point(9, 1);
                    break;
                case 1:
                    startPoint = new Point(3, 10);
                    break;
                case 2:
                    startPoint = new Point(13, 4);
                    break;
            }

            int objectiveIndex = objectiveCombo.getSelectedIndex();
            switch (objectiveIndex) {
                case 0:
                    midPoint = new Point(14, 13);
                    break;
                case 1:
                    midPoint = new Point(2, 2);
                    break;
                case 2:
                    midPoint = new Point(1, 12);
                    break;
                case 3:
                    midPoint = new Point(6, 14);
                    break;
            }
            
            if (worldCost[(int)startPoint.getX()][(int)startPoint.getY()] == -1
                    || worldCost[(int)midPoint.getX()][(int)midPoint.getY()] == -1) {
                JOptionPane.showConfirmDialog(this,
                        "El personaje seleccionado no puede transitar sobre alguno de los puntos seleccionados",
                        "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            solving = true;
            enableWidgets(false);
            
            aStar.setRoute(startPoint, midPoint);
        }
        
        if (e.getSource().equals(startAndStopButton)) {
            if (!solvingThreadRunning) {
                solvingThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        do {
                            if (solvingStage == 0) {
                                missionStep();
                            } else if (solvingStage == 1) {
                                portalStep();
                            } else {
                                solvingThreadRunning = false;
                                stepButton.setEnabled(true);
                                
                                solving = false;
                                enableWidgets(true);
                                solvingStage = 0;
                            }

//                            try {
//                                Thread.sleep(300);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(WorldGUI.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                        } while (solvingThreadRunning);
                    }
                });

                stepButton.setEnabled(false);
                solvingThreadRunning = true;
                solvingThread.start();
            } else {
//                solvingThread.interrupt();
                solvingThreadRunning = false;
                stepButton.setEnabled(true);
            }
        } else if (e.getSource().equals(stepButton)) {
            
            if (solvingStage == 0) {
                missionStep();
            } else if (solvingStage == 1) {
                portalStep();
            } else  {
                solvingThreadRunning = false;
                stepButton.setEnabled(true);

                solving = false;
                enableWidgets(true);
                solvingStage = 0;
            }
        }
    }
    
    private void missionStep() {
        ArrayList<WorldCell> nextPath = aStar.step();

        if (nextPath != null && nextPath.size() > 0) {
            missionPath = nextPath;
            surface.showPath(missionPath);

            missionCostSum = 0;
            for (WorldCell pathCell : missionPath) {
                System.out.println(pathCell.getX() + ", " + pathCell.getY());
                missionCostSum += worldCost[pathCell.getX()][pathCell.getY()];
            }

            costLabel.setText(String.valueOf(missionCostSum));
        } else {
            solvingStage = 1;

            aStar.setRoute(midPoint, endPoint);
        }
    }
    
    private void portalStep() {
        portalPath = aStar.step();

        if (portalPath != null && portalPath.size() > 0) {
            portalCostSum = 0;
            for (WorldCell pathCell : portalPath) {
                if (pathCell.getX() != midPoint.getX() || pathCell.getY() != midPoint.getY()) {
                    System.out.println(pathCell.getX() + ", " + pathCell.getY());
                    portalCostSum += worldCost[pathCell.getX()][pathCell.getY()];
                }
            }
            
            portalPath.addAll(missionPath);
            surface.showPath(portalPath);

            costLabel.setText(String.valueOf(missionCostSum + portalCostSum));
        } else {
            solvingStage = 2;
        }
    }

    private void initUI(int[][] world) {
        setTitle("Proyecto IA");
        // Canvas space + widgets space
        setSize(650 + 100, 650 + 50);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        surface = new Surface(world, worldCost);

        startAndStopButton.addActionListener(this);
        stepButton.addActionListener(this);

        buttonsPanel.add(startAndStopButton);
        buttonsPanel.add(stepButton);
        buttonsPanel.add(new JLabel("Costo: "));
        buttonsPanel.add(costLabel);

        add(surface, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        charSelectionGroup.add(humanRadio);
        charSelectionGroup.add(monkeyRadio);
        charSelectionGroup.add(octopusRadio);
        charSelectionGroup.add(crocRadio);
        charSelectionGroup.add(sasquatchRadio);
        charSelectionGroup.add(werewolfRadio);
        humanRadio.setSelected(true);

        JPanel p = new JPanel();
        
        BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
        
        p.add(new JLabel("Personaje"));
        p.setLayout(bl);
        p.add(humanRadio);
        p.add(monkeyRadio);
        p.add(octopusRadio);
        p.add(crocRadio);
        p.add(sasquatchRadio);
        p.add(werewolfRadio);
        p.add(new JLabel(" "));
        
        p.add(new JLabel("Punto de inicio"));
        p.add(startPointCombo);
        p.add(new JLabel(" "));
        
        p.add(new JLabel("Objetivo"));
        p.add(objectiveCombo);
        
        add(p, BorderLayout.WEST);
    }

    private void initCosts() {
        humanCosts.put(MO, -1);
        humanCosts.put(EA, 1);
        humanCosts.put(WA, 2);
        humanCosts.put(SA, 3);
        humanCosts.put(FO, 4);
        humanCosts.put(SW, 5);
        humanCosts.put(SN, 5);

        monkeyCosts.put(MO, -1);
        monkeyCosts.put(EA, 2);
        monkeyCosts.put(WA, 4);
        monkeyCosts.put(SA, 3);
        monkeyCosts.put(FO, 1);
        monkeyCosts.put(SW, 5);
        monkeyCosts.put(SN, -1);

        octopusCosts.put(MO, -1);
        octopusCosts.put(EA, 2);
        octopusCosts.put(WA, 1);
        octopusCosts.put(SA, -1);
        octopusCosts.put(FO, 3);
        octopusCosts.put(SW, 2);
        octopusCosts.put(SN, -1);

        crocCosts.put(MO, -1);
        crocCosts.put(EA, 3);
        crocCosts.put(WA, 2);
        crocCosts.put(SA, 4);
        crocCosts.put(FO, 5);
        crocCosts.put(SW, 1);
        crocCosts.put(SN, -1);

        sasquatchCosts.put(MO, 15);
        sasquatchCosts.put(EA, 4);
        sasquatchCosts.put(WA, -1);
        sasquatchCosts.put(SA, -1);
        sasquatchCosts.put(FO, 4);
        sasquatchCosts.put(SW, 5);
        sasquatchCosts.put(SN, 3);

        werewolfCosts.put(MO, -1);
        werewolfCosts.put(EA, 1);
        werewolfCosts.put(WA, 3);
        werewolfCosts.put(SA, 4);
        werewolfCosts.put(FO, 2);
        werewolfCosts.put(SW, -1);
        werewolfCosts.put(SN, 3);
    }

    private void initWorld(int characterType) {
        HashMap<Integer, Integer> terrainCosts = null;

        switch (characterType) {
            case HUMAN:
                terrainCosts = humanCosts;
                break;
            case MONKEY:
                terrainCosts = monkeyCosts;
                break;
            case OCTOPUS:
                terrainCosts = octopusCosts;
                break;
            case CROC:
                terrainCosts = crocCosts;
                break;
            case SASQUATCH:
                terrainCosts = sasquatchCosts;
                break;
            case WEREWOLF:
                terrainCosts = werewolfCosts;
                break;
            default:
                throw new IllegalArgumentException("Unsupported character type");
        }

        for (int i = 0; i < worldCost.length; i++) {
            for (int j = 0; j < worldCost[0].length; j++) {
                worldCost[i][j] = terrainCosts.get(worldMap[i][j]);
            }
        }
    }

    private void enableWidgets(boolean enable) {
        humanRadio.setEnabled(enable);
        monkeyRadio.setEnabled(enable);
        octopusRadio.setEnabled(enable);
        crocRadio.setEnabled(enable);
        sasquatchRadio.setEnabled(enable);
        werewolfRadio.setEnabled(enable);
        
        startPointCombo.setEnabled(enable);
        objectiveCombo.setEnabled(enable);
        
        startAndStopButton.setText(enable ? "Iniciar" : "Detener");
    }
}
