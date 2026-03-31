package com.gustavoronchi.send_book_email_spring_batch.step;

import com.gustavoronchi.send_book_email_spring_batch.domain.UserBookLoan;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SendEmailUserStepConfig {

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step sendEmailUserStep(ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader,
                                  ItemWriter<UserBookLoan> sendEmailRequestReturnWriter,
                                  JobRepository jobRepository) {
        return new StepBuilder("sendEmailUserStep", jobRepository)
                .<UserBookLoan, UserBookLoan>chunk(1, transactionManager)
                .reader(readUsersWithLoansCloseToReturnReader)
                .writer(sendEmailRequestReturnWriter)
                .build();
    }
}
