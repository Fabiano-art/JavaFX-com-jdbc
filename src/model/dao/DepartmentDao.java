package model.dao;

import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import model.entity.Department;

public interface DepartmentDao {
	
	public void insert (Department department);
	public void update (Department department);
	public void deleteById (Integer id) throws SQLIntegrityConstraintViolationException;
	public Department findById(Integer id);
	public List<Department> findAll();
	public ResultSet insertReturningId(Department department);
	
}
