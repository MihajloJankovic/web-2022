package com.ftn.Pharmacy.Project.controller;

import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping(value="/MedicineCategories")
public class MedicineCategoryController implements ServletContextAware {

    public static final String MED_CAT = "medicineCat";
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;
    @Autowired
    private ServletContext servletContext;

    private String bURL;

    @Autowired
    private IMedicineCategoryService mcs;

	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
	}

    @Override
    public void setServletContext(ServletContext servletContext) {
    	this.servletContext = servletContext;
    }
    
    @GetMapping
    public ModelAndView index() {
    	List<MedicineCategory> medicineCategories = mcs.findAllMedicineCategories();
    	ModelAndView result = new ModelAndView("medicineCategories");
    	result.addObject("medicineCategories", medicineCategories);
    	return result;
    }
    
    @GetMapping(value="/add")
    public ModelAndView create() {
    	List<MedicineCategory> medicineCategories = mcs.findAllMedicineCategories();
    	ModelAndView result = new ModelAndView("addMedicineCategory");
    	result.addObject("medicineCategories", medicineCategories);
    	return result;
    }
    
    @PostMapping(value="/add")
    public void create(@RequestParam String medicineName, @RequestParam String medicinePurpose, @RequestParam String medicineDescription, HttpServletResponse response) throws IOException{
		List<MedicineCategory> allCategories = mcs.findAllMedicineCategories();
    	MedicineCategory medicineCategory = new MedicineCategory(medicineName, medicinePurpose, medicineDescription,false);

    	if(medicineCategory != null) {
    		if(medicineName != null && !medicineName.trim().equals("")) {
    			medicineCategory.setMedicineName(medicineName);
    		}
			if(medicinePurpose != null && !medicinePurpose.trim().equals("")) {
			    medicineCategory.setMedicinePurpose(medicinePurpose);		
			}
			if(medicineDescription != null && !medicineDescription.trim().equals("")) {
				medicineCategory.setMedicineDescription(medicineDescription);
			}
    	}
    	mcs.saveMedicineCategory(medicineCategory);
    	response.sendRedirect(bURL + "MedicineCategories");
    }
    
    
	@PostMapping(value="/edit")
	public void Edit(@RequestParam Long medicineID, @RequestParam String medicineName, @RequestParam String medicinePurpose,  
			@RequestParam String medicineDescription, HttpServletResponse response) throws IOException {	
    	MedicineCategory medicineCategory = mcs.findOneMedicineCategoryByID(medicineID);
		if(medicineCategory != null) {
    		if(medicineName != null && !medicineName.trim().equals("")) {
    			medicineCategory.setMedicineName(medicineName);
    		}
			if(medicinePurpose != null && !medicinePurpose.trim().equals("")) {
			    medicineCategory.setMedicinePurpose(medicinePurpose);		
			}
			if(medicineDescription != null && !medicineDescription.trim().equals("")) {
				medicineCategory.setMedicineDescription(medicineDescription);
			}
    	}
		MedicineCategory saved = mcs.updateMedicineCategory(medicineCategory);
		response.sendRedirect(servletContext.getContextPath() + "/MedicineCategories");
	}
    
    @SuppressWarnings("unused")
	@PostMapping(value="/delete")
	public void delete(Long medicineID, HttpServletResponse response) throws IOException {		
		MedicineCategory deleted = mcs.findOneMedicineCategoryByID(medicineID);
		if( deleted.getDeleted() == false)
		{
			mcs.deleteMedicineCategory(medicineID);
		}
		else {
			mcs.activate(medicineID);
		}
		response.sendRedirect(bURL+"MedicineCategories");
	}
    
    @GetMapping(value="/details")
	@ResponseBody
	public ModelAndView details(Long id) {	
		MedicineCategory medicineCategory = mcs.findOneMedicineCategoryByID(id);
		ModelAndView result = new ModelAndView("medicineCategory");
		result.addObject("medicineCategory", medicineCategory);

		return result;
	}

}
