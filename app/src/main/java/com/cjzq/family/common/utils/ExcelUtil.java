package com.cjzq.family.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.cjzq.family.BuildConfig;
import com.cjzq.family.domainService.appModel.bean.ContextBean;
import com.cjzq.family.common.dialog.LoadingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";
    private OnSendListener listener;

    public interface OnSendListener {
        void onSendSuccess(String FilePath);

        void onSendFailed();
    }

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, filePath, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param <T>
     * @param objList  待写入的list  自定义的实体类MyDataDTO
     * @param fileName
     * @param runnable
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(Context context, List<ContextBean> objList, String fileName, Runnable runnable, OnSendListener listener) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    ContextBean bean = (ContextBean) objList.get(j);
                    List<String> list = new ArrayList<>();
                    // MyDataDTO 自定义实体类
                    list.add(bean.getCustomer());
                    list.add(bean.getBusinessDepartment());
                    list.add(String.valueOf(bean.getId()));
                    list.add(bean.getCustomerNum());
                    list.add(bean.getCustomerName());
                    list.add(bean.getServiceName());
                    list.add(bean.getReturnTaskName());
                    list.add(bean.getAllocationMethod());
                    list.add(bean.getTaskStatus());
                    list.add(bean.getReturnChannel());
                    list.add(bean.getWayVisit());
                    list.add(bean.getRevisitDays());
                    list.add(bean.getRevisitNum());
                    list.add(bean.getRevisitNameNum());
                    list.add(bean.getTaskRemarks());
                    list.add(bean.getRevisitDetails());
                    list.add(bean.getIsPrize());

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }
                writebook.write();
                workbook.close();
                Log.e("ExcelUtil", "导出Excel成功");
                LoadingDialog.cancelDialogForLoading();
                if (listener != null) {
                    listener.onSendSuccess(fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onSendFailed();
                }
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (listener != null) {
                            listener.onSendFailed();
                        }
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (listener != null) {
                            listener.onSendFailed();
                        }
                    }
                }
            }
        }
    }

    /**
     * 分享给好友
     *
     * @param context
     * @param fileName
     */
    public static void shareFile(Context context, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                share.putExtra(Intent.EXTRA_STREAM, contentUri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            share.setType("application/vnd.ms-excel");//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据类型后缀名简单判断是否Excel文件
     *
     * @param file 文件
     * @return 是否Excel文件
     */
    public static boolean checkIfExcelFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        //”.“ 需要转义字符
        String[] list = name.split("\\.");
        //划分后的小于2个元素说明不可获取类型名
        if (list.length < 2) {
            return false;
        }
        String typeName = list[list.length - 1];
        //满足xls或者xlsx才可以
        return "xls".equals(typeName);
//        return "xls".equals(typeName) || "xlsx".equals(typeName);
    }
}
