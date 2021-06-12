package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	
	private List<Match> vertici;
	private Map<Integer, Match> idMap;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	
	private List<Match> collegamento;
	private double pesoCollegamento;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public String creaGrafo(int mins, Integer mese) {
		this.grafo = new SimpleWeightedGraph<Match, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.idMap = new HashMap<Integer, Match>();
		this.vertici = this.dao.getMatchesByMonth(mese);
		for(Match m : this.vertici)
			this.idMap.put(m.getMatchID(), m);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		this.adiacenze = this.dao.getAdiacenze(mese, mins, idMap);
		for(Adiacenza a : adiacenze) {
			if(!this.grafo.containsEdge(a.getM1(), a.getM2())) {
				Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public Graph<Match, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Adiacenza> getConnessioniMax() {
		Collections.sort(adiacenze);
		
		List<Adiacenza> adiacenzeShortList = new ArrayList<Adiacenza>();
		for(Adiacenza a : adiacenze) {
			if(adiacenzeShortList.isEmpty())
				adiacenzeShortList.add(a);
			else {
				if(a.getPeso() >= adiacenzeShortList.get(0).getPeso())
					adiacenzeShortList.add(a);
			}
		}
		
		return adiacenzeShortList;
	}

	public List<Match> getVertici() {
		return vertici;
	}
	
	public List<Match> getCollegamento(Match partenza, Match arrivo) {
		this.collegamento = new ArrayList<Match>();
		this.pesoCollegamento = 0.0;
		
		List<Match> parziale = new ArrayList<Match>();
		parziale.add(partenza);
		
		this.cerca(parziale, arrivo, 1, 0.0);
		
		return this.collegamento;
	}

	private void cerca(List<Match> parziale, Match arrivo, int L, double p) {
		//casi terminali
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			//sono arrivato 
			if(p > this.pesoCollegamento) {
				this.collegamento = new ArrayList<Match>(parziale);
				this.pesoCollegamento = p;
			}
			return;
		}
		
		if(L == this.grafo.vertexSet().size()) {
			//ho finito i match
			return;
		}
		
		for(DefaultWeightedEdge e : this.grafo.edgesOf(parziale.get(parziale.size()-1))) {
			Match precedente = parziale.get(parziale.size()-1);
			Match m = Graphs.getOppositeVertex(this.grafo, e, precedente);
			
			if( !(m.getTeamHomeID() == precedente.getTeamHomeID() && m.getTeamAwayID() == precedente.getTeamAwayID())
				&& !(m.getTeamHomeID() == precedente.getTeamAwayID() && m.getTeamAwayID() == precedente.getTeamHomeID()) ) {
			
				p += this.grafo.getEdgeWeight(e);
				parziale.add(m);
				
				this.cerca(parziale, arrivo, L+1, p);
				
				//backtracking
				parziale.remove(parziale.size()-1);
				p -= this.grafo.getEdgeWeight(e);
				this.cerca(parziale, arrivo, L+1, p);
			}
		}
		
	}

	public double getPesoCollegamento() {
		return pesoCollegamento;
	}
	
}
