/*
* TSP_GA.java
* Create a tour and evolve a solution
*/

package tsp.geneticalgo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import tsp.City;
import tsp.TourManager;

public class TSP_GA {

    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("city.txt"));
            String line =  br.readLine();
            do{
                StringTokenizer st = new StringTokenizer(line, ",");
                City c = new City(st.nextToken(),
                                Double.parseDouble(st.nextToken()),
                                Double.parseDouble(st.nextToken()));
                TourManager.addCity(c);
                line = br.readLine();
            }while(line!=null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Initialize population
        Population pop = new Population(50, true);
        String format = "|%1$-10s|%2$-10s|%3$-10s|%4$-50s|\n";
        System.out.format(format, "Iteration","Accepted", "Best", "Candidate Solution");


        // Evolve population for 100 generations
        pop = GA.evolvePopulation(pop);
        System.out.format(format, "init",pop.getFittest().getDistance()+"KM", pop.getFittest().getDistance()+"KM", pop.getFittest());
        int thebest = pop.getFittest().getDistance();
        for (int i = 0; i < 100; i++) {
            Population pop2 = GA.evolvePopulation(pop);
            int newcs = pop2.getFittest().getDistance();
            if(newcs<thebest){
                thebest = newcs;
            }
            System.out.format(format,i+1 ,thebest+"KM", newcs+"KM", pop2.getFittest());
            pop = pop2;
        }

        // Print final results
        //System.out.println("Finished");
        //System.out.println("Final distance: " + pop.getFittest().getDistance());
        //System.out.println("Solution:");
        //System.out.println(pop.getFittest());
    }
}