package business;

import db.EntryDAO;
import models.Entry;
import models.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Stateless
public class Entries {
    @EJB
    private EntryDAO entryDAO;
    @EJB
    private Auth auth;

    public String addEntry(String base64, String x, String y, String r) {
        User current_user = auth.getUserByToken(base64);
        if (!auth.checkAuth(current_user, base64)) {
            return "Unauthorized";
        }
        try {
            double xd = Double.parseDouble(x);
            double yd = Double.parseDouble(y);
            double rd = Double.parseDouble(r);
            if (xd <= 2 && xd >= -2 && yd <= 3 && yd >= -3 && rd <= 2 && rd >= -2) {
                entryDAO.createEntry(xd, yd, rd, current_user);
            }
        }catch (NumberFormatException e){
            return "Bad format";
        }
        return getEntries(base64);
    }

    public String getEntries(String base64) {
        User current_user = auth.getUserByToken(base64);
        if (!auth.checkAuth(current_user, base64)) {
            return "Unauthorized";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Entry> entries = entryDAO.findEntries(current_user);
        StringJoiner joiner = new StringJoiner(",");
        for (Entry entry : entries) {
            StringBuilder stringBuilder = new StringBuilder();
            Date date = new Date(entry.getTimestamp());
            stringBuilder.append("{\"x\":\"");
            stringBuilder.append(entry.getX().toString());
            stringBuilder.append("\", \"y\":\"");
            stringBuilder.append(entry.getY().toString());
            stringBuilder.append("\", \"r\":\"");
            stringBuilder.append(entry.getR().toString());
            stringBuilder.append("\", \"result\":\"");
            stringBuilder.append(entry.isResult());
            stringBuilder.append("\", \"timestamp\":\"");
            stringBuilder.append(formatter.format(date));
            stringBuilder.append("\"}");
            joiner.add(stringBuilder.toString());
        }

        return "["+joiner.toString()+"]";
    }
}
