package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Clients;
import model.Orders;
import model.Products;
import start.Reflection;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControllerGUI {

    static GUI gui;
    static GUI cGui;

    static String table;

    protected static final Logger LOGGER = Logger.getLogger(ControllerGUI.class.getName());

    /***
     * In functie de ce apasam pe prima si a doua interfata se creaza un GUI cu caracteristicile corespunzatoare :
     * de adaugare , editare ,stergere , vizualizare a celor 3 tabele ( Orders permite doar adaugare de comenzi )
     */
    public static class ClientsController implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            table = "Clients";
            gui = new GUI(table);
        }
    }

    public static class ProductsController implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            table = "Products";
            gui = new GUI(table);
        }
    }

    public static class OrdersController implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            table = "Orders";
            gui = new GUI(table);
        }
    }

    public static class AddController implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(table.equals("Products"))
                cGui = new GUI("add");
            else
                cGui = new GUI("insert");
        }
    }

    public static class EditController implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(table.equals("Products"))
                cGui = new GUI("edit");
            else
                cGui = new GUI("update");
        }
    }

    /***
     * Creeaza un pop-up in care intreaba ce id se doreste a fi sters
     * Si totusi se verifica daca aceasta functie de delete a fost apelata pentur Clients sau Products
     * Sterge intreg fieldul daca gaseste fieldul dupa id-ul introdus
     */
    public static class DeleteController implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String theId = JOptionPane.showInputDialog(gui.secondFrame,
                    "What id ?", null);
            if(table.equals("Clients")){
                ClientBLL clientBLL =  new ClientBLL();
                Clients client = clientBLL.findClientById(Integer.parseInt(theId));
                clientBLL.deleteClient(client);
            }
            else{
                ProductBLL productBLL = new ProductBLL();
                Products product = productBLL.findProductById(Integer.parseInt(theId));
                productBLL.deleteProduct(product);
            }

        }
    }

    public static class ViewController implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.equals("Clients"))
                cGui = new GUI("viewClients");
            else
                cGui = new GUI("viewProducts");
        }
    }

    /***
     * Adauga un client
     * Daca id-ul este negativ inseamna ca nu s-a putut introudce
     */
    public static class Ok1Controller implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Clients client = new Clients(cGui.tnume.getText(),cGui.tadresa.getText(),cGui.temail.getText());
            ClientBLL clientBLL = new ClientBLL();

            if (clientBLL.insertClient(client) > 0) {
                Reflection.retrieveProperties(client);
            }

            cGui.thirdFrame.dispatchEvent(new WindowEvent(cGui.thirdFrame,WindowEvent.WINDOW_CLOSING));
        }
    }

    /***
     * Updateaza un client in functie de ce s-a introdus in textfield-uri
     */
    public static class Ok2Controller implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(cGui.tid.getText());
            ClientBLL clientBLL = new ClientBLL();
            Clients c = clientBLL.findClientById(id);
            try {
                if(!cGui.tnume.getText().equals(""))
                    c.setName(cGui.tnume.getText());
                if(!cGui.tadresa.getText().equals(""))
                    c.setAddress(cGui.tadresa.getText());
                if(!cGui.temail.getText().equals(""))
                    c.setEmail(cGui.temail.getText());
                clientBLL.updateClient(c);
                cGui.thirdFrame.dispatchEvent(new WindowEvent(cGui.thirdFrame,WindowEvent.WINDOW_CLOSING));
            } catch (Exception ex) {
                cGui.thirdFrame.dispatchEvent(new WindowEvent(cGui.thirdFrame,WindowEvent.WINDOW_CLOSING));
                LOGGER.log(Level.INFO, ex.getMessage());
            }
        }
    }

    /***
     * Adauga un produs
     * Daca se returneaza un numar mai mic decat 0 din functia din ProductBLL inseamna canu a trecut de validari
     */
    public static class Ok3Controller implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Products products = new Products(cGui.pnume.getText(),Integer.parseInt(cGui.pprice.getText()),Integer.parseInt(cGui.pstock.getText()));
            ProductBLL productBLL = new ProductBLL();
            if (productBLL.insertProduct(products) > 0) {
                Reflection.retrieveProperties(products);
            }
            cGui.fourthFrame.dispatchEvent(new WindowEvent(cGui.fourthFrame,WindowEvent.WINDOW_CLOSING));
        }
    }

    /***
     * Updateaza un produs cautat dupa id-ul introdus in primul TextField
     */
    public static class Ok4Controller implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(cGui.pid.getText());
            System.out.println(id);
            ProductBLL productBLL = new ProductBLL();
            Products p = productBLL.findProductById(id);
            try {
                if(!cGui.pnume.getText().equals(""))
                    p.setName(cGui.pnume.getText());
                if(!cGui.pprice.getText().equals(""))
                    p.setPrice(Integer.parseInt(cGui.pprice.getText()));
                if(!cGui.pstock.getText().equals(""))
                    p.setStock(Integer.parseInt(cGui.pstock.getText()));
                productBLL.updateProduct(p);
                cGui.fourthFrame.dispatchEvent(new WindowEvent(cGui.fourthFrame,WindowEvent.WINDOW_CLOSING));
            } catch (Exception ex) {
                cGui.fourthFrame.dispatchEvent(new WindowEvent(cGui.fourthFrame,WindowEvent.WINDOW_CLOSING));
                LOGGER.log(Level.INFO, ex.getMessage());
            }
        }
    }

    /***
     * Creeaza o comanda doar daca exista acel client si acea comanda selectata , iar cantitatea din acel produs este mai mica decat intregul stock
     * Stockul acelui produs scade cu fix atatea unitati comandate de client
     */
    public static class Ok5Controller implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Orders orders = new Orders(Integer.parseInt(gui.oclient.getText()),Integer.parseInt(gui.oproduct.getText()),Integer.parseInt(gui.oquant.getText()));
            OrderBLL orderBLL = new OrderBLL();
            ClientBLL clientBLL = new ClientBLL();
            ProductBLL productBLL = new ProductBLL();

            Clients c = clientBLL.findClientById(Integer.parseInt(gui.oclient.getText()));
            Products p = productBLL.findProductById(Integer.parseInt(gui.oproduct.getText()));

            if (orderBLL.insertOrder(orders,p.getStock()) > 0) {
                p.setStock(p.getStock()-Integer.parseInt(gui.oquant.getText()));
                productBLL.updateProduct(p);
                Reflection.retrieveProperties(orders);
                FileWriter log = null, log1 = null;
                try {
                    log1 = new FileWriter("bill.txt");
                    PrintWriter printWriter1 = new PrintWriter(log1);
                    printWriter1.print("");
                    printWriter1.close();
                    log1.close();

                    log = new FileWriter("bill.txt",true);
                    PrintWriter printWriter = new PrintWriter(log);
                    printWriter.print(c.getId()+"# "+c.getName()+" bought "+gui.oquant.getText()+"x "+p.getName()+" total price = "+Integer.parseInt(gui.oquant.getText())*p.getPrice());
                    printWriter.close();
                    log.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }

            gui.sixthFrame.dispatchEvent(new WindowEvent(gui.sixthFrame,WindowEvent.WINDOW_CLOSING));
        }
    }
}
