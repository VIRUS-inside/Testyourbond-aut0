package org.seleniumhq.jetty9.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.HttpConstraintElement;
import javax.servlet.HttpMethodConstraintElement;
import javax.servlet.ServletSecurityElement;
import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import org.seleniumhq.jetty9.http.PathMap;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.security.Constraint;

























public class ConstraintSecurityHandler
  extends SecurityHandler
  implements ConstraintAware
{
  private static final Logger LOG = Log.getLogger(SecurityHandler.class);
  
  private static final String OMISSION_SUFFIX = ".omission";
  private static final String ALL_METHODS = "*";
  private final List<ConstraintMapping> _constraintMappings = new CopyOnWriteArrayList();
  private final Set<String> _roles = new CopyOnWriteArraySet();
  private final PathMap<Map<String, RoleInfo>> _constraintMap = new PathMap();
  private boolean _denyUncoveredMethods = false;
  
  public ConstraintSecurityHandler() {}
  
  public static Constraint createConstraint()
  {
    return new Constraint();
  }
  

  public static Constraint createConstraint(Constraint constraint)
  {
    try
    {
      return (Constraint)constraint.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException(e);
    }
  }
  










  public static Constraint createConstraint(String name, boolean authenticate, String[] roles, int dataConstraint)
  {
    Constraint constraint = createConstraint();
    if (name != null)
      constraint.setName(name);
    constraint.setAuthenticate(authenticate);
    constraint.setRoles(roles);
    constraint.setDataConstraint(dataConstraint);
    return constraint;
  }
  









  public static Constraint createConstraint(String name, HttpConstraintElement element)
  {
    return createConstraint(name, element.getRolesAllowed(), element.getEmptyRoleSemantic(), element.getTransportGuarantee());
  }
  











  public static Constraint createConstraint(String name, String[] rolesAllowed, ServletSecurity.EmptyRoleSemantic permitOrDeny, ServletSecurity.TransportGuarantee transport)
  {
    Constraint constraint = createConstraint();
    
    if ((rolesAllowed == null) || (rolesAllowed.length == 0))
    {
      if (permitOrDeny.equals(ServletSecurity.EmptyRoleSemantic.DENY))
      {

        constraint.setName(name + "-Deny");
        constraint.setAuthenticate(true);

      }
      else
      {
        constraint.setName(name + "-Permit");
        constraint.setAuthenticate(false);
      }
      
    }
    else
    {
      constraint.setAuthenticate(true);
      constraint.setRoles(rolesAllowed);
      constraint.setName(name + "-RolesAllowed");
    }
    

    constraint.setDataConstraint(transport.equals(ServletSecurity.TransportGuarantee.CONFIDENTIAL) ? 2 : 0);
    return constraint;
  }
  



  public static List<ConstraintMapping> getConstraintMappingsForPath(String pathSpec, List<ConstraintMapping> constraintMappings)
  {
    if ((pathSpec == null) || ("".equals(pathSpec.trim())) || (constraintMappings == null) || (constraintMappings.size() == 0)) {
      return Collections.emptyList();
    }
    List<ConstraintMapping> mappings = new ArrayList();
    for (ConstraintMapping mapping : constraintMappings)
    {
      if (pathSpec.equals(mapping.getPathSpec()))
      {
        mappings.add(mapping);
      }
    }
    return mappings;
  }
  









  public static List<ConstraintMapping> removeConstraintMappingsForPath(String pathSpec, List<ConstraintMapping> constraintMappings)
  {
    if ((pathSpec == null) || ("".equals(pathSpec.trim())) || (constraintMappings == null) || (constraintMappings.size() == 0)) {
      return Collections.emptyList();
    }
    List<ConstraintMapping> mappings = new ArrayList();
    for (ConstraintMapping mapping : constraintMappings)
    {

      if (!pathSpec.equals(mapping.getPathSpec()))
      {
        mappings.add(mapping);
      }
    }
    return mappings;
  }
  











  public static List<ConstraintMapping> createConstraintsWithMappingsForPath(String name, String pathSpec, ServletSecurityElement securityElement)
  {
    List<ConstraintMapping> mappings = new ArrayList();
    

    Constraint httpConstraint = null;
    ConstraintMapping httpConstraintMapping = null;
    
    if ((securityElement.getEmptyRoleSemantic() != ServletSecurity.EmptyRoleSemantic.PERMIT) || 
      (securityElement.getRolesAllowed().length != 0) || 
      (securityElement.getTransportGuarantee() != ServletSecurity.TransportGuarantee.NONE))
    {
      httpConstraint = createConstraint(name, securityElement);
      

      httpConstraintMapping = new ConstraintMapping();
      httpConstraintMapping.setPathSpec(pathSpec);
      httpConstraintMapping.setConstraint(httpConstraint);
      mappings.add(httpConstraintMapping);
    }
    


    List<String> methodOmissions = new ArrayList();
    

    Collection<HttpMethodConstraintElement> methodConstraintElements = securityElement.getHttpMethodConstraints();
    if (methodConstraintElements != null)
    {
      for (HttpMethodConstraintElement methodConstraintElement : methodConstraintElements)
      {

        Constraint methodConstraint = createConstraint(name, methodConstraintElement);
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(methodConstraint);
        mapping.setPathSpec(pathSpec);
        if (methodConstraintElement.getMethodName() != null)
        {
          mapping.setMethod(methodConstraintElement.getMethodName());
          
          methodOmissions.add(methodConstraintElement.getMethodName());
        }
        mappings.add(mapping);
      }
    }
    

    if ((methodOmissions.size() > 0) && (httpConstraintMapping != null)) {
      httpConstraintMapping.setMethodOmissions((String[])methodOmissions.toArray(new String[methodOmissions.size()]));
    }
    return mappings;
  }
  








  public List<ConstraintMapping> getConstraintMappings()
  {
    return _constraintMappings;
  }
  


  public Set<String> getRoles()
  {
    return _roles;
  }
  









  public void setConstraintMappings(List<ConstraintMapping> constraintMappings)
  {
    setConstraintMappings(constraintMappings, null);
  }
  








  public void setConstraintMappings(ConstraintMapping[] constraintMappings)
  {
    setConstraintMappings(Arrays.asList(constraintMappings), null);
  }
  










  public void setConstraintMappings(List<ConstraintMapping> constraintMappings, Set<String> roles)
  {
    _constraintMappings.clear();
    _constraintMappings.addAll(constraintMappings);
    
    if (roles == null)
    {
      roles = new HashSet();
      for (ConstraintMapping cm : constraintMappings)
      {
        String[] cmr = cm.getConstraint().getRoles();
        if (cmr != null)
        {
          for (String r : cmr)
            if (!"*".equals(r))
              roles.add(r);
        }
      }
    }
    setRoles(roles);
    
    if (isStarted())
    {
      for (ConstraintMapping mapping : _constraintMappings)
      {
        processConstraintMapping(mapping);
      }
    }
  }
  







  public void setRoles(Set<String> roles)
  {
    _roles.clear();
    _roles.addAll(roles);
  }
  







  public void addConstraintMapping(ConstraintMapping mapping)
  {
    _constraintMappings.add(mapping);
    if ((mapping.getConstraint() != null) && (mapping.getConstraint().getRoles() != null))
    {


      for (String role : mapping.getConstraint().getRoles())
      {
        if ((!"*".equals(role)) && (!"**".equals(role)))
        {
          addRole(role);
        }
      }
    }
    if (isStarted())
    {
      processConstraintMapping(mapping);
    }
  }
  






  public void addRole(String role)
  {
    boolean modified = _roles.add(role);
    if ((isStarted()) && (modified))
    {

      for (Map<String, RoleInfo> map : _constraintMap.values())
      {
        for (RoleInfo info : map.values())
        {
          if (info.isAnyRole()) {
            info.addRole(role);
          }
        }
      }
    }
  }
  



  protected void doStart()
    throws Exception
  {
    _constraintMap.clear();
    if (_constraintMappings != null)
    {
      for (ConstraintMapping mapping : _constraintMappings)
      {
        processConstraintMapping(mapping);
      }
    }
    

    checkPathsWithUncoveredHttpMethods();
    
    super.doStart();
  }
  


  protected void doStop()
    throws Exception
  {
    super.doStop();
    _constraintMap.clear();
  }
  








  protected void processConstraintMapping(ConstraintMapping mapping)
  {
    Map<String, RoleInfo> mappings = (Map)_constraintMap.get(mapping.getPathSpec());
    if (mappings == null)
    {
      mappings = new HashMap();
      _constraintMap.put(mapping.getPathSpec(), mappings);
    }
    RoleInfo allMethodsRoleInfo = (RoleInfo)mappings.get("*");
    if ((allMethodsRoleInfo != null) && (allMethodsRoleInfo.isForbidden())) {
      return;
    }
    if ((mapping.getMethodOmissions() != null) && (mapping.getMethodOmissions().length > 0))
    {
      processConstraintMappingWithMethodOmissions(mapping, mappings);
      return;
    }
    
    String httpMethod = mapping.getMethod();
    if (httpMethod == null)
      httpMethod = "*";
    RoleInfo roleInfo = (RoleInfo)mappings.get(httpMethod);
    if (roleInfo == null)
    {
      roleInfo = new RoleInfo();
      mappings.put(httpMethod, roleInfo);
      if (allMethodsRoleInfo != null)
      {
        roleInfo.combine(allMethodsRoleInfo);
      }
    }
    if (roleInfo.isForbidden()) {
      return;
    }
    
    configureRoleInfo(roleInfo, mapping);
    
    if (roleInfo.isForbidden())
    {
      if (httpMethod.equals("*"))
      {
        mappings.clear();
        mappings.put("*", roleInfo);
      }
    }
  }
  












  protected void processConstraintMappingWithMethodOmissions(ConstraintMapping mapping, Map<String, RoleInfo> mappings)
  {
    String[] omissions = mapping.getMethodOmissions();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < omissions.length; i++)
    {
      if (i > 0)
        sb.append(".");
      sb.append(omissions[i]);
    }
    sb.append(".omission");
    RoleInfo ri = new RoleInfo();
    mappings.put(sb.toString(), ri);
    configureRoleInfo(ri, mapping);
  }
  







  protected void configureRoleInfo(RoleInfo ri, ConstraintMapping mapping)
  {
    Constraint constraint = mapping.getConstraint();
    boolean forbidden = constraint.isForbidden();
    ri.setForbidden(forbidden);
    


    UserDataConstraint userDataConstraint = UserDataConstraint.get(mapping.getConstraint().getDataConstraint());
    ri.setUserDataConstraint(userDataConstraint);
    

    if (!ri.isForbidden())
    {

      boolean checked = mapping.getConstraint().getAuthenticate();
      ri.setChecked(checked);
      
      if (ri.isChecked()) {
        String role;
        if (mapping.getConstraint().isAnyRole())
        {

          for (Iterator localIterator = _roles.iterator(); localIterator.hasNext();) { role = (String)localIterator.next();
            ri.addRole(role); }
          ri.setAnyRole(true);
        }
        else if (mapping.getConstraint().isAnyAuth())
        {

          ri.setAnyAuth(true);

        }
        else
        {
          String[] newRoles = mapping.getConstraint().getRoles();
          for (String role : newRoles)
          {

            if (!_roles.contains(role))
              throw new IllegalArgumentException("Attempt to use undeclared role: " + role + ", known roles: " + _roles);
            ri.addRole(role);
          }
        }
      }
    }
  }
  















  protected RoleInfo prepareConstraintInfo(String pathInContext, Request request)
  {
    Map<String, RoleInfo> mappings = (Map)_constraintMap.match(pathInContext);
    
    if (mappings != null)
    {
      String httpMethod = request.getMethod();
      RoleInfo roleInfo = (RoleInfo)mappings.get(httpMethod);
      if (roleInfo == null)
      {

        List<RoleInfo> applicableConstraints = new ArrayList();
        

        RoleInfo all = (RoleInfo)mappings.get("*");
        if (all != null) {
          applicableConstraints.add(all);
        }
        


        for (Map.Entry<String, RoleInfo> entry : mappings.entrySet())
        {
          if ((entry.getKey() != null) && (((String)entry.getKey()).endsWith(".omission")) && (!((String)entry.getKey()).contains(httpMethod))) {
            applicableConstraints.add(entry.getValue());
          }
        }
        if ((applicableConstraints.size() == 0) && (isDenyUncoveredHttpMethods()))
        {
          roleInfo = new RoleInfo();
          roleInfo.setForbidden(true);
        }
        else if (applicableConstraints.size() == 1) {
          roleInfo = (RoleInfo)applicableConstraints.get(0);
        }
        else {
          roleInfo = new RoleInfo();
          roleInfo.setUserDataConstraint(UserDataConstraint.None);
          
          for (RoleInfo r : applicableConstraints) {
            roleInfo.combine(r);
          }
        }
      }
      
      return roleInfo;
    }
    
    return null;
  }
  
  protected boolean checkUserDataPermissions(String pathInContext, Request request, Response response, RoleInfo roleInfo)
    throws IOException
  {
    if (roleInfo == null) {
      return true;
    }
    if (roleInfo.isForbidden()) {
      return false;
    }
    UserDataConstraint dataConstraint = roleInfo.getUserDataConstraint();
    if ((dataConstraint == null) || (dataConstraint == UserDataConstraint.None)) {
      return true;
    }
    HttpConfiguration httpConfig = Request.getBaseRequest(request).getHttpChannel().getHttpConfiguration();
    
    if ((dataConstraint == UserDataConstraint.Confidential) || (dataConstraint == UserDataConstraint.Integral))
    {
      if (request.isSecure()) {
        return true;
      }
      if (httpConfig.getSecurePort() > 0)
      {
        String scheme = httpConfig.getSecureScheme();
        int port = httpConfig.getSecurePort();
        
        String url = URIUtil.newURI(scheme, request.getServerName(), port, request.getRequestURI(), request.getQueryString());
        response.setContentLength(0);
        response.sendRedirect(url);
      }
      else {
        response.sendError(403, "!Secure");
      }
      request.setHandled(true);
      return false;
    }
    

    throw new IllegalArgumentException("Invalid dataConstraint value: " + dataConstraint);
  }
  



  protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo)
  {
    return (constraintInfo != null) && (((RoleInfo)constraintInfo).isChecked());
  }
  






  protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity)
    throws IOException
  {
    if (constraintInfo == null)
    {
      return true;
    }
    RoleInfo roleInfo = (RoleInfo)constraintInfo;
    
    if (!roleInfo.isChecked())
    {
      return true;
    }
    

    if ((roleInfo.isAnyAuth()) && (request.getUserPrincipal() != null))
    {
      return true;
    }
    

    boolean isUserInRole = false;
    for (String role : roleInfo.getRoles())
    {
      if (userIdentity.isUserInRole(role, null))
      {
        isUserInRole = true;
        break;
      }
    }
    

    if ((roleInfo.isAnyRole()) && (request.getUserPrincipal() != null) && (isUserInRole))
    {
      return true;
    }
    

    if (isUserInRole)
    {
      return true;
    }
    
    return false;
  }
  


  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpBeans(out, indent, new Collection[] {
      Collections.singleton(getLoginService()), 
      Collections.singleton(getIdentityService()), 
      Collections.singleton(getAuthenticator()), 
      Collections.singleton(_roles), _constraintMap
      .entrySet() });
  }
  





  public void setDenyUncoveredHttpMethods(boolean deny)
  {
    _denyUncoveredMethods = deny;
  }
  


  public boolean isDenyUncoveredHttpMethods()
  {
    return _denyUncoveredMethods;
  }
  






  public boolean checkPathsWithUncoveredHttpMethods()
  {
    Set<String> paths = getPathsWithUncoveredHttpMethods();
    if ((paths != null) && (!paths.isEmpty()))
    {
      for (String p : paths)
        LOG.warn("{} has uncovered http methods for path: {}", new Object[] { ContextHandler.getCurrentContext(), p });
      if (LOG.isDebugEnabled())
        LOG.debug(new Throwable());
      return true;
    }
    return false;
  }
  











  public Set<String> getPathsWithUncoveredHttpMethods()
  {
    if (_denyUncoveredMethods) {
      return Collections.emptySet();
    }
    Set<String> uncoveredPaths = new HashSet();
    
    for (Iterator localIterator1 = _constraintMap.keySet().iterator(); localIterator1.hasNext();) { path = (String)localIterator1.next();
      
      methodMappings = (Map)_constraintMap.get(path);
      



      if (methodMappings.get("*") == null)
      {

        hasOmissions = omissionsExist(path, methodMappings);
        
        for (String method : methodMappings.keySet())
        {
          if (method.endsWith(".omission"))
          {
            Set<String> omittedMethods = getOmittedMethods(method);
            for (String m : omittedMethods)
            {
              if (!methodMappings.containsKey(m)) {
                uncoveredPaths.add(path);
              }
              
            }
            
          }
          else if (!hasOmissions)
          {
            uncoveredPaths.add(path);
          } } } }
    String path;
    Map<String, RoleInfo> methodMappings;
    boolean hasOmissions;
    return uncoveredPaths;
  }
  









  protected boolean omissionsExist(String path, Map<String, RoleInfo> methodMappings)
  {
    if (methodMappings == null)
      return false;
    boolean hasOmissions = false;
    for (String m : methodMappings.keySet())
    {
      if (m.endsWith(".omission"))
        hasOmissions = true;
    }
    return hasOmissions;
  }
  









  protected Set<String> getOmittedMethods(String omission)
  {
    if ((omission == null) || (!omission.endsWith(".omission"))) {
      return Collections.emptySet();
    }
    String[] strings = omission.split("\\.");
    Set<String> methods = new HashSet();
    for (int i = 0; i < strings.length - 1; i++)
      methods.add(strings[i]);
    return methods;
  }
}
