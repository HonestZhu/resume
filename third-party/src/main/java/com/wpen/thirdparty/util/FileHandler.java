package com.wpen.thirdparty.util;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.mock.web.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileHandler {
    public static String convertFileToBase64(MultipartFile file) throws IOException, IllegalArgumentException {



        String type = file.getContentType();
        if (type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            file = docxToPdf(file);
        }
        byte[] fileContent = file.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (type.equals("application/pdf") || type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            // convert PDF file to image
            PDDocument document = PDDocument.load(fileContent);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0);
            document.close();
            // encode image to base64
            ImageIO.write(image, "png", baos);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + type);
        }

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



    public static MultipartFile docxToPdf(MultipartFile docxFile) throws IOException {
        // 从MultipartFile中读取docx文件
        XWPFDocument document = new XWPFDocument(docxFile.getInputStream());

        // 为转换创建PdfOptions
        PdfOptions options = PdfOptions.create().fontEncoding("UTF-8");

        // 将docx文件转换为pdf
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfConverter.getInstance().convert(document, pdfOutputStream, options);

        // 创建一个新的MultipartFile并将pdf输出流写入其中
        String pdfFilename = docxFile.getOriginalFilename().replace(".docx", ".pdf");
        MultipartFile pdfFile = new ByteArrayMultipartFile(pdfOutputStream.toByteArray(), pdfFilename, "applicationf");

        return pdfFile;
    }

}