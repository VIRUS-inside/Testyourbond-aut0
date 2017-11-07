package org.yaml.snakeyaml.representer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;


















public abstract class BaseRepresenter
{
  protected final Map<Class<?>, Represent> representers = new HashMap();
  
  protected Represent nullRepresenter;
  

  public BaseRepresenter() {}
  

  protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap();
  protected Character defaultScalarStyle;
  protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
  protected final Map<Object, Node> representedObjects = new IdentityHashMap() {
    private static final long serialVersionUID = -5576159264232131854L;
    
    public Node put(Object key, Node value) {
      return (Node)super.put(key, new AnchorNode(value));
    }
  };
  
  protected Object objectToRepresent;
  private PropertyUtils propertyUtils;
  private boolean explicitPropertyUtils = false;
  
  public Node represent(Object data) {
    Node node = representData(data);
    representedObjects.clear();
    objectToRepresent = null;
    return node;
  }
  
  protected final Node representData(Object data) {
    objectToRepresent = data;
    
    if (representedObjects.containsKey(objectToRepresent)) {
      Node node = (Node)representedObjects.get(objectToRepresent);
      return node;
    }
    

    if (data == null) {
      Node node = nullRepresenter.representData(null);
      return node;
    }
    

    Class<?> clazz = data.getClass();
    Node node; Node node; if (representers.containsKey(clazz)) {
      Represent representer = (Represent)representers.get(clazz);
      node = representer.representData(data);
    }
    else {
      for (Class<?> repr : multiRepresenters.keySet()) {
        if (repr.isInstance(data)) {
          Represent representer = (Represent)multiRepresenters.get(repr);
          Node node = representer.representData(data);
          return node;
        }
      }
      
      Node node;
      if (multiRepresenters.containsKey(null)) {
        Represent representer = (Represent)multiRepresenters.get(null);
        node = representer.representData(data);
      } else {
        Represent representer = (Represent)representers.get(null);
        node = representer.representData(data);
      }
    }
    return node;
  }
  
  protected Node representScalar(Tag tag, String value, Character style) {
    if (style == null) {
      style = defaultScalarStyle;
    }
    Node node = new ScalarNode(tag, value, null, null, style);
    return node;
  }
  
  protected Node representScalar(Tag tag, String value) {
    return representScalar(tag, value, null);
  }
  
  protected Node representSequence(Tag tag, Iterable<?> sequence, Boolean flowStyle) {
    int size = 10;
    if ((sequence instanceof List)) {
      size = ((List)sequence).size();
    }
    List<Node> value = new ArrayList(size);
    SequenceNode node = new SequenceNode(tag, value, flowStyle);
    representedObjects.put(objectToRepresent, node);
    boolean bestStyle = true;
    for (Object item : sequence) {
      Node nodeItem = representData(item);
      if ((!(nodeItem instanceof ScalarNode)) || (((ScalarNode)nodeItem).getStyle() != null)) {
        bestStyle = false;
      }
      value.add(nodeItem);
    }
    if (flowStyle == null) {
      if (defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
        node.setFlowStyle(defaultFlowStyle.getStyleBoolean());
      } else {
        node.setFlowStyle(Boolean.valueOf(bestStyle));
      }
    }
    return node;
  }
  
  protected Node representMapping(Tag tag, Map<?, ?> mapping, Boolean flowStyle) {
    List<NodeTuple> value = new ArrayList(mapping.size());
    MappingNode node = new MappingNode(tag, value, flowStyle);
    representedObjects.put(objectToRepresent, node);
    boolean bestStyle = true;
    for (Map.Entry<?, ?> entry : mapping.entrySet()) {
      Node nodeKey = representData(entry.getKey());
      Node nodeValue = representData(entry.getValue());
      if ((!(nodeKey instanceof ScalarNode)) || (((ScalarNode)nodeKey).getStyle() != null)) {
        bestStyle = false;
      }
      if ((!(nodeValue instanceof ScalarNode)) || (((ScalarNode)nodeValue).getStyle() != null)) {
        bestStyle = false;
      }
      value.add(new NodeTuple(nodeKey, nodeValue));
    }
    if (flowStyle == null) {
      if (defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
        node.setFlowStyle(defaultFlowStyle.getStyleBoolean());
      } else {
        node.setFlowStyle(Boolean.valueOf(bestStyle));
      }
    }
    return node;
  }
  
  public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
    defaultScalarStyle = defaultStyle.getChar();
  }
  
  public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
    this.defaultFlowStyle = defaultFlowStyle;
  }
  
  public DumperOptions.FlowStyle getDefaultFlowStyle() {
    return defaultFlowStyle;
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    this.propertyUtils = propertyUtils;
    explicitPropertyUtils = true;
  }
  
  public final PropertyUtils getPropertyUtils() {
    if (propertyUtils == null) {
      propertyUtils = new PropertyUtils();
    }
    return propertyUtils;
  }
  
  public final boolean isExplicitPropertyUtils() {
    return explicitPropertyUtils;
  }
}
