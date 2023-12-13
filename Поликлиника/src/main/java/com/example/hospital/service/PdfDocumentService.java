package com.example.hospital.service;


import com.example.hospital.model.MedicalCard;
import com.example.hospital.model.Record;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.codec.CharEncoding;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class PdfDocumentService {

    ImageServiece imageServiece;

    @Autowired
    public PdfDocumentService(ImageServiece imageServiece) {
        this.imageServiece = imageServiece;
    }

    @SneakyThrows
    public ByteArrayOutputStream writeBlankRecord(MedicalCard medicalCard){
        Document document=new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);


        document.open();
        Font fontTitle = new Font();
        fontTitle.setSize(24);


        Paragraph paragraph = new Paragraph("Заключение", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = new Font();
        fontParagraph.setSize(12);


        Paragraph paragraph2 = new Paragraph();
        Font fontAll=new Font();
        fontAll.setSize(13);

        Image image=Image.getInstance(imageServiece.getImage(medicalCard.getDoctor().getImage()).get());
        image.scaleToFit(30,30);
        Chunk chunkDoctor=new Chunk("Выдан врачом : ");
        Font font=new Font(Font.BOLD);
        font.setSize(18);
        chunkDoctor.setFont(font);
        paragraph2.add(chunkDoctor);
        paragraph2.add(medicalCard.getDoctor().getName()+" "
                + medicalCard.getDoctor().getSur_name()+" "
                + medicalCard.getDoctor().getPatronymic()+"      ");
        paragraph2.setSpacingBefore(80f);
        paragraph2.setFont(fontAll);
        paragraph2.add(new Chunk(image,0,0));

        Paragraph paragraph3=new Paragraph();
        Chunk chunk=new Chunk("Жалобы : ");
        chunk.setFont(font);
        paragraph3.setSpacingBefore(80f);
        paragraph3.add(chunk);
        paragraph3.setFont(fontAll);
        paragraph3.add(medicalCard.getDescription());


        Paragraph paragraph4=new Paragraph();
        Chunk chunk1=new Chunk("Диагноз : ");
        font.setSize(15);
        chunk1.setFont(font);
        paragraph4.setSpacingBefore(60f);
        paragraph4.add(chunk1);
        paragraph4.setFont(fontAll);
        paragraph4.add(medicalCard.getDiagnosis());

        Paragraph paragraph5=new Paragraph();
        Chunk chunk2=new Chunk("Лечение : ");
        font.setSize(15);
        chunk2.setFont(font);
        paragraph5.setSpacingBefore(60f);
        paragraph5.add(chunk2);
        paragraph5.setFont(fontAll);
        paragraph5.add(medicalCard.getAppointments());

        Date date = new Date();
        SimpleDateFormat simpl = new SimpleDateFormat("dd-MM-yyyy");
        String dat = simpl.format(date);
        Chunk chunk3=new Chunk("Дата : ");
        chunk.setFont(font);
        Paragraph paragraph1 = new Paragraph();
        paragraph1.add(chunk3);
        paragraph1.add(dat);
        paragraph1.setAlignment(Element.ALIGN_RIGHT);
        paragraph1.setSpacingBefore(60f);

        document.add(paragraph);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph1);

        document.close();

        return baos;
    }
}
