package presentation;

import dao.ClientsDAO;
import dao.ProductsDAO;

import javax.swing.*;
import java.awt.*;

public class GUI {

    public JFrame secondFrame,thirdFrame,fourthFrame,fifthFrame,sixthFrame;
    public JTextField tid, tnume, tadresa, temail;
    public JTextField pid, pnume, pprice, pstock;
    public JTextField oclient, oproduct, oquant;
    private JPanel mainPanel;
    private JPanel secondPanel;
    private JPanel panel;
    private JPanel ppanel;
    private JPanel opanel;
    private final String table;

    /***
     * Constructul principal care permite alegerea unuia din cele 3 tabele
     */
    public GUI(){

        table = "Main";
        JFrame mainFrame = new JFrame("WAREHOUSE");
        setFrame(mainFrame);
        mainPanel = new JPanel();

        JButton clients = new JButton("CLIENTS");
        JButton products = new JButton("PRODUCTS");
        JButton orders = new JButton("ORDERS");

        layout(clients);
        layout(products);
        layout(orders);

        products.addActionListener(new ControllerGUI.ProductsController());
        clients.addActionListener(new ControllerGUI.ClientsController());
        orders.addActionListener(new ControllerGUI.OrdersController());

        mainFrame.add(mainPanel);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

    }

    /***
     * Al doilea constructor , care creaza interfata a doua si poate a 3-a in functie de ce a ales utilizatorul
     * @param ord decizia utilizatorului in ce metoda vrea sa deschida GUI-ul
     */
    public GUI(String ord){

        table = ord;
        switch (table) {
            case "Clients":
            case "Products":
                secondFrame = new JFrame();
                setFrame(secondFrame);
                secondPanel = new JPanel();

                JButton add = new JButton("Add");
                JButton edit = new JButton("Edit");
                JButton delete = new JButton("Delete");
                JButton view = new JButton("View");

                layout(add);
                layout(edit);
                layout(delete);
                layout(view);

                add.addActionListener(new ControllerGUI.AddController());
                edit.addActionListener(new ControllerGUI.EditController());
                delete.addActionListener(new ControllerGUI.DeleteController());
                view.addActionListener(new ControllerGUI.ViewController());

                secondFrame.add(secondPanel);

                secondFrame.setDefaultCloseOperation(secondFrame.getDefaultCloseOperation());
                secondFrame.setVisible(true);
                break;
            case "Orders":
                sixthFrame = new JFrame("ORDERS");
                sixthFrame.setLayout(new GridLayout(1,1));
                sixthFrame.setSize(400, 310);

                opanel = new JPanel();


                JPanel jocl = new JPanel();
                oclient = new JTextField(30);
                inputOrders(jocl,oclient, "idClient : ");

                JPanel jopr = new JPanel();
                oproduct = new JTextField(30);
                inputOrders(jopr,oproduct, "idProduct : ");

                JPanel jostock = new JPanel();
                oquant = new JTextField(20);
                inputOrders(jostock,oquant,"Quantity : ");

                JPanel panelOk = new JPanel();
                JButton buttonAdd = new JButton("Add");
                buttonAdd.setBackground(Color.green);
                buttonAdd.setLayout(new FlowLayout());
                buttonAdd.setPreferredSize(new Dimension(120,30));
                buttonAdd.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                buttonAdd.addActionListener(new ControllerGUI.Ok5Controller());
                panelOk.add(buttonAdd);
                opanel.add(panelOk);

                sixthFrame.add(opanel);
                sixthFrame.setDefaultCloseOperation(sixthFrame.getDefaultCloseOperation());
                sixthFrame.setVisible(true);
                break;
            case "insert":
            case "update":
                thirdFrame = new JFrame();
                thirdFrame.setLayout(new GridLayout(1,1));
                thirdFrame.setSize(400, 310);

               panel = new JPanel();

               if(table.equals("update")) {
                   JPanel id = new JPanel();
                   tid = new JTextField(5);
                   inputClients(id, tid, "Id :");
               }

                JPanel nume = new JPanel();
                tnume = new JTextField(20);
                inputClients(nume,tnume,"Name : ");

                JPanel adresa = new JPanel();
                tadresa = new JTextField(30);
                inputClients(adresa,tadresa, "Address : ");

                JPanel email = new JPanel();
                temail = new JTextField(30);
                inputClients(email,temail, "Email : ");

                JPanel ok = new JPanel();
                JButton k = new JButton("OK");
                k.setBackground(Color.green);
                k.setLayout(new FlowLayout());
                k.setPreferredSize(new Dimension(120,30));
                k.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                if(table.equals("insert"))
                    k.addActionListener(new ControllerGUI.Ok1Controller());
                else
                    k.addActionListener(new ControllerGUI.Ok2Controller());
                ok.add(k);
                panel.add(ok);

                thirdFrame.add(panel);
                thirdFrame.setDefaultCloseOperation(thirdFrame.getDefaultCloseOperation());
                thirdFrame.setVisible(true);
                break;
            case "add":
            case "edit":
                fourthFrame = new JFrame();
                fourthFrame.setLayout(new GridLayout(1,1));
                fourthFrame.setSize(400, 310);

                ppanel = new JPanel();

                if(table.equals("edit")) {
                    JPanel jid = new JPanel();
                    pid = new JTextField(5);
                    inputProducts(jid, pid, "Id :");
                }

                JPanel jnume = new JPanel();
                pnume = new JTextField(20);
                inputProducts(jnume,pnume,"Name : ");

                JPanel jadresa = new JPanel();
                pprice = new JTextField(5);
                inputProducts(jadresa, pprice, "Price : ");

                JPanel jemail = new JPanel();
                pstock = new JTextField(5);
                inputProducts(jemail, pstock, "Stock : ");

                JPanel jok = new JPanel();
                JButton bk = new JButton("OK");
                bk.setBackground(Color.green);
                bk.setLayout(new FlowLayout());
                bk.setPreferredSize(new Dimension(120,30));
                bk.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                if(table.equals("add"))
                    bk.addActionListener(new ControllerGUI.Ok3Controller());
                else
                    bk.addActionListener(new ControllerGUI.Ok4Controller());
                jok.add(bk);
                ppanel.add(jok);

                fourthFrame.add(ppanel);
                fourthFrame.setDefaultCloseOperation(fourthFrame.getDefaultCloseOperation());
                fourthFrame.setVisible(true);
                break;
            case "viewClients":
            case "viewProducts":
                fifthFrame = new JFrame();
                fifthFrame.setLayout(new GridLayout(1,1));
                fifthFrame.setSize(500, 310);
                JTable tbl;
                if(table.equals("viewClients")) {
                    ClientsDAO clients = new ClientsDAO();
                    tbl = clients.drawTable(clients.findAll());
                }
                else{
                    ProductsDAO productsDAO = new ProductsDAO();
                    tbl = productsDAO.drawTable(productsDAO.findAll());
                }

                JScrollPane sp = new JScrollPane(tbl);
                fifthFrame.add(sp);
                fifthFrame.setDefaultCloseOperation(fifthFrame.getDefaultCloseOperation());
                fifthFrame.setVisible(true);
                break;
        }

    }

    /***
     * Creaza forma unui buton si il adauga in panel
     * @param jb butonul care vreau sa l prelucrez
     */
    public void layout(JButton jb){
        jb.setLayout(new FlowLayout());
        jb.setPreferredSize(new Dimension(300,100));
        if(table.equals("Main"))
            mainPanel.add(jb);
        else if(table.equals("Orders"))
            System.out.println("ORDERS TATII");
        else
            secondPanel.add(jb);
    }

    /***
     * Seteaza frame-ul interfelor de inceput
     * @param jf frame-ul care umreaza a fi prelucrat
     */
    public void setFrame(JFrame jf){
        jf.setLayout(new FlowLayout());
        if(table.equals("Main"))
            jf.setSize(980,160);
        else if (table.equals("Clients") || table.equals("Products"))
            jf.setSize(1280,160);

    }

    /***
     * Creaza forma unui input client
     * La fel pentru produs si order
     * @param jp paneulul care contine textfieldul
     * @param tf textfieldul in care se vor introduce detalii
     * @param details Descrierea din fata textfieldului
     */
    public void inputClients(JPanel jp, JTextField tf, String details){
        jp.setLayout(new FlowLayout());
        jp.setPreferredSize(new Dimension(400,50));
        jp.add(new JLabel(details,JLabel.LEFT));
        jp.add(tf);
        panel.add(jp);
    }

    public void inputProducts(JPanel jp, JTextField tf, String details){
        jp.setLayout(new FlowLayout());
        jp.setPreferredSize(new Dimension(400,50));
        jp.add(new JLabel(details,JLabel.LEFT));
        jp.add(tf);
        ppanel.add(jp);
    }

    public void inputOrders(JPanel jp, JTextField tf, String details){
        jp.setLayout(new FlowLayout());
        jp.setPreferredSize(new Dimension(400,50));
        jp.add(new JLabel(details,JLabel.LEFT));
        jp.add(tf);
        opanel.add(jp);
    }

}
