package com.phellipe.workoutplanner.backend.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.phellipe.workoutplanner.backend.domain.entity.*;
import com.phellipe.workoutplanner.backend.util.QRCodeGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class WorkoutPlanPdfGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter MONTH_NAME_FORMATTER = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"));

    private static final PageSize HALF_A4_LANDSCAPE = new PageSize(595, 420);
    private static final float MARGIN = 14;
    private static final float BOTTOM_RESERVED_PAGE1 = 70;
    private static final float BOTTOM_RESERVED_PAGE2 = 130;

    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(70, 70, 70);
    private static final DeviceRgb BORDER_COLOR = new DeviceRgb(0, 0, 0);

    public byte[] generateWorkoutPlanPdf(WorkoutPlan workoutPlan, String qrCodeUrl) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);

            Document document = new Document(pdfDoc, HALF_A4_LANDSCAPE);
            document.setMargins(MARGIN, MARGIN, BOTTOM_RESERVED_PAGE1, MARGIN);

            addFrontPage(document, workoutPlan, qrCodeUrl);
            addComplementaryFields(document, 1);

            document.setMargins(MARGIN, MARGIN, BOTTOM_RESERVED_PAGE2, MARGIN);
            addBackPage(document, workoutPlan);
            addCalendars(document, 2);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }

    }

    private void addFrontPage(Document document, WorkoutPlan workoutPlan, String qrCodeUrl) {

        addHeader(document, workoutPlan);

        addBasicInfo(document, workoutPlan, qrCodeUrl);

        int workoutsOnFirstPage = Math.min(2, workoutPlan.getWorkouts().size());
        for (int i = 0; i < workoutsOnFirstPage; i++) {
            addWorkoutTable(document, workoutPlan.getWorkouts().get(i));
        }

    }

    private void addBackPage(Document document, WorkoutPlan workoutPlan) {
        if (workoutPlan.getWorkouts().size() > 2) {

            int remainingWorkouts = workoutPlan.getWorkouts().size() - 2;

            if (remainingWorkouts > 2) {
                throw new IllegalArgumentException(
                        "Ficha tem muitos treinos (" + workoutPlan.getWorkouts().size() +
                                "). Máximo permitido: 4 treinos (2 por página)."
                );
            }

            for (int i = 2; i < workoutPlan.getWorkouts().size(); i ++) {
                addWorkoutTable(document, workoutPlan.getWorkouts().get(i));
            }
        }

    }

    private void addHeader(Document document, WorkoutPlan workoutPlan) {
        Paragraph title = new Paragraph("PLANILHA DE TREINO PERSONALIZADO")
                .setFontSize(11)
                .setBold()
                .setMarginTop(0)
                .setMarginBottom(4)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
    }

    private void addBasicInfo(Document document, WorkoutPlan workoutPlan, String qrCodeUrl) {

        float[] columnWidths = {75, 12.5f, 12.5f};
        Table mainTable = new Table(UnitValue.createPercentArray(columnWidths))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(5)
                .setMarginBottom(5);

        mainTable.setBorder(new SolidBorder(BORDER_COLOR, 1));

        Table fieldsTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        Table line1 = new Table(UnitValue.createPercentArray(new float[]{5, 56, 5, 34}))
                .setWidth(UnitValue.createPercentValue(100));

        line1.addCell(createLabelCell("NOME:"));
        line1.addCell(createValueCell(workoutPlan.getMember().getName()));
        line1.addCell(createLabelCell("PROF:"));
        line1.addCell(createValueCell(workoutPlan.getProfessional().getName()));

        fieldsTable.addCell(new Cell().add(line1).setBorder(Border.NO_BORDER).setPadding(0));

        Table line2 = new Table(UnitValue.createPercentArray(new float[]{10, 20, 10, 20, 15, 25}))
                .setWidth(UnitValue.createPercentValue(100));

        line2.addCell(createLabelCell("INÍCIO:"));
        line2.addCell(createValueCell(workoutPlan.getStartDate().format(DATE_FORMATTER)));
        line2.addCell(createLabelCell("REVISÃO:"));
        line2.addCell(createValueCell(workoutPlan.getReviewDate().format(DATE_FORMATTER)));
        line2.addCell(createLabelCell("REAVALIAÇÃO:"));
        line2.addCell(createValueCell(workoutPlan.getReassessmentDate().format(DATE_FORMATTER)));

        fieldsTable.addCell(new Cell().add(line2).setBorder(Border.NO_BORDER).setPadding(0));

        Table line3 = new Table(UnitValue.createPercentArray(new float[]{13, 17, 10, 20, 30, 10}))
                .setWidth(UnitValue.createPercentValue(100));

        line3.addCell(createLabelCell("PLANILHA:"));
        line3.addCell(createValueCell(String.format("%02d", workoutPlan.getSheetNumber())));
        line3.addCell(createLabelCell("CARGA:"));
        line3.addCell(createValueCell(workoutPlan.getIntensity().toString()));
        line3.addCell(createLabelCell("TEMPO DE INTERVALO:"));
        line3.addCell(createValueCell(workoutPlan.getRestSeconds() + "\""));

        fieldsTable.addCell(new Cell().add(line3).setBorder(Border.NO_BORDER).setPadding(0));


        Cell fieldsCell = new Cell()
                .add(fieldsTable)
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        mainTable.addCell(fieldsCell);

        Cell qrCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(3);

        try {
            byte[] qrCodeBytes = QRCodeGenerator.generateQRCode(qrCodeUrl, 90, 90);
            Image qrCodeImage = new Image(ImageDataFactory.create(qrCodeBytes));
            qrCodeImage.setAutoScale(true);
            qrCell.add(qrCodeImage);
        } catch (Exception e) {
            qrCell.add(new Paragraph("[QR]").setFontSize(8));
        }

        mainTable.addCell(qrCell);

        Cell logoCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(5);

        try {
            InputStream logoStream = getClass().getResourceAsStream("/static/logo.png");
            assert logoStream != null;
            byte[] logoBytes = logoStream.readAllBytes();
            Image logoImage = new Image(ImageDataFactory.create(logoBytes));
            logoImage.setAutoScale(true);
            logoImage.setMaxHeight(115);
            logoCell.add(logoImage);
        } catch (Exception e) {
            logoCell.add(new Paragraph("[LOGO]").setFontSize(8));
        }

        mainTable.addCell(logoCell);

        document.add(mainTable);

    }

    private Cell createLabelCell(String text) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFontSize(8)
                        .setBold())
                .setBorder(Border.NO_BORDER)
                .setPadding(2)
                .setPaddingRight(3)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private Cell createValueCell(String text) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFontSize(8))
                .setBorder(Border.NO_BORDER)
                .setPadding(2)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private void addWorkoutTable(Document document, Workout workout) {

        Paragraph workoutTitle = new Paragraph("TREINO " + workout.getName())
                .setFontSize(9)
                .setBold()
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setPadding(3)
                .setMarginTop(6)
                .setMarginBottom(2)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(workoutTitle);

        float[] columnWidths = {6, 1, 1, 1, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(createHeaderCell("EXERCÍCIO"));
        table.addHeaderCell(createHeaderCell("Nº"));
        table.addHeaderCell(createHeaderCell("SETS"));
        table.addHeaderCell(createHeaderCell("REPS"));
        table.addHeaderCell(createHeaderCell("CARGAS"));

        for (WorkoutBlock block : workout.getBlocks()) {
            for (BlockItem item : block.getItems()) {

                String exerciseName = item.getExercise().getName();

                table.addCell(createDataCell(exerciseName));
                table.addCell(createDataCell(item.getMachine().getNumber() != null ? String.valueOf(item.getMachine().getNumber()) : "-"));
                table.addCell(createDataCell(String.valueOf(item.getSets())));
                table.addCell(createDataCell(String.valueOf(item.getReps())));
                table.addCell(createDataCell(""));

            }
        }

        document.add(table);

    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(7).setBold())
                .setBackgroundColor(HEADER_COLOR)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(2)
                .setBorder(new SolidBorder(BORDER_COLOR, 1));
    }

    private Cell createDataCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(7))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(2)
                .setBorder(new SolidBorder(BORDER_COLOR, 1));
    }

    private void addComplementaryFields(Document document, int pageNumber) {

        float pageWidth = HALF_A4_LANDSCAPE.getWidth();
        float usableWidth = pageWidth - (2 * MARGIN);

        Div fixedDiv = new Div();
        fixedDiv.setFixedPosition(pageNumber, MARGIN, 25, usableWidth);

        fixedDiv.add(new Paragraph(
                "EXERCÍCIOS COMPLEMENTARES: _____________________________________________________________________________________")
                .setFontSize(8)
                .setMarginBottom(2));

        fixedDiv.add(new Paragraph(
                "OBSERVAÇÃO: ______________________________________________________________________________________________________")
                .setFontSize(8));

        document.add(fixedDiv);

    }

    private void addCalendars(Document document, int pageNumber) {

        float pageWidth = HALF_A4_LANDSCAPE.getWidth();
        float usableWidth = pageWidth - (2 * MARGIN);

        Div fixedDiv = new Div();
        fixedDiv.setFixedPosition(pageNumber, MARGIN, 15, usableWidth);

        float[] columnWidths = {1, 1, 1};
        Table calendarsTable = new Table(UnitValue.createPercentArray(columnWidths))
                .setWidth(UnitValue.createPercentValue(100));

        String[] months = {
                String.format(LocalDate.now().format(MONTH_NAME_FORMATTER).toUpperCase() + " / " + LocalDate.now().getYear()),
                String.format(LocalDate.now().plusMonths(1).format(MONTH_NAME_FORMATTER).toUpperCase() + " / " + LocalDate.now().getYear()),
                String.format(LocalDate.now().plusMonths(2).format(MONTH_NAME_FORMATTER).toUpperCase() + " / " + LocalDate.now().getYear())
        };

        for (String month : months) {
            calendarsTable.addCell(createCalendar(month));
        }

        fixedDiv.add(calendarsTable);
        document.add(fixedDiv);

    }

    private Cell createCalendar(String monthName) {
        Table calendar = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        calendar.addCell(new Cell(1, 6)
                .add(new Paragraph(monthName).setFontSize(8).setBold())
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(3)
                .setBorder(new SolidBorder(BORDER_COLOR, 1)));

        String[] days = {"S", "T", "Q", "Q", "S", "S"};
        for (String day : days) {
            calendar.addCell(new Cell()
                    .add(new Paragraph(day).setFontSize(7))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(2)
                    .setBorder(new SolidBorder(BORDER_COLOR, 1)));
        }

        for (int week = 0; week < 5; week++) {
            for (int day = 0; day < 6; day++) {
                calendar.addCell(new Cell()
                        .setHeight(13)
                        .setBorder(new SolidBorder(BORDER_COLOR, 1)));
            }
        }

        return new Cell()
                .add(calendar)
                .setBorder(Border.NO_BORDER)
                .setPadding(3);

    }


}
