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
    	MedicineCategory medicineCategory = new MedicineCategory(medicineName, medicinePurpose, medicineDescription);

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
		MedicineCategory deleted = mcs.deleteMedicineCategory(medicineID);
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
    
//    @GetMapping
//    @ResponseBody
//    public void index(HttpServletResponse response) throws IOException {
//        List<MedicineCategory> categoriesList = mcs.findAllMedicineCategories();
//
//        PrintWriter out = response.getWriter();
//        File htmlFile = new ClassPathResource("static/template.html").getFile();
//        Document doc = Jsoup.parse(htmlFile, "UTF-8");
//
//        Element body = doc.select("body").first();
//
//        Element table = new Element(Tag.valueOf("table"), "");
//        Element caption = new Element(Tag.valueOf("caption"), "").text("Medicine Categories");
//        Element trHeader = new Element(Tag.valueOf("tr"), "");
//        Element thID = new Element(Tag.valueOf("th"), "").text("Cat. No.");
//        Element thName = new Element(Tag.valueOf("th"), "").text("Cat. Name");
//        Element thPurpose = new Element(Tag.valueOf("th"), "").text("Cat. Purpose");
//        Element thDescription = new Element(Tag.valueOf("th"), "").text("Cat. Description");
//        
//        trHeader.appendChild(thID);
//        trHeader.appendChild(thName);
//        trHeader.appendChild(thPurpose);
//        trHeader.appendChild(thDescription);
//
//        table.appendChild(caption);
//        table.appendChild(trHeader);
//
//        for (int i=0; i < categoriesList.size(); i++) {
//            Element trMedicineCategory = new Element(Tag.valueOf("tr"), "");
//            Element tdID = new Element(Tag.valueOf("td"), "").text(String.valueOf(i + 1));
//            Element tdName = new Element(Tag.valueOf("td"), "").text(categoriesList.get(i).getMedicineName());
//            Element tdPurpose = new Element(Tag.valueOf("td"), "").text(categoriesList.get(i).getMedicinePurpose());
//            Element tdDescription = new Element(Tag.valueOf("td"), "").text(categoriesList.get(i).getMedicineDescription());
//            Element tdDetails = new Element(Tag.valueOf("td"), "");
//            Element aDetails = new Element(Tag.valueOf("a"), "").attr("href","MedicineCategories/details?id="+categoriesList.get(i).getMedicineID()).text("See more");
//
//            tdDetails.appendChild(aDetails);
//            trMedicineCategory.appendChild(tdID);
//            trMedicineCategory.appendChild(tdName);
//            trMedicineCategory.appendChild(tdPurpose);
//            trMedicineCategory.appendChild(tdDescription);
//            trMedicineCategory.appendChild(tdDetails);
//
//            Element tdForm = new Element(Tag.valueOf("td"), "");
//            Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "MedicineCategories/delete");
//            Element inputHidden = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(categoriesList.get(i).getMedicineID()));
//            Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Delete");
//            form.appendChild(inputHidden);
//            form.appendChild(inputSubmit);
//            tdForm.appendChild(form);
//            trMedicineCategory.appendChild(tdForm);
//            table.appendChild(trMedicineCategory);
//        }
//
//        Element ulAddCategory = new Element(Tag.valueOf("ul"), "");
//        Element liAddCategory = new Element(Tag.valueOf("li"), "");
//        Element aAddCategory = new Element(Tag.valueOf("a"), "").attr("href", "MedicineCategories/add").text("Add category");
//        liAddCategory.appendChild(aAddCategory);
//        ulAddCategory.appendChild(liAddCategory);
//
//        body.appendChild(table);
//        body.appendChild(ulAddCategory);
//
//        out.write(doc.html());
//        return;
//    }
//    
//    /** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
//	// GET: knjige/dodaj
//	@GetMapping(value="/add")
//	@ResponseBody
//	public void create(HttpServletResponse response) throws IOException {
//		PrintWriter out = response.getWriter();
//		File htmlFile = new ClassPathResource("static/template.html").getFile();
//		Document doc = Jsoup.parse(htmlFile, "UTF-8");
//
//		Element body = doc.select("body").first();
//
//		Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "add");
//		Element table = new Element(Tag.valueOf("table"), "");
//		Element caption = new Element(Tag.valueOf("caption"), "").text("Medicine category");
//
//		Element trName = new Element(Tag.valueOf("tr"), "");
//		Element thName = new Element(Tag.valueOf("th"), "").text("Name");
//		Element tdName = new Element(Tag.valueOf("td"), "");
//		Element inputName = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "name");
//
//		tdName.appendChild(inputName);
//		trName.appendChild(thName);
//		trName.appendChild(tdName);
//		
//		Element trPurpose = new Element(Tag.valueOf("tr"), "");
//		Element thPurpose = new Element(Tag.valueOf("th"), "").text("Purpose");
//		Element tdPurpose = new Element(Tag.valueOf("td"), "");
//		Element inputPurpose = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "purpose");
//
//		tdPurpose.appendChild(inputPurpose);
//		trPurpose.appendChild(thPurpose);
//		trPurpose.appendChild(tdPurpose);
//		
//		Element trDescription = new Element(Tag.valueOf("tr"), "");
//		Element thDescription = new Element(Tag.valueOf("th"), "").text("Description");
//		Element tdDescription = new Element(Tag.valueOf("td"), "");
//		Element descriptionArea = new Element(Tag.valueOf("textarea"), "").attr("name", "description");
//
//		tdDescription.appendChild(descriptionArea);
//		trDescription.appendChild(thDescription);
//		trDescription.appendChild(tdDescription);
//
//		
//		Element trSubmit = new Element(Tag.valueOf("tr"), "");
//		Element thSubmit = new Element(Tag.valueOf("th"), "");
//		Element tdSubmit = new Element(Tag.valueOf("td"), "");
//		Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Add");
//
//		tdSubmit.appendChild(inputSubmit);
//		trSubmit.appendChild(thSubmit);
//		trSubmit.appendChild(tdSubmit);
//
//		table.appendChild(caption);
//
//		table.appendChild(trName);
//		table.appendChild(trPurpose);
//		table.appendChild(trDescription);
//		table.appendChild(trSubmit);
//
//		form.appendChild(table);
//
//		body.appendChild(form);
//
//		out.write(doc.html());
//		return;
//	}
//    
//	@PostMapping(value="/add")
//	public void create(@RequestParam String name, @RequestParam String purpose, 
//			@RequestParam String description, HttpServletResponse response) throws IOException {		
//		MedicineCategory mc = new MedicineCategory(name, purpose, description);
//		MedicineCategory saved = mcs.saveMedicineCategory(mc);
//		response.sendRedirect(bURL +"MedicineCategories");
//	}
//	
//	@PostMapping(value="/edit")
//	public void Edit(@ModelAttribute MedicineCategory categoryEdited, HttpServletResponse response) throws IOException {	
//		MedicineCategory mc = mcs.findOneMedicineCategoryByID(categoryEdited.getMedicineID());
//		if(mc != null) {
//			if(categoryEdited.getMedicineName() != null && !categoryEdited.getMedicineName().trim().equals(""))
//				mc.setMedicineName(categoryEdited.getMedicineName());
//			if(categoryEdited.getMedicinePurpose() != null && !categoryEdited.getMedicinePurpose().trim().equals(""))
//				mc.setMedicinePurpose(categoryEdited.getMedicinePurpose());
//			if(categoryEdited.getMedicineDescription() != null && !categoryEdited.getMedicineDescription().trim().equals(""))
//				mc.setMedicineDescription(categoryEdited.getMedicineDescription());
//		}
//		MedicineCategory saved = mcs.updateMedicineCategory(mc);
//		response.sendRedirect(bURL+"MedicineCategories");
//	}
//	
//	@PostMapping(value="/delete")
//	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {		
//		MedicineCategory deleted = mcs.deleteMedicineCategory(id);
//		response.sendRedirect(bURL+"MedicineCategories");
//	}
//	
//	@GetMapping(value="/details")
//	@ResponseBody
//	public void details(@RequestParam Long id, HttpServletResponse response) throws IOException {
//		MedicineCategory mc = mcs.findOneMedicineCategoryByID(id);
//
//		PrintWriter out;
//		out = response.getWriter();
//		File htmlFile = new ClassPathResource("static/template.html").getFile();
//		Document doc = Jsoup.parse(htmlFile, "UTF-8");
//
//		Element body = doc.select("body").first();
//
//		
//
//		Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "edit");
//		Element table = new Element(Tag.valueOf("table"), "");
//		Element caption = new Element(Tag.valueOf("caption"), "").text("Medicine category");
//
//		Element inputHidden = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(mc.getMedicineID()));
//
//		Element trName = new Element(Tag.valueOf("tr"), "");
//		Element thName = new Element(Tag.valueOf("th"), "").text("Name");
//		Element tdName = new Element(Tag.valueOf("td"), "");
//		Element inputName = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "name").attr("value", mc.getMedicineName());
//
//		tdName.appendChild(inputName);
//		trName.appendChild(thName);
//		trName.appendChild(tdName);
//
//		Element trPurpose = new Element(Tag.valueOf("tr"), "");
//		Element thPurpose = new Element(Tag.valueOf("th"), "").text("Purpose");
//		Element tdPurpose = new Element(Tag.valueOf("td"), "");
//		Element inputPurpose = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "purpose").attr("value", mc.getMedicinePurpose());
//
//		tdPurpose.appendChild(inputPurpose);
//		trPurpose.appendChild(thPurpose);
//		trPurpose.appendChild(tdPurpose);
//
//		Element trDescription = new Element(Tag.valueOf("tr"), "");
//		Element thDescription = new Element(Tag.valueOf("th"), "").text("Description");
//		Element tdDescription = new Element(Tag.valueOf("td"), "");
//		Element descriptionArea = new Element(Tag.valueOf("textarea"), "").attr("name", "description").attr("value", mc.getMedicineDescription());
//
//		tdDescription.appendChild(descriptionArea);
//		trDescription.appendChild(thDescription);
//		trDescription.appendChild(tdDescription);
//
//		Element trSubmit = new Element(Tag.valueOf("tr"), "");
//		Element thSubmit = new Element(Tag.valueOf("th"), "");
//		Element tdSubmit = new Element(Tag.valueOf("td"), "");
//		Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Save changes");
//
//		tdSubmit.appendChild(inputSubmit);
//		trSubmit.appendChild(thSubmit);
//		trSubmit.appendChild(tdSubmit);
//
//		table.appendChild(caption);
//
//		table.appendChild(trName);
//		table.appendChild(trPurpose);
//		table.appendChild(trDescription);
//		table.appendChild(trSubmit);
//
//		form.appendChild(inputHidden);
//		form.appendChild(table);
//
//		Element formDelete = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "delete");
//		Element inputSubmitDelete = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Delete");
//		Element inputHiddenDelete = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(mc.getMedicineID()));
//		formDelete.appendChild(inputHiddenDelete);
//		formDelete.appendChild(inputSubmitDelete);
//
//		body.appendChild(form);
//		body.appendChild(formDelete);
//
//		out.write(doc.html());
//		return;
//	}
}
