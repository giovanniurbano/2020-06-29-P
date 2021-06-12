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
	
}
