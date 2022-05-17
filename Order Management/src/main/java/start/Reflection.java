package start;

import java.lang.reflect.Field;

public class Reflection {

    public static void retrieveProperties(Object object) {

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
                if(!field.getName().equals("id"))
                    System.out.println(field.getName() + "=" + value);

            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}



