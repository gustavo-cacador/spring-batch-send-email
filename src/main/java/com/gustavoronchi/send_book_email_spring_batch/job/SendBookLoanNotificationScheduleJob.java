package com.gustavoronchi.send_book_email_spring_batch.job;

import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Configuration
public class SendBookLoanNotificationScheduleJob extends QuartzJobBean {

    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLaucher;

    public SendBookLoanNotificationScheduleJob(Job job, JobExplorer jobExplorer, JobLauncher jobLaucher) {
        this.job = job;
        this.jobExplorer = jobExplorer;
        this.jobLaucher = jobLaucher;
    }

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer).getNextJobParameters(this.job).toJobParameters();
        try {
            this.jobLaucher.run(this.job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}