package top.sharehome.securityoperation.net;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.securityoperation.utils.net.NetUtils;

/**
 * 测试网络工具
 *
 * @author AntonyCheng
 */
@SpringBootTest
public class NetTest {

    @Test
    public void testGetRegionByIp() {
        String ipStr = "1.2.3.4";
        String regionByIp = NetUtils.getRegionByIpAddress(ipStr);
        System.out.println(regionByIp);
    }

}
