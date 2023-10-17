package com.example.hospital.config;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Scope("prototype")
public class DateConfig {

    private Date date;

    private final  SimpleDateFormat simpl;

    DateConfig(){
        date=new Date();
        simpl=new SimpleDateFormat("dd-MM-yyyy HH:mm");
    }

    public Date nowDate() {
        date = new Date();
        return date;
    }
}
