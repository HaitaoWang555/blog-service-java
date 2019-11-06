package com.wht.blog.controller;

import com.google.common.base.Joiner;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.service.ArticleService;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.Const;
import com.wht.blog.util.Method;
import com.wht.blog.util.RestResponse;
import com.wht.blog.util.Types;
import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.w3c.dom.Document;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wht
 * @since 2019-09-19 15:03
 */
@RestController
@RequestMapping("/api/manage/article")
public class ArticleController extends BaseController{
    @Resource
    private ArticleService articleService;
    @Resource
    private MetaService metasService;

    @GetMapping("/list")
    public RestResponse searchList(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "authorId", required = false) Integer authorId,
            @RequestParam(value = "meta", required = false) String meta,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_at desc") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {

        if (id!=null) {
            return RestResponse.ok(articleService.getOneById(id));
        } else if (!StringUtils.isAllBlank(title, status, type, meta) || authorId!=null || sortBy.equals("updated_at desc")) {
            Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    articleService.search(title, status, type, authorId, meta)
            );
            return RestResponse.ok(new Pagination<Article>(article));
        } else {
            Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    articleService.getAll()
            );
            return RestResponse.ok(new Pagination<Article>(article));
        }
    }

    @PostMapping("/add")
    public RestResponse add(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "tags") String tags,
            @RequestParam(value = "category") String category,
            @RequestParam(value = "status", defaultValue = Types.DRAFT) String status,
            @RequestParam(value = "type", defaultValue = Types.MARKDOWN) String type,
            @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment
    ) {
        this.insert(title, content, tags, category, status, type, allowComment);
        return RestResponse.ok("添加成功");
    }

    @PostMapping("/update")
    public RestResponse update(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "allowComment", required = false) Boolean allowComment,
            @RequestParam(value = "updatedAt", required = false) String updatedAt
    ) throws ParseException {
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        // 只有文章内容改变才更改更新时间
        if (StringUtils.isNotBlank(content)) {
            article.setContent(content);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date date = sdf.parse(updatedAt);
            article.setUpdatedAt(date);
        }
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setType(type);
        article.setAllowComment(allowComment);

        int update = articleService.updateByPrimaryKeySelective(article);
        if (update != 0) {
            if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, Types.TAG, article.getId());
            if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, Types.CATEGORY, article.getId());
        }
        return RestResponse.ok("更新成功");
    }

    @DeleteMapping("/delete")
    public RestResponse del(
            @RequestParam(value = "ids") String ids
    ) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        articleService.del(map);
        return RestResponse.ok("删除成功");
    }

    @PostMapping("/upload")
    public RestResponse upload(
            @RequestParam(value = "file") MultipartFile file
    ) throws IOException, TransformerException, ParserConfigurationException {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        assert fileName != null;
        String suffix = fileName.substring(fileName.indexOf(".") + 1);
        String content;
        switch (suffix.toLowerCase()) {
            case "md":
                content = handleMd(inputStream);
                break;
            case "doc":
                content = handleDoc(inputStream);
                break;
            case "docx":
                content = handleDocx(inputStream);
                break;
            default:
                return RestResponse.fail("格式不正确");
        }
        return RestResponse.ok(content,"导入成功");
    }

    private void insert(String title, String content, String tags, String category, String status, String type, Boolean allowComment) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setType(type);
        article.setAllowComment(allowComment);
        articleService.insertSelective(article);
        //存储分类和标签
        if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, Types.TAG, article.getId());
        if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, Types.CATEGORY, article.getId());
    }

    private String handleMd(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> list = reader.lines().collect(Collectors.toList());
        return Joiner.on("\n").join(list);
    }

    private String handleDoc(InputStream inputStream) throws IOException, ParserConfigurationException, TransformerException {
        String imagePathStr = Method.createFilePath("article");
        File dir = new File(imagePathStr);
        if(!dir.isDirectory()) dir.mkdirs();
        HWPFDocument wordDocument = new HWPFDocument(inputStream);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager((a, b, suggestedName, d, e) -> imagePathStr + File.separator + suggestedName);
        wordToHtmlConverter.processDocument(wordDocument);
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();

        for (Picture pic : pics) {
            pic.writeImageContent(new FileOutputStream(imagePathStr + pic.suggestFullFileName()));
        }

        Document htmlDocument = wordToHtmlConverter.getDocument();

        DOMAnalyzer da = new DOMAnalyzer(htmlDocument);
        da.attributesToStyles();
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.getStyleSheets(); //load the author style sheets
        da.stylesToDomInherited();

        DOMSource domSource = new DOMSource(htmlDocument);
        StringWriter stringWriter = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty( OutputKeys.ENCODING, "utf-8" );
        transformer.setOutputProperty( OutputKeys.METHOD, "html" );
        transformer.transform(
                domSource,
                new StreamResult( stringWriter ) );
        return stringWriter.toString();
    }

    private String handleDocx(InputStream inputStream) throws IOException {
        String imagePathStr = Method.createFilePath("article");

        XWPFDocument document = new XWPFDocument(inputStream);
        XHTMLOptions options = XHTMLOptions.create();

        // 存放图片的文件夹 html中图片的路径
        options.setImageManager(new ImageManager( new File("./"),  "/" + imagePathStr ));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, byteArrayOutputStream, options);

        return byteArrayOutputStream.toString();
    }
}
