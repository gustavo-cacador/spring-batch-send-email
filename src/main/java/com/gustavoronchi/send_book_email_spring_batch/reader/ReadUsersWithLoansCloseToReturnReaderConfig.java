package com.gustavoronchi.send_book_email_spring_batch.reader;

import com.gustavoronchi.send_book_email_spring_batch.domain.Book;
import com.gustavoronchi.send_book_email_spring_batch.domain.User;
import com.gustavoronchi.send_book_email_spring_batch.domain.UserBookLoan;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ReadUsersWithLoansCloseToReturnReaderConfig {

    int numDaysToNotifyReturn = 6;

    @Bean
    public ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader(
            @Qualifier("appDB")DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<UserBookLoan>()
                .name("readUsersWithLoansCloseToReturnReader")
                .dataSource(dataSource)
                .sql("""
                        select user.id as user_id,
                            user.name as user_name,
                            user.email as user_email,
                            book.id as book_id,
                            book.name as book_name,
                            loan.loan_date
                        from tb_user_book_loan as loan
                        inner join tb_user as user on loan.user_id = user.id
                        inner join tb_book as book on loan.book_id = book.id
                        where date_add(loan.loan_date, interval ? day) = '2023-02-04'
                        """)
                .queryArguments(numDaysToNotifyReturn)
                .rowMapper(rowMapper())
                .build();
    }

    private RowMapper<UserBookLoan> rowMapper() {
        return new RowMapper<UserBookLoan>() {
            @NonNull
            @Override
            public UserBookLoan mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("user_email"));
                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setName(rs.getString("book_name"));
                return new UserBookLoan(user, book, rs.getDate("loan_date"));
            }
        };
    }
}

