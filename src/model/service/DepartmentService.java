package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entity.Department;

public class DepartmentService {
	
	DepartmentDao depDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return depDao.findAll();
	}
	
}
