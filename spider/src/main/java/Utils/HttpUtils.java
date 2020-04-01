package Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private HttpURLConnection conn;

    public void setConnection(String fileUrl, String method) throws IOException {
        URL url = new URL(fileUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        listener.onSetDetails(conn, this);
        conn.connect();
    }

    OnVisitingListener listener;

    public interface OnVisitingListener {
        void onSuccess(HttpURLConnection conn);

        void onSetDetails(HttpURLConnection conn, HttpUtils httpUtils);

        void onFail(IOException e);
    }

    public HttpUtils setOnVisitingListener(OnVisitingListener listener) {
        this.listener = listener;
        return this;
    }

    public void startConnenction(String url, String method) {
        try {
            setConnection(url, method);
            if (conn.getResponseCode() == 200) {
                listener.onSuccess(conn);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            listener.onFail(e);
        }
        // if (conn != null) {
        // conn.disconnect();
        // }
    }

    public void setRequestProperties(Map<String, String> map) {
        String key;
        String value;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            key = it.next();
            value = map.get(key);

            conn.setRequestProperty(key, value);
        }
    }

    public void setRequestBody(String body) {
        try {
            conn.setDoOutput(true);
            PrintWriter writer = new PrintWriter(conn.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRequestProperty(String type, String value) {
        conn.setRequestProperty(type, value);
        // secret key to API
        // U2FsdGVkX1+ypueyOSdHWTScsrMOC9j4Jb0EDUztw1jEY4YKZU26mhtKrsRW7Ld3cZDF7p02IMUXxNNV/7Gr/GtTyJXVL5URGLU6TMzQTRJBPWKQl5TM1ibgZsRb78REgII2Rlcr3JCwnOP5O+4mwlm1G8wgiO+Jd3etIW+AKNQ/1nTDlFKfX8ie9igZ3AcsFA83D4aaDF/BE5Z0dqAVRpsWAYKPBjxlaZj4JeHi2On4v0Zqa2EI2Npnv997Xz5c3Wt2KjAOpzm8Ul8BLpwJIA==
    }

    public static HttpUtils newInstance() {
        return new HttpUtils();
    }
}
