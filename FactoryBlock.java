import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.Objects;

class ProgressBar extends JPanel {

    public double Status;
    public JTextField Info;

    ProgressBar(){
        Info = new JTextField();
        Info.setHorizontalAlignment(JTextField.CENTER);
        Info.setFont(new Font("Arial", Font.BOLD, 22));
        Info.setBackground(new Color(0f,0f,0f,0f));
        Info.setForeground(Color.WHITE);
        Info.setEditable(false);
        Info.setBorder(null);
        Info.setBorder(BorderFactory.createCompoundBorder(Info.getBorder(), BorderFactory.createEmptyBorder(15, 0, 0, 0)));
        this.add(Info);
        Status = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        ImageIcon Background = new ImageIcon(Objects.requireNonNull(ProgressBar.class.getResource("/Stuff/Background.png")));
        super.paintComponent(g);
        double Progress = (0.01 * Status);
        double tmp = (this.getWidth() * Progress);
        g.drawImage(Background.getImage(), 0, 0, null);
        g.fillRect(0,0,(int)tmp,this.getHeight());
        this.setForeground(new Color(0,155,0,170));
    }
}

public class FactoryBlock extends JPanel {

    public static final Byte BUTTON_SIZE = 96;

    public JButton RunFactory, AmountButton, BuyFactoryButton;

    public ProgressBar Progress;

    FactoryBlock(){

        this.setPreferredSize(new Dimension(300,BUTTON_SIZE));
        this.setLayout(new BorderLayout());

        JPanel FirstContainer = new JPanel(new BorderLayout());
        JPanel SecondContainer = new JPanel(new GridLayout(1,2));

        Progress = new ProgressBar();

        RunFactory = new JButton();

        RunFactory.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));

        this.add(RunFactory,BorderLayout.WEST);
        this.add(FirstContainer,BorderLayout.CENTER);
        FirstContainer.add(Progress,BorderLayout.CENTER);
        FirstContainer.add(SecondContainer,BorderLayout.SOUTH);

        AmountButton = new JButton();
        AmountButton.setBackground(Color.ORANGE);

        BuyFactoryButton = new JButton("Buy Factory");
        BuyFactoryButton.setBackground(Color.ORANGE);

        SecondContainer.add(AmountButton);
        SecondContainer.add(BuyFactoryButton);
    }

    public boolean MakeProgress(){
        boolean Output = false;
        Progress.Status++;
        if (Progress.Status > 100) {
            Progress.Status = 0;
            Output = true;
        }
        Progress.repaint();
        return Output;
    }

    public void UpdateBlock(BigInteger _Amount, BigInteger _Production){

        BigInteger Tmp;

        String _ShortInfo = "" + _Amount;
        String _LongInfo = "" + _Production;

        for (int j = 70; j > 0; j--) {

            Tmp = _Amount.divide(new BigInteger("1000").pow(j)); // Divide Amount by 1000

            if (Tmp.compareTo(new BigInteger("0")) > 0 && Tmp.compareTo(new BigInteger("999")) <= 0) { // If Divided Amount is between 0 and 999
                _ShortInfo = "" + Tmp + " * 1000^" + j; // Assign Title
                break;
            }

        } // Look For Shorter Way Of Displaying Amount

        AmountButton.setText(_ShortInfo);
        Progress.Info.setText(_LongInfo);
    }
    public void UpdateBlock(String _Amount, String _Production){
        AmountButton.setText(_Amount);
        Progress.Info.setText(_Production);
    }

    @Override
    protected void paintComponent(Graphics g) {
        ImageIcon Background = new ImageIcon(Objects.requireNonNull(FactoryBlock.class.getResource("/Stuff/Background.png")));
        super.paintComponent(g);
        g.drawImage(Background.getImage(), 0, 0, null);
    }

}
