/* Licence:
*   Use this however/wherever you like, just don't blame me if it breaks anything.
*
* Credit:
*   If you're nice, you'll leave this bit:
*
*   Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
*   email : plosson@users.sourceforge.net
*/
package be.telio.mediastore.ui.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Original : plosson on 05-janv.-2006 10:46:26 - Last modified  by $Author: plosson $ on $Date: 2006/01/05 10:09:38 $
 * @version 1.0 - Rev. $Revision: 1.1 $
 */
public class MonitoredDiskFileItemFactory extends DiskFileItemFactory
{
    private OutputStreamListener listener = null;

    public MonitoredDiskFileItemFactory(OutputStreamListener listener)
    {
        super();
        this.listener = listener;
    }

    public MonitoredDiskFileItemFactory(int sizeThreshold, File repository, OutputStreamListener listener)
    {
        super(sizeThreshold, repository);
        this.listener = listener;
    }

    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName)
    {
        return new MonitoredDiskFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository(), listener);
    }
}
