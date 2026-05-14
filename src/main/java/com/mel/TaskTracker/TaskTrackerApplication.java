package com.mel.TaskTracker;

import com.mel.TaskTracker.cli.TaskCLI;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskTrackerApplication implements ApplicationRunner {

	private final TaskCLI taskCLI;

	public TaskTrackerApplication(TaskCLI taskCLI) {
		this.taskCLI = taskCLI;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TaskTrackerApplication.class);
		app.setLogStartupInfo(false);
		app.run(args);
	}

	@Override
	public void run(ApplicationArguments args) {
		String[] rawArgs = args.getNonOptionArgs().toArray(new String[0]);
		taskCLI.run(rawArgs);
	}
}