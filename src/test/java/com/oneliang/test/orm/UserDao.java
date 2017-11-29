package com.oneliang.test.orm;

import java.util.List;

import com.oneliang.test.orm.Dao;
import com.oneliang.test.orm.Dao.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM {User}")
    public List<User> getAll(List<String> ids);
}
