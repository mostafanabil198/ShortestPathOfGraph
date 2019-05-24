/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.filestructure.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

/**
 *
 * @author arabtech
 */
public class Graph implements IGraph {
	private ArrayList<Integer> dijkstraNodes=new ArrayList<>();
	private ArrayList<Pair> adj[];
    private int[] dijkstraDistance;
	private class Edge {

		int s, d, w;

		Edge(int s, int d, int w) {
			this.s = s;
			this.d = d;
			this.w = w;
		}
	}
	private class Pair{
		int first,second;
		public Pair(int first,int second) {
			this.first=first;
			this.second=second;
		}
	}

	public Graph() {
		v = 0;
		e = 0;
		edges = new ArrayList<>();
		vertices = new ArrayList<>();
		dijkstraDistance=new int[v];
	}

	private int v, e;
	private ArrayList<Edge> edges;
	private ArrayList<Integer> vertices;
	private Scanner scan;
	private final int INF = Integer.MAX_VALUE / 2;

	@SuppressWarnings("unchecked")
	@Override
	public void readGraph(File file) {
		try {
			scan = new Scanner(file);
			String[] sizes;
			sizes = scan.nextLine().split(" ");
			v = Integer.parseInt(sizes[0]);
			e = Integer.parseInt(sizes[1]);
			edges = new ArrayList<>();
			vertices = new ArrayList<>();
			for (int i = 0; i < v; i++) {
				vertices.add(i);
			}
			adj = new ArrayList[v];
            for (int i = 0; i < v; i++) {
                adj[i] = new ArrayList<Pair>();
            }
			for (int i = 0; i < e; i++) {
				String[] line;
				line = scan.nextLine().split(" ");
				int s = Integer.parseInt(line[0]);
				int d = Integer.parseInt(line[1]);
				int w = Integer.parseInt(line[2]);
				adj[s].add(new Pair(d, w));
				Edge edge = new Edge(s, d, w);
				edges.add(edge);
			}
			if(scan.hasNextLine()) {
				throw new RuntimeErrorException(new Error(""));
			}

		} catch (FileNotFoundException ex) {
			throw new RuntimeErrorException(new Error(""));
		}

	}

	@Override
	public int size() {
		return e;
	}

	@Override
	public ArrayList<Integer> getVertices() {
		return vertices;
	}

	@Override
	public ArrayList<Integer> getNeighbors(int v) {
		ArrayList<Integer> neighbours = new ArrayList<>();
		for (int i = 0; i < e; i++) {
			if (edges.get(i).s == v) {
				neighbours.add(edges.get(i).d);
			}
		}
		return neighbours;
	}

	@Override
	public void runDijkstra(int src, int[] distances) {
		PriorityQueue<Pair> pq=new PriorityQueue<>((a,b)->a.second-b.second);
		pq.add(new Pair(src,0));
        Arrays.fill(distances, INF);
        distances[src]=0;
        while(!pq.isEmpty()){
            Pair p=pq.remove();
            int node=(int) p.first;
            dijkstraNodes.add(node);
            for(int i=0;i<adj[node].size();i++){
                if(distances[adj[node].get(i).first]>distances[node]+adj[node].get(i).second) {
                   distances[adj[node].get(i).first]=distances[node]+adj[node].get(i).second;
                   pq.add(new Pair(adj[node].get(i).first,distances[adj[node].get(i).first]));
                }
            }
        }
        dijkstraDistance=distances;
	}

	@Override
	public ArrayList<Integer> getDijkstraProcessedOrder() {
		int first = dijkstraDistance[0];
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < v; j++) {
				if (first > dijkstraDistance[j]) {
					first = dijkstraDistance[j];
					dijkstraNodes.add(j);
					dijkstraDistance[j] = INF;
				}
			}
		}
		return dijkstraNodes;
	}

	@Override
	public boolean runBellmanFord(int src, int[] distances) {
		for (int i = 0; i < v; i++) {
			distances[i] = INF;
		}
		distances[src] = 0;
		for (int i = 0; i < v - 1; i++) {
			for (int j = 0; j < e; ++j) {
				int s = edges.get(j).s;
				int d = edges.get(j).d;
				int w = edges.get(j).w;
				if (distances[s] != INF && distances[s] + w < distances[d]) {
					distances[d] = distances[s] + w;
				}
			}
		}
		for (int j = 0; j < e; ++j) {
			int s = edges.get(j).s;
			int d = edges.get(j).d;
			int w = edges.get(j).w;
			if (distances[s] != INF && distances[s] + w < distances[d]) {
				return false;
			}
		}
		return true;
	}
}
