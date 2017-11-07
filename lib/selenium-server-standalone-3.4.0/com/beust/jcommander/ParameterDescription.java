package com.beust.jcommander;

import com.beust.jcommander.internal.Console;
import com.beust.jcommander.validators.NoValidator;
import com.beust.jcommander.validators.NoValueValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;























public class ParameterDescription
{
  private Object m_object;
  private WrappedParameter m_wrappedParameter;
  private Parameter m_parameterAnnotation;
  private DynamicParameter m_dynamicParameterAnnotation;
  private Parameterized m_parameterized;
  private boolean m_assigned = false;
  
  private java.util.ResourceBundle m_bundle;
  private String m_description;
  private JCommander m_jCommander;
  private Object m_default;
  private String m_longestName = "";
  

  public ParameterDescription(Object object, DynamicParameter annotation, Parameterized parameterized, java.util.ResourceBundle bundle, JCommander jc)
  {
    if (!Map.class.isAssignableFrom(parameterized.getType())) {
      throw new ParameterException("@DynamicParameter " + parameterized.getName() + " should be of type " + "Map but is " + parameterized.getType().getName());
    }
    


    m_dynamicParameterAnnotation = annotation;
    m_wrappedParameter = new WrappedParameter(m_dynamicParameterAnnotation);
    init(object, parameterized, bundle, jc);
  }
  
  public ParameterDescription(Object object, Parameter annotation, Parameterized parameterized, java.util.ResourceBundle bundle, JCommander jc)
  {
    m_parameterAnnotation = annotation;
    m_wrappedParameter = new WrappedParameter(m_parameterAnnotation);
    init(object, parameterized, bundle, jc);
  }
  




  private java.util.ResourceBundle findResourceBundle(Object o)
  {
    java.util.ResourceBundle result = null;
    
    Parameters p = (Parameters)o.getClass().getAnnotation(Parameters.class);
    if ((p != null) && (!isEmpty(p.resourceBundle()))) {
      result = java.util.ResourceBundle.getBundle(p.resourceBundle(), Locale.getDefault());
    } else {
      ResourceBundle a = (ResourceBundle)o.getClass().getAnnotation(ResourceBundle.class);
      
      if ((a != null) && (!isEmpty(a.value()))) {
        result = java.util.ResourceBundle.getBundle(a.value(), Locale.getDefault());
      }
    }
    
    return result;
  }
  
  private boolean isEmpty(String s) {
    return (s == null) || ("".equals(s));
  }
  
  private void initDescription(String description, String descriptionKey, String[] names) {
    m_description = description;
    if ((!"".equals(descriptionKey)) && 
      (m_bundle != null)) {
      m_description = m_bundle.getString(descriptionKey);
    }
    





    for (String name : names) {
      if (name.length() > m_longestName.length()) { m_longestName = name;
      }
    }
  }
  
  private void init(Object object, Parameterized parameterized, java.util.ResourceBundle bundle, JCommander jCommander)
  {
    m_object = object;
    m_parameterized = parameterized;
    m_bundle = bundle;
    if (m_bundle == null) {
      m_bundle = findResourceBundle(object);
    }
    m_jCommander = jCommander;
    
    if (m_parameterAnnotation != null) { String description;
      String description;
      if ((Enum.class.isAssignableFrom(parameterized.getType())) && (m_parameterAnnotation.description().isEmpty()))
      {
        description = "Options: " + EnumSet.allOf(parameterized.getType());
      } else {
        description = m_parameterAnnotation.description();
      }
      initDescription(description, m_parameterAnnotation.descriptionKey(), m_parameterAnnotation.names());
    }
    else if (m_dynamicParameterAnnotation != null) {
      initDescription(m_dynamicParameterAnnotation.description(), m_dynamicParameterAnnotation.descriptionKey(), m_dynamicParameterAnnotation.names());
    }
    else
    {
      throw new AssertionError("Shound never happen");
    }
    try
    {
      m_default = parameterized.get(object);
    }
    catch (Exception e) {}
    



    if ((m_default != null) && 
      (m_parameterAnnotation != null)) {
      validateDefaultValues(m_parameterAnnotation.names());
    }
  }
  
  private void validateDefaultValues(String[] names)
  {
    String name = names.length > 0 ? names[0] : "";
    validateValueParameter(name, m_default);
  }
  
  public String getLongestName() {
    return m_longestName;
  }
  
  public Object getDefault() {
    return m_default;
  }
  
  public String getDescription() {
    return m_description;
  }
  
  public Object getObject() {
    return m_object;
  }
  
  public String getNames() {
    StringBuilder sb = new StringBuilder();
    String[] names = m_wrappedParameter.names();
    for (int i = 0; i < names.length; i++) {
      if (i > 0) sb.append(", ");
      sb.append(names[i]);
    }
    return sb.toString();
  }
  
  public WrappedParameter getParameter() {
    return m_wrappedParameter;
  }
  
  public Parameterized getParameterized() {
    return m_parameterized;
  }
  
  private boolean isMultiOption() {
    Class<?> fieldType = m_parameterized.getType();
    return (fieldType.equals(List.class)) || (fieldType.equals(Set.class)) || (m_parameterized.isDynamicParameter());
  }
  
  public void addValue(String value)
  {
    addValue(value, false);
  }
  


  public boolean isAssigned()
  {
    return m_assigned;
  }
  
  public void setAssigned(boolean b)
  {
    m_assigned = b;
  }
  




  public void addValue(String value, boolean isDefault)
  {
    p("Adding " + (isDefault ? "default " : "") + "value:" + value + " to parameter:" + m_parameterized.getName());
    
    String name = m_wrappedParameter.names()[0];
    if (((m_assigned) && (!isMultiOption()) && (!m_jCommander.isParameterOverwritingAllowed())) || (isNonOverwritableForced())) {
      throw new ParameterException("Can only specify option " + name + " once.");
    }
    
    validateParameter(name, value);
    
    Class<?> type = m_parameterized.getType();
    
    Object convertedValue = m_jCommander.convertValue(this, value);
    validateValueParameter(name, convertedValue);
    boolean isCollection = Collection.class.isAssignableFrom(type);
    
    if (isCollection)
    {
      Collection<Object> l = (Collection)m_parameterized.get(m_object);
      if ((l == null) || (fieldIsSetForTheFirstTime(isDefault))) {
        l = newCollection(type);
        m_parameterized.set(m_object, l);
      }
      if ((convertedValue instanceof Collection)) {
        l.addAll((Collection)convertedValue);
      } else {
        l.add(convertedValue);
      }
    }
    else
    {
      m_wrappedParameter.addValue(m_parameterized, m_object, convertedValue);
    }
    if (!isDefault) m_assigned = true;
  }
  
  private void validateParameter(String name, String value) {
    Class<? extends IParameterValidator> validator = m_wrappedParameter.validateWith();
    if (validator != null) {
      validateParameter(this, validator, name, value);
    }
  }
  
  private void validateValueParameter(String name, Object value) {
    Class<? extends IValueValidator> validator = m_wrappedParameter.validateValueWith();
    if (validator != null) {
      validateValueParameter(validator, name, value);
    }
  }
  
  public static void validateValueParameter(Class<? extends IValueValidator> validator, String name, Object value)
  {
    try {
      if (validator != NoValueValidator.class) {
        p("Validating value parameter:" + name + " value:" + value + " validator:" + validator);
      }
      ((IValueValidator)validator.newInstance()).validate(name, value);
    } catch (InstantiationException e) {
      throw new ParameterException("Can't instantiate validator:" + e);
    } catch (IllegalAccessException e) {
      throw new ParameterException("Can't instantiate validator:" + e);
    }
  }
  
  public static void validateParameter(ParameterDescription pd, Class<? extends IParameterValidator> validator, String name, String value)
  {
    try
    {
      if (validator != NoValidator.class) {
        p("Validating parameter:" + name + " value:" + value + " validator:" + validator);
      }
      ((IParameterValidator)validator.newInstance()).validate(name, value);
      if (IParameterValidator2.class.isAssignableFrom(validator)) {
        IParameterValidator2 instance = (IParameterValidator2)validator.newInstance();
        instance.validate(name, value, pd);
      }
    } catch (InstantiationException e) {
      throw new ParameterException("Can't instantiate validator:" + e);
    } catch (IllegalAccessException e) {
      throw new ParameterException("Can't instantiate validator:" + e);
    } catch (ParameterException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ParameterException(ex);
    }
  }
  






  private Collection<Object> newCollection(Class<?> type)
  {
    if (SortedSet.class.isAssignableFrom(type)) return new TreeSet();
    if (LinkedHashSet.class.isAssignableFrom(type)) return new LinkedHashSet();
    if (Set.class.isAssignableFrom(type)) return new HashSet();
    if (List.class.isAssignableFrom(type)) { return new ArrayList();
    }
    throw new ParameterException("Parameters of Collection type '" + type.getSimpleName() + "' are not supported. Please use List or Set instead.");
  }
  





  private boolean fieldIsSetForTheFirstTime(boolean isDefault)
  {
    return (!isDefault) && (!m_assigned);
  }
  
  private static void p(String string) {
    if (System.getProperty("jcommander.debug") != null) {
      JCommander.getConsole().println("[ParameterDescription] " + string);
    }
  }
  
  public String toString()
  {
    return "[ParameterDescription " + m_parameterized.getName() + "]";
  }
  
  public boolean isDynamicParameter() {
    return m_dynamicParameterAnnotation != null;
  }
  
  public boolean isHelp() {
    return m_wrappedParameter.isHelp();
  }
  
  public boolean isNonOverwritableForced() {
    return m_wrappedParameter.isNonOverwritableForced();
  }
}