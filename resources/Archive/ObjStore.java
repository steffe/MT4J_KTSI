package store;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch.mitoco.Attribut;
import ch.mitoco.Attributs;



public class ObjStore {{
	try {
        // create a JAXBContext capable of handling classes generated into
        // this package
        JAXBContext jc = JAXBContext.newInstance( "ktsi.ch.mitoco" );
        
        // create an Unmarshaller
        Unmarshaller u = jc.createUnmarshaller();
        for( int i=0; i<1; i++ ){
        // unmarshal a po instance document into a tree of Java content
        // objects composed of classes from this  package.
        	Attributs at = (Attributs)u.unmarshal(new File("AttributTemplates.xml"));
    
        // examine some of the content in the PurchaseOrder
        System.out.println( "Attribut: " );
        
        // display the shipping address
        Attribut Attribut = at.getAttribut();
        displayAttribut( Attribut );
        
        // display the items
        //List <LineItem> items = po.getLineItem();
        //displayItems( items );
        //displayTotal( items );
        }
    } catch( JAXBException je ) {
        je.printStackTrace();
    }
}
	public static void displayAttribut( Attribut atr ) {
    System.out.println( "\t" + atr.getName() );
    System.out.println( "\t" + atr.getAddress() + "\n"); 
	}


}
