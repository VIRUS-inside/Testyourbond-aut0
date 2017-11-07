package org.apache.xalan.lib.sql;

import java.io.PrintStream;
import java.util.Vector;





























public class ObjectArray
{
  private int m_minArraySize = 10;
  


  private Vector m_Arrays = new Vector(200);
  





  private _ObjectArray m_currentArray;
  




  private int m_nextSlot;
  





  public ObjectArray()
  {
    init(10);
  }
  



  public ObjectArray(int minArraySize)
  {
    init(minArraySize);
  }
  




  private void init(int size)
  {
    m_minArraySize = size;
    m_currentArray = new _ObjectArray(m_minArraySize);
  }
  




  public Object getAt(int idx)
  {
    int arrayIndx = idx / m_minArraySize;
    int arrayOffset = idx - arrayIndx * m_minArraySize;
    



    if (arrayIndx < m_Arrays.size())
    {
      _ObjectArray a = (_ObjectArray)m_Arrays.elementAt(arrayIndx);
      return objects[arrayOffset];
    }
    






    return m_currentArray.objects[arrayOffset];
  }
  






  public void setAt(int idx, Object obj)
  {
    int arrayIndx = idx / m_minArraySize;
    int arrayOffset = idx - arrayIndx * m_minArraySize;
    



    if (arrayIndx < m_Arrays.size())
    {
      _ObjectArray a = (_ObjectArray)m_Arrays.elementAt(arrayIndx);
      objects[arrayOffset] = obj;



    }
    else
    {


      m_currentArray.objects[arrayOffset] = obj;
    }
  }
  






  public int append(Object o)
  {
    if (m_nextSlot >= m_minArraySize)
    {
      m_Arrays.addElement(m_currentArray);
      m_nextSlot = 0;
      m_currentArray = new _ObjectArray(m_minArraySize);
    }
    
    m_currentArray.objects[m_nextSlot] = o;
    
    int pos = m_Arrays.size() * m_minArraySize + m_nextSlot;
    
    m_nextSlot += 1;
    
    return pos;
  }
  




  class _ObjectArray
  {
    public Object[] objects;
    



    public _ObjectArray(int size)
    {
      objects = new Object[size];
    }
  }
  




  public static void main(String[] args)
  {
    String[] word = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine" };
    









    ObjectArray m_ObjectArray = new ObjectArray();
    
    for (int x = 0; x < word.length; x++)
    {
      System.out.print(" - " + m_ObjectArray.append(word[x]));
    }
    
    System.out.println("\n");
    
    for (int x = 0; x < word.length; x++)
    {
      String s = (String)m_ObjectArray.getAt(x);
      System.out.println(s);
    }
    

    System.out.println((String)m_ObjectArray.getAt(5));
    System.out.println((String)m_ObjectArray.getAt(10));
    System.out.println((String)m_ObjectArray.getAt(20));
    System.out.println((String)m_ObjectArray.getAt(2));
    System.out.println((String)m_ObjectArray.getAt(15));
    System.out.println((String)m_ObjectArray.getAt(30));
    System.out.println((String)m_ObjectArray.getAt(6));
    System.out.println((String)m_ObjectArray.getAt(8));
    

    System.out.println((String)m_ObjectArray.getAt(40));
  }
}