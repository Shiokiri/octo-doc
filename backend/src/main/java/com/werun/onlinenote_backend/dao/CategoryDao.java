package com.werun.onlinenote_backend.dao;

import com.werun.onlinenote_backend.entity.Category;
import com.werun.onlinenote_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName CategoryDao
 * @Description CategoryDao
 * @Create 2022-03-24
 * @Update 2022-04-17
 **/
public interface CategoryDao extends JpaRepository<Category, String> {
    Category findCategoryByCidAndUser(String cid, User user);
    Category findCategoryByCategoryNameAndUser(String categoryName, User user);
    List<Category> findByUser(User user);
}
