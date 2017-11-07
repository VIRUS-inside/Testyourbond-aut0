package org.eclipse.jetty.util;







public class Jetty
{
  public static final String VERSION;
  






  private Jetty() {}
  






  static
  {
    Package pkg = Jetty.class.getPackage();
    if ((pkg != null) && 
      ("Eclipse.org - Jetty".equals(pkg.getImplementationVendor())) && 
      (pkg.getImplementationVersion() != null)) {
      VERSION = pkg.getImplementationVersion();
    } else
      VERSION = System.getProperty("jetty.version", "9.4.z-SNAPSHOT"); }
  
  public static final String POWERED_BY = "<a href=\"http://eclipse.org/jetty\">Powered by Jetty:// " + VERSION + "</a>";
  

  public static final boolean STABLE = !VERSION.matches("^.*\\.(RC|M)[0-9]+$");
}
