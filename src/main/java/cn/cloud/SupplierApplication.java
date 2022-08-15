package cn.cloud;


import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lzl
 */
//@ServletComponentScan
//@EnableTransactionManagement
@MapperScan("cn.cloud.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class SupplierApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierApplication.class, args);
    }

}
