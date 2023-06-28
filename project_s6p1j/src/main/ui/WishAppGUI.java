package ui;

import model.*;
import persistance.JsonWriter;
import ui.tools.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// GUI for the app
public class WishAppGUI extends JFrame {

    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    public static final int STYLE = Font.PLAIN;
    public static final int TITLE_SIZE = 30;

    JPanel grid;
    JPanel loadingScreen;

    private Balance balance;
    private JLabel balanceNum;

    private WishList wishList;
    private PurchaseList purList;

    private DefaultTableModel depositTable;
    private DefaultTableModel purchaseTable;
    private DefaultTableModel wishTable;

    private static final String JSON_STORE = "./data/wishAppData.json";
    private JsonWriter jsonWriter;

    // EFFECTS: constructs a wish app GUI
    public WishAppGUI() {
        super("Wish"); //change title
        initFields();
        initGraphics();

    }

    // MODIFIES: this
    // EFFECTS: initializes the fields and creates empty data tables
    private void initFields() {
        balance = new Balance();
        balanceNum = new JLabel();
        wishList = new WishList();
        purList = new PurchaseList();

        String[] depositHeaders = {"Amount", "Type"};
        depositTable = new DefaultTableModel(depositHeaders, 0);

        String[] purchaseHeaders = {"Name", "Price", "Category"};
        purchaseTable = new DefaultTableModel(purchaseHeaders, 0);

        String[] wishHeaders = {"Name", "Price", "Category", "Allocated"};
        wishTable = new DefaultTableModel(wishHeaders, 0);

        jsonWriter = new JsonWriter(JSON_STORE);

    }

    // MODIFIES: this
    // EFFECTS: displays splash screen and graphics
    private void initGraphics() {

        loadingScreen = new JPanel(new BorderLayout());
        try {
            BufferedImage logo = ImageIO.read(new File("data/wish_logo.png"));
            JLabel logoLabel = new JLabel(new ImageIcon(logo));
            loadingScreen.add(logoLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            //
        }

        getContentPane().add(loadingScreen);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new SaveReminder());

        int delay = 2000; //milliseconds
        ActionListener taskPerformer = evt -> {
            getContentPane().remove(loadingScreen);
            grid = new JPanel(new GridLayout(2, 2, 10, 10));
            getContentPane().add(grid);
            initComponents();
        };
        Timer t = new Timer(delay, taskPerformer);
        t.setRepeats(false);
        t.start();

    }

    private class SaveReminder extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            int option = JOptionPane.showOptionDialog(
                    WishAppGUI.this,
                    "Save data to file before exiting?",
                    "Save data to file", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, null, null);
            if (option == JOptionPane.NO_OPTION) {
                LogPrinter lp = new LogPrinter();
                lp.printLog(EventLog.getInstance());
                System.exit(0);
            } else if (option == JOptionPane.YES_OPTION) {

                try {
                    jsonWriter.open();

                    jsonWriter.writeAll(balance, wishList, purList);
                    jsonWriter.close();

                    JOptionPane.showMessageDialog(null,
                            "Your data has been saved" + "\nApplication will now close",
                            "Data saved to file", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException ex) {
                    //
                }

                LogPrinter lp = new LogPrinter();
                lp.printLog(EventLog.getInstance());
                System.exit(0);

            }
        }
    }

    // EFFECTS: displays the graphics in each quadrant of the JFrame
    private void initComponents() {

        initTopLeft();
        initTopRight();
        initBotLeft();
        initBotRight();

    }

    // MODIFIES: this
    // EFFECTS: displays the graphics and adds action listeners
    @SuppressWarnings("methodlength")
    private void initTopLeft() {
        JPanel topLeftQuad = new JPanel();
        GroupLayout layout = new GroupLayout(topLeftQuad);
        topLeftQuad.setLayout(layout);
        grid.add(topLeftQuad);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel balanceLabel = new JLabel("Available Balance: ");
        balanceLabel.setFont(new Font("Arial", STYLE, TITLE_SIZE));

        balanceNum.setText("$" + balance.getCurrentBalance());
        balanceNum.setFont(new Font("Arial", STYLE, HEIGHT / 8));

        JLabel recentsLabel = new JLabel("Transactions");
        recentsLabel.setFont(new Font("Arial", STYLE, TITLE_SIZE - 10));

        JTable depositJTable = new JTable(depositTable);
        depositJTable.setDefaultEditor(Object.class, null);
        JScrollPane depositScroller = new JScrollPane(depositJTable);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new DepositHandler(depositTable, balanceNum, balance));

        // layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(balanceLabel, GroupLayout.Alignment.LEADING)
                        .addComponent(balanceNum, GroupLayout.Alignment.CENTER)
                        .addComponent(depositButton, GroupLayout.Alignment.TRAILING)
                        .addComponent(recentsLabel)
                        .addComponent(depositScroller))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(balanceLabel)
                .addComponent(balanceNum)
                .addComponent(depositButton)
                .addComponent(recentsLabel)
                .addComponent(depositScroller)
        );
    }

    // MODIFIES: this
    // EFFECTS: displays the graphics and adds action listeners
    @SuppressWarnings("methodlength")
    private void initTopRight() { //wishes

        JPanel topRightQuad = new JPanel();
        GroupLayout layout = new GroupLayout(topRightQuad);
        topRightQuad.setLayout(layout);
        grid.add(topRightQuad);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel wishlistLabel = new JLabel("Wishlist");
        wishlistLabel.setFont(new Font("Arial", STYLE, TITLE_SIZE));

        JTable wishesJTable = new JTable(wishTable);
        wishesJTable.setDefaultEditor(Object.class, null);
        JScrollPane wishlistScroller = new JScrollPane(wishesJTable);

        JButton allocateButton = new JButton("Save Amount for Item");
        allocateButton.addActionListener(new SaveToWishHandler(wishTable, wishList, balance, balanceNum));

        JButton purchaseWishButton = new JButton("Purchase");
        purchaseWishButton.addActionListener(
                new PurchaseWishHandler(wishTable, wishList, purchaseTable, purList));

        JButton addWishButton = new JButton("Add");
        addWishButton.addActionListener(new AddWishHandler(wishTable, wishList));

        //layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(wishlistLabel, GroupLayout.Alignment.LEADING)
                        .addComponent(wishlistScroller)

                        .addComponent(addWishButton, GroupLayout.Alignment.LEADING)
                        .addComponent(allocateButton, GroupLayout.Alignment.TRAILING)
                        .addComponent(purchaseWishButton, GroupLayout.Alignment.TRAILING))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(wishlistLabel)
                        .addComponent(allocateButton))
                .addComponent(wishlistScroller)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addWishButton)
                        .addComponent(purchaseWishButton))
        );
    }

    // MODIFIES: this
    // EFFECTS: displays the graphics and adds action listeners
    @SuppressWarnings("methodlength")
    private void initBotLeft() { //purchases

        JPanel botLeftQuad = new JPanel();
        GroupLayout layout = new GroupLayout(botLeftQuad);
        botLeftQuad.setLayout(layout);
        grid.add(botLeftQuad);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel purchasesLabel = new JLabel("Purchases");
        purchasesLabel.setFont(new Font("Arial", STYLE, TITLE_SIZE));

        JTable purchasesJTable = new JTable(purchaseTable);
        purchasesJTable.setDefaultEditor(Object.class, null);
        JScrollPane purchaselistScroller = new JScrollPane(purchasesJTable);

        JButton addPurchaseButton = new JButton("Add");
        addPurchaseButton.addActionListener(
                new AddPurchaseHandler(purchaseTable, balanceNum, balance, purList));

        //layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(purchasesLabel)
                        .addComponent(purchaselistScroller)
                        .addComponent(addPurchaseButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(purchasesLabel)
                .addComponent(purchaselistScroller)
                .addComponent(addPurchaseButton)
        );

    }

    // MODIFIES: this
    // EFFECTS: displays the graphics and adds action listeners
    private void initBotRight() {

        JPanel botRightQuad = new JPanel();
        BorderLayout borLayout = new BorderLayout();
        botRightQuad.setLayout(borLayout);
        grid.add(botRightQuad);

        JButton loadButton = new JButton("load");
        JButton saveButton = new JButton("save");

        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("data/wish_logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel logoLabel = new JLabel(new ImageIcon(logo));

        JPanel saveLoad = new JPanel();
        saveLoad.setLayout(new FlowLayout());
        saveLoad.add(loadButton);
        saveLoad.add(saveButton);

        loadButton.addActionListener(new LoadDataHandler(balance, wishList, purList,
                balanceNum, depositTable, purchaseTable, wishTable, this));

        saveButton.addActionListener(new SaveDataHandler(balance, wishList, purList));

        botRightQuad.add(logoLabel);
        botRightQuad.add(saveLoad, BorderLayout.SOUTH);

    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public void setPurList(PurchaseList purList) {
        this.purList = purList;
    }

}

