package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import model.dao.DepartmentDao;
import model.entity.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection con;
	
	public DepartmentDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Department department) {
		
		try {
			String sql = "INSERT INTO department(Name) VALUES (?)";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, department.getName());
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public void update(Department department) {
		
		try {
			String sql = "UPDATE department set Name = ? where Id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, department.getName());
			ps.setInt(2, department.getId());
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		try {
			String sql = "DELETE FROM department where Id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setInt(1, id);
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public Department findById(Integer id) {
		
		try {
			ResultSet rs = null;
			
			String sql = "select * from department where Id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			Department dep = null;
			while(rs.next()) {
				dep = new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			
			return dep;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {

		try {
			ResultSet rs = null;
			
			String sql = "select * from department";
			PreparedStatement ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			List<Department> depList = new ArrayList<>();
			
			while(rs.next()) {
				depList.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			
			return depList;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public ResultSet insertReturningId(Department department) {
		
		try {
			String sql = "INSERT INTO department(Name) VALUES (?)";
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, department.getName());
			
			ps.executeUpdate();
			
			return ps.getGeneratedKeys();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	
}
