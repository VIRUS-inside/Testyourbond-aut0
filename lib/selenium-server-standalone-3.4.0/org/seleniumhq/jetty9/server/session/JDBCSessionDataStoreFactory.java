package org.seleniumhq.jetty9.server.session;










public class JDBCSessionDataStoreFactory
  extends AbstractSessionDataStoreFactory
{
  DatabaseAdaptor _adaptor;
  








  JDBCSessionDataStore.SessionTableSchema _schema;
  








  public JDBCSessionDataStoreFactory() {}
  








  public SessionDataStore getSessionDataStore(SessionHandler handler)
  {
    JDBCSessionDataStore ds = new JDBCSessionDataStore();
    ds.setDatabaseAdaptor(_adaptor);
    ds.setSessionTableSchema(_schema);
    ds.setGracePeriodSec(getGracePeriodSec());
    return ds;
  }
  




  public void setDatabaseAdaptor(DatabaseAdaptor adaptor)
  {
    _adaptor = adaptor;
  }
  




  public void setSessionTableSchema(JDBCSessionDataStore.SessionTableSchema schema)
  {
    _schema = schema;
  }
}
