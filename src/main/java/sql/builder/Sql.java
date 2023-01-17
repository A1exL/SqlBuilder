package sql.builder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static sql.builder.SqlHelper.*;

public class Sql {
    public static String offsetParameterName = "p_offset";
    public static String limitParameterName = "p_limit";
    private final String content;
    private final List<Sql> children;
    private Map<String, Object> parameters;

    public Sql() {
        this(null, new LinkedList<>());
    }

    public Sql(String content) {
        this(content, new LinkedList<>());
    }

    Sql(String content, List<Sql> children) {
        this.content = content;
        this.children = children;
    }

    public Sql select() {
        return append(SELECT);
    }

    public Sql distinct() {
        return append(DISTINCT);
    }

    public Sql select(String... columns) {
        return select().append(commaSeparated(columns));
    }

    public Sql select(Sql... sqlStatements) {
        return select().append(commaNewLineSeparated(sqlStatements));
    }

    public Sql selectDistinct() {
        return select().distinct();
    }

    public Sql selectDistinct(Sql... sqlStatements) {
        return selectDistinct().append(commaNewLineSeparated(sqlStatements));
    }

    public Sql selectDistinct(String... columns) {
        return selectDistinct().append(commaNewLineSeparated(columns));
    }

    public Sql param(String paramName, Object paramValue) {
        append(COLON).append(paramName);
        initParameters();
        parameters.put(paramName, paramValue);
        return this;
    }

    public Sql as() {
        return append(AS);
    }

    public Sql as(String alias) {
        return as(immutableSql(alias));
    }

    public Sql as(Sql sql) {
        return as().append(sql);
    }

    public Sql in(String sql) {
        return in(immutableSql(sql));
    }

    public Sql in(Sql sql) {
        return append(IN).append(brackets(sql));
    }

    public Sql groupBy(String ... sql) {
        return groupBy(commaSeparated(sql));
    }

    public Sql groupBy(Sql ... sql) {
        return append(GROUP_BY).append(commaSeparated(sql));
    }

    public Sql having(String sql) {
        return having(immutableSql(sql));
    }

    public Sql having(Sql sql) {
        return append(HAVING).append(sql);
    }

    public Sql append(Sql sql) {
        children.add(sql);
        return this;
    }

    public Sql append(String sql) {
        append(immutableSql(sql));
        return this;
    }

    public Sql from(String table) {
        return append(FROM).append(table);
    }

    public Sql from(Sql table) {
        return append(FROM).append(table);
    }

    public Sql where(String... conditions) {
        if (conditions.length > 0) {
            append(WHERE);
            append(concatenate(AND, conditions));
        }
        return this;
    }

    public Sql where(Sql... conditions) {
        if (conditions.length > 0) {
            append(WHERE);
            append(concatenate(AND, conditions));
        }
        return this;
    }

    public Sql where(Collection<Sql> conditions) {
        if (null != conditions && conditions.size() > 0) {
            append(WHERE);
            append(concatenate(AND, conditions.toArray(new Sql[0])));
        }
        return this;
    }

    public Sql and() {
        return append(AND);
    }

    public Sql and(String sql) {
        return and().append(sql);
    }

    public Sql and(Sql sql) {
        return and().append(sql);
    }

    public Sql or() {
        return append(OR);
    }

    public Sql or(String sql) {
        return or(immutableSql(sql));
    }

    public Sql or(Sql sql) {
        return or().append(sql);
    }

    public Sql equal(String sql) {
        return equal(immutableSql(sql));
    }

    public Sql equal(Sql sql) {
        return append(EQUAL).append(sql);
    }

    public Sql notEqual(String sql) {
        return notEqual(immutableSql(sql));
    }

    public Sql notEqual(Sql sql) {
        return append(NOT_EQUAL).append(sql);
    }

    public Sql not() {
        return append(NOT);
    }

    public Sql notLike(String value) {
        return notLike(quote(value));
    }

    public Sql notLike(Sql sql) {
        return append(NOT_LIKE).append(sql);
    }

    public Sql like(String value) {
        return like(quote(value));
    }
    public Sql like(Sql sql) {
        return append(LIKE).append(sql);
    }

    public Sql count(Sql sql) {
        return append(COUNT).append(brackets(sql));
    }
    public Sql count(String sql) {
        return count(immutableSql(sql));
    }

    public Sql min(String sql) {
        return min(immutableSql(sql));
    }
    public Sql min(Sql sql) {
        return append(MIN).append(brackets(sql));
    }

    public Sql max(String sql) {
        return max(immutableSql(sql));
    }
    public Sql max(Sql sql) {
        return append(MAX).append(brackets(sql));
    }

    public Sql sum(String sql) {
        return sum(immutableSql(sql));
    }
    public Sql sum(Sql sql) {
        return append(SUM).append(brackets(sql));
    }

    public Sql lessThan(String sql) {
        return lessThan(immutableSql(sql));
    }

    public Sql lessThan(Sql sql) {
        return append(LESS).append(sql);
    }

    public Sql greaterThan(String sql) {
        return greaterThan(immutableSql(sql));
    }

    public Sql greaterThan(Sql sql) {
        return append(GREATER).append(sql);
    }

    public Sql lessOrEqual(String sql) {
        return lessOrEqual(immutableSql(sql));
    }

    public Sql lessOrEqual(Sql sql) {
        return append(LESS_EQUAL).append(sql);
    }

    public Sql greaterOrEqual(String sql) {
        return greaterOrEqual(immutableSql(sql));
    }

    public Sql greaterOrEqual(Sql sql) {
        return append(GREATER_EQUAL).append(sql);
    }

    public Sql exists(String sql) {
        return exists(immutableSql(sql));
    }

    public Sql exists(Sql sql) {
        return append(EXISTS).append(brackets(sql));
    }

    public Sql notExists(String sql) {
        return notExists(immutableSql(sql));
    }

    public Sql notExists(Sql sql) {
        return append(NOT_EXISTS).append(brackets(sql));
    }

    public Sql join(String sql) {
        return join(immutableSql(sql));
    }

    public Sql join(Sql sql) {
        return append(JOIN).append(sql);
    }

    public Sql innerJoin(String sql) {
        return innerJoin(immutableSql(sql));
    }

    public Sql innerJoin(Sql sql) {
        return append(INNER_JOIN).append(sql);
    }

    public Sql leftJoin(String sql) {
        return leftJoin(immutableSql(sql));
    }

    public Sql leftJoin(Sql sql) {
        return append(LEFT_JOIN).append(sql);
    }

    public Sql rightJoin(String sql) {
        return rightJoin(immutableSql(sql));
    }

    public Sql rightJoin(Sql sql) {
        return append(RIGHT_JOIN).append(sql);
    }

    public Sql fullJoin(String sql) {
        return fullJoin(immutableSql(sql));
    }
    public Sql fullJoin(Sql sql) {
        return append(FULL_JOIN).append(sql);
    }

    public Sql on(String sql) {
        return on(immutableSql(sql));
    }

    public Sql on(Sql sql) {
        return append(ON).append(sql);
    }

    public Sql union() {
        return append(UNION);
    }
    public Sql union(String sql) {
        return union(immutableSql(sql));
    }
    public Sql union(Sql sql) {
        return union().append(sql);
    }

    public Sql unionAll() {
        return append(UNION_ALL);
    }
    public Sql unionAll(String sql) {
        return unionAll(immutableSql(sql));
    }
    public Sql unionAll(Sql sql) {
        return unionAll().append(sql);
    }

    public Sql offset(int offset) {
        return offset(new Sql().param(offsetParameterName, offset));
    }
    public Sql offset(Sql sql) {
        return append(OFFSET).append(sql);
    }

    public Sql limit(int limit) {
        return limit(new Sql().param(limitParameterName, limit));
    }
    public Sql limit(Sql sql) {
        return append(LIMIT).append(sql);
    }

    public Sql orderBy(String... columns) {
        return orderBy(commaSeparated(columns));
    }

    public Sql orderBy(Sql... columns) {
        return append(ORDER_BY).append(commaSeparated(columns));
    }

    public Sql desc() {
        return append(DESC);
    }

    public Sql asc() {
        return append(ASC);
    }

    public Sql with(String alias) {
        return append(WITH).append(alias);
    }

    public Sql with(Sql sql) {
        return append(WITH).append(sql);
    }

    public Sql withAs(String alias, Sql sql) {
        return with(alias).as(brackets(sql));
    }

    public Sql withAs(String firstAlias, Sql firstSql, String secondAlias, Sql secondSql) {
        return withAs(firstAlias, firstSql)
                .commaNewLine()
                .append(secondAlias).as(brackets(secondSql));
    }

    public Sql comma() {
        return append(COMMA);
    }

    public Sql commaNewLine() {
        return append(COMMA_NEW_LINE);
    }

    public Sql space() {
        return append(SPACE);
    }

    public Sql newLine() {
        return append(NEW_LINE);
    }

    public Sql isNull() {
        return append(IS_NULL);
    }

    public Sql isNotNull() {
        return append(IS_NOT_NULL);
    }

    public Map<String, Object> getParameters() {
        initParameters();
        for (Sql child : children) {
            collectParameters(child, parameters);
        }
        return parameters;
    }

    private void collectParameters(Sql sql, Map<String, Object> parentParameters) {
        if (null != sql.parameters) {
            parentParameters.putAll(sql.parameters);
        }
        for (Sql child : sql.children) {
            collectParameters(child, parentParameters);
        }
    }

    private void initParameters() {
        if (null == parameters) {
            parameters = new LinkedHashMap<>();
        }
    }

    private void render(StringBuilder stringBuilder) {
        if (null != content) {
            stringBuilder.append(content);
        }
        for (Sql child : children) {
            child.render(stringBuilder);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        render(result);
        return result.toString().trim();
    }
}
