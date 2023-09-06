package com.biblio.dao;

import com.biblio.app.model.Book;
import com.biblio.app.model.Category;

import java.sql.SQLException;
import java.util.Map;

public final class CategoryDao extends Category {

    public boolean save() throws SQLException {
        this.setId( Integer.parseInt(this.create(this.getCategory())) );
        return this.id > 0;
    }

    public Category read() {
        Map<String, String> Category = super.read(new String[]{String.valueOf(this.id)});

        this.setCategory(
                Integer.parseInt(Category.get("id")),
                Category.get("category"),
                Category.get("description")
        );

        return this;
    }

    public boolean update() {
        return super.update(this.getCategory(), new String[]{String.valueOf(this.id)});
    }

    public boolean delete() {
        return super.softDelete(new String[]{String.valueOf(this.id)});
    }



}
