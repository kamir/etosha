package semanpix;

import java.io.File;
import java.io.InputStream;

/** 
 *  We may use this code here:
 *  
 *  http://www.androidhive.info/2012/07/android-loading-image-from-url-http/
 * 
 * 
 * 
 *  The semantic image loader (SIL) implements the name based
 *  convention and a local image cache to show icons in the GUI
 *  of an semantic application.
 *  
 *  We must separate the context handling and raw data handling 
 *  procedures.
 *  
 *  Context handling means, we must identify the right resources
 *  and than we have to load them depending on the context. Here we
 *  must be highly dynamic. This means the define a life time of
 *  the cache.
 * 
 *  But an LRU appoach seems to be reasonable, especially in case 
 *  of an in memory cache.
 *  
 *  Here we use a disk based cache.
 *  
 * @author training
 *
 */
public class SIL {
	
	static SIL sil = null;
	
	public static SIL getSIL() {
		if ( sil == null ) sil = new SIL();
		return sil;
	} 
	
	public long lifetime = -1;
	
	File localFileBuffer = null;

	/**
	 * Define a local icon buffer folder for semanpics-icons
	 */
	public void init() {
		// where are the local icons stored?
		localFileBuffer = new File( "/storaqe/emulated/0/semanpix.sic" );
	}
	
	/**
	 * This method will load an icon resource from a local buffer.
	 * We use a convention to map a propertyname to a unique filename
	 * stored locally.
	 * 
	 * @param property
	 * @param wiki
	 * @return
	 */
	public InputStream getIconAsStream( String property ) {
		
		String fn = "File:property_" + property + ".png";
		
		// Wiki wiki = new Wiki();
		
		
		/**
		 * 
		 
       *  Fetches a thumbnail of an image file and returns the image data 
       *  in a <tt>byte[]</tt>. Works for external repositories. 
       * 
       *  @param title the title of the image (may contain "File") 
       *  @param width the width of the thumbnail (use -1 for actual width) 
       *  @param height the height of the thumbnail (use -1 for actual height) 
       *  @return the image data or null if the image doesn't exist 
       *  @throws IOException if a network error occurs 
       *  @deprecated expects a file as additional parameter 
       *  @since 0.13 
   
      @Deprecated 
      public byte[] getImage(String title, int width, int height) throws IOException 

		 * 
		 */
		
		
		
		//try to open the local file if it exists 
		
		//load from the web 
		
		//   if available then cache it
		
		//       return the local resource
		
		//   else return a generic PropertyIcon
		
		return 	getInputStreamForLocalFile( new File( fn ) );	
		
	}
	
	private InputStream getInputStreamForLocalFile(File fn2) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExpired( String localFile ) {
		if ( lifetime < 0 ) return true;
		else {

			// TODO: Use the right creation date of the file.
			long created = 0;
			long age = System.currentTimeMillis() - created;

			if( age < lifetime )
			return false;
			else return true;
		}
	}

}
