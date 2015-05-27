/*
* TSP_GA.java
* Create a tour and evolve a solution
*/

package tsp.geneticalgo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import tsp.City;
import tsp.TourManager;

import javax.swing.*;

public class TSP_GA {

    private static String Title = "Genetic Algorithm Graph";
    private static XYSeries series = new XYSeries(Title);

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


        // Evolve population for 10 generations
        pop = GA.evolvePopulation(pop);
        System.out.format(format, "init",pop.getFittest().getDistance()+"KM", pop.getFittest().getDistance()+"KM", pop.getFittest());
        int thebest = pop.getFittest().getDistance();
        for (int i = 0; i < 10; i++) {
            Population pop2 = GA.evolvePopulation(pop);
            int newcs = pop2.getFittest().getDistance();
            if(newcs<thebest){
                thebest = newcs;
            }
            System.out.format(format,i+1 ,thebest+"KM", newcs+"KM", pop2.getFittest());
            pop = pop2;

            series.add(i+1,newcs);
        }

        // Print final results
        //System.out.println("Finished");
        //System.out.println("Final distance: " + pop.getFittest().getDistance());
        //System.out.println("Solution:");
        //System.out.println(pop.getFittest());

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                display();
            }
        });

    }

    private static void display() {


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        NumberAxis domain = new NumberAxis("Generation");
        NumberAxis range = new NumberAxis("Distance");
        XYSplineRenderer r = new XYSplineRenderer(3);
        XYPlot xyplot = new XYPlot(dataset, domain, range, r);
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