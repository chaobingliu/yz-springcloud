package com.yz.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class TraceApplication {
    private final Logger logger = LoggerFactory.getLogger(getClass());

//	@Bean
//	public AlwaysSampler defaultSampler() {
//		return new AlwaysSampler();
//	}

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/trace", method = RequestMethod.GET)
    public String trace() {
        return "trace1 test success";
    }

    @RequestMapping(value = "/trace-1", method = RequestMethod.GET)
    public String trace1(HttpServletRequest request) {
        logger.info("===<call trace-1, TraceId={}, SpanId={}>===", request.getHeader("X-B3-TraceId"), request.getHeader("X-B3-SpanId"));
        return restTemplate().getForEntity("http://sleuth-trace2/trace-2", String.class).getBody();
    }

    public static void main(String[] args) {
        SpringApplication.run(TraceApplication.class, args);
    }
}
