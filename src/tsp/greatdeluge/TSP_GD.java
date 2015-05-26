package tsp.greatdeluge;

import tsp.City;
import tsp.Tour;
import tsp.TourManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * baca ni==> https://github.com/UniTime/cpsolver/blob/39d82e7e40423529d8359bf1f4a8cab4888fa0ee/src/org/cpsolver/ifs/algorithms/GreatDeluge.java
 * ni pun baca =>https://github.com/awidegreen/pathfinder/blob/master/src/net/sf/pathfinder/algo/GreatDelugeAlgorithm.java
 * http://www.cs.qub.ac.uk/~B.McCollum/publications/MIC2009.pdf
 SolGD ? Sol;
 SolbestGD ? Sol
 f(SolGD) ? f(Sol);
 f(SolbestGD)? f(Sol)
 Set optimal rate of final solution, Optimalrate;
 Set number of iterations, NumOfIteGD;
 Set initial level: level ? f(SolGD);
 Set decreasing rate ?B = ((f(SolGD)–Optimalrate)/(NumOfIteGD);
 Set iteration ? 0;
 Set not_improving_counter ? 0, not_improving_ length_GDA;
 do while (iteration < NumOfIteGD)
     Apply neighbourhood structure Ni where i ? {1,…,K} on
     SolGD,TempSolGDi;
     Calculate cost function f(TempSolGDi);
     Find the best solution among TempSolGDi where i ? {1,…,K} call new
     solution SolGD*;
     if (f(SolGD*) < f(SolbestGD))
         SolGD ? SolGD*;
         SolbestGD ? SolGD*;
         not_improving_counter ? 0;
         level = level - ?B;
     else
         if (f(SolGD*)? level)
             SolGD ? SolGD*;
             not_improving_counter ? 0;
         else
            not_improving_counter++;
            if (not_improving_counter == not_improving_length_GDA)
                level= level + random(0,3);
     Increase iteration by 1;
 end do;
 return SolbestGD;
 */
public class TSP_GD {
    public static void main(String[] args) throws Exception{
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

        //SolGD ? Sol;
        Tour Sol = new Tour();
        Sol.generateIndividual();
        Tour SolGD = Sol;

        //SolbestGD ? Sol
        Tour SolbestGD = Sol;

        //f(SolGD) ? f(Sol);
        int fSolGD = Sol.getDistance();

        //f(SolbestGD)? f(Sol)
        int fSolbestGD = SolbestGD.getDistance();

        //Set optimal rate of final solution, Optimalrate;
        double Optimalrate = 0.99;

        //Set number of iterations, NumOfIteGD;
        int NumOfIteGD = 1000;

        //Set initial level: level ? f(SolGD);
        double level = fSolGD;

        //Set decreasing rate ?B = ((f(SolGD)–Optimalrate)/(NumOfIteGD);
        double decreasingRateB = (fSolGD-Optimalrate)/NumOfIteGD;

        //Set iteration ? 0;
        int iteration = 0;

        //Set not_improving_counter ? 0, not_improving_length_GDA;
        int not_improving_counter = 0; int not_improving_length_GDA = 3;

        String format = "|%1$-10s|%2$-15s|%3$-15s|%4$-50s|\n";
        System.out.format(format, "Iteration","S\'", "Best", "Path");
        System.out.format(format, "init",fSolbestGD+"KM", fSolbestGD+"KM", SolbestGD);

        while(iteration < NumOfIteGD) {//do while (iteration < NumOfIteGD)
            String stat = "";
            //    Apply neighbourhood structure Ni where i ? {1,…,K} on SolGD,TempSolGDi;
            // Create new neighbour tour
            Tour TempSolGDi = new Tour(SolGD.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (TempSolGDi.tourSize() * Math.random());
            int tourPos2 = (int) (TempSolGDi.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            City citySwap1 = TempSolGDi.getCity(tourPos1);
            City citySwap2 = TempSolGDi.getCity(tourPos2);

            // Swap them
            TempSolGDi.setCity(tourPos2, citySwap1);
            TempSolGDi.setCity(tourPos1, citySwap2);

            // Get energy of solutions
            int fSolGDprime = TempSolGDi.getDistance();

            //Calculate cost function f(TempSolGDi);
            int fTempSolGDi = TempSolGDi.getDistance();


            //Find the best solution among TempSolGDi where i ? {1,…,K} call new solution SolGD*;
            if(fSolGDprime < fSolbestGD) {//if (f(SolGD*) < f(SolbestGD))
                SolGD = TempSolGDi; //    SolGD ? SolGD*;
                SolbestGD = SolGD; //SolbestGD ? SolGD*;
                not_improving_counter = 0;  //not_improving_counter ? 0;
                level = level - decreasingRateB; //level = level - ?B;
                stat = "(AB)";
            }else {
                if (fSolGD <= level) {    // else if (f(SolGD*)? level
                    SolGD = TempSolGDi;//SolGD ? SolGD*;
                    not_improving_counter = 0;//not_improving_counter ? 0;
                    stat = "(A )";
                } else {//else
                    not_improving_counter++;//not_improving_counter++;
                    if (not_improving_counter == not_improving_length_GDA) {//if (not_improving_counter == not_improving_length_GDA)
                        level = level + new Random().nextInt(10); //    level= level + random(0,3);
                    }
                    stat = "(R )";
                }
            }


            System.out.format(format, iteration, TempSolGDi.getDistance() + "KM" + stat, SolbestGD.getDistance() + "KM", TempSolGDi);

            //Increase iteration by 1;
            iteration++;
        } //end do;
        //return SolbestGD;




    }

}
