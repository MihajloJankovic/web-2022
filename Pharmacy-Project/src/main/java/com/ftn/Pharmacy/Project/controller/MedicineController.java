package com.ftn.Pharmacy.Project.controller;

import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.model.Type;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;
import com.ftn.Pharmacy.Project.service.MedicineService;
import com.ftn.Pharmacy.Project.service.implementation.Manucfecturere;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value="/Medicine")
public class MedicineController  implements ServletContextAware {





    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private ServletContext servletContext;

    private String bURL;
    @Autowired
    private Manucfecturere mab;
    @Autowired
    private IMedicineCategoryService mcs;

    @Autowired
    private MedicineDao medicineService;




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




        List<Medicine> medicineCategories = medicineService.findAll();
        ModelAndView result = new ModelAndView("Medicine");
        result.addObject("medicineCategories", medicineCategories);
        return result;
    }

    @GetMapping(value="/add")
    public ModelAndView create() {
        List<MedicineCategory> medicine = mcs.findAllMedicineCategories();
        ModelAndView result = new ModelAndView("addMedicine");
        result.addObject("medicine", medicine);
        return result;
    }

    @PostMapping(value="/add")
    public void create(@RequestParam String medicineName, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response) throws IOException {
        List<MedicineCategory> allCategories = mcs.findAllMedicineCategories();
        Medicine medicine = new Medicine(medicineName,Description,Contraindications, Type.valueOf(type),mcs.findOneMedicineCategoryByID(Long.valueOf(medicineCategory)),NumberofItems,price,manufecturer);
                medicineService.save(medicine);




        response.sendRedirect(bURL + "Medicine");
    }


    @PostMapping(value="/edit")
    public void Edit(@RequestParam String medicineName, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response) throws IOException {
        Medicine medicine = new Medicine(medicineName,Description,Contraindications, Type.valueOf(type),mcs.findOneMedicineCategoryByID(Long.valueOf(medicineCategory)),NumberofItems,price,manufecturer);


        response.sendRedirect(servletContext.getContextPath() + "/Medicine");
    }

    @SuppressWarnings("unused")
    @PostMapping(value="/delete")
    public void delete(Long medicineID, HttpServletResponse response) throws IOException {
       medicineService.delete(medicineID);
        response.sendRedirect(bURL+"Medicine");
    }
    @GetMapping("changeLang")
    public void promeniJezik(@RequestParam(defaultValue="en") String jezik, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Locale lokalizacija = localeResolver.resolveLocale(request);
        if(jezik.equals("sr")) {
            localeResolver.setLocale(request, response, Locale.forLanguageTag("sr"));
        } else if (jezik.equals("en")) {
            localeResolver.setLocale(request, response, Locale.ENGLISH);
        }
        session.setAttribute("loc", lokalizacija );
        response.sendRedirect(bURL);
        return;
    }
    @GetMapping(value="/details")
    @ResponseBody
    public ModelAndView details(Long id) {
        Medicine medicineCategory = medicineService.findOne(id);
        List<Manucfecturer> mann = mab.findAll();
        ModelAndView result = new ModelAndView("DetailsM");
        result.addObject("medicineCategory", medicineCategory);
        result.addObject("Man", mann);


        return result;
    }


}
