package trial2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class DateUtil {
	private static final DateFormat dateFormat = GenericArtifactHelper.df;
	public static Date parse(String dateString){
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String format(Date date){
		return dateFormat.format(date);
	}
}
