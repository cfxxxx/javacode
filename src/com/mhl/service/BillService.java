package com.mhl.service;

import com.mhl.dao.BillDAO;
import com.mhl.dao.MenuDAO;
import com.mhl.domain.Bill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class BillService {
    private static BillDAO billDAO = new BillDAO();

    //点餐
    public static int orderMenu(int diningTableId, int menuId, int nums) {
        int update = billDAO.update("insert into bill value(null,'',?,?,?,?,?,'未结账')", menuId, nums, ((Double) new MenuDAO().queryScalar("select price from menu where id=?", menuId)).doubleValue() * nums, diningTableId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        return update;
    }

    //显示账单
    public static List<Bill> showBills() {
        List<Bill> bills = billDAO.queryMulti("select * from bill", Bill.class);
        return bills;
    }

    //结账
    public static boolean pay(int diningTableId, String state) {
        int update = billDAO.update("update bill set state=? where diningTableId=?", state, diningTableId);
        return update == 0 ? false : true;
    }
}
