package com.yethree.springbootboilerplate.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest(classes = TestConfiguration.class)
public class AsyncTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Test
    void testAsync() {
        long beforeFreeMemory = Runtime.getRuntime().freeMemory();

        int loopCount = 10;

        IntStream.range(1, loopCount).forEach(i -> userService.helloAsync());

        log.error("totalMemory : {}", Runtime.getRuntime().totalMemory());
        log.error("maxMemory : {}", Runtime.getRuntime().maxMemory());
        log.error("before freeMemory : {}", beforeFreeMemory);

        long afterFreeMemory = Runtime.getRuntime().freeMemory();

        log.error("after freeMemory : {}", afterFreeMemory);
        log.error("used memory : {}MB", (beforeFreeMemory - afterFreeMemory) / 1024 / 1024);
    }

    @Test
    void testTestExecutor() throws InterruptedException {
        int loopCount = 10;

        CountDownLatch countDownLatch = new CountDownLatch(loopCount);

        IntStream.range(1, loopCount).forEach(i -> {
            taskExecutor.execute(() -> userService.hello());
            countDownLatch.countDown();
        });

        countDownLatch.await(3, TimeUnit.SECONDS);
    }

    @Test
    void testTestExecutorOnAnotherPool() throws InterruptedException {
        int loopCount = 1;

        CountDownLatch countDownLatch = new CountDownLatch(loopCount);
        userService.helloAsyncOnAnotherThreadPool();
        countDownLatch.countDown();

        countDownLatch.await(3, TimeUnit.SECONDS);
    }
}

@EnableAsync
@Configuration
class TestConfiguration {

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Primary
    @Bean
    public TaskExecutor commonTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(50);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("common-thread-pool-");
        return taskExecutor;
    }

    @Bean
    public TaskExecutor anotherTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("another-thread-pool-");
        return taskExecutor;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("default-thread-pool-");
        taskExecutor.initialize();
        return taskExecutor;
    }

}

@Slf4j
class UserService {

    public void hello() {
        log.info("hello - {}", Thread.currentThread().getName());
    }

    @Async
    public void helloAsync() {
        log.info("hello async - {}", Thread.currentThread().getName());
    }

    @Async("anotherTaskExecutor")
    public void helloAsyncOnAnotherThreadPool() {
        log.info("hello async on another pool - {}", Thread.currentThread().getName());
    }
}
