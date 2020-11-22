package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entity.Department;

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
	
	private Department dep;
	
	@FXML
	public void onBtnSaveAction() {
		
	}
	
	@FXML
	public void onBtnCancelAction() {
		
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
