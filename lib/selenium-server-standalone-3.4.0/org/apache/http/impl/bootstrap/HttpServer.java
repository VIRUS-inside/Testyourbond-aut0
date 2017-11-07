package org.apache.http.impl.bootstrap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.protocol.HttpService;















public class HttpServer
{
  private final int port;
  private final InetAddress ifAddress;
  private final SocketConfig socketConfig;
  private final ServerSocketFactory serverSocketFactory;
  private final HttpService httpService;
  private final HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
  private final SSLServerSetupHandler sslSetupHandler;
  private final ExceptionLogger exceptionLogger;
  private final ThreadPoolExecutor listenerExecutorService;
  private final ThreadGroup workerThreads;
  private final WorkerPoolExecutor workerExecutorService;
  private final AtomicReference<Status> status;
  private volatile ServerSocket serverSocket;
  private volatile RequestListener requestListener;
  
  static enum Status
  {
    READY,  ACTIVE,  STOPPING;
    










    private Status() {}
  }
  










  HttpServer(int port, InetAddress ifAddress, SocketConfig socketConfig, ServerSocketFactory serverSocketFactory, HttpService httpService, HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory, SSLServerSetupHandler sslSetupHandler, ExceptionLogger exceptionLogger)
  {
    this.port = port;
    this.ifAddress = ifAddress;
    this.socketConfig = socketConfig;
    this.serverSocketFactory = serverSocketFactory;
    this.httpService = httpService;
    this.connectionFactory = connectionFactory;
    this.sslSetupHandler = sslSetupHandler;
    this.exceptionLogger = exceptionLogger;
    listenerExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue(), new ThreadFactoryImpl("HTTP-listener-" + this.port));
    


    workerThreads = new ThreadGroup("HTTP-workers");
    workerExecutorService = new WorkerPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue(), new ThreadFactoryImpl("HTTP-worker", workerThreads));
    


    status = new AtomicReference(Status.READY);
  }
  
  public InetAddress getInetAddress() {
    ServerSocket localSocket = serverSocket;
    if (localSocket != null) {
      return localSocket.getInetAddress();
    }
    return null;
  }
  
  public int getLocalPort()
  {
    ServerSocket localSocket = serverSocket;
    if (localSocket != null) {
      return localSocket.getLocalPort();
    }
    return -1;
  }
  
  public void start() throws IOException
  {
    if (status.compareAndSet(Status.READY, Status.ACTIVE)) {
      serverSocket = serverSocketFactory.createServerSocket(port, socketConfig.getBacklogSize(), ifAddress);
      
      serverSocket.setReuseAddress(socketConfig.isSoReuseAddress());
      if (socketConfig.getRcvBufSize() > 0) {
        serverSocket.setReceiveBufferSize(socketConfig.getRcvBufSize());
      }
      if ((sslSetupHandler != null) && ((serverSocket instanceof SSLServerSocket))) {
        sslSetupHandler.initialize((SSLServerSocket)serverSocket);
      }
      requestListener = new RequestListener(socketConfig, serverSocket, httpService, connectionFactory, exceptionLogger, workerExecutorService);
      





      listenerExecutorService.execute(requestListener);
    }
  }
  
  public void stop() {
    if (status.compareAndSet(Status.ACTIVE, Status.STOPPING)) {
      listenerExecutorService.shutdown();
      workerExecutorService.shutdown();
      RequestListener local = requestListener;
      if (local != null) {
        try {
          local.terminate();
        } catch (IOException ex) {
          exceptionLogger.log(ex);
        }
      }
      workerThreads.interrupt();
    }
  }
  
  public void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
    workerExecutorService.awaitTermination(timeout, timeUnit);
  }
  
  public void shutdown(long gracePeriod, TimeUnit timeUnit) {
    stop();
    if (gracePeriod > 0L) {
      try {
        awaitTermination(gracePeriod, timeUnit);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    Set<Worker> workers = workerExecutorService.getWorkers();
    for (Worker worker : workers) {
      HttpServerConnection conn = worker.getConnection();
      try {
        conn.shutdown();
      } catch (IOException ex) {
        exceptionLogger.log(ex);
      }
    }
  }
}
