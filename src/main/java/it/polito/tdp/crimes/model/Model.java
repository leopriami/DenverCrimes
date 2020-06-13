package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private double mediaPeso;
	private List<Adiacenza> archi;
	private List<String> camminoOttimo;
	private double pesoTotale = 0;
	private double pesoOttimo = 0;
	
	public Model() {
		dao = new EventsDao();	
	}
	
	public void creaGrafo(String categoria, int mese) {
		double totPeso = 0;
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, reati(categoria, mese));
		List<Adiacenza> archi = dao.getAdiacenze(categoria, mese);
		for(Adiacenza a : archi) {
			if(a.getPeso()>0) {
				DefaultWeightedEdge e = new DefaultWeightedEdge();
				grafo.addEdge(a.getTipo1(), a.getTipo2(),e);
				grafo.setEdgeWeight(e, a.getPeso());
				totPeso = totPeso+a.getPeso();
			}
		}
		mediaPeso = totPeso/grafo.edgeSet().size();
	}
	
	public List<String> categorie(){
		return dao.listCategories();
	}
	
	public List<Integer> mesi(){
		return dao.listMonths();
	}
	
	public List<String> reati(String categoria, int mese){
		return dao.listTypes(categoria, mese);
	}
	
	public String archiOK() {
		String s = "";
		archi = new ArrayList<Adiacenza>();
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>mediaPeso) {
				Adiacenza a = new Adiacenza(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e));
				archi.add(a);
				s = s+grafo.getEdgeSource(e)+"--"+grafo.getEdgeWeight(e)+"--"+grafo.getEdgeTarget(e)+"\n";
			}
		}
		return s;
	}

	public List<Adiacenza> getArchi() {
		return archi;
	}
	
	public List<String> calcolaPercorso(String v1, String v2){
		List<String> parziale = new ArrayList<String>();
		parziale.add(v1);
		camminoOttimo = new ArrayList<String>(parziale);
		cercaTutti(parziale, v1, v2);
		return camminoOttimo;
	}

	// cammino aciclico con piu' nodi possibile
	private void cerca(List<String> parziale, String v1, String v2) {
		if(parziale.contains(v2)) {
			if(parziale.size()>camminoOttimo.size()) camminoOttimo = new ArrayList<String>(parziale);
			return;
		}
		for(String s : Graphs.neighborSetOf(grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca(parziale,v1,v2);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	// cammino aciclico con peso minimo e tutti i vertici
		private void cercaTutti(List<String> parziale, String v1, String v2) {
			if(parziale.contains(v2) && parziale.size()==grafo.vertexSet().size()) {
				if(pesoTotale<pesoOttimo) {
					camminoOttimo = new ArrayList<String>(parziale);
					pesoOttimo = pesoTotale;
				}
				pesoTotale = 0;
				return;
			}
			for(String s : Graphs.neighborSetOf(grafo, parziale.get(parziale.size()-1))) {
				if(!parziale.contains(s)) {
					DefaultWeightedEdge e = grafo.getEdge(parziale.get(parziale.size()-1), s);
					double pesoUltimo = grafo.getEdgeWeight(e);
					pesoTotale = pesoTotale + pesoUltimo;
					parziale.add(s);
					cercaTutti(parziale,v1,v2);
					parziale.remove(parziale.size()-1);
					pesoTotale = pesoTotale - pesoUltimo;
				}
			}
		}
	
}


