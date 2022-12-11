package com.ftn.PrviMavenVebProjekat.model;

public class Kategorija_Leka {
    public String Naziv;
    public String Namena;
    public String Opis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public void setNamena(String namena) {
        Namena = namena;
    }

    public void setOpis(String opis) {
        Opis = opis;
    }

    public String getNaziv() {
        return Naziv;
    }

    public String getNamena() {
        return Namena;
    }

    public String getOpis() {
        return Opis;
    }

    public Kategorija_Leka(String naziv, String namena, String opis)
    {
        this.Naziv = naziv;
        this.Namena = namena;
        this.Opis = opis;
    }
    public Kategorija_Leka(String naziv, String namena, String opis,Long Id)
    {
        this.Naziv = naziv;
        this.Namena = namena;
        this.Opis = opis;
        id = Id;
    }
    public Kategorija_Leka()
    {

    }
    public String toFileString() {
        return this.getNaziv() + ";" +this.getNamena() + ";" +this.getOpis() + ";" +this.getId() ;
    }


}
