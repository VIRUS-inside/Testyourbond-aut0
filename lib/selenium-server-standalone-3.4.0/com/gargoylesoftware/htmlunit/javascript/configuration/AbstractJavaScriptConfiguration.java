package com.gargoylesoftware.htmlunit.javascript.configuration;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

























public abstract class AbstractJavaScriptConfiguration
{
  private static final Log LOG = LogFactory.getLog(AbstractJavaScriptConfiguration.class);
  
  private static final Map<String, String> CLASS_NAME_MAP_ = new HashMap();
  

  private Map<Class<?>, Class<? extends HtmlUnitScriptable>> domJavaScriptMap_;
  

  private final Map<String, ClassConfiguration> configuration_;
  

  protected AbstractJavaScriptConfiguration(BrowserVersion browser)
  {
    configuration_ = buildUsageMap(browser);
  }
  



  protected abstract Class<? extends SimpleScriptable>[] getClasses();
  



  public Iterable<ClassConfiguration> getAll()
  {
    return configuration_.values();
  }
  
  private Map<String, ClassConfiguration> buildUsageMap(BrowserVersion browser) {
    Map<String, ClassConfiguration> classMap = new HashMap(getClasses().length);
    
    for (Class<? extends SimpleScriptable> klass : getClasses()) {
      ClassConfiguration config = getClassConfiguration(klass, browser);
      if (config != null) {
        classMap.put(config.getClassName(), config);
      }
    }
    return Collections.unmodifiableMap(classMap);
  }
  







  public static ClassConfiguration getClassConfiguration(Class<? extends HtmlUnitScriptable> klass, BrowserVersion browser)
  {
    if (browser != null) { String expectedBrowserName;
      String expectedBrowserName;
      if (browser.isIE()) {
        expectedBrowserName = "IE";
      } else { String expectedBrowserName;
        if (browser.isFirefox()) {
          expectedBrowserName = "FF";
        } else { String expectedBrowserName;
          if (browser.isEdge()) {
            expectedBrowserName = "EDGE";
          }
          else
            expectedBrowserName = "CHROME";
        } }
      float browserVersionNumeric = browser.getBrowserVersionNumeric();
      
      String hostClassName = klass.getName();
      JsxClasses jsxClasses = (JsxClasses)klass.getAnnotation(JsxClasses.class);
      if (jsxClasses != null) {
        if (klass.getAnnotation(JsxClass.class) != null) {
          throw new RuntimeException("Invalid JsxClasses/JsxClass annotation; class '" + 
            hostClassName + "' has both.");
        }
        JsxClass[] jsxClassValues = jsxClasses.value();
        if (jsxClassValues.length == 1) {
          throw new RuntimeException("No need to specify JsxClasses with a single JsxClass for " + 
            hostClassName);
        }
        Set<Class<?>> domClasses = new HashSet();
        
        boolean isJsObject = false;
        String className = null;
        for (int i = 0; i < jsxClassValues.length; i++) {
          JsxClass jsxClass = jsxClassValues[i];
          
          if ((jsxClass != null) && 
            (isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric))) {
            domClasses.add(jsxClass.domClass());
            if (jsxClass.isJSObject()) {
              isJsObject = true;
            }
            if (!jsxClass.className().isEmpty()) {
              className = jsxClass.className();
            }
          }
        }
        
        ClassConfiguration classConfiguration = 
          new ClassConfiguration(klass, (Class[])domClasses.toArray(new Class[0]), isJsObject, 
          className);
        
        process(classConfiguration, hostClassName, expectedBrowserName, browserVersionNumeric);
        return classConfiguration;
      }
      
      JsxClass jsxClass = (JsxClass)klass.getAnnotation(JsxClass.class);
      if ((jsxClass != null) && (isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric)))
      {
        Set<Class<?>> domClasses = new HashSet();
        Class<?> domClass = jsxClass.domClass();
        if ((domClass != null) && (domClass != Object.class)) {
          domClasses.add(domClass);
        }
        
        String className = jsxClass.className();
        if (className.isEmpty()) {
          className = null;
        }
        ClassConfiguration classConfiguration = 
          new ClassConfiguration(klass, (Class[])domClasses.toArray(new Class[0]), jsxClass.isJSObject(), 
          className);
        
        process(classConfiguration, hostClassName, expectedBrowserName, browserVersionNumeric);
        return classConfiguration;
      }
    }
    return null;
  }
  

  private static void process(ClassConfiguration classConfiguration, String hostClassName, String expectedBrowserName, float browserVersionNumeric)
  {
    String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);
    
    CLASS_NAME_MAP_.put(hostClassName, simpleClassName);
    Map<String, Method> allGetters = new HashMap();
    Map<String, Method> allSetters = new HashMap();
    for (Constructor<?> constructor : classConfiguration.getHostClass().getDeclaredConstructors()) {
      for (Annotation annotation : constructor.getAnnotations()) {
        if (((annotation instanceof JsxConstructor)) && 
          (isSupported(((JsxConstructor)annotation).value(), expectedBrowserName, browserVersionNumeric))) {
          classConfiguration.setJSConstructor(constructor);
        }
      }
    }
    for (Method method : classConfiguration.getHostClass().getDeclaredMethods()) {
      for (Annotation annotation : method.getAnnotations()) {
        if ((annotation instanceof JsxGetter)) {
          JsxGetter jsxGetter = (JsxGetter)annotation;
          if (isSupported(jsxGetter.value(), expectedBrowserName, browserVersionNumeric)) {
            String property;
            if (jsxGetter.propertyName().isEmpty()) {
              int prefix = method.getName().startsWith("is") ? 2 : 3;
              String property = method.getName().substring(prefix);
              property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
            }
            else {
              property = jsxGetter.propertyName();
            }
            allGetters.put(property, method);
          }
        }
        else if ((annotation instanceof JsxSetter)) {
          JsxSetter jsxSetter = (JsxSetter)annotation;
          if (isSupported(jsxSetter.value(), expectedBrowserName, browserVersionNumeric)) {
            String property;
            if (jsxSetter.propertyName().isEmpty()) {
              String property = method.getName().substring(3);
              property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
            }
            else {
              property = jsxSetter.propertyName();
            }
            allSetters.put(property, method);
          }
        }
        else if ((annotation instanceof JsxFunction)) {
          JsxFunction jsxFunction = (JsxFunction)annotation;
          if (isSupported(jsxFunction.value(), expectedBrowserName, browserVersionNumeric)) { String name;
            String name;
            if (jsxFunction.functionName().isEmpty()) {
              name = method.getName();
            }
            else {
              name = jsxFunction.functionName();
            }
            classConfiguration.addFunction(name, method);
          }
        }
        else if ((annotation instanceof JsxStaticGetter)) {
          JsxStaticGetter jsxStaticGetter = (JsxStaticGetter)annotation;
          if (isSupported(jsxStaticGetter.value(), expectedBrowserName, browserVersionNumeric)) {
            int prefix = method.getName().startsWith("is") ? 2 : 3;
            String property = method.getName().substring(prefix);
            property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
            classConfiguration.addStaticProperty(property, method, null);
          }
        }
        else if ((annotation instanceof JsxStaticFunction)) {
          JsxStaticFunction jsxStaticFunction = (JsxStaticFunction)annotation;
          if (isSupported(jsxStaticFunction.value(), expectedBrowserName, browserVersionNumeric)) { String name;
            String name;
            if (jsxStaticFunction.functionName().isEmpty()) {
              name = method.getName();
            }
            else {
              name = jsxStaticFunction.functionName();
            }
            classConfiguration.addStaticFunction(name, method);
          }
        }
        else if (((annotation instanceof JsxConstructor)) && 
          (isSupported(((JsxConstructor)annotation).value(), expectedBrowserName, browserVersionNumeric))) {
          classConfiguration.setJSConstructor(method);
        }
      }
    }
    for (Field field : classConfiguration.getHostClass().getDeclaredFields()) {
      JsxConstant jsxConstant = (JsxConstant)field.getAnnotation(JsxConstant.class);
      if ((jsxConstant != null) && (isSupported(jsxConstant.value(), expectedBrowserName, browserVersionNumeric))) {
        classConfiguration.addConstant(field.getName());
      }
    }
    for (Map.Entry<String, Method> getterEntry : allGetters.entrySet()) {
      String property = (String)getterEntry.getKey();
      classConfiguration.addProperty(property, (Method)getterEntry.getValue(), (Method)allSetters.get(property));
    }
  }
  
  private static boolean isSupported(WebBrowser[] browsers, String expectedBrowserName, float expectedVersionNumeric)
  {
    WebBrowser[] arrayOfWebBrowser = browsers;int j = browsers.length; for (int i = 0; i < j; i++) { WebBrowser browser = arrayOfWebBrowser[i];
      if ((browser.value().name().equals(expectedBrowserName)) && 
        (browser.minVersion() <= expectedVersionNumeric) && 
        (browser.maxVersion() >= expectedVersionNumeric)) {
        return true;
      }
    }
    return false;
  }
  




  public ClassConfiguration getClassConfiguration(String hostClassName)
  {
    return (ClassConfiguration)configuration_.get(hostClassName);
  }
  





  public Map<Class<?>, Class<? extends HtmlUnitScriptable>> getDomJavaScriptMapping()
  {
    if (domJavaScriptMap_ != null) {
      return domJavaScriptMap_;
    }
    
    Map<Class<?>, Class<? extends HtmlUnitScriptable>> map = new HashMap(configuration_.size());
    
    boolean debug = LOG.isDebugEnabled();
    int j; int i; for (Iterator localIterator = configuration_.keySet().iterator(); localIterator.hasNext(); 
        
        i < j)
    {
      String hostClassName = (String)localIterator.next();
      ClassConfiguration classConfig = getClassConfiguration(hostClassName);
      Class[] arrayOfClass; j = (arrayOfClass = classConfig.getDomClasses()).length;i = 0; continue;Class<?> domClass = arrayOfClass[i];
      
      if (debug) {
        LOG.debug("Mapping " + domClass.getName() + " to " + hostClassName);
      }
      map.put(domClass, classConfig.getHostClass());i++;
    }
    

    domJavaScriptMap_ = Collections.unmodifiableMap(map);
    
    return domJavaScriptMap_;
  }
}
