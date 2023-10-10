package io.github.icodegarden.nutrient.kafka.scene.corebusiness;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.github.icodegarden.nutrient.kafka.ObjectSerializer4Test;
import io.github.icodegarden.nutrient.kafka.OrderDetail4Test;
import io.github.icodegarden.nutrient.kafka.reliability.ReliabilityProducer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class CoreBusinessProducerTests {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.184.129:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", ObjectSerializer4Test.class.getName());

		ReliabilityProducer<Integer, OrderDetail4Test> producer = new ReliabilityProducer<>(props);

		OrderDetail4Test orderDetail = new OrderDetail4Test();

		for (int i = 0; i < 10; i++) {
			orderDetail.setOrderNum("orderNum-" + i);
			try {
				producer.sendSync(new ProducerRecord<Integer, OrderDetail4Test>("test-corebusiness", i, orderDetail));
				System.out.println("send index:" + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		producer.close(Duration.ofMillis(30000));
		// ？ 列出kafka所有可重试和不可重试异常
	}

}
