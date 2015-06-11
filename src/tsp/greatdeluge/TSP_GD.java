package tsp.greatdeluge;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import tsp.City;
import tsp.Tour;
import tsp.TourManager;

import javax.swing.*;
import java.awt.*;
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
 Set decreasing rate ?B = ((f(SolGD)�Optimalrate)/(NumOfIteGD);
 Set iteration ? 0;
 Set not_improving_counter ? 0, not_improving_ length_GDA;
 do while (iteration < NumOfIteGD)
     Apply neighbourhood structure Ni where i ? {1,�,K} on
     SolGD,TempSolGDi;
     Calculate cost function f(TempSolGDi);
     Find the best solution among TempSolGDi where i ? {1,�,K} call new
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

    private static String Title = "Great Deluge Graph";
    private static XYSeries series = new XYSeries(Title);
    private static XYSeries series2 = new XYSeries("Level");

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

        //Set optimal ra0te of final solution, Optimalrate;
        double Optimalrate = 7150;

        //Set number of iterations, NumOfIteGD;
        int NumOfIteGD = 1000;

        //Set initial level: level ? f(SolGD);
        double level = fSolGD;

        //Set decreasing rate ?B = ((f(SolGD)�Optimalrate)/(NumOfIteGD);
        double decreasingRateB = (fSolGD-Optimalrate)/NumOfIteGD;

        //Set iteration ? 0;
        int iteration = 0;

        //Set not_improving_counter ? 0, not_improving_length_GDA;
        int not_improving_counter = 0; int not_improving_length_GDA = 3;

        String format = "|%1$-10s|%2$-15s|%3$-15s|%3$-15s|%4$-50s|\n";
        System.out.format(format, "Iteration","S\'", "Best","Level", "Path");
        System.out.format(format, "init",fSolbestGD+"KM", fSolbestGD+"KM",level, SolbestGD);

        while(iteration < NumOfIteGD) {//do while (iteration < NumOfIteGD)
            String stat = "";
            //    Apply neighbourhood structure Ni where i ? {1,�,K} on SolGD,TempSolGDi;
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

            //Find the best solution among TempSolGDi where i ? {1,�,K} call new solution SolGD*;
            if(fSolGDprime < fSolbestGD && fSolGDprime<=level ) {//if (f(SolGD*) < f(SolbestGD))
                SolGD = TempSolGDi;         //    SolGD ? SolGD*;
                SolbestGD = SolGD;          //SolbestGD ? SolGD*;
                not_improving_counter = 0;  //not_improving_counter ? 0;
                series.add(iteration+1,SolbestGD.getDistance());
                stat = "(AB)";
            }else {
                if (fSolGD <= level) {    // else if (f(SolGD*)? level
                    SolGD = TempSolGDi;//SolGD ? SolGD*;
                    not_improving_counter = 0;//not_improving_counter ? 0;
                    stat = "(A )";
                    series.add(iteration+1,TempSolGDi.getDistance());
                } else {//else
                    not_improving_counter++;//not_improving_counter++;
                    if (not_improving_counter == not_improving_length_GDA) {//if (not_improving_counter == not_improving_length_GDA)
                        level = level; //    level= level + random(0,3);
                    }
                    stat = "(R )";
                    //series.add(iteration+1,TempSolGDi.getDistance());
                }
            }
            level = level - decreasingRateB; //level = level - ?B;
            series2.add(iteration+1,level);


            System.out.format(format, iteration, TempSolGDi.getDistance() + "KM" + stat, SolbestGD.getDistance() + "KM", level,TempSolGDi);

            //Increase iteration by 1;

            iteration++;
        } //end do;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                display();
            }
        });
    }

    private static void display() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset2.addSeries(series2);

        NumberAxis domain = new NumberAxis("Iteration");
        NumberAxis range = new NumberAxis("Distance");
        XYSplineRenderer r = new XYSplineRenderer(1);

        XYPlot xyplot = new XYPlot(dataset, domain, range, r);
        xyplot.setDataset(0,dataset);
        xyplot.setDataset(1,dataset2);

        xyplot.getRenderer().setSeriesPaint(1, new Color(0x00, 0x00, 0x00));

        JFreeChart chart = new JFreeChart(xyplot);
        ChartPanel chartPanel = new ChartPanel(chart){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        JFrame frame = new JFrame(Title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
