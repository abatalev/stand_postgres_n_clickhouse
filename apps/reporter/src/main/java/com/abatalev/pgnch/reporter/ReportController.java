package com.abatalev.pgnch.reporter;

import com.abatalev.pgnch.reporter.model.Action;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static net.sf.jasperreports.engine.JasperCompileManager.*;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

@RestController
public class ReportController {

    private ActionRepository repository;

    @Autowired
    public ReportController(ActionRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/report")
    public void getReport(HttpServletResponse response) {
        try {
            generateServerReport(response, "actions.xlsx", "example.jrxml", "Actions", repository.getDataSource());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void generateServerReport(HttpServletResponse response, String fileName, String reportName, String sheetName, JRDataSource dataSource) throws JRException, IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
        generateReport(response.getOutputStream(), dataSource, reportName, sheetName);
    }

    private void generateReport(OutputStream outputStream, JRDataSource dataSource, String reportName, String sheetName) throws JRException {
        JasperReport jasperReport = compileReport(getClass().getResourceAsStream("/reports/" + reportName));
        JasperPrint jasperPrint = fillReport(jasperReport, null, dataSource);

        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[]{sheetName});

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setConfiguration(reportConfig);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.exportReport();
    }
}
