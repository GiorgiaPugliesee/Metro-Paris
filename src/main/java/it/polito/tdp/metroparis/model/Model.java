package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultWeightedEdge> grafo;
	private MetroDAO dao = new MetroDAO();
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	
	public List<Fermata> getAllFermate() {
		return dao.readFermate();
	}
	
	public void creaGrafo() {
		//crea l'oggetto grafo
//		this.grafo = new SimpleGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		this.grafo = new SimpleWeightedGraph<Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungi i vertici
		this.fermate = dao.readFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		
		this.fermateIdMap = new HashMap<>();
		for(Fermata f : this.fermate) {
			this.fermateIdMap.put(f.getIdFermata(), f);
		}
		
		//aggiungi gli archi
		
		//Metodo 1: considero tutti i potenziali archi
//		for(Fermata partenza : this.grafo.vertexSet()) {
//			for(Fermata arrivo : this.grafo.vertexSet()) {
//				if(dao.isConnesse(partenza, arrivo)) {
//					this.grafo.addEdge(partenza, arrivo);
//				}
//			}
//		}
		
		//Metodo 2: data una fermata, trova la lista delle fermate adiacenti
//		for(Fermata partenza : this.grafo.vertexSet()) {
//			List<Fermata> collegate = dao.trovaCollegate(partenza, fermateIdMap);
//			
//			for(Fermata arrivo : collegate) {
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
		
		//Metodo 3: faccio una query per prendermi tutti gli edges
		List<Connessione> connessioni = dao.getAllCoppie(fermateIdMap);
		for(Connessione c : connessioni) {
			
			//metodo 1 per aggiungere gli archi (pesati)
//			DefaultWeightedEdge e = this.grafo.addEdge(c.getStazP(), c.getStazA());
//			//aggiunge il peso all'arco e, se l'arco non era già stato creato
//			if(e != null) {
//				this.grafo.setEdgeWeight(e, 0);
//			}
			
			//peso dell'arco
			double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(), LengthUnit.METER);
			
			//metodo 2
			Graphs.addEdge(this.grafo, c.getStazP(), c.getStazA(), distanza);
		}
		
		System.out.println("Grafo creato con " + this.grafo.vertexSet().size() + " vertici");
		System.out.println("Grafo creato con " + this.grafo.edgeSet().size() + " archi");

	}
	
	//determina il percorso minimo tra le 2 fermate (grafo non pesato)
//	public List<Fermata> percorso(Fermata partenza, Fermata arrivo) {
//		//visita il grafo partendo da "partenza"
//		BreadthFirstIterator<Fermata, DefaultEdge> visita = new BreadthFirstIterator<>(this.grafo, partenza);
//
//		List<Fermata> raggiungibili = new ArrayList<Fermata>();
//		
//		while(visita.hasNext()) {
//			Fermata f = visita.next();
//			raggiungibili.add(f);
//		}
//		
//		System.out.println(raggiungibili);
//		
//		//trova il percorso sull'albero di visita
//		List<Fermata> percorso = new ArrayList<Fermata>();
//		Fermata corrente = arrivo;   //inizializzo la fermata corrente a quella di arrivo
//		percorso.add(arrivo);   //e aggiungo la fermata di arrivo al mio percorso
//		DefaultEdge e = visita.getSpanningTreeEdge(corrente);  //dato il vertice trovo il suo arco
//
//		while(e!=null) {
//			Fermata precedente = Graphs.getOppositeVertex(grafo, e, corrente);   //dato l'arco trovo il vertice precedente
//			percorso.add(0, precedente);   //e lo aggiungo alla lista
//			corrente = precedente;   //e ripeto
//			
//			e = visita.getSpanningTreeEdge(corrente);
//
//		}
//		
//		return percorso;
//	}
	
	//determina il percorso minimo tra le 2 fermate (grafo pesato): calcolo cammini minimi
	public List<Fermata> percorso(Fermata partenza, Fermata arrivo) {
		
		//oggetto che calcola i cammini minimi
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> sp = new DijkstraShortestPath<>(this.grafo);
		
		GraphPath<Fermata, DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
		
		return gp.getVertexList();
	}
	
	public boolean isGrafoLoaded() {
		return this.grafo.vertexSet().size() > 0;
	}

}
