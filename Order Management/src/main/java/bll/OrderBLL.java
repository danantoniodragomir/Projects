package bll;

import dao.OrdersDAO;
import model.Orders;

public class OrderBLL {

    private OrdersDAO ordersDAO;

    /***
     * Constructorul clasei
     */
    public OrderBLL(){
        ordersDAO =  new OrdersDAO();
    }

    /***
     * Verifica daca clientul doreste mai multe produse decat sunt pe stock , si proceasaza daca este sau nu adevarat
     * @param orders comanda care urmeaza a fi procesata si introdusa in tabel
     * @param quantity cantitatea produsului aflat pe stock
     * @return un numar intreg gerenat pe baza methodei insert din clasa OrdersDAO
     */
    public int insertOrder(Orders orders, int quantity){
        if(orders.getQuantity() > quantity)
            throw new IllegalArgumentException("UNDER STOCK");
        return  ordersDAO.insert(orders);
    }

}
