package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.StringUtils;





























public class MSXMLJavaScriptEnvironment
{
  private final MSXMLConfiguration config_;
  private Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes_;
  
  public MSXMLJavaScriptEnvironment(BrowserVersion browserVersion)
    throws Exception
  {
    config_ = MSXMLConfiguration.getInstance(browserVersion);
    
    Map<String, ScriptableObject> prototypesPerJSName = new HashMap();
    Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes = new HashMap();
    


    for (ClassConfiguration config : config_.getAll()) {
      ScriptableObject prototype = configureClass(config);
      if (config.isJsObject()) {
        prototypes.put(config.getHostClass(), prototype);
      }
      prototypesPerJSName.put(config.getHostClass().getSimpleName(), prototype);
    }
    


    for (Map.Entry<String, ScriptableObject> entry : prototypesPerJSName.entrySet()) {
      String name = (String)entry.getKey();
      ClassConfiguration config = config_.getClassConfiguration(name);
      Scriptable prototype = (Scriptable)entry.getValue();
      if (prototype.getPrototype() != null) {
        prototype = prototype.getPrototype();
      }
      if (!StringUtils.isEmpty(config.getExtendedClassName())) {
        Scriptable parentPrototype = (Scriptable)prototypesPerJSName.get(config.getExtendedClassName());
        prototype.setPrototype(parentPrototype);
      }
    }
    




    prototypes_ = prototypes;
  }
  








  private static ScriptableObject configureClass(ClassConfiguration config)
    throws InstantiationException, IllegalAccessException
  {
    Class<?> jsHostClass = config.getHostClass();
    ScriptableObject prototype = (ScriptableObject)jsHostClass.newInstance();
    

    configureConstantsPropertiesAndFunctions(config, prototype);
    
    return prototype;
  }
  







  private static void configureConstantsPropertiesAndFunctions(ClassConfiguration config, ScriptableObject scriptable)
  {
    configureConstants(config, scriptable);
    

    Map<String, ClassConfiguration.PropertyInfo> propertyMap = config.getPropertyMap();
    ClassConfiguration.PropertyInfo info; for (String propertyName : propertyMap.keySet()) {
      info = (ClassConfiguration.PropertyInfo)propertyMap.get(propertyName);
      Method readMethod = info.getReadMethod();
      Method writeMethod = info.getWriteMethod();
      scriptable.defineProperty(propertyName, null, readMethod, writeMethod, 0);
    }
    
    int attributes = 2;
    
    for (Object functionInfo : config.getFunctionEntries()) {
      String functionName = (String)((Map.Entry)functionInfo).getKey();
      Method method = (Method)((Map.Entry)functionInfo).getValue();
      FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
      scriptable.defineProperty(functionName, functionObject, 2);
    }
  }
  
  private static void configureConstants(ClassConfiguration config, ScriptableObject scriptable)
  {
    for (ClassConfiguration.ConstantInfo constantInfo : config.getConstants()) {
      scriptable.defineProperty(constantInfo.getName(), constantInfo.getValue(), 
        5);
    }
  }
  





  public Class<? extends MSXMLScriptable> getJavaScriptClass(Class<?> c)
  {
    return (Class)config_.getDomJavaScriptMapping().get(c);
  }
  




  public Scriptable getPrototype(Class<? extends SimpleScriptable> jsClass)
  {
    return (Scriptable)prototypes_.get(jsClass);
  }
}
