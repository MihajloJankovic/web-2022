package com.ftn.PrviMavenVebProjekat.service.impl;

import com.ftn.PrviMavenVebProjekat.model.Kategorija_Leka;
import com.ftn.PrviMavenVebProjekat.model.User;

import java.util.List;

public interface KattegorijService {
    Kategorija_Leka findOneById(Long id);
    List<Kategorija_Leka> findAll();
    Kategorija_Leka save(Kategorija_Leka korisnik);
    Kategorija_Leka update(Kategorija_Leka korisnik);
    Kategorija_Leka delete(Long id);
}
