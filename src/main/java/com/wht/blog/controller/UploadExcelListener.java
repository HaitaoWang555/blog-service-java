package com.wht.blog.controller;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wht.blog.entity.Meta;
import com.wht.blog.service.MetaService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模板的读取类
 * @author wht
 * @since 2019-10-05 7:16
 */
@Slf4j
public class UploadExcelListener extends AnalysisEventListener<Meta> {
    MetaService metaService;

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private List<Meta> list = new ArrayList<>();

    @Override
    public void invoke(Meta data, AnalysisContext context) {
        data.setCreatedAt(new Date());
        data.setUpdatedAt(new Date());
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        metaService.insertSelective(list);
        log.info("{}条数据，开始存储数据库！", list.size());
        log.info("存储数据库成功！");
    }
}
