package Base;

import javax.swing.*;
import java.awt.*;

/**
 * Created by aa on 03 May 2017.
 */
public class Background extends JPanel {
    public static final int SCALED = 0;
    public static final int TILED = 1;
    public static final int ACTUAL = 2;

    private Paint paint;
    private Image image;
    private int style = TILED;
    private float alignmentX = 0.5f;
    private float alignmentY = 0.5f;
    private boolean isTransparent = true;

    public Background(Image image) {
        this(image, TILED);
    }

    public Background(Image image, int style) {
        setImage(image);
        setStyle(style);
        setLayout(new BorderLayout());
    }

    public Background(Image image, int style, float alignmentX, float alignmentY) {
        setImage(image);
        setStyle(style);
        setImageAlignmentX(alignmentX);
        setImageAlignmentY(alignmentY);
        setLayout(new BorderLayout());
    }

    public Background(Paint paint) {
        setPaint(paint);
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setStyle(int style) {
        this.style = style;
        repaint();
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        repaint();
    }

    public void setImageAlignmentX(float alignmentX) {
        this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
        repaint();
    }

    public void setImageAlignmentY(float alignmentY) {
        this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;
        repaint();
    }

    public void add(JComponent component) {
        add(component, null);
    }

    public void add(JComponent component, Object constraints) {
        if (isTransparent) {
            makeComponentTransparent(component);
        }

        super.add(component, constraints);
    }

    public void addOpaque(JComponent component, Object constraints) {
        super.add(component, constraints);
    }

    public void setTransparentAdd(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    private void makeComponentTransparent(JComponent component) {
        component.setOpaque(false);

        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            JViewport viewport = scrollPane.getViewport();
            viewport.setOpaque(false);
            Component c = viewport.getView();

            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (paint != null) {
            Dimension d = getSize();
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(paint);
            g2.fill(new Rectangle(0, 0, d.width, d.height));
        }

        if (image == null)
            return;

        switch (style) {
            case SCALED:
                drawScaled(g);
                break;
            case TILED:
                drawTiled(g);
                break;
            case ACTUAL:
                drawActual(g);
                break;
            default:
                drawScaled(g);
        }
    }

    private void drawScaled(Graphics g) {
        Dimension d = getSize();
        g.drawImage(image, 0, 0, d.width, d.height, null);
    }

    private void drawTiled(Graphics g) {
        Dimension d = getSize();
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        for (int x = 0; x < d.width; x += width) {
            for (int y = 0; y < d.height; y += height) {
                g.drawImage(image, x, y, null, null);
            }
        }
    }

    private void drawActual(Graphics g) {
        Dimension d = getSize();
        Insets insets = getInsets();

        int width = d.width - insets.left - insets.right;
        int height = d.height - insets.top - insets.left;

        float x = (width - image.getWidth(null)) * alignmentX;
        float y = (height - image.getHeight(null)) * alignmentY;

        g.drawImage(image, (int) x + insets.left, (int) y + insets.top, this);
    }
}
