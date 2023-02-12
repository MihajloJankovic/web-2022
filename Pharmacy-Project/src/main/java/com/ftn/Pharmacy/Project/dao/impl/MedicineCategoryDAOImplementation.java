package com.ftn.Pharmacy.Project.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.Pharmacy.Project.dao.MedicineCategoryDAO;
import com.ftn.Pharmacy.Project.model.MedicineCategory;


@Repository
public class MedicineCategoryDAOImplementation implements MedicineCategoryDAO{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private class MedicineCategoryRowCallBackHandler implements RowCallbackHandler {
		
		private Map<Long, MedicineCategory> medicineCategories = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int index = 1;
			Long medicineID = rs.getLong(index++);
			String medicineName = rs.getString(index++);
			String medicinePurpose = rs.getString(index++);
			String medicineDescription = rs.getString(index++);
			
			MedicineCategory medicineCategory = medicineCategories.get(medicineID);
			if(medicineCategory == null) {
				medicineCategory = new MedicineCategory(medicineID, medicineName, medicinePurpose, medicineDescription);
				medicineCategories.put(medicineID, medicineCategory);
			}
		}
		
		public List<MedicineCategory> getMedicineCategories() {
			return new ArrayList<>(medicineCategories.values());
		}
		
	}

	@Override
	public MedicineCategory findOne(Long id) {
		String sql = "select * from medicineCategories where medicineID = ?";
		
		MedicineCategoryRowCallBackHandler rowCallbackHandler = new MedicineCategoryRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);
		return rowCallbackHandler.getMedicineCategories().get(0);
	}

	@Override
	public List<MedicineCategory> findAll() {
		String sql = "select * from medicineCategories";
		
		MedicineCategoryRowCallBackHandler rowCallbackHandler = new MedicineCategoryRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);
		return rowCallbackHandler.getMedicineCategories();
		
	}
	
	@Transactional
	@Override
	public int save(MedicineCategory medicineCategory) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into medicineCategories (medicineName, medicinePurpose, medicineDescription) values (?, ?, ?)";
				
				PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, medicineCategory.getMedicineName());
				preparedStatement.setString(index++, medicineCategory.getMedicinePurpose());
				preparedStatement.setString(index++, medicineCategory.getMedicineDescription());
				return preparedStatement;
			}
			
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return success?1:0;
	}

	@Override
	public int update(MedicineCategory medicineCategory) {
		String sql = "update medicineCategories set medicineName = ?, medicinePurpose = ?, medicineDescription = ? where medicineID = ?";
		boolean success = true;
		success = jdbcTemplate.update(sql, medicineCategory.getMedicineName(), medicineCategory.getMedicinePurpose(), medicineCategory.getMedicineDescription(), medicineCategory.getMedicineID()) == 1;
		
		return success?1:0;
	}
	
	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "delete from medicineCategories where medicineID = ?";
		return jdbcTemplate.update(sql, id);
	}
}
