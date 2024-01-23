package it.polito.tdp.metroparis;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProvaGrafo {
	
	public static void main(String[] args) {
		
		//grafo con String come vertici, DefaultEdge come arco (arco semplice, senza peso)
		//in questo caso ho un grafo semplice, non orientato, non pesato: SimpleGraph
		Graph<String, DefaultEdge> grafo = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		
		//mettiamo prima i vertici
		grafo.addVertex("v");
		grafo.addVertex("r");
		grafo.addVertex("s");
		grafo.addVertex("w");
		grafo.addVertex("t");
		grafo.addVertex("u");
		grafo.addVertex("x");
		grafo.addVertex("y");
		grafo.addVertex("z");
		
		//ora mettiamo gli archi
		grafo.addEdge("v", "r");
		grafo.addEdge("r", "s");
		grafo.addEdge("s", "w");
		grafo.addEdge("w", "t");
		grafo.addEdge("w", "x");
		grafo.addEdge("t", "u");
		grafo.addEdge("t", "x");
		grafo.addEdge("x", "y");
		grafo.addEdge("x", "z");
		
		System.out.println(grafo.toString());
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
		
		for(String v : grafo.vertexSet()) {
			System.out.println("Vertice " + v + " ha grado " + grafo.degreeOf(v));
			
			for(DefaultEdge arco : grafo.edgesOf(v)) {
				
//				System.out.println(arco);
				if(v.equals(grafo.getEdgeSource(arco))) {
					String verticeArrivo = grafo.getEdgeTarget(arco);     //metodo che resistuisce il vertice di arrivo dell'arco
					System.out.println("\tè connesso a " + verticeArrivo);
				} else {
					String verticePartenza = grafo.getEdgeSource(arco);   //metodo che resistuisce il vertice di partenza dell'arco
					System.out.println("\tè connesso a " + verticePartenza);	
				}
				
				String arrivo = Graphs.getOppositeVertex(grafo, arco, v);  //fa la stessa cosa delle righe di codice sopra scritte
				System.out.println("\tè connesso a " + arrivo);	

			}
			
			List<String> vicini = Graphs.neighborListOf(grafo, v);    //fa sempre la stessa cosa, sottoforma di lista
			System.out.println("\tI vicini sono: " + vicini);
		}
		
	}

}
