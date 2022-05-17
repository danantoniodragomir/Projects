package bll.validators;

import model.Clients;

import java.util.regex.Pattern;

public class NameValidator implements  Validator<Clients>{

    private static final String NAME_PATTERN = "\\D*";

    /***
     * Verifica daca numele clientului este unul valid
     * @param client clientul care urmeaza a fi inserat
     */
    @Override
    public void validate(Clients client) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        if(!pattern.matcher(client.getName()).matches()){
            throw new IllegalArgumentException("Name is not valid!");
        }
    }
}
