package com.recruiting.oss.controller;

import com.recruiting.oss.service.FileService;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "阿里云文件管理")
@RestController
@RequestMapping("/oss")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传")
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public Result uploadFile(@ApiParam(name = "file",value = "文件",required = true)
            @RequestParam("file") MultipartFile file) throws Exception {
        String fileUrl = fileService.uploadFile(file);
        return new Result(true, StatusCode.OK,"上传成功",fileUrl);
    }
}
