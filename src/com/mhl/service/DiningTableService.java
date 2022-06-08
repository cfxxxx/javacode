package com.mhl.service;

import com.mhl.dao.DiningTableDAO;
import com.mhl.domain.DiningTable;

import java.util.List;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class DiningTableService {
    private static DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回所有餐桌的信息
    public static List<DiningTable> list() {
        List<DiningTable> list = diningTableDAO.queryMulti("select id,state from diningTable", DiningTable.class);
        return list;
    }

    //查询餐桌是否存在
    public static DiningTable query(int id) {
        DiningTable diningTable = diningTableDAO.querySingle("select * from diningtable where id=?", DiningTable.class, id);
        return diningTable;
    }

    //修改餐桌状态信息
    public static boolean updateState(int diningTableId, String state) {
        int update = diningTableDAO.update("update diningtable set state=? where id=?", state, diningTableId);
        return update == 0 ? false : true;
    }

    //预订餐桌
    public static int order(int id, String orderName, String orderTel) {
        int rows = diningTableDAO.update("update diningtable set state='已预订',orderName=?,orderTel=? where id=?", orderName, orderTel, id);
        return rows;
    }
}
