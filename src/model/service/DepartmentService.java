package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entity.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		
		list.add(new Department(1, "Livros"));
		list.add(new Department(2, "Computadores"));
		list.add(new Department(3, "eletrodemesticos"));
		
		return list;
	}
	
}
