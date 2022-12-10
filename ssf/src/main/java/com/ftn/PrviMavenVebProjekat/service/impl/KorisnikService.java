package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.User;

public interface KorisnikService {
	User findOneById(Long id);
	User findOne(String email);
	User findOne(String email, String sifra);
	List<User> findAll();
	User save(User korisnik);
	User update(User korisnik);
	User delete(Long id);
}
