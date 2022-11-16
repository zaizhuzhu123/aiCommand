//package com.laien.aiCommand.controller;
//
//import com.laien.aiCommand.controller.base.ResponseController;
//import com.laien.aiCommand.controller.base.ResponseResult;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
//@Slf4j
//@Api(tags = "管理端：文件上传")
//@RestController
//@RequestMapping("/manage/file")
//public class FileController extends ResponseController {
//
//    private String templateFileDirPath = System.getProperty("user.dir") + File.separator + "tmp_files";
//
//    @ApiOperation(value = "文件上传(临时文件)")
//    @PostMapping("/uploadTmp")
//    public ResponseResult<String> uploadTmp(@RequestParam("file") MultipartFile file, @RequestParam("dirKey") String dirKey) {
//        String uuid = UUID.randomUUID().toString();
//        File tmpDir = new File(templateFileDirPath + File.separator + dirKey + File.separator + uuid);
//        if (!tmpDir.exists()) {
//            tmpDir.mkdirs();
//        }
//        String originalFilename = file.getOriginalFilename();
//        String suffix = StringUtils.substringAfterLast(originalFilename, ".");
//        String fileName = uuid + "." + suffix;
//        String fileFullName = tmpDir.getAbsolutePath() + File.separator + fileName;
//        try {
//            file.transferTo(new File(fileFullName));
//        } catch (IOException e) {
//            return fail(e.getMessage());
//        }
//        return succ(fileFullName);
//    }
//}
