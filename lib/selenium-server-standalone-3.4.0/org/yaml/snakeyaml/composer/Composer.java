package org.yaml.snakeyaml.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;




















public class Composer
{
  private final Parser parser;
  private final Resolver resolver;
  private final Map<String, Node> anchors;
  private final Set<Node> recursiveNodes;
  
  public Composer(Parser parser, Resolver resolver)
  {
    this.parser = parser;
    this.resolver = resolver;
    anchors = new HashMap();
    recursiveNodes = new HashSet();
  }
  





  public boolean checkNode()
  {
    if (parser.checkEvent(Event.ID.StreamStart)) {
      parser.getEvent();
    }
    
    return !parser.checkEvent(Event.ID.StreamEnd);
  }
  






  public Node getNode()
  {
    if (!parser.checkEvent(Event.ID.StreamEnd)) {
      return composeDocument();
    }
    return null;
  }
  










  public Node getSingleNode()
  {
    parser.getEvent();
    
    Node document = null;
    if (!parser.checkEvent(Event.ID.StreamEnd)) {
      document = composeDocument();
    }
    
    if (!parser.checkEvent(Event.ID.StreamEnd)) {
      Event event = parser.getEvent();
      throw new ComposerException("expected a single document in the stream", document.getStartMark(), "but found another document", event.getStartMark());
    }
    

    parser.getEvent();
    return document;
  }
  
  private Node composeDocument()
  {
    parser.getEvent();
    
    Node node = composeNode(null);
    
    parser.getEvent();
    anchors.clear();
    recursiveNodes.clear();
    return node;
  }
  
  private Node composeNode(Node parent) {
    recursiveNodes.add(parent);
    if (parser.checkEvent(Event.ID.Alias)) {
      AliasEvent event = (AliasEvent)parser.getEvent();
      String anchor = event.getAnchor();
      if (!anchors.containsKey(anchor)) {
        throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark());
      }
      
      Node result = (Node)anchors.get(anchor);
      if (recursiveNodes.remove(result)) {
        result.setTwoStepsConstruction(true);
      }
      return result;
    }
    NodeEvent event = (NodeEvent)parser.peekEvent();
    String anchor = null;
    anchor = event.getAnchor();
    
    Node node = null;
    if (parser.checkEvent(Event.ID.Scalar)) {
      node = composeScalarNode(anchor);
    } else if (parser.checkEvent(Event.ID.SequenceStart)) {
      node = composeSequenceNode(anchor);
    } else {
      node = composeMappingNode(anchor);
    }
    recursiveNodes.remove(parent);
    return node;
  }
  
  private Node composeScalarNode(String anchor) {
    ScalarEvent ev = (ScalarEvent)parser.getEvent();
    String tag = ev.getTag();
    boolean resolved = false;
    Tag nodeTag;
    if ((tag == null) || (tag.equals("!"))) {
      Tag nodeTag = resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
      
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    }
    Node node = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getStyle());
    
    if (anchor != null) {
      anchors.put(anchor, node);
    }
    return node;
  }
  
  private Node composeSequenceNode(String anchor) {
    SequenceStartEvent startEvent = (SequenceStartEvent)parser.getEvent();
    String tag = startEvent.getTag();
    
    boolean resolved = false;
    Tag nodeTag; if ((tag == null) || (tag.equals("!"))) {
      Tag nodeTag = resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    }
    ArrayList<Node> children = new ArrayList();
    SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    
    if (anchor != null) {
      anchors.put(anchor, node);
    }
    while (!parser.checkEvent(Event.ID.SequenceEnd)) {
      children.add(composeNode(node));
    }
    Event endEvent = parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return node;
  }
  
  private Node composeMappingNode(String anchor) {
    MappingStartEvent startEvent = (MappingStartEvent)parser.getEvent();
    String tag = startEvent.getTag();
    
    boolean resolved = false;
    Tag nodeTag; if ((tag == null) || (tag.equals("!"))) {
      Tag nodeTag = resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    }
    
    List<NodeTuple> children = new ArrayList();
    MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    
    if (anchor != null) {
      anchors.put(anchor, node);
    }
    while (!parser.checkEvent(Event.ID.MappingEnd)) {
      Node itemKey = composeNode(node);
      if (itemKey.getTag().equals(Tag.MERGE)) {
        node.setMerged(true);
      }
      Node itemValue = composeNode(node);
      children.add(new NodeTuple(itemKey, itemValue));
    }
    Event endEvent = parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return node;
  }
}
