package uniandes.dpoo.taller4.view;
import uniandes.dpoo.taller4.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Random;

public class LightsOutGameInterface extends JFrame {

    public static int matrixSize = 5;
    private Tablero onOffMatrix;
    private  static int cellSize;
    //private static SectionUp sectionUp;
    private PBoard board;
    private SectionDown sectionDown;

    private Top10 top10 = new Top10();
    private static int levelDifficulty = 3;

    private JPanel panel;
    File file = new File("/Users/fodepixofarfan/Downloads/T4/data/top10.txt");
    public LightsOutGameInterface() {

        onOffMatrix = new Tablero(matrixSize);

        setTitle("LightsOut");
        top10.cargarRecords(file);//Initialize the file information

        // When the program is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e )
            {

                try {
                    top10.salvarRecords(file);
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        //(END)
        setSize(700, 700);

        panel = new JPanel(new BorderLayout());

        SectionUp sectionUp = new SectionUp();//#1

        sectionDown = new SectionDown();//#4
        SectionRight sectionRight = new SectionRight(); //#2

        sectionRight.setPreferredSize(new Dimension(getWidth()/6,getHeight()));
        sectionUp.setPreferredSize(new Dimension(getWidth(), getHeight() / 10));
        //Border
        sectionUp.setBorder(new LineBorder(Color.BLACK, 3));
        sectionRight.setBorder(new LineBorder(Color.BLACK, 3));

        board = new PBoard();//#3

        sectionUp.setBackground(Color.CYAN);
        sectionDown.setBackground(Color.GREEN);
        //sectionRight.setBackground(Color.CYAN);
        board.setBackground(Color.WHITE);

        //sectionDown.add(new JLabel("Section Down"));

        panel.add(sectionUp,BorderLayout.NORTH);
        panel.add(sectionDown,BorderLayout.SOUTH);
        panel.add(sectionRight,BorderLayout.EAST);
        panel.add(board,BorderLayout.CENTER);

        add(panel);
    }

    public static void setMatrixSize(int matrixSize) {
        LightsOutGameInterface.matrixSize = matrixSize;
    }

    public class PBoard extends JPanel {
        public int  counterLights;

        public PBoard() {
            onOffMatrix.desordenar(levelDifficulty);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    counterLights++;
                    sectionDown.setNumberG();
                    int row = e.getY() / cellSize;
                    int col = e.getX() / cellSize;

                    onOffMatrix.jugar(row,col);
                    repaint();

                    if (onOffMatrix.tableroIluminado()){
                        top10.agregarRegistro(sectionDown.getName(),onOffMatrix.calcularPuntaje());
                    }
                }
            });
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            cellSize = getWidth()/ matrixSize;
            for (int row = 0; row < matrixSize; row++) {
                for (int col = 0; col < matrixSize; col++) {

                    if (onOffMatrix.darTablero()[row][col]) {
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.fillRoundRect(col * cellSize, row * cellSize, cellSize, cellSize, 20, 20);
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(col * cellSize, row * cellSize, cellSize, cellSize, 20, 20);

                }
            }
        }

        public void updateSizeBoard(int newSize){
            onOffMatrix = new Tablero(newSize);
            repaint();
        }

        public void setLevelDifficulty(int level){
            updateSizeBoard(matrixSize);
            levelDifficulty = level;
            onOffMatrix.desordenar(levelDifficulty);
        }
    }

    public class SectionUp extends JPanel{
        public JRadioButton easy;
        public SectionUp(){

            JPanel panelSectionUp = new JPanel();

            JLabel sizeOptions = new JLabel("Size:");
            JLabel difficulty = new JLabel("Difficulty:");

            JRadioButton difficult = new JRadioButton("Difficult");
            JRadioButton medium =  new JRadioButton("Medium");
            easy = new JRadioButton("Easy");
            selectedEasyOption();
            ButtonGroup difficultyGroup = new ButtonGroup();

            difficultyGroup.add(difficult);
            difficultyGroup.add(medium);
            difficultyGroup.add(easy);

            sizeOptions.setHorizontalAlignment(JLabel.LEFT);
            difficulty.setHorizontalAlignment(JLabel.CENTER);

            panelSectionUp.setBackground(Color.cyan);

            String[] options = {"5x5","6x6","7x7","8x8"};
            JComboBox<String> cascadeOptions = new JComboBox<>(options);

            // Difficult
            difficult.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.setLevelDifficulty(10);
                }
            });
            //Medium
            medium.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   board.setLevelDifficulty(40);
                }
            });
            //Easy
            easy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.setLevelDifficulty(80);
                }
            });

            //Size
            cascadeOptions.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String optionSelected = (String) cascadeOptions.getSelectedItem();
                    board.counterLights = 0;
                    sectionDown.setNumberG();
                    if (optionSelected.equals("5x5")){
                        matrixSize = 5;
                        board.updateSizeBoard(matrixSize);
                        onOffMatrix.desordenar(levelDifficulty);
                    } else if (optionSelected.equals("6x6")) {
                        matrixSize = 6;
                        board.updateSizeBoard(matrixSize);
                        onOffMatrix.desordenar(levelDifficulty);
                    } else if (optionSelected.equals("7x7")) {
                        matrixSize = 7;
                        board.updateSizeBoard(matrixSize);
                        onOffMatrix.desordenar(levelDifficulty);
                    } else if (optionSelected.equals("8x8")) {
                        matrixSize = 8;
                        board.updateSizeBoard(matrixSize);
                        onOffMatrix.desordenar(levelDifficulty);
                    }
                }
            });

            panelSectionUp.add(sizeOptions,FlowLayout.LEFT);
            panelSectionUp.add(cascadeOptions);

            panelSectionUp.add(difficulty);
            panelSectionUp.add(difficult);
            panelSectionUp.add(medium);
            panelSectionUp.add(easy);

            add(panelSectionUp);
        }

        public void selectedEasyOption(){
            easy.setSelected(true);
        }

    }
    static class NumberedListCellRenderer extends JLabel implements ListCellRenderer<Object> {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            String emoji = "";
            if (index == 0) {
                emoji = "\uD83C\uDFC6" + " ";
            } else if (index == 1) {
                emoji = "\uD83C\uDFC5" + " ";
            } else if (index == 2) {
                emoji = "\uD83E\uDD48" + " ";
            } else if (index == list.getModel().getSize() - 1) {
                emoji = "\uD83D\uDE41" + " ";
            }
            String[] splitString = value.toString().replace(".","").split(" ");
            setText(emoji+" "+(index + 1)+".   "+splitString[0]+"   " +splitString[2]);

            if (index < 3) {
                setFont(getFont().deriveFont(Font.BOLD));
                setForeground(new Color(0, 128, 0));
            } else {
                setForeground(Color.RED);
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
            }

            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalAlignment(SwingConstants.CENTER);

            return this;
        }
    }

    public class SectionRight extends JPanel{
        public SectionRight(){

            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            JPanel panelSectionRight = new JPanel();
            panelSectionRight.setBackground(Color.lightGray);

            JButton new_ = new JButton("New");
            JButton reset = new JButton("Reset");
            JButton top_10 = new JButton("TOP 10");
            JButton changePlayer = new JButton("Change Player");


            Collection<RegistroTop10> records = top10.darRegistros();
            RegistroTop10[] arrayRecords = records.toArray(new RegistroTop10[0]);

            JList<RegistroTop10> listRecords = new JList<>(arrayRecords);
            listRecords.setCellRenderer(new NumberedListCellRenderer());

            listRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



            JScrollPane scrollPane = new JScrollPane(listRecords);
            scrollPane.setPreferredSize(new Dimension(250, 150));


            new_.setAlignmentX(Component.CENTER_ALIGNMENT);
            reset.setAlignmentX(Component.CENTER_ALIGNMENT);
            top_10.setAlignmentX(Component.CENTER_ALIGNMENT);
            changePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

            new_.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.updateSizeBoard(matrixSize);
                    onOffMatrix.desordenar(matrixSize);
                    board.counterLights=0;
                    sectionDown.setNumberG();
                }
            });

            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onOffMatrix.reiniciar();
                    board.counterLights =0;
                    sectionDown.setNumberG();
                    board.repaint();
                }
            });

            top_10.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(panel, scrollPane,"Top 10 Players",JOptionPane.PLAIN_MESSAGE);

                }
            });

            changePlayer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sectionDown.playerName.setText(sectionDown.setNewName());
                    repaint();
                }
            });

            panelSectionRight.add(new_);
            panelSectionRight.add(reset);
            panelSectionRight.add(top_10);
            panelSectionRight.add(changePlayer);
            add(Box.createVerticalGlue());
            add(panelSectionRight);
            add(Box.createVerticalGlue());
        }
    }

    public class SectionDown extends JPanel{
        private int numberG ;
        private static String name;

        private JLabel playerName;
        private JLabel gamesNumber;
        public SectionDown(){

            setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

            JPanel panelSectionDown = new JPanel();
            panelSectionDown.setBackground(Color.lightGray);

            JLabel games = new JLabel("Games: ");
            numberG = 0;
            gamesNumber = new JLabel(""+numberG);

            JLabel player = new JLabel("Player: ");

            playerName = new JLabel(setNewName());

            panelSectionDown.add(games);
            panelSectionDown.add(gamesNumber);
            panelSectionDown.add(player);
            panelSectionDown.add(playerName);
            add(panelSectionDown);

        }
        public String setNewName(){
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random random = new Random();

            StringBuilder stringModified = new StringBuilder();

            for (int i = 0;i<3;i++){
                stringModified.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            name = String.valueOf(stringModified);
            return name;
        }
        public void setName(String newName){this.name = newName;};

        public String getName(){return name;}
        public void setNumberG(){
            gamesNumber.setText(""+board.counterLights);
        }
    }

   public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            LightsOutGameInterface game = new LightsOutGameInterface();
            game.setVisible(true);
        });
   }


}