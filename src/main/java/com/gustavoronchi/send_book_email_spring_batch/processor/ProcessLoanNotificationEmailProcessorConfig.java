package com.gustavoronchi.send_book_email_spring_batch.processor;

import com.gustavoronchi.send_book_email_spring_batch.domain.UserBookLoan;
import com.gustavoronchi.send_book_email_spring_batch.util.GenerateBookReturnDate;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Configuration
public class ProcessLoanNotificationEmailProcessorConfig {

    @Bean
    public ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor() {
        return new ItemProcessor<UserBookLoan, Mail>() {
            @Nullable
            @Override
            public Mail process(@NonNull UserBookLoan loan) throws Exception {
                Email from = new Email("gustavoronchi456@gmail.com", "Biblioteca Municipal");
                Email to = new Email(loan.getUser().getEmail());
                Content content = new Content("text/plain", generateEmailText(loan));
                Mail mail = new Mail(from, "Notificação devolução livro", to, content);
                Thread.sleep(1000);
                return mail;
            }

            private String generateEmailText(UserBookLoan loan) {
                return String.format("Prezado(a), %s, matricula %d\n", loan.getUser().getName(), loan.getUser().getId()) +
                        String.format("Informamos que o prazo de devolução do livro %s é amanhã (%s) \n", loan.getBook().getName(), GenerateBookReturnDate.getDate(loan.getLoanDate())) +
                        "Solicitamos que você renove o livro ou devolva, assim que possível.\n" +
                        "A Biblioteca Municipal está funcionando de segunda a sexta, das 9h às 17h.\n\n" +
                        "Atenciosamente,\n" +
                        "Setor de empréstimo e devolução\n" +
                        "BIBLIOTECA MUNICIPAL";
            }
        };
    }
}
