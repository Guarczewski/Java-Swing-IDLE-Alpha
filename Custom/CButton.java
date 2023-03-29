package Custom;

import javax.swing.*;
import java.awt.*;

public class CButton extends JButton {

    protected ImageIcon[] Images;
    protected boolean CenterText;
    protected Font CustomFont;
    protected Color FontColor;
    protected String[] Lines;
    protected Dimension Size;
    protected int Spacing;
    protected int x;
    protected int y;

    public CButton(ImageIcon[] _Images, Font _CustomFont, Color _FontColor, String[] _Lines, Dimension _Size){
        this.setPreferredSize(_Size);
        Images = _Images;
        CustomFont = _CustomFont;
        FontColor = _FontColor;
        CenterText = true;
        Lines = _Lines;
        Size = _Size;
        Spacing = 4;
    }

    public void UpdateCentering(){
        CenterText = !CenterText;
        repaint();
    }

    public void UpdateSpacing(int _Value){
        Spacing = _Value;
        repaint();
    }

    public void UpdateText(String[] _Lines){
        Lines = _Lines;
        repaint();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        this.setForeground(FontColor);

        Rectangle Box = new Rectangle(Size);

        FontMetrics metrics = g.getFontMetrics(CustomFont);

        g.setFont(CustomFont); // Set the font

        for (int i = 0; i < Images.length; i++) {
            if (i > 0) { g.drawImage(Images[i].getImage(), 2, 0, null); }
            else { g.drawImage(Images[i].getImage(), 0, 0, null); }
        }

        if (Lines != null) {
            y = Box.y + ((Box.height - metrics.getHeight()) / 2) + metrics.getAscent(); // Determine the Y coordinate for the text
            if (CenterText) { x = Box.x + (Box.width - metrics.stringWidth(Lines[0])) / 2; }// Determine the X coordinate for the text }
            else { x = 15; }
            g.drawString(Lines[0], x, y - ((Size.height / Spacing))); // Draw String

            if (CenterText) { x = Box.x + (Box.width - metrics.stringWidth(Lines[1])) / 2; }// Determine the X coordinate for the text }
            else { x = 15; }
            g.drawString(Lines[1], x, y); // Draw String

            if (CenterText) { x = Box.x + (Box.width - metrics.stringWidth(Lines[2])) / 2; }// Determine the X coordinate for the text }
            else { x = 15; }
            g.drawString(Lines[2], x, y + ((Size.height / Spacing))); // Draw String
        }
    }
}
