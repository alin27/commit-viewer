package codacy.com.commitviewer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CommitViewerApplication {

	/**
	 * Let's go! :)
	 * <p>
	 * Author: Amy Lin
	 **/
	public static void main(String[] args) {
		SpringApplication.run(CommitViewerApplication.class, args);
	}

}
