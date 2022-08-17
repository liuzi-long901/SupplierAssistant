package cn.cloud.config;

import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Configuration
public class zhenziCode {

    @Value("${sms.apiUrl}")
    private String apiUrl;

    @Value("${sms.appId}")
    private String appId;

    @Value("${sms.appSecret}")
    private String appSecret;

    @Resource
    RedisUtil redisUtil;

    public String printRandom(){
        //取随机产生的验证码(4位数字)
        Random rnd = new Random();
        int randNum = rnd.nextInt(8999) + 1000;
        //将整型数字转化成字符串
        String randStr = String.valueOf(randNum);
        return randStr;
    }

    public String sendMessage(String code,String phoneNum) throws Exception {
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number", phoneNum);
        params.put("templateId","10141");
        String[] templateParams = new String[2];
        templateParams[0] = code;
        templateParams[1] = "5分钟";
        params.put("templateParams", templateParams);
        String result = client.send(params);
        redisUtil.set(phoneNum,code,5*60);
        return result;
    }

}
