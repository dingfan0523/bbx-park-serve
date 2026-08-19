package com.cgnpc.bbxpark.common.config;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(value = { List.class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonStringArrayTypeHandler extends BaseTypeHandler<List<String>> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String reString = rs.getString(columnName);
        return parseJsonArray(reString);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String reString = rs.getString(columnIndex);
        return parseJsonArray(reString);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String reString = cs.getString(columnIndex);
        return parseJsonArray(reString);
    }

    private List<String> parseJsonArray(String reString) {
        if (reString == null || reString.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseArray(reString, String.class);
        } catch (Exception e) {
            // 记录日志或抛出自定义异常
            throw new RuntimeException("Failed to parse JSON array", e);
        }
    }
}
