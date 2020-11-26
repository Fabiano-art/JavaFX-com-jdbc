package model.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entity.Seller;

public class SellerService {
	
	SellerDao depDao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return depDao.findAll();
	}
	
	public void saveOrUpdate(Seller sel) {
		if(sel.getId() == null) {
			depDao.insert(sel);
		}
		else {
			depDao.update(sel);
		}
	}
	
	public void remove(Seller dep) throws SQLIntegrityConstraintViolationException{
		depDao.deleteById(dep.getId());
	}
}
