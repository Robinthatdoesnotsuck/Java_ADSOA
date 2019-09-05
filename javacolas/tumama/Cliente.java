import java.io.EOFException;  
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;  
import javax.swing.JTextArea; 
import javax.swing.JTextField; 
import javax.swing.SwingUtilities;

public class Cliente extends JFrame
{
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;

    public Cliente( String host )
    {
        super( "Cliente" );
        chatServer = host;

        enterField = new JTextField();
        enterField.setEditable( false );
        enterField.addActionListener(
            new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                    sendData( event.getActionCommand() );
                    enterField.setText( "" );
                }
            }
        );
        add( enterField, BorderLayout.NORTH );
        displayArea = new JTextArea();
        add( new JScrollPane( displayArea ), BorderLayout.CENTER );
        setSize( 400, 300);
        setVisible( true );
    } // Finaliza el constructor de cliente
    public void runClient()
    {
        try 
        {
            connectToServer();
            getStreams();
            processConnection();    
        } 
        catch ( EOFException EOFException ) 
        {
            displayMessage( "\nClient terminated connection" ); 
               
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    } // run client metodo

    private void connectToServer() throws IOException
    {
        displayMessage( "Attempting connection\n" );

        client = new Socket( InetAddress.getByName( chatServer ), 12345);

        displayMessage( "Connected to: " + client.getInetAddress().getHostName() );
    } // conecta el server

    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream( client.getOutputStream() );
        output.flush();
        input = new ObjectInputStream( client.getInputStream() );

        displayMessage( "\nGot I/O streams\n" );
    }
    private void processConnection() throws IOException
    {
        setTextFieldEditable( true );

        do 
        {
            try 
            {
                message = ( String ) input.readObject();
            } 
            catch ( ClassNotFoundException classNotFoundException )
            {
                
            }    
        } while ( !message.equals( "SERVER>>> TERMINATE" ) );
    } // método connection
    private void closeConnection()
    {
        displayMessage( "\nClosing connection" );
        setTextFieldEditable( false ); 
        try
        {
            output.close();
            inpu.close();
            client.close();
        }
        catch (IOException ioException) 
        {
            ioException.printStackTrace();
        }
    }
    private void sendData( String message)
    {
        try 
        {
            output.writeObject( "CLIENT>>> " + message );
            output.flush();
            displayMessage( "\nCLIENT>> " + message );    
        } 
        catch (Exception e) 
        {
            
            displayArea.append( "\nError  writing object" );
        }
    } // end method send data
    
    private void displayMessage( final String messageToDisplay )
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    displayArea.append( messageToDisplay );
                }
            }
        );
    } // muestra el mensaje en un display en un hilo del proceso que despacha un evento

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
