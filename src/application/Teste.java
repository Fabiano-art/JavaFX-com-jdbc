package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Teste {

	public static void main(String[] args) {

		SellerDao dao = DaoFactory.createSellerDao();

		List<?> daoSel = dao.findAll();

		// Seller daoSel = new Seller();

		System.out.println(daoSel);

	}

}
