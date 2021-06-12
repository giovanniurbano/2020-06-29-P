/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<String> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}
    	
    	this.txtResult.setText("COPPIE CON CONNESSIONE MASSIMA:\n");
    	for(Adiacenza a : this.model.getConnessioniMax()) {
    		this.txtResult.appendText(a.getM1() + " - " + a.getM2() + " (" + a.getPeso() + ")\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.cmbM1.getItems().clear();
    	this.cmbM2.getItems().clear();
    	
    	String min = this.txtMinuti.getText();
    	try {
    		int mins = Integer.parseInt(min);
    		if(mins < 0) {
    			this.txtResult.appendText("Inserire un numero maggiore di zero!");
        		return;
    		}
    		String mese = this.cmbMese.getValue();
    		if(mese == null) {
    			this.txtResult.appendText("Scegliere un mese!");
        		return;
    		}
    		Integer m = -1;
    		switch(mese) {
    		case "Gennaio": m = 1; break;
    		case "Febbraio": m = 2; break;
    		case "Marzo": m = 3; break;
    		case "Aprile": m = 4; break;
    		case "Maggio": m = 5; break;
    		case "Giugno": m = 6; break;
    		case "Luglio": m = 7; break;
    		case "Agosto": m = 8; break;
    		case "Settembre": m = 9; break;
    		case "Ottobre": m = 10; break;
    		case "Novembre": m = 11; break;
    		case "Dicembre": m = 12; break;
    		}
    		
    		String msg = this.model.creaGrafo(mins, m);
    		this.txtResult.appendText(msg);
    		
    		this.cmbM1.getItems().addAll(this.model.getVertici());
    		this.cmbM2.getItems().addAll(this.model.getVertici());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire un numero!");
    		return;
    	}
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}
    	
    	Match partenza = this.cmbM1.getValue();
    	Match arrivo = this.cmbM2.getValue();
    	
    	if(partenza == null || arrivo == null) {
    		this.txtResult.appendText("Scegliere partenza e/o arrivo!");
    		return;
    	}
    	
    	this.txtResult.appendText("COLLEGAMENTO:\n");
    	if(this.model.getCollegamento(partenza, arrivo) == null)
    		this.txtResult.appendText("NESSUN COLLEGAMENTO!");
    	else {
	    	for(Match m : this.model.getCollegamento(partenza, arrivo)) {
	    		this.txtResult.appendText(m + "\n");
	    	}
	    	this.txtResult.appendText("\nPESO TOTALE DEL COLLEGAMENTO: " + this.model.getPesoCollegamento());
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    	this.cmbMese.getItems().addAll(mesi);
    }
    
    
}
