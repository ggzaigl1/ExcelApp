package com.cjzq.family.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.cjzq.family.domainService.appModel.bean.ContextBean;
import com.cjzq.family.domainService.appModel.bean.LanguageBean;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {

    private static WritableFont arial14font = null;
    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 读取Excel文件
     *
     * @param file
     * @throws FileNotFoundException
     */
    public static void readExcel(File file) throws FileNotFoundException {
        if (file == null) {
            Log.e("NullFile", "读取Excel出错，文件为空文件");
            return;
        }
        InputStream stream = new FileInputStream(file);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                XSSFRow row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //每次读取一行的内容
                for (int c = 0; c < cellsCount; c++) {
                    //将每一格子的内容转换为字符串形式
                    String value = getCellAsString(row, c, formulaEvaluator);
                    String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                    Log.d("tag", cellInfo);
                }
            }
        } catch (Exception e) {
            /* proper exception handling to be here */
            Log.d("tag", e.toString());
        }

    }

    /**
     * 读取excel文件中每一行的内容
     *
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.d("tag", e.toString());
        }
        return value;
    }



    /**
     * 读取excel   （xls和xlsx）
     *
     * @return
     */
    public static List<Map<String, String>> readExcel(String columns[]) {
        String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "Visitor";
        File file = new File(logFilePath, "test1.xlsx");
        String filePath = file.getAbsolutePath();
        Sheet sheet = null;
        Row row = null;
        Row rowHeader = null;
        List<Map<String, String>> list = null;
        String cellData = null;
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // 用来存放表中数据
                list = new ArrayList<Map<String, String>>();
                // 获取第一个sheet
                sheet = wb.getSheetAt(0);
                // 获取最大行数
                int rownum = sheet.getPhysicalNumberOfRows();
                // 获取第一行
                rowHeader = sheet.getRow(0);
                row = sheet.getRow(0);
                // 获取最大列数
                int colnum = row.getPhysicalNumberOfCells();
                for (int i = 1; i < rownum; i++) {
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            if (columns[j].equals(getCellFormatValue(rowHeader.getCell(j)))) {
                                cellData = (String) getCellFormatValue(row
                                        .getCell(j));
                                map.put(columns[j], cellData);
                                /*DecimalFormat df = new DecimalFormat("#");
                                System.out.println(    df.format(cellData));*/
                                Log.e("yy", "cellData=" + cellData);
                                Log.e("yy", "map=" + map);
                            }
                        }
                    } else {
                        break;
                    }
                    list.add(map);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取单个单元格数据
     *
     * @param cell
     * @return
     * @author lizixiang ,2018-05-08
     */
    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // 数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public static List<LanguageBean> parseDateSource(Context context) {
        List<LanguageBean> languageBeans = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("test.xlsx");
            XmlPullParser parse = Xml.newPullParser();
            parse.setInput(inputStream, "utf-8");
            int event = parse.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.e("FileUtil", "parseDateSource 初始化指令集合");
                        break;
                    case XmlPullParser.START_TAG:
                        String keys = parse.getName();
                        // <string name="app_name">APP</string>
                        // 读取string标签下的值，key = app_name,value = APP
                        if (keys.equals("string")) {
                            String key = parse.getAttributeValue(null, "name");
                            String value = parse.nextText();
                            Log.e("FileUtil", "START_TAG " + key + "," + value);
                            languageBeans.add(new LanguageBean(key, value));
                        } else {
                            Log.e("FileUtil", "START_TAG keys " + keys);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.e("FileUtil", "END_TAG");
                        break;
                    default:
                        break;
                }
                event = parse.next();// 进入到下一个元素并触发相应事件
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return languageBeans;
    }
}
