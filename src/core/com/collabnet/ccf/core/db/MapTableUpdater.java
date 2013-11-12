/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.auxil.connector.jdbc.writer.map.MapTableWriter;

public class MapTableUpdater extends MapTableWriter {
    private String           sql = null;

    /**
     * log4j logger instance
     */
    private static final Log log = LogFactory.getLog(MapTableUpdater.class);

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    protected PreparedStatement generatePreparedStatement(
            Connection connection, String tableName, String[] columnNames)
            throws SQLException {
        return connection.prepareStatement(sql);
    }

    protected void initialiseReusablePreparedStatement() {
        try {
            reusablePreparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            log.error("While creating an prepared statement, an error occured: "
                    + e.getMessage());
        }
        return;
    }
}
