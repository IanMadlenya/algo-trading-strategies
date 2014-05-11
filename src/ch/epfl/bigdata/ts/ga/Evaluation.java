package ch.epfl.bigdata.ts.ga;

import ch.epfl.bigdata.ts.pattern.fitness.FitnessFunction;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evaluation extends Thread{

    public static int NUM_OF_CHROMOSOMES = 50;

    private FitnessFunction strategy;
    private List<Chromosome> chrsToEval;

    private Chromosome bestChromosome;

    public Evaluation(FitnessFunction fitnessFunction, List<Chromosome> chrs){
        this.strategy = fitnessFunction;
        this.chrsToEval = chrs;
    }

    public void run(){

        FileWriter out = null;

        try {
            out = new FileWriter(strategy.getName() + "_evaluation_" + (new Date()).getTime() + ".txt", true);


            long startTime = System.currentTimeMillis();

            List<Chromosome> evalResults = new ArrayList<Chromosome>();

            for (int i=0; i<chrsToEval.size(); i++) {
                Chromosome chr = evaluateChromosome(chrsToEval.get(i));

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                evalResults.add(chr);

                out.append("Evaluation phase" + "\n");
                out.append(chr + "\n");
                out.append("Fitness: " + chr.getFitness() + "\n");
                out.append("Number of transactions: " + chr.getNumberOfTransactions() + "\n");
                out.append("This took " + duration + " milliseconds" + "\n");
                out.append("EBD OF ITERATION #" + i + "\n\n");
            }

            double sum = 0;
            double bestFitness = evalResults.get(0).getFitness();
            bestChromosome = evalResults.get(0);

            for (int i = 0; i < evalResults.size(); i++) {
                sum += evalResults.get(i).getFitness();
                if (bestFitness < evalResults.get(i).getFitness()) {
                    bestFitness = evalResults.get(i).getFitness();
                    bestChromosome = evalResults.get(i);
                }
            }

            out.append("AVERAGE CHROMOSOME FITNESS FOR " + chrsToEval.size() + " iterations is: " + sum / evalResults.size() + "\n");

            if (out != null) out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public Chromosome evaluateChromosome(Chromosome chromosome) {

        strategy.calcFitness(chromosome, false);

        return chromosome;
    }

    public Chromosome bestChromosome(){
        return bestChromosome;
    }

}