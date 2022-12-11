package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.model.UserType;
import com.ftn.PrviMavenVebProjekat.service.impl.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.impl.KorisnikService;

@Controller
@RequestMapping(value = "/Users")
public class KorisnikController{
	
	public static final String KORISNIK_KEY = "korisnik";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private KorisnikService korisnikService;
	
	/** inicijalizacija podataka za kontroler */
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
	}



	@GetMapping(value = "/login")
	public void getLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String sifra,
			HttpSession session, HttpServletResponse response) throws IOException {
		postLogin(email, sifra, session, response);
	}

	@PostMapping(value = "/login")
	@ResponseBody
	public void postLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String sifra,
			HttpSession session, HttpServletResponse response) throws IOException {

		User korisnik = korisnikService.findOne(email, sifra);
		String greska = "";
		if (korisnik == null)
			greska = "neispravni kredencijali<br/>";

		if (!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/PrviMavenVebProjekat/\">	\r\n" + "	<title>Prijava korisnika</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + "	</ul>\r\n");
			if (!greska.equals(""))
				retVal.append("	<div>" + greska + "</div>\r\n");
			retVal.append("	<form method=\"post\" action=\"korisnici/login\">\r\n" + "		<table>\r\n"
					+ "			<caption>Prijava korisnika na sistem</caption>\r\n"
					+ "			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n"
					+ "			<tr><th>Šifra:</th><td><input type=\"password\" value=\"\" name=\"sifra\" required/></td></tr>\r\n"
					+ "			<tr><th></th><td><input type=\"submit\" value=\"Prijavi se\" /></td>\r\n"
					+ "		</table>\r\n" + "	</form>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		if (session.getAttribute(KORISNIK_KEY) != null)
			greska = "korisnik je već prijavljen na sistem morate se prethodno odjaviti<br/>";

		if (!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/PrviiMavenVebProjekat/\">	\r\n" + "	<title>Prijava korisnika</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + "	</ul>\r\n");
			if (!greska.equals(""))
				retVal.append("	<div>" + greska + "</div>\r\n");
			retVal.append("	<a href=\"index.html\">Povratak</a>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		session.setAttribute(KORISNIK_KEY, korisnik);

		response.sendRedirect(bURL + "knjige");
	}
	
	@GetMapping(value="/logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

		User korisnik = (User) request.getSession().getAttribute(KORISNIK_KEY);
		String greska = "";
		if(korisnik==null)
			greska="korisnik nije prijavljen<br/>";
		
		if(!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;	
			out = response.getWriter();
			
			StringBuilder retVal = new StringBuilder();
			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" +
					"	<meta charset=\"UTF-8\">\r\n" + 
					"	<base href=\"/PrviMavenVebProjekat/\">	\r\n" + 
					"	<title>Prijava korisnika</title>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + 
					"	</ul>\r\n");
			if(!greska.equals(""))
				retVal.append(
					"	<div>"+greska+"</div>\r\n");
			retVal.append(
					"	<form method=\"post\" action=\"PrijavaOdjava/Login\">\r\n" + 
					"		<table>\r\n" + 
					"			<caption>Prijava korisnika na sistem</caption>\r\n" + 
					"			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n" + 
					"			<tr><th>Šifra:</th><td><input type=\"password\" value=\"\" name=\"sifra\" required/></td></tr>\r\n" + 
					"			<tr><th></th><td><input type=\"submit\" value=\"Prijavi se\" /></td>\r\n" + 
					"		</table>\r\n" + 
					"	</form>\r\n" + 
					"	<br/>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"Users/logout\">Odjavi se</a></li>\r\n" +
					"	</ul>" +
					"</body>\r\n" + 
					"</html>");
			
			out.write(retVal.toString());
			return;
		}
		
		
		request.getSession().removeAttribute(KORISNIK_KEY);
		request.getSession().invalidate();
		response.sendRedirect(bURL+"Users/login");
	}
	
	@PostMapping(value = "/registracijaa")
	public void registracija(@RequestParam(required = true) String email, @RequestParam(required = true) String sifra,
							 @RequestParam(required = true) String ime,@RequestParam(required = true) String ulica,@RequestParam(required = true) String broj, @RequestParam(required = true) String prezime, @RequestParam(required = true) String UserName,
							 @RequestParam(required = true) String dateOfBirth,
							 HttpSession session, HttpServletResponse response) throws IOException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime tr = LocalDateTime.now();
		String formatDateTime = tr.format(formatter);
		LocalDateTime vr =LocalDateTime.parse(formatDateTime, formatter) ;
		String userType = "CUSTOMER";

		LocalDate dt = LocalDate.parse(dateOfBirth);

		User korisnik = new User(ime, prezime, email, sifra,UserName,vr, UserType.valueOf(userType),dt,ulica,broj);
		korisnikService.save(korisnik);
		
		response.sendRedirect(bURL + "Users");
	}
	@GetMapping(value = "/registracija")
	@ResponseBody
	public String registracija(HttpSession session, HttpServletResponse response) throws IOException {

		StringBuilder retVal = new StringBuilder();
		retVal.append("<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <title>Title</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"<form action=\""+bURL+"Users/registracijaa\" method=\"post\">\n" +
				"    <label for=\"1\"  >First name:</label><br>\n" +
				"    <input type=\"text\" id=\"1\" name=\"ime\" ><br>\n" +
				"    <label for=\"2\">Last name:</label><br>\n" +
				"    <input type=\"text\" id=\"2\" name=\"prezime\" ><br>\n" +
				"    <label  for=\"3\">Username:</label><br>\n" +
				"    <input type=\"text\" id=\"3\" name=\"UserName\" ><br>\n" +
				"    <label for=\"4\" >Address Streett:</label><br>\n" +
				"    <input type=\"text\" id=\"4\" name=\"ulica\" ><br>\n" +
				"    <label  for=\"5\">Address Number:</label><br>\n" +
				"    <input type=\"text\" id=\"5\" name=\"broj\" ><br>\n" +
				"    <label for=\"6\">Email:</label><br>\n" +
				"    <input type=\"email\" id=\"6\" name=\"email\" ><br>\n" +
				"    <label 7 >Password:</label><br>\n" +
				"    <input type=\"password\" id=\"7\" minlength=\"4\" name=\"sifra\" ><br>\n" +
				"    <label for=\"8\">Date of Birth:</label><br>\n" +
				"  <input type=\"date\" id=\"8\" name=\"dateOfBirth\" ><br>\n" +
				"    <input type=\"submit\" value=\"Submit\">\n" +
				"</form>\n" +
				"</body>\n" +
				"</html>");
		return retVal.toString();
	}



	
	@GetMapping
	@ResponseBody
	public String getKorisnici(HttpSession session, HttpServletResponse response) throws IOException {	
		StringBuilder retVal = new StringBuilder();

		List<User> korisnici = korisnikService.findAll();


			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"	<meta charset=\"UTF-8\">\r\n" + 
		    		"	<base href=\""+bURL+"\">" + 
					"	<title>Clanske karte</title>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"+
					"</head>\r\n" + 
					"<body> "+
					"	<ul>\r\n" +
					"		<li><a href=\"login\">Knjige</a></li>\r\n" +
					"		<li><a href=\"clanskeKarte\">Clanske karte</a></li>\r\n" +
					"		<li><a href=\"korisnici\">Korisnici</a></li>\r\n" + 
					"	</ul>\r\n" + 
					"		<table>\r\n" + 
					"			<caption>Korisnici</caption>\r\n" + 
					"			<tr>\r\n" + 
					"				<th>Ime</th>\r\n" +
					"				<th>Prezime</th>\r\n" + 
					"				<th>Email</th>\r\n" +
					"				<th></th>\r\n" +
					"			</tr>\r\n");
			
			for (User k: korisnici) {
				retVal.append(
					"			<tr>\r\n" + 
					"				<td>"+ k.getFirstName() +"</td>\r\n" +
					"				<td>"+ k.getLastName() +"</td>\r\n" +
					"				<td>"+ k.getEmail() +"</td>\r\n" +
					"				<td>" +
									"	<form method=\"post\" action=\"korisnici/obrisi\">\r\n" +
									"		<input type=\"hidden\" name=\"id\" value=\""+k.getId()+"\">\r\n" +
									"		<input type=\"submit\" value=\"Obrisi\">\r\n" + 
									"	</form>\r\n" +
					"				</td>\r\n" +
					"			</tr>\r\n");
			}
			retVal.append("</table>\r\n");
			
			retVal.append(
					"</body>\r\n"+
					"</html>\r\n");		

		
		return retVal.toString();
	}
	
	@PostMapping(value="/obrisi")
	public void obrisiKorisnika(@RequestParam Long id, HttpServletResponse response) throws IOException {				
		korisnikService.delete(id);

		response.sendRedirect(bURL+"korisnici");
	}

}
