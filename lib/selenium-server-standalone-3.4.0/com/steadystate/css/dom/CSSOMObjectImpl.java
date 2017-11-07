package com.steadystate.css.dom;

import com.steadystate.css.util.LangUtils;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;





















public class CSSOMObjectImpl
  implements CSSOMObject, Serializable
{
  private static final long serialVersionUID = 0L;
  private Map<String, Object> userDataMap_;
  
  public Map<String, Object> getUserDataMap()
  {
    if (userDataMap_ == null) {
      userDataMap_ = new Hashtable();
    }
    return userDataMap_;
  }
  
  public void setUserDataMap(Map<String, Object> userDataMap) {
    userDataMap_ = userDataMap;
  }
  

  public CSSOMObjectImpl() {}
  
  public Object getUserData(String key)
  {
    return getUserDataMap().get(key);
  }
  
  public Object setUserData(String key, Object data) {
    return getUserDataMap().put(key, data);
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSOMObjectImpl)) {
      return false;
    }
    CSSOMObjectImpl coi = (CSSOMObjectImpl)obj;
    return LangUtils.equals(userDataMap_, userDataMap_);
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, userDataMap_);
    return hash;
  }
}
