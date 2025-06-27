package io.github.mitsumi.solutions.mybatis.postgres.extension.types;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

@SuppressWarnings("PMD.CommentRequired")
@RequiredArgsConstructor
public class EnumTypeHandler<T extends Enum<T>> extends BaseTypeHandler<T> {

    private final Class<T> type;

    @Override
    public void setNonNullParameter(final PreparedStatement statement,
                                    final int index,
                                    final T parameter,
                                    final JdbcType jdbcType) throws SQLException {
        statement.setObject(index, parameter.name(), Types.OTHER);
    }

    @Override
    public T getNullableResult(final ResultSet resultSet, final String columnName) throws SQLException {
        return convertToEnum(resultSet.getString(columnName));
    }

    @Override
    public T getNullableResult(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return convertToEnum(resultSet.getString(columnIndex));
    }

    @Override
    public T getNullableResult(final CallableStatement statement, final int columnIndex) throws SQLException {
        return convertToEnum(statement.getString(columnIndex));
    }

    protected T convertToEnum(final String name) throws SQLException {
        try {
            return StringUtils.isEmpty(name) ? null : Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        }
    }
}
