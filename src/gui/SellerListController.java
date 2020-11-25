package gui;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entity.Department;
import model.entity.Seller;
import model.service.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	@FXML
	private Button btnNovo;
	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	private SellerService service;
	private ObservableList<Seller> obsListDep;

	@FXML
	private void onBtnNovoAction(ActionEvent event) {
		Stage stage = Utils.currentStage(event);
		Seller dep = new Seller();
		createDialogForm(dep, "/gui/SellerForm.fxml", stage);
	}

	public void setService(SellerService service) {
		this.service = service;
	}

	public void updateTableView() {
		if (this.service == null) {
			throw new IllegalStateException("Service is null");

		} else {
			//List<Seller> listDep = service.findAll();
			List<Seller> listDep = new ArrayList<Seller>();
			listDep = service.findAll();
			obsListDep = FXCollections.observableArrayList(listDep);
			tableViewSeller.setItems(obsListDep);
			initEditButtons();
			initRemoveButtons();
		}
	}

	public void createDialogForm(Seller dep, String path, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
//			Pane pane = loader.load();
//
//			SellerFormController depFormController = loader.getController();
//			depFormController.setSeller(dep);
//			depFormController.setSellerService(new SellerService());
//			depFormController.subscribeDataChangeListener(this);
//			depFormController.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Digite os dados do departamento");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			Alerts.showAlert("Erro", "Erro ao abrir o formulário de cadastro", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	protected void removeEntity(Seller obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação",
				"Deseja remove o departamente " + obj.getName() + "?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service está vazia");
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

		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

}
