import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CircleImplementation extends UnicastRemoteObject implements CircleInterface 
{
    private double PI;
    public CircleImplementation() throws RemoteException 
    {
        super();
        PI = 22.0 / 7.0;
    }
    @Override
    public double getArea(int radius) {
        return PI * radius * radius;
    }
    
    @Override
    public double getPerimeter(int radius) {
        return 2 * PI * radius;
    }
}
