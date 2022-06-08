package com.mhl.view;

import com.mhl.domain.Bill;
import com.mhl.domain.DiningTable;
import com.mhl.domain.Employee;
import com.mhl.domain.Menu;
import com.mhl.service.BillService;
import com.mhl.service.DiningTableService;
import com.mhl.service.EmployeeService;
import com.mhl.service.MenuService;

import java.util.List;
import java.util.Scanner;

/**
 * @author 程柯嘉
 * @version 1.0
 * 这是主界面
 */
@SuppressWarnings({"all"})
public class MHLView {

    private final static Scanner s = new Scanner(System.in);
    //控制是否退出菜单
    private boolean loop = true;
    private boolean loop_ = true;
    private String key = "";

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    public void mainMenu() {
        while (loop) {
            System.out.println("=================满汉楼================");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.print("请输入你的选择：");
            key = s.next();
            switch (key) {
                case "1":
                    System.out.print("请输入员工号：");
                    String empId = s.next();
                    System.out.print("请输入密  码：");
                    String pwd = s.next();

                    //去数据库判断
                    Employee employee = EmployeeService.getEmployeeByIdAndPwd(empId, pwd);
                    if (employee != null) {
                        System.out.println("=================登录成功[" + employee.getName() + "]=================\n");
                        while (loop_) {
                            secondMenu();
                        }
                        System.out.println("已退出登录");
                    } else {
                        System.out.println("登录失败");
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
        System.out.println("已退出满汉楼系统");
    }

    public void secondMenu() {
        System.out.println("=================满汉楼（二级菜单）================");
        System.out.println("\t\t 1 显示餐桌状态");
        System.out.println("\t\t 2 预订餐桌");
        System.out.println("\t\t 3 显示所有菜品");
        System.out.println("\t\t 4 点餐");
        System.out.println("\t\t 5 查看账单");
        System.out.println("\t\t 6 结账");
        System.out.println("\t\t 9 退出");
        System.out.print("请输入你的选择：");
        key = s.next();
        switch (key) {
            case "1":
                listDiningTable();
                break;
            case "2":
                orderDiningTable();
                break;
            case "3":
                showMenu();
                break;
            case "4":
                orderMenu();
                break;
            case "5":
                showBills();
                break;
            case "6":
                pay();
                break;
            case "9":
                loop_ = false;
                break;
            default:
                System.out.println("你的输入有误，请重新输入");
                break;
        }
    }

    //显示所有餐桌信息
    public void listDiningTable() {
        List<DiningTable> list = DiningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态");
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("=================显示完毕=================");
    }

    //预订餐桌
    public void orderDiningTable() {
        while (true) {
            System.out.print("\n请输入你想预订的餐桌号（-1退出）：");
            int id = s.nextInt();
            if (id != -1) {
                //查询餐桌是否存在
                DiningTable diningTable = DiningTableService.query(id);
                if (diningTable != null) { //存在时
                    //确认餐桌是否已被预订
                    if (diningTable.getState().equals("空")) {
                        System.out.print("\n请输入预订人姓名：");
                        String orderName = s.next();
                        System.out.print("\n请输入预订手机号：");
                        String orderTel = s.next();
                        DiningTableService.order(id, orderName, orderTel);
                        System.out.println("预定成功！");
                        return;
                    } else { //如果已被预订
                        System.out.println("您输入的餐桌已被预订，请重新预订");
                    }
                } else { //不存在时
                    System.out.println("您输入的餐桌号不存在，请重新输入");
                }
            } else {
                System.out.println("已退出预订");
                break;
            }
        }
    }

    //显示菜单
    public void showMenu() {
        System.out.println("菜品编号\t\t类型\t\t\t菜品\t\t\t\t价格");
        List<Menu> menus = MenuService.showMenu();
        for (Menu menu : menus) {
            System.out.println(menu);
        }
    }

    //点餐
    public void orderMenu() {
        System.out.print("请选择点餐的桌号（-1退出）：");
        int diningTableId = s.nextInt();
        if (diningTableId != -1) {
            DiningTable diningTable = DiningTableService.query(diningTableId);
            if (diningTable != null && !diningTable.getState().equals("空")) {
                System.out.print("请输入菜品编号（-1退出）：");
                int menuId = s.nextInt();
                if (menuId != -1) {
                    Menu query = MenuService.query(menuId);
                    if (query != null) {
                        System.out.print("请输入菜品数量（-1退出）：");
                        int nums = s.nextInt();
                        while (true) {
                            System.out.print("请确认是否下单（y/n）：");
                            String bool = s.next();
                            if (bool.equals("y")) {
                                int i = BillService.orderMenu(diningTableId, menuId, nums);
                                if (i != 0 && DiningTableService.updateState(diningTableId, "就餐中")) {
                                    System.out.println("下单成功！");
                                    break;
                                } else {
                                    System.out.println("下单失败！");
                                }
                            } else if (bool.equals("n")) {
                                System.out.println("您已取消下单！");
                                break;
                            } else {
                                System.out.println("您的输入有误，请重新输入");
                            }
                        }
                    } else {
                        System.out.println("您选择的菜品不存在，请重新选择！");
                    }
                } else {
                    System.out.println("您已退出点餐");
                }
            } else if (diningTable.getState().equals("空")) {
                System.out.println("您必须先预订再点餐！");
            } else {
                System.out.println("您输入的桌号不存在，请重新输入！");
            }
        } else {
            System.out.println("您已退出点餐");
        }
    }

    //显示账单
    public void showBills() {
        List<Bill> bills = BillService.showBills();
        System.out.println("编号\t\t菜品号\t菜品数量\t金额\t\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    //结账
    public void pay() {
        System.out.print("请选择要结账的餐桌编号：");
        int diningTableId = s.nextInt();
        DiningTable diningTable = DiningTableService.query(diningTableId);
        if (diningTable != null && diningTable.getState().equals("就餐中")) {
            System.out.print("请选择结账方式（现金/微信/支付宝）：");
            String state = s.next();
            if (DiningTableService.updateState(diningTableId, "已支付") && BillService.pay(diningTableId, state)) {
                System.out.println("结账成功！");
            }
        } else if (diningTable.getState().equals("已支付")){
           System.out.println("您选择的餐桌已结账！");
        } else if (diningTable.getState().equals("已预订") || diningTable.getState().equals("空")) {
            System.out.println("您选择的餐桌还未点餐！");
        } else {
            System.out.println("您选择的餐桌不存在！");
        }
    }
}
