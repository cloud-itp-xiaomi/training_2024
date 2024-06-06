package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class UploadController {
    //设置上传文件存储位置
    private static final String UPLOADED_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/upload/";
//上传控制见下页

    @PostMapping("/upload")
    public String singleFileUpload( @RequestParam("file") MultipartFile file ) throws Exception {
        if ( file.isEmpty() ) {
            throw new Exception("文件为空,请选择你的文件上传");
        }
        long size = file.getSize(); // 获取文件大小(字节数)
        if( size > 1024*1024*10 ) { // 限制一下大小(尽管配置是不限制大小)
            throw new Exception("文件不能大于10M");
        }
        String fname = file.getOriginalFilename(); // 获取原始的文件名
//用UUID生成一个随机数作为主文件名(扩展名不变) 目的：避免文件同名
        String newName = UUID.randomUUID().toString() + "." + fname.substring( fname.lastIndexOf(".")+1 );
        File dest_file = new File( UPLOADED_FOLDER + newName ); //Java File类操作
        file.transferTo(dest_file);
        return "文件上传成功";
    }

    @PostMapping("/multiupload")
    public String multiFileUpload(@RequestParam("file") MultipartFile[ ] multipartFile) throws Exception {
        if ( multipartFile == null || multipartFile.length==0 ) {
            throw new Exception("文件为空,请选择你的文件上传");
        }
        for ( MultipartFile file : multipartFile ) {
            if (!file.isEmpty()) {
                long size = file.getSize();
                if (size > 1024 * 1024 * 10) {
                    throw new Exception("文件不能大于10M");
                }
                String fname = file.getOriginalFilename();
                String newName = UUID.randomUUID().toString() + "." + fname.substring(fname.lastIndexOf(".") + 1);
                File dest_file = new File(UPLOADED_FOLDER + newName);
                file.transferTo(dest_file);
            }
        }
        return "文件上传完毕";
    }
}