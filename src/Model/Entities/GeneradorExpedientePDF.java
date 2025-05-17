package Model.Entities;

import Model.Constants.EstadoPresoEnum;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorExpedientePDF {

    private static final BaseColor COLOR_PRIMARIO = new BaseColor(26, 54, 93); 
    private static final BaseColor COLOR_SECUNDARIO = new BaseColor(245, 245, 245); 
    private static final BaseColor COLOR_ALTO_RIESGO = new BaseColor(139, 0, 0); 
    private static final BaseColor COLOR_EXITO = new BaseColor(0, 100, 0); 
    private static final BaseColor COLOR_FONDO_FOTO = new BaseColor(240, 240, 240);

    public void generarPDFExpediente(Preso preso, ExpedienteJudicial expediente,
                                   List<Delito> delitos, Sentencia sentenciaTotal,
                                   String rutaDestino) throws Exception {

        Document document = new Document(PageSize.A4, 30, 30, 70, 30);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rutaDestino));

        writer.setPageEvent(new PdfPageEventHelper() {
            public void onEndPage(PdfWriter writer, Document document) {
                try {
                    PdfPTable header = new PdfPTable(1);
                    header.setTotalWidth(document.getPageSize().getWidth() - 60);
                    header.setLockedWidth(true);

                    PdfPCell cell = new PdfPCell(new Phrase("SISTEMA PENITENCIARIO NACIONAL",
                            new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE)));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(COLOR_PRIMARIO);
                    cell.setPadding(12);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.addCell(cell);

                    header.writeSelectedRows(0, -1, 30, document.getPageSize().getHeight() - 20, writer.getDirectContent());

                    PdfPTable footer = new PdfPTable(1);
                    footer.setTotalWidth(document.getPageSize().getWidth() - 60);
                    footer.setLockedWidth(true);

                    cell = new PdfPCell(new Phrase("Documento confidencial - Generado el " +
                            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                            " | Página " + writer.getPageNumber(),
                            new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.DARK_GRAY)));
                    cell.setBorder(Rectangle.TOP);
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setBorderWidth(0.5f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    footer.addCell(cell);

                    footer.writeSelectedRows(0, -1, 30, 30, writer.getDirectContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        document.open();

        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, COLOR_PRIMARIO);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_PRIMARIO);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font fontResaltado = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font fontEtiqueta = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.DARK_GRAY);

        Paragraph titulo = new Paragraph("EXPEDIENTE PENITENCIARIO", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);
        
        Paragraph linea = new Paragraph();
        linea.add(new Chunk(new LineSeparator(0.5f, 100, COLOR_PRIMARIO, Element.ALIGN_CENTER, -1)));
        linea.setSpacingAfter(20);
        
        document.add(titulo);
        document.add(linea);

        PdfPTable datosPersonales = new PdfPTable(2);
        datosPersonales.setWidthPercentage(100);
        datosPersonales.setWidths(new float[]{70, 30});
        datosPersonales.setSpacingBefore(10);

        PdfPTable datosColumna = new PdfPTable(2);
        datosColumna.setWidthPercentage(100);
        datosColumna.setSpacingBefore(5);

        agregarCeldaTituloModerno(datosColumna, "DATOS PERSONALES", fontSubtitulo, 2);
        
        agregarCeldaDatosModerno(datosColumna, "Nombre completo:", preso.getNombresCompletos() + " " + preso.getApellidosCompletos(), 
                               fontEtiqueta, fontNormal);
        agregarCeldaDatosModerno(datosColumna, "Identificación:", preso.getIdentificacion(), 
                               fontEtiqueta, fontNormal);
        agregarCeldaDatosModerno(datosColumna, "Edad/Nacionalidad:", preso.getEdad() + " años / " + preso.getNacionalidad(), 
                               fontEtiqueta, fontNormal);
        
        String estado = preso.getEstado().name();
        BaseColor colorEstado = preso.getEstado() == EstadoPresoEnum.FALLECIDO ? BaseColor.RED :
                               preso.getEstado() == EstadoPresoEnum.LIBERADO ? COLOR_EXITO : 
                               COLOR_PRIMARIO;
                               
        Phrase estadoPhrase = new Phrase(estado, new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE));
        PdfPCell estadoCell = new PdfPCell(estadoPhrase);
        estadoCell.setBackgroundColor(colorEstado);
        estadoCell.setBorder(Rectangle.NO_BORDER);
        estadoCell.setPadding(5);
        estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        PdfPCell etiquetaCell = new PdfPCell(new Phrase("Estado:", fontEtiqueta));
        etiquetaCell.setBorder(Rectangle.NO_BORDER);
        
        datosColumna.addCell(etiquetaCell);
        datosColumna.addCell(estadoCell);

        PdfPCell datosCell = new PdfPCell(datosColumna);
        datosCell.setBorder(Rectangle.BOX);
        datosCell.setBorderWidth(0.5f);
        datosCell.setBorderColor(BaseColor.LIGHT_GRAY);
        datosCell.setPadding(10);
        datosCell.setBackgroundColor(COLOR_SECUNDARIO);
        datosPersonales.addCell(datosCell);

        PdfPCell fotoCell;
        if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
            try {
                PdfPTable fotoContainer = new PdfPTable(1);
                fotoContainer.setWidthPercentage(100);
                
                PdfPCell shadowCell = new PdfPCell();
                shadowCell.setBackgroundColor(COLOR_FONDO_FOTO);
                shadowCell.setBorder(Rectangle.BOX);
                shadowCell.setBorderWidth(1);
                shadowCell.setBorderColor(BaseColor.LIGHT_GRAY);
                shadowCell.setPadding(5);
                
                Image foto = Image.getInstance(preso.getFotoPath());
                foto.scaleToFit(120, 150);
                foto.setAlignment(Element.ALIGN_CENTER);
                
                PdfContentByte canvas = writer.getDirectContentUnder();
                float x = document.right() - 150; 
                float y = document.top() - 228;    
                
                canvas.setColorFill(BaseColor.LIGHT_GRAY);
                canvas.roundRectangle(x+2, y-2, 124, 154, 5);
                canvas.fill();
                
                canvas.setColorFill(BaseColor.WHITE);
                canvas.roundRectangle(x, y, 120, 150, 5);
                canvas.fill();
                
                foto.setAbsolutePosition(x, y);
                document.add(foto);
                
                
                fotoCell = new PdfPCell();
                fotoCell.setBorder(Rectangle.NO_BORDER);
                
            } catch (Exception e) {
                fotoCell = crearCeldaFotoPlaceholder("Error al cargar la foto", fontNormal);
            }
        } else {
            fotoCell = crearCeldaFotoPlaceholder("Foto no disponible", fontNormal);
        }
        
        fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        fotoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        datosPersonales.addCell(fotoCell);

        document.add(datosPersonales);
        document.add(Chunk.NEWLINE);

        PdfPTable tablaExpediente = new PdfPTable(2);
        tablaExpediente.setWidthPercentage(100);
        tablaExpediente.setSpacingBefore(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        agregarCeldaTituloModerno(tablaExpediente, "DATOS DEL EXPEDIENTE", fontSubtitulo, 2);
        
        agregarCeldaDatosModerno(tablaExpediente, "Número de registro:", expediente.getNumeroRegistro(), 
                               fontEtiqueta, fontNormal);
        agregarCeldaDatosModerno(tablaExpediente, "Código de expediente:", expediente.getCodigoExpediente(), 
                               fontEtiqueta, fontNormal);
        agregarCeldaDatosModerno(tablaExpediente, "Fecha de apertura:", expediente.getFechaApertura().format(formatter), 
                               fontEtiqueta, fontNormal);
        agregarCeldaDatosModerno(tablaExpediente, "Juzgado:", expediente.getJuzgado(), 
                               fontEtiqueta, fontNormal);
                               
        Phrase riesgoPhrase = new Phrase(expediente.getNivelRiesgo(), 
            new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, 
                   expediente.getNivelRiesgo().equalsIgnoreCase("Alto") ? COLOR_ALTO_RIESGO : BaseColor.BLACK));
        PdfPCell riesgoLabel = new PdfPCell(new Phrase("Nivel de riesgo:", fontEtiqueta));
        riesgoLabel.setBorder(Rectangle.NO_BORDER);
        PdfPCell riesgoValue = new PdfPCell(riesgoPhrase);
        riesgoValue.setBorder(Rectangle.NO_BORDER);
        tablaExpediente.addCell(riesgoLabel);
        tablaExpediente.addCell(riesgoValue);
        
        agregarCeldaDatosModerno(tablaExpediente, "Estado del expediente:", expediente.getEstado().toString(), 
                               fontEtiqueta, fontNormal);

        PdfPCell expedienteContainer = new PdfPCell(tablaExpediente);
        expedienteContainer.setBorder(Rectangle.BOX);
        expedienteContainer.setBorderWidth(0.5f);
        expedienteContainer.setBorderColor(BaseColor.LIGHT_GRAY);
        expedienteContainer.setPadding(10);
        expedienteContainer.setBackgroundColor(COLOR_SECUNDARIO);
        
        PdfPTable expedienteWrapper = new PdfPTable(1);
        expedienteWrapper.setWidthPercentage(100);
        expedienteWrapper.addCell(expedienteContainer);

        document.add(expedienteWrapper);
        document.add(Chunk.NEWLINE);

        PdfPTable tablaSentencia = new PdfPTable(1);
        tablaSentencia.setWidthPercentage(100);
        
        agregarCeldaTituloModerno(tablaSentencia, "SENTENCIA TOTAL", fontSubtitulo, 1);

        PdfPCell sentenciaCell = new PdfPCell();
        sentenciaCell.setBorder(Rectangle.NO_BORDER);
        sentenciaCell.setPadding(10);

        Phrase sentenciaPhrase = new Phrase();
        sentenciaPhrase.add(new Chunk("Sentencia total: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sentenciaPhrase.add(new Chunk(sentenciaTotal.getSentenciaFormateada(), 
                                   new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, COLOR_PRIMARIO)));
        sentenciaPhrase.add(Chunk.NEWLINE);
        sentenciaPhrase.add(Chunk.NEWLINE);

        if (sentenciaTotal.getFechaSalidaCalculada() != null) {
            String textoFecha = preso.getEstado() == EstadoPresoEnum.FALLECIDO ?
                    "Fecha de fallecimiento: " : "Fecha estimada de liberación: ";
            sentenciaPhrase.add(new Chunk(textoFecha, fontEtiqueta));
            sentenciaPhrase.add(new Chunk(sentenciaTotal.getFechaSalidaCalculada().format(formatter), 
                                     new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        }

        sentenciaCell.addElement(sentenciaPhrase);
        
        PdfPTable progressBar = new PdfPTable(1);
        progressBar.setWidthPercentage(100);
        
        PdfPCell progressBackground = new PdfPCell();
        progressBackground.setFixedHeight(10);
        progressBackground.setBackgroundColor(BaseColor.LIGHT_GRAY);
        progressBackground.setBorder(Rectangle.NO_BORDER);
        progressBar.addCell(progressBackground);
        
        PdfPCell progressCell = new PdfPCell();
        progressCell.setFixedHeight(8);
        progressCell.setBackgroundColor(COLOR_PRIMARIO);
        progressCell.setBorder(Rectangle.NO_BORDER);
        
        PdfPTable innerProgress = new PdfPTable(1);
        innerProgress.setWidthPercentage(100);
        innerProgress.addCell(progressCell);
        
        PdfPCell progressContainer = new PdfPCell(innerProgress);
        progressContainer.setBorder(Rectangle.NO_BORDER);
        progressContainer.setPaddingTop(1);
        progressContainer.setPaddingLeft(1);
        progressBar.addCell(progressContainer);
        
        sentenciaCell.addElement(progressBar);
        tablaSentencia.addCell(sentenciaCell);

        PdfPCell sentenciaContainer = new PdfPCell(tablaSentencia);
        sentenciaContainer.setBorder(Rectangle.BOX);
        sentenciaContainer.setBorderWidth(0.5f);
        sentenciaContainer.setBorderColor(BaseColor.LIGHT_GRAY);
        sentenciaContainer.setBackgroundColor(COLOR_SECUNDARIO);
        
        PdfPTable sentenciaWrapper = new PdfPTable(1);
        sentenciaWrapper.setWidthPercentage(100);
        sentenciaWrapper.addCell(sentenciaContainer);

        document.add(sentenciaWrapper);
        document.add(Chunk.NEWLINE);

        PdfPTable tablaDelitos = new PdfPTable(5);
        tablaDelitos.setWidthPercentage(100);
        tablaDelitos.setSpacingBefore(10);
        tablaDelitos.setHeaderRows(1);

        float[] columnWidths = {20f, 15f, 12f, 15f, 38f}; 
        tablaDelitos.setWidths(columnWidths);

        agregarCeldaEncabezadoModerno(tablaDelitos, "Delito", fontEtiqueta);
        agregarCeldaEncabezadoModerno(tablaDelitos, "Fecha comisión", fontEtiqueta);
        agregarCeldaEncabezadoModerno(tablaDelitos, "Gravedad", fontEtiqueta);
        agregarCeldaEncabezadoModerno(tablaDelitos, "Sentencia", fontEtiqueta);
        agregarCeldaEncabezadoModerno(tablaDelitos, "Descripción", fontEtiqueta);

        if (delitos != null && !delitos.isEmpty()) {
            boolean alternate = false;
            for (Delito delito : delitos) {
                BaseColor rowColor = alternate ? COLOR_SECUNDARIO : BaseColor.WHITE;
                alternate = !alternate;
                
                PdfPCell delitoCell = new PdfPCell(new Phrase(delito.getNombre(), fontNormal));
                delitoCell.setBackgroundColor(rowColor);
                delitoCell.setPadding(6);
                tablaDelitos.addCell(delitoCell);
                
                PdfPCell fechaCell = new PdfPCell(new Phrase(delito.getFechaComision().format(formatter), fontNormal));
                fechaCell.setBackgroundColor(rowColor);
                fechaCell.setPadding(6);
                tablaDelitos.addCell(fechaCell);
                
                Font gravedadFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,
                    delito.getGravedad().equalsIgnoreCase("Alta") ? COLOR_ALTO_RIESGO : 
                    delito.getGravedad().equalsIgnoreCase("Media") ? COLOR_PRIMARIO : BaseColor.DARK_GRAY);
                PdfPCell gravedadCell = new PdfPCell(new Phrase(delito.getGravedad(), gravedadFont));
                gravedadCell.setBackgroundColor(rowColor);
                gravedadCell.setPadding(6);
                gravedadCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaDelitos.addCell(gravedadCell);
                
                PdfPCell sentenciaDelitoCell = new PdfPCell(new Phrase(delito.getSentencia().getSentenciaFormateada(), fontNormal));
                sentenciaDelitoCell.setBackgroundColor(rowColor);
                sentenciaDelitoCell.setPadding(6);
                tablaDelitos.addCell(sentenciaDelitoCell);
                
                PdfPCell descripcionCell = new PdfPCell();
                descripcionCell.setBackgroundColor(rowColor);
                descripcionCell.setPadding(6);
                
                String descripcion = delito.getDescripcion() != null ? delito.getDescripcion() : "Sin descripción";
                
                if (descripcion.length() > 150) {
                    Paragraph descripcionParrafo = new Paragraph(descripcion, 
                        new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.DARK_GRAY));
                    descripcionCell.addElement(descripcionParrafo);
                } else {
                    descripcionCell.setPhrase(new Phrase(descripcion, 
                        new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.DARK_GRAY)));
                }
                
                tablaDelitos.addCell(descripcionCell);
            }
        } else {
            PdfPCell noDelitos = new PdfPCell(new Phrase("No hay delitos registrados", fontNormal));
            noDelitos.setColspan(5);
            noDelitos.setHorizontalAlignment(Element.ALIGN_CENTER);
            noDelitos.setBorder(Rectangle.NO_BORDER);
            noDelitos.setPadding(10);
            tablaDelitos.addCell(noDelitos);
        }

        PdfPCell delitosContainer = new PdfPCell(tablaDelitos);
        delitosContainer.setBorder(Rectangle.BOX);
        delitosContainer.setBorderWidth(0.5f);
        delitosContainer.setBorderColor(BaseColor.LIGHT_GRAY);
        delitosContainer.setPadding(0);
        
        PdfPTable delitosWrapper = new PdfPTable(1);
        delitosWrapper.setWidthPercentage(100);
        delitosWrapper.addCell(delitosContainer);

        document.add(delitosWrapper);

        document.close();
    }

    private void agregarCeldaTituloModerno(PdfPTable tabla, String texto, Font fuente, int colspan) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setColspan(colspan);
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setPadding(8);
        celda.setBackgroundColor(COLOR_PRIMARIO);
        celda.setBorderColor(BaseColor.LIGHT_GRAY);
        celda.setPaddingTop(10);
        celda.setPaddingBottom(10);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPhrase(new Phrase(texto, new Font(fuente.getFamily(), fuente.getSize(), Font.BOLD, BaseColor.WHITE)));
        tabla.addCell(celda);
    }

    private void agregarCeldaDatosModerno(PdfPTable tabla, String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteValor) {
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, fuenteEtiqueta));
        celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
        celdaEtiqueta.setPadding(5);
        tabla.addCell(celdaEtiqueta);
        
        PdfPCell celdaValor = new PdfPCell(new Phrase(valor != null ? valor : "N/A", fuenteValor));
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setPadding(5);
        tabla.addCell(celdaValor);
    }

    private void agregarCeldaEncabezadoModerno(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(COLOR_PRIMARIO);
        celda.setPadding(8);
        celda.setPhrase(new Phrase(texto, new Font(fuente.getFamily(), fuente.getSize(), Font.BOLD, BaseColor.WHITE)));
        tabla.addCell(celda);
    }

    private PdfPCell crearCeldaFotoPlaceholder(String texto, Font fuente) {
        PdfPTable placeholderTable = new PdfPTable(1);
        placeholderTable.setWidthPercentage(100);
        
        PdfPCell shadowCell = new PdfPCell();
        shadowCell.setFixedHeight(150);
        shadowCell.setBackgroundColor(COLOR_FONDO_FOTO);
        shadowCell.setBorder(Rectangle.BOX);
        shadowCell.setBorderWidth(1);
        shadowCell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        Phrase placeholderText = new Phrase(texto, new Font(fuente.getFamily(), fuente.getSize(), Font.ITALIC, BaseColor.DARK_GRAY));
        PdfPCell textCell = new PdfPCell(placeholderText);
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        placeholderTable.addCell(shadowCell);
        placeholderTable.addCell(textCell);
        
        PdfPCell fotoCell = new PdfPCell(placeholderTable);
        fotoCell.setBorder(Rectangle.NO_BORDER);
        return fotoCell;
    }
}