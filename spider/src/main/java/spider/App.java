package spider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Utils.HttpUtils;
import Utils.HttpUtils.OnVisitingListener;
import Utils.JSONToExcel;
import Utils.StreamUtils;
import Utils.StreamUtils.OnGetStringListener;

/**
 * spider program
 *
 */
public class App {
    public static int i = 2; // 0 is balance sheet, 1 is income statement, 2 is cash flow
    public static ArrayList<String> codes = new ArrayList<String>(); // stock code
    public static String code = ""; // current stock code
    public static String output = "";

    public static void main(String[] args) throws InterruptedException, IOException {
        JSONToExcel excel = new JSONToExcel();
        String csvFile = "/Users/wenboliu/Library/Mobile Documents/com~apple~CloudDocs/File/spider/codes.txt";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                codes.add(country[1]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        int flag = 0;
        for (String i : codes) {
            if (flag == 0) {
                flag = 1;
                continue;
            }
            code = String.format("%06d", Integer.parseInt(i));
            getWebData();
            excel.toExcel(output, code);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.print(output);
        try {
            excel.write();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void getWebData() {

        HttpUtils httpUtils = HttpUtils.newInstance();
        httpUtils.setOnVisitingListener(new OnVisitingListener() {
            HashMap<String, String> map = new HashMap<String, String>();

            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    InputStream inputStream = conn.getInputStream();
                    String stringOutput = StreamUtils.getString(inputStream);
                    System.out.println(stringOutput);
                    output = stringOutput;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn, HttpUtils httpUtils) {

                StreamUtils.getString(
                        "/Users/wenboliu/Library/Mobile Documents/com~apple~CloudDocs/File/spider/spider/requestheader.txt",
                        new OnGetStringListener() {

                            @Override
                            public void onGeted() {
                            }

                            @Override
                            public void onGetString(String line) {
                                System.out.println(line);
                                String[] strings = line.split(":");
                                map.put(strings[0], strings[1]);
                            }
                        });
                String[] body = { "mergerMark=sysapi1077&paramStr=scode%3D" + code + "%3Brtype%3D4%3Bsign%3D1",
                        "mergerMark=sysapi1075&paramStr=scode%3D" + code + "%3Brtype%3D4%3Bsign%3D1",
                        "mergerMark=sysapi1076&paramStr=scode%3D" + code + "%3Brtype%3D4%3Bsign%3D1",
                        "U2FsdGVkX1+ypueyOSdHWTScsrMOC9j4Jb0EDUztw1jEY4YKZU26mhtKrsRW7Ld3cZDF7p02IMUXxNNV"
                                + "/7Gr/GtTyJXVL5URGLU6TMzQTRJBPWKQl5TM1ibgZsRb78REgII2Rlcr3JCwnOP5O+4mwlm1G8wgiO+Jd"
                                + "3etIW+AKNQ/1nTDlFKfX8ie9igZ3AcsFA83D4aaDF/BE5Z0dqAVRpsWAYKPBjxlaZj4JeHi2On4v0Zqa2E"
                                + "I2Npnv997Xz5c3Wt2KjAOpzm8Ul8BLpwJIA==" };
                if (body[3] == StreamUtils.secretKey) {
                    httpUtils.setRequestProperties(map);
                    httpUtils.setRequestBody(body[i]);
                }
            }

            @Override
            public void onFail(IOException e) {
            }
        }).startConnenction("http://www.cninfo.com.cn/data/project/commonInterface", "POST");
        // the request url
    }

}
