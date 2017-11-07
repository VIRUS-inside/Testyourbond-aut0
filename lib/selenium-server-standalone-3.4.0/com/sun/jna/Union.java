package com.sun.jna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





















public abstract class Union
  extends Structure
{
  private Structure.StructField activeField;
  
  protected Union() {}
  
  protected Union(Pointer p)
  {
    super(p);
  }
  
  protected Union(Pointer p, int alignType) {
    super(p, alignType);
  }
  
  protected Union(TypeMapper mapper) {
    super(mapper);
  }
  
  protected Union(Pointer p, int alignType, TypeMapper mapper) {
    super(p, alignType, mapper);
  }
  


  protected List getFieldOrder()
  {
    List flist = getFieldList();
    ArrayList list = new ArrayList();
    for (Iterator i = flist.iterator(); i.hasNext();) {
      Field f = (Field)i.next();
      list.add(f.getName());
    }
    return list;
  }
  





  public void setType(Class type)
  {
    ensureAllocated();
    for (Iterator i = fields().values().iterator(); i.hasNext();) {
      Structure.StructField f = (Structure.StructField)i.next();
      if (type == type) {
        activeField = f;
        return;
      }
    }
    throw new IllegalArgumentException("No field of type " + type + " in " + this);
  }
  




  public void setType(String fieldName)
  {
    ensureAllocated();
    Structure.StructField f = (Structure.StructField)fields().get(fieldName);
    if (f != null) {
      activeField = f;
    }
    else {
      throw new IllegalArgumentException("No field named " + fieldName + " in " + this);
    }
  }
  




  public Object readField(String fieldName)
  {
    ensureAllocated();
    setType(fieldName);
    return super.readField(fieldName);
  }
  



  public void writeField(String fieldName)
  {
    ensureAllocated();
    setType(fieldName);
    super.writeField(fieldName);
  }
  



  public void writeField(String fieldName, Object value)
  {
    ensureAllocated();
    setType(fieldName);
    super.writeField(fieldName, value);
  }
  











  public Object getTypedValue(Class type)
  {
    ensureAllocated();
    for (Iterator i = fields().values().iterator(); i.hasNext();) {
      Structure.StructField f = (Structure.StructField)i.next();
      if (type == type) {
        activeField = f;
        read();
        return getFieldValue(activeField.field);
      }
    }
    throw new IllegalArgumentException("No field of type " + type + " in " + this);
  }
  









  public Object setTypedValue(Object object)
  {
    Structure.StructField f = findField(object.getClass());
    if (f != null) {
      activeField = f;
      setFieldValue(field, object);
      return this;
    }
    throw new IllegalArgumentException("No field of type " + object.getClass() + " in " + this);
  }
  




  private Structure.StructField findField(Class type)
  {
    ensureAllocated();
    for (Iterator i = fields().values().iterator(); i.hasNext();) {
      Structure.StructField f = (Structure.StructField)i.next();
      if (type.isAssignableFrom(type)) {
        return f;
      }
    }
    return null;
  }
  
  protected void writeField(Structure.StructField field)
  {
    if (field == activeField) {
      super.writeField(field);
    }
  }
  



  protected Object readField(Structure.StructField field)
  {
    if ((field == activeField) || ((!Structure.class.isAssignableFrom(type)) && (!String.class.isAssignableFrom(type)) && (!WString.class.isAssignableFrom(type))))
    {


      return super.readField(field);
    }
    



    return null;
  }
  
  protected int getNativeAlignment(Class type, Object value, boolean isFirstElement)
  {
    return super.getNativeAlignment(type, value, true);
  }
}
