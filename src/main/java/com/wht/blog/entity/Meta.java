package com.wht.blog.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Meta {
    private Integer id;

    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("类型")
    private String type;
    @ExcelProperty("背景色")
    private String color;
    @ExcelProperty("颜色")
    private String textColor;
    @ExcelProperty("生成时间")
    private Date createdAt;
    @ExcelProperty("更新时间")
    private Date updatedAt;
}