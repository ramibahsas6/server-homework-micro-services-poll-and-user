CREATE TABLE questions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(1000) NOT NULL,
  option_a VARCHAR(300) NOT NULL,
  option_b VARCHAR(300) NOT NULL,
  option_c VARCHAR(300) NOT NULL,
  option_d VARCHAR(300) NOT NULL
);

INSERT INTO questions (title, option_a, option_b, option_c, option_d) VALUES    -- inserting 10 default questions about spring boot subject
 ('Which annotation is used to mark a Spring Boot main class?', '@Component', '@SpringBootApplication', '@Controller', '@Repository'),
 ('What file is used in Spring Boot to configure application properties?', 'application.properties', 'config.yml', 'settings.ini', 'boot.json'),
 ('Which embedded server is commonly used by Spring Boot by default?', 'Tomcat', 'Jetty', 'Undertow', 'GlassFish'),
 ('What annotation is used to create RESTful web services in Spring Boot?', '@Service', '@RestController', '@Repository', '@ControllerAdvice'),
 ('How do you run a Spring Boot application from the command line?', 'java -jar app.jar', 'run app.java', 'mvn start', 'spring run app.jar'),
 ('Which dependency is needed to use Spring Data JPA in Spring Boot?', 'spring-boot-starter-web', 'spring-boot-starter-data-jpa', 'spring-boot-starter-security', 'spring-boot-starter-thymeleaf'),
 ('Which annotation is used for dependency injection in Spring Boot?', '@Inject', '@Autowired', '@Value', '@Bean'),
 ('What is the default port number for a Spring Boot application?', '8080', '80', '443', '3000'),
 ('Which Spring Boot feature helps to simplify exception handling globally?', '@ControllerAdvice', '@ComponentScan', '@EnableAutoConfiguration', '@ConfigurationProperties'),
 ('Which starter is used to create a web application in Spring Boot?', 'spring-boot-starter-data-jpa', 'spring-boot-starter-web', 'spring-boot-starter-test', 'spring-boot-starter-security');


CREATE TABLE answers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  question_id INT,
  selected_option CHAR(1) NOT NULL,
  CONSTRAINT fk_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

INSERT INTO answers (user_id, question_id, selected_option) VALUES
(1, 1, 'B'),
(2, 2, 'A');
