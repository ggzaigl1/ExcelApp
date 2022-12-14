package com.cjzq.family.app.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cjzq.family.base.BaseActivity;
import com.cjzq.family.base.Const;
import com.cjzq.family.common.dialog.LoadingDialog;
import com.cjzq.family.common.utils.ExcelUtil;
import com.cjzq.family.common.utils.FileUtil;
import com.cjzq.family.common.utils.KeyboardUtils;
import com.cjzq.family.databinding.ActivityExcelBinding;
import com.cjzq.family.domainService.appModel.bean.ContextBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import androidx.annotation.RequiresApi;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelActivity extends BaseActivity<ActivityExcelBinding> {
    private int size = 0;
    private String fileUrl;
    private static final String TAG = "ExcelActivity";
    String[] strList = {"上海文诚路", "宿州银河一路", "合肥南京路", "贵州分公司", "深圳泰然五路", "长春临河街", "合肥长江西路",
            "深圳福华路", "芜湖黄山西路", "太原龙兴街", "温岭中华路", "西宁文博路", "南昌井冈山大道", "渭南仓程路", "常州和平中路",
            "宜春宜阳大道", "苏州苏州大道", "温州瓯江路", "上海分公司", "阜阳颍淮大道", "济宁金宇路", "邵阳西湖路", "南通世纪大道", "扬州扬子江北路", "杭州秋涛北路", "重庆科园二街"
            , "盐城世纪大道", "吉林泰山路", "鹰潭胜利东路"};

    public static void start(Context context) {
        Intent starter = new Intent(context, ExcelActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityExcelBinding getViewBinding() {
        return ActivityExcelBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        binding.btOpen.setOnClickListener(v -> {
            String btContent = binding.editNum.getText().toString().trim();
            if (TextUtils.isEmpty(btContent) || btContent.equals("%")) {
                ToastUtils.showLong("请设置筛选比例");
                return;
            }
            if (Integer.parseInt(btContent.replace("%", "")) > 100) {
                ToastUtils.showLong("请输入正确的筛选比例");
                return;
            }
            if (TextUtils.isEmpty(fileUrl)) {
                OpenFile();
            } else {
                confirmBuilder(this, "系统提示", "是否使用上一次Excel文件路径",
                        (dialogInterface, i) -> OpenFile(),
                        (dialogInterface, i) -> setDate(fileUrl))
                        .show();
            }
        });
        binding.editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    binding.editNum.removeTextChangedListener(this);//移除输入监听，避免陷入死循环
                    if (editable.toString().trim().equals(Const.Unit)) {
                        //当此时内容是单位的时候，将edittext置为空
                        binding.editNum.setText("");
                    } else {
                        String lenStr = editable.toString();
                        //辅助判断单位存不存在/存在的位置
                        int i = lenStr.length() - Const.Unit.length();
                        if (i > 0 && lenStr.endsWith(Const.Unit)) {
                            //单位就在最后不处理
                        } else {
                            //当用户自己将光标置到单位后面时，输入的内容就会在单位后面，此时将前面的单位去掉，将单位放到最后
                            lenStr = lenStr.replace(Const.Unit, "") + Const.Unit;
                            binding.editNum.setText(lenStr);
                            //这里得到光标应该在的位置（单位前面）
                            int index = lenStr.length() - Const.Unit.length();
                            //设置光标的位置
                            Selection.setSelection(binding.editNum.getText(), Math.max(index, 0));
                        }
                    }
                    //重新进行监听
                    binding.editNum.addTextChangedListener(this);
                }
            }
        });
    }

    @Override
    protected void initView() {

    }

    /**
     * 选择Excel文件
     */
    private void OpenFile() {
//        指定选择的文件类型
        String[] mimeTypes = {"*/*"};
//        ACTION_GET_CONTENT：允许用户选择特殊种类的数据，并返回
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        StringBuilder mimeTypesStr = new StringBuilder();
        for (String mimeType : mimeTypes) {
            mimeTypesStr.append(mimeType).append("|");
        }
        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        startActivityForResult(Intent.createChooser(intent, "选择文件"), Const.REQUEST_FILE_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG", "onActivityResult: " + requestCode + ":requestCode    " + resultCode + ":resultCode");
        if (requestCode == Const.REQUEST_FILE_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            fileUrl = FileUtil.getPath(this, uri);
            if (!TextUtils.isEmpty(fileUrl)) {
                if (ExcelUtil.checkIfExcelFile(new File(fileUrl))) {
                    setDate(fileUrl);
                    Log.e("TAG", "onActivityResult: " + fileUrl);
                } else {
                    ToastUtils.showLong("当前选择不是Excel类型");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置数据
     */
    private void setDate(String ExcelPath) {
        List<ContextBean> allList = new ArrayList<>();
        LoadingDialog.showDialogForLoading(ExcelActivity.this, "Excel生成中，请稍后...", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                float percentage = Float.parseFloat(binding.editNum.getText().toString().replace("%", "")) / 100;
                Log.e("TAG111", "run: " + percentage);
                List<ContextBean> list = readExcel(ExcelActivity.this, ExcelPath);
//                List<ContextBean> list = readExcel(ExcelActivity.this, Environment.getExternalStorageDirectory() + File.separator + "Visitor");
                Map<Integer, List<ContextBean>> resultMap = new HashMap<>();
                for (ContextBean contextBean : list) {
                    if (resultMap.containsKey(contextBean.getId())) {
                        resultMap.get(contextBean.getId()).add(contextBean);
                    } else {
                        //map中不存在，新建key，用来存放数据
                        List<ContextBean> tmpList = new ArrayList<>();
                        tmpList.add(contextBean);
                        resultMap.put(contextBean.getId(), tmpList);
                    }
                }
                //遍历Map集合的方法，输出List分组后的结果
                Set<Map.Entry<Integer, List<ContextBean>>> entrySet = resultMap.entrySet();
                for (Map.Entry<Integer, List<ContextBean>> entry : entrySet) {
                    List<ContextBean> m = entry.getValue();
                    Collections.shuffle(m);
                    List<ContextBean> list1;
                    double ceil = Math.ceil(m.size() * percentage);
                    size = (int) ceil;
                    if (m.size() < size) {
                        list1 = m.subList(0, m.size());
                    } else {
                        list1 = m.subList(0, size);
                    }
                    allList.addAll(list1);
//                    LogUtils.e(entry.getKey() + ":" + entry.getValue());
                }
//                LogUtils.e("taggAll", allList.toString());
                allList.sort((contextBean, t1) -> contextBean.getId() - t1.getId());
                saveExcel(allList);
            }
        }).start();
    }

    /**
     * 读取Excel
     *
     * @param context
     * @param filePath
     * @return
     */
    public static List<ContextBean> readExcel(Context context, String filePath) {
        List<ContextBean> list = new ArrayList<>();
//        File file = new File(filePath, "test2.xls");
        File file = new File(filePath);
        try {
            InputStream is = new FileInputStream(file);
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();

            for (int i = 1; i < Rows; ++i) {
                ContextBean bean = new ContextBean();
                bean.setCustomer((sheet.getCell(0, i)).getContents());
                bean.setBusinessDepartment((sheet.getCell(1, i)).getContents());
                String id = (sheet.getCell(2, i)).getContents();
                if (TextUtils.isEmpty(id)) {
                    continue;
                }
                bean.setId(Integer.parseInt((id)));
                bean.setCustomerNum((sheet.getCell(3, i)).getContents());
                bean.setCustomerName((sheet.getCell(4, i)).getContents());
                bean.setServiceName((sheet.getCell(5, i)).getContents());
                bean.setReturnTaskName((sheet.getCell(6, i)).getContents());
                bean.setAllocationMethod((sheet.getCell(7, i)).getContents());
                bean.setTaskStatus((sheet.getCell(8, i)).getContents());
                bean.setReturnChannel((sheet.getCell(9, i)).getContents());
                bean.setWayVisit((sheet.getCell(10, i)).getContents());
                bean.setRevisitDays((sheet.getCell(11, i)).getContents());
                bean.setRevisitNum((sheet.getCell(12, i)).getContents());
                bean.setRevisitNameNum((sheet.getCell(13, i)).getContents());
                bean.setRevisitDetails((sheet.getCell(14, i)).getContents());
                bean.setIsPrize("是");
                bean.setBonus((sheet.getCell(16, i)).getContents());
                list.add(bean);
                Log.e(TAG, "第" + i + "行数据=" + (sheet.getCell(0, i)).getContents());
//                Log.e("yy", "第" + i + "行数据=" + customer + "," + BusinessDepartment + "," + BusinessDepartmentNum + "," + customerNum + "," + customerName + "," + serviceName + "," + ReturnTaskName
//                        + "," + AllocationMethod + "," + TaskStatus + "," + ReturnChannel + "," + WayVisit + "," + RevisitDays + "," + RevisitNum + "," + RevisitNameNum + "," + TaskRemarks + "," + RevisitDetails
//                        + "," + isPrize);
            }
            book.close();
            return list;
        } catch (Exception e) {
            Log.e(TAG, "e" + e);
        }
        return new ArrayList<>();
    }

    /**
     * 生产Excel文档
     *
     * @param mList
     */
    private void saveExcel(List<ContextBean> mList) {
        // MyDataDTO 自定义实体类
        // 保存excel
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置保存文件夹
                String filePath = PathUtils.getExternalStoragePath() + File.separator + "ExcelFolder";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //设置保存文件名
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.CHINESE);
                String time = sDateFormat.format(new Date(System.currentTimeMillis()));
                String excelFileName = "/回访任务_" + time + ".xls";
                //设置Excel第一行表头
                String[] title = {"长江龙客户", "客户营业部名称", "客户营业部编号", "客户编号", "客户姓名", "服务人员", "回访任务名称", "分配回访方式", "任务状态", "回访渠道"
                        , "回访方式", "回访时间", "回访员营业部号", "回访员编号", "回访结果及明细", "是否获奖", "获奖金额"};
                //设置Excel的Sheet名
                String sheetName = "回访任务明细";
                String filePaths = filePath + excelFileName;
                ExcelUtil.initExcel(filePaths, sheetName, title);
                ExcelUtil.writeObjListToExcel(ExcelActivity.this, mList, filePaths, this, new ExcelUtil.OnSendListener() {
                    @Override
                    public void onSendSuccess(String FilePath) {
                        ExcelUtil.shareFile(ExcelActivity.this, FilePath);
                        binding.editNum.setText("");
                    }

                    @Override
                    public void onSendFailed() {
                        ToastUtils.showLong("分享失败");
                    }
                });
                Log.e(TAG, "导出Excel成功");
                ToastUtils.showLong("导出Excel成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvEnd.setText("导出Excel成功,存放在:" + filePaths);
                    }
                });
            }
        }).start();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取当前获得焦点的View
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            //调用方法判断是否需要隐藏键盘
            KeyboardUtils.hideKeyboard(ev, view, this);
        }
        return super.dispatchTouchEvent(ev);
    }
}
