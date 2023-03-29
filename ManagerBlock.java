import Custom.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ManagerBlock extends JPanel{

    public static final Byte BUTTON_SIZE = 96;

    public CButton AutomateButton, SpeedBoostButton, ProductionBoostButton;

    ManagerBlock() {

        this.setLayout(new GridLayout());

        Font CustomFont = new Font("Arial", Font.BOLD, 12);
        Dimension ButtonSize = new Dimension(410/4,BUTTON_SIZE);

        ImageIcon ManagerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Manager.png")));
        ImageIcon Background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Stuff/Background.png")));

        CButton Icon = new CButton(new ImageIcon[]{ Background,ManagerIcon },CustomFont,Color.WHITE,null,ButtonSize);
        AutomateButton = new CButton(new ImageIcon[]{ Background },CustomFont,Color.WHITE,new String[]{"Automate","Off",""},ButtonSize);
        SpeedBoostButton = new CButton(new ImageIcon[]{ Background },CustomFont,Color.WHITE,new String[]{"Speed","1",""},ButtonSize);
        ProductionBoostButton = new CButton(new ImageIcon[]{ Background },CustomFont,Color.WHITE,new String[]{"Production","1",""},ButtonSize);

        this.add(Icon);
        this.add(AutomateButton);
        this.add(SpeedBoostButton);
        this.add(ProductionBoostButton);

    }

    public void Automate(){
        AutomateButton.UpdateText(new String[]{"Automate", "On", ""});
    }

    public void UpgradeSpeed(int _Value){
        SpeedBoostButton.UpdateText(new String[]{"Speed","" + _Value,""});
    }

    public void UpgradeProduction(int _Value){
        ProductionBoostButton.UpdateText(new String[]{"Production","" + _Value,""});
    }

}
