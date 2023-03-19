package com.ftn.Pharmacy.Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.dao.impl.BuyReportDAO;
import com.ftn.Pharmacy.Project.dao.impl.OrderDAO;
import com.ftn.Pharmacy.Project.model.*;
import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;
import com.ftn.Pharmacy.Project.service.MedicineService;
import com.ftn.Pharmacy.Project.service.implementation.CommentService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private BuyReportDAO reportDAO;


    private String bURL;
    @Autowired
    private Manucfecturere mab;
    @Autowired
    private OrderService ord;
    @Autowired
    private CommentService CommentS;
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
        if(loggedUser.getRole() == UserRole.CUSTOMER)
        {


            return result;

        }

        List<Medicine> medicineCategories = medicineService.findAll();
        ModelAndView result1 = new ModelAndView("Medicine");
        result1.addObject("medicineCategories", medicineCategories);
        return result1;
    }
    @GetMapping(value = "shop")
    public ModelAndView shop(HttpServletResponse response,HttpServletRequest request) {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);



        List<Medicine> medicineCategories = medicineService.findAllForShop();
        ModelAndView result1 = new ModelAndView("shop");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("log",loggedUser);
        return result1;
    }
    @GetMapping(value = "addcart")
    public ModelAndView cart(@RequestParam Long id,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if(ses.getAttribute("mapa")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa");


              if(meds.get(id)!=null)
              {
                  int p = meds.get(id);
                  meds.put(id,p+1);
              }
              else {

                  meds.put(id,1);
              }
                ses.setAttribute("mapa",meds);
        }
        else {
            meds.put(id,1);
            ses.setAttribute("mapa",meds);
        }

        List<Medicine> medicineCategories = medicineService.findAllForShop();
        ModelAndView result1 = new ModelAndView("shop");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("log",loggedUser);
        return result1;
    }
    @PostMapping(value = "SaveCart")
    public ModelAndView cart1(@RequestBody String json,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Integer> map = mapper.readValue(json, Map.class);
        Map<Long, Integer> meds = new LinkedHashMap<>();
        if(ses.getAttribute("mapa")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa");
            for(String a : map.keySet())
            {
                meds.put(Long.valueOf(a),map.get(a));
            }
            ses.setAttribute("mapa",meds);
        }



        List<Medicine> medicineCategories = medicineService.findAllForShop();
        ModelAndView result1 = new ModelAndView("shop");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("log",loggedUser);
        return result1;
    }
    @ResponseBody
    @PostMapping(value = "BuyCart")
    public String car(@RequestBody String json,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {


        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return "false";


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return "false";

        }
        int ab = 0;
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Integer> map = mapper.readValue(json, Map.class);
        Map<Long, Integer> meds = new LinkedHashMap<>();
        if(ses.getAttribute("mapa")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa");
            for(String a : map.keySet())
            {
               Medicine lek =  medicineService.findOne(Long.valueOf(a));
               if(lek.getNumberofItems() < meds.get(Long.valueOf(a)))
               {
                   ab=1;
                   return "false";
               }
            }

            Map<Medicine, Map<Integer, Integer>> mapa = new LinkedHashMap<>();
            for(String a : map.keySet())
            {
                Medicine lek =  medicineService.findOne(Long.valueOf(a));
                lek.setNumberofItems(lek.getNumberofItems()- meds.get(Long.valueOf(a)));
                medicineService.update(lek);
                Map<Integer, Integer> mapa2 = new LinkedHashMap<>();
                mapa2.put(meds.get(Long.valueOf(a)),lek.getPrice());
                mapa.put(lek,mapa2);
            }



            Report pera = new Report(loggedUser,LocalDate.now(),mapa);
            reportDAO.save(pera);
            return "true";



        }

        return "false";
    }
    @GetMapping(value = "addwish")
    public ModelAndView wish(@RequestParam Long id,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if(ses.getAttribute("mapa1")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa1");

                meds.put(id,1);

            ses.setAttribute("mapa1",meds);
        }
        else {
            meds.put(id,1);
            ses.setAttribute("mapa1",meds);
        }

        List<Medicine> medicineCategories = medicineService.findAllForShop();
        ModelAndView result1 = new ModelAndView("shop");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("log",loggedUser);
        return result1;
    }
    @GetMapping(value = "removewish")
    public ModelAndView wishremove(@RequestParam Long id,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();
        List<Medicine> medicineCategories = new ArrayList<Medicine>();
        if(ses.getAttribute("mapa1")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa1");

            meds.remove(id,1);

            ses.setAttribute("mapa1",meds);

            for (Long a:meds.keySet())
            {
                Medicine lek = medicineService.findOne(a);
                medicineCategories.add(lek);
            }
        }

        ModelAndView result1 = new ModelAndView("wiches");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("log",loggedUser);
        return result1;

    }

    @GetMapping(value = "ClearCart")
    public ModelAndView cartremove(HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {


        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if (loggedUser.getRole() != UserRole.CUSTOMER) {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if (ses.getAttribute("mapa") != null) {
            request.getSession().removeAttribute("mapa");

        }
        List<Integer> brojevi = new ArrayList<Integer>();
        List<Medicine> medicineCategories = new ArrayList<Medicine>();
        ModelAndView result1 = new ModelAndView("cart");
        result1.addObject("medicineCategories", medicineCategories);
        result1.addObject("nums",brojevi);
        result1.addObject("log",loggedUser);
        return result1;
    }
    @GetMapping(value = "removeCart")
    public ModelAndView cartremove(@RequestParam Long id,HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {


        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if(ses.getAttribute("mapa")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa");
            meds.remove(id);
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            List<Integer> brojevi = new ArrayList<Integer>();
            for (Long a:meds.keySet())
            {
                Medicine lek = medicineService.findOne(a);
                medicineCategories.add(lek);
                brojevi.add(meds.get(a));
            }
            ModelAndView result1 = new ModelAndView("cart");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("nums",brojevi);
            result1.addObject("log",loggedUser);
            return result1;

        }
        else {
            List<Integer> brojevi = new ArrayList<Integer>();
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            ModelAndView result1 = new ModelAndView("cart");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("nums",brojevi);
            result1.addObject("log",loggedUser);
            return result1;
        }

    }
    @GetMapping(value = "wishList")
    public ModelAndView wish(HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if(ses.getAttribute("mapa1")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa1");
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            for (Long a:meds.keySet())
            {
                Medicine lek = medicineService.findOne(a);
                medicineCategories.add(lek);
            }
            ModelAndView result1 = new ModelAndView("wiches");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("log",loggedUser);
            return result1;

        }
        else {
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            ModelAndView result1 = new ModelAndView("wiches");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("log",loggedUser);
            return result1;
        }
    }
    @GetMapping(value = "Cart")
    public ModelAndView Cart(HttpServletResponse response,HttpServletRequest request,HttpSession ses) throws IOException {

        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.CUSTOMER)
        {

            return result;

        }

        Map<Long, Integer> meds = new LinkedHashMap<>();

        if(ses.getAttribute("mapa")!=null)
        {
            meds = (Map<Long, Integer>) request.getSession().getAttribute("mapa");
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            List<Integer> brojevi = new ArrayList<Integer>();
            for (Long a:meds.keySet())
            {
                Medicine lek = medicineService.findOne(a);
                medicineCategories.add(lek);
                brojevi.add(meds.get(a));
            }
            ModelAndView result1 = new ModelAndView("cart");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("nums",brojevi);
            result1.addObject("log",loggedUser);
            return result1;

        }
        else {
            List<Integer> brojevi = new ArrayList<Integer>();
            List<Medicine> medicineCategories = new ArrayList<Medicine>();
            ModelAndView result1 = new ModelAndView("cart");
            result1.addObject("medicineCategories", medicineCategories);
            result1.addObject("nums",brojevi);
            result1.addObject("log",loggedUser);
            return result1;
        }
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
        if(loggedUser.getRole() != UserRole.PHARMACIST)
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
        if(loggedUser.getRole() != UserRole.PHARMACIST)
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
    @GetMapping(value="/orderfar")
    public ModelAndView create1(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;


        }
        if(loggedUser.getRole() != UserRole.PHARMACIST)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return result;

        }
        List<Order> ords = ord.findAllforEDIT(loggedUser.getUserID());
        List<Order> ordss = ord.findAllDeleted(loggedUser.getUserID());

        ModelAndView result1 = new ModelAndView("FrmacistOrders");

        result1.addObject("Man", ords);
        result1.addObject("Mana", ordss);

        return result1;
    }
    @PostMapping(value="/orderfar")
    public void create2(@RequestBody String json,HttpServletResponse response, HttpServletRequest request) throws IOException {


        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(loggedUser.getRole() != UserRole.PHARMACIST )
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;

        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = mapper.readValue(json, Map.class);
        int i = Integer.parseInt(map.get("id"));
        String come = map.get("coment");

        ord.update4((long) i,come);



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
    public void Edit(@RequestParam String medicineName,@RequestParam long id, @RequestParam String Description, @RequestParam String Contraindications, @RequestParam String type, @RequestParam String medicineCategory, @RequestParam int NumberofItems, @RequestParam int price, @RequestParam String manufecturer, HttpServletResponse response,HttpServletRequest request) throws IOException {
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
       Manucfecturer man = new Manucfecturer();
       man = mab.findOneByName(manufecturer);
        Medicine medicine = new Medicine(id,medicineName,Description,Contraindications, Type.valueOf(type),mcs.findOneMedicineCategoryByName(medicineCategory),NumberofItems,price,man);

        medicineService.update(medicine);
        response.sendRedirect(servletContext.getContextPath() + "/Medicine");
    }

    @PostMapping(value="/aprv")
    public void Edit(@RequestParam Long aprv,HttpServletResponse response,HttpServletRequest request) throws IOException {
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(loggedUser.getRole() != UserRole.PHARMACIST)
        {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;

        }

        Medicine medicine = medicineService.findOne(aprv);
        medicine.setApproved(true);

        medicineService.update2(medicine);
        response.sendRedirect(servletContext.getContextPath() + "/Medicine");
    }

    @PostMapping(value="/addComment")
    public void Edit(@RequestParam int med,@RequestParam int points, @RequestParam String com,@RequestParam(required = false) Boolean ano, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {

            response.sendRedirect(bURL + "LogInLogOut/login");
            return;


        }
        if(ano == null)
        {
            ano = false;
        }
        LocalDate date = LocalDate.now();

        Comment pera = new Comment(com,points,date,loggedUser,med,ano);
        CommentS.save(pera);

        response.setHeader("Refresh", "0; URL="+ request.getContextPath());
    }
    @SuppressWarnings("unused")
    @PostMapping(value="/delete")
    public void delete(Long medicineID, HttpServletResponse response,HttpServletRequest request) throws IOException {
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
    public ModelAndView details(Long id,HttpServletRequest request,HttpServletResponse response) {
        ModelAndView result = new ModelAndView("UserLogin");
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        if (loggedUser == null) {


            return result;


        }
        if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
        {


            return result;

        }
        Medicine medicineCategory = medicineService.findOne(id);
        List<Manucfecturer> mann = mab.findAll();
        List<MedicineCategory> cat = mcs.findAllMedicineCategories();
        ModelAndView result1 = new ModelAndView("DetailsM");
        result1.addObject("medicineCategory", medicineCategory);
        result1.addObject("Man", mann);
        result1.addObject("tip",Type.values());
        result1.addObject("kat",cat);
        return result1;
    }
    @GetMapping(value="/detailsU")
    @ResponseBody
    public ModelAndView detailss(Long id,HttpServletRequest request) {
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
        List<Comment> coms = CommentS.getComments(id);
        Medicine medicineCategory = medicineService.findOne(id);
        List<Manucfecturer> mann = mab.findAll();
        List<MedicineCategory> cat = mcs.findAllMedicineCategories();
        ModelAndView result = new ModelAndView("MedicineDetailsUser");
        result.addObject("medicineCategory", medicineCategory);
        result.addObject("Man", mann);
        result.addObject("tip",Type.values());
        result.addObject("kat",cat);
        result.addObject("coms",coms);
        result.addObject("log",loggedUser);
        return result;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
