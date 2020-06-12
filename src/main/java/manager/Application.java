package manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication @RequiredArgsConstructor
public class Application {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
    }

}
