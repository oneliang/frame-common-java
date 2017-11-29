package com.oneliang.test.orm;

import com.oneliang.Constant;
import com.oneliang.test.orm.Entity.Column;

@Entity(table = "t_user")
public class User {

    @Column(isId = true, condition = { @Column.Condition(key = Constant.Database.MYSQL, value = "varchar(40) NOT NULL DEFAULT ''") })
    private String id = null;
    @Column(condition = { @Column.Condition(key = Constant.Database.MYSQL, value = "varchar(40) DEFAULT NULL") })
    private String name = null;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
