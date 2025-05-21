/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import Model.Entities.CitaMedica;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class GeneradorHistoriaClinica {

    public static void generarPDF(CitaMedica cita, File archivoDestino) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivoDestino));
            document.open();

            // Fuentes
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Font recetaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK);

            // Encabezado
            Paragraph titulo = new Paragraph("HISTORIA CLÍNICA PENITENCIARIA", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // Línea divisoria
            document.add(new Chunk(new LineSeparator()));
            document.add(Chunk.NEWLINE);

            // Sección de datos generales
            PdfPTable tableGeneral = new PdfPTable(2);
            tableGeneral.setWidthPercentage(100);
            tableGeneral.setSpacingBefore(10);
            tableGeneral.setSpacingAfter(10);

            addTableHeader(tableGeneral, "DATOS GENERALES", headerFont, 2);
            addTableCell(tableGeneral, "Fecha de atención:", cita.getFechaHoraAtencion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), labelFont, valueFont);
            addTableCell(tableGeneral, "Motivo de consulta:", cita.getMotivo(), labelFont, valueFont);

            document.add(tableGeneral);

            // Sección de datos del paciente con la foto al lado
            PdfPTable tablePaciente = new PdfPTable(new float[]{2, 1});
            tablePaciente.setWidthPercentage(100);
            tablePaciente.setSpacingBefore(10);
            tablePaciente.setSpacingAfter(10);

            // Subtabla para datos del paciente
            PdfPTable datosPaciente = new PdfPTable(2);
            datosPaciente.setWidthPercentage(100);

            addTableHeader(datosPaciente, "DATOS DEL PACIENTE", headerFont, 2);
            addTableCell(datosPaciente, "Identificación:", cita.getPreso().getIdentificacion(), labelFont, valueFont);
            addTableCell(datosPaciente, "Nombre completo:", cita.getPreso().getNombreCompleto(), labelFont, valueFont);
            addTableCell(datosPaciente, "Edad:", String.valueOf(cita.getPreso().getEdad()), labelFont, valueFont);
            addTableCell(datosPaciente, "Grupo sanguíneo:", cita.getPreso().getGrupoSanguineo(), labelFont, valueFont);
            addTableCell(datosPaciente, "Peso (kg):", String.valueOf(cita.getPreso().getPeso()), labelFont, valueFont);
            addTableCell(datosPaciente, "Estatura (m):", String.valueOf(cita.getPreso().getEstatura()), labelFont, valueFont);

            PdfPCell datosCell = new PdfPCell(datosPaciente);
            datosCell.setBorder(Rectangle.NO_BORDER);
            tablePaciente.addCell(datosCell);

            // Foto del paciente
            PdfPCell fotoCell = new PdfPCell();
            fotoCell.setBorder(Rectangle.BOX);
            fotoCell.setBorderWidth(1f);

            fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (cita.getPreso().getFotoPath() != null && !cita.getPreso().getFotoPath().isEmpty()) {
                try {
                    Image fotoPreso = Image.getInstance(cita.getPreso().getFotoPath());
                    fotoPreso.scaleToFit(100, 100);
                    fotoPreso.setAlignment(Image.ALIGN_CENTER);
                    fotoCell.addElement(fotoPreso);

                    Paragraph caption = new Paragraph("ID: " + cita.getPreso().getIdentificacion(),
                            new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
                    caption.setAlignment(Element.ALIGN_CENTER);
                    fotoCell.addElement(caption);
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen del preso: " + e.getMessage());
                    fotoCell.addElement(new Phrase("Foto no disponible", new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY)));
                }
            } else {
                fotoCell.addElement(new Phrase("Sin foto", new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY)));
            }

            tablePaciente.addCell(fotoCell);
            document.add(tablePaciente);

            // Sección de personal involucrado
            PdfPTable tablePersonal = new PdfPTable(2);
            tablePersonal.setWidthPercentage(100);
            tablePersonal.setSpacingBefore(10);
            tablePersonal.setSpacingAfter(10);

            addTableHeader(tablePersonal, "PERSONAL INVOLUCRADO", headerFont, 2);
            addTableCell(tablePersonal, "Guardia acompañante:", cita.getGuardia().getNombreCompleto() + " (" + cita.getGuardia().getIdentificacion() + ")", labelFont, valueFont);
            addTableCell(tablePersonal, "Enfermera/o atendió:", cita.getEnfermera().getNombreCompleto() + " (" + cita.getEnfermera().getIdentificacion() + ")", labelFont, valueFont);

            document.add(tablePersonal);

            // Diagnóstico
            PdfPTable tableDiagnostico = new PdfPTable(1);
            tableDiagnostico.setWidthPercentage(100);
            tableDiagnostico.setSpacingBefore(10);

            addTableHeader(tableDiagnostico, "DIAGNÓSTICO", headerFont, 1);
            document.add(tableDiagnostico);

            Paragraph diagnostico = new Paragraph(cita.getDiagnostico(), valueFont);
            diagnostico.setSpacingAfter(10);
            document.add(diagnostico);

            // Receta médica
            if (cita.getReceta() != null && !cita.getReceta().isEmpty()) {
                PdfPTable tableReceta = new PdfPTable(1);
                tableReceta.setWidthPercentage(100);
                tableReceta.setSpacingBefore(10);

                addTableHeader(tableReceta, "RECETA MÉDICA", headerFont, 1);
                document.add(tableReceta);

                Paragraph receta = new Paragraph(cita.getReceta(), recetaFont);
                receta.setSpacingAfter(10);
                document.add(receta);
            }

            // Pie de página
            Paragraph footer = new Paragraph(
                    "Documento generado automáticamente por el Sistema Penitenciario\n"
                    + "Fecha de generación: "
                    + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    private static void addTableHeader(PdfPTable table, String title, Font font, int colspan) {
        PdfPCell header = new PdfPCell(new Phrase(title, font));
        header.setBackgroundColor(new BaseColor(0, 51, 102)); // Color azul oscuro
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setColspan(colspan);
        table.addCell(header);
    }

    private static void addTableCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(new BaseColor(220, 220, 220)); // Gris claro
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        table.addCell(valueCell);
    }
}
