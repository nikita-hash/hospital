package com.example.hospital.service;

import jakarta.annotation.PostConstruct;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    private XWPFDocument document;

    private XWPFParagraph paragraph;

    private XWPFRun run;

    ReportService(){
        this.document=new XWPFDocument();
        this.paragraph=document.createParagraph();
        this.run=paragraph.createRun();
    }

    @PostConstruct
    void init(){

    }
}
