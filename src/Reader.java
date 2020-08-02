import com.sun.jdi.event.MonitorWaitEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Reader extends Component {
    private JButton openButton;
    private JPanel panelJ;
    private JTextField wordField;
    private JButton previousPageButton;
    private JButton nextPageButton;
    private JLabel totalPages;
    private JLabel slash;
    private JButton playButton;
    private JButton pauseButton;
    private JTextField currentPage;
    private PDFReader pDFReader;
    private String[] pdfPageWords;
    private int wordNo;
    private boolean documentOpened;
    private WordSpeedController wordSpeedController;
    private void loadWords()
    {
        pdfPageWords=pDFReader.pageString.split("(?<=\\W)");//requires tuning!!
        wordNo=-1;
        currentPage.setText(""+pDFReader.pageNo);
    }
    private void showNextWord()
    {
        if(wordNo<pdfPageWords.length-1)
        {
            wordNo++;
            wordField.setText(pdfPageWords[wordNo]);
        }
        else
            wordSpeedController.timer.cancel();
    }
    private void showPreviousWord()
    {
        if(wordNo>0 && wordNo<pdfPageWords.length)
        {
            wordNo--;
            wordField.setText(pdfPageWords[wordNo]);
        }
    }
    class WordFlasher extends TimerTask
    {
        public void run()
        {
                showNextWord();
        }
    }
    static class WordSpeedController
    {
        public Timer timer;
        WordSpeedController()
        {
            timer=new Timer();
        }
    }
    public Reader() {
        wordSpeedController=new WordSpeedController();
        documentOpened=false;
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser openFile=new JFileChooser();
                int ret=openFile.showOpenDialog(Reader.this);
                if(ret==JFileChooser.APPROVE_OPTION)
                {
                    try {
                        pDFReader=new PDFReader(openFile.getSelectedFile());
                        loadWords();
                        totalPages.setText(""+pDFReader.pDDocument.getNumberOfPages());
                        documentOpened=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(documentOpened) {
                    pDFReader.previousPage();
                    loadWords();
                }
            }
        });
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(documentOpened) {
                    pDFReader.nextPage();
                    loadWords();
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(documentOpened) {
                    wordSpeedController = new WordSpeedController();
                    wordSpeedController.timer.schedule(new WordFlasher(), 100, 200);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                wordSpeedController.timer.cancel();
            }
        });
    }
    public static void main(String[] args) {
        JFrame frameJ=new JFrame("PDF Reader");
        frameJ.setContentPane(new Reader().panelJ);
        frameJ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameJ.pack();
        frameJ.setVisible(true);

    }
}
