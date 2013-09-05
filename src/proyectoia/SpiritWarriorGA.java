package proyectoia;

import java.util.Random;

/**
 *
 * @author Miguel
 */
public class SpiritWarriorGA {

    public static final int CHROMOSOME_SIZE = 7;
    public static final int POPULATION_SIZE = 7;
    public static final int MAX_COST = 4;
    private Random random = new Random(System.currentTimeMillis());

    public SpiritWarriorGA() {
    }

    private int[] createChromosome() {
        int[] chromosome = new int[CHROMOSOME_SIZE];

        for (int i = 0; i < CHROMOSOME_SIZE; i++) {
            chromosome[i] = random.nextInt(MAX_COST)+1;
        }

        return chromosome;
    }
    
    private int[][] createPopulation() {
        int[][] population = new int[POPULATION_SIZE][CHROMOSOME_SIZE];
        
        for (int i=0; i<POPULATION_SIZE; i++) {
            population[i] = createChromosome();
        }
        
        return population;
    }

    private int getFitness(int[] chromosome) {
        int fitness = 0;
        
        
        
        return fitness;
    }
}
