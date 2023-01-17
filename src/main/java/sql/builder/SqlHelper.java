package sql.builder;

import java.util.Arrays;
import java.util.Collections;

public class SqlHelper {

    public static final Sql SELECT = immutableSql("\nSELECT ");
    public static final Sql DISTINCT = immutableSql("DISTINCT ");
    public static final Sql FROM = immutableSql("\nFROM ");

    public static final Sql JOIN = immutableSql("\nJOIN ");
    public static final Sql LEFT_JOIN = immutableSql("\nLEFT JOIN ");
    public static final Sql RIGHT_JOIN = immutableSql("\nRIGHT JOIN ");
    public static final Sql INNER_JOIN = immutableSql("\nINNER JOIN ");
    public static final Sql FULL_JOIN = immutableSql("\nFULL JOIN ");

    public static final Sql WHERE = immutableSql("\nWHERE ");
    public static final Sql GROUP_BY = immutableSql("\nGROUP BY ");
    public static final Sql HAVING = immutableSql("\nHAVING ");
    public static final Sql ORDER_BY = immutableSql("\nORDER BY ");

    public static final Sql AND = immutableSql("\nAND ");
    public static final Sql OR = immutableSql("\nOR ");
    public static final Sql AS = immutableSql(" AS ");
    public static final Sql IN = immutableSql(" IN ");
    public static final Sql ON = immutableSql(" ON ");
    public static final Sql EQUAL = immutableSql(" = ");
    public static final Sql NOT_EQUAL = immutableSql(" != ");
    public static final Sql LEFT_BRACKET = immutableSql("(");
    public static final Sql RIGHT_BRACKET = immutableSql(")");
    public static final Sql QUOTE = immutableSql("'");

    public static final Sql LIKE = immutableSql(" LIKE ");
    public static final Sql NOT_LIKE = immutableSql(" NOT LIKE ");

    public static final Sql UNION = immutableSql("\nUNION ");
    public static final Sql UNION_ALL = immutableSql("\nUNION ALL ");

    public static final Sql GREATER = immutableSql(" > ");
    public static final Sql LESS = immutableSql(" < ");
    public static final Sql GREATER_EQUAL = immutableSql(" >= ");
    public static final Sql LESS_EQUAL = immutableSql(" <= ");

    public static final Sql NOT = immutableSql(" NOT ");

    public static final Sql ASC = immutableSql(" ASC");
    public static final Sql DESC = immutableSql(" DESC");

    public static final Sql EXISTS = immutableSql("EXISTS\n");
    public static final Sql NOT_EXISTS = immutableSql("NOT EXISTS\n");

    public static final Sql IS_NULL = immutableSql(" IS NULL");
    public static final Sql IS_NOT_NULL = immutableSql(" IS NOT NULL");

    public static final Sql COUNT = immutableSql("COUNT");
    public static final Sql SUM = immutableSql("SUM");
    public static final Sql MIN = immutableSql("MIN");
    public static final Sql MAX = immutableSql("MAX");

    public static final Sql WITH = immutableSql("WITH ");

    public static final Sql OFFSET = immutableSql(" OFFSET ");
    public static final Sql LIMIT = immutableSql(" LIMIT ");

    public static final Sql SPACE = immutableSql(" ");
    public static final Sql COLON = immutableSql(":");
    public static final Sql COMMA = immutableSql(", ");
    public static final Sql NEW_LINE = immutableSql("\n");
    public static final Sql COMMA_NEW_LINE = immutableSql(",\n");

    public static Sql sql(String content) {
        return new Sql(content);
    }

    public static Sql sql(Object content) {
        return new Sql(content.toString());
    }

    static Sql immutableSql(String content) {
        return new Sql(content, Collections.emptyList());
    }

    public static Sql brackets(String content) {
        return brackets(sql(content));
    }

    public static Sql brackets(Sql sql) {
        return new Sql(null)
                .append(LEFT_BRACKET)
                .append(sql)
                .append(RIGHT_BRACKET);
    }

    public static Sql quote(String sql) {
        return quote(immutableSql(sql));
    }

    public static Sql quote(Sql sql) {
        return new Sql(null)
                .append(QUOTE)
                .append(sql)
                .append(QUOTE);
    }

    public static Sql commaSeparated(String... sqlStrings) {
        return concatenate(COMMA, sqlStrings);
    }

    public static Sql commaSeparated(Sql... sqlFragments) {
        return concatenate(COMMA, sqlFragments);
    }

    public static Sql commaNewLineSeparated(String... sqlStrings) {
        return concatenate(COMMA_NEW_LINE, sqlStrings);
    }

    public static Sql commaNewLineSeparated(Sql... sqlFragments) {
        return concatenate(COMMA_NEW_LINE, sqlFragments);
    }

    public static Sql select() {
        return new Sql().select();
    }

    public static Sql select(String... columns) {
        return new Sql().select(columns);
    }

    public static Sql param(String paramName, Object paramValue) {
        return new Sql().param(paramName, paramValue);
    }

    public static Sql asc(String column) {
        return new Sql(column).asc();
    }
    public static Sql desc(String column) {
        return new Sql(column).desc();
    }

    public static Sql exists(Sql sql) {
        return new Sql().exists(sql);
    }
    public static Sql exists(String sql) {
        return exists(immutableSql(sql));
    }
    public static Sql notExists(Sql sql) {
        return new Sql().notExists(sql);
    }
    public static Sql notExists(String sql) {
        return notExists(immutableSql(sql));
    }

    public static Sql min(String sql) {
        return new Sql().min(sql);
    }
    public static Sql min(Sql sql) {
        return new Sql().min(sql);
    }

    public static Sql max(String sql) {
        return new Sql().max(sql);
    }
    public static Sql max(Sql sql) {
        return new Sql().max(sql);
    }

    public static Sql sum(String sql) {
        return new Sql().sum(sql);
    }
    public static Sql sum(Sql sql) {
        return new Sql().sum(sql);
    }

    public static Sql count(String sql) {
        return new Sql().count(sql);
    }
    public static Sql count(Sql sql) {
        return new Sql().count(sql);
    }

    public static Sql concatenate(Sql separator, String... sqlFragments) {
        Sql[] sqlArray = Arrays.stream(sqlFragments)
                .map(SqlHelper::immutableSql)
                .toArray(Sql[]::new);
        return concatenate(separator, sqlArray);
    }
    public static Sql concatenate(Sql separator, Sql... sqlFragments) {
        Sql result = new Sql(null);
        if (sqlFragments.length == 0) {
            return result;
        } else if (sqlFragments.length == 1) {
            return sqlFragments[0];
        }

        boolean isFirst = true;
        for (Sql sql : sqlFragments) {
            if (!isFirst) {
                result.append(separator);
            }
            result.append(sql);
            isFirst = false;
        }
        return result;
    }
}
