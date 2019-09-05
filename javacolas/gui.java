import javax.swing.JOptionPane;

public class gui
{
    public static void main(String[] args)
    {
        // usa una librería ya definida de java.swing como JOptionPane para las forms
        String firstNumber = JOptionPane.showInputDialog("Enter first integer");
        String secondNumber = JOptionPane.showInputDialog("Enter second integer");
        // parsea el string en integers como c#

        int number1 = Integer.parseInt(firstNumber);
        int number2 = Integer.parseInt(secondNumber);
        int sum = number1 + number2;

        JOptionPane.showMessageDialog(null, "The sum is " + sum, "Sum of two integers", JOptionPane.PLAIN_MESSAGE);
    }
}