package tsp;

import tsp.CalculateDistance;
public class City {
    String name;
    double lat, lon;

    // Constructs a randomly placed city
    public City(){
        this.lat = (double)(Math.random()*200);
        this.lon = (double)(Math.random()*200);
    }

    // Constructs a city at chosen x, y location
    public City(String name,double lat, double lon){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    // Gets city's  coordinate
    public String getCoordinate(){
        return this.lat+","+this.lon;
    }

    // Gets the distance to given city
    public double distanceTo(City city){
        return CalculateDistance.distance(this.lat,city.lat,this.lon,city.lon);
    }

    @Override
    public String toString(){
        return this.name;
    }
}