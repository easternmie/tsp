package tsp;
//source: http://www.geodatasource.com/developers/java
//usage: CalculateDistance.distance(32.9697, -96.80322, 29.46786, -98.53506)
//result: in KM

public class CalculateDistance {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return (dist  * 1.1515 * 1.609344);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
