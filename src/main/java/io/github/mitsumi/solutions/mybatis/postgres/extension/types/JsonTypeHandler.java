package io.github.mitsumi.solutions.mybatis.postgres.extension.types;

import io.github.mitsumi.solutions.spring.json.Jsons;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

@SuppressWarnings("PMD.CommentRequired")
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Jsons jsons;
    private final Class<T> type;

    public JsonTypeHandler(final Class<T> type) {
        super();

        this.jsons = new Jsons();
        this.type = type;
    }

    @Override
    public void setNonNullParameter(final PreparedStatement statement,
                                    final int index,
                                    final T parameter,
                                    final JdbcType jdbcType) throws SQLException {
        statement.setObject(index, jsons.serialize(parameter, SQLException::new), Types.OTHER);
    }

    @Override
    public T getNullableResult(final ResultSet resultSet, final String columnName) throws SQLException {
        return toJavaTypeObject(resultSet.getObject(columnName));
    }

    @Override
    public T getNullableResult(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return toJavaTypeObject(resultSet.getObject(columnIndex));
    }

    @Override
    public T getNullableResult(final CallableStatement statement, final int columnIndex) throws SQLException {
        return toJavaTypeObject(statement.getObject(columnIndex));
    }

    protected T toJavaTypeObject(final Object value) throws SQLException {
        return isEmpty(value) ? null : jsons.deserialize(value.toString(), type, SQLException::new);
    }

    protected boolean isEmpty(final Object value) {
        return value == null || StringUtils.isEmpty(value.toString());
    }
}
