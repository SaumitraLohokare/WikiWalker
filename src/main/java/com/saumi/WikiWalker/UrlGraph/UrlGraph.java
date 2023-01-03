package com.saumi.WikiWalker.UrlGraph;

import java.util.HashMap;
import java.util.Map;

public class UrlGraph {
	private Map<String, UrlNode> nodes;
	
	public UrlGraph() {
		nodes = new HashMap<>();
	}
	
	public void addNode(String label, String url) {
		if (!nodes.containsKey(url)) {
			var node = new UrlNode(label, url);
			nodes.put(url, node);
		}
	}
	
	public void addNode(String label, String url, UrlNode parent) {
		if (nodes.containsKey(url)) {
			var node = nodes.get(url);
			node.addBacklink(parent);
		} else {
			var node = new UrlNode(label, url, parent);
			nodes.put(url, node);
		}
	}
	
	public UrlNode getNode(String url) {
		if (nodes.containsKey(url))
			return nodes.get(url);
		else
			return null;
	}
	
	public int getNodeCount() {
		return nodes.size();
	}
	
	public boolean containsKey(String url) {
		return nodes.containsKey(url);
	}
}
