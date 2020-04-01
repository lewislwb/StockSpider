package Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import spider.App;

import java.io.*;
import java.util.Set;

public class JSONToExcel {
    public Set<String> keys = null;
    // create HSSFWorkbook object
    public HSSFWorkbook book = new HSSFWorkbook();
    public HSSFSheet sheet = book.createSheet("sheet0");
    public int roleNo = 0;
    public int rowNo = 0;
    public boolean index = false;

    public void toExcel(String str, String code) {
        JSONArray jsonArray = JSONArray.parseArray(str);
        // create HSSFCell object
        for (Object jsonObject : (JSONArray) jsonArray) {
            HSSFRow row = sheet.createRow(roleNo++);
            keys = ((JSONObject) jsonObject).keySet();
            if (index == false) {
                // title
                index = true;
                for (String s : keys) {
                    HSSFCell cell = row.createCell(rowNo++);
                    cell.setCellValue(s);
                }
                HSSFCell cell = row.createCell(rowNo++);
                cell.setCellValue("company");
                rowNo = 0;
                row = sheet.createRow(roleNo++);
            }
            boolean flag = false;
            int counter = 0;
            for (String string : keys) {
                String temp = ((JSONObject) jsonObject).getString(string);
                HSSFCell cell = row.createCell(rowNo++);
                if (temp == null && counter == 0) {
                    flag = true;
                    rowNo = 0;
                    break;
                }
                cell.setCellValue(((JSONObject) jsonObject).getString(string));
                counter = 1;
            }
            if (flag == false) {
                HSSFCell cell = row.createCell(rowNo++);
                cell.setCellValue(code);
                rowNo = 0;
            }
        }
    }

    public void write() throws IOException {
        // output Excel file
        FileOutputStream output = new FileOutputStream(App.i + ".xls");
        book.write(output);
        book.close();
        output.flush();
        output.close();
    }
}