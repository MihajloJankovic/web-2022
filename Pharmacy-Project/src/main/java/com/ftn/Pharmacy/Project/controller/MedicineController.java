package com.ftn.Pharmacy.Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.dao.impl.OrderDAO;
import com.ftn.Pharmacy.Project.model.*;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;
import com.ftn.Pharmacy.Project.service.MedicineService;
import com.ftn.Pharmacy.Project.service.implementation.Manucfecturere;
import com.ftn.Pharmacy.Project.service.implementation.OrderService;

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






    @GetMapping
    public ModelAndView index(HttpServletResponse response,HttpServletRequest request) {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {


            return result;

        }

        List<Medicine> medicineCategories = medicineService.findAll();
        ModelAndView result1 = new ModelAndView("Medicine");
        result1.addObject("medicineCategories", medicineCategories);
        return result1;
    }

    @GetMapping(value="/add")
    public ModelAndView create(HttpServletRequest request,HttpServletResponse response) throws IOException {
        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;

        }
        List<MedicineCategory> medicine = mcs.findAllMedicineCategories();
        List<Manucfecturer> mann = mab.findAll();
        List<MedicineCategory> cat = mcs.findAllUNDELETED();
        ModelAndView result1 = new ModelAndView("addMedicine");
        result1.addObject("medicine", medicine);
        result1.addObject("Man", mann);
        result1.addObject("tip",Type.values());
        result1.addObject("kat",cat);
        return result1;
    }

    @PostMapping(value="/add")
    public void create(@RequestParam String medicineName, @RequestParam("file")  MultipartFile file, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response, HttpServletRequest request) throws IOException {

        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;

        }
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
        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;

        }
        List<Medicine> meds = medicineService.findAll();


        ModelAndView result1 = new ModelAndView("orderMedicine");

        result1.addObject("Man", meds);

        return result1;
    }
    @PostMapping(value="/order")
    public void create(@RequestBody String json,HttpServletResponse response, HttpServletRequest request) throws IOException {


        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
           return;

        }
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
    @GetMapping(value="/adminorder")
    public ModelAndView create( HttpServletResponse response, HttpServletRequest request) throws IOException {
        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;

        }

        List<Order> meds =  ord.findAllforAprovval();



        ModelAndView result1 = new ModelAndView("orderadmin");
        result1.addObject("user",loggedUser);

        result1.addObject("Man", meds);

        return result1;





    }
    @PostMapping(value="/adminorder")
    public void create1(@RequestBody String json,HttpServletResponse response, HttpServletRequest request) throws IOException {

        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;

        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = mapper.readValue(json, Map.class);
        int i = Integer.parseInt(map.get("id"));
        int param = Integer.parseInt(map.get("param"));
        String come = map.get("coment");

        if(param == 1)
        {
            Order aa = ord.findOneForReview(Long.valueOf(i));
            for (String key: aa.getMapa().keySet())
            {
                Medicine lek = medicineService.findOneByName(key);
                int broj = lek.getNumberofItems();
                int lekbroj = aa.getMapa().get(key);
                lek.setNumberofItems(broj+lekbroj);
                medicineService.update(lek);

            }
            ord.update1((long) i);

        }
        if(param == 2)
        {
            ord.update2((long) i);
        }
        if(param == 3)
        {
            ord.update3((long) i,come);
        }


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


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
