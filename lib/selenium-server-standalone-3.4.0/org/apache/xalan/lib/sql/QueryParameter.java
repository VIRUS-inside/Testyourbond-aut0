package org.apache.xalan.lib.sql;

import java.util.Hashtable;



























public class QueryParameter
{
  private int m_type;
  private String m_name;
  private String m_value;
  private boolean m_output;
  private String m_typeName;
  private static Hashtable m_Typetable = null;
  
  public QueryParameter()
  {
    m_type = -1;
    m_name = null;
    m_value = null;
    m_output = false;
    m_typeName = null;
  }
  




  public QueryParameter(String v, String t)
  {
    m_name = null;
    m_value = v;
    m_output = false;
    setTypeName(t);
  }
  
  public QueryParameter(String name, String value, String type, boolean out_flag)
  {
    m_name = name;
    m_value = value;
    m_output = out_flag;
    setTypeName(type);
  }
  


  public String getValue()
  {
    return m_value;
  }
  



  public void setValue(String newValue)
  {
    m_value = newValue;
  }
  




  public void setTypeName(String newType)
  {
    m_type = map_type(newType);
    m_typeName = newType;
  }
  



  public String getTypeName()
  {
    return m_typeName;
  }
  



  public int getType()
  {
    return m_type;
  }
  



  public String getName()
  {
    return m_name;
  }
  





  public void setName(String n)
  {
    m_name = n;
  }
  



  public boolean isOutput()
  {
    return m_output;
  }
  





  public void setIsOutput(boolean flag)
  {
    m_output = flag;
  }
  
  private static int map_type(String typename)
  {
    if (m_Typetable == null)
    {

      m_Typetable = new Hashtable();
      m_Typetable.put("BIGINT", new Integer(-5));
      m_Typetable.put("BINARY", new Integer(-2));
      m_Typetable.put("BIT", new Integer(-7));
      m_Typetable.put("CHAR", new Integer(1));
      m_Typetable.put("DATE", new Integer(91));
      m_Typetable.put("DECIMAL", new Integer(3));
      m_Typetable.put("DOUBLE", new Integer(8));
      m_Typetable.put("FLOAT", new Integer(6));
      m_Typetable.put("INTEGER", new Integer(4));
      m_Typetable.put("LONGVARBINARY", new Integer(-4));
      m_Typetable.put("LONGVARCHAR", new Integer(-1));
      m_Typetable.put("NULL", new Integer(0));
      m_Typetable.put("NUMERIC", new Integer(2));
      m_Typetable.put("OTHER", new Integer(1111));
      m_Typetable.put("REAL", new Integer(7));
      m_Typetable.put("SMALLINT", new Integer(5));
      m_Typetable.put("TIME", new Integer(92));
      m_Typetable.put("TIMESTAMP", new Integer(93));
      m_Typetable.put("TINYINT", new Integer(-6));
      m_Typetable.put("VARBINARY", new Integer(-3));
      m_Typetable.put("VARCHAR", new Integer(12));
      

      m_Typetable.put("STRING", new Integer(12));
      m_Typetable.put("BIGDECIMAL", new Integer(2));
      m_Typetable.put("BOOLEAN", new Integer(-7));
      m_Typetable.put("BYTES", new Integer(-4));
      m_Typetable.put("LONG", new Integer(-5));
      m_Typetable.put("SHORT", new Integer(5));
    }
    
    Integer type = (Integer)m_Typetable.get(typename.toUpperCase());
    int rtype;
    int rtype; if (type == null) {
      rtype = 1111;
    } else {
      rtype = type.intValue();
    }
    return rtype;
  }
}
