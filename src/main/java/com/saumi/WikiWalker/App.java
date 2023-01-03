package com.saumi.WikiWalker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.jsoup.Jsoup;

import com.saumi.WikiWalker.UrlGraph.UrlGraph;
import com.saumi.WikiWalker.UrlGraph.UrlNode;

public class App 
{
	private UrlGraph graph;
	private Set<String> exploredLinks;
	
	private App() {
		graph = new UrlGraph();
		exploredLinks = new HashSet<>();
	}
	
    public static void main(String[] args)
    {
    	try {
    		Stack<UrlNode> solution;
    		if (args.length == 2) {
    			solution = new App().solve(args[0], args[1]);
    		} else {
    			var scanner = new Scanner(System.in);
    			
    			System.out.print("Enter the start link: ");
    			var startUrl = scanner.nextLine().trim();

    			System.out.print("Enter the target link: ");
    			var targetUrl = scanner.nextLine().trim();
    			
    			solution = new App().solve(startUrl, targetUrl);
    			
    			scanner.close();
    		}
    		
    		System.out.println("\n========== Solution ==========\n");
    		System.out.println("It should take " + (solution.size() - 1) + " clicks!\n");
    		
    		System.out.println("Starting here: " + solution.pop());
    		while (solution.size() > 0) {
    			System.out.println("Click on: " + solution.pop());
    		}
    	} catch (Exception e) {
    		System.err.println("An error occurred during the program.");
    		System.err.println(e.getMessage());
    	}
    }
    
    private String convertToFullUrl(String url)
    {
    	if (url.startsWith("https://en.wikipedia.org")) {
    		return url.trim();
    	} else if (url.startsWith("/wiki")) {
    		return ("https://en.wikipedia.org" + url).trim();
    	} else {
    		return null;
    	}
    }
    
    // method to explore all links in a wiki page
    private List<String> exploreLink(String linkIn, UrlNode parent) throws IOException {
    	var fullLink = convertToFullUrl(linkIn);
    	if (fullLink == null) return null;
    	
    	// already explored
    	if (exploredLinks.contains(fullLink)) return null;
    	
    	System.out.println("Exploring " + fullLink);
    	
    	var nextQueue = new ArrayList<String>();
    	
    	var body = Jsoup.connect(fullLink).get().getElementById("bodyContent");
    	var links = body.getElementsByTag("a");
    	
    	for (var link : links) {
        	var url = convertToFullUrl(link.attr("href"));
        	if (url != null) {
        		graph.addNode(link.text(), url, parent);
        		nextQueue.add(url);
        	}
        }
    	
    	exploredLinks.add(fullLink);
    	
    	return nextQueue;
    }
    
    private Stack<UrlNode> solve(String start, String target) throws IOException, IllegalStateException {
    	var startUrl = convertToFullUrl(start);
    	var targetUrl = convertToFullUrl(target);

    	if (startUrl == null || targetUrl == null) {
    		throw new IllegalStateException("Invalid start or target links.");
    	}
    	
    	/// --- Part 1: Generate graph till target found
    	System.out.println("\nGenerating a graph of wikipedia URLs...");
    	
    	var urlSplit = startUrl.split("/");
    	var startName = urlSplit[urlSplit.length - 1];
    	graph.addNode(startName, startUrl);
    	var root = graph.getNode(startUrl);
    	
    	var queue = exploreLink(startUrl, root);
    	
    	while (!queue.contains(targetUrl)) {
    		var nextQueue = new ArrayList<String>();
    		var nodeCount = graph.getNodeCount();
    		
    		for (var link : queue) {
    			if (nextQueue.contains(targetUrl))
    				break;
    			
    			var parent = graph.getNode(link);
    			if (parent == null) throw new IllegalStateException("A link that should be in the graph was not present.");
    			
    			var foundLinks = exploreLink(link, parent);
    			if (foundLinks != null)
    				nextQueue.addAll(foundLinks);
    		}
    		
    		if (!nextQueue.contains(targetUrl) && nodeCount == graph.getNodeCount()) {
    			// means no actual new link was explored... nor was target found
    			throw new RuntimeException("No more links to search.");
    		}
    		queue = nextQueue;
    	}
    	
    	System.out.println("Found the target!");
    	System.out.println("Backtracking to find the solution with least clicks...");
    	
    	/// --- Part 2: Backtrack from target to start to get the solution
    	
    	var solution = new Stack<UrlNode>();
    	var node = graph.getNode(targetUrl);
    	if (backtrack(node, startUrl, solution)) {
    		return solution;
    	} else {
    		throw new RuntimeException("Could not backtrack.");
    	}
    }
    
    private boolean backtrack(UrlNode node, String target, Stack<UrlNode> solution) {
    	solution.push(node);
    	
    	if (node.getUrl().equals(target)) return true;
    	
    	for (var backlink : node.getBacklinks()) {
    		if (backtrack(backlink, target, solution)) {
    			return true;
    		}
    	}
    	solution.pop();
    	
    	return false;
    }
}
