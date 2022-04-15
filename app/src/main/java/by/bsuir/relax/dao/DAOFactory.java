package by.bsuir.relax.dao;

public class DAOFactory {
    static DAO dao=new DB();

    public static DAO getDAO(){
        return dao;
    }
}
