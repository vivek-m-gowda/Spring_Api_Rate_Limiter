# Implementing API Rate Limiter using SpringBoot Framework

## _What is API rate limiter?_
An API rate limiter is a system that is used to control the rate of incoming requests to an API. It is used to prevent overuse and abuse of an API, and to ensure that all users have fair access to the API's resources. The rate limiter monitors the number of requests made by a user or application, and if the number of requests exceeds a certain threshold within a specified time period, the rate limiter will block or restrict further requests.

API rate limiters typically work by implementing a specific algorithm, such as token bucket, leaky bucket, fixed window, or sliding window. These algorithms determine how requests are counted and how the rate limiter makes decisions about whether to block or restrict further requests.

__Note* We will be using Bucket4j library, which is a Java rate limiter library that uses the token bucket algorithm in this project__
## _How token bucket algorithm works?_

Here's how it works:

<img alt="token-bucket-image" height="500" width="700"  src="https://miro.medium.com/max/1400/1*_tuX7nBlZpvmpElBHc3T2Q.png" title="TOKEN BUCKET PICTORIAL REPRESENTATION"/>

- The token bucket algorithm starts with a fixed number of tokens in the bucket, and a fixed rate at which tokens are added to the bucket.
- When a request is made to the API, a token is consumed from the bucket.
- If there are enough tokens in the bucket to fulfill the request, the request is processed and the corresponding number of tokens is consumed.
- If there are not enough tokens in the bucket to fulfill the request, the request is blocked or rejected.
- Tokens are added to the bucket at a fixed rate, regardless of whether or not requests are being made. This means that if the bucket is empty, and no requests are made, the bucket will fill up over time.
- The number of tokens in the bucket represents the number of requests that can be made in a given time period.
- The rate at which tokens are added to the bucket can be adjusted to control the rate of incoming requests. For example, if the rate is high, more requests can be made, and if the rate is low, fewer requests can be made.
- The token bucket algorithm is simple, efficient and easy to implement, it's suitable for systems that can handle bursts of traffic.

## _How to implement API Rate Limiter in Spring Project?_

- Create a new SpringBoot project using either **Spring STS** or **Spring Initializr**
        
  - Add required Dependencies
  - Bucket4j dependency might not appear while initializing, we can add that using Maven Repository. 

    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>	
    
    <dependency>
      <groupId>com.github.vladimir-bukhtoyarov</groupId>
      <artifactId>bucket4j-core</artifactId>
      <version>4.10.0</version>
    </dependency>
    ```
- Import the generated project in your IDE. Next we will add just one controller 
    
    ```
    import java.time.Duration;
    
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    import io.github.bucket4j.Bandwidth;
    import io.github.bucket4j.Bucket;
    import io.github.bucket4j.Bucket4j;
    import io.github.bucket4j.Refill;
    
    @RestController
    @RequestMapping("/api")
    public class RateLimitController {

        Refill refill = Refill.of(5, Duration.ofMinutes(1));
        private Bucket bucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();
    
        @GetMapping("/consume_token")
        public ResponseEntity<String> consumeToken(){
            if(bucket.tryConsume(1)) {
                return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
            }
            return new ResponseEntity<String>("HITS EXCEEDED", HttpStatus.TOO_MANY_REQUESTS);
        }
    }
    ```
  
## Explanation 

The first line **Refill refill = Refill.of(5, Duration.ofMinutes(1));** creates a Refill object with the following parameters:

- The first parameter 5 is the number of tokens to add to the bucket every time the bucket refills.
- The second parameter Duration.ofMinutes(1) is the time interval at which the bucket refills with tokens.

The second line **private Bucket bucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();** creates the actual rate limiter, using the Bucket4j builder.

- The Bucket4j.builder() creates a new instance of the builder.
- addLimit(Bandwidth.classic(5, refill)) method adds a new limit to the rate limiter. The limit is defined by the Bandwidth.classic(5, refill) which is the number of tokens that can be consumed at a time and the rate at which the tokens are refilled.
- build() method is used to build the rate limiter with the given configuration.

This line **if(bucket.tryConsume(1))** tries to consume 1 token from the bucket. If there is at least one token in the bucket, it returns true and the request can be processed. If it returns false, it means that the bucket is empty and the request should be blocked or rejected.

### _This rate limiter allows a maximum of 5 requests per minute, and it refills the bucket with 5 tokens every minute. Once the bucket is empty, the rate limiter will start blocking or rejecting requests until tokens are added back to the bucket._












