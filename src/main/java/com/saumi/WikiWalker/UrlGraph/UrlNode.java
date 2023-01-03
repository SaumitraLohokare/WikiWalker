package com.saumi.WikiWalker.UrlGraph;

import java.util.ArrayList;
import java.util.List;

public class UrlNode {
	private String url;
	private String label;
	private List<UrlNode> backlinks;
	
	public UrlNode(String label, String url) {
		this.label = label;
		this.url = url;
		this.backlinks = new ArrayList<>();
	}
	
	public UrlNode(String label, String url, UrlNode parent) {
		this.label = label;
		this.url = url;
		this.backlinks = new ArrayList<>();
		this.backlinks.add(parent);
	}

	public String getUrl() {
		return url;
	}

	public List<UrlNode> getBacklinks() {
		return backlinks;
	}
	
	public void addBacklink(UrlNode backlink) {
		this.backlinks.add(backlink);
	}

	public String getLabel() {
		return label;
	}
	
	public String toString() {
		return "{ " + label + " | Link: " + url + " }";
	}
}
