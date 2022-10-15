import com.hzx.GuliMailOrderApplication;
import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes= GuliMailOrderApplication.class)
public class tes {
    @Autowired(required = false)
    AmqpAdmin amqpAdmin;
    @Test
    public void cont(){
        DirectExchange exchange = new DirectExchange("hello-java",true,false);
        amqpAdmin.declareExchange(exchange);
        log.info("创建成功");
    }
    @Test
    public void cont2(){
        Queue queue = new Queue("hello-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("创建成功");
    }
    @Test
    public void cont211(){
        Binding binding = new Binding("hello-queue", Binding.DestinationType.QUEUE,
                "hello-java","hello.java",null );
        amqpAdmin.declareBinding(binding);
        log.info("创建成功");
    }
}
