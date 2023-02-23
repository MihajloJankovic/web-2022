package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.MedicineCategoryDAO;
import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@Qualifier("catFiles")
public class MedicineCategoryService implements IMedicineCategoryService {
	
	@Autowired
	private MedicineCategoryDAO medicineCategoryDAO;
	
	@Override
	public MedicineCategory findOneMedicineCategoryByID(Long id) {
		return medicineCategoryDAO.findOne(id);
	}
	public MedicineCategory findOneMedicineCategoryByName(String name)
	{
		return medicineCategoryDAO.findOneMedicineCategoryByName(name);
	}
	@Override
	public List<MedicineCategory> findAllMedicineCategories() {
		return medicineCategoryDAO.findAll();
	}

	@Override
	public List<MedicineCategory> findAllUNDELETED()
	{
		return  medicineCategoryDAO.findAllUNDELETED();
	}
	public int activate(Long id) {
		return medicineCategoryDAO.activate(id);
	}
	@Override
	public MedicineCategory saveMedicineCategory(MedicineCategory mc) {
		medicineCategoryDAO.save(mc);
		return mc;
	}

	@Override
	public MedicineCategory updateMedicineCategory(MedicineCategory mc) {
		medicineCategoryDAO.update(mc);
		return mc;
	}

	@Override
	public MedicineCategory deleteMedicineCategory(Long id) {
		MedicineCategory medicineCategory = medicineCategoryDAO.findOne(id);
		medicineCategoryDAO.delete(id);
		return medicineCategory;
	}
//    @Value("${medicineCategories.pathToFile}")
//    private String pathToFile;
//
//    private Map<Long, MedicineCategory> readMedicineCategoryFromFile() {
//
//        Map<Long, MedicineCategory> categoriesList = new HashMap<>();
//        Long nextId = 1L;
//
//        try {
//            Path path = Paths.get(pathToFile);
//            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
//
//            for (String line : lines) {
//                line = line.trim();
//                if (line.equals("") || line.indexOf('#') == 0)
//                    continue;
//
//                String[] tokens = line.split("\\|");
//                Long id = Long.parseLong(tokens[0]);
//                String name = tokens[1];
//                String purpose = tokens[2];
//                String description = tokens[3];
//
//                categoriesList.put(id, new MedicineCategory(id, name, purpose, description));
//
//                if(nextId<id)
//                    nextId=id;
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//        return categoriesList;
//    }
//
//    private Map<Long, MedicineCategory> saveMedicineCategoryToFile(Map<Long, MedicineCategory> categoriesList) {
//
//        Map<Long, MedicineCategory> ret = new HashMap<>();
//
//        try {
//            Path path = Paths.get(pathToFile);
//            List<String> fileLines = new ArrayList<>();
//
//            for (MedicineCategory md : categoriesList.values()) {
//                fileLines.add(md.toString());
//                ret.put(md.getMedicineID(), md);
//            }
//            Files.write(path, fileLines, Charset.forName("UTF-8"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private Long nextId(Map<Long, MedicineCategory> categoriesList) {
//        Long nextId = 1L;
//        for (Long id : categoriesList.keySet()) {
//            if(nextId<id)
//                nextId=id;
//        }
//        return ++nextId;
//    }
//
//    @Override
//    public MedicineCategory findOneMedicineCategoryByID(Long id) {
//        Map<Long, MedicineCategory> categoriesList = readMedicineCategoryFromFile();
//        return categoriesList.get(id);
//    }
//
//    @Override
//    public List<MedicineCategory> findAllMedicineCategories() {
//        Map<Long, MedicineCategory> categoriesList = readMedicineCategoryFromFile();
//        return new ArrayList<>(categoriesList.values());
//    }
//
//    @Override
//    public MedicineCategory saveMedicineCategory(MedicineCategory mc) {
//        Map<Long, MedicineCategory> categoriesList = readMedicineCategoryFromFile();
//        Long nextId = nextId(categoriesList);
//
//        if (mc.getMedicineID() == null) {
//            mc.setMedicineID(nextId++);
//
//        }
//        categoriesList.put(mc.getMedicineID(), mc);
//        saveMedicineCategoryToFile(categoriesList);
//        return mc;
//    }
//
//    @Override
//    public MedicineCategory updateMedicineCategory(MedicineCategory mc) {
//        Map<Long, MedicineCategory> categoriesList = readMedicineCategoryFromFile();
//
//        categoriesList.put(mc.getMedicineID(), mc);
//        saveMedicineCategoryToFile(categoriesList);
//        return mc;
//    }
//
//    @Override
//    public MedicineCategory deleteMedicineCategory(Long id) {
//        Map<Long, MedicineCategory> categoriesList = readMedicineCategoryFromFile();
//
//        if (!categoriesList.containsKey(id)) {
//            throw new IllegalArgumentException("Category do not exist!");
//        }
//
//        MedicineCategory mc = categoriesList.get(id);
//        if (mc != null) {
//            categoriesList.remove(id);
//        }
//        saveMedicineCategoryToFile(categoriesList);
//        return mc;
//    }

}

