package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entity.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label lblError;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	
	private List<DataChangeListener> dataChangeList = new ArrayList<DataChangeListener>();
	private Department dep;
	private DepartmentService depService;
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(dep == null) {
			throw new IllegalStateException("Department is null");
		}
		if(depService == null) {
			throw new IllegalStateException("Service is null");
		}
		try {
			
			dep = getFormData();
			depService.saveOrUpdate(dep);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			lblError.setText(e.getMessage());
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
	
	private Department getFormData() throws ValidationException{
		Department dep = new Department();
		
		dep.setId(Utils.tryParseToInt(txtId.getText()));
		dep.setName(txtName.getText());
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			throw new ValidationException("Campo nome não pode estar vazio");
		}
		
		return dep;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService;
	}
	
	public void setDepartment(Department department) {
		this.dep = department;
	}

	public void updateFormData() {
		if(dep == null) {
			throw new IllegalStateException("the object is null");
		}
		txtId.setText(String.valueOf(dep.getId()));
		txtName.setText(dep.getName());
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLenght(txtName, 40);
	}
	
	
}
