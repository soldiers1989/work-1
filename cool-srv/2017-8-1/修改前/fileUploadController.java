package com.cf.consoleAdmin.controller.admin;
import java.io.File;
import com.cf.consoleAdmin.domain.Result;
import com.cf.consoleAdmin.service.admin.FileUploadService;
import com.cf.consoleAdmin.util.UHttpServlet;
import com.cf.consoleApi.controller.AboutController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import com.cf.consoleAdmin.util.BaseSupport;
import com.cf.common.utils.constant.SysParamsDefine;

/**
 * Created by wengy on 2017/7/31.
 */

@RestController
@RequestMapping("FileUploadAction")
public class fileUploadController {
    private Logger log = LoggerFactory.getLogger(AboutController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping("doUpload")
    public Boolean upload(HttpServletRequest request) throws Exception {
        System.out.print(request);
        Result<?> rs = new Result<>();
        HashMap<String,String> params = UHttpServlet.GetRequestParameters(request);
        //基于File创建一个文件输入流
        System.out.print(params);
        InputStream is = new FileInputStream("C:\\work\\cool-srv\\doc\\jpg\\"+params.get("Filename"));
//        String uploadFilePathBase = BaseSupport.CframeUtil.getSysParamsValue(SysParamsDefine.GROUP_RESOURCE_PATH,
//                SysParamsDefine.RESOURCE_PATH_AML_IMPORTFILE_BASE);
//        String upLoadFilePath = uploadFilePathBase+params.get("WORK_DATE");
//        +"/"+params.get("DEPT_CODE")+"/";
//        File f = new File(upLoadFilePath);
        String upLoadFilePath ="C://work//image";
        File f = new File(upLoadFilePath);
        if(!f.exists()){
            f.mkdirs();//建立目录
        }else{
            String fileFlag = params.get("Filename").substring(0,params.get("Filename").indexOf("VAT")+3);
            File [] files= f.listFiles();
            for(int i=0;i<files.length;i++){
                if(!files[i].isDirectory()){
                    int index=files[i].getName().indexOf(fileFlag);
                    if(index>=0)
                    {
                        files[i].delete();
                    }
                }
            }

        }
//         设置上传文件目录
        String uploadPath = upLoadFilePath;

//         设置目标文件
        File toFile = new File(uploadPath,params.get("Filename"));

        // 创建一个输出流
        OutputStream os = new FileOutputStream(toFile);

        //设置缓存
        byte[] buffer = new byte[1024];

        int length = 0;

        //读取File文件输出到toFile文件中
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        //关闭输入流
        is.close();

        //关闭输出流
        os.close();
       Boolean success = true;

        return success;
    }


}
