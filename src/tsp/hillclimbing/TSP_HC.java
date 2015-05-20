package tsp.hillclimbing;

import tsp.City;
import tsp.Tour;
import tsp.TourManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by azrul_000 on 20/5/2015.
 */
public class TSP_HC {
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
        for(int i=0;i<100;i++){
            initialsolution.generateIndividual();
            int newcs = initialsolution.getDistance();
            if(newcs<bestsolution){
                bestsolution = newcs;
                System.out.format(format,i+1 ,bestsolution+"KM", bestsolution+"KM", initialsolution);
            }else{
                System.out.format(format,(i+1)+"(STOP)" ,newcs+"KM", bestsolution+"KM", initialsolution);
                break;
            }

        }

    }
}
