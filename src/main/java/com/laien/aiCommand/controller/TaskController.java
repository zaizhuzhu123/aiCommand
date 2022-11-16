//package com.laien.aiCommand.controller;
//
//
//import com.laien.aiCommand.controller.base.ResponseController;
//import com.laien.aiCommand.controller.base.ResponseResult;
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.service.ITaskService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.MediaTypeFactory;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.IOException;
//import java.lang.Integer;
//import java.util.UUID;
//
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_STATUS_WAIT;
//import static com.laien.aiCommand.constant.MediaConstant.MEDIA_TYPES;
//
///**
// * <p>
// * 压缩任务表 前端控制器
// * </p>
// *
// * @author xsd
// * @since 2022-06-21
// */
//@RestController
//@Api(tags = "压缩任务")
//@RequestMapping("/compression/task")
//public class TaskController extends ResponseController {
//
//    @Autowired
//    private ITaskService taskService;
//
//    private String sourceDirPath;
//
//    public TaskController() {
//        sourceDirPath = System.getProperty("user.dir") + File.separator + "source_file";
//    }
//
//    @ApiOperation(value = "增加压缩任务")
//    @PostMapping("/add")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "serviceName", value = "业务名称 约定传 oog001、cms之类的 项目名称", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "serviceId", value = "调用方业务id", required = true),
//            @ApiImplicitParam(dataTypeClass = Integer.class, allowableValues = "1,2,3", paramType = "form", name = "fileType", value = "媒体文件类型,1 video,2 music,3 sound", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "firebaseJson", value = "上传firebase所需参数,主要指.json文件中的内容", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "callbackUrl", value = "上传结果回调通知地址", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "storageBucket", value = "storage_bucket", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "databaseUrl", value = "database_url", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "dirName", value = "上传目录", required = true),
//            @ApiImplicitParam(dataTypeClass = String.class, paramType = "form", name = "filePath", value = "在压缩程序上的位置,与file 2选1", required = true),
//
//    })
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseResult<Void> add(HttpServletRequest request, @RequestParam(required = false) MultipartFile file, String serviceName, String serviceId, Integer fileType, String firebaseJson, String callbackUrl, String storageBucket, String databaseUrl, String dirName, String filePath) {
//        File file2 = null;
//        if (StringUtils.isNotBlank(filePath)) {
//            file2 = new File(filePath);
//            if (!file2.exists()) {
//                return fail("file path is error");
//            }
//        }
//        //判断文件类型是否正确
//        if (fileType == null || !MEDIA_TYPES.containsKey(fileType)) {
//            return fail("File type error");
//        }
//        //获取文件后缀名
//        String originalFilename = file2 != null ? file2.getName() : file.getOriginalFilename();
//        String suffix = StringUtils.substringAfterLast(originalFilename, ".");
//        //判断后缀名是否正确
//        if (!MEDIA_TYPES.get(fileType).contains(StringUtils.upperCase(suffix))) {
//            return fail("File type not supported");
//        }
//        //获取文件大小
//        int sizeByM = (int) ((file2 != null ? (file2.length() / 1024) : (file.getSize() / 1024)));
//        String fileName = null;
//        if (file2 == null) {
//            //将源文件保存再本地磁盘
//            String uuid = UUID.randomUUID().toString();
//            String dirPath = sourceDirPath + File.separator + uuid;
//            File dir = new File(dirPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            fileName = sourceDirPath + File.separator + uuid + File.separator + uuid + "." + suffix;
//            try {
//                file.transferTo(new File(fileName));
//            } catch (IOException e) {
//                return fail(e.getMessage());
//            }
//        } else {
//            fileName = filePath;
//        }
//        //保存任务
//        Task task = new Task();
//        task.setServiceName(serviceName);
//        task.setDatabaseUrl(databaseUrl);
//        task.setStorageBucket(storageBucket);
//        task.setCallbackUrl(callbackUrl);
//        task.setCompressionStatus(COMPRESSION_STATUS_WAIT);
//        task.setDirName(dirName);
//        task.setFileSize(sizeByM);
//        task.setFirebaseJson(firebaseJson);
//        task.setServiceId(serviceId);
//        task.setSourceFileContentType(file2 != null ? MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM).toString() : file.getContentType());
//        task.setSourceFileType(fileType);
//        task.setRemoteIp(request.getRemoteAddr());
//        task.setSourceFilePath(fileName);
//        taskService.save(task);
//        return succ();
//    }
//
//}
