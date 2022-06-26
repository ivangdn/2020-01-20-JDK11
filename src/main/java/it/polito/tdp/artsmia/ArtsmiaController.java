package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenza> adiacenze = model.getAdiacenze();
    	if(adiacenze==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	for(Adiacenza a : adiacenze) {
    		txtResult.appendText(String.format("(%d,%d) = %d\n", a.getA1(), a.getA2(), a.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	int artista;
    	try {
    		artista = Integer.parseInt(txtArtista.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("L'ID dell'artista deve essere un valore numerico");
    		return;
    	}
    	
    	List<Integer> artisti = model.getVertici();
    	if(artisti==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	if(!artisti.contains(artista)) {
    		txtResult.setText("L'artista inserito non è presente nel grafo");
    		return;
    	}
    	
    	List<Integer> percorso = model.calcolaPercorso(artista);
    	txtResult.appendText("Numero di esposizioni ottimo: "+model.getPesoMigliore()+"\n");
    	for(Integer i : percorso) {
    		txtResult.appendText(i+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String ruolo = boxRuolo.getValue();
    	if(ruolo==null) {
    		txtResult.setText("Selezionare un ruolo");
    		return;
    	}
    	
    	String msg = model.creaGrafo(ruolo);
    	txtResult.setText(msg);
    }

    public void setModel(Model model) {
    	this.model = model;
    	boxRuolo.getItems().addAll(model.getRoles());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
