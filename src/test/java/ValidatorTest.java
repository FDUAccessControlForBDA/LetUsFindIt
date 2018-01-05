import com.lufi.regex.BankInfoUtil;
import com.lufi.regex.IdInfoUtil;
import com.lufi.utils.DateValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidatorTest {
    @Test
    public void testDateValidator(){
        assertEquals(true, DateValidator.getInstance().isValid("20171202","yyyymmdd"));
        assertEquals(false, DateValidator.getInstance().isValid("20171232","yyyymmdd"));
        //assertEquals(false, DateValidator.getInstance().isValid("20171502","yyyymmdd"));
        assertEquals(true, DateValidator.getInstance().isValid("2017/12/02","yyyy/mm/dd"));
        assertEquals(true, DateValidator.getInstance().isValid("2017-12-02","yyyy-mm-dd"));
        assertEquals(true, DateValidator.getInstance().isValid("12022017","mmddyyyy"));
        assertEquals(true, DateValidator.getInstance().isValid("02122017","ddmmyyyy"));
    }

    @Test
    public void testIdValidator(){
        assertEquals(true, IdInfoUtil.isValid("362330199605262070"));
    }

    @Test
    public void testBankValidator(){
        assertEquals(true, BankInfoUtil.isValid("6228480039050672177"));
    }
}
