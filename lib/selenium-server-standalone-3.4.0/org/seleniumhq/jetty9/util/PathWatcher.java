package org.seleniumhq.jetty9.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




























public class PathWatcher
  extends AbstractLifeCycle
  implements Runnable
{
  private static final boolean IS_WINDOWS;
  
  public static class Config
  {
    public static final int UNLIMITED_DEPTH = -9999;
    private static final String PATTERN_SEP;
    protected final Path dir;
    
    static
    {
      String sep = File.separator;
      if (File.separatorChar == '\\')
      {
        sep = "\\\\";
      }
      PATTERN_SEP = sep;
    }
    

    protected int recurseDepth = 0;
    protected List<PathMatcher> includes;
    protected List<PathMatcher> excludes;
    protected boolean excludeHidden = false;
    
    public Config(Path path)
    {
      dir = path;
      includes = new ArrayList();
      excludes = new ArrayList();
    }
    






    public void addExclude(PathMatcher matcher)
    {
      excludes.add(matcher);
    }
    









    public void addExclude(String syntaxAndPattern)
    {
      if (PathWatcher.LOG.isDebugEnabled())
      {
        PathWatcher.LOG.debug("Adding exclude: [{}]", new Object[] { syntaxAndPattern });
      }
      addExclude(dir.getFileSystem().getPathMatcher(syntaxAndPattern));
    }
    

















    public void addExcludeGlobRelative(String pattern)
    {
      addExclude(toGlobPattern(dir, pattern));
    }
    



    public void addExcludeHidden()
    {
      if (!excludeHidden)
      {
        if (PathWatcher.LOG.isDebugEnabled())
        {
          PathWatcher.LOG.debug("Adding hidden files and directories to exclusions", new Object[0]);
        }
        excludeHidden = true;
        
        addExclude("regex:^.*" + PATTERN_SEP + "\\..*$");
        addExclude("regex:^.*" + PATTERN_SEP + "\\..*" + PATTERN_SEP + ".*$");
      }
    }
    







    public void addExcludes(List<String> syntaxAndPatterns)
    {
      for (String syntaxAndPattern : syntaxAndPatterns)
      {
        addExclude(syntaxAndPattern);
      }
    }
    






    public void addInclude(PathMatcher matcher)
    {
      includes.add(matcher);
    }
    







    public void addInclude(String syntaxAndPattern)
    {
      if (PathWatcher.LOG.isDebugEnabled())
      {
        PathWatcher.LOG.debug("Adding include: [{}]", new Object[] { syntaxAndPattern });
      }
      addInclude(dir.getFileSystem().getPathMatcher(syntaxAndPattern));
    }
    

















    public void addIncludeGlobRelative(String pattern)
    {
      addInclude(toGlobPattern(dir, pattern));
    }
    







    public void addIncludes(List<String> syntaxAndPatterns)
    {
      for (String syntaxAndPattern : syntaxAndPatterns)
      {
        addInclude(syntaxAndPattern);
      }
    }
    









    public Config asSubConfig(Path dir)
    {
      Config subconfig = new Config(dir);
      includes = includes;
      excludes = excludes;
      if (dir == this.dir) {
        recurseDepth = recurseDepth;

      }
      else if (recurseDepth == 55537) {
        recurseDepth = 55537;
      } else {
        recurseDepth -= dir.getNameCount() - this.dir.getNameCount();
      }
      return subconfig;
    }
    
    public int getRecurseDepth()
    {
      return recurseDepth;
    }
    
    public boolean isRecurseDepthUnlimited()
    {
      return recurseDepth == 55537;
    }
    
    public Path getPath()
    {
      return dir;
    }
    
    private boolean hasMatch(Path path, List<PathMatcher> matchers)
    {
      for (PathMatcher matcher : matchers)
      {
        if (matcher.matches(path))
        {
          return true;
        }
      }
      return false;
    }
    
    public boolean isExcluded(Path dir) throws IOException
    {
      if (excludeHidden)
      {
        if (Files.isHidden(dir))
        {
          if (PathWatcher.NOISY_LOG.isDebugEnabled())
          {
            PathWatcher.NOISY_LOG.debug("isExcluded [Hidden] on {}", new Object[] { dir });
          }
          return true;
        }
      }
      
      if (excludes.isEmpty())
      {

        return false;
      }
      
      boolean matched = hasMatch(dir, excludes);
      if (PathWatcher.NOISY_LOG.isDebugEnabled())
      {
        PathWatcher.NOISY_LOG.debug("isExcluded [{}] on {}", new Object[] { Boolean.valueOf(matched), dir });
      }
      return matched;
    }
    
    public boolean isIncluded(Path dir)
    {
      if (includes.isEmpty())
      {

        if (PathWatcher.NOISY_LOG.isDebugEnabled())
        {
          PathWatcher.NOISY_LOG.debug("isIncluded [All] on {}", new Object[] { dir });
        }
        return true;
      }
      
      boolean matched = hasMatch(dir, includes);
      if (PathWatcher.NOISY_LOG.isDebugEnabled())
      {
        PathWatcher.NOISY_LOG.debug("isIncluded [{}] on {}", new Object[] { Boolean.valueOf(matched), dir });
      }
      return matched;
    }
    
    public boolean matches(Path path)
    {
      try
      {
        return (!isExcluded(path)) && (isIncluded(path));
      }
      catch (IOException e)
      {
        PathWatcher.LOG.warn("Unable to match path: " + path, e); }
      return false;
    }
    









    public void setRecurseDepth(int depth)
    {
      recurseDepth = depth;
    }
    









    public boolean shouldRecurseDirectory(Path child)
    {
      if (!child.startsWith(dir))
      {

        return false;
      }
      

      if (isRecurseDepthUnlimited()) {
        return true;
      }
      
      int childDepth = dir.relativize(child).getNameCount();
      return childDepth <= recurseDepth;
    }
    
    private String toGlobPattern(Path path, String subPattern)
    {
      StringBuilder s = new StringBuilder();
      s.append("glob:");
      
      boolean needDelim = false;
      

      Path root = path.getRoot();
      if (root != null)
      {
        if (PathWatcher.NOISY_LOG.isDebugEnabled())
        {
          PathWatcher.NOISY_LOG.debug("Path: {} -> Root: {}", new Object[] { path, root });
        }
        for (char c : root.toString().toCharArray())
        {
          if (c == '\\')
          {
            s.append(PATTERN_SEP);
          }
          else
          {
            s.append(c);
          }
        }
      }
      else
      {
        needDelim = true;
      }
      

      for (??? = path.iterator(); ((Iterator)???).hasNext();) { segment = (Path)((Iterator)???).next();
        
        if (needDelim)
        {
          s.append(PATTERN_SEP);
        }
        s.append(segment);
        needDelim = true;
      }
      
      Path segment;
      if ((subPattern != null) && (subPattern.length() > 0))
      {
        if (needDelim)
        {
          s.append(PATTERN_SEP);
        }
        for (char c : subPattern.toCharArray())
        {
          if (c == '/')
          {
            s.append(PATTERN_SEP);
          }
          else
          {
            s.append(c);
          }
        }
      }
      
      return s.toString();
    }
    

    public String toString()
    {
      StringBuilder s = new StringBuilder();
      s.append(dir);
      if (recurseDepth > 0)
      {
        s.append(" [depth=").append(recurseDepth).append("]");
      }
      return s.toString();
    }
  }
  
  public static class DepthLimitedFileVisitor extends SimpleFileVisitor<Path>
  {
    private PathWatcher.Config base;
    private PathWatcher watcher;
    
    public DepthLimitedFileVisitor(PathWatcher watcher, PathWatcher.Config base)
    {
      this.base = base;
      this.watcher = watcher;
    }
    
























    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
      throws IOException
    {
      if (!base.isExcluded(dir))
      {
        if (base.isIncluded(dir))
        {
          if (watcher.isNotifiable())
          {


            PathWatcher.PathWatchEvent event = new PathWatcher.PathWatchEvent(dir, PathWatcher.PathWatchEventType.ADDED);
            if (PathWatcher.LOG.isDebugEnabled())
            {
              PathWatcher.LOG.debug("Pending {}", new Object[] { event });
            }
            watcher.addToPendingList(dir, event);
          }
        }
        





        if (((base.getPath().equals(dir)) && ((base.isRecurseDepthUnlimited()) || (base.getRecurseDepth() >= 0))) || (base.shouldRecurseDirectory(dir))) {
          watcher.register(dir, base);
        }
      }
      




      if (((base.getPath().equals(dir)) && ((base.isRecurseDepthUnlimited()) || (base.getRecurseDepth() >= 0))) || (base.shouldRecurseDirectory(dir))) {
        return FileVisitResult.CONTINUE;
      }
      return FileVisitResult.SKIP_SUBTREE;
    }
    




    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
      throws IOException
    {
      if ((base.matches(file)) && (watcher.isNotifiable()))
      {
        PathWatcher.PathWatchEvent event = new PathWatcher.PathWatchEvent(file, PathWatcher.PathWatchEventType.ADDED);
        if (PathWatcher.LOG.isDebugEnabled())
        {
          PathWatcher.LOG.debug("Pending {}", new Object[] { event });
        }
        watcher.addToPendingList(file, event);
      }
      
      return FileVisitResult.CONTINUE;
    }
  }
  



  public static abstract interface Listener
    extends EventListener
  {
    public abstract void onPathWatchEvent(PathWatcher.PathWatchEvent paramPathWatchEvent);
  }
  



  public static abstract interface EventListListener
    extends EventListener
  {
    public abstract void onPathWatchEvents(List<PathWatcher.PathWatchEvent> paramList);
  }
  


  public static class PathWatchEvent
  {
    private final Path path;
    

    private final PathWatcher.PathWatchEventType type;
    

    private int count = 0;
    
    public PathWatchEvent(Path path, PathWatcher.PathWatchEventType type)
    {
      this.path = path;
      count = 1;
      this.type = type;
    }
    

    public PathWatchEvent(Path path, WatchEvent<Path> event)
    {
      this.path = path;
      count = event.count();
      if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
      {
        type = PathWatcher.PathWatchEventType.ADDED;
      }
      else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
      {
        type = PathWatcher.PathWatchEventType.DELETED;
      }
      else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
      {
        type = PathWatcher.PathWatchEventType.MODIFIED;
      }
      else
      {
        type = PathWatcher.PathWatchEventType.UNKNOWN;
      }
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
      PathWatchEvent other = (PathWatchEvent)obj;
      if (path == null)
      {
        if (path != null)
        {
          return false;
        }
      }
      else if (!path.equals(path))
      {
        return false;
      }
      if (type != type)
      {
        return false;
      }
      return true;
    }
    
    public Path getPath()
    {
      return path;
    }
    
    public PathWatcher.PathWatchEventType getType()
    {
      return type;
    }
    
    public void incrementCount(int num)
    {
      count += num;
    }
    
    public int getCount()
    {
      return count;
    }
    




    public int hashCode()
    {
      int prime = 31;
      int result = 1;
      result = 31 * result + (path == null ? 0 : path.hashCode());
      result = 31 * result + (type == null ? 0 : type.hashCode());
      return result;
    }
    




    public String toString()
    {
      return String.format("PathWatchEvent[%s|%s]", new Object[] { type, path });
    }
  }
  



  public static class PathPendingEvents
  {
    private Path _path;
    

    private List<PathWatcher.PathWatchEvent> _events;
    

    private long _timestamp;
    

    private long _lastFileSize = -1L;
    
    public PathPendingEvents(Path path)
    {
      _path = path;
    }
    
    public PathPendingEvents(Path path, PathWatcher.PathWatchEvent event)
    {
      this(path);
      addEvent(event);
    }
    
    public void addEvent(PathWatcher.PathWatchEvent event)
    {
      long now = System.currentTimeMillis();
      _timestamp = now;
      
      if (_events == null)
      {
        _events = new ArrayList();
        _events.add(event);

      }
      else
      {

        PathWatcher.PathWatchEvent existingType = null;
        for (PathWatcher.PathWatchEvent e : _events)
        {
          if (e.getType() == event.getType())
          {
            existingType = e;
            break;
          }
        }
        
        if (existingType == null)
        {
          _events.add(event);
        }
        else
        {
          existingType.incrementCount(event.getCount());
        }
      }
    }
    

    public List<PathWatcher.PathWatchEvent> getEvents()
    {
      return _events;
    }
    
    public long getTimestamp()
    {
      return _timestamp;
    }
    

















    public boolean isQuiet(long now, long expiredDuration, TimeUnit expiredUnit)
    {
      long pastdue = _timestamp + expiredUnit.toMillis(expiredDuration);
      _timestamp = now;
      
      long fileSize = _path.toFile().length();
      boolean fileSizeChanged = _lastFileSize != fileSize;
      _lastFileSize = fileSize;
      
      if ((now > pastdue) && (!fileSizeChanged))
      {



        return true;
      }
      
      return false;
    }
  }
  






  public static enum PathWatchEventType
  {
    ADDED,  DELETED,  MODIFIED,  UNKNOWN;
    
    private PathWatchEventType() {}
  }
  
  static
  {
    String os = System.getProperty("os.name");
    if (os == null)
    {
      IS_WINDOWS = false;
    }
    else
    {
      String osl = os.toLowerCase(Locale.ENGLISH);
      IS_WINDOWS = osl.contains("windows");
    }
  }
  
  private static final Logger LOG = Log.getLogger(PathWatcher.class);
  


  private static final Logger NOISY_LOG = Log.getLogger(PathWatcher.class.getName() + ".Noisy");
  

  protected static <T> WatchEvent<T> cast(WatchEvent<?> event)
  {
    return event;
  }
  
  private static final WatchEvent.Kind<?>[] WATCH_EVENT_KINDS = { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY };
  
  private WatchService watchService;
  
  private WatchEvent.Modifier[] watchModifiers;
  private boolean nativeWatchService;
  private Map<WatchKey, Config> keys = new HashMap();
  private List<EventListener> listeners = new CopyOnWriteArrayList();
  private List<Config> configs = new ArrayList();
  



  private long updateQuietTimeDuration = 1000L;
  private TimeUnit updateQuietTimeUnit = TimeUnit.MILLISECONDS;
  private Thread thread;
  private boolean _notifyExistingOnStart = true;
  private Map<Path, PathPendingEvents> pendingEvents = new LinkedHashMap();
  



















  public void watch(Path file)
  {
    Path abs = file;
    if (!abs.isAbsolute())
    {
      abs = file.toAbsolutePath();
    }
    


    Config config = null;
    Path parent = abs.getParent();
    for (Config c : configs)
    {
      if (c.getPath().equals(parent))
      {
        config = c;
        break;
      }
    }
    

    if (config == null)
    {
      config = new Config(abs.getParent());
      
      config.addIncludeGlobRelative("");
      
      config.addIncludeGlobRelative(file.getFileName().toString());
      watch(config);
    }
    else
    {
      config.addIncludeGlobRelative(file.getFileName().toString());
    }
  }
  






  public void watch(Config config)
  {
    configs.add(config);
  }
  






  protected void prepareConfig(Config baseDir)
    throws IOException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Watching directory {}", new Object[] { baseDir });
    }
    Files.walkFileTree(baseDir.getPath(), new DepthLimitedFileVisitor(this, baseDir));
  }
  







  public void addListener(EventListener listener)
  {
    listeners.add(listener);
  }
  





  private void appendConfigId(StringBuilder s)
  {
    List<Path> dirs = new ArrayList();
    
    for (Config config : keys.values())
    {
      dirs.add(dir);
    }
    
    Collections.sort(dirs);
    
    s.append("[");
    if (dirs.size() > 0)
    {
      s.append(dirs.get(0));
      if (dirs.size() > 1)
      {
        s.append(" (+").append(dirs.size() - 1).append(")");
      }
    }
    else
    {
      s.append("<null>");
    }
    s.append("]");
  }
  




  protected void doStart()
    throws Exception
  {
    createWatchService();
    

    setUpdateQuietTime(getUpdateQuietTimeMillis(), TimeUnit.MILLISECONDS);
    


    for (Config c : configs) {
      prepareConfig(c);
    }
    
    StringBuilder threadId = new StringBuilder();
    threadId.append("PathWatcher-Thread");
    appendConfigId(threadId);
    
    thread = new Thread(this, threadId.toString());
    thread.setDaemon(true);
    thread.start();
    super.doStart();
  }
  



  protected void doStop()
    throws Exception
  {
    if (watchService != null)
      watchService.close();
    watchService = null;
    thread = null;
    keys.clear();
    pendingEvents.clear();
    super.doStop();
  }
  




  public void reset()
  {
    if (!isStopped()) {
      throw new IllegalStateException("PathWatcher must be stopped before reset.");
    }
    configs.clear();
    listeners.clear();
  }
  







  private void createWatchService()
    throws IOException
  {
    watchService = FileSystems.getDefault().newWatchService();
    
    WatchEvent.Modifier[] modifiers = null;
    boolean nativeService = true;
    

    try
    {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Class<?> pollingWatchServiceClass = Class.forName("sun.nio.fs.PollingWatchService", false, cl);
      if (pollingWatchServiceClass.isAssignableFrom(watchService.getClass()))
      {
        nativeService = false;
        LOG.info("Using Non-Native Java {}", new Object[] { pollingWatchServiceClass.getName() });
        Class<?> c = Class.forName("com.sun.nio.file.SensitivityWatchEventModifier");
        Field f = c.getField("HIGH");
        

        modifiers = new WatchEvent.Modifier[] {(WatchEvent.Modifier)f.get(c) };
      }
      

    }
    catch (Throwable t)
    {
      LOG.ignore(t);
    }
    
    watchModifiers = modifiers;
    nativeWatchService = nativeService;
  }
  







  protected boolean isNotifiable()
  {
    return (isStarted()) || ((!isStarted()) && (isNotifyExistingOnStart()));
  }
  





  public Iterator<EventListener> getListeners()
  {
    return listeners.iterator();
  }
  





  public long getUpdateQuietTimeMillis()
  {
    return TimeUnit.MILLISECONDS.convert(updateQuietTimeDuration, updateQuietTimeUnit);
  }
  





  protected void notifyOnPathWatchEvents(List<PathWatchEvent> events)
  {
    if ((events == null) || (events.isEmpty())) {
      return;
    }
    for (EventListener listener : listeners)
    {
      if ((listener instanceof EventListListener))
      {
        try
        {
          ((EventListListener)listener).onPathWatchEvents(events);
        }
        catch (Throwable t)
        {
          LOG.warn(t);
        }
      }
      else
      {
        l = (Listener)listener;
        for (PathWatchEvent event : events)
        {
          try
          {
            l.onPathWatchEvent(event);
          }
          catch (Throwable t)
          {
            LOG.warn(t);
          }
        }
      }
    }
    



    Listener l;
  }
  



  protected void register(Path dir, Config root)
    throws IOException
  {
    LOG.debug("Registering watch on {}", new Object[] { dir });
    if (watchModifiers != null)
    {

      WatchKey key = dir.register(watchService, WATCH_EVENT_KINDS, watchModifiers);
      keys.put(key, root.asSubConfig(dir));
    }
    else
    {
      WatchKey key = dir.register(watchService, WATCH_EVENT_KINDS);
      keys.put(key, root.asSubConfig(dir));
    }
  }
  






  public boolean removeListener(Listener listener)
  {
    return listeners.remove(listener);
  }
  




















  public void run()
  {
    List<PathWatchEvent> notifiableEvents = new ArrayList();
    

    if (LOG.isDebugEnabled())
    {
      LOG.debug("Starting java.nio file watching with {}", new Object[] { watchService });
    }
    
    while ((watchService != null) && (thread == Thread.currentThread()))
    {
      WatchKey key = null;
      

      try
      {
        if (pendingEvents.isEmpty())
        {
          if (NOISY_LOG.isDebugEnabled())
            NOISY_LOG.debug("Waiting for take()", new Object[0]);
          key = watchService.take();

        }
        else
        {

          if (NOISY_LOG.isDebugEnabled()) {
            NOISY_LOG.debug("Waiting for poll({}, {})", new Object[] { Long.valueOf(updateQuietTimeDuration), updateQuietTimeUnit });
          }
          key = watchService.poll(updateQuietTimeDuration, updateQuietTimeUnit);
          

          if (key == null)
          {
            now = System.currentTimeMillis();
            
            for (Path path : new HashSet(pendingEvents.keySet()))
            {
              PathPendingEvents pending = (PathPendingEvents)pendingEvents.get(path);
              if (pending.isQuiet(now, updateQuietTimeDuration, updateQuietTimeUnit))
              {


                for (PathWatchEvent p : pending.getEvents())
                {
                  notifiableEvents.add(p);
                }
                
                pendingEvents.remove(path);
              }
            }
          }
        }
      }
      catch (ClosedWatchServiceException e)
      {
        long now;
        return;
      }
      catch (InterruptedException e)
      {
        if (isRunning())
        {
          LOG.warn(e);
        }
        else
        {
          LOG.ignore(e);
        }
        
        return;
      }
      Config config;
      if (key != null)
      {

        config = (Config)keys.get(key);
        if (config == null)
        {
          if (LOG.isDebugEnabled())
          {
            LOG.debug("WatchKey not recognized: {}", new Object[] { key });
          }
          
        }
        else {
          for (Object event : key.pollEvents())
          {

            WatchEvent.Kind<Path> kind = ((WatchEvent)event).kind();
            WatchEvent<Path> ev = cast((WatchEvent)event);
            Path name = (Path)ev.context();
            Path child = dir.resolve(name);
            
            if (kind == StandardWatchEventKinds.ENTRY_CREATE)
            {


              if (Files.isDirectory(child, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
              {
                try
                {
                  prepareConfig(config.asSubConfig(child));
                }
                catch (IOException e)
                {
                  LOG.warn(e);
                }
                
              } else if (config.matches(child))
              {
                addToPendingList(child, new PathWatchEvent(child, ev));
              }
            }
            else if (config.matches(child))
            {
              addToPendingList(child, new PathWatchEvent(child, ev));
            }
          }
        }
      }
      else {
        notifyOnPathWatchEvents(notifiableEvents);
        notifiableEvents.clear();
        
        if ((key != null) && (!key.reset()))
        {
          keys.remove(key);
          if (keys.isEmpty())
          {
            return;
          }
        }
      }
    }
  }
  







  public void addToPendingList(Path path, PathWatchEvent event)
  {
    PathPendingEvents pending = (PathPendingEvents)pendingEvents.get(path);
    

    if (pending == null)
    {

      pendingEvents.put(path, new PathPendingEvents(path, event));

    }
    else
    {
      pending.addEvent(event);
    }
  }
  







  public void setNotifyExistingOnStart(boolean notify)
  {
    _notifyExistingOnStart = notify;
  }
  
  public boolean isNotifyExistingOnStart()
  {
    return _notifyExistingOnStart;
  }
  






  public void setUpdateQuietTime(long duration, TimeUnit unit)
  {
    long desiredMillis = unit.toMillis(duration);
    
    if ((watchService != null) && (!nativeWatchService) && (desiredMillis < 5000L))
    {
      LOG.warn("Quiet Time is too low for non-native WatchService [{}]: {} < 5000 ms (defaulting to 5000 ms)", new Object[] { watchService.getClass().getName(), Long.valueOf(desiredMillis) });
      updateQuietTimeDuration = 5000L;
      updateQuietTimeUnit = TimeUnit.MILLISECONDS;
      return;
    }
    
    if ((IS_WINDOWS) && (desiredMillis < 1000L))
    {
      LOG.warn("Quiet Time is too low for Microsoft Windows: {} < 1000 ms (defaulting to 1000 ms)", new Object[] { Long.valueOf(desiredMillis) });
      updateQuietTimeDuration = 1000L;
      updateQuietTimeUnit = TimeUnit.MILLISECONDS;
      return;
    }
    

    updateQuietTimeDuration = duration;
    updateQuietTimeUnit = unit;
  }
  

  public String toString()
  {
    StringBuilder s = new StringBuilder(getClass().getName());
    appendConfigId(s);
    return s.toString();
  }
  
  public PathWatcher() {}
}
