package org.openqa.selenium.remote;




public class Response
{
  private volatile Object value;
  


  private volatile String sessionId;
  


  private volatile Integer status;
  


  private volatile String state;
  



  public Response() {}
  


  public Response(SessionId sessionId)
  {
    this.sessionId = String.valueOf(sessionId);
  }
  
  public Integer getStatus() {
    return status;
  }
  
  public void setStatus(Integer status) {
    this.status = status;
  }
  
  public String getState() {
    return state;
  }
  
  public void setState(String state) {
    this.state = state;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
  
  public Object getValue() {
    return value;
  }
  
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public String getSessionId() {
    return sessionId;
  }
  
  public String toString()
  {
    return String.format("(Response: SessionID: %s, Status: %s, Value: %s)", new Object[] { getSessionId(), getStatus(), getValue() });
  }
}
