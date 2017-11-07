package org.seleniumhq.jetty9.server.session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























public class SessionData
  implements Serializable
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  private static final long serialVersionUID = 1L;
  
  protected String _id;
  protected String _contextPath;
  protected String _vhost;
  protected String _lastNode;
  protected long _expiry;
  protected long _created;
  protected long _cookieSet;
  protected long _accessed;
  protected long _lastAccessed;
  protected long _maxInactiveMs;
  protected Map<String, Object> _attributes;
  protected boolean _dirty;
  protected long _lastSaved;
  
  public SessionData(String id, String cpath, String vhost, long created, long accessed, long lastAccessed, long maxInactiveMs)
  {
    this(id, cpath, vhost, created, accessed, lastAccessed, maxInactiveMs, new ConcurrentHashMap());
  }
  
  public SessionData(String id, String cpath, String vhost, long created, long accessed, long lastAccessed, long maxInactiveMs, Map<String, Object> attributes)
  {
    _id = id;
    setContextPath(cpath);
    setVhost(vhost);
    _created = created;
    _accessed = accessed;
    _lastAccessed = lastAccessed;
    _maxInactiveMs = maxInactiveMs;
    calcAndSetExpiry();
    _attributes = attributes;
  }
  





  public void copy(SessionData data)
  {
    if (data == null) {
      return;
    }
    if ((data.getId() == null) || (!getId().equals(data.getId()))) {
      throw new IllegalStateException("Can only copy data for same session id");
    }
    if (data == this) {
      return;
    }
    setLastNode(data.getLastNode());
    setContextPath(data.getContextPath());
    setVhost(data.getVhost());
    setCookieSet(data.getCookieSet());
    setCreated(data.getCreated());
    setAccessed(data.getAccessed());
    setLastAccessed(data.getLastAccessed());
    setMaxInactiveMs(data.getMaxInactiveMs());
    setExpiry(data.getExpiry());
    setLastSaved(data.getLastSaved());
    clearAllAttributes();
    putAllAttributes(data.getAllAttributes());
  }
  



  public long getLastSaved()
  {
    return _lastSaved;
  }
  

  public void setLastSaved(long lastSaved)
  {
    _lastSaved = lastSaved;
  }
  




  public boolean isDirty()
  {
    return _dirty;
  }
  
  public void setDirty(boolean dirty)
  {
    _dirty = dirty;
  }
  




  public Object getAttribute(String name)
  {
    return _attributes.get(name);
  }
  



  public Set<String> getKeys()
  {
    return _attributes.keySet();
  }
  
  public Object setAttribute(String name, Object value)
  {
    Object old = value == null ? _attributes.remove(name) : _attributes.put(name, value);
    if ((value == null) && (old == null)) {
      return old;
    }
    setDirty(name);
    return old;
  }
  
  public void setDirty(String name)
  {
    setDirty(true);
  }
  
  public void putAllAttributes(Map<String, Object> attributes)
  {
    _attributes.putAll(attributes);
  }
  



  public void clearAllAttributes()
  {
    _attributes.clear();
  }
  



  public Map<String, Object> getAllAttributes()
  {
    return Collections.unmodifiableMap(_attributes);
  }
  



  public String getId()
  {
    return _id;
  }
  
  public void setId(String id)
  {
    _id = id;
  }
  



  public String getContextPath()
  {
    return _contextPath;
  }
  
  public void setContextPath(String contextPath)
  {
    _contextPath = contextPath;
  }
  



  public String getVhost()
  {
    return _vhost;
  }
  
  public void setVhost(String vhost)
  {
    _vhost = vhost;
  }
  



  public String getLastNode()
  {
    return _lastNode;
  }
  
  public void setLastNode(String lastNode)
  {
    _lastNode = lastNode;
  }
  



  public long getExpiry()
  {
    return _expiry;
  }
  
  public void setExpiry(long expiry)
  {
    _expiry = expiry;
  }
  
  public long calcExpiry()
  {
    return calcExpiry(System.currentTimeMillis());
  }
  
  public long calcExpiry(long time)
  {
    return getMaxInactiveMs() <= 0L ? 0L : time + getMaxInactiveMs();
  }
  
  public void calcAndSetExpiry(long time)
  {
    setExpiry(calcExpiry(time));
  }
  
  public void calcAndSetExpiry()
  {
    setExpiry(calcExpiry());
  }
  
  public long getCreated()
  {
    return _created;
  }
  
  public void setCreated(long created)
  {
    _created = created;
  }
  



  public long getCookieSet()
  {
    return _cookieSet;
  }
  
  public void setCookieSet(long cookieSet)
  {
    _cookieSet = cookieSet;
  }
  



  public long getAccessed()
  {
    return _accessed;
  }
  
  public void setAccessed(long accessed)
  {
    _accessed = accessed;
  }
  



  public long getLastAccessed()
  {
    return _lastAccessed;
  }
  
  public void setLastAccessed(long lastAccessed)
  {
    _lastAccessed = lastAccessed;
  }
  
  public long getMaxInactiveMs()
  {
    return _maxInactiveMs;
  }
  
  public void setMaxInactiveMs(long maxInactive)
  {
    _maxInactiveMs = maxInactive;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException
  {
    out.writeUTF(_id);
    out.writeUTF(_contextPath);
    out.writeUTF(_vhost);
    
    out.writeLong(_accessed);
    out.writeLong(_lastAccessed);
    out.writeLong(_created);
    out.writeLong(_cookieSet);
    out.writeUTF(_lastNode);
    
    out.writeLong(_expiry);
    out.writeLong(_maxInactiveMs);
    out.writeObject(_attributes);
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    _id = in.readUTF();
    _contextPath = in.readUTF();
    _vhost = in.readUTF();
    
    _accessed = in.readLong();
    _lastAccessed = in.readLong();
    _created = in.readLong();
    _cookieSet = in.readLong();
    _lastNode = in.readUTF();
    _expiry = in.readLong();
    _maxInactiveMs = in.readLong();
    _attributes = ((Map)in.readObject());
  }
  
  public boolean isExpiredAt(long time)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Testing expiry on session {}: expires at {} now {} maxIdle {}", new Object[] { _id, Long.valueOf(getExpiry()), Long.valueOf(time), Long.valueOf(getMaxInactiveMs()) });
    if (getMaxInactiveMs() <= 0L)
      return false;
    return getExpiry() <= time;
  }
  



  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("id=" + _id);
    builder.append(", contextpath=" + _contextPath);
    builder.append(", vhost=" + _vhost);
    builder.append(", accessed=" + _accessed);
    builder.append(", lastaccessed=" + _lastAccessed);
    builder.append(", created=" + _created);
    builder.append(", cookieset=" + _cookieSet);
    builder.append(", lastnode=" + _lastNode);
    builder.append(", expiry=" + _expiry);
    builder.append(", maxinactive=" + _maxInactiveMs);
    return builder.toString();
  }
}
