package org.yaml.snakeyaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;






















public class Yaml
{
  protected final Resolver resolver;
  private String name;
  protected BaseConstructor constructor;
  protected Representer representer;
  protected DumperOptions dumperOptions;
  
  public Yaml()
  {
    this(new Constructor(), new Representer(), new DumperOptions(), new Resolver());
  }
  





  public Yaml(DumperOptions dumperOptions)
  {
    this(new Constructor(), new Representer(), dumperOptions);
  }
  






  public Yaml(Representer representer)
  {
    this(new Constructor(), representer);
  }
  






  public Yaml(BaseConstructor constructor)
  {
    this(constructor, new Representer());
  }
  








  public Yaml(BaseConstructor constructor, Representer representer)
  {
    this(constructor, representer, new DumperOptions());
  }
  








  public Yaml(Representer representer, DumperOptions dumperOptions)
  {
    this(new Constructor(), representer, dumperOptions, new Resolver());
  }
  










  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions)
  {
    this(constructor, representer, dumperOptions, new Resolver());
  }
  













  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver)
  {
    if (!constructor.isExplicitPropertyUtils()) {
      constructor.setPropertyUtils(representer.getPropertyUtils());
    } else if (!representer.isExplicitPropertyUtils()) {
      representer.setPropertyUtils(constructor.getPropertyUtils());
    }
    this.constructor = constructor;
    representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
    representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
    representer.getPropertyUtils().setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
    
    representer.setTimeZone(dumperOptions.getTimeZone());
    this.representer = representer;
    this.dumperOptions = dumperOptions;
    this.resolver = resolver;
    name = ("Yaml:" + System.identityHashCode(this));
  }
  






  public String dump(Object data)
  {
    List<Object> list = new ArrayList(1);
    list.add(data);
    return dumpAll(list.iterator());
  }
  








  public Node represent(Object data)
  {
    return representer.represent(data);
  }
  






  public String dumpAll(Iterator<? extends Object> data)
  {
    StringWriter buffer = new StringWriter();
    dumpAll(data, buffer, null);
    return buffer.toString();
  }
  







  public void dump(Object data, Writer output)
  {
    List<Object> list = new ArrayList(1);
    list.add(data);
    dumpAll(list.iterator(), output, null);
  }
  







  public void dumpAll(Iterator<? extends Object> data, Writer output)
  {
    dumpAll(data, output, null);
  }
  
  private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
    Serializer serializer = new Serializer(new Emitter(output, dumperOptions), resolver, dumperOptions, rootTag);
    try
    {
      serializer.open();
      while (data.hasNext()) {
        Node node = representer.represent(data.next());
        serializer.serialize(node);
      }
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    }
  }
  







































  public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle)
  {
    DumperOptions.FlowStyle oldStyle = representer.getDefaultFlowStyle();
    if (flowStyle != null) {
      representer.setDefaultFlowStyle(flowStyle);
    }
    List<Object> list = new ArrayList(1);
    list.add(data);
    StringWriter buffer = new StringWriter();
    dumpAll(list.iterator(), buffer, rootTag);
    representer.setDefaultFlowStyle(oldStyle);
    return buffer.toString();
  }
  


















  public String dumpAsMap(Object data)
  {
    return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
  }
  







  public List<Event> serialize(Node data)
  {
    SilentEmitter emitter = new SilentEmitter(null);
    Serializer serializer = new Serializer(emitter, resolver, dumperOptions, null);
    try {
      serializer.open();
      serializer.serialize(data);
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    }
    return emitter.getEvents(); }
  
  private static class SilentEmitter implements Emitable { private SilentEmitter() {}
    
    private List<Event> events = new ArrayList(100);
    
    public List<Event> getEvents() {
      return events;
    }
    
    public void emit(Event event) throws IOException {
      events.add(event);
    }
  }
  







  public Object load(String yaml)
  {
    return loadFromReader(new StreamReader(yaml), Object.class);
  }
  







  public Object load(InputStream io)
  {
    return loadFromReader(new StreamReader(new UnicodeReader(io)), Object.class);
  }
  







  public Object load(Reader io)
  {
    return loadFromReader(new StreamReader(io), Object.class);
  }
  












  public <T> T loadAs(Reader io, Class<T> type)
  {
    return loadFromReader(new StreamReader(io), type);
  }
  












  public <T> T loadAs(String yaml, Class<T> type)
  {
    return loadFromReader(new StreamReader(yaml), type);
  }
  












  public <T> T loadAs(InputStream input, Class<T> type)
  {
    return loadFromReader(new StreamReader(new UnicodeReader(input)), type);
  }
  
  private Object loadFromReader(StreamReader sreader, Class<?> type) {
    Composer composer = new Composer(new ParserImpl(sreader), resolver);
    constructor.setComposer(composer);
    return constructor.getSingleData(type);
  }
  








  public Iterable<Object> loadAll(Reader yaml)
  {
    Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), resolver);
    constructor.setComposer(composer);
    Iterator<Object> result = new Iterator() {
      public boolean hasNext() {
        return constructor.checkData();
      }
      
      public Object next() {
        return constructor.getData();
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    return new YamlIterable(result);
  }
  
  private static class YamlIterable implements Iterable<Object> {
    private Iterator<Object> iterator;
    
    public YamlIterable(Iterator<Object> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Object> iterator() {
      return iterator;
    }
  }
  









  public Iterable<Object> loadAll(String yaml)
  {
    return loadAll(new StringReader(yaml));
  }
  








  public Iterable<Object> loadAll(InputStream yaml)
  {
    return loadAll(new UnicodeReader(yaml));
  }
  









  public Node compose(Reader yaml)
  {
    Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), resolver);
    constructor.setComposer(composer);
    return composer.getSingleNode();
  }
  








  public Iterable<Node> composeAll(Reader yaml)
  {
    final Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), resolver);
    constructor.setComposer(composer);
    Iterator<Node> result = new Iterator() {
      public boolean hasNext() {
        return composer.checkNode();
      }
      
      public Node next() {
        return composer.getNode();
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    return new NodeIterable(result);
  }
  
  private static class NodeIterable implements Iterable<Node> {
    private Iterator<Node> iterator;
    
    public NodeIterable(Iterator<Node> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Node> iterator() {
      return iterator;
    }
  }
  











  public void addImplicitResolver(Tag tag, Pattern regexp, String first)
  {
    resolver.addImplicitResolver(tag, regexp, first);
  }
  
  public String toString()
  {
    return name;
  }
  






  public String getName()
  {
    return name;
  }
  





  public void setName(String name)
  {
    this.name = name;
  }
  







  public Iterable<Event> parse(Reader yaml)
  {
    final Parser parser = new ParserImpl(new StreamReader(yaml));
    Iterator<Event> result = new Iterator() {
      public boolean hasNext() {
        return parser.peekEvent() != null;
      }
      
      public Event next() {
        return parser.getEvent();
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    return new EventIterable(result);
  }
  
  private static class EventIterable implements Iterable<Event> {
    private Iterator<Event> iterator;
    
    public EventIterable(Iterator<Event> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Event> iterator() {
      return iterator;
    }
  }
  
  public void setBeanAccess(BeanAccess beanAccess) {
    constructor.getPropertyUtils().setBeanAccess(beanAccess);
    representer.getPropertyUtils().setBeanAccess(beanAccess);
  }
}
