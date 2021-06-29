package it.polito.tdp.crimes.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	
	EventsDao dao;
	SimpleWeightedGraph <District, DefaultWeightedEdge> grafo;
	Map<Integer, District> idMapDistretti;
	
	
	
	public Model() {
		
		dao=new EventsDao();
		idMapDistretti=new HashMap<Integer, District>();
		dao.loadDistretti(idMapDistretti);
	}
	
	
	public String creaGrafo(int anno) {
		String  msg=null;
		
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungi vertici
		
		Graphs.addAllVertices(this.grafo, idMapDistretti.values());
		
		
		
		//aggiungi archi
		
		
		for(District d1: idMapDistretti.values()) {
			for(District d2: idMapDistretti.values()) {
				
				if(!d1.equals(d2)) {
					
					
					if(!this.grafo.containsEdge(d1, d2)) {
					
					
					double avgLat1= dao.getAvgLat(d1, anno);
					double avgLon1=dao.getAvgLon(d1, anno);
					
					LatLng centro1=new LatLng(avgLat1, avgLon1);
					
					double avgLat2= dao.getAvgLat(d2, anno);
					double avgLon2=dao.getAvgLon(d2, anno);
					
					
					LatLng centro2=new LatLng(avgLat2, avgLon2);
					
					
					double distanza=LatLngTool.distance(centro1, centro2, LengthUnit.KILOMETER );
					
					Adiacenza a = new Adiacenza(d1,d2,distanza);
					
					Graphs.addEdgeWithVertices(this.grafo, d1, d2, distanza);
					
					
					
					
					}
					
				}
				
				
			}
			
			
			
			
		}
		
		msg="Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi";
		
		return msg;
		
		//System.out.println("Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi");
		
	}
	
	
	
	public List<Integer> getAnni(){
		
		return dao.getAnni();
		
		
	}
	
	
	public List<Adiacenza> getVicini(District d){
		
		List<Adiacenza> vicini=new LinkedList<>();	
		
		List<District> result=new LinkedList<>();
		
		//tutti i distretti vicini
		result=Graphs.neighborListOf(this.grafo, d);
		//ordiniamoli per peso crescente 
		//mi devo costruire l'adiacenza
		for(District vicino: result) {
			
			DefaultWeightedEdge e= this.grafo.getEdge(d, vicino);
			Adiacenza a=new Adiacenza(d, vicino, this.grafo.getEdgeWeight(e));
			vicini.add(a);
			
			
		}
		
		Collections.sort(vicini);
		
		
		return vicini;
		
		
	}
	
	public Collection<District> getVertici(){
		
		return  this.idMapDistretti.values();
		
		
		
	}
	
	
	
}
