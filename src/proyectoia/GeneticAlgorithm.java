package proyectoia;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Miguel
 */
public class GeneticAlgorithm {

    public static final int POPULATION_SIZE = 20;
    public static final int CHROMOSOME_LENGHT = 4;
    public static final int ITERATIONS = 1000000;
    
    private ArrayList<Point[]> population;
    private ArrayList<Integer> validRows = new ArrayList<>();
    private ArrayList<Integer> validColumns = new ArrayList<>();
    
    int[][] routeCosts;
    Random random = new Random(System.currentTimeMillis());
    int maxRouteCost;
    ArrayList<Integer> chromosomesIndexes;
    Point[] bestChromosome;
    
    public GeneticAlgorithm() {
        chromosomesIndexes = new ArrayList<>();

        for (int j = 0; j < POPULATION_SIZE; j++) {
            chromosomesIndexes.add(j);
        }
        
        int[][] mRouteCosts = {
            {38, 42, 44, 51, 33, 59, 45, 42, 41, 37, 27, 35},
            {-1, 62, 67, -1, -1, -1, -1, 52, 57, -1, 16, 47},
            {-1, -1, -1, -1, -1, -1, -1, 29, -1, -1, 19, -1},
            {-1, 52, 61, -1, -1, -1 ,-1, 40, 33, -1, 34, 42},
            {-1, -1, -1, -1, 51, -1, -1, 110, -1, -1, 59, -1},
            {47, 52, 56, 44, 24, 54, -1, -1, -1, 35, 18, 43},};
        
        routeCosts = mRouteCosts;
        
//        maxRouteCost = getMaxRouteCost();
        maxRouteCost = routeCosts[0][0];
        for (int i=0; i<routeCosts.length; i++) {
            for (int j=0; j<routeCosts[0].length; j++) {
                int routeCost = routeCosts[i][j];
                if (routeCost > maxRouteCost) {
                    maxRouteCost = routeCost;
                }
            }
        }
        
        createPopulation();
//        printPopulation();
        
        for (int i=0; i<ITERATIONS; i++) {
//            printPopulation();
            
            bestChromosome = getBestChromosome();
//            System.out.println("best solution (" + population.indexOf(bestChromosome) + "): " + getFitness(bestChromosome));
//            System.out.println();
            
            population = createNewPopulation();
        }
        
        printSolution();
    }
    
    private void printSolution() {
        System.out.println("Best solution: ");
        printChromo(bestChromosome);

        for (int i = 0; i < CHROMOSOME_LENGHT; i++) {
            Point gen = bestChromosome[i];

            int row = (int) gen.getX();
            int column = (int) gen.getY();

            String character[] = new String[4];
            String mission[] = new String[4];
            String startPoint[] = new String[4];

            switch (row) {
                case 0:
                    character[i] = "Human";
                    break;
                case 1:
                    character[i] = "Monkey";
                    break;
                case 2:
                    character[i] = "Octopus";
                    break;
                case 3:
                    character[i] = "Croc";
                    break;
                case 4:
                    character[i] = "Sasquatch";
                    break;
                case 5:
                    character[i] = "Werewolf";
                    break;
            }

            switch (column) {
                case 0:
                case 1:
                case 2:
                    mission[i] = "Dark temple";
                    break;

                case 3:
                case 4:
                case 5:
                    mission[i] = "Magic stones";
                    break;

                case 6:
                case 7:
                case 8:
                    mission[i] = "Portal key";
                    break;

                case 9:
                case 10:
                case 11:
                    mission[i] = "Rescue friend";
                    break;
            }

            System.out.println(character[i] + " from " + String.valueOf((column % 3) + 1) + " to " + mission[i] + ": " + routeCosts[row][column]);
        }

        System.out.println("Cost: " + getCost(bestChromosome));
//        printChromo(bestChromosome);
    }
    
    private void printPopulation() {
        for (int i=0; i<POPULATION_SIZE; i++) {
            Point[] chromo = population.get(i);
            
            printChromo(chromo);
        }
    }
    
    private void printChromo(Point[] chromo) {
        for (int j=0; j<CHROMOSOME_LENGHT; j++) {
                Point gen = chromo[j];
                System.out.println("[" + (int)gen.getX() + "," + (int)gen.getY() + "]: " + routeCosts[(int)gen.getX()][(int)gen.getY()]);
            }
//            System.out.println("cost: " + getCost(chromo));
            System.out.println("fitness: " + getFitness(chromo));
            System.out.println();
    }
    
    private boolean createPopulation() {
        population = new ArrayList<>();
        
        for (int i=0; i<POPULATION_SIZE; i++) {
            Point[] chromosome = createChromosome();
            
            if (chromosome == null) {
                return false;
            }
            
            population.add(chromosome);
            
//            System.out.println();
        }
        
        return true;
    }
    
    private ArrayList<Point[]> createNewPopulation() {
//        population
        ArrayList<Point[]> crossedPopulation = new ArrayList<>();
        
        crossedPopulation.add(bestChromosome);
//        printChromo(bestChromosome);
        
        // Tournament selection
        for (int i=0; i<POPULATION_SIZE-1; i++) {
            Collections.shuffle(chromosomesIndexes);
            
            Point[] chromo1 = population.get(chromosomesIndexes.get(0));
            Point[] chromo2 = population.get(chromosomesIndexes.get(1));
            Point[] chromo3 = population.get(chromosomesIndexes.get(2));
            Point[] chromo4 = population.get(chromosomesIndexes.get(3));
            Point[] newChromo;
            
            if (getFitness(chromo1) > getFitness(chromo2)) {
                if (getFitness(chromo3) > getFitness(chromo4)) {
//                    System.out.println("Crossing " + chromosomesIndexes.get(0) + " with " + chromosomesIndexes.get(2));
                    newChromo = cross(chromo1, chromo3);
                }
                else {
//                    System.out.println("Crossing " + chromosomesIndexes.get(0) + " with " + chromosomesIndexes.get(3));
                    newChromo = cross(chromo1, chromo4);
                }
            } else {
                if (getFitness(chromo3) > getFitness(chromo4)) {
//                    System.out.println("Crossing " + chromosomesIndexes.get(1) + " with " + chromosomesIndexes.get(2));
                    newChromo = cross(chromo2, chromo3);
                }
                else {
//                    System.out.println("Crossing " + chromosomesIndexes.get(1) + " with " + chromosomesIndexes.get(3));
                    newChromo = cross(chromo2, chromo4);
                }
            }
            
            // Mutation probability of 1%
            if (random.nextInt(10) == 0) {
                newChromo = mutation(newChromo);
            }
//            printChromo(newChromo);
            
            crossedPopulation.add(newChromo);
        }
        
        return crossedPopulation;
    }
    
    private Point[] swappingMutation(Point[] chromosome) {
        Point[] mutatedChromo = new Point[CHROMOSOME_LENGHT];
                
        int locus1 = random.nextInt(CHROMOSOME_LENGHT);
        int locus2;
        
        do {
            locus2 = random.nextInt(CHROMOSOME_LENGHT);
        } while(locus1 == locus2);
        
        System.arraycopy(chromosome, 0, mutatedChromo, 0, CHROMOSOME_LENGHT);
        
        Point aux = new Point(mutatedChromo[locus1]);
        mutatedChromo[locus1].setLocation(mutatedChromo[locus2]);
        mutatedChromo[locus2].setLocation(aux);
        
        return mutatedChromo;
    }
    
    private Point[] mutation(Point[] chromosome) {
        Point[] mutatedChromo = new Point[CHROMOSOME_LENGHT];
        
//        System.arraycopy(chromosome, 0, mutatedChromo, 0, CHROMOSOME_LENGHT);
        
        for (int i=0; i<CHROMOSOME_LENGHT; i++) {
            Point l = chromosome[i];
            mutatedChromo[i] = new Point((int)l.getX(), (int)l.getY());
        }
        
        int locus = random.nextInt(CHROMOSOME_LENGHT);
        
        int newRow = random.nextInt(routeCosts.length);
        int newColumn = random.nextInt(routeCosts[0].length);
        mutatedChromo[locus].setLocation(newRow, newColumn);
        
//        System.out.println("mutation at [" + locus + "]: " + newRow + "," + newColumn);
        
        return mutatedChromo;
    }
    
    private Point[] cross(Point[] chromo1, Point[] chromo2) {
        Point[] newChromo = new Point[4];
        
        int crossingPoint = random.nextInt(CHROMOSOME_LENGHT-1) + 1;
//        System.out.println("crossing point: " + crossingPoint);
        
        System.arraycopy(chromo1, 0, newChromo, 0, crossingPoint);
        System.arraycopy(chromo2, crossingPoint, newChromo, crossingPoint, CHROMOSOME_LENGHT-crossingPoint);
        
        return newChromo;
    }
    
    private Point[] getBestChromosome() {
        Point[] bestChromosome;
        bestChromosome = population.get(0);
        int i;
        for (i=1; i<POPULATION_SIZE; i++) {
            Point[] chromosome = population.get(i);
            int chromoFitness = getFitness(chromosome);
            int bestChromoFitness = getFitness(bestChromosome);
            if (chromoFitness > bestChromoFitness) {
                bestChromosome = chromosome;
            }
        }
//        System.out.println("best chromo: [" + population.indexOf(bestChromosome) + "]: " + getFitness(bestChromosome));
        return bestChromosome;
    }
    
    private int getFitness(Point[] chromosome) {
        int fitness = -4;
        
        resetValidRowsAndColumns();
        for (int i = 0; i < CHROMOSOME_LENGHT; i++) {
            Point gen = chromosome[i];
            int row = (int) gen.getX();
            int column = (int) gen.getY();

            if (!validRows.contains(row)
                    || !validColumns.contains(column)
                    || routeCosts[row][column] == -1) {
                return fitness;
            }

            validRows.remove(new Integer(row));
            removeColumns(column);
            fitness++;
        }
        
        fitness = 0;
        for (int i=0; i<CHROMOSOME_LENGHT; i++) {
            Point gen = chromosome[i];
            fitness += maxRouteCost - routeCosts[(int)gen.getX()][(int)gen.getY()];
        }

        return fitness;
    }
    
    private Point[] createChromosome() {
        Point[] chromosome = new Point[CHROMOSOME_LENGHT];
        
        for (int i=0; i<CHROMOSOME_LENGHT; i++) {
            int row = random.nextInt(routeCosts.length);
            int column = random.nextInt(routeCosts[0].length);
            
            Point gen = new Point(row, column);
            chromosome[i] = gen;
        }
        
        return chromosome;
    }
    
    private void removeColumns(int column) {
        switch (column) {
            case 0:
            case 1:
            case 2:
                validColumns.remove(new Integer(0));
                validColumns.remove(new Integer(1));
                validColumns.remove(new Integer(2));
                break;

            case 3:
            case 4:
            case 5:
                validColumns.remove(new Integer(3));
                validColumns.remove(new Integer(4));
                validColumns.remove(new Integer(5));
                break;

            case 6:
            case 7:
            case 8:
                validColumns.remove(new Integer(6));
                validColumns.remove(new Integer(7));
                validColumns.remove(new Integer(8));
                break;

            case 9:
            case 10:
            case 11:
                validColumns.remove(new Integer(9));
                validColumns.remove(new Integer(10));
                validColumns.remove(new Integer(11));
                break;
        }
    }
    
    private void resetValidRowsAndColumns() {
        validRows.clear();
        for (int i=0; i<routeCosts.length; i++)  {
            validRows.add(i);
        }
        
        validColumns.clear();
        for (int i=0; i<routeCosts[0].length; i++) {
            validColumns.add(i);
        }
    }
    
    private int getCost(Point[] chromosome) {
        int cost = 0;
        for (int i=0; i<chromosome.length; i++) {
            Point gen = chromosome[i];
            cost += routeCosts[(int)gen.getX()][(int)gen.getY()];
        }
        return cost;
    }
    
    private boolean isValidChromosome(Point[] chromosome) {
        resetValidRowsAndColumns();
        
        for (int i=0; i<CHROMOSOME_LENGHT; i++) {
            Point gen = chromosome[i];
            int row = (int) gen.getX();
            int column = (int) gen.getY();
            
            if (!validRows.contains(row)
                    || !validColumns.contains(column)
                    || routeCosts[row][column] == -1) {
                return false;
            }
            
            validRows.remove(new Integer(row));
            removeColumns(column);
        }
        
        return true;
    }
    
    private boolean isValidPosition(int row, int column) {
        boolean validRow = validRows.contains(row);
        boolean validColumn = validColumns.contains(column);
        boolean validRoute = routeCosts[row][column] != -1;
        
        return validRow && validColumn && validRoute;
    }
    
    private int getValidRow(int startRow, int column) {
        int newRow = startRow;
        
        for (int i=0; i<routeCosts.length; i++) {
            newRow = startRow-i;
            if (newRow >= 0) {
                if (validRows.contains(newRow) && routeCosts[newRow][column] != -1) {
                    break;
                }
            }
            
            newRow = startRow+i;
            if (startRow+i < routeCosts.length) {
                if (validRows.contains(newRow) && routeCosts[newRow][column] != -1) {
                    break;
                }
            }
            
            newRow = startRow;
        }

        return newRow;
    }
    
    private int getValidColumn(int row, int startColumn) {
        int newColumn = startColumn;
        
        for (int j=0; j<routeCosts[0].length; j++) {
            newColumn = startColumn-j;
            if (newColumn >= 0) {
                if (validColumns.contains(newColumn) && routeCosts[row][newColumn] != -1) {
                    break;
                }
            }
            
            newColumn = startColumn+j;
            if (newColumn < routeCosts[0].length) {
                if (validColumns.contains(newColumn) && routeCosts[row][newColumn] != -1) {
                    break;
                }
            }
            
            newColumn = startColumn;
        }
        
        return newColumn;
    }
}
