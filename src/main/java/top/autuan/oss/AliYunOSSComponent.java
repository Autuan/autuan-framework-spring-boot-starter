package top.autuan.oss;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import top.autuan.web.exception.BusinessException;

import java.time.LocalDate;

@Slf4j
public class AliYunOSSComponent {
    private  String ENDPOINT;
    private  String BUCKET_NAME;
    private OSS CLIENT;
    public AliYunOSSComponent(String endpoint,String bucketName,
                              String key, String secret) {
        this.ENDPOINT = endpoint;
        this.BUCKET_NAME = bucketName;
        this.CLIENT = new OSSClientBuilder().build(endpoint, key, secret);
    }


    public String uploadImg(MultipartFile file) {
        try {

            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));

            String newName = IdUtil.nanoId();
            LocalDate today = LocalDate.now();
            String prefix = LocalDateTimeUtil.format(today, "yy/MM/dd/");

            String objectName = prefix + newName + suffix;

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, file.getInputStream());

            // 设置该属性可以返回response。如果不设置，则返回的response为空。
//            putObjectRequest.setProcess("false");

            // 上传文件
            CLIENT.putObject(putObjectRequest);

            log.info("AliYunOSSComponent -> uploadImg -> done -> path -> {}", objectName);
            return "https://tanyouhui-img.oss-cn-shanghai.aliyuncs.com" + "/" + objectName;

        } catch (Exception e) {
            log.error("AliYunOSSComponent -> uploadImg -> exception -> ", e);
            throw new BusinessException("文件上传失败,请稍后重试");
        }

    }

    void shutdown() {
        if (CLIENT != null) {
            CLIENT.shutdown();
        }
    }
}
