package com.EzInvoice.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing // this helps in auto generating the date of new record created and last update done
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

        try
        {
            SpringApplication.run(BackendApplication.class, args);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

	}

}
