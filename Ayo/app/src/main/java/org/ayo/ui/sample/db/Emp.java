package org.ayo.ui.sample.db;


import org.ayo.db.db.annotation.Column;
import org.ayo.db.db.annotation.Table;

/**
 * Created by Administrator on 2016/1/26.
 */
@Table(name="emp")
public class Emp {

    @Column(isId = true, autoGen = true, name = "id")
    public int id;

    @Column(name = "ename")
    public String name = "name-" + System.currentTimeMillis()%100;

    @Column(name = "sex")
    public boolean sex = true;

    public String uiTitle = "name1";






}

