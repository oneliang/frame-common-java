package com.oneliang.test.orm;

import com.oneliang.test.orm.Entity.Column;

@Entity
public class User {

    @Column(isId = true)
    private String id = null;
    @Column
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
