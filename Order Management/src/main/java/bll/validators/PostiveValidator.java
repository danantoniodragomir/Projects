package bll.validators;

import model.Products;

public class PostiveValidator implements Validator<Products> {
    private static final int MIN = 0;

    /***
     * Verifica daca pentru produsul care urmeaza a fi inseart stockul si pretul sunt pozitive
     * @param products produsul care urmeaza a fi inserat
     */

    @Override
    public void validate(Products products) {
        if(products.getStock()<0 || products.getPrice()<0)
            throw new IllegalArgumentException("Must be positive");
    }
}
