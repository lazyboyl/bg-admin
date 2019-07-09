package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-06-11
 * 类描述：实现文件上传
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 功能描述：实现文件上传
     * @param file 文件的file
     * @param idImageType 文件类型
     * @return
     */
    @PostMapping(value = "uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ReturnInfo uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam("idImageType") String idImageType) {
        if (file.isEmpty()) {
            return new ReturnInfo(SystemStaticConst.FAIL, "上传文件不能为空！");
        }
        if (idImageType == null || "".equals(idImageType)) {
            idImageType = "tmp";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = uploadPath;
        if (filePath == null || "".equalsIgnoreCase(filePath)) {
            filePath = "E://test//";
        }
        filePath = filePath + idImageType + "//";
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UuidUtils.getUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            Map<String, Object> entity = new HashMap<>(2);
            entity.put("name", idImageType + "/" + fileName);
            entity.put("url", idImageType + "/" + fileName);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "上传文件成功！", entity);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ReturnInfo(SystemStaticConst.FAIL, "上传文件失败！请联系管理员！");
        } catch (IOException e) {
            e.printStackTrace();
            return new ReturnInfo(SystemStaticConst.FAIL, "上传文件失败！请联系管理员！");
        }
    }

}
