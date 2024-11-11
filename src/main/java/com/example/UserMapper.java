package com.example;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserMapper implements RowMapper<ImmutableUser> {
    @Override
    public ImmutableUser map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ImmutableUser.builder()
                .id(Optional.ofNullable(rs.getString("id")))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .build();
    }
}
