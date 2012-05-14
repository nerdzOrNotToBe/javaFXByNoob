/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.gui.projet;

import timereportfx.service.SimpleProjetService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import timereportfx.module.colorpicker.ColorPicker;
import timereportfx.models.entities.ProjetEntity;
import timereportfx.service.ProjetService;

/**
 *
 * @author Dimitri Lebel
 */
public class ProjetPanePresenter implements Initializable {

    @FXML
    private TableView projetTable;
    @FXML
    private TextField projetName;
    @FXML
    private Button btnAdd;
    @FXML
    private AnchorPane projetPane;
    private ProjetService projetService;
    private ObservableList<Projet> projets;
    private ObservableObjectValue<Projet> projet;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        configureTable();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLayoutX(100.0);
        colorPicker.setLayoutY(56);
        colorPicker.setPrefWidth(150);
        projetPane.getChildren().add(colorPicker);
        //  projetName.textProperty().bind(projet.getNom());
        findProjet();
    }

    public ProjetService getProjetService() {
        return projetService;
    }

    public void setProjetService(ProjetService projetService) {
        this.projetService = projetService;
    }

    private void configureTable() {
        if (projetTable != null) {
            projetTable.getColumns().clear();
            TableColumn code = new TableColumn();
            code.setText("Code");
            code.setCellValueFactory(new PropertyValueFactory("idprojet"));
            code.setResizable(false);
            TableColumn name = new TableColumn("Nom");
            //name.setText("Nom");
            name.setCellValueFactory(new PropertyValueFactory("nom"));
            TableColumn color =
                    new TableColumn("Couleur");
            color.setCellValueFactory(new PropertyValueFactory("couleur"));

            code.setPrefWidth(50);
            name.setPrefWidth(130);
            color.setPrefWidth(70);


            code.setMinWidth(50);
            name.setMinWidth(130);
            color.setMinWidth(70);

            code.setMaxWidth(500);
            name.setMaxWidth(1300);
            color.setMaxWidth(700);

            final ObservableList columns = projetTable.getColumns();
            columns.add(code);
            columns.add(name);
            columns.add(color);
            projetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            //   projetTable.setItems(projets);
            assert projetTable.getItems() == projets;
            projetTable.addEventHandler(new EventType<ActionEvent>(), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                }
            });

//            final ObservableList<ObservableIssue> tableSelection = projetTable.getSelectionModel().getSelectedItems();

//            tableSelection.addListener(tableSelectionChanged);
        }
    }

    public void findProjet() {
        
       // projets = FXCollections.observableList(projetService.findProjetEntities());
        projetTable.setItems(projets);

    }

    @FXML
    public void clickAdd(MouseEvent event) {
        projets.get(0).setNom("toto");
    }
    
    public Parent getView(){
        return (Parent)projetPane;
    }
}
