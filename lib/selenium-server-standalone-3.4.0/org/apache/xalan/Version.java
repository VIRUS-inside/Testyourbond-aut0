package org.apache.xalan;

import java.io.PrintStream;






































public class Version
{
  public Version() {}
  
  public static String getVersion()
  {
    return getProduct() + " " + getImplementationLanguage() + " " + getMajorVersionNum() + "." + getReleaseVersionNum() + "." + (getDevelopmentVersionNum() > 0 ? "D" + getDevelopmentVersionNum() : new StringBuffer().append("").append(getMaintenanceVersionNum()).toString());
  }
  








  public static void main(String[] argv)
  {
    System.out.println(getVersion());
  }
  



  public static String getProduct()
  {
    return "Xalan";
  }
  



  public static String getImplementationLanguage()
  {
    return "Java";
  }
  












  public static int getMajorVersionNum()
  {
    return 2;
  }
  









  public static int getReleaseVersionNum()
  {
    return 7;
  }
  









  public static int getMaintenanceVersionNum()
  {
    return 2;
  }
  
















  public static int getDevelopmentVersionNum()
  {
    try
    {
      if (new String("").length() == 0) {
        return 0;
      }
      return Integer.parseInt("");
    } catch (NumberFormatException nfe) {}
    return 0;
  }
}
