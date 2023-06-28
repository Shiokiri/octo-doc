package com.werun.onlinenote_backend.service;

import com.werun.onlinenote_backend.bean.CategoryBean;
import com.werun.onlinenote_backend.bean.UserBean;
import com.werun.onlinenote_backend.dao.CategoryDao;
import com.werun.onlinenote_backend.dao.UserDao;
import com.werun.onlinenote_backend.entity.Category;
import com.werun.onlinenote_backend.entity.User;
import com.werun.onlinenote_backend.result.UserResult;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserService
 * @Description 实体User的service层
 * @Create 2022-04-03
 * @Update 2022-04-17
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserDao userDao;
    private final CategoryDao categoryDao;

    public UserResult deleteUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(user == null) {
            return new UserResult(false, "This didn't exist");
        }

        userDao.delete(user);
        return new UserResult(true, "Delete Successfully");
    }

    public UserResult getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(user == null) {
            return new UserResult(false, "This didn't exist");
        }
        List<CategoryBean> categoryBeanList = new ArrayList<>();
        List<Category> categories = categoryDao.findByUser(user);
        if(categories != null) {
            for(Category category : categories) {
                categoryBeanList.add(new CategoryBean(category));
            }
        }
        return new UserResult(new UserBean(user), categoryBeanList);
    }

    public UserResult changeUserName(String changeUserName) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(user == null) {
            return new UserResult(false, "This didn't exist");
        }

        user.setUserName(changeUserName);
        userDao.save(user);
        List<CategoryBean> categoryBeanList = new ArrayList<>();
        List<Category> categories = categoryDao.findByUser(user);
        if(categories != null) {
            for(Category category : categories) {
                categoryBeanList.add(new CategoryBean(category));
            }
        }
        return new UserResult(new UserBean(user), categoryBeanList);
    }

    public UserResult changeUserPassword(String changeUserPassword) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(user == null) {
            return new UserResult(false, "This didn't exist");
        }

        String userAccount = user.getUserAccount();
        ByteSource salt = ByteSource.Util.bytes(userAccount);
        String newPassword = new SimpleHash("MD5", changeUserPassword, salt,1024).toHex();
        user.setUserPassword(newPassword);
        userDao.save(user);
        List<CategoryBean> categoryBeanList = new ArrayList<>();
        List<Category> categories = categoryDao.findByUser(user);
        if(categories != null) {
            for(Category category : categories) {
                categoryBeanList.add(new CategoryBean(category));
            }
        }
        return new UserResult(new UserBean(user), categoryBeanList);
    }

}
