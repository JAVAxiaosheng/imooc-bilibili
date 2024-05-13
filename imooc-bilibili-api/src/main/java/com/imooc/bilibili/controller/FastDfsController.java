package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.service.FastDfsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/fast/dfs")
public class FastDfsController {

    @Resource
    private FastDfsService fastDfsService;

    @PostMapping("/file/slices")
    public JsonResponse<String> convertFileToSlices(@RequestParam("file") MultipartFile file) throws Exception {
        String md5 = fastDfsService.convertFileToSlices(file);
        return new JsonResponse<>(md5);
    }

    @PostMapping("/slices/upload")
    public JsonResponse<String> uploadFileSlices(@RequestParam("sliceFile") MultipartFile sliceFile,
                                                 @RequestParam("md5") String md5,
                                                 @RequestParam("sliceNum") Integer sliceNum,
                                                 @RequestParam("totalSliceNum") Integer totalSliceNum) throws Exception {
        String path = fastDfsService.uploadFileSlices(sliceFile, md5, sliceNum, totalSliceNum);
        return new JsonResponse<>(path);
    }

    @PostMapping("/upload/appender/file")
    public JsonResponse<String> uploadAppenderFile(@RequestParam("file") MultipartFile file) throws Exception {
        String path = fastDfsService.uploadAppenderFile(file);
        return new JsonResponse<>(path);
    }
}
