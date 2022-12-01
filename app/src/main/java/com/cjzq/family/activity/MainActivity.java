package com.cjzq.family.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cjzq.family.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = findViewById(R.id.btOpen);
        mTextView = findViewById(R.id.tvEnd);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readExcel(MainActivity.this);
//                ExcelUtils.readExcel(new String[]{"长江龙客户", "客户营业部名称", "客户营业部编号", "客户编号", "客户姓名", "服务人员", "回访任务名称", "分配回访方式", "任务状态", "回访渠道"
//                        , "回访方式", "回访时间", "回访员营业部号", "回访员编号", "任务备注", "回访结果及明细", "是否获奖", "获奖金额"});
//                ExcelUtils.parseDateSource(MainActivity.this);
            }
        });
    }

    public static void readExcel(Context context) {
        String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "Visitor";
        File file = new File(logFilePath, "test2.xls");
        Log.e("yy", "file=" + file.getAbsolutePath());
        try {
            InputStream is = new FileInputStream(file);
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();

            for (int i = 1; i < Rows; ++i) {
                String name = (sheet.getCell(0, i)).getContents();
                String department = (sheet.getCell(1, i)).getContents();
                String company = (sheet.getCell(2, i)).getContents();
                String phone = (sheet.getCell(3, i)).getContents();

                Log.e("yy", "第" + i + "行数据=" + name + "," + department + "," + company + "," + phone);

            }
            book.close();

        } catch (Exception e) {

            Log.e("yy", "e" + e);
        }
    }
}