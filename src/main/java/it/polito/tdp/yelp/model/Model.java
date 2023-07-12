package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	
	private YelpDao dao;
	private Graph<User, DefaultWeightedEdge> grafo;
	private Map<String, User> userMap;
	private List<User> userList;
	
	public Model() {
		this.dao = new YelpDao();
		
		this.userMap=new HashMap<>();
		this.userList = this.dao.getAllUsers();
		for(User i : userList) {
			this.userMap.put(i.getUserId(), i);
		}
		
	}
	

	public void creaGrafo(Integer n, Integer anno) {
		// TODO Auto-generated method stub

	this.grafo = new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	// Aggiunta VERTICI 
	List<User> vertici=new LinkedList<>();
	
	for(User i : this.userList) {
		if(this.dao.contaNReviewPerUser(i.getUserId())>=n) {
			vertici.add(i);
		}
	}
	
	Graphs.addAllVertices(this.grafo, vertici);

	
	// Aggiunta ARCHI
	
	double peso;
	for (User v1 : vertici) {
		for (User v2 : vertici) {
			if(!(v1.getUserId().compareTo(v2.getUserId())>0) && !v1.getUserId().equals(v2.getUserId())){ 
				peso=this.dao.contaNReviewInComuneConAnno(v1.getUserId(), v2.getUserId(), anno);
					if(peso>0) {
		      this.grafo.addEdge(v1,v2);
		      this.grafo.setEdgeWeight(this.grafo.getEdge(v1, v2), peso);
			}}
			}
			}

	}

public int nVertici() {
	return this.grafo.vertexSet().size();
}

public int nArchi() {
	return this.grafo.edgeSet().size();
}

public Set<User> getVertici(){
	
	Set<User> vertici=this.grafo.vertexSet();
	
	return vertici;
}

public List<Set<User>> getComponente() {
	ConnectivityInspector<User, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo) ;
	return ci.connectedSets() ;
}

public List<Pair<String, Double>> trovaSimili(User v1){

	List<Pair<String, Double>> ris = new LinkedList<>();

	double p=0;
	
	for(DefaultWeightedEdge i :this.grafo.outgoingEdgesOf(v1)) {
		if(this.grafo.getEdgeWeight(i)>=p) {
			p=this.grafo.getEdgeWeight(i);
		}
	}
		
		for(DefaultWeightedEdge i :this.grafo.outgoingEdgesOf(v1)) {
			if(this.grafo.getEdgeWeight(i)==p) {
			ris.add(new Pair<>(this.grafo.getEdgeSource(i).getUserId(), this.grafo.getEdgeWeight(i)));
//			System.out.println(this.grafo.getEdgeSource(i).getUserId()+"--"+ this.grafo.getEdgeWeight(i));
		}
		}
		return ris;
}
	
	
}
