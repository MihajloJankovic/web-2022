package com.ftn.Pharmacy.Project.bean;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class SecondConfiguration implements WebMvcConfigurer {
    @Bean(name= {"messageSource"})
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        //postavljanje diretorijuma koji sadrži poruke/prefiks naziva property datoteke
        source.setBasenames("messages");
        //ukoliko se ne postoji poruka za kluč ispiši samo ključ
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        //postavljanje default lokalizacije na nivou aplikacije
//        source.setDefaultLocale(Locale.forLanguageTag("sr"));
        source.setDefaultLocale(Locale.US);
        return source;
    }

    //LocaleResolver određuju lokalizaciju na osnovu podataka iz HTTP zahteva
//SessionLocaleResolver - određuje lokalizaciju i skladišti je u HttpSession klijenta
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //postavljanje default lokalizacije
        slr.setDefaultLocale(Locale.forLanguageTag("en"));
        return slr;
    }

    //Interceptor that allows for changing the current locale on every request,
//via a configurable request parameter (default parameter name: "locale").
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("locale");
        return lci;
    }

//Restracija presretača se postiže dodavanjem presretača u InterceptorRegistry
//implementacijom WebMvcConfigurer interfejs

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}

