package com.biblio.dao;

import com.biblio.app.Models.Author;
import com.biblio.app.Models.Category;
import com.biblio.libs.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CategoryDao extends Model {

    private Category category = null;

    public CategoryDao() {
        super("categories", new String[]{"id"});

        this.category = new Category();
    }

    public boolean save() throws SQLException {
        this.category.setId( Integer.parseInt(this.create(this.category.getCategory())) );
        return this.category.getId() > 0;
    }

    public Category read() {
        Map<String, String> Category = super.read(new String[]{String.valueOf(this.category.getId())});

        this.category.setCategory(
                Integer.parseInt(Category.get("id")),
                Category.get("category"),
                Category.get("description")
        );

        return this.category;
    }

    public boolean update() {
        return super.update(this.category.getCategory(), new String[]{String.valueOf(this.category.getId())});
    }

    public boolean delete() {
        return super.softDelete(new String[]{String.valueOf(this.category.getId())});
    }

    public String[] getAllCategories() {
        List<Map<String, String>> resultList = super.getAll();
        String[] categories = new String[resultList.size()];

        for (int i = 0; i < resultList.size(); i++) {
            Map<String, String> rowData = resultList.get(i);
            int id = Integer.parseInt(rowData.get("id"));
            String categoryName = rowData.get("category");

            categories[i] = String.valueOf(id) + " ," + categoryName;
        }

        return categories;
    }


}
