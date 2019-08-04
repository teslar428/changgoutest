package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), //文件名
                file.getBytes(), //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()));//文件后缀名
        String[] upload = FastDFSUtil.upload(fastDFSFile);
        String url = FastDFSUtil.getTrackerUrl() + "/" + upload[0] + "/" + upload[1];
        return new Result(true, StatusCode.OK, "上传成功", url);
    }

    //http://192.168.211.132:8080/group1/M00/00/00/wKjThF1FRAeAa6L4AAHPHf5YHRQ538.jpg

}
