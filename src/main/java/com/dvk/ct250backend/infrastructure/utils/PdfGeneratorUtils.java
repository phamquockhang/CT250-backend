package com.dvk.ct250backend.infrastructure.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfGeneratorUtils {

    public static byte[] generateTicketPdf(String bookingCode, String passengerName) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        document.add(new Paragraph("Vé Điện Tử Davika Airways"));
        document.add(new Paragraph("Mã Đặt Chỗ: " + bookingCode));
        document.add(new Paragraph("Hành Khách: " + passengerName));
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}