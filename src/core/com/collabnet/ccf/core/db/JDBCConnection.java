package com.collabnet.ccf.core.db;

import com.collabnet.ccf.core.utils.Obfuscator;

public class JDBCConnection extends org.openadaptor.auxil.connector.jdbc.JDBCConnection {
    @Override
    public void setPassword(String password) {
        super.setPassword(Obfuscator.deObfuscatePassword(password));
    }
}
