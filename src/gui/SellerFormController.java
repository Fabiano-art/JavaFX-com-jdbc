package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entity.Department;
import model.entity.Seller;
import model.exceptions.ValidationException;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerFormController implements Initializable {

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dtpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorBirthDate;
	@FXML
	private Label lblErrorBaseSalary;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	@FXML
	private ComboBox<Department> cmbDepartment;

	private ObservableList<Department> obsDep;
	private List<DataChangeListener> dataChangeList = new ArrayList<DataChangeListener>();
	private Seller dep;
	private SellerService selService;
	private DepartmentService depService;

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (dep == null) {
			throw new IllegalStateException("Seller is null");
		}
		if (depService == null) {
			throw new IllegalStateException("Service is null");
		}
		try {

			dep = getFormData();
			selService.saveOrUpdate(dep);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			lblErrorName.setText(e.getMessage());
		}
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeList.add(listener);
	}

	public void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeList) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() throws ValidationException {
		Seller dep = new Seller();

		dep.setId(Utils.tryParseToInt(txtId.getText()));
		dep.setName(txtName.getText());

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			throw new ValidationException("Campo nome não pode estar vazio");
		}

		return dep;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setSellerService(SellerService selService) {
		this.selService = selService;
	}

	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService;
	}

	public void setSeller(Seller department) {
		this.dep = department;
	}

	public void updateFormData() {
		if (dep == null) {
			throw new IllegalStateException("the object is null");
		}
		txtId.setText(String.valueOf(dep.getId()));
		txtName.setText(dep.getName());
		txtEmail.setText(dep.getEmail());
		txtBaseSalary.setText(String.format("%.2f", dep.getBaseSalary()));
		if (dep.getBirthDate() != null) {
			dtpBirthDate.setValue(LocalDate.ofInstant(dep.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(dep.getDepartment() == null) {
			cmbDepartment.getSelectionModel().selectFirst();
		}
		else {
			cmbDepartment.setValue(dep.getDepartment());
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLenght(txtName, 80);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Utils.formatDatePicker(dtpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cmbDepartment.setCellFactory(factory);
		cmbDepartment.setButtonCell(factory.call(null));
	}

	public void initializeComboBox() {
		if (depService == null) {
			throw new IllegalStateException("depService is null");
		}
		List<Department> list = depService.findAll();
		obsDep = FXCollections.observableArrayList(list);
		cmbDepartment.setItems(obsDep);
	}

}
