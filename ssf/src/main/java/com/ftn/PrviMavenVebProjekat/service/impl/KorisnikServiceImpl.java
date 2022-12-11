package com.ftn.PrviMavenVebProjekat.service.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.model.UserType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;


import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.impl.KorisnikService;

@Service
@Qualifier("fajloviKorisnici")
public class KorisnikServiceImpl implements KorisnikService {

	@Value("${korisnici.pathToFile}")
	private String pathToFile;
	
	private Map<Long, User> readFromFile() {

    	Map<Long, User> korisnici = new HashMap<>();
    	Long nextId = 1L;
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				String ime = tokens[1];
				String prezime = tokens[2];
				String email = tokens[3];
				String lozinka = tokens[4];
				String UserName = tokens[5];
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
				LocalDateTime creation =LocalDateTime.parse(tokens[6], formatter) ;
				UserType tip =UserType.valueOf(tokens[7]);
				String Str = tokens[8];
				String Strnum = tokens[9];


				LocalDate dateofBirth = LocalDate.parse(tokens[8]);


				korisnici.put(Long.parseLong(tokens[0]), new User(id, ime, prezime, email, lozinka,UserName,creation,tip,dateofBirth,Str,Strnum));

				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return korisnici;
    }
    
    private Map<Long, User> saveToFile(Map<Long, User> korisnici) {
    	
    	Map<Long, User> ret = new HashMap<>();
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();
			
			for (User k : korisnici.values()) {
				lines.add(k.toFileString());
				ret.put(k.getId(), k);
			}
			//pisanje svih redova za clanske karte
			Files.write(path, lines, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ret;
    }
    
    private Long nextId(Map<Long, User> korisnici) {
    	Long nextId = 1L;
    	for (Long id : korisnici.keySet()) {
    		if(nextId<id)
				nextId=id;
		}
    	return ++nextId;
    }
	
	@Override
	public User findOneById(Long id) {
		Map<Long, User> korisnici = readFromFile();
		return korisnici.get(id);
	}

	@Override
	public User findOne(String email) {
		Map<Long, User> korisnici = readFromFile();
		User found = null;
		for (User korisnik : korisnici.values()) {
			if (korisnik.getEmail().equals(email)) {
				found = korisnik;
				break;
			}
		}
		
		return found;
	}

	@Override
	public User findOne(String email, String sifra) {
		Map<Long, User> korisnici = readFromFile();
		User found = null;
		for (User korisnik : korisnici.values()) {
			if (korisnik.getEmail().equals(email) && korisnik.getPassword().equals(sifra)) {
				found = korisnik;
				break;
			}
		}
		
		return found;
	}

	@Override
	public List<User> findAll() {
		Map<Long, User> korisnici = readFromFile();
		return new ArrayList<>(korisnici.values());
	}

	@Override
	public User save(User korisnik) {
		Map<Long, User> korisnici = readFromFile();
		Long nextId = nextId(korisnici); 
		
		//u slučaju da korisnik nema id
		//tada treba da se dodeli id
		if (korisnik.getId() == null) {
			korisnik.setId(nextId++);
			
		}
		korisnici.put(korisnik.getId(), korisnik);
		saveToFile(korisnici);
		return korisnik;
	}

	@Override
	public User update(User korisnik) {
		Map<Long, User> korisnici = readFromFile();
		
		korisnici.put(korisnik.getId(), korisnik);
		saveToFile(korisnici);
		return korisnik;
	}

	@Override
	public User delete(Long id) {
		Map<Long, User> korisnici = readFromFile();
		
		if (!korisnici.containsKey(id)) {
			throw new IllegalArgumentException("tried to remove non existing book");
		}
		
		User korisnik = korisnici.get(id);
		if (korisnik != null) {
			korisnici.remove(id);
		}
		saveToFile(korisnici);
		return korisnik;
	}

}
