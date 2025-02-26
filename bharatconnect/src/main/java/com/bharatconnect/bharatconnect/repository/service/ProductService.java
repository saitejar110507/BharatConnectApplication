package com.bharatconnect.service;

import com.bharatconnect.model.Product;
import com.bharatconnect.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.bharatconnect.grpc.AIServiceGrpc;
import com.bharatconnect.grpc.AIRequest;
import com.bharatconnect.grpc.AIResponse;

import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ManagedChannel channel;
    private final AIServiceGrpc.AIServiceBlockingStub aiStub;

    public ProductService() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        this.aiStub = AIServiceGrpc.newBlockingStub(channel);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        kafkaTemplate.send("product-topic", "New Product Added: " + product.getName());
        redisTemplate.opsForValue().set("product:" + savedProduct.getId(), product.getName());
        return savedProduct;
    }

    public String analyzeProduct(String productName) {
        AIRequest request = AIRequest.newBuilder().setProductName(productName).build();
        AIResponse response = aiStub.analyzeProduct(request);
        return response.getAnalysis();
    }
}
