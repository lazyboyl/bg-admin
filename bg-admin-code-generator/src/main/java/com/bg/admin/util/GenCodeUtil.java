package com.bg.admin.util;


import com.bg.admin.entity.ColumnModel;
import com.bg.admin.entity.TableModel;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 类描述：生成后端代码的实现类
 *
 * @author linzf
 * @since 2019-08-05
 */
public class GenCodeUtil {

    /**
     * 从表结构中去生成javabean
     *
     * @param author
     * @param table
     * @param beanName
     * @param packagePath
     * @return
     */
    private static String genJavaBeanFromTableStructure(String author, TableModel table, String beanName, String packagePath) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(packagePath)) {
            sb.append("package " + packagePath + ";\n");
        }
        for (String imp : table.getImports()) {
            sb.append("import " + imp + ";\n");
        }
        sb.append("import javax.persistence.*;\n");
        sb.append("import org.springframework.format.annotation.DateTimeFormat;\n");
        sb.append("import tk.mybatis.mapper.annotation.KeySql;");
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        sb.append("import " + businessPackage + ".util.UuidGenId;\n");
        sb.append("\n");
        sb.append("/**\n *@author " + author + "\n **/\n");
        List<ColumnModel> columnModelList = table.getColumns();
        try {
            sb.append("@Table(name = \"" + table.getTableName() + "\") \r\n");
            sb.append("public class " + toFirstCharUpCase(beanName) + " {\r\n");
            for (ColumnModel columnModel : columnModelList) {
                if (StringUtils.isNotBlank(columnModel.getRemarks())) {
                    sb.append("	/** \r\n");
                    sb.append("	* " + columnModel.getRemarks() + " \r\n");
                    sb.append("	*/\r\n");
                }
                if (columnModel.isPrimaryKey()) {
                    sb.append("    @Id \r\n");
                    // 判断当前的主键是否是自增的，若不是自增的则使用默认的UUID的规则生成
                    if (!columnModel.isAutoIncrement()) {
                        sb.append("    @KeySql(genId = UuidGenId.class) \r\n");
                    }
                }
                sb.append("    @Column(name = \"" + columnModel.getFieldName() + "\") \r\n");
                if (columnModel.getFieldType().equals("Date")) {
                    sb.append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\") \r\n");
                }
                sb.append("	private " + columnModel.getFieldType() + " " + columnModel.getFieldName() + ";\r\n");

            }
            sb.append("\r\n");
            //get set
            for (ColumnModel columnModel : columnModelList) {
                sb.append(
                        "\tpublic " + columnModel.getColumnClassName() + " get" + toFirstCharUpCase((String) columnModel.getFieldName()) + "() {\r\n" +
                                "\t\treturn " + columnModel.getFieldName() + ";\r\n" +
                                "\t}\r\n" +
                                "\r\n" +
                                "\tpublic void set" + toFirstCharUpCase((String) columnModel.getFieldName()) + "(" + columnModel.getColumnClassName() + " " + columnModel.getFieldName() + ") {\r\n" +
                                "\t\tthis." + columnModel.getFieldName() + " = " + columnModel.getFieldName() + ";\r\n" +
                                "\t}\r\n\r\n");
            }
            sb.append("}\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从表结构中去生成查询实体类
     *
     * @param author
     * @param table
     * @param beanName
     * @param extendsBasePackage
     * @param packagePath
     * @return
     */
    private static String genQueryModelFromTableStructure(String author, TableModel table, String beanName, String extendsBasePackage, String packagePath) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(packagePath)) {
            sb.append("package " + packagePath + ";\n\n");
        }
        sb.append("import " + extendsBasePackage + ".entity.QueryBase;\n\n");
        sb.append("/**\n *@author " + author + "\n **/\n");
        try {
            sb.append("public class " + toFirstCharUpCase(beanName) + " extends QueryBase {\r\n");
            List<ColumnModel> columns = getQueryFields(table);
            for (ColumnModel columnModel : columns) {
                if (StringUtils.isNotBlank(columnModel.getRemarks())) {
                    sb.append("	//" + columnModel.getRemarks() + " \r\n");
                }
                String qFieldType = getQueryModelFieldType(columnModel.getFieldType());
                sb.append("	private " + qFieldType + " " + columnModel.getFieldName() + ";\r\n");
            }
            sb.append("\r\n");
            //get set
            for (ColumnModel columnModel : columns) {
                String qFieldType = getQueryModelFieldType(columnModel.getFieldType());
                sb.append(
                        "\tpublic " + qFieldType + " get" + toFirstCharUpCase((String) columnModel.getFieldName()) + "() {\r\n" +
                                "\t\treturn " + columnModel.getFieldName() + ";\r\n" +
                                "\t}\r\n" +
                                "\r\n" +
                                "\tpublic void set" + toFirstCharUpCase((String) columnModel.getFieldName()) + "(" + qFieldType + " " + columnModel.getFieldName() + ") {\r\n" +
                                "\t\tthis." + columnModel.getFieldName() + " = " + columnModel.getFieldName() + ";\r\n" +
                                "\t}\r\n\r\n");
            }
            sb.append("}\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 生成Dao
     */
    private static String genDao(String author, String packagePath, String beanName) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(packagePath)) {
            sb.append("package " + packagePath + ";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        sb.append("import tk.mybatis.mapper.common.Mapper;\n");
        sb.append("import " + businessPackage + ".entity." + beanName + ";\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import java.util.List;\n");
        sb.append("/**\n *@author " + author + "\n **/\n");
        sb.append("public interface " + beanName + "Dao extends Mapper<" + beanName + "> {\r\n");
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：获取列表数据\n");
        sb.append("\t* @param search 查询的实体\n");
        sb.append("\t* @return 返回查询结果\n");
        sb.append("\t*/\n");
        sb.append("\t List<" + beanName + "> query" + beanName + "List(@Param(\"search\") String search);");
        sb.append("\n");
        sb.append("\n\t\n}");
        return sb.toString();
    }

    /**
     * 生成Service
     */
    private static String genService(String author, String packagePath, String beanName, TableModel table) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(packagePath)) {
            sb.append("package " + packagePath + ";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\n\n");
        sb.append("import " + businessPackage + ".entity." + beanName + ";\n");
        sb.append("import " + businessPackage + ".constant.SystemStaticConst;\n");
        sb.append("import " + businessPackage + ".entity.ReturnInfo;\n");
        sb.append("import " + businessPackage + ".dao." + beanName + "Dao;\n");
        sb.append("import com.github.pagehelper.PageHelper;\n");
        sb.append("import " + businessPackage + ".util.PageUtil;\n");
        sb.append("import " + businessPackage + ".entity.Page;\n");

        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.List;\n");

        sb.append("/**\n *@author " + author + "\n **/\n");
        sb.append("@Service(\"" + toFirstCharLowerCase(beanName) + "Service\")\n");
        sb.append("@Transactional(rollbackFor={Exception.class})\n");
        sb.append("public class " + beanName + "Service {\r\n");
        sb.append("\t@Autowired\n");
        sb.append("\t@SuppressWarnings(\"SpringJavaAutowiringInspection\")\n");
        sb.append("\tprivate " + beanName + "Dao " + toFirstCharLowerCase(beanName) + "Dao;\n");
        // 新增数据的方法
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：新增数据\n");
        sb.append("\t* @param " + toFirstCharLowerCase(beanName) + " 实体\n");
        sb.append("\t* @return 返回新增结果\n");
        sb.append("\t*/\n");
        sb.append("\tpublic ReturnInfo add" + beanName + "(" + beanName + " " + toFirstCharLowerCase(beanName) + "){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\t" + toFirstCharLowerCase(beanName) + "Dao.insert(" + toFirstCharLowerCase(beanName) + ");\n");
        sb.append("\t\t} catch (Exception e) {\n");
        sb.append("\t\t\t return new ReturnInfo(SystemStaticConst.FAIL, \"增加失败，失败原因：\" + e.getMessage());\n");
        sb.append("\t\t}\n");
        sb.append("\t\treturn new ReturnInfo(SystemStaticConst.SUCCESS, \"增加成功!\");\n");
        sb.append("\t}\n");
        // 根据主键删除数据的方法
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：根据主键来删除数据\n");
        sb.append("\t* @param " + JdbcUtil.getPrimaryKeyName(table) + " 主键\n");
        sb.append("\t* @return 返回删除结果\n");
        sb.append("\t*/\n");
        sb.append("\tpublic ReturnInfo delete" + beanName + "(" + JdbcUtil.getPrimaryKeyType(table) + " " + JdbcUtil.getPrimaryKeyName(table) + "){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\tif(" + toFirstCharLowerCase(beanName) + "Dao.deleteByPrimaryKey(" + JdbcUtil.getPrimaryKeyName(table) + ")>0){\n");
        sb.append("\t\t\t\treturn new ReturnInfo(SystemStaticConst.SUCCESS, \"删除成功!\");\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t\treturn new ReturnInfo(SystemStaticConst.FAIL, \"删除失败!\");\n");
        sb.append("\t\t} catch (Exception e) {\n");
        sb.append("\t\t\t return new ReturnInfo(SystemStaticConst.FAIL, \"删除据败，失败原因：\" + e.getMessage());\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        // 根据主键来获取数据
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：根据主键来获取数据\n");
        sb.append("\t* @param " + JdbcUtil.getPrimaryKeyName(table) + " 主键\n");
        sb.append("\t* @return 返回获取结果\n");
        sb.append("\t*/\n");
        sb.append("\tpublic ReturnInfo get" + beanName + "(" + JdbcUtil.getPrimaryKeyType(table) + " " + JdbcUtil.getPrimaryKeyName(table) + "){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\t" + beanName + " " + toFirstCharLowerCase(beanName) + " = " + toFirstCharLowerCase(beanName) + "Dao.selectByPrimaryKey(" + JdbcUtil.getPrimaryKeyName(table) + ");\n");
        sb.append("\t\t\tif(" + toFirstCharLowerCase(beanName) + "!=null){\n");
        sb.append("\t\t\t\treturn new ReturnInfo(SystemStaticConst.SUCCESS, \"获取成功!\"," + toFirstCharLowerCase(beanName) + ");\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t} catch (Exception e) {\n");
        sb.append("\t\t\t return new ReturnInfo(SystemStaticConst.FAIL, \"获取据败，失败原因：\" + e.getMessage());\n");
        sb.append("\t\t}\n");
        sb.append("\t\t return new ReturnInfo(SystemStaticConst.FAIL, \"获取据败，失败原因：查无此数据！\");\n");
        sb.append("\t}\n");
        // 分页查询
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：实现分页查询\n");
        sb.append("\t* @param search 需要模糊查询的信息\n");
        sb.append("\t* @param pageSize 每页显示的记录的条数\n");
        sb.append("\t* @param current 当前访问第几页\n");
        sb.append("\t* @param orderKey 排序字段\n");
        sb.append("\t* @param orderByValue 排序方式，降序还是升序\n");
        sb.append("\t* @return 返回获取结果\n");
        sb.append("\t*/\n");
        sb.append("\tpublic ReturnInfo query" + beanName + "List(String search, int pageSize, int current, String orderKey, String orderByValue){\n");
        sb.append("\t\t PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20,(orderKey != null && !\"\".equals(orderKey)) ? ((orderByValue != null && !\"\".equals(orderByValue)) ? (orderKey + \" \" + orderByValue) : orderKey) : \"\");\n");
        sb.append("\t\t HashMap<String, Object> res = PageUtil.getResult(" + toFirstCharLowerCase(beanName) + "Dao.query" + beanName + "List(search));\n");
        sb.append("\t\t return new ReturnInfo(SystemStaticConst.SUCCESS, \"获取数据字典列表数据成功！\", new Page(pageSize, current, (long) res.get(\"total\"), (List) res.get(\"rows\")));\n");
        sb.append("\t}\n");
        // 根据主键来更新数据
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：更新数据\n");
        sb.append("\t* @param " + toFirstCharLowerCase(beanName) + " 实体\n");
        sb.append("\t* @return 返回更新结果\n");
        sb.append("\t*/\n");
        sb.append("\tpublic ReturnInfo update" + beanName + "(" + beanName + " " + toFirstCharLowerCase(beanName) + "){\n");
        sb.append("\t\tif(" + toFirstCharLowerCase(beanName) + "Dao.updateByPrimaryKey(" + toFirstCharLowerCase(beanName) + ")>0){\n");
        sb.append("\t\t\treturn new ReturnInfo(SystemStaticConst.SUCCESS, \"更新成功!\");\n");
        sb.append("\t\t}\n");
        sb.append("\t\treturn new ReturnInfo(SystemStaticConst.FAIL, \"更新失败\");\n");
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 生成controller
     */
    private static String genController(String author, String packagePath, String beanName, TableModel table) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(packagePath)) {
            sb.append("package " + packagePath + ";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.PostMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestParam;\n");
        sb.append("import org.springframework.web.bind.annotation.RestController;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import " + businessPackage + ".entity.ReturnInfo;\n");
        sb.append("import " + businessPackage + ".entity." + beanName + ";\n");
        sb.append("import " + businessPackage + ".service." + beanName + "Service;\n\n");
        sb.append("/**\n *@author " + author + "\n **/\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/" + toFirstCharLowerCase(beanName) + "\")\n");
        sb.append("public class " + beanName + "Controller{\r\n");
        sb.append("\t@Autowired\n");
        sb.append("\tprivate " + beanName + "Service " + toFirstCharLowerCase(beanName) + "Service;\n");
        // 新增数据的方法
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：新增数据\n");
        sb.append("\t* @param " + toFirstCharLowerCase(beanName) + " 实体\n");
        sb.append("\t* @return 返回新增结果\n");
        sb.append("\t*/\n");
        sb.append("\t@PostMapping(\"add" + beanName + "\")\n");
        sb.append("\tpublic ReturnInfo add" + beanName + "(" + beanName + " " + toFirstCharLowerCase(beanName) + "){\n");
        sb.append("\t\treturn  " + toFirstCharLowerCase(beanName) + "Service.add" + beanName + "(" + toFirstCharLowerCase(beanName) + ");\n");
        sb.append("\t}\n");
        // 根据主键删除数据的方法
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：根据主键来删除数据\n");
        sb.append("\t* @param " + JdbcUtil.getPrimaryKeyName(table) + " 主键\n");
        sb.append("\t* @return 返回删除结果\n");
        sb.append("\t*/\n");
        sb.append("\t@PostMapping(\"delete" + beanName + "\")\n");
        sb.append("\tpublic ReturnInfo delete" + beanName + "(@RequestParam(name = \"" + JdbcUtil.getPrimaryKeyName(table) + "\")" + JdbcUtil.getPrimaryKeyType(table) + " " + JdbcUtil.getPrimaryKeyName(table) + "){\n");
        sb.append("\t\treturn  " + toFirstCharLowerCase(beanName) + "Service.delete" + beanName + "(" + JdbcUtil.getPrimaryKeyName(table) + ");\n");
        sb.append("\t}\n");
        // 根据主键来获取数据
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：根据主键来获取数据\n");
        sb.append("\t* @param " + JdbcUtil.getPrimaryKeyName(table) + " 主键\n");
        sb.append("\t* @return 返回获取结果\n");
        sb.append("\t*/\n");
        sb.append("\t@PostMapping(\"get" + beanName + "\")\n");
        sb.append("\tpublic ReturnInfo get" + beanName + "(@RequestParam(name = \"" + JdbcUtil.getPrimaryKeyName(table) + "\")" + JdbcUtil.getPrimaryKeyType(table) + " " + JdbcUtil.getPrimaryKeyName(table) + "){\n");
        sb.append("\t\treturn  " + toFirstCharLowerCase(beanName) + "Service.get" + beanName + "(" + JdbcUtil.getPrimaryKeyName(table) + ");\n");
        sb.append("\t}\n");
        // 分页查询
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：实现分页查询\n");
        sb.append("\t* @param search 需要模糊查询的信息\n");
        sb.append("\t* @param pageSize 每页显示的记录的条数\n");
        sb.append("\t* @param current 当前访问第几页\n");
        sb.append("\t* @param orderKey 排序字段\n");
        sb.append("\t* @param orderByValue 排序方式，降序还是升序\n");
        sb.append("\t* @return 返回获取结果\n");
        sb.append("\t*/\n");
        sb.append("\t@PostMapping(\"query" + beanName + "List\")\n");
        sb.append("\tpublic ReturnInfo query" + beanName + "List(@RequestParam(name = \"search\", required = false)String search,@RequestParam(name = \"pageSize\") int pageSize, @RequestParam(name = \"current\") int current,@RequestParam(name = \"orderKey\")String orderKey, @RequestParam(name = \"orderByValue\")String orderByValue){\n");
        sb.append("\t\treturn  " + toFirstCharLowerCase(beanName) + "Service.query" + beanName + "List(search, pageSize, current, orderKey, orderByValue);\n");
        sb.append("\t}\n");
        // 根据主键来更新数据
        sb.append("\n");
        sb.append("\t/**\n");
        sb.append("\t* 功能描述：更新数据\n");
        sb.append("\t* @param " + toFirstCharLowerCase(beanName) + " 实体\n");
        sb.append("\t* @return 返回更新结果\n");
        sb.append("\t*/\n");
        sb.append("\t@PostMapping(\"update" + beanName + "\")\n");
        sb.append("\tpublic ReturnInfo update" + beanName + "(" + beanName + " " + toFirstCharLowerCase(beanName) + "){\n");
        sb.append("\t\treturn  " + toFirstCharLowerCase(beanName) + "Service.update" + beanName + "(" + toFirstCharLowerCase(beanName) + ");\n");
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 将首字母变大写
     *
     * @param str 字符串
     * @return 返回大写字符串
     */
    private static String toFirstCharUpCase(String str) {
        char[] columnCharArr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnCharArr.length; i++) {
            char cur = columnCharArr[i];
            if (i == 0) {
                sb.append(Character.toUpperCase(cur));
            } else {
                sb.append(cur);
            }
        }
        return sb.toString();
    }

    /**
     * 将首字母变小写
     *
     * @param str 字符串
     * @return 返回小写字符串
     */
    public static String toFirstCharLowerCase(String str) {
        char[] columnCharArr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnCharArr.length; i++) {
            char cur = columnCharArr[i];
            if (i == 0) {
                sb.append(Character.toLowerCase(cur));
            } else {
                sb.append(cur);
            }
        }
        return sb.toString();
    }

    /**
     * 获取查询实体类的字段类型
     *
     * @param javaType java类型
     * @return 返回对应的值
     */
    private static String getQueryModelFieldType(String javaType) {
        switch (javaType) {
            case "byte":
                return "Byte";
            case "short":
                return "Short";
            case "int":
                return "Integer";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "long":
                return "Long";
            case "datetime":
                return "Date";
            case "date":
                return "Date";
            case "timestamp":
                return "Date";
        }
        return "String";
    }

    /**
     * @param table 表格参数
     * @return 返回查询的字符串
     */
    public static List<ColumnModel> getQueryFields(TableModel table) {
        if (table.getPrimaryKeyColumns().size() == 1 && table.getPrimaryKeyColumns().get(0).isAutoIncrement()) {
            List<ColumnModel> columns = new ArrayList<ColumnModel>();
            for (ColumnModel cm : table.getColumns()) {
                if (!cm.isPrimaryKey()) {
                    columns.add(cm);
                }
            }
            return columns;
        }
        return table.getColumns();
    }

    /**
     * 创建文件夹，防止文件路径不存在
     */
    private static String createFloder(String src, String packagePath) throws IOException {
        File file = new File("");
        String path = file.getCanonicalPath();
        File pf = new File(path);
        pf = new File(pf.getAbsolutePath() + "/" + src);
        String[] subF = packagePath.split("/");
        for (String sf : subF) {
            pf = new File(pf.getPath() + "/" + sf);
            if (!pf.exists()) {
                pf.mkdirs();
            }
        }
        return pf.getAbsolutePath();
    }

    /**
     * 创建文件夹，防止文件路径不存在
     */
    private static String createFloder(String basePath) throws IOException {
        File file = new File("");
        String path = file.getCanonicalPath();
        File pf = new File(path);
        String[] subF = basePath.split("/");
        for (String sf : subF) {
            if (StringUtils.isNotEmpty(sf)) {
                pf = new File(pf.getPath() + "/" + sf);
                if (!pf.exists()) {
                    pf.mkdirs();
                }
            }
        }
        return pf.getAbsolutePath();
    }

    /**
     * 功能描述：创建前端的api
     *
     * @param webPath  前端工程路径
     * @param beanName 实体名称
     */
    private static void genApi(String webPath, String beanName) throws IOException {
        // api文件目录不存在则创建api文件目录
        String apiPath = webPath + "/src/api/sys/" + toFirstCharLowerCase(beanName);
        File file = new File(apiPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //生成api相关的文件
        File fEntity = new File(apiPath + "/" + toFirstCharLowerCase(beanName) + ".api" + ".js");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("import {fetch} from '../../base';\n");
        // 添加API
        sb.append("\texport const add" + beanName + " = params => {\n");
        sb.append("\t\treturn fetch('/" + toFirstCharLowerCase(beanName) + "/add" + beanName + "',params);\n");
        sb.append("\t};\n");
        // 删除API
        sb.append("\texport const delete" + beanName + " = params => {\n");
        sb.append("\t\treturn fetch('/" + toFirstCharLowerCase(beanName) + "/delete" + beanName + "',params);\n");
        sb.append("\t};\n");
        // 更新API
        sb.append("\texport const update" + beanName + " = params => {\n");
        sb.append("\t\treturn fetch('/" + toFirstCharLowerCase(beanName) + "/update" + beanName + "',params);\n");
        sb.append("\t};\n");
        // 获取API列表
        sb.append("\texport const query" + beanName + "List = params => {\n");
        sb.append("\t\treturn fetch('/" + toFirstCharLowerCase(beanName) + "/query" + beanName + "List',params);\n");
        sb.append("\t};\n");
        // 获取API
        sb.append("\texport const get" + beanName + " = params => {\n");
        sb.append("\t\treturn fetch('/" + toFirstCharLowerCase(beanName) + "/get" + beanName + "',params);\n");
        sb.append("\t};\n");
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    /**
     * 功能描述：生成路由信息
     *
     * @param webPath    工程路径
     * @param beanName   实体类名称
     * @param routerNode 当前创建的节点需要挂载到那个节点底下
     */
    private static String genRouter(String webPath, String beanName, String routerNode) throws IOException {
        String parentTreeCode = "";
        boolean isAdd = false;
        boolean hasRouter = false;
        boolean hasParentCode = false;
        StringBuilder sb = new StringBuilder("");
        String routerPath = webPath + "/src/router/router.js";
        File fEntity = new File(routerPath);
        FileInputStream fis = new FileInputStream(routerPath);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        List<String> routerStr = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            routerStr.add(line);
            // 表示在我们的路由中已经存在该配置信息了，我们就不再新增该配置信息
            if (line.replaceAll(" ", "").equals("name:'" + toFirstCharLowerCase(beanName) + "List',")) {
                hasRouter = true;
            }

            if (line.replaceAll(" ", "").equals("name:'" + routerNode + "',")) {
                hasParentCode = true;
            }
            if (hasParentCode) {
                System.out.println("====>"+line.replaceAll(" ", ""));
                if (line.replaceAll(" ", "").indexOf("code:") != -1) {
                    parentTreeCode = line.replaceAll(" ", "").replace("code:", "").replaceAll("'", "").replace(",", "");
                    hasParentCode = false;
                }
            }
        }
        for (String s : routerStr) {
            if (hasRouter) {
                sb.append(s + "\n");
            } else {
                // 表示已经定位到了我们需要增加路由节点的父节点的位置了
                if (s.replaceAll(" ", "").equals("name:'" + routerNode + "',")) {
                    isAdd = true;
                }
                // 进入此处做子节点增加逻辑的处理
                if (isAdd) {
                    // 表示已经到了我们需要加入节点的位置了
                    if (s.replaceAll(" ", "").equals("children:[")) {
                        sb.append(s);
                        sb.append("\t{\n");
                        sb.append("\t\tpath: '" + toFirstCharLowerCase(beanName) + "List',\n");
                        sb.append("\t\tname: '" + toFirstCharLowerCase(beanName) + "List',\n");
                        sb.append("\t\tmeta: {\n");
                        sb.append("\t\t\ticon: 'ios-paper',\n");
                        sb.append("\t\t\ttitle: '" + beanName + "',\n");
                        sb.append("\t\t\tcode:'system-manage-" + toFirstCharLowerCase(beanName) + "',\n");
                        sb.append("\t\t\trequireAuth: true\n");
                        sb.append("\t\t},\n");
                        sb.append("\t\tcomponent: resolve => {\n");
                        sb.append("\t\t\trequire(['../view/sys/" + toFirstCharLowerCase(beanName) + "/" + toFirstCharLowerCase(beanName) + "List.vue'], resolve);\n");
                        sb.append("\t\t}\n");
                        sb.append("\t},\n");
                        isAdd = false;
                    } else {
                        sb.append(s + "\n");
                    }
                } else {
                    sb.append(s + "\n");
                }
            }
        }
        br.close();
        isr.close();
        fis.close();
        if (fEntity.exists()) {
            fEntity.delete();
        }
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(sb.toString().getBytes());
        fos.close();
        return parentTreeCode;
    }

    /**
     * 功能描述：生成updateVue的相关文件
     *
     * @param webPath  工程路径
     * @param beanName 实体类的名称
     * @param table    表信息
     */
    private static void genUpdateVue(String webPath, String beanName, TableModel table) throws IOException {
        // vue文件目录不存在则创建api文件目录
        String vuePath = webPath + "/src/view/sys/" + toFirstCharLowerCase(beanName);
        File file = new File(vuePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //生成vue列表的文件
        File fEntity = new File(vuePath + "/update" + beanName + "" + ".vue");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<template>\n");
        sb.append("\t<Modal v-model=\"show\" title=\"修改\" @on-ok=\"ok\" :loading=\"loading\" :mask-closable=\"false\">\n");
        sb.append("\t\t<Form ref=\"" + toFirstCharLowerCase(beanName) + "Form\" :model=\"" + toFirstCharLowerCase(beanName) + "Form\" :rules=\"" + toFirstCharLowerCase(beanName) + "FormRule\">\n");
        for (ColumnModel c : table.getColumns()) {
            if (!c.isPrimaryKey()) {
                if (c.getFieldType().equals("String")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<Input type=\"text\" :maxlength=50 v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\" placeholder=\"请输入" + c.getRemarks() + "\"/>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
                if (c.getFieldType().equals("Date")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<DatePicker style=\"width:100%;\" type=\"datetime\" placeholder=\"请输入" + c.getRemarks() + "\"  v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\" ></DatePicker>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
                if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<InputNumber style=\"width:100%;\"  :min=\"1\" v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\"></InputNumber>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
            }
        }
        sb.append("\t\t</Form>\n");
        sb.append("\t</Modal>\n");
        sb.append("</template>\n");
        sb.append("<script>\n");
        sb.append("\timport {update" + beanName + ",get" + beanName + "} from '../../../api/sys/" + toFirstCharLowerCase(beanName) + "/" + toFirstCharLowerCase(beanName) + ".api'\n");
        sb.append("\texport default {\n");
        sb.append("\t\tname: \"update" + beanName + "\",\n");
        sb.append("\t\tprops: {\n");
        sb.append("\t\t\tvalue: {\n");
        sb.append("\t\t\t\ttype: Boolean,\n");
        sb.append("\t\t\t\tdefault: false\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\t" + JdbcUtil.getPrimaryKeyName(table) + ": {\n");
        sb.append("\t\t\t\ttype: String\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\tdata() {\n");
        sb.append("\t\t\treturn {\n");
        sb.append("\t\t\t\tshow: this.value,\n");
        sb.append("\t\t\t\tloading: true,\n");
        sb.append("\t\t\t\t" + toFirstCharLowerCase(beanName) + "Form: {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("String")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":'',\n");
            }
            if (c.getFieldType().equals("Date")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":'',\n");
            }
            if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":1,\n");
            }
        }
        sb.append("\t\t\t\t},\n");
        sb.append("\t\t\t\t" + toFirstCharLowerCase(beanName) + "FormRule: this.get" + beanName + "FormRule()\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\tmethods: {\n");
        sb.append("\t\t\tok() {\n");
        sb.append("\t\t\t\tthis.$refs['" + toFirstCharLowerCase(beanName) + "Form'].validate((valid) => {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("Date")) {
                String dateVal = "this." + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName();
                sb.append("\t\t\t\t\t\tif(" + dateVal + "!=''){\n");
                sb.append("\t\t\t\t\t\t\t" + dateVal + "=this.formatDate(new Date(" + dateVal + "), 'yyyy-MM-dd hh:mm:ss')\n");
                sb.append("\t\t\t\t\t\t}\n");
            }
        }
        sb.append("\t\t\t\t\tif (valid) {\n");
        sb.append("\t\t\t\t\t\tupdate" + beanName + "(this." + toFirstCharLowerCase(beanName) + "Form).then(res => {\n");
        sb.append("\t\t\t\t\t\t\tif (res.code == 200) {\n");
        sb.append("\t\t\t\t\t\t\t\tthis.closeModal(false);\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$emit('handleSearch');\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$Message.success(res.msg);\n");
        sb.append("\t\t\t\t\t\t\t}else{\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$Message.error(res.msg);\n");
        sb.append("\t\t\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\t\t})\n");
        sb.append("\t\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\t\tthis.$Message.error('表单验证不通过！');\n");
        sb.append("\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\tsetTimeout(() => {\n");
        sb.append("\t\t\t\t\t\tthis.loading = false;\n");
        sb.append("\t\t\t\t\t\tthis.$nextTick(() => {\n");
        sb.append("\t\t\t\t\t\t\tthis.loading = true;\n");
        sb.append("\t\t\t\t\t\t});\n");
        sb.append("\t\t\t\t\t}, 1000);\n");
        sb.append("\t\t\t\t});\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tcloseModal(val) {\n");
        sb.append("\t\t\t\tthis.$emit('input', val);\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tget" + beanName + "FormRule() {\n");
        sb.append("\t\t\t\treturn {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("String")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur'},\n");
                sb.append("\t\t\t\t\t\t{type: 'string', max: " + c.getColumnSize() + ", message: '数据的最大长度为" + c.getColumnSize() + "！', trigger: 'blur'}\n");
                sb.append("\t\t\t\t\t],\n");
            }
            if (c.getFieldType().equals("Date")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur',pattern: /.+/ },\n");
                sb.append("\t\t\t\t\t],\n");
            }
            if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true,pattern:/^[0-9]+$/, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur' },\n");
                sb.append("\t\t\t\t\t],\n");
            }
        }
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\twatch: {\n");
        sb.append("\t\t\tvalue(val) {\n");
        sb.append("\t\t\t\tthis.show = val;\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tshow(val) {\n");
        sb.append("\t\t\t\tif (val) {\n");
        sb.append("\t\t\t\t\tthis.$refs['" + toFirstCharLowerCase(beanName) + "Form'].resetFields();\n");
        sb.append("\t\t\t\t\tget" + beanName + "({" + JdbcUtil.getPrimaryKeyName(table) + ": this." + JdbcUtil.getPrimaryKeyName(table) + "}).then(res => {\n");
        sb.append("\t\t\t\t\t\tif (res.code == 200) {\n");
        sb.append("\t\t\t\t\t\t\tthis." + toFirstCharLowerCase(beanName) + "Form= res.obj;\n");
        sb.append("\t\t\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\t\t\tthis.$Message.error(res.msg);\n");
        sb.append("\t\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\t});\n");
        sb.append("\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\tthis.closeModal(val);\n");
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        sb.append("</script>\n");
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    /**
     * 功能描述：生成addVue的相关文件
     *
     * @param webPath  工程路径
     * @param beanName 实体类的名称
     * @param table    表信息
     */
    private static void genAddVue(String webPath, String beanName, TableModel table) throws IOException {
        // vue文件目录不存在则创建api文件目录
        String vuePath = webPath + "/src/view/sys/" + toFirstCharLowerCase(beanName);
        File file = new File(vuePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //生成vue列表的文件
        File fEntity = new File(vuePath + "/add" + beanName + "" + ".vue");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<template>\n");
        sb.append("\t<Modal v-model=\"show\" title=\"新增\" @on-ok=\"ok\" :loading=\"loading\" :mask-closable=\"false\">\n");
        sb.append("\t\t<Form ref=\"" + toFirstCharLowerCase(beanName) + "Form\" :model=\"" + toFirstCharLowerCase(beanName) + "Form\" :rules=\"" + toFirstCharLowerCase(beanName) + "FormRule\">\n");
        for (ColumnModel c : table.getColumns()) {
            if (!c.isPrimaryKey()) {
                if (c.getFieldType().equals("String")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<Input type=\"text\" :maxlength=50 v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\" placeholder=\"请输入" + c.getRemarks() + "\"/>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
                if (c.getFieldType().equals("Date")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<DatePicker style=\"width:100%;\" type=\"datetime\" placeholder=\"请输入" + c.getRemarks() + "\"  v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\" ></DatePicker>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
                if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                    sb.append("\t\t\t<FormItem label=\"" + c.getRemarks() + "\" prop=\"" + c.getFieldName() + "\">\n");
                    sb.append("\t\t\t\t<InputNumber style=\"width:100%;\"  :min=\"1\" v-model=\"" + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName() + "\"></InputNumber>\n");
                    sb.append("\t\t\t</FormItem>\n");
                }
            }
        }
        sb.append("\t\t</Form>\n");
        sb.append("\t</Modal>\n");
        sb.append("</template>\n");
        sb.append("<script>\n");
        sb.append("\timport {add" + beanName + "} from '../../../api/sys/" + toFirstCharLowerCase(beanName) + "/" + toFirstCharLowerCase(beanName) + ".api'\n");
        sb.append("\texport default {\n");
        sb.append("\t\tname: \"add" + beanName + "\",\n");
        sb.append("\t\tprops: {\n");
        sb.append("\t\t\tvalue: {\n");
        sb.append("\t\t\t\ttype: Boolean,\n");
        sb.append("\t\t\t\tdefault: false\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\tdata() {\n");
        sb.append("\t\t\treturn {\n");
        sb.append("\t\t\t\tshow: this.value,\n");
        sb.append("\t\t\t\tloading: true,\n");
        sb.append("\t\t\t\t" + toFirstCharLowerCase(beanName) + "Form: {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("String")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":'',\n");
            }
            if (c.getFieldType().equals("Date")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":'',\n");
            }
            if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                sb.append("\t\t\t\t\t").append(c.getFieldName()).append(":1,\n");
            }
        }
        sb.append("\t\t\t\t},\n");
        sb.append("\t\t\t\t" + toFirstCharLowerCase(beanName) + "FormRule: this.get" + beanName + "FormRule()\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\tmethods: {\n");
        sb.append("\t\t\tok() {\n");
        sb.append("\t\t\t\tthis.$refs['" + toFirstCharLowerCase(beanName) + "Form'].validate((valid) => {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("Date")) {
                String dateVal = "this." + toFirstCharLowerCase(beanName) + "Form." + c.getFieldName();
                sb.append("\t\t\t\t\t\tif(" + dateVal + "!=''){\n");
                sb.append("\t\t\t\t\t\t\t" + dateVal + "=this.formatDate(new Date(" + dateVal + "), 'yyyy-MM-dd hh:mm:ss')\n");
                sb.append("\t\t\t\t\t\t}\n");
            }
        }
        sb.append("\t\t\t\t\tif (valid) {\n");
        sb.append("\t\t\t\t\t\tadd" + beanName + "(this." + toFirstCharLowerCase(beanName) + "Form).then(res => {\n");
        sb.append("\t\t\t\t\t\t\tif (res.code == 200) {\n");
        sb.append("\t\t\t\t\t\t\t\tthis.closeModal(false);\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$emit('handleSearch');\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$Message.success(res.msg);\n");
        sb.append("\t\t\t\t\t\t\t}else{\n");
        sb.append("\t\t\t\t\t\t\t\tthis.$Message.error(res.msg);\n");
        sb.append("\t\t\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\t\t})\n");
        sb.append("\t\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\t\tthis.$Message.error('表单验证不通过！');\n");
        sb.append("\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\tsetTimeout(() => {\n");
        sb.append("\t\t\t\t\t\tthis.loading = false;\n");
        sb.append("\t\t\t\t\t\tthis.$nextTick(() => {\n");
        sb.append("\t\t\t\t\t\t\tthis.loading = true;\n");
        sb.append("\t\t\t\t\t\t});\n");
        sb.append("\t\t\t\t\t}, 1000);\n");
        sb.append("\t\t\t\t});\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tcloseModal(val) {\n");
        sb.append("\t\t\t\tthis.$emit('input', val);\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tget" + beanName + "FormRule() {\n");
        sb.append("\t\t\t\treturn {\n");
        for (ColumnModel c : table.getColumns()) {
            if (c.getFieldType().equals("String")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur'},\n");
                sb.append("\t\t\t\t\t\t{type: 'string', max: " + c.getColumnSize() + ", message: '数据的最大长度为" + c.getColumnSize() + "！', trigger: 'blur'}\n");
                sb.append("\t\t\t\t\t],\n");
            }
            if (c.getFieldType().equals("Date")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur',pattern: /.+/ },\n");
                sb.append("\t\t\t\t\t],\n");
            }
            if (c.getFieldType().equals("Integer") || c.getFieldType().equals("Long")) {
                sb.append("\t\t\t\t\t" + c.getFieldName() + ": [\n");
                sb.append("\t\t\t\t\t\t{required: true,pattern:/^[0-9]+$/, message: '" + c.getRemarks() + "不能为空！', trigger: 'blur' },\n");
                sb.append("\t\t\t\t\t],\n");
            }
        }
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\twatch: {\n");
        sb.append("\t\t\tvalue(val) {\n");
        sb.append("\t\t\t\tthis.show = val;\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tshow(val) {\n");
        sb.append("\t\t\t\tif (val) {\n");
        sb.append("\t\t\t\t\tthis.$refs['" + toFirstCharLowerCase(beanName) + "Form'].resetFields();\n");
        sb.append("\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\tthis.closeModal(val);\n");
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        sb.append("</script>\n");
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    /**
     * 功能描述：生成listVue的相关文件
     *
     * @param webPath  工程路径
     * @param beanName 实体类的名称
     * @param table    表信息
     */
    private static void genListVue(String webPath, String beanName, TableModel table) throws IOException {
        // vue文件目录不存在则创建api文件目录
        String vuePath = webPath + "/src/view/sys/" + toFirstCharLowerCase(beanName);
        File file = new File(vuePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //生成vue列表的文件
        File fEntity = new File(vuePath + "/" + toFirstCharLowerCase(beanName) + "List" + ".vue");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        StringBuilder sb = new StringBuilder();
        // html部分开始
        sb.append("<template>\n");
        sb.append("\t<div>\n");
        sb.append("\t\t<Card title=\"" + table.getTableName() + "\">\n");
        sb.append("\t\t\t<div>\n");
        sb.append("\t\t\t\t<div style=\"display:inline-block;float:left;\">\n");
        sb.append("\t\t\t\t\t<Button type=\"success\" @click=\"add" + beanName + "\">+创建</Button>\n");
        sb.append("\t\t\t\t</div>\n");
        sb.append("\t\t\t\t<div style=\"display:inline-block;float:right;\">\n");
        sb.append("\t\t\t\t\t<Input v-model=\"search\" suffix=\"ios-search\" placeholder=\"请输入需要查询的信息\" style=\"width: auto\" :search=true @on-search=\"handleSearch\"/>\n");
        sb.append("\t\t\t\t</div>\n");
        sb.append("\t\t\t</div>\n");
        sb.append("\t\t\t<div style=\"clear: both;\"></div>\n");
        sb.append("\t\t\t<div style=\"margin-top: 10px;\">\n");
        sb.append("\t\t\t\t<Table ref=\"" + toFirstCharLowerCase(beanName) + "Table\" @on-sort-change=\"onSortChange\" :columns=\"columns\" :data=\"" + toFirstCharLowerCase(beanName) + "Data\" :height=\"tableHeight\">\n");
        sb.append("\t\t\t\t\t<template slot-scope=\"{ row, index }\" slot=\"action\">\n");
        sb.append("\t\t\t\t\t\t<Button type=\"primary\" @click=\"handleEdit(row, index)\" size=\"small\">修改</Button>\n");
        sb.append("\t\t\t\t\t\t<Button type=\"error\" @click=\"handleDelete(row, index)\" size=\"small\">删除</Button>\n");
        sb.append("\t\t\t\t\t</template>\n");
        sb.append("\t\t\t\t</Table>\n");
        sb.append("\t\t\t</div>\n");
        sb.append("\t\t\t<div style=\"margin-top: 10px;\">\n");
        sb.append("\t\t\t\t<Page show-elevator show-sizer show-total :total=\"total\" :current=\"current\" :page-size=\"pageSize\" @on-change=\"changePage\" @on-page-size-change=\"changePageSize\"/>\n");
        sb.append("\t\t\t</div>\n");
        sb.append("\t\t</Card>\n");
        sb.append("\t\t<add" + beanName + " v-model=\"addShow\" v-on:handleSearch=\"handleSearch\"></add" + beanName + ">\n");
        sb.append("\t\t<update" + beanName + " v-model=\"updateShow\" :" + JdbcUtil.getPrimaryKeyName(table) + "=\"" + JdbcUtil.getPrimaryKeyName(table) + "\" v-on:handleSearch=\"handleSearch\"></update" + beanName + ">\n");
        sb.append("\t</div>\n");
        sb.append("</template>\n");
        // html部分结束，js部分开始
        sb.append("<script>\n");
        sb.append("\timport {delete" + beanName + ", query" + beanName + "List} from '../../../api/sys/" + toFirstCharLowerCase(beanName) + "/" + toFirstCharLowerCase(beanName) + ".api'\n");
        sb.append("\timport add" + beanName + " from './add" + beanName + "'\n");
        sb.append("\timport update" + beanName + " from './update" + beanName + "'\n");
        sb.append("\texport default {\n");
        sb.append("\t\tcomponents: {\n");
        sb.append("\t\t\tadd" + beanName + ",\n");
        sb.append("\t\t\tupdate" + beanName + "\n");
        sb.append("\t\t},\n");
        sb.append("\t\tdata() {\n");
        sb.append("\t\t\treturn {\n");
        sb.append("\t\t\t\tsearch: '',\n");
        sb.append("\t\t\t\t" + toFirstCharLowerCase(beanName) + "Data: [],\n");
        sb.append("\t\t\t\tcolumns: this.get" + beanName + "Columns(),\n");
        sb.append("\t\t\t\tkey: '',\n");
        sb.append("\t\t\t\torder: '',\n");
        sb.append("\t\t\t\ttotal: 0,\n");
        sb.append("\t\t\t\tcurrent: 1,\n");
        sb.append("\t\t\t\tpageSize: 10,\n");
        sb.append("\t\t\t\taddShow: false,\n");
        sb.append("\t\t\t\t" + JdbcUtil.getPrimaryKeyName(table) + ": '',\n");
        sb.append("\t\t\t\tupdateShow: false,\n");
        sb.append("\t\t\t\ttableHeight: 200\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t},\n");
        sb.append("\t\t methods: {\n");
        sb.append("\t\t\tadd" + beanName + "() {\n");
        sb.append("\t\t\t\tthis.addShow = true\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tchangePage(current) {\n");
        sb.append("\t\t\t\tthis.current = current;\n");
        sb.append("\t\t\t\tthis.handleSearch();\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tchangePageSize(pageSize) {// 改变每页记录的条数\n");
        sb.append("\t\t\t\tthis.pageSize = pageSize;\n");
        sb.append("\t\t\t\tthis.handleSearch();\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tonSortChange(sort) {\n");
        sb.append("\t\t\t\tif (sort.order == 'normal') {\n");
        sb.append("\t\t\t\t\tthis.order = '';\n");
        sb.append("\t\t\t\t} else {\n");
        sb.append("\t\t\t\t\tthis.key = sort.key;\n");
        sb.append("\t\t\t\t\tthis.order = sort.order;\n");
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t\tthis.handleSearch();\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\thandleEdit(row, index) {\n");
        sb.append("\t\t\t\tthis." + JdbcUtil.getPrimaryKeyName(table) + "=row." + JdbcUtil.getPrimaryKeyName(table) + ";\n");
        sb.append("\t\t\t\tthis.updateShow = true;\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\thandleDelete(row, index) {\n");
        sb.append("\t\t\t\tthis.$Modal.confirm({");
        sb.append("\t\t\t\t\ttitle: '提示',\n");
        sb.append("\t\t\t\t\tcontent: '<p>是否删除当前数据？</p>',\n");
        sb.append("\t\t\t\t\tonOk: () => {\n");
        sb.append("\t\t\t\t\t\tdelete" + beanName + "({" + JdbcUtil.getPrimaryKeyName(table) + ": row." + JdbcUtil.getPrimaryKeyName(table) + "}).then(res => {\n");
        sb.append("\t\t\t\t\t\t\tif (res.code == 200) {\n");
        sb.append("\t\t\t\t\t\t\t\tthis.handleSearch();\n");
        sb.append("\t\t\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\t\t\tthis.$Message.success(res.msg);\n");
        sb.append("\t\t\t\t\t\t});\n");
        sb.append("\t\t\t\t\t},\n");
        sb.append("\t\t\t\t\tonCancel: () => {\n");
        sb.append("\t\t\t\t\t\tthis.$Message.info('取消了当前的操作行为！');\n");
        sb.append("\t\t\t\t\t}\n");
        sb.append("\t\t\t\t});\n");
        sb.append("\t\t\t },\n");
        sb.append("\t\t\thandleSearch() {\n");
        sb.append("\t\t\t\tlet current = this.current\n");
        sb.append("\t\t\t\tlet pageSize = this.pageSize\n");
        sb.append("\t\t\t\tlet search = this.search\n");
        sb.append("\t\t\t\tlet orderKey = this.key\n");
        sb.append("\t\t\t\tlet orderByValue = this.order\n");
        sb.append("\t\t\t\tconst _this = this;\n");
        sb.append("\t\t\t\tquery" + beanName + "List({\n");
        sb.append("\t\t\t\t\tcurrent,\n");
        sb.append("\t\t\t\t\tpageSize,\n");
        sb.append("\t\t\t\t\tsearch,\n");
        sb.append("\t\t\t\t\torderKey,\n");
        sb.append("\t\t\t\t\torderByValue\n");
        sb.append("\t\t\t\t}).then(res => {\n");
        sb.append("\t\t\t\t\tif (res.code == 200) {\n");
        sb.append("\t\t\t\t\t\t_this.total = res.obj.total\n");
        sb.append("\t\t\t\t\t\t_this." + toFirstCharLowerCase(beanName) + "Data = res.obj.rows\n");
        sb.append("\t\t\t\t\t}\n");
        sb.append("\t\t\t\t\tthis.$Message.success(res.msg)\n");
        sb.append("\t\t\t\t});\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t\tget" + beanName + "Columns() {\n");
        sb.append("\t\t\t\treturn [\n");
        for (ColumnModel c : table.getColumns()) {
            if (!c.isPrimaryKey()) {
                if (c.getFieldType().equals("Date")) {
                    sb.append("\t\t\t\t\t{\n");
                    sb.append("\t\t\t\t\t\ttitle: '" + c.getRemarks() + "',\n");
                    sb.append("\t\t\t\t\t\tkey: '" + c.getFieldName() + "',\n");
                    sb.append("\t\t\t\t\t\tsortable: true,\n");
                    sb.append("\t\t\t\t\t\trender: (h, params) => {\n");
                    sb.append("\t\t\t\t\t\t\treturn h('div',\n");
                    sb.append("\t\t\t\t\t\t\t\tthis.formatDate(new Date(params.row." + c.getFieldName() + "), 'yyyy/MM/dd hh:mm:ss')\n");
                    sb.append("\t\t\t\t\t\t\t)\n");
                    sb.append("\t\t\t\t\t\t}\n");
                    sb.append("\t\t\t\t\t},\n");
                } else {
                    sb.append("\t\t\t\t\t{\n");
                    sb.append("\t\t\t\t\t\ttitle: '" + c.getRemarks() + "',\n");
                    sb.append("\t\t\t\t\t\tkey: '" + c.getFieldName() + "',\n");
                    sb.append("\t\t\t\t\t\tsortable: true\n");
                    sb.append("\t\t\t\t\t},\n");
                }
            }
        }
        sb.append("\t\t\t\t\t{\n");
        sb.append("\t\t\t\t\t\ttitle:'操作',\n");
        sb.append("\t\t\t\t\t\tslot: 'action'\n");
        sb.append("\t\t\t\t\t}\n");
        sb.append("\t\t\t\t]\n");
        sb.append("\t\t\t},\n");
        sb.append("\t\t},\n");
        sb.append("\t\tmounted() {\n");
        sb.append("\t\t\tthis.handleSearch();\n");
        sb.append("\t\t\tthis.tableHeight = window.innerHeight - this.$refs." + toFirstCharLowerCase(beanName) + "Table.$el.offsetTop - 270\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        sb.append("</script>\n");
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    /**
     * 获取主键uuid
     *
     * @return uuid
     */
    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 功能描述：生成授权的sql脚本
     *
     * @param sqlPath        生成的sql存放的路径
     * @param beanName       实体名称
     * @param parentTreeCode 父节点编码
     */
    private static void genAuthSql(String sqlPath, String beanName, String parentTreeCode) throws IOException {
        if (sqlPath==null || "".equals(sqlPath)) {
            File file = new File("");
            sqlPath = file.getCanonicalPath() + "/doc";
        }
        System.out.println("sqlPath=>"+sqlPath);
        File file = new File(sqlPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());
        //生成sql脚本文件
        File fEntity = new File(sqlPath + "/" + DATE_TIME.format(LocalDateTime.now()) + "" + ".sql");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        StringBuilder insertSql = new StringBuilder();
        // 生成插入菜单表列表的sql数据
        insertSql.append("insert into t_tree(treeName,treeCode,treeState,treeType,parentTreeId,parentTreeName,crtDate,fullPath,powerPath)\n");
        insertSql.append("select '" + beanName + "' as treeName,'system-manage-" + toFirstCharLowerCase(beanName) + "' as treeCode ,'1' as treeState,'1' as treeType,treeId as parentTreeId,treeName as parentTreeName,");
        insertSql.append("SYSDATE() as crtDate,fullPath,'/" + toFirstCharLowerCase(beanName) + "/query" + beanName + "List:/" + toFirstCharLowerCase(beanName) + "/get" + beanName + "' as powerPath from t_tree  where treeCode = '" + parentTreeCode + "';");
        insertSql.append("UPDATE t_tree set fullPath = CONCAT(fullPath,',',treeId) where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';\n");
        // 生成插入菜单底下的新增按钮节点的sql数据
        insertSql.append("insert into t_tree(treeName,treeCode,treeState,treeType,parentTreeId,parentTreeName,crtDate,fullPath,powerPath)\n");
        insertSql.append("select '新增' as treeName,'system-manage-" + toFirstCharLowerCase(beanName) + "-add' as treeCode ,'1' as treeState,'2' as treeType,treeId as parentTreeId,treeName as parentTreeName,");
        insertSql.append("SYSDATE() as crtDate,fullPath,'/" + toFirstCharLowerCase(beanName) + "/add" + beanName + "' as powerPath from t_tree  where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';");
        insertSql.append("UPDATE t_tree set fullPath = CONCAT(fullPath,',',treeId) where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';\n");
        // 生成插入菜单底下的修改按钮节点的sql数据
        insertSql.append("insert into t_tree(treeName,treeCode,treeState,treeType,parentTreeId,parentTreeName,crtDate,fullPath,powerPath)\n");
        insertSql.append("select '修改' as treeName,'system-manage-" + toFirstCharLowerCase(beanName) + "-update' as treeCode ,'1' as treeState,'2' as treeType,treeId as parentTreeId,treeName as parentTreeName,");
        insertSql.append("SYSDATE() as crtDate,fullPath,'/" + toFirstCharLowerCase(beanName) + "/update" + beanName + "' as powerPath from t_tree  where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';");
        insertSql.append("UPDATE t_tree set fullPath = CONCAT(fullPath,',',treeId) where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';\n");
        // 生成插入菜单底下的删除按钮节点的sql数据
        insertSql.append("insert into t_tree(treeName,treeCode,treeState,treeType,parentTreeId,parentTreeName,crtDate,fullPath,powerPath)\n");
        insertSql.append("select '新增' as treeName,'system-manage-" + toFirstCharLowerCase(beanName) + "-delete' as treeCode ,'1' as treeState,'2' as treeType,treeId as parentTreeId,treeName as parentTreeName,");
        insertSql.append("SYSDATE() as crtDate,fullPath,'/" + toFirstCharLowerCase(beanName) + "/delete" + beanName + "' as powerPath from t_tree  where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';");
        insertSql.append("UPDATE t_tree set fullPath = CONCAT(fullPath,',',treeId) where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "-delete';\n");
        // 生成角色菜单关联关系的数据
        insertSql.append("insert into t_role_tree(roleTreeId,roleId,treeId) select '" + getUUID() + "' as roleTreeId,'1' as roleId,treeId from t_tree where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "';\n");
        insertSql.append("insert into t_role_tree(roleTreeId,roleId,treeId) select '" + getUUID() + "' as roleTreeId,'1' as roleId,treeId from t_tree where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "-add';\n");
        insertSql.append("insert into t_role_tree(roleTreeId,roleId,treeId) select '" + getUUID() + "' as roleTreeId,'1' as roleId,treeId from t_tree where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "-delete';\n");
        insertSql.append("insert into t_role_tree(roleTreeId,roleId,treeId) select '" + getUUID() + "' as roleTreeId,'1' as roleId,treeId from t_tree where treeCode = 'system-manage-" + toFirstCharLowerCase(beanName) + "-update';\n");
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(insertSql.toString().getBytes());
        fos.close();
    }


    /**
     * 功能描述： 生成前端代码
     *
     * @param author          作者
     * @param tableName       表名
     * @param webPath         前端工程的路径
     * @param beanName        实体类名称
     * @param driverClassName 驱动名称
     * @param username        账号
     * @param password        密码
     * @param url             地址
     * @param tableName       表名
     * @param routerNode      挂载的节点的位置
     * @param sqlPath         生成的sql脚本的放置的位置
     */
    public static void genWebFiles(String author, String tableName,
                                   String webPath, String beanName,
                                   String driverClassName, String username,
                                   String password, String url,
                                   String routerNode, String sqlPath) throws IOException {
        TableModel table = JdbcUtil.getTableStructure(driverClassName, username, password, url, tableName);
        // 生成API文件
        genApi(webPath, beanName);
        // 生成ListVue文件
        genListVue(webPath, beanName, table);
        // 生成addVue文件
        genAddVue(webPath, beanName, table);
        // 生成updateVue文件
        genUpdateVue(webPath, beanName, table);
        // 生成路由信息
        String parentTreeCode = genRouter(webPath, beanName, routerNode);
        if (!"".equals(parentTreeCode)) {
            genAuthSql(sqlPath, beanName, parentTreeCode);
        }
    }

    /**
     * 功能描述： 生成后端代码
     *
     * @param author          作者
     * @param tableName       表名
     * @param basePackage     生成文件的包的基础路径
     * @param mybatisBasePath mybatis配置文件夹路径
     * @param beanName        实体类名称
     * @param driverClassName 驱动名称
     * @param username        账号
     * @param password        密码
     * @param url             地址
     * @param tableName       表名
     */
    public static void genFiles(String author, String tableName,
                                String basePackage, String mybatisBasePath,
                                String beanName,
                                String driverClassName, String username,
                                String password, String url) throws IOException {
        String packagePath = basePackage.replaceAll("\\.", "/");
        TableModel table = JdbcUtil.getTableStructure(driverClassName, username, password, url, tableName);
        String entityPath = createFloder("src/main/java", packagePath + "/entity");
        //生成实体类文件
        File fEntity = new File(entityPath + "/" + beanName + ".java");
        if (fEntity.exists()) {
            fEntity.delete();
        }
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(genJavaBeanFromTableStructure(author, table, beanName, basePackage + ".entity").getBytes());
        fos.close();
        //生成mybatis配置文件
        String mybatisPath = createFloder("/src/main" + mybatisBasePath);
        System.out.println("mybatisPath=>" + mybatisPath);
        fos = new FileOutputStream(mybatisPath + "/mybatis_" + toFirstCharLowerCase(beanName) + ".xml");
        fos.write(MyBatisUtil.genMapperConfig(table, basePackage + ".dao." + beanName + "Dao", basePackage + ".entity." + beanName, beanName).getBytes());
        fos.close();
        //生成Dao
        String daoPath = createFloder("src/main/java", packagePath + "/dao");
        File fDao = new File(daoPath + "/" + beanName + "Dao.java");
        fos = new FileOutputStream(fDao);
        fos.write(genDao(author, basePackage + ".dao", beanName).getBytes());
        fos.close();
        //生成Service
        String servicePath = createFloder("src/main/java", packagePath + "/service");
        File fService = new File(servicePath + "/" + beanName + "Service.java");
        fos = new FileOutputStream(fService);
        fos.write(genService(author, basePackage + ".service", beanName, table).getBytes());
        fos.close();
        // 生成controller
        String controllerPath = createFloder("src/main/java", packagePath + "/controller");
        File fController = new File(controllerPath + "/" + beanName + "Controller.java");
        fos = new FileOutputStream(fController);
        fos.write(genController(author, basePackage + ".controller", beanName, table).getBytes());
        fos.close();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        genAuthSql("E:\\\\idea-work\\\\vcm\\\\vcm-we", "AbcTest", "system-manage");
        // genRouter("E:\\idea-work\\vcm\\vcm-web", "TTest", "abc");
//        genWebFiles("linzf",
//                "t_test",
//                "E:\\idea-work\\vcm\\vcm-web",
//                "TTest",
//                "com.mysql.jdbc.Driver",
//                "root",
//                "123456",
//                "jdbc:mysql://127.0.0.1:3306/vcm?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai",
//                "sys");
//        genFiles("linzf",
//                "t_test",
//                "com.github.lazyboyl.vcm.web.core",
//                "/resources/mybatis/mapper",
//                "TTest",
//                "com.mysql.cj.jdbc.Driver",
//                "root",
//                "123456",
//                "jdbc:mysql://127.0.0.1:3306/vcm?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
    }

}
