package org.openqa.selenium.html5;






public class Location
{
  private final double latitude;
  




  private final double longitude;
  




  private final double altitude;
  




  public Location(double latitude, double longitude, double altitude)
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
  }
  
  public double getLatitude() {
    return latitude;
  }
  
  public double getLongitude() {
    return longitude;
  }
  
  public double getAltitude() {
    return altitude;
  }
  
  public String toString()
  {
    return String.format("Latitude: %s, Longitude: %s, Altitude: %s", new Object[] {
      Double.valueOf(latitude), Double.valueOf(longitude), Double.valueOf(altitude) });
  }
}
