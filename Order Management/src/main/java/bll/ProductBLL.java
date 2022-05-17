package bll;

import bll.validators.PostiveValidator;
import bll.validators.Validator;
import dao.ProductsDAO;
import model.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {
    private List<Validator<Products>> validators;
    private ProductsDAO productsDAO;

    /***
     * Constructor
     */
    public ProductBLL(){
        validators = new ArrayList<Validator<Products>>();
        validators.add(new PostiveValidator());

        productsDAO = new ProductsDAO();
    }

    /***
     * Se incearca gasirea produsului cu id-ul id din tabelul products
     * @param id produsului este parametrul primit din interfata
     * @return produsul gasit
     */
    public Products findProductById(int id) {
        Products products = productsDAO.findById(id);
        if (products == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return products;
    }

    /***
     * Se incearca inserarea unui produs in tabelul Prodcuts
     * @param products produsul care urmeaza a fi introdus
     * @return un numar intreg generat pe baza unei methode din clasa ProductsDAO
     */

    public int insertProduct(Products products){
        for(Validator<Products> validator : validators)
            validator.validate(products);
        return productsDAO.insert(products);
    }

    /***
     * Updateaza datele produsului
     * @param products produsul care urmeaza a fi updatat
     */
    public void updateProduct(Products products){
        productsDAO.update(products);
    }

    /***
     * Sterge un produs din tabel
     * @param products produsul care urmeaza a fi sters
     */
    public void deleteProduct(Products products){
        productsDAO.delete(products);
    }

}
