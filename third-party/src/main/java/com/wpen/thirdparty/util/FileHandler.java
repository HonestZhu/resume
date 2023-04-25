package com.wpen.thirdparty.util;

import com.wpen.thirdparty.constant.FileType;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FileHandler {
    public static String convertFileToBase64(MultipartFile file) throws IOException, IllegalArgumentException {

        String type = file.getContentType();
        if(!type.equals(FileType.DOCX) && !type.equals(FileType.DOC) && !type.equals(FileType.PDF)) {
            throw new IllegalArgumentException("Unsupported file type: " + type);
        }
        byte[] fileContent;
        if (type.equals(FileType.DOCX) || type.equals(FileType.DOC)) {
            // convert Word document to PDF
            InputStream is = file.getInputStream();
            XWPFDocument document = new XWPFDocument(is);

            PdfOptions options = PdfOptions.create();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(document, out, options);

            System.out.println(out.size());
            fileContent = out.toByteArray();
            System.out.println(fileContent.length);
        } else {
            fileContent = file.getBytes();
            System.out.println(fileContent.length);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // convert PDF file to image
        PDDocument document = PDDocument.load(fileContent);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImage(0);
        document.close();
        // encode image to base64
        ImageIO.write(image, "png", baos);


        // get Base64-encoded image data
        byte[] imageData = baos.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(imageData);
        return base64String;
    }

    public static String convertPictrueToBase64(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        return base64String;
    }

}