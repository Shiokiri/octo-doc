package com.werun.notebook;

public class Constant {
    public static final String base_url = "http://101.43.177.191:8081/";
    public static final String post_url_login = base_url + "login/";
    public static final String post_url_signup = base_url + "register/";
    public static final String post_url_logout = base_url + "logout/";
    public static final String get_url_getuser = base_url + "user/getUser/";
    public static final String put_url_change = base_url + "user/changeUserPassword/";
    public static final String delete_url_classification = base_url + "category/deleteCategory/";
    public static final String post_url_classification = base_url + "category/addCategory/";
    public static final String put_url_classificationChange = base_url + "category/changeCategoryName/";
    public static final String post_url_addNote = base_url + "note/addNote/";
    public static final String get_url_getNote = base_url + "note/getAllNote/";
    public static final String delete_url_note = base_url + "note/deleteNote/";
    public static final String put_url_title = base_url + "note/changeNoteTitle/";
    public static final String put_url_cls = base_url + "note/changeNoteCategory/";
    public static final String put_url_content = base_url + "note/changeNoteContent/";
    public static final String put_url_all = base_url + "note/changeNoteAll/";
    public static final String get_url_search = base_url + "note/getNoteByNoteTitle/";
}
