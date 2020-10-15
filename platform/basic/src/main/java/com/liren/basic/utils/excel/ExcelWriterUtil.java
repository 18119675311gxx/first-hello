package com.liren.basic.utils.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @description: <h1>ExcelWriterUtil 导出excel工具类</h1>
 * 按照使用习惯分为：
 * 1、使用构造方法 导出 excel
 * 2、使用注解方法 导出 excel
 * @author:
 **/

public class ExcelWriterUtil {


    public static void WriteExcel(HttpServletResponse response, List rows, String fileName, String title, String sheet) {
        int count = StrUtil.count(String.valueOf(rows.get(0)), ",");
        WriteExcel(response, rows, fileName, count, title, sheet);
    }

    public static void WriteExcel(HttpServletResponse response, List rows, String fileName, String sheet) {
        WriteExcel(response, rows, fileName, 0, null, sheet);
    }

    /**
     * @param response
     * @param rows      输出数据
     * @param fileName  文件名 -> test
     * @param titleLine 合并单元格后的标题行
     * @param title     合并后的标题
     * @param sheet     sheet 页名称
     * @Description: <h2>写出到web端下载excel</h2>
     * @author:
     */
    public static void WriteExcel(HttpServletResponse response, List rows, String fileName, int titleLine, String title, String sheet) {
        BigExcelWriter writer = ExcelUtil.getBigWriter(sheet);
        ServletOutputStream out = null;
        try {
            if (StrUtil.isNotBlank(title)) {
                writer.merge(titleLine, title);
            }
            assert rows != null;
            writer.write(rows, true);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush(out, true);
            writer.close();
            IoUtil.close(out);
        }
    }

    /**
     * @param filePath 写出excel的文件路径：--> d:/writeMapTest.xlsx
     * @param rows     输出数据
     * @param sheet    sheet 页名称
     * @Description: <h2>写出到excel 到本地</h2>
     * @author:
     */
    public static void WriteExcel(String filePath, List rows, String sheet) {
        BigExcelWriter writer = ExcelUtil.getBigWriter(filePath, sheet);
        try {
            writer.write(rows, true);
        } finally {
            writer.close();
        }
    }

    //-------------------------------------------------使用注解进行导出>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 如果不会用，就用上面的即可
     */

    /**
     * @param response
     * @param head     excel 的标题头 包含标题样式
     * @param fileName excel 文件名
     * @param sheet    excel sheet 页
     * @param data     excel 的数据
     * @Description: <h2>写出到web端下载excel</h2>
     * @author:
     */
    public static void writeExcel(HttpServletResponse response, Class head, String fileName, String sheet, List data) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), head).sheet(sheet).doWrite(data);
    }

    /**
     * @param head  excel 的标题头 包含标题样式
     * @param name  excel 路径名 + 文件名
     * @param sheet excel sheet 页
     * @param data  excel 的数据
     * @Description: <h2>写出到excel 到本地</h2>
     * @author:
     */
    public static void writeExcelLocal(Class head, String name, String sheet, List data) throws IOException {
        EasyExcel.write(name + ".xlsx", head).sheet(sheet).doWrite(data);
    }
}
