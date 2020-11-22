package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private void onBtnNovoAction() {
		System.out.println("Novo");
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
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		System.out.println("initialize DepartmentListController");
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		double maxHeight = stage.getMaxHeight();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		updateTableView();
	}

}
