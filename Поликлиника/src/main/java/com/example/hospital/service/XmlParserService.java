package com.example.hospital.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Service
public class XmlParserService {

    File fileXml;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;

    @SneakyThrows
    XmlParserService(){
        factory=DocumentBuilderFactory.newInstance();
        builder=factory.newDocumentBuilder();
        fileXml=new File("");
    }


}
