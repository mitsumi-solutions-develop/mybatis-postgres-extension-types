package io.github.mitsumi.solutions.mybatis.postgres.extension.types;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.UUID;

@SuppressWarnings("PMD.CommentRequired")
@RequiredArgsConstructor
public class UUIDTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(final PreparedStatement statement,
                                    final int index,
                                    final UUID parameter,
                                    final JdbcType jdbcType) throws SQLException {
        statement.setObject(index, UUID.fromString(parameter.toString()), Types.OTHER);
    }

    @Override
    public UUID getNullableResult(final ResultSet resultSet, final String columnName) throws SQLException {
        return convertToUUID(resultSet.getString(columnName));
    }

    @Override
    public UUID getNullableResult(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return convertToUUID(resultSet.getString(columnIndex));
    }

    @Override
    public UUID getNullableResult(final CallableStatement statement, final int columnIndex) throws SQLException {
        return convertToUUID(statement.getString(columnIndex));
    }

    protected UUID convertToUUID(final String value) throws SQLException {
        try {
            return StringUtils.isEmpty(value) ? null : UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        }
    }
}
