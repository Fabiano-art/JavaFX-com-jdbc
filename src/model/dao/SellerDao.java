package model.dao;

import java.sql.ResultSet;
import java.util.List;

import model.entity.Department;
import model.entity.Seller;

public interface SellerDao {
	
	public void insert (Seller Seller);
	public void update (Seller Seller);
	public void deleteById (Integer id);
	public Seller findById(Integer id);
	public List<Seller> findAll();
	public List<Seller> findByDepartment(Department dep);
	public ResultSet insertReturningId(Seller seller);
	
}
