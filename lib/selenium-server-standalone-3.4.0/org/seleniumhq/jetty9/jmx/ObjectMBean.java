package org.seleniumhq.jetty9.jmx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBean;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


































public class ObjectMBean
  implements DynamicMBean
{
  private static final Logger LOG = Log.getLogger(ObjectMBean.class);
  
  private static Class<?>[] OBJ_ARG = { Object.class };
  
  protected Object _managed;
  private MBeanInfo _info;
  private Map<String, Method> _getters = new HashMap();
  private Map<String, Method> _setters = new HashMap();
  private Map<String, Method> _methods = new HashMap();
  

  private Set<String> _attributes = new HashSet();
  


  private Set<String> _convert = new HashSet();
  
  private ClassLoader _loader;
  private MBeanContainer _mbeanContainer;
  private static String OBJECT_NAME_CLASS = ObjectName.class.getName();
  private static String OBJECT_NAME_ARRAY_CLASS = [Ljavax.management.ObjectName.class.getName();
  



















  public static Object mbeanFor(Object o)
  {
    try
    {
      Class<?> oClass = o.getClass();
      Object mbean = null;
      
      while ((mbean == null) && (oClass != null))
      {
        String pName = oClass.getPackage().getName();
        String cName = oClass.getName().substring(pName.length() + 1);
        String mName = pName + ".jmx." + cName + "MBean";
        
        try
        {
          Class<?> mClass;
          
          try
          {
            mClass = Object.class.equals(oClass) ? (oClass = ObjectMBean.class) : Loader.loadClass(oClass, mName);
          }
          catch (ClassNotFoundException e)
          {
            Class<?> mClass;
            if (Thread.currentThread().getContextClassLoader() == oClass.getClassLoader())
              throw e;
            LOG.ignore(e);
            mClass = Loader.loadClass(oClass, mName);
          }
          
          if (LOG.isDebugEnabled()) {
            LOG.debug("ObjectMbean: mbeanFor {} mClass={}", new Object[] { o, mClass });
          }
          try
          {
            Constructor<?> constructor = mClass.getConstructor(OBJ_ARG);
            mbean = constructor.newInstance(new Object[] { o });
          }
          catch (Exception e)
          {
            LOG.ignore(e);
            if (ModelMBean.class.isAssignableFrom(mClass))
            {
              mbean = mClass.newInstance();
              ((ModelMBean)mbean).setManagedResource(o, "objectReference");
            }
          }
          
          if (LOG.isDebugEnabled()) {
            LOG.debug("mbeanFor {} is {}", new Object[] { o, mbean });
          }
          return mbean;


        }
        catch (ClassNotFoundException e)
        {


          if (e.getMessage().contains(mName)) {
            LOG.ignore(e);
          } else {
            LOG.warn(e);
          }
        }
        catch (Error e) {
          LOG.warn(e);
          mbean = null;
        }
        catch (Exception e)
        {
          LOG.warn(e);
          mbean = null;
        }
        
        oClass = oClass.getSuperclass();
      }
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    
    return null;
  }
  

  public ObjectMBean(Object managedObject)
  {
    _managed = managedObject;
    _loader = Thread.currentThread().getContextClassLoader();
  }
  
  public Object getManagedObject()
  {
    return _managed;
  }
  
  public ObjectName getObjectName()
  {
    return null;
  }
  
  public String getObjectContextBasis()
  {
    return null;
  }
  
  public String getObjectNameBasis()
  {
    return null;
  }
  
  protected void setMBeanContainer(MBeanContainer container)
  {
    _mbeanContainer = container;
  }
  
  public MBeanContainer getMBeanContainer()
  {
    return _mbeanContainer;
  }
  

  public MBeanInfo getMBeanInfo()
  {
    try
    {
      if (_info == null)
      {

        String desc = null;
        List<MBeanAttributeInfo> attributes = new ArrayList();
        List<MBeanConstructorInfo> constructors = new ArrayList();
        List<MBeanOperationInfo> operations = new ArrayList();
        List<MBeanNotificationInfo> notifications = new ArrayList();
        

        Class<?> o_class = _managed.getClass();
        List<Class<?>> influences = new ArrayList();
        influences.add(getClass());
        influences = findInfluences(influences, _managed.getClass());
        
        if (LOG.isDebugEnabled()) {
          LOG.debug("Influence Count: {}", influences.size());
        }
        
        ManagedObject primary = (ManagedObject)o_class.getAnnotation(ManagedObject.class);
        
        if (primary != null)
        {
          desc = primary.value();


        }
        else if (LOG.isDebugEnabled()) {
          LOG.debug("No @ManagedObject declared on {}", new Object[] { _managed.getClass() });
        }
        


        for (int i = 0; i < influences.size(); i++)
        {
          Class<?> oClass = (Class)influences.get(i);
          
          ManagedObject typeAnnotation = (ManagedObject)oClass.getAnnotation(ManagedObject.class);
          
          if (LOG.isDebugEnabled()) {
            LOG.debug("Influenced by: " + oClass.getCanonicalName(), new Object[0]);
          }
          if (typeAnnotation == null)
          {
            if (LOG.isDebugEnabled()) {
              LOG.debug("Annotations not found for: {}", new Object[] { oClass.getCanonicalName() });
            }
            

          }
          else {
            for (Method method : oClass.getDeclaredMethods())
            {
              ManagedAttribute methodAttributeAnnotation = (ManagedAttribute)method.getAnnotation(ManagedAttribute.class);
              
              if (methodAttributeAnnotation != null)
              {

                if (LOG.isDebugEnabled())
                  LOG.debug("Attribute Annotation found for: {}", new Object[] { method.getName() });
                MBeanAttributeInfo mai = defineAttribute(method, methodAttributeAnnotation);
                if (mai != null)
                {
                  attributes.add(mai);
                }
              }
              
              ManagedOperation methodOperationAnnotation = (ManagedOperation)method.getAnnotation(ManagedOperation.class);
              
              if (methodOperationAnnotation != null)
              {
                if (LOG.isDebugEnabled())
                  LOG.debug("Method Annotation found for: {}", new Object[] { method.getName() });
                MBeanOperationInfo oi = defineOperation(method, methodOperationAnnotation);
                if (oi != null)
                {
                  operations.add(oi);
                }
              }
            }
          }
        }
        





        _info = new MBeanInfo(o_class.getName(), desc, (MBeanAttributeInfo[])attributes.toArray(new MBeanAttributeInfo[attributes.size()]), (MBeanConstructorInfo[])constructors.toArray(new MBeanConstructorInfo[constructors.size()]), (MBeanOperationInfo[])operations.toArray(new MBeanOperationInfo[operations.size()]), (MBeanNotificationInfo[])notifications.toArray(new MBeanNotificationInfo[notifications.size()]));
      }
    }
    catch (RuntimeException e)
    {
      LOG.warn(e);
      throw e;
    }
    return _info;
  }
  

  public Object getAttribute(String name)
    throws AttributeNotFoundException, MBeanException, ReflectionException
  {
    Method getter = (Method)_getters.get(name);
    if (getter == null)
    {
      throw new AttributeNotFoundException(name);
    }
    
    try
    {
      Object o = _managed;
      if (getter.getDeclaringClass().isInstance(this)) {
        o = this;
      }
      
      Object r = getter.invoke(o, (Object[])null);
      

      if (r != null)
      {
        if (r.getClass().isArray())
        {
          if (r.getClass().getComponentType().isAnnotationPresent(ManagedObject.class))
          {
            ObjectName[] on = new ObjectName[Array.getLength(r)];
            for (int i = 0; i < on.length; i++)
            {
              on[i] = _mbeanContainer.findMBean(Array.get(r, i));
            }
            r = on;
          }
        }
        else if ((r instanceof Collection))
        {

          Collection<Object> c = (Collection)r;
          
          if ((!c.isEmpty()) && (c.iterator().next().getClass().isAnnotationPresent(ManagedObject.class)))
          {


            ObjectName[] on = new ObjectName[c.size()];
            int i = 0;
            for (Object obj : c)
            {
              on[(i++)] = _mbeanContainer.findMBean(obj);
            }
            r = on;
          }
        }
        else
        {
          Class<?> clazz = r.getClass();
          
          while (clazz != null)
          {
            if (clazz.isAnnotationPresent(ManagedObject.class))
            {
              ObjectName mbean = _mbeanContainer.findMBean(r);
              
              if (mbean != null)
              {
                return mbean;
              }
              

              return null;
            }
            
            clazz = clazz.getSuperclass();
          }
        }
      }
      
      return r;
    }
    catch (IllegalAccessException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new AttributeNotFoundException(e.toString());
    }
    catch (InvocationTargetException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new ReflectionException(new Exception(e.getCause()));
    }
  }
  

  public AttributeList getAttributes(String[] names)
  {
    AttributeList results = new AttributeList(names.length);
    for (int i = 0; i < names.length; i++)
    {
      try
      {
        results.add(new Attribute(names[i], getAttribute(names[i])));
      }
      catch (Exception e)
      {
        LOG.warn("EXCEPTION ", e);
      }
    }
    return results;
  }
  
  public void setAttribute(Attribute attr)
    throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
  {
    if (attr == null) {
      return;
    }
    if (LOG.isDebugEnabled())
      LOG.debug("setAttribute " + _managed + ":" + attr.getName() + "=" + attr.getValue(), new Object[0]);
    Method setter = (Method)_setters.get(attr.getName());
    if (setter == null) {
      throw new AttributeNotFoundException(attr.getName());
    }
    try {
      Object o = _managed;
      if (setter.getDeclaringClass().isInstance(this)) {
        o = this;
      }
      
      Object value = attr.getValue();
      

      if ((value != null) && (_convert.contains(attr.getName())))
      {
        if (value.getClass().isArray())
        {
          Class<?> t = setter.getParameterTypes()[0].getComponentType();
          Object na = Array.newInstance(t, Array.getLength(value));
          for (int i = Array.getLength(value); i-- > 0;)
            Array.set(na, i, _mbeanContainer.findBean((ObjectName)Array.get(value, i)));
          value = na;
        }
        else {
          value = _mbeanContainer.findBean((ObjectName)value);
        }
      }
      
      setter.invoke(o, new Object[] { value });
    }
    catch (IllegalAccessException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new AttributeNotFoundException(e.toString());
    }
    catch (InvocationTargetException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new ReflectionException(new Exception(e.getCause()));
    }
  }
  

  public AttributeList setAttributes(AttributeList attrs)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setAttributes", new Object[0]);
    }
    AttributeList results = new AttributeList(attrs.size());
    Iterator<Object> iter = attrs.iterator();
    while (iter.hasNext())
    {
      try
      {
        Attribute attr = (Attribute)iter.next();
        setAttribute(attr);
        results.add(new Attribute(attr.getName(), getAttribute(attr.getName())));
      }
      catch (Exception e)
      {
        LOG.warn("EXCEPTION ", e);
      }
    }
    return results;
  }
  
  public Object invoke(String name, Object[] params, String[] signature)
    throws MBeanException, ReflectionException
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("ObjectMBean:invoke " + name, new Object[0]);
    }
    String methodKey = name + "(";
    if (signature != null)
      for (int i = 0; i < signature.length; i++)
        methodKey = methodKey + (i > 0 ? "," : "") + signature[i];
    methodKey = methodKey + ")";
    
    ClassLoader old_loader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(_loader);
      Method method = (Method)_methods.get(methodKey);
      if (method == null) {
        throw new NoSuchMethodException(methodKey);
      }
      Object o = _managed;
      
      if (method.getDeclaringClass().isInstance(this))
      {
        o = this;
      }
      return method.invoke(o, params);
    }
    catch (NoSuchMethodException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new ReflectionException(e);
    }
    catch (IllegalAccessException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new MBeanException(e);
    }
    catch (InvocationTargetException e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new ReflectionException(new Exception(e.getCause()));
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(old_loader);
    }
  }
  
  private static List<Class<?>> findInfluences(List<Class<?>> influences, Class<?> aClass)
  {
    if (aClass != null)
    {
      if (!influences.contains(aClass))
      {

        influences.add(aClass);
      }
      

      influences = findInfluences(influences, aClass.getSuperclass());
      

      Class<?>[] ifs = aClass.getInterfaces();
      for (int i = 0; (ifs != null) && (i < ifs.length); i++)
      {
        influences = findInfluences(influences, ifs[i]);
      }
    }
    
    return influences;
  }
  




















  public MBeanAttributeInfo defineAttribute(Method method, ManagedAttribute attributeAnnotation)
  {
    String name = attributeAnnotation.name();
    
    if ("".equals(name))
    {
      name = toVariableName(method.getName());
    }
    
    if (_attributes.contains(name))
    {
      return null;
    }
    
    String description = attributeAnnotation.value();
    boolean readonly = attributeAnnotation.readonly();
    boolean onMBean = attributeAnnotation.proxied();
    
    boolean convert = false;
    

    Class<?> return_type = method.getReturnType();
    

    Class<?> component_type = return_type;
    while (component_type.isArray())
    {
      component_type = component_type.getComponentType();
    }
    

    convert = isAnnotationPresent(component_type, ManagedObject.class);
    
    String uName = name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    Class<?> oClass = onMBean ? getClass() : _managed.getClass();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("defineAttribute {} {}:{}:{}:{}", new Object[] { name, Boolean.valueOf(onMBean), Boolean.valueOf(readonly), oClass, description });
    }
    Method setter = null;
    

    if (!readonly)
    {
      String declaredSetter = attributeAnnotation.setter();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("DeclaredSetter: {}", new Object[] { declaredSetter });
      }
      Method[] methods = oClass.getMethods();
      for (int m = 0; m < methods.length; m++)
      {
        if ((methods[m].getModifiers() & 0x1) != 0)
        {

          if (!"".equals(declaredSetter))
          {


            if ((methods[m].getName().equals(declaredSetter)) && (methods[m].getParameterCount() == 1))
            {
              if (setter != null)
              {
                LOG.warn("Multiple setters for mbean attr {} in {}", new Object[] { name, oClass });
                continue;
              }
              setter = methods[m];
              if (!component_type.equals(methods[m].getParameterTypes()[0]))
              {
                LOG.warn("Type conflict for mbean attr {} in {}", new Object[] { name, oClass });
                continue;
              }
              if (LOG.isDebugEnabled()) {
                LOG.debug("Declared Setter: " + declaredSetter, new Object[0]);
              }
            }
          }
          
          if ((methods[m].getName().equals("set" + uName)) && (methods[m].getParameterCount() == 1))
          {
            if (setter != null)
            {
              LOG.warn("Multiple setters for mbean attr {} in {}", new Object[] { name, oClass });
            }
            else {
              setter = methods[m];
              if (!return_type.equals(methods[m].getParameterTypes()[0]))
              {
                LOG.warn("Type conflict for mbean attr {} in {}", new Object[] { name, oClass });
              }
            }
          }
        }
      }
    }
    if (convert)
    {
      if (component_type == null)
      {
        LOG.warn("No mbean type for {} on {}", new Object[] { name, _managed.getClass() });
        return null;
      }
      
      if ((component_type.isPrimitive()) && (!component_type.isArray()))
      {
        LOG.warn("Cannot convert mbean primative {}", new Object[] { name });
        return null;
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("passed convert checks {} for type {}", new Object[] { name, component_type });
      }
    }
    
    try
    {
      _getters.put(name, method);
      _setters.put(name, setter);
      
      MBeanAttributeInfo info = null;
      if (convert)
      {
        _convert.add(name);
        
        if (component_type.isArray())
        {
          info = new MBeanAttributeInfo(name, OBJECT_NAME_ARRAY_CLASS, description, true, setter != null, method.getName().startsWith("is"));
        }
        else
        {
          info = new MBeanAttributeInfo(name, OBJECT_NAME_CLASS, description, true, setter != null, method.getName().startsWith("is"));
        }
      }
      else
      {
        info = new MBeanAttributeInfo(name, description, method, setter);
      }
      
      _attributes.add(name);
      
      return info;
    }
    catch (Exception e)
    {
      LOG.warn(e);
      throw new IllegalArgumentException(e.toString());
    }
  }
  
















  private MBeanOperationInfo defineOperation(Method method, ManagedOperation methodAnnotation)
  {
    String description = methodAnnotation.value();
    boolean onMBean = methodAnnotation.proxied();
    
    boolean convert = false;
    

    Class<?> returnType = method.getReturnType();
    
    if (returnType.isArray())
    {
      if (LOG.isDebugEnabled())
        LOG.debug("returnType is array, get component type", new Object[0]);
      returnType = returnType.getComponentType();
    }
    
    if (returnType.isAnnotationPresent(ManagedObject.class))
    {
      convert = true;
    }
    
    String impactName = methodAnnotation.impact();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("defineOperation {} {}:{}:{}", new Object[] { method.getName(), Boolean.valueOf(onMBean), impactName, description });
    }
    String signature = method.getName();
    

    try
    {
      int impact = 3;
      if ((impactName == null) || (impactName.equals("UNKNOWN"))) {
        impact = 3;
      } else if (impactName.equals("ACTION")) {
        impact = 1;
      } else if (impactName.equals("INFO")) {
        impact = 0;
      } else if (impactName.equals("ACTION_INFO")) {
        impact = 2;
      } else {
        LOG.warn("Unknown impact '" + impactName + "' for " + signature, new Object[0]);
      }
      
      Annotation[][] allParameterAnnotations = method.getParameterAnnotations();
      Class<?>[] methodTypes = method.getParameterTypes();
      MBeanParameterInfo[] pInfo = new MBeanParameterInfo[allParameterAnnotations.length];
      
      for (int i = 0; i < allParameterAnnotations.length; i++)
      {
        Annotation[] parameterAnnotations = allParameterAnnotations[i];
        
        for (Annotation anno : parameterAnnotations)
        {
          if ((anno instanceof Name))
          {
            Name nameAnnotation = (Name)anno;
            
            pInfo[i] = new MBeanParameterInfo(nameAnnotation.value(), methodTypes[i].getName(), nameAnnotation.description());
          }
        }
      }
      
      signature = signature + "(";
      for (int i = 0; i < methodTypes.length; i++)
      {
        signature = signature + methodTypes[i].getName();
        
        if (i != methodTypes.length - 1)
        {
          signature = signature + ",";
        }
      }
      signature = signature + ")";
      
      Class<?> returnClass = method.getReturnType();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Method Cache: " + signature, new Object[0]);
      }
      if (_methods.containsKey(signature))
      {
        return null;
      }
      
      _methods.put(signature, method);
      if (convert) {
        _convert.add(signature);
      }
      return new MBeanOperationInfo(method.getName(), description, pInfo, returnClass.isPrimitive() ? TypeUtil.toName(returnClass) : returnClass.getName(), impact);
    }
    catch (Exception e)
    {
      LOG.warn("Operation '" + signature + "'", e);
      throw new IllegalArgumentException(e.toString());
    }
  }
  

  protected String toVariableName(String methodName)
  {
    String variableName = methodName;
    
    if ((methodName.startsWith("get")) || (methodName.startsWith("set")))
    {
      variableName = variableName.substring(3);
    }
    else if (methodName.startsWith("is"))
    {
      variableName = variableName.substring(2);
    }
    
    variableName = variableName.substring(0, 1).toLowerCase(Locale.ENGLISH) + variableName.substring(1);
    
    return variableName;
  }
  
  protected boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation)
  {
    Class<?> test = clazz;
    
    while (test != null)
    {
      if (test.isAnnotationPresent(annotation))
      {
        return true;
      }
      

      test = test.getSuperclass();
    }
    
    return false;
  }
}
