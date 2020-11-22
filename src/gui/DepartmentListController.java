package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Department;
import model.service.DepartmentService;

public class DepartmentListController implements Initializable {
	
	@FXML
	private Button btnNovo;
	@FXML
	private TableView<Department>  tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	private DepartmentService service;
	private ObservableList<Department> obsListDep;
	@FXML
	private void onBtnNovoAction(ActionEvent event) {
		Stage stage = Utils.currentStage(event);
		Department dep = new Department();
		createDialogForm(dep, "/gui/DepartmentForm.fxml", stage);
	}
	
	public void setService(DepartmentService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		if (this.service == null) {
			throw new IllegalStateException("Service is null");
			
		}
		else {
			List<Department> listDep = service.findAll();
			obsListDep = FXCollections.observableArrayList(listDep);
			tableViewDepartment.setItems(obsListDep);
		}
	}
	
	public void createDialogForm(Department dep, String path, Stage parenteStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Pane pane = loader.load();
			
			DepartmentFormController depFormController = loader.getController();
			depFormController.setDepartment(dep);
			depFormController.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parenteStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", "Erro ao abrir o formulário de cadastro", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
