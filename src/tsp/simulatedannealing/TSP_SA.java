package tsp.simulatedannealing;

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
import java.util.StringTokenizer;

/**
 * Created by azrul_000 on 21/5/2015.
 */
public class TSP_SA {
    private static String Title = "Simulated Annealing Graph";
    private static XYSeries series = new XYSeries(Title);

    public static void main(String[] args) throws Exception {

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set initial temp
        double temp = 100;

        // Cooling rate
        double coolingRate = 0.03;

        // Initialize intial solution
        Tour currentSolution = new Tour();
        currentSolution.generateIndividual();
        // Set as current best
        int bestsolution = currentSolution.getDistance();
        Tour best = currentSolution;

        String format = "|%1$-20s|%2$-15s|%3$-10s|%4$-50s|\n";
        System.out.format(format, "Iteration","S\'", "Best", "Path");
        System.out.format(format, "init",bestsolution+"KM", bestsolution+"KM", currentSolution);

        double initsolution = bestsolution;
        // Loop until system has cooled
        String stat = "";
        int count = 0;
        while (temp > 1) {
            stat = "";
            // Create new neighbour tour
            Tour newSolution = new Tour(currentSolution.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            City citySwap1 = newSolution.getCity(tourPos1);
            City citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);

            // Get energy of solutions
            int currentEnergy = currentSolution.getDistance();
            int neighbourEnergy = newSolution.getDistance();

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
                stat = "(A)";
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance() < best.getDistance()) {
                best = new Tour(currentSolution.getTour());
                stat += "(B)";
            }

            // Cool system
            temp *= 1-coolingRate;
            series.add(100-temp,currentSolution.getDistance());
            //double fitness = (initsolution - newSolution.getDistance());
            System.out.format(format, temp,currentSolution.getDistance()+"KM"+stat, best.getDistance()+"KM", currentSolution);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                display();
            }
        });
    }
    // Calculate the acceptance probability
    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    private static void display() {


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        NumberAxis domain = new NumberAxis("Temprature");
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
