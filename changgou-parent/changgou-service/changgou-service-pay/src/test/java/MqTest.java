import com.changgou.pay.WeixinPayApplication;
import com.changgou.pay.service.WeixinPayService;
import org.junit.Test;
import org.junit.internal.Classes;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeixinPayApplication.class)
public class MqTest {


    @Autowired
    private Environment environment;
    @Test
    public void fun01(){
        System.out.println(environment.getProperty("mq.pay.queue.order"));
    }
}
