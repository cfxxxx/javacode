package com.mhl.service;

import com.mhl.dao.MenuDAO;
import com.mhl.domain.Menu;

import java.util.List;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class MenuService {
    private static MenuDAO menuDAO = new MenuDAO();

    public static Menu query(int id) {
        Menu menu = menuDAO.querySingle("select * from menu where id=?", Menu.class, id);
        return menu;
    }
    public static List<Menu> showMenu() {
        List<Menu> menus = menuDAO.queryMulti("select * from menu", Menu.class);
        return menus;
    }
}
