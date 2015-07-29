/**
 * The server application that runs and waits for input.
 */
package pulse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import pacemaker.MammalianHeart;

/**
 * The end point of a signal.
 */
public class Heart extends UnicastRemoteObject
        implements MammalianHeart
{

    public Heart() throws RemoteException
    {
        super(0);    // required to avoid the 'rmic' step, see below
    }

    /**
     * Solve the equation.
     *
     * @param equation The equation to be solved in a String object
     * @param parameters The parameters of the equation in a Map object
     * @return Solution to the equation
     * @throws RemoteException
     */
    @Override
    public String beat(String equation, Map<String, Double> parameters)
    {
        Expression e = new ExpressionBuilder(equation)
                .variables(parameters.keySet())
                .build();
        e.setVariables(parameters);

        double result = e.evaluate();

        Logger.getLogger(Heart.class.getName()).log(Level.INFO, 
                "Valve {0} invoked to solve equation {1}",
                new Object[]{getValveID(), equation});

        return String.valueOf(result);
    }

    /**
     * Reads the ID of this particular valve as stored in an ID file.
     *
     * @return
     */
    private String getValveID()
    {

        try
        {
            File file = new File("log/valve.txt");
            FileReader platelet = new FileReader(file);
            BufferedReader blood = new BufferedReader(platelet);

            String vein = null;
            String valveNumber = null;

            while((vein = blood.readLine()) != null)
            {
                valveNumber = vein;
            }

            return valveNumber;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Heart.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }

    /**
     * Star the heart.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception
    {
        System.out.println("Pacemaker warming up...");

        try
        { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("Aorta created.");
        }
        catch (RemoteException e)
        {
            //do nothing, error means registry already exists
            System.out.println("Aorta already exists.");
        }

        //Instantiate RmiServer
        Heart soul = new Heart();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/RmiServer", soul);
        System.out.println("Heart started. Waiting for pulse");
    }
}
