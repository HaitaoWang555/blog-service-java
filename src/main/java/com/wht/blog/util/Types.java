package com.wht.blog.util;

/**
 * 通用的Type类型
 *
 * @author wht
 * @since 2019-09-19 15:39
 */
public interface Types {
    /**
     * 文章状态 publish 已发布 draft 草稿
     */
    String PUBLISH = "publish";
    String DRAFT = "draft";
    /**
     * 文章类型 post 文章 page 页面
     */
    String POST = "post";
    String PAGE = "page";
    /**
     * 分类和标签
     */
    String CATEGORY = "category";
    String TAG = "tag";

    String LOG_MESSAGE_DELETE_ARTICLE = "删除文章";
    String LOG_MESSAGE_DELETE_META = "删除分类/标签";
    String LOG_MESSAGE_DELETE_USER = "删除用户";
    String LOG_MESSAGE_ADD_USER = "用户注册";
    String LOG_ACTION_DELETE = "删除";
    String LOG_ACTION_ADD = "新增";
    String LOG_TYPE_OPERATE = "operate";
}
