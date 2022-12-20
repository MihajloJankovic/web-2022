package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.model.Kategorija_Leka;
import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.model.UserType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


@Service
@Qualifier("fajloviKategorija")
public class KategorijaServiceimpl implements KattegorijService {
    @Value("C:\\Users\\janko\\Documents\\GitHub\\sr3-2021-web-2022\\ssf\\src\\main\\resources\\kategorije.txt")
    private String pathToFile;
    private Map<Long, Kategorija_Leka> readFromFile()
    {
        Map<Long, Kategorija_Leka> kategorije = new HashMap<>();
        Long nextId = 1L;

        try {
            Path path = Paths.get(pathToFile);
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            for (String line : lines) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;

                String[] tokens = line.split(";");
                Long id = Long.parseLong(tokens[3]);
                String naziv = tokens[0];
                String namena = tokens[1];
                String opis= tokens[2];


                kategorije.put(id,new Kategorija_Leka(naziv,namena,opis,id));

                if(nextId<id)
                    nextId=id;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return kategorije;
    }
    private Map<Long, Kategorija_Leka> saveToFile(Map<Long, Kategorija_Leka> kategorija) {

        Map<Long, Kategorija_Leka> ret = new HashMap<>();

        try {
            Path path = Paths.get(pathToFile);
            System.out.println(path.toFile().getAbsolutePath());
            List<String> lines = new ArrayList<>();

            for (Kategorija_Leka k : kategorija.values()) {
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

    private Long nextId(Map<Long, Kategorija_Leka> korisnici) {
        Long nextId = 1L;
        for (Long id : korisnici.keySet()) {
            if(nextId<id)
                nextId=id;
        }
        return ++nextId;
    }

    @Override
    public Kategorija_Leka findOneById(Long id) {
        Map<Long, Kategorija_Leka> kategorije = readFromFile();
        return kategorije.get(id);
    }
    @Override
    public List<Kategorija_Leka> findAll() {
        Map<Long, Kategorija_Leka> korisnici = readFromFile();
        return new ArrayList<>(korisnici.values());
    }

    @Override
    public Kategorija_Leka save(Kategorija_Leka kategoarija) {
        Map<Long, Kategorija_Leka> ktgr = readFromFile();
        Long nextId = nextId(ktgr);

        //u slučaju da korisnik nema id
        //tada treba da se dodeli id
        if (kategoarija.getId() == null) {
            kategoarija.setId(nextId++);

        }
        ktgr.put(kategoarija.getId(), kategoarija);
        saveToFile(ktgr);
        return kategoarija;
    }

    @Override
    public Kategorija_Leka update(Kategorija_Leka korisnik) {
        Map<Long, Kategorija_Leka> kategorije= readFromFile();

        kategorije.put(korisnik.getId(), korisnik);
        saveToFile(kategorije);
        return korisnik;
    }

    @Override
    public Kategorija_Leka delete(Long id) {
        Map<Long, Kategorija_Leka> kategorije = readFromFile();

        if (!kategorije.containsKey(id)) {
            throw new IllegalArgumentException("tried to remove non existing book");
        }

        Kategorija_Leka pera = kategorije.get(id);
        if (pera != null) {
            kategorije.remove(id);
        }
        saveToFile(kategorije);
        return pera;
    }
}
