/**
 * All mammalian hearts should do this.
 *
 */
package pacemaker;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Describe properties of all mammalian hearts.
 * 
 * @author bulivaa
 */
public interface MammalianHeart extends Remote
{
    /**
     * The beating of our heart symbolizes the solving of an equation.
     *
     * @param equation The equation to be solved in a String object
     * @param parameters The parameters of the equation in a Map object
     * @return Solution to the equation
     * @throws RemoteException
     */
    public String beat(String equation, Map<String, Double> parameters) throws RemoteException;
}
