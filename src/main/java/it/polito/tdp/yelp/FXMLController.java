/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<Pair<String, String>> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	 String tn = txtN.getText() ;
     	
     	if(tn.equals("")) {
     		txtResult.setText("Inserire un numero di recensioni.\n");
     		return ;
     	}
     	
     	int n = 0 ;

     	try {
 	    	n = Integer.parseInt(tn) ;
     	} catch(NumberFormatException e) {
     		txtResult.setText("Inserire un numero.\n");
     		return ;
     	}
     	
        Integer anno = cmbAnno.getValue() ;
     	
     	if(anno==null) {
     		txtResult.setText("Inserire un anno.\n");
     		return ;
     	}
     	
//    	creazione grafo
    	this.model.creaGrafo(n, anno);
    	
    	
//    	stampa grafo
    	this.txtResult.setText("Grafo creato.\n");
    	this.txtResult.appendText("Ci sono " + this.model.nVertici() + " vertici\n");
    	this.txtResult.appendText("Ci sono " + this.model.nArchi() + " archi\n\n");
    	
    	Set<User> vertici = this.model.getVertici();
    	
    	for(User i : vertici) {
    		cmbUtente.getItems().add(new Pair<String, String>(i.getName(), i.getUserId()));
    	}
    	
    	btnUtenteSimile.setDisable(false);
     }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	
    	Pair<String, String> uid=cmbUtente.getValue();
    	User v = new User(null, 0, 0, 0, null, 0, 0);
    	
    	for(User u : this.model.getVertici()) {
    		if(u.getUserId().equals(uid.getSecond())) {
    			v = u;
    			break;
    		}
    	}
    	
    	List<Pair<String, Double>> ris = this.model.trovaSimili(v);
    	
    	for(Pair p : ris) {
    		this.txtResult.appendText(""+p.getFirst()+" --> Grado:"+ p.getSecond()+"\n");
    	}
    	
    }
    
    @FXML
    void doSimula(ActionEvent event) {

    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	List<Integer> anni = new LinkedList<>();
    	for(int i=2005; i<2014; i++) {
    		anni.add(i);
    	}
    	cmbAnno.getItems().addAll(anni);
    	btnUtenteSimile.setDisable(true);
    	
    }
}
