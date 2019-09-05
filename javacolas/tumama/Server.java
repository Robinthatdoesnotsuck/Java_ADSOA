import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame
{
    private JTextField enterField; //input del usuario creo
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;

    public Server()
    {
        super("Server");
        enterField = new JTextField();
        enterField.setEditable(false);
        enterField.addActionListener(
            new ActionListener(){
            
                public void actionPerformed(ActionEvent event) {
                    sendData( event.getActionCommand());
                    enterField.setText("");
                }
            }
        );

        add( enterField, BorderLayout.NORTH );
        displayArea = new JTextArea(); // Crea un área de display
        add( new JScrollPane(displayArea), BorderLayout.CENTER );

        setSize( 300, 150 ); // Determinar el tamaño de la ventana
        setVisible( true ); //
    }
    public void runServer()
    {
        try 
        {
            server = new ServerSocket( 12345, 100); // esto crea el socket
            while(true)
            {
                try
                {
                    waitForConnection(); // esperar la conexión
                    getStreams();
                    processConnection(); // intenta procesar la conexión
                }
                catch ( EOFException eofException)
                {
                    displayMessage( "\nServer terminated connection" );
                }
                finally
                {
                    closeConnection(); // termina la conexión
                    ++counter;
                }
            } // while cacas de la conexion
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
    } // metodo que pone en ejecución el server

    private void waitForConnection() throws IOException{

        displayMessage( "Waiting for connection\n ");
        connection = server.accept(); // esta cosa acepta las conexiones del servidor
    }// metodo para esperar por la conexion

    // toma el valor de los streams de datos para recibir cosas
    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream ( connection.getOutputStream() );
        output.flush(); // manda el buffer de salida para mandar la cabecera de informacion

        // trae la entrada para los objetos
        input = new ObjectInputStream( connection.getInputStream() );

        displayMessage( "\nGot I/O streams\n");
    }// metodo para obtener streams

    private void processConnection() throws IOException
    {
        String message = "Connection succesful";
        sendData( message ); // envia un mensaje de conexión exitosa

        // habilita el input para que el usuario mande un mensaje
        sendTextFieldEditable( true );
        
        do // procesa los mensajes de los usuarios
        {
            try
            {
                message = ( String ) input.readObject();
                displayMessage("\n" + message); // muestra el mensaje
            }
            catch ( ClassNotFoundException classNotFoundException )
            {
                displayMessage ( "\nUnkown object type received" ); 
            }
        } while ( !message.equals("CLIENT>>> TERMINATE" ) );
    }
    
    private void closeConnection()
    {
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable( false ); // inhabilita el input

        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }// termina el método de cerrar la conexion
    
    private void sendData( String message )
    {
        try
        {
            output.writeObject("SERVER>>> " + message);
            output.flush(); // le da la salida al cliente o usuario
        }
        catch (IOException ioException)
        {
            displayArea.append( "\nError writing object" );
        }
    } // termina metodo enviar datos

    private void displayMessage ( final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run() // actualiza lo que se muestra
                {
                    displayArea.append( messageToDisplay );
                }
            }
        );
    } // termina el metodo de mostrar el mensaje
    private void setTextFieldEditable( final boolean editable )
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    enterField.setEditable( editable );
                }
          }
        );
    } 

}