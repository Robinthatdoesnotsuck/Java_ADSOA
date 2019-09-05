import java.io.IOException;;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame
{
    private JTextArea displayArea; // muestra los packetes que se reciben
    private DatagramSocket socket; // conecta los sockets a los clientes
    
    // implementa la interfaz y el Socket datagrama
    public Server()
    {
        super("Server");

        displayArea = new JTextArea(); // crea la ventanita
        add( new JScrollPane( displayArea ), BorderLayout.CENTER );
        setSize( 400, 300 ); // ajusta el tamaño en pixeles
        setVisible( true ); // muestra la ventana

        // crea el socket para enviar los packetes
        try
        {
            socket = new DatagramSocket( 5000 );
        }
        catch ( SocketException socketException )
        {
            SocketException.printStackTrace();
            System.exit( 1 );
        }
    }
}