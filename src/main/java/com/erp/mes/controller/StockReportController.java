package com.erp.mes.controller;

import com.erp.mes.dto.StockReportDTO;
import com.erp.mes.service.StockReportService;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/stock-report")
public class StockReportController {
    private final StockReportService stockReportService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/generate")
    public String generateReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer itemId,
            Model model) {

        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);  // 기본값으로 1달 전
            startDate = cal.getTime();
        }

        if (endDate == null) {
            endDate = new Date();  // 기본값으로 현재 날짜
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (itemId != null) {
            params.put("itemId", itemId);
        }

        List<StockReportDTO> reportData = stockReportService.generateStockReport(params);
        Double totalValue = stockReportService.calculateTotalValue(params);

        model.addAttribute("reportData", reportData);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("itemId", itemId);

        return "stock/report";
    }

    @GetMapping("/reportdetail/{stkId}")
    public String viewStockDetails(@PathVariable("stkId") int stkId, Model model) {
        StockReportDTO stockDetail = stockReportService.getStockDetails(stkId);
        if (stockDetail == null) {
            model.addAttribute("error", "해당 재고 정보를 찾을 수 없습니다.");
            return "errorPage";
        }
        model.addAttribute("stock", stockDetail);
        return "stock/reportdetail";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) Integer itemId) throws IOException {

        Date parsedStartDate = null;
        Date parsedEndDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            parsedStartDate = dateFormat.parse(startDate);
            parsedEndDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
            // 날짜 파싱 실패 시 에러 처리
            return ResponseEntity.badRequest().body(new InputStreamResource(new ByteArrayInputStream("Invalid date format".getBytes())));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", parsedStartDate);
        params.put("endDate", parsedEndDate);
        if (itemId != null) {
            params.put("itemId", itemId);
        }

        List<StockReportDTO> reportData = stockReportService.generateStockReport(params);
        Double totalValue = stockReportService.calculateTotalValue(params);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Report");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("날짜");
        headerRow.createCell(1).setCellValue("제품명");
        headerRow.createCell(2).setCellValue("수량");
        headerRow.createCell(3).setCellValue("재고 위치");
        headerRow.createCell(4).setCellValue("공급 가격");
        headerRow.createCell(5).setCellValue("총 재고 금액");

        int rowNum = 1;
        for (StockReportDTO item : reportData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getDate() != null ? item.getDate().toString() : "");
            row.createCell(1).setCellValue(item.getItemName() != null ? item.getItemName() : "");
            row.createCell(2).setCellValue(item.getTotalQty()); // int는 null이 될 수 없으므로 체크 불필요
            row.createCell(3).setCellValue(item.getLocation() != null ? item.getLocation() : "");
            row.createCell(4).setCellValue(item.getUnitPrice() != null ? item.getUnitPrice().doubleValue() : 0.0);
            row.createCell(5).setCellValue(item.getTotalValue() != null ? item.getTotalValue().doubleValue() : 0.0);
        }

        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("Total");
        totalRow.createCell(5).setCellValue(totalValue != null ? totalValue : 0.0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) Integer itemId) throws IOException, ParseException, DocumentException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date parsedStartDate = dateFormat.parse(startDate);
        Date parsedEndDate = dateFormat.parse(endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", parsedStartDate);
        params.put("endDate", parsedEndDate);
        if (itemId != null) {
            params.put("itemId", itemId);
        }

        List<StockReportDTO> reportData = stockReportService.generateStockReport(params);
        Double totalValue = stockReportService.calculateTotalValue(params);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Stock Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph dateRange = new Paragraph("From: " + startDate + " To: " + endDate, dateFont);
        document.add(dateRange);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 3, 3, 3});

        Stream.of("Date", "Item Name", "Quantity", "Location", "Unit Price", "Total Value")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });

        for (StockReportDTO item : reportData) {
            table.addCell(item.getDate().toString());
            table.addCell(item.getItemName());
            table.addCell(String.valueOf(item.getTotalQty()));
            table.addCell(item.getLocation());
            table.addCell(String.valueOf(item.getUnitPrice()));
            table.addCell(String.valueOf(item.getTotalValue()));
        }


        document.add(table);


        Paragraph totalValueParagraph = new Paragraph("Total Value: " + totalValue, dateFont);
        totalValueParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalValueParagraph);

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    // 상세보기 Excel 내보내기 기능
    @GetMapping("/export/excel/{stkId}")
    public ResponseEntity<InputStreamResource> exportStockDetailsToExcel(@PathVariable("stkId") int stkId) throws IOException {
        StockReportDTO stockDetail = stockReportService.getStockDetails(stkId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Details");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("재고 ID");
        headerRow.createCell(1).setCellValue("품목명");
        headerRow.createCell(2).setCellValue("수량");
        headerRow.createCell(3).setCellValue("위치");
        headerRow.createCell(4).setCellValue("단가");
        headerRow.createCell(5).setCellValue("총 가치");

        // Populate data row
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(stockDetail.getStk_id());
        row.createCell(1).setCellValue(stockDetail.getItemName());
        row.createCell(2).setCellValue(stockDetail.getTotalQty());
        row.createCell(3).setCellValue(stockDetail.getLocation());
        row.createCell(4).setCellValue(stockDetail.getUnitPrice().doubleValue());
        row.createCell(5).setCellValue(stockDetail.getTotalValue().doubleValue());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_details.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    // 상세보기 PDF 내보내기 기능
    @GetMapping("/export/pdf/{stkId}")
    public ResponseEntity<InputStreamResource> exportStockDetailsToPdf(@PathVariable("stkId") int stkId) throws IOException, DocumentException {
        StockReportDTO stockDetail = stockReportService.getStockDetails(stkId);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();


        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Stock Detail Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);


        Font detailsFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);


        document.add(new Paragraph("Stock Information:", labelFont));
        document.add(new Paragraph("Stock ID: " + stockDetail.getStk_id(), detailsFont));
        document.add(Chunk.NEWLINE);


        document.add(new Paragraph("Item Name: " + stockDetail.getItemName(), detailsFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Quantity: " + stockDetail.getTotalQty(), detailsFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Location: " + stockDetail.getLocation(), detailsFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Unit Price: " + stockDetail.getUnitPrice() + " KRW", detailsFont));
        document.add(Chunk.NEWLINE);


        document.add(new Paragraph("Total Value: " + stockDetail.getTotalValue() + " KRW", detailsFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Date: " + stockDetail.getDate().toString(), detailsFont));
        document.add(Chunk.NEWLINE);

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_detail_report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}