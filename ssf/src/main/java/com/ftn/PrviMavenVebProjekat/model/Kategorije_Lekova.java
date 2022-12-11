package com.ftn.PrviMavenVebProjekat.model;

import com.ftn.PrviMavenVebProjekat.controller.Kategorija_Controller;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kategorije_Lekova {
    private Map<Kategorija_Leka,Kategorija_Leka> Kategorije_Lekova = new HashMap<>();



    public Kategorije_Lekova() {

        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("kategorije.txt").toURI());
            System.out.println(path.toFile().getAbsolutePath());
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            for (String line : lines) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;

                String[] tokens = line.split(";");
                String Naz =   tokens[0];
                String nam =   tokens[1];
                String opis =   tokens[2];
                Kategorija_Leka pera = new Kategorija_Leka();
                pera.Naziv = Naz;
                pera.Opis = opis;
                pera.Namena = nam;
                Kategorije_Lekova.put(pera,pera);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** vraca knjigu u odnosu na zadati id */
    public Kategorija_Leka findOne(String id) {
        return Kategorije_Lekova.get(id);
    }

    /** vraca sve knjige */
    public List<Kategorija_Leka> findAll() {
        return new ArrayList<Kategorija_Leka>(Kategorije_Lekova.values());
    }

    /** cuva podatke o knjizi */
    public Kategorija_Leka save(Kategorija_Leka kategorija) {


        Kategorije_Lekova.put(kategorija,kategorija);
        return kategorija;
    }

    /** cuva podatke o svim knjigama */
    public List<Kategorija_Leka> save(List<Kategorija_Leka> Kategorije_Lekova) {
        List<Kategorija_Leka> ret = new ArrayList<>();

        for (Kategorija_Leka k : Kategorije_Lekova) {
            // za svaku prosleđenu knjigu pozivamo save
            // metodu koju smo već napravili i testirali -
            // ona će sepobrinuti i za dodelu ID-eva
            // ako je to potrebno
            Kategorija_Leka saved = save(k);

            // uspešno snimljene dodajemo u listu za vraćanje
            if (saved != null) {
                ret.add(saved);
            }
        }
        return ret;
    }

    /** brise knjigu u odnosu na zadati id */
    public Kategorija_Leka delete(String id) {
        if (!Kategorije_Lekova.containsKey(id)) {
            throw new IllegalArgumentException("tried to remove non existing book");
        }
        Kategorija_Leka kategorija = Kategorije_Lekova.get(id);
        if (kategorija != null) {
            Kategorije_Lekova.remove(id);
        }
        return kategorija;
    }

//    /** brise knjige u odnosu na zadate ids */
//    public void delete(List<Long> ids) {
//        for (Long id : ids) {
//            // pozivamo postojeću metodu za svaki
//            delete(id);
//        }
//    }

//    /** vraca sve knjige ciji naziv zapocinje sa tekstom*/
//    public List<Knjiga> findByNaziv(String naziv) {
//        List<Knjiga> ret = new ArrayList<>();
//
//        for (Knjiga k : knjige.values()) {
//            if (naziv.startsWith(k.getNaziv())) {
//                ret.add(k);
//            }
//        }
//
//        return ret;
//    }
}
