import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

public class PDFReader {
    File file;
    PDDocument pDDocument;
    PDPage pDPage;
    PDDocument pageDocument;
    String pageString;
    int pageNo;
    PDFReader(File receivedFile) throws IOException
    {
        file=receivedFile;
        pageDocument=new PDDocument();
        pDDocument=PDDocument.load(file);
        if(pDDocument.getNumberOfPages()>0)
        {
            loadPage(0);
        }
    }
//    public static void main(String[] args) throws IOException
//    {
//        PDDocument pdd=new PDDocument();
//
//        pdd.save("/home/rajduino/Documents/dified.pdf");
//    }
    public void loadPage(int receivedPageNo)  {
        if(pDDocument.getNumberOfPages()>receivedPageNo && receivedPageNo>-1) {
            pageNo=receivedPageNo;
            pDPage = pDDocument.getPage(pageNo);
            removePagesFromPageDocument();
            pageDocument.addPage(pDPage);
            PDFTextStripper pts= null;
            try {
                pts = new PDFTextStripper();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pageString = pts.getText(pageDocument);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void removePagesFromPageDocument()
    {
        while(pageDocument.getNumberOfPages()!=0)
        {
            pageDocument.removePage(0);
        }
    }
    public void nextPage()
    {
        if(pageNo!=pDDocument.getNumberOfPages()-1)
        {
            loadPage(pageNo+1);
        }
    }
    public void previousPage()
    {
        if(pageNo!=0)
        {
            loadPage(pageNo-1);
        }
    }
    public void openFile(File receivedFile) {
        //System.out.println("Hello World");
        //File file=new File("/home/rajduino/Documents/ICSE2018CA.pdf");
        file=receivedFile;
        try {
            pDDocument=PDDocument.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pDDocument.getNumberOfPages()>0) {
            loadPage(0);
        }
//        PDFTextStripper pts=new PDFTextStripper();
        //String text=pts.getText(document);
        //System.out.println(text);

//        PDPage pdp=document.getPage(0);

//        document.addPage(new PDPage());
//        document.save("/home/rajduino/Documents/ICSE2018CAmodified.pdf");
        //document.close();
        //return text;
//        String text;
//        text.s
    }
}
///home/rajduino/Documents/ICSE2018CA.pdf