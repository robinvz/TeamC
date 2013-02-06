package be.kdg.trips.model;

/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class User
{
    private String email;
    private String password;

    public User()
    {

    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
