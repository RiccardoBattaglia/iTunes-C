package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;


public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	private Map<Album, Double> albumMap;
	private List<Album> albumList;
	
	public Model() {
		
		this.dao = new ItunesDAO();
		
		this.albumList = this.dao.getAllAlbums();
		
		this.albumMap=new HashMap<>();
		for(Album i : albumList) {
			this.albumMap.put(i, this.dao.getPrezzo(i.getAlbumId()));
		}
		
	}
	

	public void creaGrafo(double n) {
		// TODO Auto-generated method stub

	this.grafo = new SimpleWeightedGraph<Album, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	// Aggiunta VERTICI 
	List<Album> vertici=new LinkedList<>();
	
	for(Album i : this.albumList) {
		if(this.albumMap.get(i)>=n) {
			vertici.add(i);
		}
	}
	
	Graphs.addAllVertices(this.grafo, vertici);
	
	
//	this.retailersMap=new HashMap<>();
//	for(Retailers i : retailersList) {
//		this.retailersMap.put((i), this.dao.getProdottiPerRetailerInAnno(i, anno));
//	}

	
	// Aggiunta ARCHI
	
	for (Album v1 : vertici) {
		for (Album v2 : vertici) {
			if((this.albumMap.get(v1)-this.albumMap.get(v2))>0 && v1.getAlbumId()!=v2.getAlbumId()){ 

		      this.grafo.addEdge(v1,v2);
		      this.grafo.setEdgeWeight(this.grafo.getEdge(v1, v2), (this.albumMap.get(v1)-this.albumMap.get(v2)));
			}
			}
			}

	}

public int nVertici() {
	return this.grafo.vertexSet().size();
}

public int nArchi() {
	return this.grafo.edgeSet().size();
}

public Set<Album> getVertici(){
	
	Set<Album> vertici=this.grafo.vertexSet();
	
	return vertici;
}

public Set<DefaultWeightedEdge> getArchi(){
	
	Set<DefaultWeightedEdge> archi=this.grafo.edgeSet();
	
	return archi;
}

//public List<Set<User>> getComponente() {
//	ConnectivityInspector<User, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo) ;
//	return ci.connectedSets() ;
//}

//public Set<Retailers> getComponente(Retailers v) {
//	ConnectivityInspector<Retailers, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo) ;
//	return ci.connectedSetOf(v) ;
//}

public List<String> getVerticiName(){
	
	List<String> nomi=new LinkedList<>();
	
	for(Album i : this.grafo.vertexSet()) {
		nomi.add(i.getTitle());
	}
	
	Collections.sort(nomi);
	
	return nomi;
}

public Double calcolaBilancio(Album album) {
	
	double s=0.0;
	
	for(DefaultWeightedEdge i : this.grafo.edgesOf(album)) {
		s=s+this.grafo.getEdgeWeight(i);
	}
	
	s=s/(this.grafo.edgesOf(album).size());
	
	return s;
	
}


public List<AlbumBilancio> calcolaAdiacenze(String album) {
	
	Album v = new Album(null, null);
	List<AlbumBilancio> dettagli = new LinkedList<>();
	
	double bilancio=0.0;
	
	for(Album i : albumList) {
		if(i.getTitle().equals(album)) {
			v=i;
		}
	}
		
	for(DefaultWeightedEdge i : this.grafo.edgesOf(v)) {
		if(!this.grafo.getEdgeSource(i).equals(v)) {
		dettagli.add(new AlbumBilancio(this.grafo.getEdgeSource(i), this.calcolaBilancio(this.grafo.getEdgeSource(i))));}
		if(!this.grafo.getEdgeTarget(i).equals(v)) {
			dettagli.add(new AlbumBilancio(this.grafo.getEdgeTarget(i), this.calcolaBilancio(this.grafo.getEdgeTarget(i))));}
	}
	
	Collections.sort(dettagli);
	
//	System.out.println(dettagli.toString());
	
	return dettagli;
	
	
}
	
}
