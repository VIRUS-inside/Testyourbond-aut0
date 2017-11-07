package org.eclipse.jetty.websocket.common.events.annotated;












public class EventMethods
{
  private Class<?> pojoClass;
  









  public EventMethod onConnect = null;
  public EventMethod onClose = null;
  public EventMethod onBinary = null;
  public EventMethod onText = null;
  public EventMethod onError = null;
  public EventMethod onFrame = null;
  
  public EventMethods(Class<?> pojoClass)
  {
    this.pojoClass = pojoClass;
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    EventMethods other = (EventMethods)obj;
    if (pojoClass == null)
    {
      if (pojoClass != null)
      {
        return false;
      }
    }
    else if (!pojoClass.getName().equals(pojoClass.getName()))
    {
      return false;
    }
    return true;
  }
  
  public Class<?> getPojoClass()
  {
    return pojoClass;
  }
  

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (pojoClass == null ? 0 : pojoClass.getName().hashCode());
    return result;
  }
  

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("EventMethods [pojoClass=");
    builder.append(pojoClass);
    builder.append(", onConnect=");
    builder.append(onConnect);
    builder.append(", onClose=");
    builder.append(onClose);
    builder.append(", onBinary=");
    builder.append(onBinary);
    builder.append(", onText=");
    builder.append(onText);
    builder.append(", onException=");
    builder.append(onError);
    builder.append(", onFrame=");
    builder.append(onFrame);
    builder.append("]");
    return builder.toString();
  }
}
