package com.ftn.PrviMavenVebProjekat.controller;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration;
import com.ftn.PrviMavenVebProjekat.model.Kategorija_Leka;
import com.ftn.PrviMavenVebProjekat.model.Kategorije_Lekova;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
    @RequestMapping(value = "Kategorija")
    public class Kategorija_Controller implements ApplicationContextAware {

        public static final String KATEGORIJA_KEY = "kategorija";

        @Autowired
        private ServletContext servletContext;
        private String bURL;

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private SecondConfiguration.ApplicationMemory memorijaAplikacije;

        /**
         * pristup ApplicationContext
         */
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        /**
         * inicijalizacija podataka za kontroler
         */
        @SuppressWarnings("unchecked")
        @PostConstruct
        public void init() {
            bURL = servletContext.getContextPath() + "/Kategorija/";
            memorijaAplikacije = applicationContext.getBean(SecondConfiguration.ApplicationMemory.class);
            Kategorije_Lekova kategorije = new Kategorije_Lekova();


//		servletContext.setAttribute(KnjigeController.KNJIGE_KEY, knjige);

            memorijaAplikacije.put(Kategorija_Controller.KATEGORIJA_KEY,kategorije);
        }


        /** pribavnjanje HTML stanice za prikaz svih entiteta, get zahtev */
        // GET: knjige
        @GetMapping
        @ResponseBody
        public String index()
        {
            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);
            StringBuilder str = new StringBuilder();
            List<Kategorija_Leka> lista = kategorije.findAll();
            str.append("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<style type=\"text/css\">\n" +
                    "    .tg  {border-collapse:collapse;border-spacing:0;}\n" +
                    "    .tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;\n" +
                    "        overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                    "    .tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;\n" +
                    "        font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                    "    .tg .tg-0lax{text-align:left;vertical-align:top}\n" +
                    "</style>\n" +
                    "<table class=\"tg\">\n" +
                    "    <thead>\n" +
                    "    <tr>\n" +
                    "        <th class=\"tg-0lax\">Naziv</th>\n" +
                    "        <th class=\"tg-0lax\">Namena</th>\n" +
                    "        <th class=\"tg-0lax\">Opis</th>\n" +
                    "    </tr>\n" +
                    "    </thead>");

                    for(int i =0; i < lista.size();i++)
                    {
                        str.append("<tbody>\n" +
                                "    <tr>\n" +
                                "        <td class=\"tg-0lax\"><a href=\""+bURL+"details?id="+lista.get(i).Naziv+"\">"+lista.get(i).Naziv+"</a></td>\n" +
                                "        <td class=\"tg-0lax\"><a>"+lista.get(i).Namena+"</a></td>\n" +
                                "        <td class=\"tg-0lax\"><a>>"+lista.get(i).Opis+"</a></td>\n" +
                                "    </tr>\n" +
                                "    </tbody>");
                    }
                    str.append("</table>\n" +
                            "<a href=\""+bURL+"add\"><button>Dodaj</button></a>\n" +
                            "</body>\n" +
                            "</html>");
                return str.toString();

        }

        /** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
        // GET: knjige/dodaj
        @GetMapping(value="/add")
        @ResponseBody
        public String create() {
            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);
            StringBuilder str = new StringBuilder();
            List<Kategorija_Leka> lista = kategorije.findAll();
            str.append("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <title>Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<form action = \"/PrviMavenVebProjekat/Kategorija/add\" method = \"post\">");


            str.append("    <label for=\"fname\">Naziv:</label><br>\n" +
                    "    <input type=\"text\" id=\"fname\" value=\"\" name=\"naziv\"><br>\n" +
                    "    <label for=\"lname\">Namena:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" value=\"\" name=\"namena\">\n" +
                    "    <label for=\"lname\">Opis:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" value=\"\" name=\"opis\">\n" +
                    "    <input type=\"submit\" value=\"Submit\">");

                str.append("</form>\n" +
                        "</body>\n" +
                        "</html>");
                return str.toString();
        }

        /** obrada podataka forme za unos novog entiteta, post zahtev */
        // POST: knjige/add
        @PostMapping(value="/add")
        public void create(@RequestParam String naziv, @RequestParam String namena,
                           @RequestParam String opis, HttpServletResponse response) throws IOException {
            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);

            List<Kategorija_Leka> lista = kategorije.findAll();
            Kategorija_Leka k = new Kategorija_Leka();
            k.Naziv = naziv;
            k.Namena = namena;
            k.Opis = opis;
            kategorije.save(k);



        }

        /** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
        // POST: knjige/edit
        @PostMapping(value="/edit")
        public void edit(@ModelAttribute Kategorija_Leka kategorija_EDITED , HttpServletResponse response) throws IOException {

            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);

            List<Kategorija_Leka> lista = kategorije.findAll();

            for(int i =0;i < lista.size();i++)
            {

                if(lista.get(i).Naziv==kategorija_EDITED.Naziv)
                {
                    lista.get(i).Naziv =  kategorija_EDITED.Naziv;
                    lista.get(i).Namena = kategorija_EDITED.Namena;
                    lista.get(i).Opis = kategorija_EDITED.Opis;
                }
            }
        }

        /** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
        // POST: knjige/delete
        @PostMapping(value="/delete")
        public void delete(@RequestParam String id, HttpServletResponse response) throws IOException {
            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);

            List<Kategorija_Leka> lista = kategorije.findAll();
            for(int i =0;i < lista.size();i++)
            {
                if(lista.get(i).Naziv == id)
                {
                    kategorije.delete(lista.get(i).Naziv);
                }
            }
        }

        /** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
        // GET: knjige/details?id=1
        @GetMapping(value="/details")
        @ResponseBody
        public String details(@RequestParam String id) {
            Kategorije_Lekova kategorije = (Kategorije_Lekova) memorijaAplikacije.get(Kategorija_Controller.KATEGORIJA_KEY);
            StringBuilder str = new StringBuilder();
            List<Kategorija_Leka> lista = kategorije.findAll();
            str.append("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <title>Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<form action = \""+bURL+"add\" method = \"post\">");

            Kategorija_Leka pera = new Kategorija_Leka();
            for(int i =0;i < lista.size();i++)
            {
                if(lista.get(i).Naziv == id)
                {
                    pera = lista.get(i);
                }
            }
            str.append("    <label for=\"fname\">Naziv:</label><br>\n" +
                    "    <input type=\"text\" id=\"fname\" value=\""+pera.Naziv+"\" name=\"fname\"><br>\n" +
                    "    <label for=\"lname\">Namena:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" value=\""+pera.Namena+"\" name=\"lname\">\n" +
                    "    <label for=\"lname\">Opis:</label><br>\n" +
                    "    <input type=\"text\" id=\"lname\" value=\""+pera.Opis+"\" name=\"lname\">\n" +
                    "    <input type=\"submit\" value=\"Submit\">");

            str.append("</form>\n" +
                    "</body>\n" +
                    "</html>");
            return str.toString();
        }





        }




