package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private List<String> roles;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	private List<Integer> vertici;
	
	private List<Integer> best;
	private int pesoMigliore;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}
	
	public List<String> getRoles() {
		if(this.roles==null)
			this.roles = dao.getRoles();
		
		return this.roles;
	}
	
	public List<Adiacenza> getAdiacenze() {
		Collections.sort(adiacenze);
		return this.adiacenze;
	}
	
	public List<Integer> getVertici() {
		return this.vertici;
	}
	
	public String creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getVertici(ruolo);
		Graphs.addAllVertices(grafo, this.vertici);
		
		this.adiacenze = dao.getAdiacenze(ruolo);
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
		return "GRAFO CREATO\n"
				+ "# VERTICI: "+this.grafo.vertexSet().size()+"\n"
				+ "# ARCHI: "+this.grafo.edgeSet().size();
	}
	
	public List<Integer> calcolaPercorso(Integer artista) {
		this.best = new ArrayList<>();
		List<Integer> parziale = new ArrayList<>();
		this.pesoMigliore = 0;
		parziale.add(artista);
		cerca(artista, parziale);
		return this.best;
	}

	private void cerca(Integer artista, List<Integer> parziale) {
		if(parziale.size()>best.size()) {
			this.best = new ArrayList<>(parziale);
			if(parziale.size()>1) {
				this.pesoMigliore = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(artista, parziale.get(1)));
			}
		}
		
		if(parziale.size()==1) {
			for(Integer vicino : Graphs.neighborListOf(this.grafo, artista)) {
				if(!parziale.contains(vicino)) {
					parziale.add(vicino);
					cerca(artista, parziale);
					parziale.remove(parziale.size()-1);
				}
			}
		} else {
			for(Integer vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
				if(!parziale.contains(vicino) && isAggiuntaValida(parziale, vicino)) {
					parziale.add(vicino);
					cerca(artista, parziale);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}
	
	private boolean isAggiuntaValida(List<Integer> parziale, Integer daAggiungere) {
		if(this.grafo.getEdgeWeight(this.grafo.getEdge(daAggiungere, parziale.get(parziale.size()-1))) ==
				this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), parziale.get(parziale.size()-2))))
				return true;
		else
			return false;
	}
	
	public int getPesoMigliore() {
		return this.pesoMigliore;
	}
	
}