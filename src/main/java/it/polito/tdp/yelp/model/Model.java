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
	private Map<User, List<Review>> reviewMap;
	private List<Review> reviewList;
	private List<Review> reviewPerUserList;
	
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
	
//	this.reviewMap=new HashMap<>();
//	this.reviewList = this.dao.getAllReviews();
//	this.reviewPerUserList = this.dao.getAllReviews();
//	
//	for(User i : vertici) {
//	for(Review j : reviewList) {
//		if(i.getUserId().equals(j.getUserId()) && j.getDate().getYear()==anno) {
//		this.reviewPerUserList.add(j);
//		}
//	}
//	reviewMap.put(i, reviewPerUserList);
//	}
	
//	for (User v1 : vertici) {
//		for (User v2 : vertici) {
//			if(!v1.getUserId().equals(v2.getUserId())) {
//			for (String u1id : reviewMap.keySet()) {
//			for (String u2id : reviewMap.keySet()) {
//			if(v1.getUserId().equals(u1id) && v2.getUserId().equals(u2id)) {
//				List<Review> r1=reviewMap.get(u1id);
//				List<Review> r2=reviewMap.get(u2id);
//				for (Review a : r1) {
//					for (Review b : r2) {
//						if(a.getDate().getYear()==anno && b.getDate().getYear()==anno && a.getBusinessId().equals(b.getBusinessId())) {
//		this.grafo.addEdge(v1,v2);
//			}}
//	}
//	}}}}}}
	
//	for (User v1 : reviewMap.keySet()) {
//		for (User v2 : reviewMap.keySet()) {
//			if(!v1.getUserId().equals(v2.getUserId()) && !this.grafo.edgeSet().contains(this.grafo.getEdge(v1, v2))) {
//				for (Review a : this.reviewMap.get(v1)) {
//					for (Review b : this.reviewMap.get(v2)) {
//						if(a.getDate().getYear()==anno && b.getDate().getYear()==anno && a.getBusinessId().equals(b.getBusinessId())) {
//		      this.grafo.addEdge(v1,v2);
//			}}
//			}
//			
//		}}}
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
	
//	for (DefaultWeightedEdge i : this.grafo.edgeSet()) {
//		System.out.println(this.grafo.getEdgeWeight(i));
//	}
//	
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
//	
//		double p=0;
//		for (User v2 : this.grafo.vertexSet()) {
//			if(this.grafo.getEdgeWeight(this.grafo.getEdge(v1, v2))>=p && !v1.getUserId().equals(v2.getUserId())) {
//				p=this.grafo.getEdgeWeight(this.grafo.getEdge(v1, v2));
//			}
//		}
//	
//	
//		for (User v2 : this.grafo.vertexSet()) {
//			if(this.grafo.getEdgeWeight(this.grafo.getEdge(v1, v2))==p && !v1.getUserId().equals(v2.getUserId())) {
//				ris.add(new Pair<>(v2.getUserId(), p));
//			}
//		}
	
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
