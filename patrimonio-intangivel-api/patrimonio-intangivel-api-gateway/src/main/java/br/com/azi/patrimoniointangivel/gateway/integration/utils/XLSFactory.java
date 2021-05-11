package br.com.azi.patrimoniointangivel.gateway.integration.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XLSFactory {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private int linhaInicial;
    private int proximaLinha;
    private int linhaAtual;
    private int colunas;
    private HSSFFont font = null;
    private HSSFCellStyle cellStyle = null;
    private HSSFCellStyle styleCurrencyFormat = null;
    private short dateFormat;

    private XLSFactory(int colunas, String formatDate, int linhaInicial) {
        this.colunas = colunas;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sheet");
        dateFormat = workbook.createDataFormat().getFormat(formatDate);
        this.linhaInicial = linhaInicial;
        linhaAtual = linhaInicial;
        proximaLinha = linhaInicial;
    }

    private XLSFactory(int colunas, String formatDate) {
        this.colunas = colunas;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sheet");
        dateFormat = workbook.createDataFormat().getFormat(formatDate);
        linhaInicial = 0;
        linhaAtual = 0;
        proximaLinha = 1;
    }

    public static XLSFactory getInstance(int colunas, String formatDate) {
        return new XLSFactory(colunas, formatDate);
    }

    public static XLSFactory getInstance(int colunas, String formatDate, int linhaInicial) {
        return new XLSFactory(colunas, formatDate, linhaInicial);
    }

    public XLSFactory inserirTituloLinha(String tituloRelatorioXls) {
        if (tituloRelatorioXls != null) {
            HSSFRow tituloLinha = getPrimeiraLinha();

            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, colunas - 1);
            sheet.addMergedRegion(cellRangeAddress);
            setBordersOnMergedCell(cellRangeAddress, BorderStyle.THIN, sheet, workbook);

            createColumn(tituloLinha, 0, tituloRelatorioXls, getTitleHeaderStyle());
        }
        return this;
    }

    public XLSFactory inserirCabecalhos(String... cabecalhos) {
        if (cabecalhos != null && cabecalhos.length == colunas) {

            HSSFRow sheetRow = sheet.createRow(getProximaLinha());

            for (int i = 0; i < cabecalhos.length; i++) {
                createColumn(sheetRow, i, cabecalhos[i], getTitleHeaderStyle());
            }
        }
        return this;
    }

    public XLSFactory autoSizeColumns(Integer numOfColumns) {
        for (short i = 0; i < numOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
        return this;
    }

    public XLSFactory setSizeColumn(int columnIndex, int width) {
        sheet.setColumnWidth(columnIndex, width);
        return this;
    }

    public XLSFactory autoSizeRows(Integer numOfRows, int alturaDaLinha) {
        int inicio = linhaInicial + 1;
        for (int linha = inicio; linha < numOfRows + inicio; linha++) {
            sheet.getRow(linha).setHeightInPoints(alturaDaLinha);
        }
        return this;
    }

    public byte[] build() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        baos.close();
        return baos.toByteArray();
    }

    public XLSFactory inserirDadosNasCelulas(List<Map<Integer, Object>> inconsistencias, ArrayList<String> types) {
        List<HSSFCellStyle> estilosPorColuna = gerarEstilosPorColuna(types);

        for (Map<Integer, Object> linhas : inconsistencias) {
            HSSFRow sheetRow = sheet.createRow(getProximaLinha());
            for (Map.Entry<Integer, Object> linha : linhas.entrySet()) {
                createColumn(sheetRow, linha.getKey(), linha.getValue(), estilosPorColuna.get(linha.getKey()));
            }
        }

        return this;
    }

    private List<HSSFCellStyle> gerarEstilosPorColuna(ArrayList<String> types) {
        return types.stream().map(type -> getBodyStyle(type)).collect(Collectors.toList());
    }

    private static void setBordersOnMergedCell(CellRangeAddress cellRangeAddress, BorderStyle borderStyle, HSSFSheet sheet, HSSFWorkbook workbook) {
        int rowStart = cellRangeAddress.getFirstRow();
        int rowEnd = cellRangeAddress.getLastRow();
        int column = cellRangeAddress.getFirstColumn();

        for (int i = rowStart; i <= rowEnd; i++) {
            HSSFCell cell = getCell(getRow(i, sheet), column);
            HSSFCellStyle cellStyle = cell.getCellStyle();

            setCellBorders(cellStyle, borderStyle);
        }
    }

    private static void setCellBorders(HSSFCellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
    }

    private static HSSFCell getCell(HSSFRow row, int columnIndex) {
        HSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    private static HSSFRow getRow(int rowIndex, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    private void createColumn(HSSFRow row, int cellNum, Object cellValue, HSSFCellStyle cellStyle) {
        HSSFCell cell = row.createCell(cellNum);
        if (cellValue instanceof String) {
            String str = (String) cellValue;
            cell.setCellValue(str);
            cell.setCellStyle(cellStyle);
        } else if (cellValue instanceof Boolean) {
            Boolean bool = (Boolean) cellValue;
            cell.setCellValue(bool);
            cell.setCellStyle(cellStyle);
        } else if (cellValue instanceof Calendar) {
            Calendar calendar = (Calendar) cellValue;
            cell.setCellValue(calendar);
            cell.setCellStyle(cellStyle);
        } else if (cellValue instanceof Date) {
            cell.setCellValue((Date) cellValue);
            cell.setCellStyle(cellStyle);
        } else if (cellValue instanceof HSSFRichTextString) {
            HSSFRichTextString richTextString = (HSSFRichTextString) cellValue;
            cell.setCellValue(richTextString);
            cell.setCellStyle(cellStyle);
        } else if (cellValue instanceof Double) {
            Double num = (Double) cellValue;
            cell.setCellValue(num);
            cell.setCellStyle(cellStyle);
        }
    }

    private HSSFCellStyle getTitleHeaderStyle() {
        HSSFFont fontTitle = workbook.createFont();
        fontTitle.setFontName("DejaVu Sans");
        HSSFCellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setWrapText(true);
        styleTitle.setHidden(false);
        makeItBold(fontTitle, styleTitle);
        makeItsBackGroundGrey25(styleTitle);
        setFontHeight(fontTitle, 10, styleTitle);
        setAlignment(styleTitle, HorizontalAlignment.CENTER);
        setCellBorders(styleTitle, BorderStyle.THIN);
        return styleTitle;
    }

    private HSSFCellStyle getBodyStyle(String type) {

        HSSFDataFormat df = workbook.createDataFormat();
        font = font == null ? workbook.createFont() : font;
        font.setFontName("DejaVu Sans");
        cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(df.getFormat(type));
        cellStyle.setHidden(false);
        cellStyle.setWrapText(true);
        setFontHeight(font, 10, cellStyle);
        setAlignment(cellStyle, HorizontalAlignment.LEFT);
        setCellBorders(cellStyle, BorderStyle.THIN);

        return cellStyle;
    }

    private static void makeItBold(HSSFFont font, HSSFCellStyle cellStyle) {
        font.setBold(true);
        cellStyle.setFont(font);
    }

    private static void setAlignment(HSSFCellStyle cellStyle, HorizontalAlignment horizontalAlignment) {
        cellStyle.setAlignment(horizontalAlignment);
    }

    private static void makeItsBackGroundGrey25(HSSFCellStyle cellStyle) {
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }


    private static void setFontHeight(HSSFFont font, int weight, HSSFCellStyle cellStyle) {
        font.setFontHeightInPoints((short) weight);
        cellStyle.setFont(font);
    }

    private HSSFRow getPrimeiraLinha() {
        return sheet.createRow(linhaInicial);
    }

    private int getProximaLinha() {
        linhaAtual = proximaLinha;
        proximaLinha++;
        return linhaAtual;
    }
}
