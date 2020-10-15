package com.beizeng.admin.controller;

import com.beizeng.admin.common.sys.annotion.IgnToken;
import com.beizeng.admin.entity.DemoData;
import com.liren.basic.utils.excel.ExcelWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: <h1>ExcelController web导出excel</h1>
 * @author:
 **/
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    @IgnToken
    @GetMapping("/download.ohtml")
    public void download(HttpServletResponse response) throws IOException {
        ExcelWriterUtil.writeExcel(response, DemoData.class, "导出excel标题", "模板sheet页", data());
    }

    @IgnToken
    @GetMapping("/download1")
    public void download1(HttpServletResponse response) {
        ExcelWriterUtil.WriteExcel(response, data(), "text", "导出excel标题", "模板sheet页");
    }

}
