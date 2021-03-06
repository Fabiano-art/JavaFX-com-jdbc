package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Department;
import model.service.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	@FXML
	private Button btnNovo;
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

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

		} else {
			List<Department> listDep = service.findAll();
			obsListDep = FXCollections.observableArrayList(listDep);
			tableViewDepartment.setItems(obsListDep);
			initEditButtons();
			initRemoveButtons();
		}
	}

	public void createDialogForm(Department dep, String path, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Pane pane = loader.load();

			DepartmentFormController depFormController = loader.getController();
			depFormController.setDepartment(dep);
			depFormController.setDepartmentService(new DepartmentService());
			depFormController.subscribeDataChangeListener(this);
			depFormController.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao abrir o formul�rio de cadastro", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	protected void removeEntity(Department obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o",
				"Deseja remove o departamente " + obj.getName() + "?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service est� vazia");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (SQLIntegrityConstraintViolationException e) {
				Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
			}
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
