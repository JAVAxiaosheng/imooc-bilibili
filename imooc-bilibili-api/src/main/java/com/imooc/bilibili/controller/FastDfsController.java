package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.service.FastDfsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/fast/dfs")
public class FastDfsController {

    @Resource
    private FastDfsService fastDfsService;

    @GetMapping("/file/slices")
    public JsonResponse<String> convertFileToSlices(@RequestParam("file") MultipartFile file) throws Exception {
        String md5 = fastDfsService.convertFileToSlices(file);
        return new JsonResponse<>(md5);
    }

    @GetMapping("/slices/upload")
    public JsonResponse<String> uploadFileSlices(@RequestParam("sliceFile") MultipartFile sliceFile,
                                                 @RequestParam("md5") String md5,
                                                 @RequestParam("sliceNum") Integer sliceNum,
                                                 @RequestParam("totalSliceNum") Integer totalSliceNum) throws Exception {
        String path = fastDfsService.uploadFileSlices(sliceFile, md5, sliceNum, totalSliceNum);
        return new JsonResponse<>(path);
    }

    @GetMapping("/slices")
    public JsonResponse<String> slices(@RequestParam("file") MultipartFile file) throws Exception {
        String path = fastDfsService.uploadAppenderFile(file);
        return new JsonResponse<>(path);
    }
}
