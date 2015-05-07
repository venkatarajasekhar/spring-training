/**
 * Copyright (c) 2015 Innominds. All Rights Reserved.
 */
package com.innominds.spring.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.innominds.spring.beans.Organization;
import com.innominds.spring.dao.OrganizationDao;

/**
 * OrganizationDaoImpl.java
 *
 * @author ThirupathiReddy V
 *
 */
@Repository("organizationDao")
@Transactional
public class OrganizationDaoImpl extends JdbcDaoSupport implements OrganizationDao {

    //

    @Autowired
    DataSource dataSource;

    /**
     *
     */
    @PostConstruct
    public void initializeDB() {

        setDataSource(dataSource);// you need to call this method to make available this object in the super class level.

        getJdbcTemplate().execute(
                "CREATE TABLE IF NOT EXISTS `organization` (" + "`id` int(11) NOT NULL AUTO_INCREMENT," + "`name` varchar(45) NOT NULL,"
                        + "`address` varchar(45) DEFAULT NULL," + "PRIMARY KEY (`id`)" + ")");
    }

    /**
     * @return list
     */
    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {

        final List<Organization> organizations = getJdbcTemplate().query("SELECT * FROM Organization", new OrganizationRowMapper());

        return organizations;
    }

    /**
     * @param organization
     */
    @Transactional(readOnly = true)
    @Override
    public void save(Organization organization) {

        final String SQL = "INSERT INTO ORGANIZATION(name,address)  VALUES(?,?)";
        getJdbcTemplate().update(SQL, new Object[] { organization.getName(), organization.getAddress() });
    }

    /**
     * you can specify noRollbackFor
     *
     * @param organization
     */
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public void save2(Organization organization) {
        final String SQL = "INSERT INTO ORGANIZATION(name,address)  VALUES(?,?)";
        getJdbcTemplate().update(SQL, new Object[] { organization.getName(), organization.getAddress() });
    }

}

class OrganizationRowMapper implements RowMapper<Organization> {

    @Override
    public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Organization organization = new Organization();
        organization.setAddress(rs.getString("address"));
        organization.setName(rs.getString("name"));
        return organization;
    }
    //
}
