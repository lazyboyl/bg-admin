package com.bg.admin;

import com.bg.admin.util.GenCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author linzf
 * @since 2019/8/9
 * 类描述：快速生成bg-admin前后端代码的插件
 */
@Mojo(name = "bgAdminCodeGenerator", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class BgAdminCodeGenerator extends AbstractMojo {

    /**
     * @param 数据库地址
     */
    @Parameter(property = "jdbcUrl", defaultValue = "jdbc:mysql://127.0.0.1:3306/vcm?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai")
    private String jdbcUrl;

    /**
     * @param 数据库账号
     */
    @Parameter(property = "userName", defaultValue = "root")
    private String userName;

    /**
     * @param 数据库密码
     */
    @Parameter(property = "password", defaultValue = "123456")
    private String password;

    /**
     * @param 数据库驱动名称
     */
    @Parameter(property = "driverClassName", defaultValue = "com.mysql.jdbc.Driver")
    private String driverClassName;

    /**
     * @param web后端工程所在的位置
     */
    @Parameter(property = "webCorePath", defaultValue = "com.github.lazyboyl.vcm.web.core")
    private String webCorePath;

    /**
     * @param web后端mybatsi的路径
     */
    @Parameter(property = "mybatisBasePath", defaultValue = "/resources/mybatis/mapper")
    private String mybatisBasePath;


    /**
     * @param 开发作者
     */
    @Parameter(property = "author", defaultValue = "无名氏")
    private String author;

    /**
     * @param 表名
     */
    @Parameter(property = "tableName")
    private String tableName;

    /**
     * @param 实体名称
     */
    @Parameter(property = "beanName")
    private String beanName;


    /**
     * @param web前端工程所在的位置
     */
    @Parameter(property = "webPath")
    private String webPath;

    /**
     * @param 当前创建的节点需要挂载到那个节点底下
     */
    @Parameter(property = "routerNode", defaultValue = "sys")
    private String routerNode;

    /**
     * @param 生成的sql脚本的放置的位置
     */
    @Parameter(property = "sqlPath", defaultValue = "")
    private String sqlPath;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (StringUtils.isEmpty(tableName)) {
            throw new MojoFailureException("表名不能为空！");
        }
        if (StringUtils.isEmpty(beanName)) {
            throw new MojoFailureException("实体名称不能为空！");
        }
        try {
            // 生成后端代码
            GenCodeUtil.genFiles(
                    author, tableName,
                    webCorePath, mybatisBasePath,
                    beanName, driverClassName,
                    userName, password, jdbcUrl);
            // 生成前端代码
            GenCodeUtil.genWebFiles(
                    author, tableName, webPath,
                    beanName, driverClassName,
                    userName, password, jdbcUrl,
                    routerNode, sqlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
