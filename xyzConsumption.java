package <repository>

import static oracle.jdbc.OracleType.DATE;
import static oracle.jdbc.OracleType.NUMBER;
import static oracle.jdbc.OracleType.VARCHAR2;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;



import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class myclassImpl implements myclassInterface {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value
    private class Tuple {
        private Object o;
        private SQLType sqlType;
    }

    private void bulkOperation(String sql, List<List<Tuple>> setObjects) throws BatchUpdateException {
        int[] resultCount = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement st, int i) throws SQLException {
                List<Tuple> object = setObjects.get(i);
                for (int j = 0, size = object.size(); j < size; j++) {
                    Tuple t = object.get(j);
                    st.setObject(j + 1, t.getO(), t.getSqlType());
                }
            }

            @Override
            public int getBatchSize() {
                return setObjects.size();
            }
        });
        
        if(resultCount.length != setObjects.size()) {
            log.error("Committed records are not matching with total records count {} :: {} - {} ", sql, resultCount.length, setObjects.size());
            throw new BatchUpdateException(sql , resultCount);
        }
    }

    @Override
    public void updateInBulk(Set<LineConsumption> uConsumptions) throws SQLException {

        String sql = "update <consumption table> set <field names to be updated> where ID=?";
        List<List<Tuple>> setObjects = new ArrayList<>(uConsumptions.size());
        uConsumptions.forEach(entry1 -> {
            List<Tuple> object = new ArrayList<>(20);
            object.add(new Tuple(entry1.getConsumption() == BigDecimal.ZERO ? null : entry1.getConsumption(), NUMBER));	
			<add all consumption data> 
            object.add(new Tuple(entry1.getId(), VARCHAR2));
            setObjects.add(object);
        });
        bulkOperation(sql, setObjects);
    }

    @Override
    public void insertInBulk(Set<LineConsumption> iConsumptions) throws SQLException {

        String sql = "insert /*+ APPEND */ into <sub consumption table> (<event_line fields>) )"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<List<Tuple>> setObjects = new ArrayList<>(iConsumptions.size());

        iConsumptions.forEach(entry1 -> {
            List<Tuple> object = new ArrayList<>(19);
            object.add(new Tuple(entry1. getConsumption() == BigDecimal.ZERO ? null : entry1. getConsumption(), NUMBER));
            object.add(new Tuple(entry1.getLineId(), NUMBER));
			<add all consumption data> 
            object.add(new Tuple(entry1.getId(), VARCHAR2));
            setObjects.add(object);
        });
        bulkOperation(sql, setObjects);
    }

    @Override
    public void insertInBulk(List<EventConsumptionMap> eventConsumptionMap) throws SQLException {

        String sql = "insert /*+ APPEND */ into <event lines table> ( <event_line fields>) "
                + "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<List<Tuple>> setObjects = new ArrayList<>(eventConsumptionMap.size());

        eventConsumptionMap.forEach(entry1 -> {
            List<Tuple> object = new ArrayList<>(10);
            object.add(new Tuple(entry1.getChargeAmount(), NUMBER));
 			<add all consumption data> 
            object.add(new Tuple(entry1.getId().getEventLogId(), NUMBER));
            setObjects.add(object);
        });
        bulkOperation(sql, setObjects);
    }
}

