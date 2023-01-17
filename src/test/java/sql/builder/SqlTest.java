package sql.builder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static sql.builder.SqlHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;


class SqlTest {

    @Test
    void and() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("1 = 1"))
                .and().append("c2 = c3")
                .and("c3 = c4")
                .and(sql("c4 = c5"));

        String expected = "SELECT c1 FROM t1 WHERE 1 = 1 AND c2 = c3 AND c3 = c4 AND c4 = c5";
        assertSql(expected, sql);
    }

    @Test
    void or() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("1 = 1"))
                .or().append("c2 = c3")
                .or("c3 = c4")
                .or(sql("c4 = c5"));

        String expected = "SELECT c1 FROM t1 WHERE 1 = 1 OR c2 = c3 OR c3 = c4 OR c4 = c5";
        assertSql(expected, sql);
    }

    @Test
    void ascDesc() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .orderBy(asc("c1"), desc("c2"));

        String expected = "SELECT c1 FROM t1 ORDER BY c1 ASC, c2 DESC";
        assertSql(expected, sql);
    }

    @Test
    void count() {
        Sql sql = new Sql().select(SqlHelper.count("c1"), SqlHelper.count(sql("c2")))
                .from(sql("t1"))
                .groupBy("c3");

        String expected = "SELECT COUNT(c1), COUNT(c2) FROM t1 GROUP BY c3";
        assertSql(expected, sql);
    }

    @Test
    void minMaxStrings() {
        Sql sql = new Sql().select(min("c1"), max(("c2")))
                .from("t1")
                .groupBy("c3");

        String expected = "SELECT MIN(c1), MAX(c2) FROM t1 GROUP BY c3";
        assertSql(expected, sql);
    }

    @Test
    void minMaxSql() {
        Sql minStatement = new Sql("c1");
        Sql maxStatement = new Sql("c2");
        Sql sql = new Sql().select(min(minStatement), max((maxStatement)))
                .from("t1")
                .groupBy("c3");

        String expected = "SELECT MIN(c1), MAX(c2) FROM t1 GROUP BY c3";
        assertSql(expected, sql);
    }

    @Test
    void equal() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").equal("c3"))
                .and(sql("c4").equal(quote("x")));

        String expected = "SELECT c1 FROM t1 WHERE c1 = c3 AND c4 = 'x'";
        assertSql(expected, sql);
    }

    @Test
    void notEqual() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").notEqual("c3"))
                .and(sql("c4").notEqual(quote("x")));

        String expected = "SELECT c1 FROM t1 WHERE c1 != c3 AND c4 != 'x'";
        assertSql(expected, sql);
    }

    @Test
    void greaterThan() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").greaterThan("c3"))
                .and(sql("c4").greaterThan(sql(42)));

        String expected = "SELECT c1 FROM t1 WHERE c1 > c3 AND c4 > 42";
        assertSql(expected, sql);
    }

    @Test
    void lessThan() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").lessThan("c3"))
                .and(sql("c4").lessThan(sql(42)));

        String expected = "SELECT c1 FROM t1 WHERE c1 < c3 AND c4 < 42";
        assertSql(expected, sql);
    }

    @Test
    void greaterOrEqual() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").greaterOrEqual("c3"))
                .and(sql("c4").greaterOrEqual(sql(42)));

        String expected = "SELECT c1 FROM t1 WHERE c1 >= c3 AND c4 >= 42";
        assertSql(expected, sql);
    }

    @Test
    void lessOrEqual() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").lessOrEqual("c3"))
                .and(sql("c4").lessOrEqual(sql(42)));

        String expected = "SELECT c1 FROM t1 WHERE c1 <= c3 AND c4 <= 42";
        assertSql(expected, sql);
    }

    @Test
    void like() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").like(sql("'%A%'")))
                .and(sql("c2").like("%B%"));

        String expected = "SELECT c1 FROM t1 WHERE c1 LIKE '%A%' AND c2 LIKE '%B%'";
        assertSql(expected, sql);
    }

    @Test
    void notLike() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").notLike(sql("'%A%'")))
                .and(sql("c2").notLike("%B%"));

        String expected = "SELECT c1 FROM t1 WHERE c1 NOT LIKE '%A%' AND c2 NOT LIKE '%B%'";
        assertSql(expected, sql);
    }

    @Test
    void nullNotNull() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(sql("c1").isNull())
                .and(sql("c2").isNotNull());

        String expected = "SELECT c1 FROM t1 WHERE c1 IS NULL AND c2 IS NOT NULL";
        assertSql(expected, sql);
    }

    @Test
    void in() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where("c1").in("1, 2, 3")
                .or("c2").in(param("ids", Arrays.asList(1, 2, 3)));

        String expected = "SELECT c1 FROM t1 WHERE c1 IN (1, 2, 3) OR c2 IN (:ids)";
        assertSql(expected, sql);
    }

    @Test
    void exists() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(SqlHelper.exists("select id from t2"))
                .and()
                .exists("select id2 from t3");

        String expected = "SELECT c1 FROM t1 WHERE EXISTS (select id from t2) AND EXISTS (select id2 from t3)";
        assertSql(expected, sql);
    }

    @Test
    void notExists() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .where(SqlHelper.notExists("select id from t2"))
                .or()
                .notExists("select id2 from t3");

        String expected = "SELECT c1 FROM t1 WHERE NOT EXISTS (select id from t2) OR NOT EXISTS (select id2 from t3)";
        assertSql(expected, sql);
    }

    @Test
    void offsetLimitParameters() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .offset(20)
                .limit(10);

        String expected = "SELECT c1 FROM t1 OFFSET :p_offset LIMIT :p_limit";
        assertSql(expected, sql);
        Map<String, Object> parameters = sql.getParameters();
        assertThat(parameters).containsEntry(Sql.offsetParameterName, 20);
        assertThat(parameters).containsEntry(Sql.limitParameterName, 10);
    }

    @Test
    void offsetLimitSql() {
        Sql sql = new Sql().select("c1")
                .from("t1")
                .offset(sql(Integer.toString(20)))
                .limit(sql(Integer.toString(10)));

        String expected = "SELECT c1 FROM t1 OFFSET 20 LIMIT 10";
        assertSql(expected, sql);
    }

    @Test
    void join() {
        Sql sql = new Sql().select("c1").from("t1")
                .join("t2 on t2.c2 = t1.c1")
                .join(sql("t3")).on("t3.c3 = t2.c2")
                .join(sql("t4").on(sql("t4.c4").equal("t3.c3")));

        String expected = "SELECT c1 FROM t1 " +
                "JOIN t2 on t2.c2 = t1.c1 " +
                "JOIN t3 ON t3.c3 = t2.c2 " +
                "JOIN t4 ON t4.c4 = t3.c3";
        assertSql(expected, sql);
    }

    @Test
    void innerJoin() {
        Sql sql = new Sql().select("c1").from("t1")
                .innerJoin("t2 on t2.c2 = t1.c1")
                .innerJoin(sql("t3")).on("t3.c3 = t2.c2")
                .innerJoin(sql("t4").on(sql("t4.c4").equal("t3.c3")));

        String expected = "SELECT c1 FROM t1 " +
                "INNER JOIN t2 on t2.c2 = t1.c1 " +
                "INNER JOIN t3 ON t3.c3 = t2.c2 " +
                "INNER JOIN t4 ON t4.c4 = t3.c3";
        assertSql(expected, sql);
    }

    @Test
    void leftJoin() {
        Sql sql = new Sql().select("c1").from("t1")
                .leftJoin("t2 on t2.c2 = t1.c1")
                .leftJoin(sql("t3")).on("t3.c3 = t2.c2")
                .leftJoin(sql("t4").on(sql("t4.c4").equal("t3.c3")));

        String expected = "SELECT c1 FROM t1 " +
                "LEFT JOIN t2 on t2.c2 = t1.c1 " +
                "LEFT JOIN t3 ON t3.c3 = t2.c2 " +
                "LEFT JOIN t4 ON t4.c4 = t3.c3";
        assertSql(expected, sql);
    }

    @Test
    void rightJoin() {
        Sql sql = new Sql().select("c1").from("t1")
                .rightJoin("t2 on t2.c2 = t1.c1")
                .rightJoin(sql("t3")).on("t3.c3 = t2.c2")
                .rightJoin(sql("t4").on(sql("t4.c4").equal("t3.c3")));

        String expected = "SELECT c1 FROM t1 " +
                "RIGHT JOIN t2 on t2.c2 = t1.c1 " +
                "RIGHT JOIN t3 ON t3.c3 = t2.c2 " +
                "RIGHT JOIN t4 ON t4.c4 = t3.c3";
        assertSql(expected, sql);
    }

    @Test
    void fullJoin() {
        Sql sql = new Sql().select("c1").from("t1")
                .fullJoin("t2 on t2.c2 = t1.c1")
                .fullJoin(sql("t3")).on("t3.c3 = t2.c2")
                .fullJoin(sql("t4").on(sql("t4.c4").equal("t3.c3")));

        String expected = "SELECT c1 FROM t1 " +
                "FULL JOIN t2 on t2.c2 = t1.c1 " +
                "FULL JOIN t3 ON t3.c3 = t2.c2 " +
                "FULL JOIN t4 ON t4.c4 = t3.c3";
        assertSql(expected, sql);
    }

    @Test
    void withString() {
        Sql sql = new Sql()
                .with("string_cte")
                .as(brackets(new Sql().select("id").from("t1")))
                .select("*")
                .from("string_cte");

        String expected = "WITH string_cte AS ( SELECT id FROM t1) SELECT * FROM string_cte";
        assertSql(expected, sql);
    }

    @Test
    void withSql() {
        Sql sql = new Sql()
                .with(new Sql("string_cte"))
                .as(brackets(new Sql().select("id").from("t1")))
                .select("*")
                .from("string_cte");

        String expected = "WITH string_cte AS ( SELECT id FROM t1) SELECT * FROM string_cte";
        assertSql(expected, sql);
    }

    @Test
    void withAs() {
        Sql sql = new Sql()
                .withAs("string_cte", new Sql().select("id").from("t1"))
                .select("*")
                .from("sql_cte");

        String expected = "WITH string_cte AS ( SELECT id FROM t1) SELECT * FROM sql_cte";
        assertSql(expected, sql);
    }

    @Test
    void withAsMultiple() {
        Sql cte1 = new Sql().select("id").from("t1");
        Sql cte2 = sql("select * from cte1");
        Sql sql = new Sql()
                .withAs("cte1", cte1, "cte2", cte2)
                .select("*")
                .from("cte2");

        String expected = "WITH cte1 AS ( SELECT id FROM t1), cte2 AS (select * from cte1) SELECT * FROM cte2";
        assertSql(expected, sql);
    }

    @Test
    void selectDistinct() {
        Sql sql = new Sql()
                .selectDistinct().append("c1")
                .from("t1");

        String expected = "SELECT DISTINCT c1 FROM t1";
        assertSql(expected, sql);
    }

    @Test
    void selectDistinctString() {
        Sql sql = new Sql()
                .selectDistinct("c1", "c2", "c3")
                .from("t1");

        String expected = "SELECT DISTINCT c1, c2, c3 FROM t1";
        assertSql(expected, sql);
    }

    @Test
    void selectDistinctSql() {
        Sql sql = new Sql()
                .selectDistinct(sql("c1"), sql("c2"), sql("c3"))
                .from("t1");

        String expected = "SELECT DISTINCT c1, c2, c3 FROM t1";
        assertSql(expected, sql);
    }

    @Test
    void whereEmpty() {
        Collection<Sql> emptyWhereConditions = Collections.emptyList();
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .where(emptyWhereConditions)
                .where(new String[0])
                .where(new Sql[0]);

        String expected = "SELECT c1 FROM t1";
        assertSql(expected, sql);
    }

    @Test
    void whereAsCollection() {
        Collection<Sql> whereConditions = Arrays.asList(sql("c1 = c2"), sql("c3 = c4"));
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .where(whereConditions);

        String expected = "SELECT c1 FROM t1 WHERE c1 = c2 AND c3 = c4";
        assertSql(expected, sql);
    }

    @Test
    void whereAsSql() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .where(sql("c1 = c2"), sql("c3 = c4"));

        String expected = "SELECT c1 FROM t1 WHERE c1 = c2 AND c3 = c4";
        assertSql(expected, sql);
    }

    @Test
    void whereAsString() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .where("c1 = c2", "c3 = c4");

        String expected = "SELECT c1 FROM t1 WHERE c1 = c2 AND c3 = c4";
        assertSql(expected, sql);
    }

    @Test
    void groupBy1StringColumn() {
        Sql sql = new Sql()
                .select("dep_id", "sum(salary)")
                .from("employees")
                .groupBy("dep_id");

        String expected = "SELECT dep_id, sum(salary) FROM employees GROUP BY dep_id";
        assertSql(expected, sql);
    }

    @Test
    void groupBy1SqlColumn() {
        Sql sql = new Sql()
                .select(sql("dep_id"), sum("salary"))
                .from("employees")
                .groupBy(sql("dep_id"));

        String expected = "SELECT dep_id, SUM(salary) FROM employees GROUP BY dep_id";
        assertSql(expected, sql);
    }

    @Test
    void groupBy2StringColumns() {
        Sql sql = new Sql()
                .select(commaSeparated("dep_id", "office_id"), sum("salary"))
                .from("employees")
                .groupBy("dep_id", "office_id");

        String expected = "SELECT dep_id, office_id, SUM(salary) FROM employees GROUP BY dep_id, office_id";
        assertSql(expected, sql);
    }

    @Test
    void groupBy2SqlColumns() {
        Sql groupColumns = commaSeparated("dep_id", "office_id");
        Sql sql = new Sql()
                .select(groupColumns, sum("salary"))
                .from("employees")
                .groupBy(sql("dep_id"), sql("office_id"));

        String expected = "SELECT dep_id, office_id, SUM(salary) FROM employees GROUP BY dep_id, office_id";
        assertSql(expected, sql);
    }

    @Test
    void havingString() {
        Sql sql = new Sql()
                .select("dep_id", "sum(salary)")
                .from("employees")
                .groupBy("dep_id")
                .having("SUM(salary)").greaterThan("1000");

        String expected = "SELECT dep_id, sum(salary) FROM employees GROUP BY dep_id HAVING SUM(salary) > 1000";
        assertSql(expected, sql);
    }

    @Test
    void havingSql() {
        Sql sql = new Sql()
                .select("dep_id", "sum(salary)")
                .from("employees")
                .groupBy("dep_id")
                .having(sum("salary").greaterThan("1000"));

        String expected = "SELECT dep_id, sum(salary) FROM employees GROUP BY dep_id HAVING SUM(salary) > 1000";
        assertSql(expected, sql);
    }

    @Test
    void orderBy1StringColumn() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .orderBy("c1");

        String expected = "SELECT c1 FROM t1 ORDER BY c1";
        assertSql(expected, sql);
    }

    @Test
    void orderBy1SqlColumn() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .orderBy(sql("c1"));

        String expected = "SELECT c1 FROM t1 ORDER BY c1";
        assertSql(expected, sql);
    }

    @Test
    void orderBy2StringColumns() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .orderBy("c1", "c2");

        String expected = "SELECT c1 FROM t1 ORDER BY c1, c2";
        assertSql(expected, sql);
    }

    @Test
    void orderBy2SqlColumns() {
        Sql sql = new Sql()
                .select("c1")
                .from("t1")
                .orderBy(sql("c1"), sql("c2"));

        String expected = "SELECT c1 FROM t1 ORDER BY c1, c2";
        assertSql(expected, sql);
    }

    @Test
    void union() {
        Sql sql = new Sql()
                .select("c1").from("t1")
                .union()
                .select(sql("c2").from("t2"))
                .union(select("c3").from("t3"))
                .union("select c4 from t4");

        String expected = "SELECT c1 FROM t1 UNION  SELECT c2 FROM t2 UNION  SELECT c3 FROM t3 UNION select c4 from t4";
        assertSql(expected, sql);
    }

    @Test
    void unionAll() {
        Sql sql = new Sql()
                .select("c1").from("t1")
                .unionAll()
                .select(sql("c2").from("t2"))
                .unionAll(select("c3").from("t3"))
                .unionAll("select c4 from t4");

        String expected = "SELECT c1 FROM t1 UNION ALL  SELECT c2 FROM t2 UNION ALL " +
                " SELECT c3 FROM t3 UNION ALL select c4 from t4";
        assertSql(expected, sql);
    }

    @Test
    void punctuation() {
        Sql sql = new Sql()
                .append("|")
                .comma()
                .append("|")
                .space()
                .append("|")
                .commaNewLine()
                .append("|")
                .newLine()
                .append("|");

        String expected = "|, | |,\n|\n|";
        assertEquals(expected, sql.toString());
    }

    private void assertSql(String expected, Sql actual) {
        String actualString = actual.toString().replace('\n', ' ');
        assertEquals(expected, actualString);
    }
}
