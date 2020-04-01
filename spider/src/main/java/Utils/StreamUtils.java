package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class StreamUtils {
    // secret key to API
    public static String secretKey = "U2FsdGVkX1+ypueyOSdHWTScsrMOC9j4Jb0EDUztw1jEY4YKZU26mhtKrsRW7Ld3cZDF7p02IMUXxNNV/7Gr/GtTyJXVL5URGLU6TMzQTRJBPWKQl5TM1ibgZsRb78REgII2Rlcr3JCwnOP5O+4mwlm1G8wgiO+Jd3etIW+AKNQ/1nTDlFKfX8ie9igZ3AcsFA83D4aaDF/BE5Z0dqAVRpsWAYKPBjxlaZj4JeHi2On4v0Zqa2EI2Npnv997Xz5c3Wt2KjAOpzm8Ul8BLpwJIA==";

    public static String readFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        String result = outputStream.toString();
        inputStream.close();
        outputStream.close();
        return result;
    }

    private static String line;

    public static FileReader createFileReader(File file) throws FileNotFoundException {
        return new FileReader(file);
    }

    public static FileWriter createFileWriter(File file) throws IOException {
        return new FileWriter(file);
    }

    public static InputStreamReader createInputStreamReader(Object obj) {
        if (obj instanceof File)
            if (!((File) obj).exists())
                ((File) obj).getParentFile().mkdirs();
        try {
            return new InputStreamReader(new FileInputStream((File) obj), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OutputStreamWriter createOutputStreamWriter(Object obj) {
        if (obj instanceof File)
            if (!((File) obj).exists())
                ((File) obj).getParentFile().mkdirs();
        try {
            return new OutputStreamWriter(new FileOutputStream((File) obj, true), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader createBufferedReader(Object obj, String cd) throws IOException {
        if (obj instanceof String)
            return new BufferedReader(createInputStreamReader(new File((String) obj)));
        if (obj instanceof InputStream) {
            if (cd == null)
                return new BufferedReader(new InputStreamReader((InputStream) obj));
            else
                return new BufferedReader(new InputStreamReader((InputStream) obj, cd));
        }
        if (obj instanceof File) {
            if (!((File) obj).exists())
                ((File) obj).createNewFile();
            return new BufferedReader(createFileReader((File) obj));
        }
        if (obj instanceof Reader)
            return new BufferedReader((Reader) obj);
        if (obj instanceof BufferedReader)
            return (BufferedReader) obj;
        return null;
    }

    public static BufferedWriter createBufferedWriter(Object obj) throws IOException {
        if (obj instanceof String)
            return new BufferedWriter(createOutputStreamWriter(new File((String) obj)));
        if (obj instanceof OutputStream)
            return new BufferedWriter(new OutputStreamWriter((OutputStream) obj, "utf-8"));
        if (obj instanceof File)
            return new BufferedWriter(createOutputStreamWriter(obj));
        if (obj instanceof Writer)
            return new BufferedWriter((Writer) obj);
        if (obj instanceof BufferedWriter)
            return (BufferedWriter) obj;
        return null;
    }

    public interface OnGetStringListener {
        void onGetString(String line);

        void onGeted();
    }

    public static void getString(Object obj, OnGetStringListener listener) {
        BufferedReader reader;
        try {
            reader = createBufferedReader(obj, null);
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    listener.onGetString(line);
                }
                listener.onGeted();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(Object obj) {
        BufferedReader reader;
        String str = "";
        try {
            reader = createBufferedReader(obj, "utf-8");
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    str += line + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void writeString(Object obj, String str) {
        BufferedWriter write;
        try {
            write = createBufferedWriter(obj);
            if (write != null) {
                write.write(str);
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
