package com.dvk.ct250backend.infrastructure.utils;

import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfGeneratorUtils {

    public static byte[] generateTicketPdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            FontFactory.register("src/main/resources/static/fonts/Arial.ttf", "Arial");
            renderer.getFontResolver().addFont("src/main/resources/static/fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Failed to generate PDF", e);
        }
    }
}