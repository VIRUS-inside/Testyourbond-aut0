package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;






















































public class LogFactoryImpl
  extends LogFactory
{
  private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
  private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
  private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
  private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
  private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
  private static final int PKG_LEN = "org.apache.commons.logging.impl.".length();
  public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
  protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
  public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
  public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
  public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
  
  public LogFactoryImpl()
  {
    initDiagnostics();
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Instance created.");
    }
  }
  





































































  private static final String[] classesToDiscover = { "org.apache.commons.logging.impl.Log4JLogger", "org.apache.commons.logging.impl.Jdk14Logger", "org.apache.commons.logging.impl.Jdk13LumberjackLogger", "org.apache.commons.logging.impl.SimpleLog" };
  











  private boolean useTCCL = true;
  



  private String diagnosticPrefix;
  



  protected Hashtable attributes = new Hashtable();
  




  protected Hashtable instances = new Hashtable();
  





  private String logClassName;
  





  protected Constructor logConstructor = null;
  



  protected Class[] logConstructorSignature = { String.class };
  




  protected Method logMethod = null;
  



  protected Class[] logMethodSignature = { LogFactory.class };
  




  private boolean allowFlawedContext;
  




  private boolean allowFlawedDiscovery;
  




  private boolean allowFlawedHierarchy;
  




  public Object getAttribute(String name)
  {
    return attributes.get(name);
  }
  




  public String[] getAttributeNames()
  {
    return (String[])attributes.keySet().toArray(new String[attributes.size()]);
  }
  







  public Log getInstance(Class clazz)
    throws LogConfigurationException
  {
    return getInstance(clazz.getName());
  }
  















  public Log getInstance(String name)
    throws LogConfigurationException
  {
    Log instance = (Log)instances.get(name);
    if (instance == null) {
      instance = newInstance(name);
      instances.put(name, instance);
    }
    return instance;
  }
  








  public void release()
  {
    logDiagnostic("Releasing all known loggers");
    instances.clear();
  }
  





  public void removeAttribute(String name)
  {
    attributes.remove(name);
  }
  























  public void setAttribute(String name, Object value)
  {
    if (logConstructor != null) {
      logDiagnostic("setAttribute: call too late; configuration already performed.");
    }
    
    if (value == null) {
      attributes.remove(name);
    } else {
      attributes.put(name, value);
    }
    
    if (name.equals("use_tccl")) {
      useTCCL = ((value != null) && (Boolean.valueOf(value.toString()).booleanValue()));
    }
  }
  










  protected static ClassLoader getContextClassLoader()
    throws LogConfigurationException
  {
    return LogFactory.getContextClassLoader();
  }
  



  protected static boolean isDiagnosticsEnabled()
  {
    return LogFactory.isDiagnosticsEnabled();
  }
  




  protected static ClassLoader getClassLoader(Class clazz)
  {
    return LogFactory.getClassLoader(clazz);
  }
  






















  private void initDiagnostics()
  {
    Class clazz = getClass();
    ClassLoader classLoader = getClassLoader(clazz);
    String classLoaderName;
    try { String classLoaderName;
      if (classLoader == null) {
        classLoaderName = "BOOTLOADER";
      } else {
        classLoaderName = objectId(classLoader);
      }
    } catch (SecurityException e) {
      classLoaderName = "UNKNOWN";
    }
    diagnosticPrefix = ("[LogFactoryImpl@" + System.identityHashCode(this) + " from " + classLoaderName + "] ");
  }
  






  protected void logDiagnostic(String msg)
  {
    if (isDiagnosticsEnabled()) {
      logRawDiagnostic(diagnosticPrefix + msg);
    }
  }
  



  /**
   * @deprecated
   */
  protected String getLogClassName()
  {
    if (logClassName == null) {
      discoverLogImplementation(getClass().getName());
    }
    
    return logClassName;
  }
  














  /**
   * @deprecated
   */
  protected Constructor getLogConstructor()
    throws LogConfigurationException
  {
    if (logConstructor == null) {
      discoverLogImplementation(getClass().getName());
    }
    
    return logConstructor;
  }
  


  /**
   * @deprecated
   */
  protected boolean isJdk13LumberjackAvailable()
  {
    return isLogLibraryAvailable("Jdk13Lumberjack", "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
  }
  







  /**
   * @deprecated
   */
  protected boolean isJdk14Available()
  {
    return isLogLibraryAvailable("Jdk14", "org.apache.commons.logging.impl.Jdk14Logger");
  }
  




  /**
   * @deprecated
   */
  protected boolean isLog4JAvailable()
  {
    return isLogLibraryAvailable("Log4J", "org.apache.commons.logging.impl.Log4JLogger");
  }
  



  protected Log newInstance(String name)
    throws LogConfigurationException
  {
    try
    {
      Log instance;
      

      Log instance;
      

      if (logConstructor == null) {
        instance = discoverLogImplementation(name);
      }
      else {
        Object[] params = { name };
        instance = (Log)logConstructor.newInstance(params);
      }
      
      if (logMethod != null) {
        Object[] params = { this };
        logMethod.invoke(instance, params);
      }
      
      return instance;


    }
    catch (LogConfigurationException lce)
    {

      throw lce;

    }
    catch (InvocationTargetException e)
    {
      Throwable c = e.getTargetException();
      throw new LogConfigurationException(c == null ? e : c);
    } catch (Throwable t) {
      handleThrowable(t);
      

      throw new LogConfigurationException(t);
    }
  }
  



















  private static ClassLoader getContextClassLoaderInternal()
    throws LogConfigurationException
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run() {
        return LogFactoryImpl.access$000();
      }
    });
  }
  








  private static String getSystemProperty(String key, String def)
    throws SecurityException
  {
    (String)AccessController.doPrivileged(new PrivilegedAction() { private final String val$key;
      private final String val$def;
      
      public Object run() { return System.getProperty(val$key, val$def); }
    });
  }
  






  private ClassLoader getParentClassLoader(ClassLoader cl)
  {
    try
    {
      (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
        private final ClassLoader val$cl;
        
        public Object run() { return val$cl.getParent(); }
      });
    }
    catch (SecurityException ex) {
      logDiagnostic("[SECURITY] Unable to obtain parent classloader"); }
    return null;
  }
  






  private boolean isLogLibraryAvailable(String name, String classname)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Checking for '" + name + "'.");
    }
    try {
      Log log = createLogFromClass(classname, getClass().getName(), false);
      



      if (log == null) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("Did not find '" + name + "'.");
        }
        return false;
      }
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Found '" + name + "'.");
      }
      return true;
    }
    catch (LogConfigurationException e) {
      if (isDiagnosticsEnabled())
        logDiagnostic("Logging system '" + name + "' is available but not useable.");
    }
    return false;
  }
  











  private String getConfigurationValue(String property)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("[ENV] Trying to get configuration for item " + property);
    }
    
    Object valueObj = getAttribute(property);
    if (valueObj != null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] Found LogFactory attribute [" + valueObj + "] for " + property);
      }
      return valueObj.toString();
    }
    
    if (isDiagnosticsEnabled()) {
      logDiagnostic("[ENV] No LogFactory attribute found for " + property);
    }
    



    try
    {
      String value = getSystemProperty(property, null);
      if (value != null) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[ENV] Found system property [" + value + "] for " + property);
        }
        return value;
      }
      
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] No system property found for property " + property);
      }
    } catch (SecurityException e) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] Security prevented reading system property " + property);
      }
    }
    
    if (isDiagnosticsEnabled()) {
      logDiagnostic("[ENV] No configuration defined for item " + property);
    }
    
    return null;
  }
  



  private boolean getBooleanConfiguration(String key, boolean dflt)
  {
    String val = getConfigurationValue(key);
    if (val == null) {
      return dflt;
    }
    return Boolean.valueOf(val).booleanValue();
  }
  






  private void initConfiguration()
  {
    allowFlawedContext = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedContext", true);
    allowFlawedDiscovery = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedDiscovery", true);
    allowFlawedHierarchy = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedHierarchy", true);
  }
  








  private Log discoverLogImplementation(String logCategory)
    throws LogConfigurationException
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Discovering a Log implementation...");
    }
    
    initConfiguration();
    
    Log result = null;
    

    String specifiedLogClassName = findUserSpecifiedLogClassName();
    
    if (specifiedLogClassName != null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Attempting to load user-specified log class '" + specifiedLogClassName + "'...");
      }
      

      result = createLogFromClass(specifiedLogClassName, logCategory, true);
      

      if (result == null) {
        StringBuffer messageBuffer = new StringBuffer("User-specified log class '");
        messageBuffer.append(specifiedLogClassName);
        messageBuffer.append("' cannot be found or is not useable.");
        


        informUponSimilarName(messageBuffer, specifiedLogClassName, "org.apache.commons.logging.impl.Log4JLogger");
        informUponSimilarName(messageBuffer, specifiedLogClassName, "org.apache.commons.logging.impl.Jdk14Logger");
        informUponSimilarName(messageBuffer, specifiedLogClassName, "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
        informUponSimilarName(messageBuffer, specifiedLogClassName, "org.apache.commons.logging.impl.SimpleLog");
        throw new LogConfigurationException(messageBuffer.toString());
      }
      
      return result;
    }
    




























    if (isDiagnosticsEnabled()) {
      logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
    }
    

    for (int i = 0; (i < classesToDiscover.length) && (result == null); i++) {
      result = createLogFromClass(classesToDiscover[i], logCategory, true);
    }
    
    if (result == null) {
      throw new LogConfigurationException("No suitable Log implementation");
    }
    

    return result;
  }
  







  private void informUponSimilarName(StringBuffer messageBuffer, String name, String candidate)
  {
    if (name.equals(candidate))
    {

      return;
    }
    



    if (name.regionMatches(true, 0, candidate, 0, PKG_LEN + 5)) {
      messageBuffer.append(" Did you mean '");
      messageBuffer.append(candidate);
      messageBuffer.append("'?");
    }
  }
  






  private String findUserSpecifiedLogClassName()
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
    }
    String specifiedClass = (String)getAttribute("org.apache.commons.logging.Log");
    
    if (specifiedClass == null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
      }
      
      specifiedClass = (String)getAttribute("org.apache.commons.logging.log");
    }
    
    if (specifiedClass == null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
      }
      try
      {
        specifiedClass = getSystemProperty("org.apache.commons.logging.Log", null);
      } catch (SecurityException e) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("No access allowed to system property 'org.apache.commons.logging.Log' - " + e.getMessage());
        }
      }
    }
    

    if (specifiedClass == null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
      }
      try
      {
        specifiedClass = getSystemProperty("org.apache.commons.logging.log", null);
      } catch (SecurityException e) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("No access allowed to system property 'org.apache.commons.logging.log' - " + e.getMessage());
        }
      }
    }
    




    if (specifiedClass != null) {
      specifiedClass = specifiedClass.trim();
    }
    
    return specifiedClass;
  }
  
















  private Log createLogFromClass(String logAdapterClassName, String logCategory, boolean affectState)
    throws LogConfigurationException
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Attempting to instantiate '" + logAdapterClassName + "'");
    }
    
    Object[] params = { logCategory };
    Log logAdapter = null;
    Constructor constructor = null;
    
    Class logAdapterClass = null;
    ClassLoader currentCL = getBaseClassLoader();
    

    for (;;)
    {
      logDiagnostic("Trying to load '" + logAdapterClassName + "' from classloader " + objectId(currentCL));
      try {
        if (isDiagnosticsEnabled())
        {




          String resourceName = logAdapterClassName.replace('.', '/') + ".class";
          URL url; URL url; if (currentCL != null) {
            url = currentCL.getResource(resourceName);
          } else {
            url = ClassLoader.getSystemResource(resourceName + ".class");
          }
          
          if (url == null) {
            logDiagnostic("Class '" + logAdapterClassName + "' [" + resourceName + "] cannot be found.");
          } else {
            logDiagnostic("Class '" + logAdapterClassName + "' was found at '" + url + "'");
          }
        }
        Class c;
        try
        {
          c = Class.forName(logAdapterClassName, true, currentCL);

        }
        catch (ClassNotFoundException originalClassNotFoundException)
        {
          String msg = originalClassNotFoundException.getMessage();
          logDiagnostic("The log adapter '" + logAdapterClassName + "' is not available via classloader " + objectId(currentCL) + ": " + msg.trim());
          






          try
          {
            c = Class.forName(logAdapterClassName);
          }
          catch (ClassNotFoundException secondaryClassNotFoundException) {
            msg = secondaryClassNotFoundException.getMessage();
            logDiagnostic("The log adapter '" + logAdapterClassName + "' is not available via the LogFactoryImpl class classloader: " + msg.trim());
            
            break;
          }
        }
        
        constructor = c.getConstructor(logConstructorSignature);
        Object o = constructor.newInstance(params);
        




        if ((o instanceof Log)) {
          logAdapterClass = c;
          logAdapter = (Log)o;
          break;
        }
        










        handleFlawedHierarchy(currentCL, c);


      }
      catch (NoClassDefFoundError e)
      {

        String msg = e.getMessage();
        logDiagnostic("The log adapter '" + logAdapterClassName + "' is missing dependencies when loaded via classloader " + objectId(currentCL) + ": " + msg.trim());
        

        break;


      }
      catch (ExceptionInInitializerError e)
      {


        String msg = e.getMessage();
        logDiagnostic("The log adapter '" + logAdapterClassName + "' is unable to initialize itself when loaded via classloader " + objectId(currentCL) + ": " + msg.trim());
        

        break;
      }
      catch (LogConfigurationException e)
      {
        throw e;
      } catch (Throwable t) {
        handleThrowable(t);
        


        handleFlawedDiscovery(logAdapterClassName, currentCL, t);
      }
      
      if (currentCL == null) {
        break;
      }
      


      currentCL = getParentClassLoader(currentCL);
    }
    
    if ((logAdapterClass != null) && (affectState))
    {
      logClassName = logAdapterClassName;
      logConstructor = constructor;
      
      try
      {
        logMethod = logAdapterClass.getMethod("setLogFactory", logMethodSignature);
        logDiagnostic("Found method setLogFactory(LogFactory) in '" + logAdapterClassName + "'");
      } catch (Throwable t) {
        handleThrowable(t);
        logMethod = null;
        logDiagnostic("[INFO] '" + logAdapterClassName + "' from classloader " + objectId(currentCL) + " does not declare optional method " + "setLogFactory(LogFactory)");
      }
      

      logDiagnostic("Log adapter '" + logAdapterClassName + "' from classloader " + objectId(logAdapterClass.getClassLoader()) + " has been selected for use.");
    }
    

    return logAdapter;
  }
  
















  private ClassLoader getBaseClassLoader()
    throws LogConfigurationException
  {
    ClassLoader thisClassLoader = getClassLoader(LogFactoryImpl.class);
    
    if (!useTCCL) {
      return thisClassLoader;
    }
    
    ClassLoader contextClassLoader = getContextClassLoaderInternal();
    
    ClassLoader baseClassLoader = getLowestClassLoader(contextClassLoader, thisClassLoader);
    

    if (baseClassLoader == null)
    {



      if (allowFlawedContext) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
        }
        




        return contextClassLoader;
      }
      
      throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
    }
    



    if (baseClassLoader != contextClassLoader)
    {




      if (allowFlawedContext) {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");

        }
        

      }
      else
      {
        throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
      }
    }
    



    return baseClassLoader;
  }
  











  private ClassLoader getLowestClassLoader(ClassLoader c1, ClassLoader c2)
  {
    if (c1 == null) {
      return c2;
    }
    
    if (c2 == null) {
      return c1;
    }
    



    ClassLoader current = c1;
    while (current != null) {
      if (current == c2) {
        return c1;
      }
      
      current = getParentClassLoader(current);
    }
    

    current = c2;
    while (current != null) {
      if (current == c1) {
        return c2;
      }
      
      current = getParentClassLoader(current);
    }
    
    return null;
  }
  

















  private void handleFlawedDiscovery(String logAdapterClassName, ClassLoader classLoader, Throwable discoveryFlaw)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Could not instantiate Log '" + logAdapterClassName + "' -- " + discoveryFlaw.getClass().getName() + ": " + discoveryFlaw.getLocalizedMessage());
      



      if ((discoveryFlaw instanceof InvocationTargetException))
      {


        InvocationTargetException ite = (InvocationTargetException)discoveryFlaw;
        Throwable cause = ite.getTargetException();
        if (cause != null) {
          logDiagnostic("... InvocationTargetException: " + cause.getClass().getName() + ": " + cause.getLocalizedMessage());
          


          if ((cause instanceof ExceptionInInitializerError)) {
            ExceptionInInitializerError eiie = (ExceptionInInitializerError)cause;
            Throwable cause2 = eiie.getException();
            if (cause2 != null) {
              StringWriter sw = new StringWriter();
              cause2.printStackTrace(new PrintWriter(sw, true));
              logDiagnostic("... ExceptionInInitializerError: " + sw.toString());
            }
          }
        }
      }
    }
    
    if (!allowFlawedDiscovery) {
      throw new LogConfigurationException(discoveryFlaw);
    }
  }
  


























  private void handleFlawedHierarchy(ClassLoader badClassLoader, Class badClass)
    throws LogConfigurationException
  {
    boolean implementsLog = false;
    String logInterfaceName = Log.class.getName();
    Class[] interfaces = badClass.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      if (logInterfaceName.equals(interfaces[i].getName())) {
        implementsLog = true;
        break;
      }
    }
    
    if (implementsLog)
    {

      if (isDiagnosticsEnabled()) {
        try {
          ClassLoader logInterfaceClassLoader = getClassLoader(Log.class);
          logDiagnostic("Class '" + badClass.getName() + "' was found in classloader " + objectId(badClassLoader) + ". It is bound to a Log interface which is not" + " the one loaded from classloader " + objectId(logInterfaceClassLoader));
        }
        catch (Throwable t)
        {
          handleThrowable(t);
          logDiagnostic("Error while trying to output diagnostics about bad class '" + badClass + "'");
        }
      }
      
      if (!allowFlawedHierarchy) {
        StringBuffer msg = new StringBuffer();
        msg.append("Terminating logging for this context ");
        msg.append("due to bad log hierarchy. ");
        msg.append("You have more than one version of '");
        msg.append(Log.class.getName());
        msg.append("' visible.");
        if (isDiagnosticsEnabled()) {
          logDiagnostic(msg.toString());
        }
        throw new LogConfigurationException(msg.toString());
      }
      
      if (isDiagnosticsEnabled()) {
        StringBuffer msg = new StringBuffer();
        msg.append("Warning: bad log hierarchy. ");
        msg.append("You have more than one version of '");
        msg.append(Log.class.getName());
        msg.append("' visible.");
        logDiagnostic(msg.toString());
      }
    }
    else {
      if (!allowFlawedDiscovery) {
        StringBuffer msg = new StringBuffer();
        msg.append("Terminating logging for this context. ");
        msg.append("Log class '");
        msg.append(badClass.getName());
        msg.append("' does not implement the Log interface.");
        if (isDiagnosticsEnabled()) {
          logDiagnostic(msg.toString());
        }
        
        throw new LogConfigurationException(msg.toString());
      }
      
      if (isDiagnosticsEnabled()) {
        StringBuffer msg = new StringBuffer();
        msg.append("[WARNING] Log class '");
        msg.append(badClass.getName());
        msg.append("' does not implement the Log interface.");
        logDiagnostic(msg.toString());
      }
    }
  }
}
