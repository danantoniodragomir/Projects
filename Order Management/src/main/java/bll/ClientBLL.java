package bll;

import bll.validators.EmailValidator;
import bll.validators.NameValidator;
import bll.validators.Validator;
import dao.ClientsDAO;
import model.Clients;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL{

    private List<Validator<Clients>> validators;
    private ClientsDAO clientsDAO;

    /***
     * Constructorul clasei
     */
    public ClientBLL() {
        validators = new ArrayList<Validator<Clients>>();
        validators.add(new EmailValidator());
        validators.add(new NameValidator());

        clientsDAO = new ClientsDAO();
    }

    /***
     * Cautam un client in functie de id , daca nu l gasim aruncam Exceptie NoSuchElement
     * @param id Este id-ul clienntului pe care il dam in GUI
     * @return clientul gasit dupa id
     */
    public Clients findClientById(int id) {
        Clients clients = clientsDAO.findById(id);
        if (clients == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return clients;
    }

    /***
     * Inseram un client in tabelul Clients daca trece toate validarile
     * @param clients este clinetul care urmeaza a fi inserat
     * @return returneaza un numar intreg generat din clasa ClientsDAO
     */
    public int insertClient(Clients clients){
        for(Validator<Clients> validator : validators)
            validator.validate(clients);
        return clientsDAO.insert(clients);
    }

    /***
     * Updateaza un client
     * @param clients clientul ce uremaza a fi updatat
     */
    public void updateClient(Clients clients){
        clientsDAO.update(clients);
    }


    /***
     * Sterge un client
     * @param clients clientul ce urmeaza a fi sters
     */
    public void deleteClient(Clients clients){
        clientsDAO.delete(clients);
    }


}