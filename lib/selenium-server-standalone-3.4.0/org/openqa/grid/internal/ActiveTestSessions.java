package org.openqa.grid.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import net.jcip.annotations.ThreadSafe;
import org.openqa.grid.common.exception.GridException;























@ThreadSafe
class ActiveTestSessions
{
  private static final Logger log = Logger.getLogger(ActiveTestSessions.class.getName());
  
  private final Set<TestSession> activeTestSessions = new CopyOnWriteArraySet();
  
  private final Queue<ExternalSessionKey> terminatedSessions = new ConcurrentLinkedQueue();
  


  private final Map<ExternalSessionKey, SessionTerminationReason> reasons = new ConcurrentHashMap();
  

  ActiveTestSessions() {}
  
  public boolean add(TestSession testSession)
  {
    boolean added = activeTestSessions.add(testSession);
    if (!added) {
      log.severe("Error adding session : " + testSession);
    }
    return added;
  }
  
  public boolean remove(TestSession o, SessionTerminationReason reason) {
    updateReason(o, reason);
    return activeTestSessions.remove(o);
  }
  
  private void updateReason(TestSession o, SessionTerminationReason reason) {
    if (o.getExternalKey() == null) {
      if (SessionTerminationReason.CREATIONFAILED != reason) {
        log.info("Removed a session that had not yet assigned an external key " + o
          .getInternalKey() + ", indicates failure in session creation " + reason);
      }
      
      return;
    }
    
    terminatedSessions.add(o.getExternalKey());
    reasons.put(o.getExternalKey(), reason);
    if (reasons.size() > 1000) {
      ExternalSessionKey remove = (ExternalSessionKey)terminatedSessions.remove();
      reasons.remove(remove);
    }
  }
  
  public TestSession findSessionByInternalKey(String internalKey) {
    if (internalKey == null) {
      return null;
    }
    
    for (TestSession session : activeTestSessions) {
      if (internalKey.equals(session.getInternalKey())) {
        return session;
      }
    }
    return null;
  }
  
  public TestSession getExistingSession(ExternalSessionKey externalkey) {
    TestSession sessionByExternalKey = findSessionByExternalKey(externalkey);
    if (sessionByExternalKey == null) {
      SessionTerminationReason sessionTerminationReason = externalkey != null ? (SessionTerminationReason)reasons.get(externalkey) : null;
      String keyId = externalkey != null ? externalkey.getKey() : "(null externalkey)";
      if (sessionTerminationReason != null) {
        String msg = "Session [" + keyId + "] was terminated due to " + sessionTerminationReason;
        log.fine(msg);
        throw new GridException(msg);
      }
      
      String msg = "Session [" + keyId + "] not available and is not among the last 1000 terminated sessions.\nActive sessions are" + unmodifiableSet();
      log.fine(msg);
      throw new GridException(msg);
    }
    return sessionByExternalKey;
  }
  
  public TestSession findSessionByExternalKey(ExternalSessionKey externalkey) {
    if (externalkey == null) {
      return null;
    }
    
    for (TestSession session : activeTestSessions) {
      if (externalkey.equals(session.getExternalKey())) {
        return session;
      }
    }
    return null;
  }
  
  public Set<TestSession> unmodifiableSet() {
    return Collections.unmodifiableSet(activeTestSessions);
  }
}
