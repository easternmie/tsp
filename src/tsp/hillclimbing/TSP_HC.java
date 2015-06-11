package tsp.hillclimbing;

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

public class TSP_HC {

    private static String Title = "Hill Climbing Graph";
    private static XYSeries series = new XYSeries(Title);

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

        int maxgeneration = 100;
        //1) get initial solution
        Tour initialsolution = new Tour();
        initialsolution.generateIndividual();
        int bestsolution = initialsolution.getDistance();

        String format = "|%1$-10s|%2$-10s|%3$-10s|%4$-50s|\n";
        System.out.format(format, "Iteration","S\'", "Best", "Path");
        System.out.format(format, "init",bestsolution+"KM", bestsolution+"KM", initialsolution);

        //2) climb until get better solution. break if candidate solution is same/worse or until maxgeneration
        //series.add(1,bestsolution);
        for(int i=0;i<100;i++){
            //initialsolution.generateIndividual();
            //int newcs = initialsolution.getDistance();
            Tour TempSolGDi = new Tour(initialsolution.getTour());

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


            if(fTempSolGDi<bestsolution){
                bestsolution = fTempSolGDi;
                System.out.format(format,i+1 ,bestsolution+"KM", bestsolution+"KM", initialsolution);

            }else{
                System.out.format(format,(i+1)+"(RS)" ,fTempSolGDi+"KM", bestsolution+"KM", initialsolution);
                break;
                //initialsolution.generateIndividual();   //random restart
            }
            series.add(i+1,fTempSolGDi);
        }

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
        NumberAxis domain = new NumberAxis("Distance");
        NumberAxis range = new NumberAxis("Iteration");
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
