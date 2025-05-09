import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CircleInterface extends Remote {
    public double getArea(int radius) throws RemoteException;
    public double getPerimeter(int radius) throws RemoteException; 
}
