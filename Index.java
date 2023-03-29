import Custom.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

class Manager {

    public boolean ManuallyTriggered;
    public boolean AutomateFactory;
    public int ProductionBoost;
    public int SpeedBoost;

    Manager(){
        ManuallyTriggered = false;
        AutomateFactory = false;
        ProductionBoost = 1;
        SpeedBoost = 1;
    }

    public void Automate(){
        AutomateFactory = true;
    }

    public void UpgradeSpeed(){
        SpeedBoost *= 2;
    }

    public void UpgradeProduction(){
        ProductionBoost *= 2;
    }

}

class Factory {

    public byte ProductionProgress;
    public int TicksPerProgress;
    public int LastProgress;
    public BigInteger Production;
    public int ProductionBoost;
    public BigInteger Amount;

    Factory(){
        Amount = new BigInteger("1");
        Production = new BigInteger("1");
        ProductionBoost = 1;
        ProductionProgress = 0;
    }

    void UpdateProduction(){
        Production = Amount.multiply(BigInteger.valueOf(ProductionBoost));
    }

    void BuildNewFactory(BigInteger _Amount){
        Amount = Amount.add(_Amount);
        UpdateProduction();
    }

    void UpgradeFactory(int _Boost){
        ProductionBoost = _Boost;
        UpdateProduction();
    }

}

public class Index extends JFrame implements ActionListener {

    // Icons For Some Factories
    public ImageIcon Log = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Log.png")));
    public ImageIcon Axe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Axe.png")));
    public ImageIcon Stone = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Stone.png")));
    public ImageIcon Pickaxe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Pickaxe.png")));
    public ImageIcon Ore = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Ore.png")));
    public ImageIcon Hammer = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Hammer.png")));
    public ImageIcon Anvil = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Anvil.png")));
    public ImageIcon Sword = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Sword.png")));
    public ImageIcon Bow = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Bow.png")));
    public ImageIcon Unknown = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Unknown.png")));

    public static Index MainWindow;

    public static final int MANUFACTURE_COUNT = 999; // <-- Game Limit

    public static int PlayerLevel = 1, UpgradePoints = 5, CurrentTick = 0;

    public static BigInteger FinalOutcome = new BigInteger("0"); // <-- Money Container

    public static Manager[] Supervisor = new Manager[MANUFACTURE_COUNT]; // Array Containing All Managers
    public static ManagerBlock[] Office = new ManagerBlock[MANUFACTURE_COUNT]; // Array Containing All Manager Blocks

    public static Factory[] Building = new Factory[MANUFACTURE_COUNT]; // Array Containing All Factories
    public static FactoryBlock[] Plot = new FactoryBlock[MANUFACTURE_COUNT]; // Array Containing All Factory Blocks

    public static JPanel FactoryContainer, ManagerContainer; // Containers For Factories And Managers

    public static CButton AboutPlayer; // <- All Info Display

    Index() {

        super("Idle Game!");
        setBounds(0, 0, 450, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        this.setBackground(Color.DARK_GRAY);

        JTabbedPane MainContainer = new JTabbedPane();

        FactoryContainer = new JPanel(new GridLayout(MANUFACTURE_COUNT,1,5,5));
        JScrollPane FactoryScroll = new JScrollPane(FactoryContainer);

        ManagerContainer = new JPanel(new GridLayout(MANUFACTURE_COUNT,1,5,5));
        JScrollPane ManagerScroll = new JScrollPane(ManagerContainer);

        MainContainer.addTab("Factory Pane", null, FactoryScroll,"Factory Pane");
        MainContainer.addTab("Upgrade Pane", null, ManagerScroll,"Upgrade Pane");

        FactoryContainer.setBackground(Color.DARK_GRAY);
        ManagerContainer.setBackground(Color.DARK_GRAY);

        ImageIcon ManagerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Manager.png")));
        ImageIcon Background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Background.png")));
        Font CustomFont = new Font("Arial", Font.BOLD, 12);
        Dimension ButtonSize = new Dimension(96,96);

        JPanel Summary = new JPanel(new BorderLayout());
        JPanel Main = new JPanel(new BorderLayout());

        CButton Player = new CButton(new ImageIcon[]{ Background,ManagerIcon },CustomFont,Color.WHITE,null,ButtonSize);
        Player.setBorder(null);

        AboutPlayer = new CButton(new ImageIcon[]{ Background },CustomFont,Color.WHITE,new String[]{"Level: " + PlayerLevel,"Upgrade Points: 0","Money: 0"},ButtonSize);
        AboutPlayer.UpdateCentering();
        AboutPlayer.UpdateSpacing(3);
        AboutPlayer.setBorder(null);

        Summary.add(Player, BorderLayout.WEST);
        Summary.add(AboutPlayer, BorderLayout.CENTER);

        Main.add(MainContainer,BorderLayout.CENTER);
        Main.add(Summary,BorderLayout.NORTH);

        AddHigherRankFactory();

        setContentPane(Main);
        setVisible(true);
    }

    public void AddHigherRankFactory(){
        for (byte i = 0; i < PlayerLevel; i++) {
            if (Supervisor[i] == null) { // Check if Supervisor with i id already exists
                Supervisor[i] = new Manager();
                Building[i] = new Factory();
                Office[i] = new ManagerBlock();
                Plot[i] = new FactoryBlock();

                Building[i].TicksPerProgress = (16 * (i + 1));
                Building[i].UpdateProduction();
                Building[i].LastProgress = 0;

                switch (i) {
                    case 0 -> Plot[i].RunFactory.setIcon(Log);
                    case 1 -> Plot[i].RunFactory.setIcon(Axe);
                    case 2 -> Plot[i].RunFactory.setIcon(Stone);
                    case 3 -> Plot[i].RunFactory.setIcon(Pickaxe);
                    case 4 -> Plot[i].RunFactory.setIcon(Ore);
                    case 5 -> Plot[i].RunFactory.setIcon(Hammer);
                    case 6 -> Plot[i].RunFactory.setIcon(Anvil);
                    case 7 -> Plot[i].RunFactory.setIcon(Sword);
                    case 8 -> Plot[i].RunFactory.setIcon(Bow);
                    default -> Plot[i].RunFactory.setIcon(Unknown);
                }

                Plot[i].RunFactory.setBackground(Color.CYAN);
                Plot[i].UpdateBlock(Building[i].Amount, Building[i].Production);
                Plot[i].RunFactory.addActionListener(this);
                Plot[i].BuyFactoryButton.addActionListener(this);

                Office[i].AutomateButton.addActionListener(this);
                Office[i].SpeedBoostButton.addActionListener(this);
                Office[i].ProductionBoostButton.addActionListener(this);

                FactoryContainer.add(Plot[i]);
                ManagerContainer.add(Office[i]);
            }
        }
    } // This Function Adds New Factory And Manager

    public static String ShortValue(BigInteger _Tmp, int _LowerLimit){

        BigDecimal _BigIntContainer = new BigDecimal(_Tmp);
        BigDecimal _OutputValueContainer;
        String Output = "" + _Tmp;

        for (int j = 70; j > _LowerLimit; j--) {
            _OutputValueContainer = _BigIntContainer.divide(new BigDecimal("1000").pow(j),3, RoundingMode.CEILING); // Divide Amount by 1000

           if (_OutputValueContainer.compareTo(new BigDecimal("1")) > 0 && _OutputValueContainer.compareTo(new BigDecimal("999")) <= 0) { // If Divided Amount is between 0 and 999
               if (j == 1) {
                    Output = _OutputValueContainer + " Thousand ";
                }
                else if (j == 2) {
                    Output = _OutputValueContainer + " Million ";
                }
                else if (j == 3) {
                    Output = _OutputValueContainer + " Billion ";
                }
                else if (j == 4) {
                    Output = _OutputValueContainer + " Trillion ";
                }
                else {
                    Output = _OutputValueContainer + " * 1000^" + j; // Assign Title
                }
                break;
            }

        } // Look For Shorter Way Of Displaying Amount

        return Output;

    } // This Function Makes Displayed Value Shorter

    public static void Production(){

        CurrentTick++; // Update ticks

        // For Every Visible Factory
        for(int i = 0; i < PlayerLevel; i++) {

            // Check if factory is running
            if (!Supervisor[i].ManuallyTriggered && !Supervisor[i].AutomateFactory) { continue; }

            // Check How Much Ticks Passed Since Last Production Progress
            if (CurrentTick - Building[i].LastProgress > Building[i].TicksPerProgress) {

                Building[i].LastProgress = CurrentTick; // Set Last Progress Tick To Current Tick

                // Call Make Progress Method And Check If Progress equals 100%
                if (Plot[i].MakeProgress()) {
                    // First Factory Adds Production Value To Money Amount
                    if (i == 0) {
                        FinalOutcome = FinalOutcome.add(Building[i].Production);
                        AboutPlayer.UpdateText(new String[]{"Level: " + PlayerLevel, "Upgrade Points: " + UpgradePoints, "Money: " + ShortValue(FinalOutcome,15)});
                        Supervisor[i].ManuallyTriggered = false;
                    }
                    // Rest Factories Build Factories Rank Below Them
                    else {

                        Building[i - 1].BuildNewFactory(Building[i].Production); // Build Lower Tier Factory
                        Building[i - 1].UpdateProduction(); // Update Their Status

                        Plot[i - 1].UpdateBlock(ShortValue(Building[i - 1].Amount,0), ShortValue(Building[i - 1].Production,8));

                        Supervisor[i].ManuallyTriggered = false;

                        FactoryContainer.repaint();

                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        MainWindow = new Index();

        while (PlayerLevel < 1000){
                Production();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object Source = e.getSource();
        for(int i = 0; i < PlayerLevel; i++) {

            if (Source == Plot[i].RunFactory) {
                Supervisor[i].ManuallyTriggered = true;
            }
            else if (Source == Plot[i].BuyFactoryButton) {
                boolean Status = false;
                if (i == 0) {
                    if (FinalOutcome.compareTo(new BigInteger("10")) > -1) {
                        FinalOutcome = FinalOutcome.subtract(new BigInteger("10"));
                        Status = true;
                    }
                }
                else {
                    if (Building[i - 1].Amount.compareTo(new BigInteger("10")) > 0) {
                        Building[i - 1].Amount = Building[i - 1].Amount.subtract(new BigInteger("10"));
                        Building[i - 1].UpdateProduction();
                        Plot[i - 1].UpdateBlock(Building[i-1].Amount, Building[i-1].Production);
                        Plot[i - 1].UpdateBlock(ShortValue(Building[i - 1].Amount,0), ShortValue(Building[i - 1].Production,8));

                        Status = true;
                    }
                }
                if (Status) {
                    Building[i].BuildNewFactory(new BigInteger("1"));
                    Building[i].UpdateProduction();
                    Plot[i].UpdateBlock(Building[i].Amount, Building[i].Production);
                    Plot[i].UpdateBlock(ShortValue(Building[i].Amount,0), ShortValue(Building[i].Production,8));

                    if (i == (PlayerLevel - 1)){
                        PlayerLevel++;
                        UpgradePoints += 10;
                        MainWindow.AddHigherRankFactory();
                    }
                }
            }
            else if (Source == Office[i].AutomateButton) {
                if (UpgradePoints > 0) {
                    Supervisor[i].Automate();
                    Office[i].Automate();
                    UpgradePoints--;
                }
            }
            else if (Source == Office[i].SpeedBoostButton) {
                if (UpgradePoints > 0) {
                    if (Supervisor[i].SpeedBoost < 8192) {
                        Supervisor[i].UpgradeSpeed();
                        Office[i].UpgradeSpeed(Supervisor[i].SpeedBoost);
                        Building[i].TicksPerProgress /= Supervisor[i].SpeedBoost;
                        UpgradePoints--;
                    }
                }
            }
            else if (Source == Office[i].ProductionBoostButton) {
                if (UpgradePoints > 0) {
                    if (Supervisor[i].ProductionBoost < 8192) {
                        Supervisor[i].UpgradeProduction();
                        Office[i].UpgradeProduction(Supervisor[i].ProductionBoost);
                        Building[i].UpgradeFactory(Supervisor[i].ProductionBoost);
                        Plot[i].UpdateBlock(Building[i].Amount, Building[i].Production);
                        UpgradePoints--;
                    }
                }
            }
        }
        AboutPlayer.UpdateText(new String[]{"Level: " + PlayerLevel, "Upgrade Points: " + UpgradePoints, "Money: " + ShortValue(FinalOutcome,15)});
    }
}
