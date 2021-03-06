package model.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entity.Department;

public class DepartmentService {
	
	DepartmentDao depDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return depDao.findAll();
	}
	
	public void saveOrUpdate(Department dep) {
		if(dep.getId() == null) {
			depDao.insert(dep);
		}
		else {
			depDao.update(dep);
		}
	}
	
	public void remove(Department dep) throws SQLIntegrityConstraintViolationException{
		depDao.deleteById(dep.getId());
	}
}
