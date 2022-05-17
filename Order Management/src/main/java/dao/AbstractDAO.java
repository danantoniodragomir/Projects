package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

import javax.swing.*;


public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;
    private JTable table;

    /***
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /***
     * Se vor afisa toate fieldurile din tabel care ineplinesc conditia
     * @param field field-ul dupa care se cauta in tabela
     * @return query-ul sub forma de String
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /***
     * Creeaza query-ul pentru inserarea in tabel
     * @return query-ul sub forma de String
     */
    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName());
        sb.append(" (");
        int count = type.getDeclaredFields().length - 1;
        int aux = 1;
        for (Field field : type.getDeclaredFields()){
            if (!field.getName().equals("id"))
                if (aux == count+1)
                    sb.append(field.getName()).append(")");
                else
                    sb.append(field.getName()).append(", ");
            aux++;
        }
        sb.append(" VALUES ( ");
        for(int i=0;i<count-1;i++)
            sb.append("?, ");
        sb.append("? )");
        return sb.toString();
    }

    /***
     * Creeaza query-ul care permite updatarea unui field din tabel
     * @return query-ul sub forma de String
     */
    private String createEditQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        int count = type.getDeclaredFields().length-1;
        int aux = 1;
        for(Field field : type.getDeclaredFields()){
            if(!field.getName().equals("id"))
                if(aux == count+1)
                    sb.append(field.getName()).append(" = ? WHERE id = ?");
                else
                    sb.append(field.getName()).append(" = ?, ");
            aux++;
        }
        return sb.toString();
    }

    /***
     * Creeaza query-ul care permite stergerea unui field din tabel
     * @return query-ul sub forma de String
     */
    private String createDeleteQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE id = ?");
        return sb.toString();
    }

    /***
     * Creeaza query-ul care permite vizualizarea unui intreg tabel
     * @return query-ul sub forma de String
     */
    private String createViewQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /***
     * Gaseste toate fieludirle unui tabel
     * @return toate fieldurile sub forma de List
     */
    public List<T> findAll() {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement viewStatement = null;
        String query = createViewQuery();
        ResultSet resultSet = null;
        try {
            viewStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            resultSet = viewStatement.executeQuery();

           return createObjects(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(viewStatement);
            ConnectionFactory.close(dbConnection);
        }

        return null;
    }

    /***
     * Creeaza un tabel pe baza obiectelor create mai sus cu findALL
     * @param t ce fel de obiecte se afla in lista
     * @return un JTabel care contine tot tabelul din MySql
     */
    public JTable drawTable(List<T> t) {

        int count = type.getDeclaredFields().length;
        String [] coloane =  new String[count];
        String [][] data = new String[100][count];

        int i=0,j=0,k=0;
        try {

            for(Field field : type.getDeclaredFields()){
                String fieldName = field.getName();
                coloane[i++] = fieldName;
            }

            for (T t1 : t) {
                j=0;
                for (Field field : type.getDeclaredFields()) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    try {
                        data[k][j++] = method.invoke(t1).toString();
                    }
                    catch(NullPointerException e)
                    {
                        data[k][j++]="null";
                    }
                }
                k++;
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }


        table = new JTable(data,coloane);
        table.setBounds(30,40,200,300);

        return table;
    }

    /***
     * Gaseste un element din tabel pe baza id-ului
     * @param id este dat ca parametru in GUI
     * @return intreg elementul gasit( poate fi client sau produs )
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /***
     * Creeaza o lista de obiecte aflate intr un tabel
     * @param resultSet rezultatul unui query
     * @return toate obiectele sub forma de lista
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * Insereaza obicetul t in tabela corespunzatoare tipului de obiect t
     * @param t obiectul care urmeaza a fi inserat
     * @return id-ul clientului introdus
     */
    public int insert(T t) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        String query = createInsertQuery();
        int insertedId = -1;

        try {
            insertStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int parameterIndex=1;
            for(Field field : t.getClass().getDeclaredFields()){
                String fieldName = field.getName();
                if(!fieldName.equals("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getReadMethod();
                    insertStatement.setObject(parameterIndex++, method.invoke(t));
                }
            }
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, "DAO:insert " + e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }

    /***
     * Modifica tabelul corespunzator editand informatiile fieldului corespunzator
     * @param t este fieldul care urmeaza a fi updatat in tabel
     */
    public void update(T t) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement updateStatement = null;
        String query = createEditQuery();
        int len = t.getClass().getDeclaredFields().length;

        try {
            updateStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            for(Field field : t.getClass().getDeclaredFields()){
                String fieldName = field.getName();
                if(!fieldName.equals("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getReadMethod();
                    updateStatement.setObject(parameterIndex++, method.invoke(t));
                }
                else {
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName,type);
                    Method m = pd.getReadMethod();
                    updateStatement.setObject(len,m.invoke(t));
                }
            }

            updateStatement.executeUpdate();

        } catch (SQLException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, "DAO: edit " + e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(updateStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    /***
     * Sterge un field din tablul corespunzator
     * @param t filedul care urmeaza a fi sters
     */
    public void delete(T t){
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement deleteStatement = null;
        String query = createDeleteQuery();

        try {
            deleteStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for(Field field : t.getClass().getDeclaredFields()){
                String fieldName = field.getName();
                if(fieldName.equals("id")){
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName,type);
                    Method method = propertyDescriptor.getReadMethod();
                    deleteStatement.setObject(1, method.invoke(t));
                    break;
                }
            }
            deleteStatement.executeUpdate();
        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }
}
