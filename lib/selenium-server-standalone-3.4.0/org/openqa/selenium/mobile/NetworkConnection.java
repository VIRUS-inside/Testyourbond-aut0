package org.openqa.selenium.mobile;














public abstract interface NetworkConnection
{
  public abstract ConnectionType getNetworkConnection();
  













  public abstract ConnectionType setNetworkConnection(ConnectionType paramConnectionType);
  













  public static class ConnectionType
  {
    public static final ConnectionType WIFI = new ConnectionType(2);
    public static final ConnectionType DATA = new ConnectionType(4);
    public static final ConnectionType AIRPLANE_MODE = new ConnectionType(1);
    public static final ConnectionType ALL = new ConnectionType(6);
    public static final ConnectionType NONE = new ConnectionType(0);
    








    private int mask = 0;
    
    public ConnectionType(Boolean wifi, Boolean data, Boolean airplaneMode) {
      if (wifi.booleanValue()) {
        mask += WIFImask;
      }
      if (data.booleanValue()) {
        mask += DATAmask;
      }
      if (airplaneMode.booleanValue()) {
        mask += AIRPLANE_MODEmask;
      }
    }
    
    public ConnectionType(int mask)
    {
      this.mask = Math.max(mask, 0);
    }
    
    public Boolean isAirplaneMode() {
      return Boolean.valueOf(mask % 2 == 1);
    }
    
    public Boolean isWifiEnabled()
    {
      return Boolean.valueOf(mask / 2 % 2 == 1);
    }
    
    public Boolean isDataEnabled()
    {
      return Boolean.valueOf(mask / 4 > 0);
    }
    
    public boolean equals(Object type)
    {
      return ((type instanceof ConnectionType)) && (mask == mask);
    }
    
    public int hashCode()
    {
      return mask;
    }
    
    public String toString()
    {
      return Integer.toString(mask);
    }
    
    public String toJson() {
      return toString();
    }
  }
}
