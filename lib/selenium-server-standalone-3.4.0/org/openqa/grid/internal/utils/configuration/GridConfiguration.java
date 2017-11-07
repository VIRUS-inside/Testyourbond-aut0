package org.openqa.grid.internal.utils.configuration;

import com.beust.jcommander.Parameter;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Servlet;
import org.openqa.grid.internal.utils.configuration.converters.CustomConverter;

































class GridConfiguration
  extends StandaloneConfiguration
{
  @Expose
  @Parameter(names={"-cleanUpCycle"}, description="<Integer> in ms : specifies how often the hub will poll running proxies for timed-out (i.e. hung) threads. Must also specify \"timeout\" option")
  public Integer cleanUpCycle;
  @Expose
  @Parameter(names={"-custom"}, description="<String> : comma separated key=value pairs for custom grid extensions. NOT RECOMMENDED -- may be deprecated in a future revision. Example: -custom myParamA=Value1,myParamB=Value2", converter=CustomConverter.class)
  public Map<String, String> custom = new HashMap();
  







  @Expose
  @Parameter(names={"-host"}, description="<String> IP or hostname : usually determined automatically. Most commonly useful in exotic network configurations (e.g. network with VPN)")
  public String host;
  







  @Expose
  @Parameter(names={"-maxSession"}, description="<Integer> max number of tests that can run at the same time on the node, irrespective of the browser used")
  public Integer maxSession;
  







  @Expose
  @Parameter(names={"-servlet", "-servlets"}, description="<String> : list of extra servlets the grid (hub or node) will make available. Specify multiple on the command line: -servlet tld.company.ServletA -servlet tld.company.ServletB. The servlet must exist in the path: /grid/admin/ServletA /grid/admin/ServletB")
  public List<String> servlets = new ArrayList();
  






  @Expose
  @Parameter(names={"-withoutServlet", "-withoutServlets"}, description="<String> : list of default (hub or node) servlets to disable. Advanced use cases only. Not all default servlets can be disabled. Specify multiple on the command line: -withoutServlet tld.company.ServletA -withoutServlet tld.company.ServletB")
  public List<String> withoutServlets = new ArrayList();
  







  GridConfiguration() {}
  






  public void merge(GridConfiguration other)
  {
    if (other == null) {
      return;
    }
    super.merge(other);
    

    if (isMergeAble(cleanUpCycle, cleanUpCycle)) {
      cleanUpCycle = cleanUpCycle;
    }
    if (isMergeAble(custom, custom)) {
      if (custom == null) {
        custom = new HashMap();
      }
      custom.putAll(custom);
    }
    if ((isMergeAble(maxSession, maxSession)) && 
      (maxSession.intValue() > 0)) {
      maxSession = maxSession;
    }
    if (isMergeAble(servlets, servlets)) {
      servlets = servlets;
    }
    if (isMergeAble(withoutServlets, withoutServlets)) {
      withoutServlets = withoutServlets;
    }
  }
  



  public boolean isWithOutServlet(Class<? extends Servlet> servlet)
  {
    if ((withoutServlets != null) && (servlet != null)) {} return 
    
      withoutServlets.contains(servlet.getCanonicalName());
  }
  
  public String toString(String format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString(format));
    sb.append(toString(format, "cleanUpCycle", cleanUpCycle));
    sb.append(toString(format, "custom", custom));
    sb.append(toString(format, "host", host));
    sb.append(toString(format, "maxSession", maxSession));
    sb.append(toString(format, "servlets", servlets));
    sb.append(toString(format, "withoutServlets", withoutServlets));
    return sb.toString();
  }
}
