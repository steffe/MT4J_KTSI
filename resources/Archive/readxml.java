package store;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch.mitoco.store.generated.Customer;
import ch.mitoco.store.generated.PurchaseOrder;

public class readxml {
	public static Customer readcustomer;
	public static PurchaseOrder at;
	public readxml() {
		
	};
	
	public static void readfile(String filename) {
		try {
			JAXBContext jc = JAXBContext.newInstance( "ch.mitoco.store.generated" );
			Unmarshaller u = jc.createUnmarshaller();
			for( int i=0; i<1; i++ ){
				at = (PurchaseOrder)u.unmarshal(new File("AttributTemplates1.xml"));
			           	        System.out.println( "Attribut: " );
		        
		        //display the shipping address
			    //int Attributs = at.getID();
			     //      	     Attributs Attributs = at.getName();
			    //System.out.println(Attributs);
		        
			    //readcustomer = at.getCustomer();
		
				}
		}
		catch (JAXBException je) {
	        je.printStackTrace();
	    }
	}
	
	public static Customer getObject(int id) {
		readcustomer = at.getCustomer();
		
	    System.out.println("\t" + readcustomer.getName());
	    System.out.println("\t" + readcustomer.getAddress() + "\n"); 
		
		//readxml objekt zurpck
	    return readcustomer;
	    
	}
	
	public static void getAtribut() {
		
	}

}
