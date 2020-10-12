package com.yzlm.cyl.cfragment.Excel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;


/**
 * Created by WL on 2017/5/3.
 * Android开发中Excel操作封装类
 */

public class ExcelUtils {
    /*工作表标签字体*/
    private static WritableFont sheetFont = null;
    /*工作表标签单元格样式*/
    private static WritableCellFormat sheetCellFormat = null;

    /*标题字体*/
    private static WritableFont titleFont = null;
    /*标题单元格样式*/
    private static WritableCellFormat titleCellFormat = null;

    /*数据区域字体*/
    private static WritableFont dataFont = null;
    /*数据区域单元格样式*/
    private static WritableCellFormat dataCellFormat = null;

    private final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";


    /*初始化Excel文件的样式变量*/
    private static void initStyleVariable() {
        try {
            sheetFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            sheetFont.setColour(Colour.LIGHT_BLUE);
            sheetCellFormat = new WritableCellFormat(sheetFont);
            sheetCellFormat.setAlignment(Alignment.CENTRE);
            sheetCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            sheetCellFormat.setBackground(Colour.VERY_LIGHT_YELLOW);

            titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            titleCellFormat = new WritableCellFormat(titleFont);
            titleCellFormat.setAlignment(Alignment.CENTRE);
            titleCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleCellFormat.setBackground(Colour.LIGHT_BLUE);

            dataFont = new WritableFont(WritableFont.ARIAL, 12);
            dataCellFormat = new WritableCellFormat(dataFont);
            dataCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        } catch (WriteException e) {
        }
    }

    /*创建Excel文件并建立表头*/
    public static void createExcelAndTitle(String fileName, String sheetName, String[] colName) {
        initStyleVariable();

        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            /*创建工作簿*/
            workbook = Workbook.createWorkbook(file);
            /*创建工作表，两个参数分别为工作表名和表插入位置*/
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            sheet.addCell((WritableCell) new Label(0, 0, fileName, sheetCellFormat));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], titleCellFormat));
            }
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
            saveExceptInfo2File("createExcelAndTitle ：" + e.toString());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    saveExceptInfo2File("createExcelAndTitle workbook：" + e.toString());
                }
            }
        }
    }

    /*将数据内容导出到指定的Excel文件*/
    public static <T> void writeObjListToExcel(Handler handler, List<T> ObjList, String fileName, Context context) {
        if (ObjList != null && ObjList.size() > 0) {
            WritableWorkbook writeBook = null;
            InputStream inputStream = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                inputStream = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(inputStream);
                writeBook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writeBook.getSheet(0);
                for (int j = 0; j < ObjList.size(); j++) {
                    String[] list = (String[]) ObjList.get(j);
                    for (int i = 0; i < list.length; i++) {
                        sheet.addCell(new Label(i, j + 1, list[i], dataCellFormat));
                    }
                }

                writeBook.write();

                Message msg = new Message();
                msg.what = 500;
                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                saveExceptInfo2File("writeObjListToExcel ：" + e.toString());
            } finally {
                if (writeBook != null) {
                    try {
                        writeBook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        saveExceptInfo2File("writeObjListToExcel  writeBook：" + e.toString());
                    }
                }

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        saveExceptInfo2File("writeObjListToExcel  inputStream：" + e.toString());
                    }
                }
            }
        }
    }

    public static void read(String inputFile) throws IOException  {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            for (int j = 0; j < sheet.getColumns(); j++) {
                for (int i = 0; i < sheet.getRows(); i++) {
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    if (cell.getType() == CellType.LABEL) {
                        System.out.println("I got a label "
                                + cell.getContents());
                    }

                    if (cell.getType() == CellType.NUMBER) {
                        System.out.println("I got a number "
                                + cell.getContents());
                    }

                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

}
