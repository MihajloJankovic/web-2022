package com.ftn.Pharmacy.Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.model.*;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;
import com.ftn.Pharmacy.Project.service.MedicineService;
import com.ftn.Pharmacy.Project.service.implementation.Manucfecturere;
import com.ftn.Pharmacy.Project.service.implementation.OrderService;
import com.mysql.cj.xdevapi.JsonString;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

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
    private OrderService ord;
    @Autowired
    HttpSession sesija;
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
        List<Manucfecturer> mann = mab.findAll();
        List<MedicineCategory> cat = mcs.findAllUNDELETED();
        ModelAndView result = new ModelAndView("addMedicine");
        result.addObject("medicine", medicine);
        result.addObject("Man", mann);
        result.addObject("tip",Type.values());
        result.addObject("kat",cat);
        return result;
    }

    @PostMapping(value="/add")
    public void create(@RequestParam String medicineName, @RequestParam("file")  MultipartFile file, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<MedicineCategory> allCategories = mcs.findAllMedicineCategories();

        ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());
        BufferedImage photo = ImageIO.read(bis);

        Manucfecturer man = new Manucfecturer();
        man = mab.findOneByName(manufecturer);
        Medicine medicine = new Medicine(medicineName,Description,Contraindications, Type.valueOf(type),mcs.findOneMedicineCategoryByName(medicineCategory),NumberofItems,price,man,photo);
                medicineService.save(medicine);




        response.sendRedirect(bURL + "Medicine");
    }
    @GetMapping(value="/order")
    public ModelAndView create(@RequestParam Long id , HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<Medicine> meds = medicineService.findAll();


        ModelAndView result = new ModelAndView("orderMedicine");

        result.addObject("Man", meds);

        return result;
    }
    @PostMapping(value="/order")
    public void create(@RequestBody String json,HttpServletResponse response, HttpServletRequest request) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(json, Map.class);
        Map<String,Integer> result = new ObjectMapper().readValue(map.get("mapa").toString(), HashMap.class);
        String tekst = map.get("com").toString();


       User pera = (User) sesija.getAttribute("user");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateNow = LocalDateTime.now();
        String formatDateTime = dateNow.format(formatter);
        LocalDateTime registeredTime = LocalDateTime.parse(formatDateTime, formatter);
       Order or = new Order(tekst,false,false,pera,registeredTime,result);
       ord.save(or);

    }

    @PostMapping(value="/edit")
    public void Edit(@RequestParam String medicineName,@RequestParam long id, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response) throws IOException {
       Manucfecturer man = new Manucfecturer();
       man = mab.findOneByName(manufecturer);
        Medicine medicine = new Medicine(id,medicineName,Description,Contraindications, Type.valueOf(type),mcs.findOneMedicineCategoryByName(medicineCategory),NumberofItems,price,man);

        medicineService.update(medicine);
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
        List<MedicineCategory> cat = mcs.findAllMedicineCategories();
        ModelAndView result = new ModelAndView("DetailsM");
        result.addObject("medicineCategory", medicineCategory);
        result.addObject("Man", mann);
        result.addObject("tip",Type.values());
        result.addObject("kat",cat);
        return result;
    }


}
