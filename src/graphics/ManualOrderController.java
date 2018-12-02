package graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import objects.Mansion;

import java.util.ArrayList;

/**
 * Controller for manualOrder.fxml
 */
public class ManualOrderController {

    @FXML
    private TextArea infoText;

    @FXML
    private ComboBox<Mansion> mansionCombo;

    @FXML
    private ComboBox<Integer> palletCombo;

    /**
     * Empty constructor for creating instance
     */
    public ManualOrderController(){
        //prazdny konstruktor
    }

    @FXML
    private void initialize(){
        setUpComboBoxes();
    }

    /**
     * Action button of Make Order
     */
    @FXML
    public void makeOrder() {
        if(mansionCombo.getValue() != null && palletCombo.getValue() != null){
            GUI.sim.manualOrder(palletCombo.getValue(), mansionCombo.getValue().iD);
            Stage s = (Stage) infoText.getScene().getWindow();
            s.close();
        } else {
            infoText.setText("Can't make order. Mansion or number of pallets not chosen.");
        }
    }

    private void setUpComboBoxes(){
        ArrayList<Mansion> man = new ArrayList<Mansion>();
        for(int i = 1; i < GUI.mansions.size(); i++){
            Mansion m = (Mansion)GUI.mansions.get(i);
            if(m.getCanOrderNumber() > 0){
                man.add(m);
            }
        }
        if(man.size() <= 0){
            System.out.println("There is no mansions that can order now.");
            return;
        }
        ObservableList<Mansion> mansions = FXCollections.observableArrayList(man);

        mansionCombo.setItems(mansions);
        mansionCombo.setConverter(new StringConverter<Mansion>() {
            @Override
            public String toString(Mansion object) {
                return object.name;
            }

            @Override
            public Mansion fromString(String string) {
                return null;
            }
        });
        mansionCombo.setOnAction(event -> {
            Mansion m = mansionCombo.getValue();
            palletCombo.getItems().clear();
            for(int i = 0; i < m.getCanOrderNumber(); i++){
                palletCombo.getItems().add(i+1);
            }
            palletCombo.setValue(null);
            infoText.setText(m.mansionInfo());
        });
    }
}
